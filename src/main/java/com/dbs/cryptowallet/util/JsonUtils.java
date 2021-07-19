package com.dbs.cryptowallet.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {

	private JsonUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String toJson(Object message) {
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(message);
		} catch (JsonProcessingException e) {
			log.error("Json parse error", e);
		}
		return json;
	}

	public static <T extends Object> T toObject(String message, Class<? extends T> clazz) {
		Object json = null;
		try {
			json = new ObjectMapper().readValue(message, clazz);
		} catch (JsonProcessingException e) {
			log.error("Json parse error", e);
		}
		return clazz.cast(json);
	}

	public static <T extends Object> T toObject(InputStream inputStream, Class<? extends T> clazz) {
		try {
			return toObject(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), clazz);
		} catch (IOException e) {
			log.error("Json parse error", e);
		}
		return null;
	}

	public static <T extends Object> T toObject(Map<String, Object> message, Class<? extends T> clazz) {
		return clazz.cast(new ObjectMapper().convertValue(message, clazz));
	}

}
