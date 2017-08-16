package org.drumm.mosaic.mailinglist.apps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.drumm.mosaic.mailinglist.Addresses;
import org.drumm.mosaic.mailinglist.domain.AnnotatedObject;
import org.drumm.mosaic.mailinglist.domain.CcbObject;
import org.drumm.mosaic.mailinglist.domain.GoogleDirectionsDto;
import org.drumm.mosaic.mailinglist.services.CachedGoogleDirectionsService;
import org.drumm.mosaic.mailinglist.services.GoogleDirectionsService;
import org.drumm.mosaic.util.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MappingIterator;

public class CreateUniqueAddressMailingList {
	private static final Logger logger = LoggerFactory
			.getLogger(CreateUniqueAddressMailingList.class);

	private static final String DEFAULT_EXPAND_FILE = "data/source/expandList.txt";

	public static final String UNIQUE_ADDRESS_OUTPUT_FILE = "data/results/UniqueMailingList.csv";

	private static final String MAILING_LIST_OUTPUT_FILE = "data/results/OutboundMailingList.csv";

	public static void main(String[] args) throws IOException {
		CachedGoogleDirectionsService service = getService();

		MappingIterator<CcbObject> itr = CsvUtil.getCsvReaderIterator(
				CsvUtil.DEFAULT_SOURCE_LIST, CcbObject.class);
		Map<String, List<UniqueMailingListDto>> map = new HashMap<String, List<UniqueMailingListDto>>();
		int count = 0;
		while (itr.hasNext()) {
			CcbObject obj = itr.next();
			String addr = obj.toAddressString();
			GoogleDirectionsDto dirsArundel = service.getDirections(addr,
					Addresses.MOSAIC_ARUNDEL_ADDRESS);
			GoogleDirectionsDto dirsElkridge = service.getDirections(addr,
					Addresses.MOSAIC_ELKRIDGE_ADDRESS);
			AnnotatedObject ao = getAnnotatedObj(obj, addr, dirsArundel,
					dirsElkridge);
			String normalizedAddress = addr;
			if (dirsElkridge != null) {
				normalizedAddress = dirsElkridge.findStartAddress();
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

			UniqueMailingListDto v = new UniqueMailingListDto(ao);
			v.setNormalizedAddress(normalizedAddress);
			v.setLookupStatus(dirsElkridge.getStatus());

			if (map.containsKey(normalizedAddress)) {
				map.get(normalizedAddress).add(v);
			} else {
				ArrayList<UniqueMailingListDto> list = new ArrayList<UniqueMailingListDto>();
				list.add(v);
				map.put(normalizedAddress, list);
			}
			count++;
			if (count % 500 == 0) {
				logger.info("processed " + count + " names.");
			}
		}

		writeFamilyMailingList(map);
		writeUniqueAddresses(map);

		Set<String> badAddr = service.getBadAddresses();
		logger.info("Bad Addresses:");
		for (String addr : badAddr) {
			logger.info("  " + addr);
		}
	}

	private static void writeUniqueAddresses(
			Map<String, List<UniqueMailingListDto>> map) throws IOException {
		Set<String> expandAddressList = populateExpandAddressList(DEFAULT_EXPAND_FILE);
		List<UniqueMailingListDto> writeList = new ArrayList<UniqueMailingListDto>();
		for (Entry<String, List<UniqueMailingListDto>> entry : map.entrySet()) {
			String normalized = entry.getKey();
			List<UniqueMailingListDto> objList = entry.getValue();
			ArrayList<String> allNames = new ArrayList<String>();
			for (UniqueMailingListDto item : objList) {
				allNames.add(item.getFirstName() + " " + item.getLastName());
			}
			int numAtAddress = objList.size();
			UniqueMailingListDto v = objList.get(0);
			if (v != null) {
				if (expandAddressList.contains(normalized.trim())) {
					for (UniqueMailingListDto item : objList) {
						item.setNumAtAddress(1);
						item.setAllNames(item.getFirstName() + " "
								+ item.getLastName());
						writeList.add(v);
					}
				} else {
					v.setNumAtAddress(numAtAddress);
					v.setAllNames(allNames);
					writeList.add(v);
				}
			}
		}
		CsvUtil.writeListAsCsv(UNIQUE_ADDRESS_OUTPUT_FILE,
				UniqueMailingListDto.class, writeList);
	}

	private static void writeFamilyMailingList(
			Map<String, List<UniqueMailingListDto>> map) throws IOException {
		List<UniqueMailingListDto> writeList = new ArrayList<UniqueMailingListDto>();
		for (Entry<String, List<UniqueMailingListDto>> entry : map.entrySet()) {
			List<UniqueMailingListDto> objList = entry.getValue();
			ArrayList<String> allNames = new ArrayList<String>();
			for (UniqueMailingListDto item : objList) {
				allNames.add(item.getFirstName() + " " + item.getLastName());
			}
			HashMap<String, UniqueMailingListDto> lastNameMap = new HashMap<String, UniqueMailingListDto>();
			for (UniqueMailingListDto item : objList) {
				UniqueMailingListDto u = lastNameMap.get(item.getLastName());
				if (u == null) {
					u = item;
					u.setNumAtAddress(1);
					u.setAllNames(u.getFirstName() + " " + u.getLastName());
					lastNameMap.put(u.getLastName(), u);
				} else {
					u.setNumAtAddress(u.getNumAtAddress() + 1);
					u.setAllNames(u.getAllNames() + ", " + u.getFirstName()
							+ " " + u.getLastName());
				}
			}
			for (Entry<String, UniqueMailingListDto> e : lastNameMap.entrySet()) {
				writeList.add(e.getValue());
			}
		}
		CsvUtil.writeListAsCsv(MAILING_LIST_OUTPUT_FILE,
				UniqueMailingListDto.class, writeList);
	}

	private static CachedGoogleDirectionsService getService()
			throws IOException {
		String cacheFile = "data/google/google-directions.cache";
		String apiKey = GoogleDirectionsService
				.getKeyFromFile("data/source/Google-API-key.txt");
		CachedGoogleDirectionsService service = new CachedGoogleDirectionsService(
				apiKey, cacheFile);
		return service;
	}

	private static AnnotatedObject getAnnotatedObj(CcbObject obj, String addr,
			GoogleDirectionsDto dirsArundel, GoogleDirectionsDto dirsElkridge)
			throws IOException {
		AnnotatedObject ao = new AnnotatedObject(obj);
		if (dirsArundel != null && dirsElkridge != null
				&& dirsArundel.getStatus().equals("OK")
				&& dirsElkridge.getStatus().equals("OK")) {
			ao.setGoogleAddress(getStartAddress(dirsArundel));
			ao.setDistanceArundelMeters(getDistance(dirsArundel));
			ao.setDistanceArundelMillis(getDriveTime(dirsArundel));
			ao.setDistanceElkridgeMeters(getDistance(dirsElkridge));
			ao.setDistanceElkridgeMillis(getDriveTime(dirsElkridge));
		} else {
			// ao.setGoogleAddress(getStartAddress(dirsArundel));
			if (dirsElkridge.getStatus().equals("ZERO_RESULTS")) {
				//Driving is impossible
				ao.setDistanceArundelMeters(7000000);
				ao.setDistanceArundelMillis(300000000);
				ao.setDistanceElkridgeMeters(7000000);
				ao.setDistanceElkridgeMillis(300000000);
			} else {
				ao.setDistanceArundelMeters(-1);
				ao.setDistanceArundelMillis(-1);
				ao.setDistanceElkridgeMeters(-1);
				ao.setDistanceElkridgeMillis(-1);
			}
		}
		return ao;
	}

	private static Set<String> populateExpandAddressList(String expandList) {
		HashSet<String> list = new HashSet<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(expandList));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) {
					continue;
				} else {
					list.add(line);
				}
			}
			return list;
		} catch (IOException e) {
			logger.error("Unable to read Expand Address List");
			return list;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("Error closing reader for expand address list");
				}
			}
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

	public static class UniqueMailingListDto extends AnnotatedObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int numAtAddress;
		private String normalizedAddress;
		private String allNames;
		private String lookupStatus;

		public UniqueMailingListDto(AnnotatedObject entry) {
			super(entry);
			// this.Campus = entry.getCampus();
			// this.familyPrimaryContact = entry.getFamilyPrimaryContact();
			// this.firstName = entry.getFirstName();
			// this.lastName = entry.getLastName();
			// this.mailingCity = entry.getMailingCity();
			// this.mailingState = entry.getMailingState();
			// this.mailingStreet = entry.getMailingStreet();
			// this.mailingZip = entry.getMailingZip();
			this.distanceElkridgeMeters = entry.getDistanceElkridgeMeters();
			this.distanceArundelMeters = entry.getDistanceArundelMeters();
			this.distanceElkridgeMillis = entry.getDistanceElkridgeMillis();
			this.distanceArundelMillis = entry.getDistanceArundelMillis();
			this.googleAddress = entry.getGoogleAddress();
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
