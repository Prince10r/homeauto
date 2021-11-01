package com.murphy.homeauto.service;

import com.google.common.io.Files;
import com.murphy.homeauto.model.FrontDoorImage;
import com.murphy.homeauto.model.FrontDoorImageFile;
import com.murphy.homeauto.model.TimestampImage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This service is used to return the images that a created and stored in the ftp directory by the front door camera.
 */
@Service
public class FrontDoorCameraService {
    private static final String ABSOLUTE_FRONT_DOOR_IMAGES_WEB_FOLDER = "/samba/www/frontdoor";
    private static final String FTP_LOCATION = "/samba/FTP";
    private static final String FRONT_DOOR_IMAGES_LOCATION = FTP_LOCATION + "/FrontDoor";
    private static final SimpleDateFormat DATE_TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat IMAGE_DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HHmmss");

    private final Map<String, Map<String, FrontDoorImage>> frontDoorImageMap = new HashMap<>();

    public List<FrontDoorImage> getFrontDoorImagesToday() {
        return getFrontDoorImagesByDate(new Date());
    }

    public List<String> getFrontDoorImageDates() {
        List<String> dates = new ArrayList<String>();
        File frontDoorRootDir = new File(FRONT_DOOR_IMAGES_LOCATION);
        File[] files = frontDoorRootDir.listFiles();
        File frontDoorImageTrueRoot = null;
        for (File file : files) {
            if (file.isDirectory()) {
                frontDoorImageTrueRoot = file;
                break;
            }
        }

        files = frontDoorImageTrueRoot.listFiles();
        for (File dateFolder : files) {
            dates.add(dateFolder.getName());
        }

        return dates;
    }

    public List<FrontDoorImage> getFrontDoorImagesYesterday() {
        long oneDay = 60 * 1000 * 60 * 24;
        Date yesterday = new Date(new Date().getTime() - oneDay);
        return getFrontDoorImagesByDate(yesterday);
    }

    public List<FrontDoorImage> getFrontDoorImagesByDate(String date) {
        List<FrontDoorImage> frontDoorImageList = new ArrayList<>();
        synchronized (frontDoorImageMap) {
            if (frontDoorImageMap.get(date) != null || loadFrontDoorImages(date)) {
                frontDoorImageList.addAll(frontDoorImageMap.get(date).values());
            }
        }
        Set<FrontDoorImage> sortedFrontDoorImages = new TreeSet<>(frontDoorImageList);
        frontDoorImageList.clear();
        frontDoorImageList.addAll(sortedFrontDoorImages);
        return frontDoorImageList;
    }

    public boolean loadFrontDoorImages(String date) {
        boolean result = false;
        try {
            List<FrontDoorImage> frontDoorImages = getFrontDoorImagesByDate(DATE_TIMESTAMP.parse(date));
            Map<String, FrontDoorImage> fdiToLabelMap = new HashMap<>();
            synchronized (frontDoorImageMap) {
                frontDoorImageMap.put(date, fdiToLabelMap);
            }
            result = frontDoorImages.size() > 0;
            for (FrontDoorImage fdi : frontDoorImages) {
                fdiToLabelMap.put(fdi.getLabel(), fdi);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<FrontDoorImage> getFrontDoorImagesByDate(Date date) {
        List<FrontDoorImage> images;
        String folderDate = DATE_TIMESTAMP.format(date);
        File dateFolder = findDirectoryWithName(folderDate, new File(FRONT_DOOR_IMAGES_LOCATION));
        images = getFrontDoorImages(dateFolder, folderDate);
        return images;
    }

    public List<FrontDoorImage> getFrontDoorImages(File directory, String date) {
        if (directory == null || !directory.exists()) {
            return new ArrayList<>();
        }
        List<FrontDoorImage> images = new ArrayList<>();
        Iterable<File> imageFiles = Files.fileTraverser().breadthFirst(directory);
        Set<TimestampImage> sortedFiles = new TreeSet<>();
        for (File imageFile : imageFiles) {
            if (imageFile.isFile()) {
                long timestamp = getImageTimestamp(imageFile.getName(), date);
                TimestampImage ti = new TimestampImage(imageFile, timestamp);
                sortedFiles.add(ti);
            }
        }

        FrontDoorImage frontDoorImage = null;
        for (TimestampImage tsImage : sortedFiles) {
            if (frontDoorImage == null || !frontDoorImage.addImage(tsImage)) {
                frontDoorImage = new FrontDoorImage(date);
                frontDoorImage.addImage(tsImage);
                images.add(frontDoorImage);
            }
        }

        return images;
    }

    public List<FrontDoorImageFile> getFrontDoorGifsByDate(String date){
        File dateFolder = findDirectoryWithName(date.replaceAll("-", ""), new File(ABSOLUTE_FRONT_DOOR_IMAGES_WEB_FOLDER + File.separator + "gifs"));
        return getFrontDoorImageFiles(dateFolder, date);
    }

    public List<FrontDoorImageFile> getFrontDoorImageFiles(File directory, String date) {
        if (directory == null || !directory.exists()) {
            return new ArrayList<>();
        }
        Iterable<File> imageFiles = Files.fileTraverser().breadthFirst(directory);
        Set<FrontDoorImageFile> sortedGifs = new TreeSet<>();

        for (File imageFile : imageFiles) {
            if (imageFile.isFile()) {
                sortedGifs.add(new FrontDoorImageFile(date, imageFile.getName()));
            }
        }

        return new ArrayList<>(sortedGifs);
    }

    public List<FrontDoorImageFile> getFrontDoorWebPsByDate(String date){
        File dateFolder = findDirectoryWithName(date.replaceAll("-", ""), new File(ABSOLUTE_FRONT_DOOR_IMAGES_WEB_FOLDER + File.separator + "webp"));
        return getFrontDoorImageFiles(dateFolder, date);
    }



    public static File findDirectoryWithName(String name, File root) {

        for (File file : Objects.requireNonNull(root.listFiles())) {
            if (file.isDirectory()) {
                if (file.getName().equals(name)) {
                    return file;
                }

                File result = findDirectoryWithName(name, file);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    public long getImageTimestamp(String imageName, String date) {
        String timePart = imageName.substring(0, 6);
        String imageDateString = date + " " + timePart;
        Date imageDate = null;
        try {
            imageDate = IMAGE_DATETIME_FORMATTER.parse(imageDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert imageDate != null;
        return imageDate.getTime();
    }

    public void clearCachedImages() {
        synchronized (frontDoorImageMap) {
            frontDoorImageMap.clear();
        }
    }

}
