package org.drumm.mosaic.mailinglist.apps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.drumm.mosaic.mailinglist.Addresses;
import org.drumm.mosaic.mailinglist.domain.CcbObject;
import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;
import org.drumm.mosaic.mailinglist.services.CacheOnlyGoogleDirectionsService;
import org.drumm.mosaic.util.CsvUtil;

import com.fasterxml.jackson.databind.MappingIterator;

public class CreateUniqueAddressMailingList {

	public static String DEFAULT_OUTPUT_FILE = "data/results/UniqueMailingList.csv";

	public static void main(String[] args) throws IOException {

		String cacheFile = "data/google/Copy (7) of google-directions.cache";
		CacheOnlyGoogleDirectionsService service = new CacheOnlyGoogleDirectionsService(
				cacheFile);

		MappingIterator<CcbObject> itr = CsvUtil.getCsvReaderIterator(
				CsvUtil.DEFAULT_SOURCE_LIST, CcbObject.class);
		Map<String, List<CcbObject>> map = new HashMap<String, List<CcbObject>>();
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
			if (map.containsKey(normalizedAddress)) {
				map.get(normalizedAddress).add(obj);
			} else {
				ArrayList<CcbObject> list = new ArrayList<CcbObject>();
				list.add(obj);
				map.put(normalizedAddress, list);
			}

		}

		List<UniqueMailingListDto> writeList = new ArrayList<UniqueMailingListDto>();
		for (Entry<String, List<CcbObject>> entry : map.entrySet()) {
			String normalizedAddress = entry.getKey();
			List<CcbObject> objList = entry.getValue();
			CcbObject baseObj = objList.isEmpty() ? null : objList.get(0);
			int numAtAddress = objList.size();
			UniqueMailingListDto v = new UniqueMailingListDto(baseObj);
			v.setNormalizedAddress(normalizedAddress);
			v.setNumAtAddress(numAtAddress);
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
	}
}
