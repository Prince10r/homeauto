package com.murphy.homeauto.model;

import java.io.File;
import java.util.Date;

public 	class TimestampImage implements Comparable<TimestampImage>{
    File imageFile;
    long timestamp;
    Date date;

    public TimestampImage(File imageFile, long timestamp) {
        this.imageFile = imageFile;
        this.timestamp = timestamp;
        this.date = new Date(timestamp);
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(TimestampImage tImage) {
        if(tImage.timestamp > timestamp) {
            return -1;
        }else if(tImage.timestamp < timestamp) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return imageFile.getName() + " : " + date.toString();
    }
}