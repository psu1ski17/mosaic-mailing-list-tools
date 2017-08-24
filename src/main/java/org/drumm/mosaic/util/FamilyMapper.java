package org.drumm.mosaic.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.drumm.mosaic.mailinglist.domain.IdTableDto;

import com.fasterxml.jackson.databind.MappingIterator;

public class FamilyMapper {
	private static final String DEFAULT_ID_MAP_FILE = "data/source/ID-Table.csv";
	private TreeMap<String, IdTableDto> individualIdMap;
	private TreeMap<String, List<IdTableDto>> familyIdMap;

	public FamilyMapper() throws IOException {
		this(DEFAULT_ID_MAP_FILE);
	}

	public FamilyMapper(String idMapFile) throws IOException {
		familyIdMap = new TreeMap<String, List<IdTableDto>>();
		individualIdMap = new TreeMap<String, IdTableDto>();
		MappingIterator<IdTableDto> itr = CsvUtil.getCsvReaderIterator(
				idMapFile, IdTableDto.class);
		while (itr.hasNext()) {
			IdTableDto dto = itr.next();
			individualIdMap.put(dto.getIndividualId(), dto);
			List<IdTableDto> list = familyIdMap.get(dto.getFamilyId());
			if (list == null) {
				list = new ArrayList<IdTableDto>();
				familyIdMap.put(dto.getFamilyId(), list);
			}
			list.add(dto);
		}
	}

	public String getFamilyIdForIndividualId(String individualId) {
		IdTableDto dto = individualIdMap.get(individualId);
		if (dto == null) {
			return null;
		}
		return dto.getFamilyId();
	}
	
	public IdTableDto getPrimaryContactForFamilyId(String familyId) {
		List<IdTableDto> dtos = familyIdMap.get(familyId);
		for (IdTableDto dto:dtos){
			if (dto.getFamilyPosition().equals("Primary Contact")){
				return dto;
			}
		}
		return null;
	}

}
