package com.murphy.homeauto.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FrontDoorImage implements Comparable<FrontDoorImage> {

    private static final long GIF_MAX_TIMESPREAD = 60 * 1000;
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");
    private final List<String> allImages = new ArrayList<>();


    transient protected List<TimestampImage> timeStampImages = new ArrayList<>();
    final transient private String date;

    public FrontDoorImage(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getRootFolder() {
        return timeStampImages.get(0).getImageFile().getParentFile().getAbsolutePath();
    }

    public long getLastTimestamp() {
        long greatestTimestamp = 0;
        for (TimestampImage tsImg : timeStampImages) {
            if (greatestTimestamp < tsImg.getTimestamp()) {
                greatestTimestamp = tsImg.getTimestamp();
            }
        }
        return greatestTimestamp;
    }

    public String getMainImage() {
        return allImages.get(0);
    }

    public List<File> getAllImageFiles() {
        List<File> allFiles = new ArrayList<>();

        for (TimestampImage tsImg : timeStampImages) {
            allFiles.add(tsImg.getImageFile());
        }

        return allFiles;
    }

    public List<String> getAllImages() {
        return allImages;
    }

    public String getLabel() {
        return TIME_FORMATTER.format(timeStampImages.get(0).getDate());
    }

    public boolean addImage(TimestampImage tsImage) {
        boolean result = false;
        if (allImages.size() == 0 || isImageWithinSpread(tsImage)) {
            result = addTimestampImage(tsImage);
        }
        return result;
    }

    private boolean addTimestampImage(TimestampImage tsImage) {
        timeStampImages.add(tsImage);
        allImages.add(tsImage.getImageFile().getName());
        return true;
    }

    public boolean isImageWithinSpread(TimestampImage tsImage) {
        boolean result = false;
        for (TimestampImage lclTsImage : timeStampImages) {
            long lclTsImageTime = lclTsImage.getTimestamp();
            long tsImageTime = tsImage.getTimestamp();
            if (Math.abs((int) (lclTsImageTime - tsImageTime)) < GIF_MAX_TIMESPREAD) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "FrontDoorImage [mainImage=" + getMainImage() + ", label)=" + getLabel() + "]";
    }

    @Override
    public int compareTo(FrontDoorImage fdiToCompare) {
        long timestamp = timeStampImages.get(0).getTimestamp();
        long timestampToCompare = fdiToCompare.timeStampImages.get(0).getTimestamp();
        if (timestampToCompare > timestamp) {
            return -1;
        } else if (timestampToCompare < timestamp) {
            return 1;
        }
        return 0;
    }

}

