import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
//import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
//import java.util.Set;
import java.util.Set;

import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
//import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.SequenceCollection;
//import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
//import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;
import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.Scan;
import uk.ac.ebi.jmzml.model.mzml.ScanList;
import uk.ac.ebi.jmzml.model.mzml.ScanWindowList;
import uk.ac.ebi.jmzml.model.mzml.Spectrum;
import uk.ac.ebi.jmzml.xml.io.MzMLObjectIterator;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;

/*---------- Reading the mzML file ----------*/

public class mzMLParser_with_Sets
{
	//HashTable to map PepRef to DBseqID (Hashtable stores a bunch of data in no specific order)
	public static Hashtable<String, String> PeptideRefMapDBseqID = new Hashtable<String, String>();
	/*Hashtable maps keys to values and has two parameters: initial capacity & load factor 
	 *The first parameter specifies the initial size
	 *The second parameter specifics how full the hash table can be before it is resized bigger
	 */

	public static void main(String[] args) 
	{

		/*------------CHECK--------------*/
		//System.out.println("ONE");
		
		/*Unmarshalling = process of transforming a representation of an object that was used for storage or transmission to a representation of the object that is executable "deserializes" XML data into a Java object
		 *(extracts a data structure from a series of bytes)
		 *serialized = marshalling = data structure of object translated to a format that can be stored (i.e. text file)
		 */

		//Provide the file name to be unmarshalled
		//File f = new File("/Users/Nora/Documents/workspace/RealTimeMS/MS_QC_60min_short.mzML");
		//File f = new File("/Users/Nora/Documents/workspace/RealTimeMS/MS_QC_60min_editedV2.mzML");
		File f = new File("/Users/Nora/Documents/workspace/RealTimeMS/MS_QC_60min.mzML");
		
		
		/*------------CHECK--------------*/
		//System.out.println("TWO");

		//Creates a new object, unmarshaller, of class, MzMLUnmarshaller, with the file as a parameter
		MzMLUnmarshaller unmarshaller = new MzMLUnmarshaller(f);
		
		/*------------CHECK--------------*/
		//System.out.println("THREE");

		//Iterates through the spectra one spectrum at a time
		MzMLObjectIterator<Spectrum> spectrumIterator = unmarshaller.unmarshalCollectionFromXpath("/run/spectrumList/spectrum", Spectrum.class);
	

		/*------------CHECK--------------*/
		//System.out.println("F0UR");
		
		//ArrayList to store the objects, spectrum, in order of their scan start time
		ArrayList<Spectra> Arr = new ArrayList<Spectra>();
		
		 /*
		  *Unmarshaller reads through the spectra, gets each value and stores it in the attributes of an object (spectrum) 
		  *The values are of type string, so create a String variable to store them (indicated by " "_line) 
		  *Use the Double.parseDouble to convert the Strings to doubles before storing them in the object
		  */
		 
		/*------------CHECK--------------*/
		//System.out.println("FIVE");
		
		//Iterates through the spectra while there is a next spectrum (hasNext); like saying while line != null
		while(spectrumIterator.hasNext())
		{
			//Creates a new object, spectrum, of class Spectrum
			Spectrum spectrum = spectrumIterator.next();
			
			//Gets the values of the spectrum attributes from the mzML file in line <spectrum>
			double index = spectrum.getIndex();
			String id = spectrum.getId();
			//From the line "id=...", split the String whenever there is "=" to get the single scan time value
			String ID[] = id.split("=");
			//Convert the last cell of ID[] from String to integer to get the scan number
			int scanNum = Integer.parseInt(ID[3]);
			
			//Gets the values from the list of <cvParams> --> <spectrum>
			List<CVParam> cvParam1 = spectrum.getCvParam();
			//Convert the string to a double
			String msLevel_line = cvParam1.get(1).getValue();
			int msLevel = Integer.parseInt(msLevel_line);

			String base_mz_line = cvParam1.get(4).getValue();
			double base_mz = Double.parseDouble(base_mz_line);

			String base_intens_line = cvParam1.get(5).getValue();
			double base_intens = Double.parseDouble(base_intens_line);

			String tot_curr_line = cvParam1.get(6).getValue();
			double tot_curr = Double.parseDouble(tot_curr_line);

			String low_mz_line = cvParam1.get(7).getValue();
			double low_mz = Double.parseDouble(low_mz_line);

			String high_mz_line = cvParam1.get(8).getValue();
			double high_mz = Double.parseDouble(high_mz_line);
			
			//Gets the values from the <cvParam> --> <scan> --> <ScanList> --> <spectrum>
			ScanList scan_list = spectrum.getScanList();
			//Under the ScanList, there is <scan> of type list
			List<Scan> scan = scan_list.getScan();
			//Under Scan, there is a list of cvParam
			List<CVParam> cvParam2 = scan.get(0).getCvParam();
			//The first cvParam line contains the scan start time
			String start_t_line = cvParam2.get(0).getValue();
			double start_t = Double.parseDouble(start_t_line);
			
			//The filter type is made of words, don't need to convert the String to double
			String filter = cvParam2.get(1).getValue();
			
			String scan_config_line = cvParam2.get(2).getValue();
			double scan_config = Double.parseDouble(scan_config_line);
			
			String inject_t_line = cvParam2.get(3).getValue();
			double inject_t = Double.parseDouble(inject_t_line);
			
			//Gets the values from <scanWindowList> --> <scan> --> <ScanList> --> <spectrum>
			ScanWindowList scanWindowList = scan.get(0).getScanWindowList();
			//Gets the values from <scanWindow> --> <scanWindowList> --> <scan> --> <ScanList> --> <spectrum>
			List<ParamGroup> scanWindow = scanWindowList.getScanWindow();
			//Gets the values from <cvParam> --> <scanWindow> --> <scanWindowList> --> <scan> --> <ScanList> --> <spectrum>
			List<CVParam> cvParam3 = scanWindow.get(0).getCvParam();
			String scan_lower_line = cvParam3.get(0).getValue();
			double scan_lower = Double.parseDouble(scan_lower_line);
			
			String scan_upper_line = cvParam3.get(1).getValue();
			double scan_upper = Double.parseDouble(scan_upper_line);
			
			
			//Creates a new object, spec, of type Spectra to store the values of each individual spectrum
			Spectra spec = new Spectra(index, scanNum, msLevel, base_mz, base_intens, tot_curr, low_mz, high_mz, start_t, filter, scan_config, inject_t, scan_lower, scan_upper);
			
			/*-----Binary search to order spectra based on scan time (splits the array in half to minimize the amount of searching)-----*/

			//The lower boundary of the array to search
			int first = 0;
			//The upper boundary of the array to search
			int last = Arr.size() - 1;
			//The middle value of the array
			int mid;
			
			//If the arraylist is empty, just add the object
			if(Arr.size() < 1) 
			{
				Arr.add(spec);
			}
			
			else if(Arr.size() == 1)
			{
				if(Arr.get(0).getstart_t() > start_t)
				{
					Arr.add(0, spec);
				}
				
				else
				{
					Arr.add(spec);
				}
			}
			
			//If the arraylist is not empty
			else
			{
				/*"while" loop to find the index to which the scan start time value should be added
				 *Loop until the two boundaries reach (no more cells to search)
				 *Scan start times should never be equal so no "if" statement needed for this case
				 */
				while(first <= last)
				{
					//If odd # of elements in the arraylist
					if(Arr.size()/2 != 0)
					{
						mid = (first + last)/2;
						
						//If the newest found "scan start time" is smaller than that in the middle of the arraylist, search the lower half of the arraylist
						if(start_t < Arr.get(mid).getstart_t())
						{
							//Mid value becomes the upper boundary; only bottom half of arraylist will be searched
							last = mid - 1;
						}
						
						//If the newest scan start time is smaller than the value in the middle of the arraylist, search the upper half
						else
						{
							//Mid values becomes the lower boundary; only top half of arraylist will be searched
							first = mid + 1;
						}
					}
					
					//If even # of elements in the arraylist (will use the lower value of the 2 middle values)
					else
					{
						mid = (first + last + 1)/2;
						
						//If the newest found "scan start time" is smaller than that in the middle of the arraylist, search the lower half of the arraylist
						if(start_t < Arr.get(mid).getstart_t())
						{
							//Mid value becomes the upper boundary; only bottom half of arraylist will be searched
							last = mid - 1;
						}
						
						//If the newest scan start time is smaller than the value in the middle of the arraylist, search the upper half
						else
						{
							//Mid values becomes the lower boundary; only top half of arraylist will be searched
							first = mid + 1;
						}
					}
					
					
					//------------------Checks that the binary sorting method is working ------------------
					/*
					System.out.println("first: " + first);
					System.out.println("last: " + last);
					*/
				
				
				} //End of "while" loop
				
				/*Once the entire array has been searched, choose the lower boundary and add the new spectrum above this index
				 *The scan start time will be larger than that of the lower index and smaller than that of the upper index
				 */
				
				/*------------CHECK--------------*/
				//System.out.println("SIX");
				
				//Index into the arraylist to store the object spec
				int indx = first;
				
				//Inserts a value into the ArrayList at a particular index (shifts all the subsequent cells one index forward)
				Arr.add(indx, spec);
				

			} //End of "else" loop
			
	
			/*------------CHECK--------------*/
			//System.out.println("SEVEN");
			
				
		} //End of outer "while loop"

		/*------------CHECK--------------*/
		//System.out.println("EIGHT");
		
		/*Check to make sure the array is sorted by scan start time*/
		for(int check = 0; check < Arr.size(); check++)
		{
			//If the scan start time of the cell is greater than the scan start time of the next cell, print a warning
			if(Arr.get(check).getstart_t() > Arr.get(check).getstart_t())
			{
				System.out.println("WARNING: Array of spectra is not correctly sorted by the spectrum scan start times!!!");
				break;
			}
		}
		
		//------------------Prints the scan time of the first spectrum------------------
		/*
		double scanT = Arr.get(0).getstart_t();
		System.out.println(scanT);
		
		//Prints out all the scan times
		for(int toArr = 0; toArr < Arr.size(); toArr++)
		{
			System.out.println(Arr.get(toArr).getstart_t());
		}
		*/
		
		
		/*
		 * Changed the second scan time (0.016783567) to 4.123456 and...
		 * File f = new File("/Users/Nora/Documents/workspace/RealTimeMS/MS_QC_60min_short.mzML");
		 * changed to
		 * File f = new File("/Users/Nora/Documents/workspace/RealTimeMS/MS_QC_60min_editedV2.mzML");
		 * */
		
		//Check if program runs properly
		System.out.println("YAY this works!\n");
		

		
		

		
		
		
		/*------------------PARSING mzid FILE------------------*/
			
		//Create a new object of class, Mapping, that contains all the Hashtables (created outside of loops so that it can be later accessed)
		Mapping_with_Sets map_values = new Mapping_with_Sets();
		
		//Try statement used to catch exceptions that might be thrown when a program executes (prevents crashing). Always followed by "catch" statement that's executed when exception is thrown
		try 
		{
			//URL is a class that points to a "resource" on the web (i.e. file, directory...etc)
			URL file = null; 
	
			/*------------CHECK--------------*/
			//System.out.println("A");
			
			
			//parses the mzML file (reads text and builds a data structure)
			//file = mzMLParser.class.getClassLoader().getResource("MS_QC_60min_decoy.mzid");
			file = mzMLParser_new_23May.class.getClassLoader().getResource("MS_QC_60min.mzid");
			
			/*------------CHECK--------------*/
			//System.out.println("B");
	
			//System.out.println(file);
			
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
				
				//Calls the function in Mapping.java to map the scan start time to the scan number to the peptideEvidence
				map_values.MapScanT_to_ScanNum(Arr, specIDlist);
				
				//Calls the function in Mapping.java to map the peptideEvidence to the peptide_ref
				map_values.MapPepEvid_to_PepRef(PeptideEvidence);
				
				//Calls the function in Mapping.java to map the peptide_ref to the PeptideSequence
				map_values.MapPepRef_to_PepSequence(Peptide);
				
				//Calls the function in Mapping.java to map the peptideEvidence to multiple dBSequence
				map_values.MapPepEv_to_DBseqID(PeptideEvidence);
				
				//Calls the function Mapping.java to map the dBSequence to multiple peptideEvidences
				map_values.MapDBseq_to_PepEv(PeptideEvidence);
				
				//Calls the function in Mapping.java to map the dBSequence to the accession
				map_values.MapDBseqID_to_Accession(dBSequence);
				
				//To access the private Hashtables, must go through a "get" function and store the value in a new Hashtable
				//Hashtable <String, String> map = map_values.getDBseqID_to_Accession();
				
				//---------------------Associate the scan start time (mzid) to the scan number (mzML)---------------------
				
				/*------------CHECK--------------*/
				//System.out.println("C");
			}
			
			/*------------CHECK--------------*/
			//System.out.println("D");
		}
	
		//Executed if exception is thrown when running the program
		catch (Exception e)
		{
			//Prints the throwable and its backtrace to the standard error stream
			e.printStackTrace();
			
			/*------------CHECK--------------*/
			//System.out.println("Exception thrown");
		}
		
		//-------------Test by printing a Hashtable-------------
		System.out.println("Printing the content of the Hashtables: ");
		
		//ScanT=ScanNum
		Hashtable<Double, Integer> print_ScanT_to_ScanNum = map_values.getScanT_to_ScanNum();
		System.out.println(print_ScanT_to_ScanNum);
		
		//ScanNum=PepEvid
		Hashtable<Integer, String> print_ScanNum_to_PepEvid = map_values.getScanNum_to_PepEvid();
		System.out.println(print_ScanNum_to_PepEvid);
		
		//PepEvid=PepRef
		Hashtable<String, String> print_PepEvid_to_PepRef = map_values.getPepEvid_to_PepRef();
		System.out.println(print_PepEvid_to_PepRef);
		
		//PepEvid=DBseqID
		Hashtable<String, String> print_PepEvid_to_DBseqID = map_values.getPepEvid_to_DBseqID();
		System.out.println(print_PepEvid_to_DBseqID);
		
		//DBseqID=PepEvid
		Hashtable<String, Set<String>> print_DBseqID_to_PepEvid = map_values.getDBseqID_to_PepEvid();
		System.out.println(print_DBseqID_to_PepEvid);
		
		//PepRef=PepSequence
		Hashtable<String, String> print_PepRef_to_PepSequence = map_values.getPepRef_to_PepSequence();
		System.out.println(print_PepRef_to_PepSequence);
		
		//DBseqID=Accession
		Hashtable<String, String> print_DBseqID_to_Accession = map_values.getDBseqID_to_Accession();
		System.out.println(print_DBseqID_to_Accession + "\n");
		

		//---------------Stores objects containing the spectrum ID values in an ArrayList in ascending order of scan start time------------
		
		//Creates an object of class ID to 
		IDs_with_Sets MappedSpec;
		//Creates an arraylist to store the objects
		ArrayList<IDs_with_Sets> MappedArr = new ArrayList<IDs_with_Sets>();
		
		//System.out.println("Printing the spectra attributes: ");
		
		//Loops through the array containing ordered scan start times as a starting point for getting the other values
		for(int i = 0; i < Arr.size(); i++)
		{
			//Gets the scan start time
			double startTime = Arr.get(i).getstart_t();
			
			//If the mzID file contains the same scan number as the mzML file
			if(map_values.getScanT_to_ScanNum().containsKey(startTime))
			{
				//Gets the scan number associated with the scan start time
				int scanNum = map_values.getScanT_to_ScanNum().get(startTime);
				//System.out.println("Scan number: " + scanNum);
				//Gets the peptideEvidence associated with the scan number
				String pepEv = map_values.getScanNum_to_PepEvid().get(scanNum);
				//System.out.println("Peptide Evidence: " + pepEv);
				//Gets the peptide_ref associated with the peptideEvidence
				String pepRef = map_values.getPepEvid_to_PepRef().get(pepEv);
				System.out.println("Peptide Reference: " + pepRef);
				//Gets the dBSequence associated with the peptideEvidence
				String dBSeq = map_values.getPepEvid_to_DBseqID().get(pepEv);
				//System.out.println("dBSequence: " + dBSeq);
				//Gets the peptideSequence associated with the peptide_ref
				String pepSeq = map_values.getPepRef_to_PepSequence().get(pepRef);
				//System.out.println("Peptide Sequence: " + pepSeq);
				//Gets the accession associated with the dBSequence
				String acc = map_values.getDBseqID_to_Accession().get(dBSeq);
				//System.out.println("Accession: " + acc);
				
				//Initializes the object, MappedSpec
				MappedSpec = new IDs_with_Sets(startTime, scanNum, pepEv, pepRef, dBSeq, pepSeq, acc);
				
				//Adds the object to the arraylist (so each cell contains one object with all the attributes of a spectrum)
				MappedArr.add(MappedSpec);
			}
			
		}
		
		//Checks if values were properly stored in the objects in the array list
		
		
		for (int j = 0; j < MappedArr.size(); j++)
		{
			System.out.println(MappedArr.get(j).getscan_t());
			System.out.println(MappedArr.get(j).getScan());
			System.out.println(MappedArr.get(j).getpeptideEvidence());
			System.out.println(MappedArr.get(j).getpeptide_ref());
			System.out.println(MappedArr.get(j).getpeptide_Sequence());
			System.out.println(MappedArr.get(j).getdBSequence_id()); 
			System.out.println(MappedArr.get(j).getaccession());
		}
		
		
		System.out.println("\nEnd");
		
		
		//---------------------Function to find duplicate peptides---------------------
		
		for(int j = 0; j < ???; j++)
		{
			if()
		}
		
		
		
		
		
		
		
		//---------------------Buffered Writer---------------------
		
		//Buffered writer can also be written in multiple lines:
			//File f2 = new File("RealTimeMS/src/ID_output.txt");
			//FileWriter fw = new FileWriter(f2);
			//BufferedWriter w = new BufferedWriter(fw);
		
		//Try and catch statement generally used for files
		try
		{	
			/*------------CHECK--------------*/
			//System.out.println("check 1");
			
			double scan_time;
			int scan_num;
			String pep_evid;
			String pep_ref;
			String pep_seq;
			String dB_seq; 
			String acces;
			
			//Create a buffered writer
			//Combine all into one line to make it easier (don't need to make actual variables)
			//BufferedWriter w = new BufferedWriter(new FileWriter(new File("RealTimeMS/src/ID_output.txt")));
			
			File file = new File("/Users/Nora/Documents/workspace/RealTimeMS/ID_Output.txt");
			
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			
			/*------------CHECK--------------*/
			//System.out.println("check 2");
			
			for (int j = 0; j < MappedArr.size(); j++)
			{
				scan_time = MappedArr.get(j).getscan_t();
				scan_num = MappedArr.get(j).getScan();
				pep_evid = MappedArr.get(j).getpeptideEvidence();
				pep_ref = MappedArr.get(j).getpeptide_ref();
				pep_seq = MappedArr.get(j).getpeptide_Sequence();
				dB_seq = MappedArr.get(j).getdBSequence_id(); 
				acces = MappedArr.get(j).getaccession();
				
				/*------------CHECK--------------*/
				//System.out.println("check 3");
				
				//Writes the values into the text file as follows...
				w.write("Scan time: "+scan_time+"\n"+"Scan number: "+scan_num+"\n"+"Peptide evidence: "+pep_evid+
						"\n"+"Peptide reference: "+pep_ref+"\n"+"Peptide sequence: "+pep_seq+"\n"+"dBSequence: "+
						dB_seq+"\n"+"Accession: "+acces+"\n");
				
				/*------------CHECK--------------*/
				//System.out.println("check 4");
				
				//Clears the memory and moves it the hardrive 
				w.flush(); 
				
				/*------------CHECK--------------*/
				//System.out.println("check 5");
			}
			
			/*------------CHECK--------------*/
			//System.out.println("check 6");
			
			//Closes the buffered writer 
			w.close();
			
			/*------------CHECK--------------*/
			//System.out.println("check 7");
			
			/*-----------------------Read file to check if info has been correctly written to the file-----------------------*/
			BufferedReader reader = new BufferedReader(new FileReader(file));
			//Stores the lines of the text file in this variable 
			String line = reader.readLine();
			
			System.out.println("\nReading the file:");
			while(line != null)
			{
				line = reader.readLine();
				System.out.println(line);
			}
			
			reader.close();
			
		}
		
		//Catches any errors in reading/ writing the file 
		catch (Exception e) 
		{
			System.out.println("Threw an exception");
			e.printStackTrace();
		}

		/*------------CHECK--------------*/
		System.out.println("\nEnd of program");
		
	} //end of main 


} //end of public class

