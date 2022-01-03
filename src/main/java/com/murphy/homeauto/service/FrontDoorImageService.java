package com.murphy.homeauto.service;

import com.murphy.homeauto.command.CommandManager;
import com.murphy.homeauto.model.FrontDoorImage;
import com.murphy.homeauto.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * This service is used to convert front door images into animatable images to be displayed in the front end.
 */
@Slf4j
@Service
public class FrontDoorImageService {

    private static final int LINUX_POOL_SIZE = 2;
    private static final String FTP_LOCATION = "/samba/FTP";
    private static final String IMAGE_BACKUP_LOCATION = "/samba/FrontDoor";
    private static final String FRONT_DOOR_IMAGES_LOCATION = FTP_LOCATION + "/FrontDoor";
    private static final SimpleDateFormat DATE_TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    CommandManager commandManager;
    @Resource
    FrontDoorCameraService frontDoorCameraService;

    public boolean createFrontDoorImage(FrontDoorImage fdi, String destinationFolder) {
        return createWebPAnimatedImage(fdi, destinationFolder);
    }

    private boolean createWebPAnimatedImage(FrontDoorImage fdi, String destinationFolder) {
        log.info("Creating WebP image");
        List<String> imageNames = fdi.getAllImages();
        String imageDirectory = fdi.getRootFolder();
        File outputDirectory = new File(destinationFolder);
        if (!outputDirectory.exists()) {
            boolean result = outputDirectory.mkdir();
            if (!result) {
                log.info("Failed to create destination folder for ImageMagick gif file: " + destinationFolder);
            } else {
                log.info("Created destination folder for ImageMagick gif file: " + destinationFolder);
            }
        }
        String webPName = ImageUtils.getWebPImageName(fdi);
        return commandManager.createAnimatedWebP(imageDirectory, imageNames, webPName, destinationFolder);
    }

    // Make sure this get completed before being called again
    // Returns the number of gifs to be created.
    public synchronized int createAnimatableImages() {
        List<String> dates = frontDoorCameraService.getFrontDoorImageDates();
        List<FrontDoorImage> frontDoorImages;
        int noOfImagesToConvert = 0;
        for (String date : dates) {
            // Take the timestamp snapshot before any long-running operation.
            long currentTimeStamp = System.currentTimeMillis();
            frontDoorImages = frontDoorCameraService.getFrontDoorImagesByDate(date);

            List<FrontDoorImage> imagesNotToConvert = new ArrayList<>();

            // Remove any images that have occurred in the last minute
            long timeToCheck = currentTimeStamp - (60 * 1000);
            for (FrontDoorImage fdi : frontDoorImages) {
                if (fdi.getLastTimestamp() > timeToCheck) {
                    imagesNotToConvert.add(fdi);
                }
            }

            frontDoorImages.removeAll(imagesNotToConvert);

            // Check if this is running on a Windows machine or a raspberry pi
            // Should check the version of raspberry pi , as a pi 3 has 4 cores
            ExecutorService executor = Executors.newFixedThreadPool(LINUX_POOL_SIZE);

            List<Callable<FrontDoorImage>> todo = new ArrayList<>();

            List<FrontDoorImage> imagesToDelete = pruneFrontDoorImages(frontDoorImages, date);
            List<Future<FrontDoorImage>> answers = null;
            for (FrontDoorImage fdi : frontDoorImages) {
                Callable<FrontDoorImage> callable = () -> {
                    boolean result = createFrontDoorImage(fdi, ImageUtils.getWebPWebDirectory(date));
                    if (result) {
                        return fdi;
                    } else {
                        return null;
                    }
                };

                todo.add(callable);
            }

            try {
                answers = executor.invokeAll(todo);
            } catch (InterruptedException e) {
                log.error("Failed to complete creation of gifs", e);
            }


            for (Future<FrontDoorImage> deleteAbleAnswer : Objects.requireNonNull(answers)) {
                try {
                    if (deleteAbleAnswer.get() != null) {
                        imagesToDelete.add(deleteAbleAnswer.get());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Failed to parse Gif Image Creation Result ", e);
                }
            }

            deleteParsedImages(imagesToDelete, currentTimeStamp);
            frontDoorCameraService.clearCachedImages();
            noOfImagesToConvert += frontDoorImages.size();
        }

        log.debug("Parsed: " + noOfImagesToConvert);
        return noOfImagesToConvert;
    }

    /**
     * Removes already created images from the frontDoorImages that are passed.
     *
     * @param frontDoorImages The list of images to check if they have already been created
     * @param date            The date the images where created on
     * @return The list of images that have already been created
     */
    private synchronized List<FrontDoorImage> pruneFrontDoorImages(List<FrontDoorImage> frontDoorImages, String date) {
        List<FrontDoorImage> imagesToRemove = new ArrayList<>();
        for (FrontDoorImage fdi : frontDoorImages) {
            if (doesAnimatableImageExist(fdi, date)) {
                imagesToRemove.add(fdi);
            }
        }
        frontDoorImages.removeAll(imagesToRemove);
        return imagesToRemove;
    }

    /**
     * Check to see if there is an existing Gif image in the Gif Web folder.
     *
     * @param frontDoorImage The FrontDoorImage to check if there exists a gif image
     * @param date           The date that the FrontDoorImage is created on.
     * @return Returns true if there is an animatable image created for the associated FrontDoorImage false otherwise.
     */
    private synchronized boolean doesAnimatableImageExist(FrontDoorImage frontDoorImage, String date) {
        String absoluteGifPath = ImageUtils.getGifWebDirectory(date) + File.separatorChar + ImageUtils.getGifImageName(frontDoorImage);
        File gifFile = new File(absoluteGifPath);
        return gifFile.exists();
    }

    private synchronized void moveParsedImages(List<FrontDoorImage> frontDoorImages, long currentTimestamp){
        log.debug("About to delete images");
        File ftpFolder = new File(FTP_LOCATION);
        File frontDoorImageBackup = new File(IMAGE_BACKUP_LOCATION);
        List<File> deletedFiles = new ArrayList<>();
        // This deletes all folders not in the FrontDoor Images directory
        for (File folder : Objects.requireNonNull(ftpFolder.listFiles())) {
            if (!folder.getAbsolutePath().replaceAll("\\\\", "/").equals(FRONT_DOOR_IMAGES_LOCATION)) {
                Collection<File> filesToDeletes = FileUtils.listFiles(folder, new String[]{"jpg"}, true);
                for (File fileToDelete : filesToDeletes) {
                    try {

                        FileUtils.moveFileToDirectory(fileToDelete, frontDoorImageBackup, true);
                        FileUtils.forceDelete(fileToDelete);
                        deletedFiles.add(fileToDelete);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Delete the empty parent file
                    if (Objects.requireNonNull(fileToDelete.getParentFile().list()).length == 0) {
                        try {
                            FileUtils.forceDelete(fileToDelete.getParentFile());
                            deletedFiles.add(fileToDelete.getParentFile());
                        } catch (IOException e) {
                            log.error("Failed to delete file: " + fileToDelete, e);
                        }
                    }
                }
            }
        }
    }

    // Delete all the frontdoor images that are passed into this method and
    // tidy up other folder with images.
    public synchronized void deleteParsedImages(List<FrontDoorImage> frontDoorImages, long currentTimestamp) {
        log.debug("About to delete images");
        File ftpFolder = new File(FTP_LOCATION);
        List<File> deletedFiles = new ArrayList<>();
        // This deletes all folders not in the FrontDoor Images directory
        for (File folder : Objects.requireNonNull(ftpFolder.listFiles())) {
            if (!folder.getAbsolutePath().replaceAll("\\\\", "/").equals(FRONT_DOOR_IMAGES_LOCATION)) {
                Collection<File> filesToDeletes = FileUtils.listFiles(folder, new String[]{"jpg"}, true);
                for (File fileToDelete : filesToDeletes) {
                    try {
                        FileUtils.forceDelete(fileToDelete);
                        deletedFiles.add(fileToDelete);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Delete the empty parent file
                    if (Objects.requireNonNull(fileToDelete.getParentFile().list()).length == 0) {
                        try {
                            FileUtils.forceDelete(fileToDelete.getParentFile());
                            deletedFiles.add(fileToDelete.getParentFile());
                        } catch (IOException e) {
                            log.error("Failed to delete file: " + fileToDelete, e);
                        }
                    }
                }
            }
        }

        String currentDateFolderName = DATE_TIMESTAMP.format(new Date(currentTimestamp));
        for (FrontDoorImage fdi : frontDoorImages) {
            for (File imageFile : fdi.getAllImageFiles()) {
                imageFile.delete();
                if (Objects.requireNonNull(imageFile.getParentFile().list()).length == 0) {
                    List<File> delFDITreeFiles = deleteFrontDoorImageTree(imageFile, currentDateFolderName);
                    deletedFiles.addAll(delFDITreeFiles);
                }
            }
        }

        log.debug("Successfully Deleted " + deletedFiles.size() + " files");
    }

    // Deletes the image file tree first 3 folders unless it's the current date.
    public synchronized List<File> deleteFrontDoorImageTree(File image, String currentDateFolderName) {
        boolean todaysDateFolder = image.getParentFile().getParentFile().getParentFile().getName()
                .equals(currentDateFolderName);
        List<File> deletedFiles = new ArrayList<>();
        if (!todaysDateFolder) {
            for (int i = 0; i < 3; i++) {
                image = image.getParentFile();
                try {
                    FileUtils.forceDelete(image);
                    deletedFiles.add(image);
                } catch (IOException e) {
                    log.error("Failed to delete image: " + image, e);
                }
            }
        }

        return deletedFiles;
    }
}
