package com.egand.fabricktest.utils;

import com.google.gson.Gson;

public class TestUtils {

    public static String toJson(Object in) {
        Gson gson = new Gson();
        return gson.toJson(in);
    }
}
