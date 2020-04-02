package com.krinotech.bakingapp;

public class PresenterLogic {

    public static boolean prevExists(int position) {
        return position - 1 >= 0;
    }

    public static boolean nextExists(int position, int size) {
        return position + 1 < size;
    }

    public static boolean videoExists(String url, String thumbnailUrl) {
        return url != null && !url.isEmpty()
                || thumbnailUrl != null && !thumbnailUrl.isEmpty();
    }

    public static String getUri(String url, String thumbnailUrl) {
        String chosenUrl;
        if (url.isEmpty()){
            chosenUrl = thumbnailUrl;
        }
        else {
            chosenUrl = url;
        }
        return chosenUrl;
    }
}
