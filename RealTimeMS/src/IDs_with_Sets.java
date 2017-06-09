
/*class created to store the attributes of the mzID file */

public class IDs_with_Sets
{
	
	/*ADD SCAN TIME!!!! (table containing scan time, scan #, peptide sequence, accession*/
	private double scan_t; //stores the scan time from the mzML file to help find the scan number
	private int scan; //associates the mzML file to the mzID file and map to the peptideEvidence_ref
	private String peptideEvidence; //map to the PeptideEvidence_id
	private String Peptide_ref; //map to the peptide_ref and dBsequence_ref
	private String dBSequence_id; //map to the accession
	private String accession; //found through dBSequence
	private String peptide_ref; //map to the peptide sequence 
	private String peptide_Sequence; //found through peptide evidence
	
	//Constructor to associate objects with the class mzID
	/*
	public IDs(double scan_t2, int scan2, String peptideEvidence2, String Peptide_ref2, String dBSequence_id2, String accession2, String peptide_ref2, String peptide_Sequence2)
	{
		scan_t = scan_t2;
		scan = scan2;
		peptideEvidence = peptideEvidence2;
		Peptide_ref = Peptide_ref2;
		dBSequence_id = dBSequence_id2;
		accession = accession2;
		peptide_ref = peptide_ref2;
		peptide_Sequence = peptide_Sequence2;
	}
	*/


	public IDs_with_Sets(double scanTime, int scanNum, String peptideEvidence2, String peptideRef,
			String dBSequence, String accession2, String peptideSequence) 
	{
		scan_t = scanTime;
		scan = scanNum;
		peptideEvidence = peptideEvidence2;
		peptide_ref = peptideRef;
		dBSequence_id = dBSequence;
		accession = accession2;
		peptide_Sequence = peptideSequence;
	}

	//Functions to access the private values from class Spectrapublic double getscan_t()
	public double getscan_t()
	{
		return scan_t;
	}
	
	public int getScan()
	{
		return scan;
	}
	
	public String getpeptideEvidence()
	{
		return peptideEvidence;
	}

	public String getPeptide_ref()
	{
		return Peptide_ref;
	}
	
	public String getdBSequence_id()
	{
		return dBSequence_id;
	}
	
	public String getaccession()
	{
		return accession;
	}
	
	public String getpeptide_ref()
	{
		return peptide_ref;
	}
	
	public String getpeptide_Sequence()
	{
		return peptide_Sequence;
	}

}
