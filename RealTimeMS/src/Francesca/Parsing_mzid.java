package Francesca;
/*THIS IS PLACED IN THE mzMLParser_new_23May.java file*/


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


public class Parsing_mzid 
{
	public static void temporary()
	{
		/*------------------PARSING mzid FILE------------------*/

		//Initialize HashTables to map between variables created in Mapping.java
		
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
				
				//Accesses the line of PeptideEvidence
				List<PeptideEvidence> PeptideEvidence = sc.getPeptideEvidence();
				
				//Accesses the lines of Peptide
				List<Peptide> Peptide = sc.getPeptide();
				
				//Accesses the lines of DBSequence
				List<DBSequence> dBSequence = sc.getDBSequence();
				
				//Create a new object of class, Mapping, that contains all the Hashtables
				Mapping map_values = new Mapping();
				//Calls the function in Mapping.java to map the scan number to the peptideEvidence
				map_values.MapScanNum_to_PepEvid(specIDlist);
				//Calls the function in Mapping.java to map the peptideEvidence to the peptide_ref
				map_values.MapPepEvid_to_PepRef(PeptideEvidence);
				//Calls the function in Mapping.java to map the peptide_ref to the PeptideSequence
				map_values.MapPepRef_to_PepSequence(Peptide);
				//Calls the function in Mapping.java to map the peptideEvidence to the dBSequence
				map_values.MapPepEvid_to_DBseqID(PeptideEvidence);
				//Calls the function in Mapping.java to map the dBSequence to the accession
				map_values.MapDBseqID_to_Accession(dBSequence);
				
				//To access the private Hashtables, must go through a "get" function and store the value in a new Hashtable
				Hashtable <String, String> map = map_values.getDBseqID_to_Accession();
				
			}
		}
		
		//Executed if exception is thrown when running the program
		catch (Exception e)
		{
			//Prints the throwable and its backtrace to the standard error stream
			e.printStackTrace();
		}
		
	}
	
}


/*To store the attributes of the mzID file*/
