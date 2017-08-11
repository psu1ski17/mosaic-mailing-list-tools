package org.drumm.mosaic.mailinglist.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public class CachedGoogleDirectionsService {
	private static final Logger logger = LoggerFactory
			.getLogger(CachedGoogleDirectionsService.class);
	private static final String DEFAULT_FILE = "data/google/google-directions.cache";
	private GoogleDirectionsService service;
	private Map<String, GoogleDirectionsDto> cache;
	private String cacheFile;

	public CachedGoogleDirectionsService(String apiKey) {
		this(new GoogleDirectionsService(apiKey), DEFAULT_FILE);
	}

	public CachedGoogleDirectionsService(String apiKey, String cacheFile) {
		this(new GoogleDirectionsService(apiKey), cacheFile);
	}

	public CachedGoogleDirectionsService(GoogleDirectionsService service,
			String cacheFile) {
		this.service = service;
		this.cacheFile = cacheFile;
		try {
			readCache(cacheFile);
			for (GoogleDirectionsDto v : cache.values()) {
				if (v.getError_message() == null
						|| v.getError_message().equals("")) {

				} else {
					logger.error("bad data");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JsonNode getDirectionsToJson(String origin, String dest)
			throws IOException {
		throw new UnsupportedOperationException("Not supported yet");
	}

	public GoogleDirectionsDto getDirections(String origin, String dest)
			throws IOException {
		String key = getCacheKey(origin, dest);
		if (cache.containsKey(key)) {
			logger.trace("cache hit for origin='" + origin + "', dest='"
					+ dest + "'");
			return cache.get(key);
		} else {
			GoogleDirectionsDto dir = this.service.getDirections(origin, dest);
			logger.trace("cache miss for origin='" + origin + "', dest='"
					+ dest + "'");
			if (dir.getStatus().equals("OK")
					|| dir.getStatus().equals("ZERO_RESULTS")) {
				cache.put(key, dir);
				writeCache(cacheFile);
			} else {
				logger.trace("status not ok");
				logger.trace(dir.getStatus());
			}
			return dir;
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

	private void writeCache(String filename) throws IOException {
		File file = new File(filename);
		File newFile = null;
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
			return;
		} else {
			newFile = new File(filename + "2");
			if (newFile.exists()) {
				newFile.delete();
			}
			file.renameTo(newFile);
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(filename))) {
			oos.writeObject(cache);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (newFile != null && newFile.exists()) {
				newFile.delete();
			}
		}
	}
}
