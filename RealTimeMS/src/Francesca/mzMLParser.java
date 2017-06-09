package Francesca;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.SequenceCollection;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;
import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.Scan;
import uk.ac.ebi.jmzml.model.mzml.ScanList;
import uk.ac.ebi.jmzml.model.mzml.Spectrum;
import uk.ac.ebi.jmzml.xml.io.MzMLObjectIterator;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;

/*---------- Reading the mzML file ----------*/

public class mzMLParser 
{
	// HashTable to map PepRef to DBseqID (Hashtable stores a bunch of data in no specific order)
	public static Hashtable<String, String> PeptideRefMapDBseqID = new Hashtable<String, String>();
	/*
	 * Hashtable maps keys to values and has two parameters: initial capacity & load factor 
	 * The first parameter specifies the initial size
	 * The second parameter specifics how full the hash table can be before it is resized bigger
	 */

	public static void main(String[] args) 
	{
		// HashTable to map DBsequence ID to DBsequence accession
		Hashtable<String, String> DBseqAccessionMapDBseqID = null;

		// HashTable to map DBsequenceID(aka DBsequence ref) mapped to set peptide refs (multiple of these, so you have to make a set and add to hashtable) 
		Hashtable<String, Set<String>> DBseqIDrefMapPeptideRefs = null;

		// HashTable to map each peptide ref to first cvParam mascot score
		Hashtable<String, Double> peptideRefMapScoreType = new Hashtable<String, Double>();

		// HashTable to map each pepRef to the concatenation so we can see if we have unique peptides for a protein or not
		Hashtable<String, Set<String>> pepRefMapScanPepRef = new Hashtable<String, Set<String>>();

		// HashTable to map each unique peptide scan and ref to its respective xcorr
		Hashtable<String, Double> scanPepRefMapScoreType = new Hashtable<String, Double>();

		// HashTable to map each scan concat peptide ref to second cvParam eltaCn (only with files containing xcorr scores)
		Hashtable<String, Double> scanPepRefMapPepDeltaCn = new Hashtable<String, Double>();

		// HashTable to map each scan concat pep red to the charge state from file
		Hashtable<String, Integer> scanPepRefMapPepChargeState = new Hashtable<String, Integer>();

		// Need to be able to use/access this in multiple functions so make it a global object in the main
		DBseqMap globalDBseqMapObj = null;

		/*
		 * Unmarshalling = process of transforming a representation of an object
		 * that was used for storage or transmission to a repreentation of the
		 * object that is executable "deserializes" XML data into a Java object
		 * (extracts a data structure from a series of bytes) serialized =
		 * marshalling = data structure of object translated to a format that
		 * can be stored (i.e. text file)
		 */

		// Provide the file name to be unmarshalled
		File f = new File("/Users/Nora/Documents/workspace/RealTimeMS/MS_QC_60min_editedV2.mzML");

		//Creates a new unmarshaller of class MzMLUnmarshaller with the file as a parameter
		MzMLUnmarshaller unmarshaller = new MzMLUnmarshaller(f);

		//Iterates through spectra one spectrum at a time
		MzMLObjectIterator<Spectrum> spectrumIterator = unmarshaller
				.unmarshalCollectionFromXpath("src/MS_QC_60min.mzML", Spectrum.class);
		// DELETED --> MzMLObjectIterator<Spectrum> spectrumIterator = unmarshaller.unmarshalCollectionFromXpath("/run/spectrumList/spectrum", Spectrum.class);

		//Iterate through the spectra while there is a next spectrum (hasNext); like saying while line != null
		while (spectrumIterator.hasNext()) 
		{
			//Creates a new object, spectrum, of class Spectrum
			Spectrum spectrum = spectrumIterator.next();
			/*<spectrum> represents the beginning of a spectrum and </spectrum> represents the end*/
			
			//Gets the Id of the spectrum and prints it
				//Line looks like: <spectrum index="1" id="controllerType=0 controllerNumber=1 scan=2" defaultArrayLength="190">
			System.out.println(spectrum.getId());

			/*To get the spectrum name?? ... under the <spectrum>, there's a list of 9 cvParams before "scanList"*/
			//Creates a new List of type CVParam that has an index associated to each cvParam line 
			List<CVParam> cvParams = spectrum.getCvParam();

			//Prints the name (of type String) from the first cvParam line
			System.out.println(cvParams.get(0).getName());

			/*To get the value of the scan start time...ScanList --> Scan --> cvParam --> value*/
			//Under the spectrum heading, there is a type ScanList
			ScanList scan_list = spectrum.getScanList();

			//Under the ScanList, there is a scan of type list
			List<Scan> scan = scan_list.getScan();

			//The lists under scan contain multiple cvParams
			List<CVParam> cv_par = scan.get(0).getCvParam();

			//The first cvParam line contains the scan start time
			String value = cv_par.get(0).getValue();
		}

		//Check if program runs properly
		System.out.println("YAY this works!");
		

		/*------------------PARSING mzid FILE------------------*/

		//Try statement used to catch exceptions that might be thrown when a program executes (prevents crashing). Always followed by "catch" statement that's executed when exception is thrown
		try 
		{
			//URL is a class that points to a "resource" on the web (i.e. file, directory...etc)
			URL file = null; 

			file = mzMLParser.class.getClassLoader().getResource("/RealTimeMS/src/MS_QC_60min.mzid");

			//File cannot be null
			if (file != null) 
			{
				MzIdentMLUnmarshaller unmarshallerMzid = new MzIdentMLUnmarshaller(file);
				DataCollection dc = unmarshallerMzid.unmarshal(DataCollection.class);
				AnalysisData ad = dc.getAnalysisData();

				//Essentially all the data from the XML file falls under this tab heading
				SequenceCollection sc = unmarshallerMzid.unmarshal(SequenceCollection.class);

				List<DBSequence> DBS = sc.getDBSequence();

				DBseqMap mappingsofDBAccessionsDBSeqID = getObjMappingsofDBAccessionsDBSeqID(DBS);
				globalDBseqMapObj = mappingsofDBAccessionsDBSeqID;
				DBseqAccessionMapDBseqID = mappingsofDBAccessionsDBSeqID.getDBseqAccessionMapDBseqID();

				System.out.println(mappingsofDBAccessionsDBSeqID.getDBseqIDMapDBseqAccession().get("DBSeq1400"));
				// Test to see if it works (input key, value will be outputted)
					// System.out.println(DBseqAccessionMapDBseqID.get("LCK_HUMAN"));

				List<PeptideEvidence> pepEv = sc.getPeptideEvidence();
				DBseqIDrefMapPeptideRefs = getMappingDBseqIDrefToPeptideRefs(pepEv);

				// Test to see if it works
					// System.out.println(DBseqIDrefMapPeptideRefs.get("DBSeq_1_JAK1_HUMAN"));

				// Code to do most of the Hashtable mapping from the input file
				List<SpectrumIdentificationList> sil2s = ad.getSpectrumIdentificationList();
				// for(int i = 0; i < sil2s.size(); i++){
				// SpectrumIdentificationList sl = sil2s.get(i);
				// }
				for (SpectrumIdentificationList sl : sil2s) 
				{

					List<SpectrumIdentificationResult> sirs = sl.getSpectrumIdentificationResult();

					for (SpectrumIdentificationResult sir : sirs) 
					{
						// Code to get all of the variable needed to populate the mapped Hashtables
						String peptideSpectrumID = sir.getSpectrumID();
						String[] parsedSpectrumID = peptideSpectrumID.split("=");
						String peptideScanNumber = parsedSpectrumID[3];

						List<SpectrumIdentificationItem> siis = sir.getSpectrumIdentificationItem();
						SpectrumIdentificationItem first_sii = siis.get(0);
						String peptideRef = first_sii.getPeptideRef();
						int chargeState = first_sii.getChargeState();
						List<CvParam> cvparams = first_sii.getCvParam();
						CvParam cvparam0 = cvparams.get(0);
						CvParam cvparam1 = cvparams.get(1);
						Double scoreType = Double.parseDouble(cvparam0.getValue());
						Double deltaCn = Double.parseDouble(cvparam1.getValue());
						String scanPepRef = peptideScanNumber + "_" + peptideRef;

						// Code to actually do the mapping to all the previously instantiated Hashtables
						scanPepRefMapScoreType.put(scanPepRef, scoreType);

						scanPepRefMapPepChargeState.put(scanPepRef, chargeState);

						peptideRefMapScoreType.put(peptideRef, scoreType);

						scanPepRefMapPepDeltaCn.put(scanPepRef, deltaCn);

						if (pepRefMapScanPepRef.containsKey(peptideRef)) 
						{

							Set<String> oldMapScanConcatPepRefSet = pepRefMapScanPepRef.remove(peptideRef);
							Set<String> newMapScanConcatPepRefSet = new HashSet<String>();

							newMapScanConcatPepRefSet.addAll(oldMapScanConcatPepRefSet);
							newMapScanConcatPepRefSet.add(scanPepRef);

							pepRefMapScanPepRef.put(peptideRef, newMapScanConcatPepRefSet);

						}

						else 
						{

							Set<String> keyConcats = new HashSet<String>();
							keyConcats.add(scanPepRef);
							pepRefMapScanPepRef.put(peptideRef, keyConcats);

						}
						/*
						 * if (scoreType.equals("xcorr")) {
						 * 
						 * scanPepRefMapPepDeltaCn.put(scanPepRef, deltaCn);
						 * 
						 * }
						 */
					}
				}

			}

		}

		//Executed if exception is thrown when running the program
		catch (Exception e) 
		{
			//Prints the throwable and its backtrace to the standard error stream
			e.printStackTrace(); 
		}

	}

	//Maps the dBSequence to multiple peptideRefs
	public static Hashtable<String, Set<String>> getMappingDBseqIDrefToPeptideRefs(List<PeptideEvidence> pepEv) 
	{
		Hashtable<String, Set<String>> DBseqIDrefMapPeptideRefs = new Hashtable<String, Set<String>>();

		for (PeptideEvidence pepEvList : pepEv) 
		{
			String DBseqRef = pepEvList.getDBSequenceRef();
			String pepRef = pepEvList.getPeptideRef();

			// ***This does not account for shared peptides since if the key exists, it will be overwritten; account for later!
			PeptideRefMapDBseqID.put(pepRef, DBseqRef); 

			if (DBseqIDrefMapPeptideRefs.containsKey(DBseqRef))    
			{

				//Since values in Hastable are being added as a set (since there's multiple peprefs per ID), as we parse through the XML, if there is another value associated)
				Set<String> oldPepRefsSet = DBseqIDrefMapPeptideRefs.remove(DBseqRef);
				
				//With the same key, we must remove the HashSet from the value section of the HashTable, copy it to a new HashSet, add the new value, then put the HashSet back into the HashTable
				Set<String> newPepRefsSet = new HashSet<String>();
				
				newPepRefsSet.addAll(oldPepRefsSet);
				newPepRefsSet.add(pepRef);

				/*
				 * //OR COULD HAVE DONE THIS INSTEAD OF "addAll" FUNCTION ABOVE!
				 * for (String oldPepRef : oldPepRefsSet) {
				 * newPepRefsSet.add(oldPepRef); }
				 */

				DBseqIDrefMapPeptideRefs.put(DBseqRef, newPepRefsSet);

			}

			else 
			{

				Set<String> pepRefs = new HashSet<String>();
				pepRefs.add(pepRef);
				DBseqIDrefMapPeptideRefs.put(DBseqRef, pepRefs);

			}

		}

		return DBseqIDrefMapPeptideRefs;

	}

	
	public static DBseqMap getObjMappingsofDBAccessionsDBSeqID(List<DBSequence> DBS) 
	{

		DBseqMap DBseqMap = new DBseqMap();

		for (DBSequence DBSList : DBS) 
		{

			String DBseqAccessionList = DBSList.getAccession();
			String DBseqID = DBSList.getId();
			String DBseqAccession;

			if (DBseqAccessionList.contains("|")) 
			{

				String parsedLine[] = DBseqAccessionList.split("\\|");

				if (parsedLine[0].startsWith("DECOY")) 
				{

					DBseqAccession = "REV_" + parsedLine[2];

				}

				else 
				{

					DBseqAccession = parsedLine[2];

				}

			}

			else 
			{

				if (DBseqAccessionList.startsWith("DECOY")) 
				{
					DBseqAccession = "REV_" + DBseqAccessionList;

				}

				else 
				{
					DBseqAccession = DBseqAccessionList;

				}

			}

			DBseqMap.putDBseqAccessionMapDBseqID(DBseqAccession, DBseqID);
			DBseqMap.putDBseqIDMapDBseqAccession(DBseqID, DBseqAccession);

		}

		return DBseqMap;

	}

}
