package MS2_txt_11May2017;

import java.util.ArrayList; //library to be able to use ArrayLists

public class Spectrum //overall Spectrum class that contains attributes of a peptide spectrum 
{
	private double ScanNum; 	//scan number of 
	private double PreMZ; 		//mass-to-charge ratio of precursor ion
	private double RetTime; 	//retention time (mins)
	private double PreInt; 		//precursor ion intensity
	private double IonInjTime; 	//precursor ion injection time (secs)
	private String ActType; 	//fragmentation technique (activation type)
	private String PreFile;		//file name of precursor ion
	private double PreScanNum;	//scan number of precursor ion
	private String InstType; 	//instrument type
	private double mass2; 		//mass of peptide at charge state 2
	
	private ArrayList<Double> m_z; //recorded mass to charge ratio
	private ArrayList<Double> Intensity; //recorded intensity 

	/*constructor is a special type of method that is used to initialize the state of an object
	 * it's called when an instance of an object is created
	 * to associate the variables when calling an object (spectrum)
	 * constructor variables take the same name as the class
	 * don't need a return type like int or double */
	public Spectrum(double _scanNum, double _PreMZ, double _RetTime, double _PreInt, double _IonInjTime, String _ActType, String _PreFile, double _PreScanNum, String _InstType, double _mass2, ArrayList<Double> _mz, ArrayList<Double> _Intensity)
	{
		ScanNum = _scanNum;
		PreMZ = _PreMZ;
		RetTime = _RetTime;
		PreInt = _PreInt;
		IonInjTime = _IonInjTime;
		ActType = _ActType;
		PreFile = _PreFile;
		PreScanNum = _PreScanNum;
		InstType = _InstType;
		mass2 = _mass2;
		m_z = new ArrayList<Double>();	//creates a new separate array from _mz so that the m/z values are not overwritten
		m_z.addAll(_mz);				//adds all the values from _mz to m_z
		Intensity = new ArrayList<Double>();
		Intensity.addAll(_Intensity);
	}
	
	//functions to get the private values from class Spectrum (returns the private values so they can be used
	public double getScanNum()	
	{
		return ScanNum;
	}
	
	public double getPreMZ()	
	{
		return PreMZ;
	}
	
	public double getRetTime()	
	{
		return RetTime;
	}
	
	public double getPreInt()	
	{
		return PreInt;
	}
	
	public double getIonInjTime()	
	{
		return IonInjTime;
	}
	
	public String getActType()	
	{
		return ActType;
	}
	
	public String getPreFile()	
	{
		return PreFile;
	}
	
	public double getPreScanNum()	
	{
		return PreScanNum;
	}
	
	public String getInstType()	
	{
		return InstType;
	}
	
	public double getmass2()	
	{
		return mass2;
	}
	
	public ArrayList<Double> getm_z()	
	{
		return m_z;
	}
	
	public ArrayList<Double> getIntensity()	
	{
		return Intensity;
	}
	
}
