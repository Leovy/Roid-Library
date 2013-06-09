package com.rincliu.library.util;

import com.google.gsonx.Gson;
import com.google.gsonx.GsonBuilder;

public class RLJsonUtil {
	public static final Gson GSON = new GsonBuilder()
	.excludeFieldsWithoutExposeAnnotation()
	.enableComplexMapKeySerialization()
    .create();
}