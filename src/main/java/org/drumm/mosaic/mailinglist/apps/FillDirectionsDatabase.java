package org.drumm.mosaic.mailinglist.apps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.drumm.mosaic.mailinglist.Addresses;
import org.drumm.mosaic.mailinglist.domain.CcbObject;
import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;
import org.drumm.mosaic.mailinglist.services.CachedGoogleDirectionsService;
import org.drumm.mosaic.mailinglist.services.GoogleDirectionsService;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class FillDirectionsDatabase {

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

		itr = entries.iterator();
		int count = 0;
		while (itr.hasNext()) {
			CcbObject entry = itr.next();
			String address = entry.getMailingStreet() + " "
					+ entry.getMailingCity() + ", " + entry.getMailingState()
					+ " " + entry.getMailingZip();
			GoogleDirectionsDto dirs = dirSrc.getDirections(address,
					Addresses.MOSAIC_ARUNDEL_ADDRESS);
			if (!dirs.getStatus().equals("OK")) {
				System.out.println(count + ": " + dirs);
			}
			dirs = dirSrc.getDirections(address,
					Addresses.MOSAIC_ELKRIDGE_ADDRESS);
			if (!dirs.getStatus().equals("OK")) {
				System.out.println(count + ": " + dirs);
			}
			count++;
			if (dirs.getStatus().equals("OVER_QUERY_LIMIT")) {
				break;
			}
		}

	}

}
