package com.murphy.homeauto.model;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
@Slf4j
public class FrontDoorImageFile implements Comparable<FrontDoorImageFile> {

    private static final SimpleDateFormat IMAGE_DATETIME_FORMATER = new SimpleDateFormat("yyyy-MM-dd HHmmss");

    final transient private String date;
    final transient private String imageFileName;
    transient protected long timestamp;

    public FrontDoorImageFile(String date, String imageFileName) {
        this.date = date;
        this.imageFileName = imageFileName;
        String timePart = imageFileName.substring(0, 6);
        String gifDateString = date + " " + timePart;
        try {
            this.timestamp = IMAGE_DATETIME_FORMATER.parse(gifDateString).getTime();
        } catch (ParseException e) {
            log.error("Failed to parse Gif TimeStamp", e);
        }
    }

    public String getLabel() {
        return date;
    }

    public String getLocation() {
        return "/images/frontdoor/webp/" + date.replaceAll("-", "").trim() + "/" + imageFileName;
    }

    @Override
    public int compareTo(FrontDoorImageFile fdGifToCompare) {
        if (fdGifToCompare.timestamp > timestamp) {
            return -1;
        } else if (fdGifToCompare.timestamp < timestamp) {
            return 1;
        }
        return 0;
    }

}

