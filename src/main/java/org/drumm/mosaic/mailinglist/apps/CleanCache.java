package org.drumm.mosaic.mailinglist.apps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanCache {
	private static final Logger logger = LoggerFactory
			.getLogger(CleanCache.class);
	private static final String DEFAULT_FILE = "data/google/google-directions.cache";
	private static Map<String, GoogleDirectionsDto> cache;

	public static void main(String[] args) throws IOException {
		readCache(DEFAULT_FILE);
		Set<String> statuses = new HashSet<String>();
		Map<String, GoogleDirectionsDto> newCache = new HashMap<String, GoogleDirectionsDto>();
		for (Entry<String, GoogleDirectionsDto> entry : cache.entrySet()) {
			GoogleDirectionsDto v = entry.getValue();
			statuses.add(v.getStatus());
			if (!v.getStatus().equals("NOT_FOUND")) {
				newCache.put(entry.getKey(), v);
			}
		}
		logger.info("Status: " + statuses.toString());
		statuses.clear();
		for (Entry<String, GoogleDirectionsDto> entry : newCache.entrySet()) {
			GoogleDirectionsDto v = entry.getValue();
			statuses.add(v.getStatus());
		}
		logger.info("Status: " + statuses.toString());
		cache = newCache;
		writeCache(DEFAULT_FILE);
	}

	@SuppressWarnings("unchecked")
	private static void readCache(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			cache = new HashMap<String, GoogleDirectionsDto>();
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				filename))) {

			cache = (Map<String, GoogleDirectionsDto>) ois.readObject();
			logger.info("loaded cache with " + cache.size() + " entries.");
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
	}

	private static void writeCache(String filename) throws IOException {
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
			logger.info("Wrote cache to file with " + cache.size()
					+ " entries.");
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
