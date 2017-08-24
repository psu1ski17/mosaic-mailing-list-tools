package org.drumm.mosaic.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.drumm.mosaic.mailinglist.domain.CcbTransaction;
import org.drumm.mosaic.util.CsvUtil;
import org.drumm.mosaic.util.FamilyMapper;

import com.fasterxml.jackson.databind.MappingIterator;

public class TransactionsUtils {
	public static final String SOURCE_DIRECTORY = "data/source/transactions";

	public static void main(String[] args) throws IOException {
		File source = new File(SOURCE_DIRECTORY);
		Set<String> list = getAllCategories(source);

		System.out.println(list);
		sortCategories(list);

		Collection<? extends CcbTransaction> txs = getAllTransactions(source);
		System.out.println(txs.size());

		getFamilyIdsWithOnlyMissionsTripGivingButNoGeneral(txs);

		// oldMethod(txs);

	}
	
	public static Collection<String> getFamilyIdsWithOnlyMissionsTripGivingButNoGeneral(String transactionsDirPath) throws IOException{
		File source = new File(transactionsDirPath);
		Collection<? extends CcbTransaction> txs = getAllTransactions(source);
		return getFamilyIdsWithOnlyMissionsTripGivingButNoGeneral(txs);
	}

	private static Collection<String> getFamilyIdsWithOnlyMissionsTripGivingButNoGeneral(Collection<? extends CcbTransaction> txs) {
		FamilyMapper mapper;
		try {

			Set<String> mSet = new TreeSet<String>();
			Set<String> gSet = new TreeSet<String>();
//			int mNum = 0;
//			int gNum = 0;
//			int notFoundCount = 0;
			mapper = new FamilyMapper();

			Iterator<? extends CcbTransaction> itr = txs.iterator();
			while (itr.hasNext()) {
				CcbTransaction t = itr.next();
				String famId = mapper.getFamilyIdForIndividualId(t.getId());
				if (famId != null) {
					boolean isM = isMissionsTrip(t);
					boolean isG = isGeneralFundOrCapital(t);
					String key = famId;
					if (isM) {
						mSet.add(key);
//						mNum++;
					}
					if (isG) {
						gSet.add(key);
//						gNum++;
					}
					if (!isM && !isG) {
						// oc.add(c);
					}
				} else {
//					notFoundCount++;
					// System.out.println("Fam Id is null for " + t.toString());
				}
			}
			//System.out.println(gNum + " " + gSet.size());
			//System.out.println(mNum + " " + mSet.size());
			TreeSet<String> missionsOnly = new TreeSet<String>();
			for (String s : mSet) {
				if (!gSet.contains(s)) {
					missionsOnly.add(s);
				}
			}
			//System.out.println(missionsOnly.size());
			//for (String m : missionsOnly) {
			//	System.out.println(mapper.getPrimaryContactForFamilyId(m));
			//}
			//System.out.println("missions only: " + missionsOnly.size());
			//System.out.println("notfound: " + notFoundCount);
			return missionsOnly;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private static void oldMethod(Collection<? extends CcbTransaction> txs) {
		Set<String> mSet = new TreeSet<String>();
		Set<String> gSet = new TreeSet<String>();
		int mNum = 0;
		int gNum = 0;
		for (CcbTransaction t : txs) {
			boolean isM = isMissionsTrip(t);
			boolean isG = isGeneralFundOrCapital(t);
			String key = t.getName() + " - " + t.getStreet() + " "
					+ t.getCity() + ", " + t.getState();
			if (isM) {
				mSet.add(key);
				mNum++;
			}
			if (isG) {
				gSet.add(key);
				gNum++;
			}
			if (!isM && !isG) {
				// oc.add(c);
			}
		}
		System.out.println(gNum + " " + gSet.size());
		System.out.println(mNum + " " + mSet.size());
		TreeSet<String> missionsOnly = new TreeSet<String>();
		for (String s : mSet) {
			if (!gSet.contains(s)) {
				missionsOnly.add(s);
			}
		}
		System.out.println(missionsOnly.size());
		for (String m : missionsOnly) {
			System.out.println(m);
		}
		System.out.println(missionsOnly.size());
	}

	private static Collection<? extends CcbTransaction> getAllTransactions(
			File fileOrDirectory) throws IOException {
		if (fileOrDirectory.exists()) {
			List<CcbTransaction> txs = new ArrayList<CcbTransaction>();
			if (fileOrDirectory.isDirectory()) {
				File[] files = fileOrDirectory.listFiles();
				for (File file : files) {
					System.out.println("getting all transactions for "
							+ file.getAbsolutePath());
					txs.addAll(getAllTransactions(file));
				}
			} else {
				MappingIterator<CcbTransaction> itr = CsvUtil
						.getCsvReaderIterator(fileOrDirectory,
								CcbTransaction.class);
				while (itr.hasNext()) {
					txs.add(itr.next());
				}
			}
			return txs;
		}
		return null;

	}

	private static void sortCategories(Set<String> list) {
		Set<String> mc = new TreeSet<String>();
		Set<String> gc = new TreeSet<String>();
		Set<String> oc = new TreeSet<String>();
		for (String c : list) {
			CcbTransaction t = new CcbTransaction();
			t.setCoaCategory(c);
			boolean isM = isMissionsTrip(t);
			boolean isG = isGeneralFundOrCapital(t);
			if (isM) {
				mc.add(c);
			}
			if (isG) {
				gc.add(c);
			}
			if (!isM && !isG) {
				oc.add(c);
			}
		}
		System.out.println("Missions: " + mc.toString());
		System.out.println("General: " + gc.toString());
		System.out.println("Other:" + oc.toString());

	}

	public static Set<String> getAllCategories(File fileOrDirectory)
			throws IOException {
		if (fileOrDirectory.exists()) {
			Set<String> categories = new TreeSet<String>();
			if (fileOrDirectory.isDirectory()) {
				File[] files = fileOrDirectory.listFiles();
				for (File file : files) {
					categories.addAll(getAllCategories(file));
				}
			} else {
				MappingIterator<CcbTransaction> itr = CsvUtil
						.getCsvReaderIterator(fileOrDirectory,
								CcbTransaction.class);
				while (itr.hasNext()) {
					String category = itr.next().getCoaCategory();
					categories.add(category);
				}
			}
			return categories;
		}
		return null;
	}

	public static boolean isMissionsTrip(CcbTransaction trans) {
		return (trans != null && trans.getCoaCategory() != null && trans
				.getCoaCategory().trim().toLowerCase().startsWith("missions"));
	}

	public static boolean isGeneralFundOrCapital(CcbTransaction trans) {
		ArrayList<String> validCategories = new ArrayList<String>();
		validCategories.add("Expansion Fund");
		// validCategories.add("Outside Support");
		validCategories.add("Relocation Fund");
		// validCategories.add("Special Gift");
		validCategories.add("Tithe/General Fund");
		validCategories.add("Upside Down");
		if (trans == null || trans.getCoaCategory() == null) {
			return false;
		}
		String c = trans.getCoaCategory();
		return validCategories.contains(c);
	}
}
