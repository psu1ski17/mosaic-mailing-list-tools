package org.drumm.mosaic.mailinglist.apps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.drumm.mosaic.mailinglist.Addresses;
import org.drumm.mosaic.mailinglist.domain.CcbObject;
import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;
import org.drumm.mosaic.mailinglist.services.CachedGoogleDirectionsService;
import org.drumm.mosaic.mailinglist.services.GoogleDirectionsService;
import org.drumm.mosaic.util.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MappingIterator;

public class CreateUniqueAddressMailingList {
	private static final Logger logger = LoggerFactory.getLogger(CreateUniqueAddressMailingList.class);

	public static String DEFAULT_OUTPUT_FILE = "data/results/UniqueMailingList.csv";

	public static void main(String[] args) throws IOException {

		String cacheFile = "data/google/google-directions.cache";
		String apiKey = GoogleDirectionsService
				.getKeyFromFile("data/source/Google-API-key.txt");
		CachedGoogleDirectionsService service = new CachedGoogleDirectionsService(
				apiKey, cacheFile);

		MappingIterator<CcbObject> itr = CsvUtil.getCsvReaderIterator(
				CsvUtil.DEFAULT_SOURCE_LIST, CcbObject.class);
		Map<String, List<UniqueMailingListDto>> map = new HashMap<String, List<UniqueMailingListDto>>();
		int count = 0;
		while (itr.hasNext()) {
			CcbObject obj = itr.next();
			String addr = obj.toAddressString();
			GoogleDirectionsDto dir = service.getDirections(addr,
					Addresses.MOSAIC_ELKRIDGE_ADDRESS);
			String normalizedAddress = addr;
			if (dir != null) {
				normalizedAddress = dir.findStartAddress();
			}
			if (normalizedAddress == null
					|| normalizedAddress.trim().equals("")) {
				normalizedAddress = addr;
			}
			if (addr.toUpperCase().contains("PO BOX")
					|| addr.toUpperCase().contains("P.O. BOX")) {
				normalizedAddress = addr;
			}
			char addrFirst = addr.trim().charAt(0);
			char normAddrFirst = normalizedAddress.trim().charAt(0);
			if (Character.isDigit(addrFirst)
					&& !Character.isDigit(normAddrFirst)) {
				normalizedAddress = addr;
			}
			String aFirstWord = addr.trim().split(" ")[0];
			String nFirstWord = normalizedAddress.trim().split(" ")[0];
			if (!aFirstWord.equals(nFirstWord)) {
				normalizedAddress = addr;
			}

			UniqueMailingListDto v = new UniqueMailingListDto(obj);
			v.setNormalizedAddress(normalizedAddress);
			v.setLookupStatus(dir.getStatus());

			if (map.containsKey(normalizedAddress)) {
				map.get(normalizedAddress).add(v);
			} else {
				ArrayList<UniqueMailingListDto> list = new ArrayList<UniqueMailingListDto>();
				list.add(v);
				map.put(normalizedAddress, list);
			}
			count++;
			if (count%500 == 0){
				logger.info("processed "+count+" names.");
			}
		}

		List<UniqueMailingListDto> writeList = new ArrayList<UniqueMailingListDto>();
		for (Entry<String, List<UniqueMailingListDto>> entry : map.entrySet()) {
			List<UniqueMailingListDto> objList = entry.getValue();
			ArrayList<String> allNames = new ArrayList<String>();
			for (UniqueMailingListDto item : objList) {
				allNames.add(item.getFirstName() + " " + item.getLastName());
			}
			int numAtAddress = objList.size();
			UniqueMailingListDto v = objList.get(0);
			if (v != null) {
				v.setNumAtAddress(numAtAddress);
				v.setAllNames(allNames);
			}
			writeList.add(v);
		}
		CsvUtil.writeListAsCsv(DEFAULT_OUTPUT_FILE, UniqueMailingListDto.class,
				writeList);
	}

	public static class UniqueMailingListDto extends CcbObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int numAtAddress;
		private String normalizedAddress;
		private String allNames;
		private String lookupStatus;

		public UniqueMailingListDto(CcbObject entry) {
			this.Campus = entry.getCampus();
			this.familyPrimaryContact = entry.getFamilyPrimaryContact();
			this.firstName = entry.getFirstName();
			this.lastName = entry.getLastName();
			this.mailingCity = entry.getMailingCity();
			this.mailingState = entry.getMailingState();
			this.mailingStreet = entry.getMailingStreet();
			this.mailingZip = entry.getMailingZip();
		}

		public int getNumAtAddress() {
			return numAtAddress;
		}

		public void setNumAtAddress(int numAtAddress) {
			this.numAtAddress = numAtAddress;
		}

		public String getNormalizedAddress() {
			return normalizedAddress;
		}

		public void setNormalizedAddress(String normalizedAddress) {
			this.normalizedAddress = normalizedAddress;
		}

		public String getAllNames() {
			return allNames;
		}

		public void setAllNames(String... names) {
			this.allNames = Arrays.toString(names);
		}

		public void setAllNames(Collection<String> names) {
			this.setAllNames(names.toArray(new String[0]));
		}

		public void setAllNames(String allNames) {
			this.allNames = allNames;
		}

		public String getLookupStatus() {
			return lookupStatus;
		}

		public void setLookupStatus(String lookupStatus) {
			this.lookupStatus = lookupStatus;
		}
	}
}
