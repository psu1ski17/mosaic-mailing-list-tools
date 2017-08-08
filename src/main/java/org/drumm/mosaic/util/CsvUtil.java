package org.drumm.mosaic.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvUtil {

	public static final String DEFAULT_SOURCE_LIST = "data/source/Mailing-List.csv";

	public static <K> MappingIterator<K> getCsvReaderIterator(
			String sourceFile, Class<K> klass) throws IOException {
		CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		CsvSchema readSchema = CsvSchema.emptySchema().withHeader()
				.withColumnSeparator(',');

		File csvFile = new File(sourceFile);
		// ObjectReader it = mapper.reader(CcbObject.class);
		MappingIterator<K> it = mapper.readerWithSchemaFor(klass)
				.with(readSchema).readValues(csvFile);
		return it;
	}

	public static <K> void writeListAsCsv(String outputFile, Class<K> klass,
			List<K> list) throws IOException {
		Writer writer = null;
		try {
			CsvMapper mapper = new CsvMapper();
			mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
			writer = new FileWriter(outputFile);
			CsvSchema writeSchema = mapper.schemaFor(klass)
			 .withHeader();
			String val = mapper.writer(writeSchema).writeValueAsString(list);
			writer.write(val);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
