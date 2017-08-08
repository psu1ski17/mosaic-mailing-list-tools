package org.drumm.mosaic.mailinglist.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;

import com.fasterxml.jackson.databind.JsonNode;

public class CacheOnlyGoogleDirectionsService {
	public static final String DEFAULT_FILE = "data/google/google-directions.cache";
	private Map<String, GoogleDirectionsDto> cache;

	public CacheOnlyGoogleDirectionsService() throws IOException {
		this(DEFAULT_FILE);
	}
	
	public CacheOnlyGoogleDirectionsService(String cacheFile) throws IOException {
		readCache(cacheFile);
	}

	public JsonNode getDirectionsToJson(String origin, String dest)
			throws IOException {
		throw new UnsupportedOperationException("Not supported yet");
	}

	public GoogleDirectionsDto getDirections(String origin, String dest)
			throws IOException {
		String key = getCacheKey(origin, dest);
		if (cache.containsKey(key)) {
			// System.out.println("cache hit");
			return cache.get(key);
		} else {
			return null;
		}
	}

	private String getCacheKey(String origin, String dest) {
		return origin + "^&" + dest;
	}

	@SuppressWarnings("unchecked")
	private void readCache(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			cache = new HashMap<String, GoogleDirectionsDto>();
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				filename))) {

			cache = (Map<String, GoogleDirectionsDto>) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
	}
}
