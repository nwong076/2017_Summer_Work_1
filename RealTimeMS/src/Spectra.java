/*
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.Node;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.Scan;
import uk.ac.ebi.jmzml.model.mzml.ScanList;
import uk.ac.ebi.jmzml.model.mzml.ScanWindowList;
import uk.ac.ebi.jmzml.model.mzml.Spectrum;
import uk.ac.ebi.jmzml.xml.io.MzMLObjectIterator;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;
*/

/*Used when reading the mzML file 
 *Class Spectra contains all the attributes of each individual spectrum
 *An array of objects, spectrum, of class Spectra will be created to store each spectrum and its attributes
 */

public class Spectra 
{
	//Each attribute is private so that other programs cannot change their values

	/*From first line of <spectrum> in the mzML file*/
	private double index; // spectrum.getIndex()
	private int scanNum; //spectrum.getScan()

	/*--> spectrum*/
	// private ? MS1; //cvParams.get(0).value()
	private int msLevel; // cvParams.get(1).value()
	// private ? positiveScan; //cvParams.get(2).value()
	// private ? profileSpec; //cvParams.get(3).value()
	private double base_mz; // cvParams.get(4).value()
	private double base_intens; // cvParams.get(5).value()
	private double tot_curr; // cvParams.get(6).value()
	private double low_mz; // cvParams.get(7).value()
	private double high_mz; // cvParams.get(8).value()

	/*--> scan --> ScanList --> spectrum*/
	private double start_t; // cvParams.get(0)
	private String filter; // cvParams.get(1)
	private double scan_config; // cvParams.get(2)
	private double inject_t; // cvParams.get(3)

	/*--> scanWindow --> scanWindowList --> scan --> ScanList --> spectrum*/
	private double scan_lower; // cvParams.get(0).value()
	private double scan_upper; // cvParams.get(1).value()

	//Constructor used when creating a new object of type Spectra (associates the object's variables with those of the class)
	public Spectra(double index2, int scanNum2, int msLevel2, double base_mz2, double base_intens2, double tot_curr2,
			double low_mz2, double high_mz2, double start_t2, String filter2, double scan_config2, double inject_t2,
			double scan_lower2, double scan_upper2) 
	{
		index = index2;
		scanNum = scanNum2;
		msLevel = msLevel2;
		base_mz = base_mz2;
		base_intens = base_intens2;
		tot_curr = tot_curr2;
		low_mz = low_mz2;
		high_mz = high_mz2;
		start_t = start_t2;
		filter = filter2;
		scan_config = scan_config2;
		inject_t = inject_t2;
		scan_lower = scan_lower2;
		scan_upper = scan_upper2;

	}
	
	//Functions to access the private values from class Spectra
	public double getindex() 
	{
		return index;
	}

	public int getscanNum()
	{
		return scanNum;
	}
	
	public int getmsLevel() 
	{
		return msLevel;
	}

	public double getbase_mz() 
	{
		return base_mz;
	}

	public double getbase_intens() 
	{
		return base_intens;
	}

	public double gettot_curr() 
	{
		return tot_curr;
	}

	public double getlow_mz() 
	{
		return low_mz;
	}

	public double gethigh_mz() 
	{
		return high_mz;
	}

	public double getstart_t() 
	{
		return start_t;
	}

	public String getfilter() 
	{
		return filter;
	}

	public double getscan_config() 
	{
		return scan_config;
	}

	public double getinject_t() 
	{
		return inject_t;
	}

	public double getscan_lower() 
	{
		return scan_lower;
	}

	public double getscan_upper() 
	{
		return scan_upper;
	}

} //End of class Spectra


