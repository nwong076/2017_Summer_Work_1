package Francesca;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import Mapping;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.SequenceCollection;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;
import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.Scan;
import uk.ac.ebi.jmzml.model.mzml.ScanList;
import uk.ac.ebi.jmzml.model.mzml.ScanWindowList;
import uk.ac.ebi.jmzml.model.mzml.Spectrum;
import uk.ac.ebi.jmzml.xml.io.MzMLObjectIterator;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;


public class Parsing_mzid_old
{
	public static void temporary()
	{
		/*------------------PARSING mzid FILE------------------*/

		//initialize HashTables to map between variables
		//created in Mapping.java
		
		Hashtable<Integer, String> ScanNum_to_PepEvid = null;
		Hashtable<String, String> PepEvid_to_PepRef = null;
		Hashtable<String, String> PepRef_to_PepSequence = null;
		Hashtable<String, String> PepEvid_to_DBseqID = null;
		//there's multiple peptide references for each dB sequence id so need to make a set and add to hashtable)
		Hashtable<String, String> DBseqID_to_Accession = null;
		

		/*
		HashTable to map each pepRef to the concatenation so we can see if we have unique peptides for a protein or not
		Hashtable<String, Set<String>> pepRef_to_Scan = new Hashtable<String, Set<String>>();
		*/
		
		//Try statement used to catch exceptions that might be thrown when a program executes (prevents crashing). Always followed by "catch" statement that's executed when exception is thrown
		try 
		{
			//URL is a class that points to a "resource" on the web (i.e. file, directory...etc)
			URL file = null; 

			//parses the mzML file (reads text and builds a data structure)
			file = mzMLParser.class.getClassLoader().getResource("MS_QC_60min_decoy.mzid");

			//File cannot be null
			if (file != null) 
			{
				//Hierarchy in the file: MzIdentML --> Sequence Collection or DataCollection --> AnalysisData ....etc
				//Creates a new unmarshaller (extracts a data structure from a series of bytes)
				MzIdentMLUnmarshaller unmarshallerMzid = new MzIdentMLUnmarshaller(file);
				DataCollection dc = unmarshallerMzid.unmarshal(DataCollection.class);
				AnalysisData ad = dc.getAnalysisData();
				//Essentially all the data from the XML file falls under this tab heading
				SequenceCollection sc = unmarshallerMzid.unmarshal(SequenceCollection.class);
				
				//Accesses SpectrumIdentificationList 
				List<SpectrumIdentificationList> specIDlist = ad.getSpectrumIdentificationList();
				//Calls the function to do the Hashtable mapping of scan number to peptideEvidence_ref
				Mapping scanNum_to_pepEvid = MapScanNum_to_PepEvid(specIDlist);
				
				//Accesses the line of PeptideEvidence
				List<PeptideEvidence> PeptideEvidence = sc.getPeptideEvidence();
				//Calls the function to do the Hashtable mapping
				Mapping PepEvid_to_PepRef = MapPepEvid_to_PepRef(PeptideEvidence);
				
				//Accesses the lines of Peptide
				List<Peptide> Peptide = sc.getPeptide();
				//Calls the function to do the Hashtable mapping
				Mapping PepRef_to_PepSequence = MapPepRef_to_PepSequence(Peptide);
				
				//Calls the function to do the Hashtable mapping
				Mapping PepEvid_to_dBSequence = MapPepEvid_to_DBseqID(PeptideEvidence);
				
				//Accesses the lines of DBSequence
				List<DBSequence> dBSequence = sc.getDBSequence();
				//Calls the function to fill the Hashtable associating the dBSequence and accession
				Mapping dBSequence_to_accession = MapDBseqID_to_Accession(dBSequence);
				
				Mapping Map = Map_Function(specIDlist, PeptideEvidence, Peptide, dBSequence);
				
				
				//NEED TO MAKE peptideEvidence_ref = PeptideEvidence id
				//NEED TO MAKE dBSequence_ref = dbID
				//NEED TO MAKE peptide_ref = pepID
				
			
				
			}
		}
		
		//executed if exception is thrown when running the program
		catch (Exception e)
		{
			//prints the throwable and its backtrace to the standard error stream
			e.printStackTrace();
		}
		
		
		
		//Function to map the PeptideEvidence to the peptide_ref
		public static Mapping MapPepEvid_to_PepRef(List<PeptideEvidence> PeptideEvidence)
		{
			//Creates new object of class Mapping
			Mapping PepEvid_to_PepRef = new Mapping();
			
			//Loop passes through all the lines under <PeptideEvidence>
			for(PeptideEvidence PeptideEvid : PeptideEvidence)
			{
				//Gets the first line of the PeptideEvidence as a string
				PeptideEvidence PeptideEvidence_line = PeptideEvidence.get(0);
				//Gets the PeptideEvidence id
				String PeptideEvidence_id = PeptideEvidence_line.getId();
				//Gets the peptide_ref number in a string
				String peptide_ref = PeptideEvidence_line.getPeptideRef();
				
				//Calls the function "put..." from the class Mapping to associate the values in the Hashtable
				PepEvid_to_PepRef.putPepEvid_to_PepRef(PeptideEvidence_id, peptide_ref);
				
			}
			
			//Function returns the object PepEvid_to_PepRef that contains the Hasthable associating the peptideEvidence to the peptide_ref
			return PepEvid_to_PepRef;
		}
		
		
		
		//Function to map the peptide_ref to the PeptideSequence
		public static Mapping MapPepRef_to_PepSequence(List<Peptide> Peptide)
		{
			//Creates a new object of class Mapping
			Mapping PepRef_to_PepSeq = new Mapping();
			
			for(Peptide pep : Peptide)
			{
				//Gets the first line of the Peptide
				Peptide pep_list = Peptide.get(0);
				//Gets the Peptide id
				String pepID = pep_list.getId();
				//Gets the PeptideSequence
				String PeptideSequence = pep_list.getPeptideSequence();
				
				//Calls the function "put..." from the class Mapping to associate the values in the Hastable
				PepRef_to_PepSeq.putPepRef_to_PepSequence(pepID, PeptideSequence);
			}
			
			//Function returns the object PepRef_to_PepSeq that contains the Hashtable associating the peptide_ref to the PeptideSequence
			return PepRef_to_PepSeq;
		}
		
		//Function to map the PeptideEvidence to the dBSequence
		public static Mapping MapPepEvid_to_DBseqID(List<PeptideEvidence> PeptideEvidence)
		{
			Mapping PepEvid_to_DBseqID = new Mapping();
			
			//Passes through all the lines of PeptideEvidence
			for(PeptideEvidence pepEv : PeptideEvidence)
			{
				//Gets the first line of the PeptideEvidence as a string
				PeptideEvidence PeptideEvidence_line = PeptideEvidence.get(0);
				//Gets the PeptideEvidence id
				String PeptideEvidence_id = PeptideEvidence_line.getId();
				//Gets the peptide_ref number in a string
				String dBSequence_ref = PeptideEvidence_line.getDBSequenceRef();
				
				//Puts the key and associated value in the Hashtable from class Mapping
				PepEvid_to_DBseqID.putPepEvid_to_DBseqID(PeptideEvidence_id, dBSequence_ref);
				
			}
			
			//This function returns the object PepEvid_to_DBseqID that contains the Hashtable associating the PeptideEvidence and dBSequence 
			return PepEvid_to_DBseqID;
		}
		
		//Function to map the dBSequence to the accession
		public static Mapping MapDBseqID_to_Accession(List<DBSequence> dBSequence)
		{
			//Creates a new object of Class Mapping
			Mapping dbSeq_to_acc = new Mapping();
			
			//Go through the dBSequences
			for(DBSequence dbList : dBSequence)
			{
				//Gets the DBSequence id as a string
				String dbID = dbList.getId();
				//Gets the accession as a string
				String accession = dbList.getAccession();
				
				//Puts the key and associated value in the Hashtable from class Mapping
				dbSeq_to_acc.putDBseqID_to_Accession(dbID, accession);
				
			}
			
			//This function returns the object dbSeq_to_acc that contains the Hashtable associating the dBSequence and accession
			return dbSeq_to_acc;
		}
	}
	
	
	
}


/*To store the attributes of the mzID file*/