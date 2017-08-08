package org.drumm.mosaic.mailinglist.apps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.drumm.mosaic.mailinglist.domain.CcbObject;
import org.drumm.mosaic.mailinglist.services.CachedGoogleDirectionsService;
import org.drumm.mosaic.mailinglist.services.GoogleDirectionsService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class FindDuplicates {

	private static final String MOSAIC_ELKRIDGE_ADDRESS = "Mosaic Christian Church 21043";
	private static final String MOSAIC_ARUNDEL_ADDRESS = "1001 Annapolis Rd. Gambrills, MD 21054";

	public static void main(String[] args) throws IOException {
		CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		CsvSchema schema = CsvSchema.emptySchema().withHeader()
				.withColumnSeparator(',');
		File csvFile = new File("data/source/Mailing-List.csv");
		MappingIterator<CcbObject> it = mapper.reader(CcbObject.class)
				.with(schema).readValues(csvFile);
		List<CcbObject> entries = new ArrayList<CcbObject>();
		String apiKey = GoogleDirectionsService
				.getKeyFromFile("data/source/Google-API-key.txt");
		CachedGoogleDirectionsService dirSrc = new CachedGoogleDirectionsService(
				apiKey);

		while (it.hasNext()) {
			CcbObject entry = it.next();
			entries.add(entry);
		}

		Iterator<CcbObject> itr = entries.iterator();
		KeyGrabber lastNameKeyGrabber = new KeyGrabber() {

			public String getKey(CcbObject obj) {
				return obj.getLastName();
			}
		};

		KeyGrabber streetKeyGrabber = new KeyGrabber() {

			public String getKey(CcbObject obj) {
				return obj.getMailingStreet();
			}
		};

		KeyGrabber lastNameAddressKeyGrabber = new KeyGrabber() {

			public String getKey(CcbObject obj) {
				return obj.getLastName() + "-" + obj.getMailingStreet();
			}
		};

		String filePrefix = "data/results/";
		getDuplicates(entries, lastNameKeyGrabber, new FileWriter(filePrefix
				+ "duplicateLastNames.log"));
		getDuplicates(entries, lastNameAddressKeyGrabber, new FileWriter(
				filePrefix + "duplicateLastNamesAddress.log"));
		getDuplicatesCsv(entries, lastNameAddressKeyGrabber, new FileWriter(
				filePrefix + "duplicateLastNamesAddress.csv"));
		getDuplicates(entries, streetKeyGrabber, new FileWriter(filePrefix
				+ "duplicateAddresses.log"));
	}

	private static void getDuplicatesCsv(List<CcbObject> entries,
			KeyGrabber kg, FileWriter writer) {
		Map<String, List<CcbObject>> map = new TreeMap<String, List<CcbObject>>();
		Iterator<CcbObject> itr = entries.iterator();
		while (itr.hasNext()) {
			CcbObject entry = itr.next();
			String key = kg.getKey(entry);
			List<CcbObject> value = map.get(key);
			if (value == null) {
				value = new ArrayList<CcbObject>();
				map.put(key, value);
			}
			value.add(entry);
		}
		int count = 0;
		try {

			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.schemaFor(CcbObject.class).withHeader();
			ArrayList<CcbObject> dupList = new ArrayList<CcbObject>();
			for (List<CcbObject> list : map.values()) {
				if (list.size() > 1) {
					for (CcbObject obj : list) {
						dupList.add(obj);
					}
					count += list.size() - 1;
				}
			}
			String val = mapper.writer(schema).writeValueAsString(dupList);
			writer.write(val);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Count: " + count);
	}

	private static void getDuplicates(List<CcbObject> entries, KeyGrabber kg,
			Writer writer) throws IOException {
		Map<String, List<CcbObject>> map = new TreeMap<String, List<CcbObject>>();
		Iterator<CcbObject> itr = entries.iterator();
		while (itr.hasNext()) {
			CcbObject entry = itr.next();
			String key = kg.getKey(entry);
			List<CcbObject> value = map.get(key);
			if (value == null) {
				value = new ArrayList<CcbObject>();
				map.put(key, value);
			}
			value.add(entry);
		}

		int count = 0;
		try {
			if (writer != null) {
				writer.write("Printing Duplicates:\n");
			}
			for (List<CcbObject> list : map.values()) {
				if (list.size() > 1) {
					if (writer != null) {
						writer.write("  Duplicate: " + kg.getKey(list.get(0))
								+ "\n");
						for (CcbObject obj : list) {
							writer.write(obj + "\n");
						}
					}
					count += list.size() - 1;
				}
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		System.out.println("Count: " + count);
	}

	public interface KeyGrabber {
		public String getKey(CcbObject obj);
	}

}
