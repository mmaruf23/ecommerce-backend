package com.solo.ecommerce.util;

public class SlugConverter {
    public static String convertToSlug(String title){
        return title.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    public static String convertToTitle(String slug){
        return slug.replaceAll("-", " ");
    }
}
