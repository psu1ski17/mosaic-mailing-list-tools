package org.drumm.mosaic.mailinglist.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleDirectionsService {

	private static final Object OVER_QUOTA_STATUS = null;
	private String apiKey;
	private ObjectMapper jsonMapper;

	public GoogleDirectionsService(String apiKey) {
		this.apiKey = apiKey;
		this.jsonMapper = new ObjectMapper();
	}

	private URL generateUrl(String origin, String dest) {
		URI uri;
		try {
			origin = URLEncoder.encode(origin, "UTF-8");
			dest = URLEncoder.encode(dest, "UTF-8");
			uri = new URI(
					"https://maps.googleapis.com/maps/api/directions/json?origin="
							+ origin + "&destination=" + dest + "&key="
							+ apiKey);
			System.out.println(uri.toString());
			return uri.toURL();
		} catch (URISyntaxException e) {
			throw new RuntimeException("error creating url", e);
		} catch (MalformedURLException e) {
			throw new RuntimeException("error creating url", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("error creating url", e);
		}

	}

	public JsonNode getDirectionsToJson(String origin, String dest)
			throws IOException {
		URL url = generateUrl(origin, dest);
		InputStream stream = url.openStream();
		return jsonMapper.readTree(stream);
	}
	
	public GoogleDirectionsDto getDirections(String origin, String dest)
			throws IOException {
		URL url = generateUrl(origin, dest);
		InputStream stream = url.openStream();
		GoogleDirectionsDto val = jsonMapper.readValue(stream, GoogleDirectionsDto.class);
		if (val.getStatus().equals(OVER_QUOTA_STATUS)){
			return val;
		}
		return val;
	}

	public static String getKeyFromResource(String resourcePath)
			throws IOException {
		InputStream stream = GoogleDirectionsService.class.getClassLoader()
				.getResourceAsStream(resourcePath);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		String key = reader.readLine();
		reader.close();
		return key;
	}

	public static void main(String[] args) throws IOException {
		String key = GoogleDirectionsService
				.getKeyFromFile("./data/source/Google-API-key.txt");
		GoogleDirectionsService service = new GoogleDirectionsService(key);
		JsonNode dir = service.getDirectionsToJson("PA", "CA");
		System.out.println(dir);
	}

	public static String getKeyFromFile(String filePath) throws IOException {
		FileInputStream stream = new FileInputStream(filePath);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		String key = reader.readLine();
		reader.close();
		return key;
	}
}
