import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidenceRef;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;

//Class that contains Hashtables to map values from low level scan number to high level peptide sequence and accession
public class Mapping 
{
	//-----------------------Creates new Hashtables-----------------------
	private Hashtable<Double, Integer> ScanT_to_ScanNum;
	private Hashtable<Integer, String> ScanNum_to_PepEvid;
	private Hashtable<String, String> PepEvid_to_PepRef;
	private Hashtable<String, String> PepRef_to_PepSequence;
	private Hashtable<String, String> PepEvid_to_DBseqID;
	private Hashtable<String, String> DBseqID_to_PepEvid;
	private Hashtable<String, String> DBseqID_to_Accession;
	private Hashtable<String, Integer> Accession_to_NumIds;
	//private Hashtable<Double, Integer> ScanT_to_NumIds;
	
	//-----------------------Initializes the Hashtables (constructor)-----------------------
	public Mapping()
	{
		ScanT_to_ScanNum = new Hashtable<Double, Integer>();
		ScanNum_to_PepEvid = new Hashtable<Integer, String>();
		PepEvid_to_PepRef = new Hashtable<String, String>();
		PepRef_to_PepSequence = new Hashtable<String, String>();
		PepEvid_to_DBseqID = new Hashtable<String, String>();
		DBseqID_to_PepEvid = new Hashtable<String, String>();
		DBseqID_to_Accession = new Hashtable<String, String>();
		Accession_to_NumIds = new Hashtable<String, Integer>();
	}
	

	//-----------------------Functions to associate the Hashtable keys with their values-----------------------
	
	//Function to map the scan start time to the scan number then scan number to PeptideEvidence
	public void MapScanT_to_ScanNum(ArrayList<Spectra> ArrList, List<SpectrumIdentificationList> specIDlist)
	{
		double scanTime;
		int scanNum;
		int scanNumID;
		
		//Loops through the arraylist containing the objects of type Mapping that contain the scan number
		for(int i = 0; i < ArrList.size(); i++)
		{
			//Gets scan start time from the mzML file
			scanTime = ArrList.get(i).getstart_t();
			//Gets scan number from the mzML file associated with the scan start time (will check if = scan number from the mzID file)
			scanNum = ArrList.get(i).getscanNum();
			
			//Loops through all the scan numbers and adds the one that equals that of the mzML file to the Hashtable (ScanT_to_ScanNum)
			//Then breaks the loop and increments to the next scan start time of the mzML and checks again for an associated scan number in the mzID file
			//This "for" loop is like incrementing the int i until i < specIDlist.size(), so it goes through the entire array
			//Accesses the SpectrumIdentificationResult below the SpectrumIdentificationList
			for(SpectrumIdentificationList specList : specIDlist)
			{
				List<SpectrumIdentificationResult> specResult = specList.getSpectrumIdentificationResult();
				
				//Passes through each line of the SpectrumidentificationResult
				for(SpectrumIdentificationResult specIDresult : specResult)
				{
					//--------------------To get the scan number--------------------
					//Gets the string of spectrumID that contains the scan number
					String spectrumID = specIDresult.getSpectrumID();
					//Separates the string where "=" is present (so we can obtain only the scan number)
					String[] parsedSpectrumID = spectrumID.split("=");
					//Gets the scan number from the array of strings ..... spectrumID="controllerType=0 controllerNumber=1 scan=61"
					String scanNum_line = parsedSpectrumID[3];
					//Converts the scanNum from string to integer
					scanNumID = Integer.parseInt(scanNum_line);
					
					if(scanNum == scanNumID)
					{
						//Maps the key (scanTime) to the value (scanNumID)
						ScanT_to_ScanNum.put(scanTime, scanNumID);
					}
					
					/*
					//--------------------To get the PeptideEvidence_ref--------------------
					//Accesses the SpectrumIdentificationItem below the SpectrumIdentificationResult
					List<SpectrumIdentificationItem> specItem = specIDresult.getSpectrumIdentificationItem();
					//First line of the SpectrumIdentificationItem contains the peptideEvidence_ref
					SpectrumIdentificationItem specID_Item1 = specItem.get(0);
					String peptideEvidence_ref = specID_Item1.getPeptideRef();
					
					//Puts the key and associated value in the Hashtable from class Mapping (by calling the function put...)
					ScanNum_to_PepEvid.put(scanNumID, peptideEvidence_ref);
					*/
					
					//--------------------To get the PeptideEvidence_ref--------------------
					//Accesses the SpectrumIdentificationItem below the SpectrumIdentificationResult
					List<SpectrumIdentificationItem> specItem = specIDresult.getSpectrumIdentificationItem();
					//First line of the SpectrumIdentificationItem contains the peptideEvidence_ref
					SpectrumIdentificationItem specID_Item1 = specItem.get(0);
					List<PeptideEvidenceRef> peptideEvidence_ref = specID_Item1.getPeptideEvidenceRef();
					String pepEvRef = peptideEvidence_ref.get(0).getPeptideEvidenceRef();
					
					//Puts the key and associated value in the Hashtable from class Mapping (by calling the function put...)
					ScanNum_to_PepEvid.put(scanNumID, pepEvRef);
					
				}
			}
			
		}
	}
		
	/*
	//Function to map the scan number to PeptideEvidence
	public void MapScanNum_to_PepEvid(List<SpectrumIdentificationList> specIDlist)
	{
		//Accesses the SpectrumIdentificationResult below the SpectrumIdentificationList
		//This "for" loop is like incrementing the int i until i < specIDlist.size(), so it goes through the entire array
		for(SpectrumIdentificationList specList : specIDlist)
		{
			List<SpectrumIdentificationResult> specResult = specList.getSpectrumIdentificationResult();
			
			//Passes through each line of the SpectrumidentificationResult
			for(SpectrumIdentificationResult specIDresult : specResult)
			{
				//To get the scan number
				//Gets the string of spectrumID that contains the scan number
				String spectrumID = specIDresult.getSpectrumID();
				//Separates the string where "=" is present (so we can obtain only the scan number)
				String[] parsedSpectrumID = spectrumID.split("=");
				//Gets the scan number from the array of strings ..... spectrumID="controllerType=0 controllerNumber=1 scan=61"
				String scanNum_line = parsedSpectrumID[3];
				//Converts the scanNum from string to integer
				int scanNum = Integer.parseInt(scanNum_line);
				
				//To get the PeptideEvidence_ref
				//Accesses the SpectrumIdentificationItem below the SpectrumIdentificationResult
				List<SpectrumIdentificationItem> specItem = specIDresult.getSpectrumIdentificationItem();
				//First line of the SpectrumIdentificationItem contains the peptideEvidence_ref
				SpectrumIdentificationItem specID_Item1 = specItem.get(0);
				String peptideEvidence_ref = specID_Item1.getPeptideRef();
				
				
				//Puts the key and associated value in the Hashtable from class Mapping (by calling the function put...)
				ScanNum_to_PepEvid.put(scanNum, peptideEvidence_ref);
				
			}
		}
	}
	*/

	//Function to map the PeptideEvidence to the peptide_ref
	public void MapPepEvid_to_PepRef(List<PeptideEvidence> PeptideEvidence)
	{
		//Loop passes through all the lines under <PeptideEvidence>
		for(PeptideEvidence PeptideEvid : PeptideEvidence)
		{
			//Gets the PeptideEvidence id
			String PeptideEvidence_id = PeptideEvid.getId();
			//Gets the peptide_ref number in a string
			String peptide_ref = PeptideEvid.getPeptideRef();
			
			//Calls the function "put..." from the class Mapping to associate the values in the Hashtable
			PepEvid_to_PepRef.put(PeptideEvidence_id, peptide_ref);
			
		}
	}
			
	//Function to map the peptide_ref to the PeptideSequence
	public void MapPepRef_to_PepSequence(List<Peptide> Peptide)
	{
		for(Peptide pepList : Peptide)
		{
			//Gets the Peptide id
			String pepID = pepList.getId();
			//Gets the PeptideSequence
			String PeptideSequence = pepList.getPeptideSequence();
			
			//Calls the function "put..." from the class Mapping to associate the values in the Hastable
			PepRef_to_PepSequence.put(pepID, PeptideSequence);
		}
	}
	
	//Function to map a PeptideEvidence to multiple dBSequences
	public void MapPepEv_to_DBseqID(List<PeptideEvidence> PeptideEvidence)
	{
		//Creates and initializes a new Hashtable to map a PeptideEvidence to multiple dBSequences
		Hashtable<String, Set<String>> PepEv_to_dBSeq = new Hashtable<String, Set<String>>();
		
		//Passes through all lines of PeptideEvidence
		for(PeptideEvidence pepEv : PeptideEvidence)
		{
			//Gets the PeptideEvidence id
			String pep_evid = pepEv.getId();
			//Gets the peptide_ref number in a string
			String dBSeq_ref = pepEv.getDBSequenceRef();
			
			//Puts the key and associated value in the Hashtable from class Mapping
			PepEvid_to_DBseqID.put(pep_evid, dBSeq_ref);
			
			//Checks if the Hashtable already contains this dBSequece
			if(PepEv_to_dBSeq.containsKey(dBSeq_ref))
			{
				//Create a new set that contains the duplicated dBSequence removed from the Hashtable
				Set<String> old_dBSeqs = PepEv_to_dBSeq.remove(dBSeq_ref);
				
				//Copy the value to a new HashSet and put the HashSet back into the Hashtable
				Set<String> new_Hash = new HashSet<String>();
				
				//Add the old dBSequences to the Hashtable
				new_Hash.addAll(old_dBSeqs);
				//Add the new dBSequence to the Hashtable 
				new_Hash.add(dBSeq_ref);
				
				PepEv_to_dBSeq.put(pep_evid, new_Hash);
			}
			
			//If the Hashtable doesn't already contain the dBSequence, just simply add it to the Hashtable
			else
			{
				Set<String> dB_Set = new HashSet<String>();
				dB_Set.add(dBSeq_ref);
				PepEv_to_dBSeq.put(pep_evid, dB_Set);
			}
			
		}
		
	}
	
	//Function to map a dBSequence to multiple PeptideEvidences 
	public void MapDBseq_to_PepEv(List<PeptideEvidence> PeptideEvidence)
	{
		Hashtable<String, Set<String>> dBSeq_to_PepEv = new Hashtable<String, Set<String>>();
		
		for(PeptideEvidence pepEvid : PeptideEvidence)
		{
			//Gets the dBSequence reference
			String dBSeqRef = pepEvid.getDBSequenceRef();
			//Gets the peptideEvidence reference 
			String pepEv = pepEvid.getPeptideRef();
			
			//Associates the dBSequence with the peptiveEvidence in the previously created Hashtable (doesn't yet account for shared values)
			DBseqID_to_PepEvid.put(dBSeqRef, pepEv);
			
			//Checks if the Hashtable already contains the peptideEvidence value
			if(dBSeq_to_PepEv.containsKey(pepEv))
			{
				//Creates a set of old peptideEvidences that are removed from the Hashtable
				Set<String> old_pepEv = dBSeq_to_PepEv.remove(pepEv);
				//Creates a new HashSet 
				Set<String> new_pepEv = new HashSet<String>();
				
				//Adds all the hold peptideEvidences to the new HashSet
				new_pepEv.addAll(old_pepEv);
				//Adds the new peptideEvidence to the HashSet
				new_pepEv.add(pepEv);
				
				//Associates the new HashSet with the single dBSequence 
				dBSeq_to_PepEv.put(dBSeqRef, new_pepEv);
			}
			
			else
			{
				Set<String> pepEv_Set = new HashSet<String>();
				pepEv_Set.add(pepEv);
				dBSeq_to_PepEv.put(dBSeqRef, pepEv_Set);
			}
			
			
			//*************************-------------------------------------------*************************
			
			//Gets the number of elements in the set of peptide evidences (i.e. the number of peptide identifications thus far)
			int NumIds = dBSeq_to_PepEv.get(dBSeqRef).size();
			int scanN;
			double scanT;
			
			Hashtable<String, Integer> Accession_to_NumIds = new Hashtable<String, Integer>();
			Accession_to_NumIds.put(DBseqID_to_Accession.get(dBSeqRef), NumIds);
			
			Hashtable<Double, Integer> ScanT_to_
			//Loops through the set of peptide evidences
			for(String pepEvs : dBSeq_to_PepEv.get(dBSeqRef))
			{
				//Get the scan number associated with that peptide evidence
				Set<Integer> keys = ScanNum_to_PepEvid.keySet();
				for(Integer Scan : keys)
				{
					if(ScanNum_to_PepEvid.get(Scan) == pepEvs)
					{
						scanN = Scan;
					}
				}
				
				//Get the scan start time associated with the scan number
				Set<Double> Keys = ScanT_to_ScanNum.keySet();
				for(double Time : Keys)
				{
					if(ScanT_to_ScanNum.get(Time) == scanN)
					{
						scanT = Time;
					}
				}
			}
			
			
			
			
		}
//************************-------------------------------------------------------------------------------------------------------************************
		//Loop through all the dBSequences
		Set<String> allKeys = dBSeq_to_PepEv.keySet();
		
		//ArrayList to store the peptide evidences which have shared dBSequences
		//ArrayList<Shared_PepEvid> Arr_PepEvid = new ArrayList<Shared_PepEvid>();
		
		for(String keys : allKeys)
		{
			//If the dBSequence is associated to multiple peptide evidences
			if(dBSeq_to_PepEv.get(keys).size() > 1)
			{
				//Number of identifications = number of elements in the set of peptide evidences
				int NumIds = dBSeq_to_PepEv.get(keys).size();
				//Gets the accession associated with that dBSequence
				String Accession = DBseqID_to_Accession.get(keys);
				
				//Creates an object that contains the associated accession, the number of identifications and the list of associated peptide evidences
				//Shared_PepEvid shared = new Shared_PepEvid(DBseqID_to_Accession.get(keys),dBSeq_to_PepEv.get(keys).size(), dBSeq_to_PepEv.get(keys));
				
				//Arr_PepEvid.add(shared);
				
				//Associates the accession with the number of identifications of that peptide 
				Accession_to_NumIds.put(Accession, NumIds);
			}
		}
		
		
	}
	
//------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	//Function to map the dBSequence to the accession
	public void MapDBseqID_to_Accession(List<DBSequence> dBSequence)
	{
		//Go through the dBSequences
		for(DBSequence dbList : dBSequence)
		{
			//Gets the DBSequence id as a string
			String dbID = dbList.getId();
			//Gets the accession as a string
			String accession = dbList.getAccession();
			
			//Puts the key and associated value in the Hashtable from class Mapping
			DBseqID_to_Accession.put(dbID, accession);
			
		}
	}
	
	

	//-----------------------"Get" functions to return the mapping between variables-----------------------
	public Hashtable<Double, Integer> getScanT_to_ScanNum()
	{
		return ScanT_to_ScanNum;
	}
	
	public Hashtable<Integer, String> getScanNum_to_PepEvid()
	{
		return ScanNum_to_PepEvid;
	}
	
	public Hashtable<String, String> getPepEvid_to_PepRef()
	{
		return PepEvid_to_PepRef;
	}
	
	public Hashtable<String, String> getPepRef_to_PepSequence()
	{
		return PepRef_to_PepSequence;
	}
	
	public Hashtable<String, String> getPepEvid_to_DBseqID()
	{
		return PepEvid_to_DBseqID;
	}
	
	public Hashtable<String, String> getDBseqID_to_PepEvid()
	{
		return DBseqID_to_PepEvid;
	}
	
	public Hashtable<String, String> getDBseqID_to_Accession()
	{
		return DBseqID_to_Accession;
	}
	
	public Hashtable<String, Integer> getAccession_to_NumIds()
	{
		return Accession_to_NumIds;
	}
	
}
