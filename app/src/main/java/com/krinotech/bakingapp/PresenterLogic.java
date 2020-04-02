package com.krinotech.bakingapp;

public class PresenterLogic {
    private static int PORTRAIT_ONE = 0;
    private static int PORTRAIT_TWO = 180;
    private static int LANDSCAPE_ONE = 90;
    private static int LANDSCAPE_TWO = 270;


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
