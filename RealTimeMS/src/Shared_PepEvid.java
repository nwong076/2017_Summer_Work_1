import java.util.Set;

//Class to store the accession, number of identifications and the associated peptide evidences of peptides (dBSequences) that have multiple identifications

public class Shared_PepEvid 
{
	private String Accession;
	private int num_ids;
	private Set<String> PepEvidence;
	
	//Constructor
	public Shared_PepEvid(String _Accession, int _num_ids, Set<String> set)
	{
		Accession = _Accession;
		num_ids = _num_ids;
		PepEvidence = set;
	}
	
	
	//-----------------Functions to get the private values----------------
	public String getAccession()
	{
		return Accession;
	}
	
	public int get_num_ids()
	{
		return num_ids;
	}
	
	public Set<String> getPepEvidence()
	{
		return PepEvidence;
	}
	
}
