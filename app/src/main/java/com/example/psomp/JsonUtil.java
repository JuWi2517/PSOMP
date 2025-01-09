package com.example.psomp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {
    private static final Gson gson = new Gson();

    public static String toJson(List<PubWithItems> pubList) {
        return gson.toJson(pubList);
    }

    public static List<PubWithItems> fromJson(String json) {
        Type pubListType = new TypeToken<List<PubWithItems>>() {}.getType();
        return gson.fromJson(json, pubListType);
    }
}