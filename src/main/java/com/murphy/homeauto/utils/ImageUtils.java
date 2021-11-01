package com.murphy.homeauto.utils;

import com.murphy.homeauto.model.FrontDoorImage;

import java.io.File;
import java.util.List;

public class ImageUtils {

    private static final String ABSOLUTE_FRONTDOOR_IMAGES_WEBFOLDER = "/samba/www/frontdoor";

    public static String getWebPImageName(FrontDoorImage fdi) {
        return getImageName(fdi, "webp");
    }

    public static String getImageName(FrontDoorImage fdi, String fileType) {
        List<String> imageNames = fdi.getAllImages();
        String imageName = imageNames.get(imageNames.size() - 1).substring(0, 6) + "." + fileType;
        return imageName;
    }

    public static String getWebpWebDirectory(String date) {
        return ABSOLUTE_FRONTDOOR_IMAGES_WEBFOLDER + File.separatorChar + date.replaceAll("-", "");
    }

    public static String getGifImageName(FrontDoorImage fdi) {
        return getImageName(fdi, "gif");
    }

    public static String getGifWebDirectory(String date) {
        return ABSOLUTE_FRONTDOOR_IMAGES_WEBFOLDER + File.separatorChar + "gifs" + File.separatorChar + date.replaceAll("-", "");
    }

    public static String getWebPWebDirectory(String date) {
        return ABSOLUTE_FRONTDOOR_IMAGES_WEBFOLDER + File.separatorChar + "webp" + File.separatorChar + date.replaceAll("-", "");
    }

}

