package net.rinc.library.util;

import com.google.gsonx.Gson;
import com.google.gsonx.GsonBuilder;

public class JsonUtil {
	public static final Gson GSON = new GsonBuilder()
	.excludeFieldsWithoutExposeAnnotation()
	.enableComplexMapKeySerialization()
    .create();
}