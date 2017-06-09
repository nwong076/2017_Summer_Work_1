package Francesca;

import java.util.Hashtable;

public class DBseqMap 
{
	//creates new HashTables
	Hashtable<String, String> DBseqAccessionMapDBseqID; 
	Hashtable<String, String> DBseqIDMapDBseqAccession;

	//initializes the HashTables
	public DBseqMap() 
	{
		DBseqAccessionMapDBseqID = new Hashtable<String, String>();
		DBseqIDMapDBseqAccession = new Hashtable<String, String>();
	}

	
	
	
	//map to DB sequence ID from the Accession
	public void putDBseqAccessionMapDBseqID(String _DBseqAccession, String _DBseqID) 
	{
		//puts the key (_DBseqAccession) and associated value (_DBseqID) into the hashtable (DBseqAccessionMapDBseqID)
		DBseqAccessionMapDBseqID.put(_DBseqAccession, _DBseqID);
	}

	//map to Accession from the DB sequence ID
	public void putDBseqIDMapDBseqAccession(String _DBseqID, String _DBseqAccession) 
	{
		//puts the key (_DBseqID) and associated value (_DBseqAccession) into the hashtable (DBseqIDMapDBseqAccession)
		DBseqIDMapDBseqAccession.put(_DBseqID, _DBseqAccession);
	}

	
	
	
	//gets the DB sequence ID
	public Hashtable<String, String> getDBseqAccessionMapDBseqID() 
	{
		return DBseqAccessionMapDBseqID;
	}

	//gets the Accession
	public Hashtable<String, String> getDBseqIDMapDBseqAccession() 
	{
		return DBseqIDMapDBseqAccession;
	}
}