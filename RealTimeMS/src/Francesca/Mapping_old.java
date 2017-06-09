package Francesca;
import java.util.Hashtable;
import java.util.List;

import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;

//Class that contains Hashtables to map values from low level scan number to high level peptide sequence and accession
public class Mapping_old 
{
	//new Hashtable to relate the scan number to the peptideEvidence_id number
	private Hashtable<Integer, String> ScanNum_to_PepEvid;
	//new Hashtable to relate the peptideEvidence_id to the peptide_ref
	private Hashtable<String, String> PepEvid_to_PepRef;
	//new Hashtable to relate the peptide_ref to the PeptideSequence
	private Hashtable<String, String> PepRef_to_PepSequence;
	//new Hashtable to relate the peptideEvidence_id to the dBSequence_ref
	private Hashtable<String, String> PepEvid_to_DBseqID;
	//new Hashtable to relate the DB sequence id to the accession
	private Hashtable<String, String> DBseqID_to_Accession;
	
	/*initializes the Hashtables*/
	public Mapping_old()
	{
		ScanNum_to_PepEvid = new Hashtable<Integer, String>();
		PepEvid_to_PepRef = new Hashtable<String, String>();
		PepRef_to_PepSequence = new Hashtable<String, String>();
		PepEvid_to_DBseqID = new Hashtable<String, String>();
		DBseqID_to_Accession = new Hashtable<String, String>();
	}
	
	
	/*functions to associate the Hashtable keys to values*/
	public void putScanNum_to_PepEvid(Integer scan_num, String pep_evid)
	{
		ScanNum_to_PepEvid.put(scan_num, pep_evid);
	}
	
	public void putPepEvid_to_PepRef(String pep_evid, String pep_ref)
	{
		PepEvid_to_PepRef.put(pep_evid, pep_ref);
	}
	
	public void putPepRef_to_PepSequence(String pep_ref, String pep_seq)
	{
		PepRef_to_PepSequence.put(pep_ref,  pep_seq);
	}
	
	public void putPepEvid_to_DBseqID(String pep_evid, String dbSeqID)
	{
		PepEvid_to_DBseqID.put(pep_evid,  dbSeqID);
	}
	
	public void putDBseqID_to_Accession(String dbSeqID, String accession)
	{
		DBseqID_to_Accession.put(dbSeqID, accession);
	}
	
	
	
	
	
	/*functions return the mapping between variables*/
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
	
	public Hashtable<String, String> getDBseqID_to_Accession()
	{
		return DBseqID_to_Accession;
	}
	
	
}
