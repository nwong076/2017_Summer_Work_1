package MS2_txt_11May2017;

/*Program reads data from a text file of tandem mass spec (MS2) data. Class contains attributes of peptide mass ArrSpec. Objects each contain unique attribute of this class.*/

import java.util.ArrayList; 	//library to use ArrayList
import java.io.BufferedReader;	//library to read from a file
import java.io.File;
import java.io.FileReader; 		//library to allow access to the file and read it

public class MS2_Data	//class that contains the attributes of a peptide ArrSpec
{
	
	public static void main(String[] args) throws Exception	//incase file isn't found, prevents program from giving an error
	{
		File file = new File("src/MS2_txt_11May2017/ms2.txt");	//first create the file
		
		FileReader filereader = new FileReader(file); //second create a FileReader
		
		BufferedReader reader = new BufferedReader(filereader);	//third read the file with the BufferedReader
		
		String line = reader.readLine();	//stores read text in this variable
		
		ArrayList<Spectrum> ArrSpec = new ArrayList<Spectrum>();	//create an ArrayList, ArrSpec, of type Spectrum to store the objects 
		
		while (line != null)
		{
			//first line 
			String[] line1 = line.split("\t");				//reads the line into an array String which are separated by tabs declares an array of strings called "line1"
			int ScanNum = Integer.parseInt(line1[1]);		//converts the scan number from string to int
			double PreMZ = Double.parseDouble(line1[3]);	//converts the m/z ratio from string to double
			line = reader.readLine();						//moves to the next line
			
			//second line
			String[] line2 = line.split("\t");
			double RetTime = Double.parseDouble(line2[2]);	
			line = reader.readLine();
			
			//third line
			String[] line3 = line.split("\t");
			double PreInt = Double.parseDouble(line3[2]);
			line = reader.readLine();
			
			//fourth line
			String[] line4 = line.split("\t");
			double IonInjTime = Double.parseDouble(line4[2]);
			line = reader.readLine();
			
			//fifth line
			String[] line5 = line.split("\t");
			String ActType = line5[2];
			line = reader.readLine();
			
			//sixth line
			String[] line6 = line.split("\t");
			String PreFile = line6[2];
			line = reader.readLine();
			
			//seventh line
			String[] line7 = line.split("\t");
			int PreScanNum = Integer.parseInt(line7[2]);
			line = reader.readLine();
			
			//eighth line
			String[] line8 = line.split("\t");
			String InstType = line8[2];
			line = reader.readLine();
			
			//ninth line
			String[] line9 = line.split("\t");
			double mass2 = Double.parseDouble(line9[2]);
			line = reader.readLine();
			
			ArrayList<Double> m_z = new ArrayList<Double>();		//array to store the m/z values associated with each intensity
			ArrayList<Double> Intensity = new ArrayList<Double>();	//array to store the intensity values associated with each m/z value
			
			do //reads the m/z and intensity values
			{	
				String[] parsedLine = line.split(" ");				//in the text file, this is a space instead of a tab
				m_z.add(Double.parseDouble(parsedLine[0]));			//converts the m/z value from string to double and adds to m_z array
				Intensity.add(Double.parseDouble(parsedLine[1]));	//converts the intensity value from string to double and adds to intensity array
				line = reader.readLine();							//need this line to move the cursor to the next line or else it will keep looping on one line
				
				if(line == null)	//prevents code from crashing if "null" is read
				{
					break; //exits the loop
				}
				
			} while(line.charAt(0) != 'S');	//moves to outer loop when character S is read (S represents start of new spectrum)

			
			//creates object, spectrum, of class (or type) Spectrum that contains all the attributes read from the file
			//associated with the constructor, Spectrum, in the file that created the class Spectrum
			Spectrum spectrum = new Spectrum(ScanNum, PreMZ, RetTime, PreInt, IonInjTime, ActType, PreFile, PreScanNum, InstType, mass2, m_z, Intensity);
			
			ArrSpec.add(spectrum);	//adds the object, spectrum, to the array, ArrSpec
			
		}
		/* once loop restarts, the values in object, spectrum, are overwritten
		 * the previous values of spectrum are not lost because they are stored in the previous cell of the array*/
		
		reader.close(); //closes the reader
		
		//to print the scan number of the first spectrum:
		Spectrum spec1 = ArrSpec.get(0);		//gets the 1st spectrum (object containing attributes) from the array, ArrSpec, and stores as new object, spec1
		double scan_num = spec1.getScanNum();	//calls function, getScanNum, of the instance spec1 of class Spectrum to obtain the private value, ScanNum, and store it in scan_num
		System.out.println("Peptide 1 scan number: " + scan_num);	//prints the scan number of the 1st spectrum
		
		String file_name = spec1.getPreFile();
		System.out.println("Peptide 1 Precursor File: " +file_name);
		
		Spectrum spec2 = ArrSpec.get(1);
		double pre_mz = spec2.getPreMZ();
		System.out.println("Peptide 2 Precursor ion m/z: " + pre_mz);
		
		Spectrum spec3 = ArrSpec.get(2);
		double Ret_Time = spec3.getRetTime();
		System.out.println("Peptide 3 Retention time: " + Ret_Time);
		
		//to print spectrums m/z ratios and associated intensities
		Spectrum spec4 = ArrSpec.get(5); //create a variable, spec4, of class Spectrum to store equal the 4th spectrum (object) stored in cell of index 5 in the array ArrSpec
		ArrayList<Double> MZ = new ArrayList<Double>();	//create an ArrayList of type double containing to hold the m/z values
		MZ = spec4.getm_z();							//gets the actual m/z values from arraylist m_z and stores them in the new MZ arraylist
		ArrayList<Double> Inten = new ArrayList<Double>(); 
		Inten = spec4.getIntensity();
		System.out.println("m/z Ratio:\tIntensity:"); //prints the header for the m/z and intensity values to be printed
		
		for(int i = 0; i < MZ.size(); i++) //index into the arrays, MZ and Inten, that store the m/z values and intensities
		{
			System.out.println(MZ.get(i) + "\t" + Inten.get(i)); //prints the m/z ratio and the associated intensity separated by a tab
		}
		
	}

}
