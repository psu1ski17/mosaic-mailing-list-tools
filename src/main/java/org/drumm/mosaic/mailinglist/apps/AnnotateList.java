package org.drumm.mosaic.mailinglist.apps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.drumm.mosaic.mailinglist.Addresses;
import org.drumm.mosaic.mailinglist.domain.AnnotatedObject;
import org.drumm.mosaic.mailinglist.domain.CcbObject;
import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;
import org.drumm.mosaic.mailinglist.services.CacheOnlyGoogleDirectionsService;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class AnnotateList {
	public static void main(String[] args) throws IOException {
		CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		CsvSchema readSchema = CsvSchema.emptySchema().withHeader()
				.withColumnSeparator(',');

		File csvFile = new File("data/source/Mailing-List.csv");
		MappingIterator<CcbObject> it = mapper.reader(CcbObject.class)
				.with(readSchema).readValues(csvFile);
		List<AnnotatedObject> list = new ArrayList<AnnotatedObject>();
		List<CcbObject> skippedList = new ArrayList<CcbObject>();
		String filePrefix = "data/results/";
		Writer writer = new FileWriter(filePrefix
				+ "annotated.csv");
		Writer skippedWriter = new FileWriter(filePrefix
				+ "skipped.csv");
		CacheOnlyGoogleDirectionsService dirSrc = new CacheOnlyGoogleDirectionsService();
		int count = 0;
		int skipped = 0;
		CsvSchema writeSchema = mapper.schemaFor(AnnotatedObject.class)
				.withHeader();
		CsvSchema skipSchema = mapper.schemaFor(CcbObject.class)
				.withHeader();
		try {
			while (it.hasNext()) {
				CcbObject entry = it.next();
				String address = entry.getMailingStreet() + " "
						+ entry.getMailingCity() + ", "
						+ entry.getMailingState() + " " + entry.getMailingZip();
				// entries.add(entry);
				AnnotatedObject ao = new AnnotatedObject(entry);
				GoogleDirectionsDto dirsArundel = dirSrc.getDirections(address,
						Addresses.MOSAIC_ARUNDEL_ADDRESS);
				GoogleDirectionsDto dirsElkridge = dirSrc.getDirections(
						address, Addresses.MOSAIC_ELKRIDGE_ADDRESS);
				if (dirsArundel != null && dirsElkridge != null
						&& dirsArundel.getStatus().equals("OK")
						&& dirsElkridge.getStatus().equals("OK")) {
					count++;
					ao.setNormalizedAddress(getStartAddress(dirsArundel));
					ao.setDistanceArundelMeters(getDistance(dirsArundel));
					ao.setDistanceArundelMillis(getDriveTime(dirsArundel));
					ao.setDistanceElkridgeMeters(getDistance(dirsElkridge));
					ao.setDistanceElkridgeMillis(getDriveTime(dirsElkridge));
					list.add(ao);
				} else {
					skippedList.add(entry);
					skipped++;
				}
			}
			String val = mapper.writer(writeSchema).writeValueAsString(
					list);
			writer.write(val);
			 val = mapper.writer(skipSchema).writeValueAsString(
					skippedList);
			skippedWriter.write(val);
			System.out.println("written: "+count);
			System.out.println("skipped: "+skipped);
		} finally {
			
			writer.close();
			skippedWriter.close();
		}
	}

	private static long getDriveTime(GoogleDirectionsDto dirs) {
		return dirs.getRoutes().get(0).getLegs().get(0).getDuration()
				.getValue() * 1000;
	}

	private static int getDistance(GoogleDirectionsDto dirs) {
		return dirs.getRoutes().get(0).getLegs().get(0).getDistance()
				.getValue();
	}

	private static String getStartAddress(GoogleDirectionsDto dirs) {
		return dirs.getRoutes().get(0).getLegs().get(0).getStart_address();
	}
}
