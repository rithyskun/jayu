package jayu;

import java.io.File;
import java.util.*;

/**
 *  Converts a ASNData to CSV file. 
 *  Requires ASNGrammar file and config file describing CSV format. 
 */
public class CsvUtil {
	
	static Map<String,ASCIIFormattable> mapOfFormatObjects = new HashMap<String,ASCIIFormattable>();
	
	/**
	 * Converts ASNData to CSV data, caches grammarFile and CSV formating data so that subsequent 
	 * calls to this method return faster for the same grammar file. 
	 *  
	 * @param inputGrammarFile - INput grammar File
	 * @param javaConfigFile - optional fully qualified Java class name of ASCIIFormattable interface
	 * If javaconfigFile is null, then inputGrammarFile should be on classpath along with java config file. 
	 * @param outputCSVFile - the output csv to be generated.
 	 * @param inputDataFile - Input Data file	 
 	 * */
	
	public static void toCSV(String inputGrammarFile, String configJavaClass, String outputCSVFile , String inputDataFile ) {
		
		Node rootNode = NodeFactory.parse(inputDataFile, inputGrammarFile);
		if ( configJavaClass == null ) {
			configJavaClass = getJavaConfigClass( inputGrammarFile );
		}
		ASCIIFormattable af = null;
		if( mapOfFormatObjects.containsKey( configJavaClass ) ) {
			af = (ASCIIFormattable) mapOfFormatObjects.get( configJavaClass );
		} else {
			af = (ASCIIFormattable) ConfigurableCode.loadClass(configJavaClass);
			mapOfFormatObjects.put(configJavaClass, af );
		}
		rootNode.toASCII( af , outputCSVFile );
	}

	// Assumes grammar file is directly on classpath and not kept under nested packages
	// Get the file name without the directory path first then
	// Removes file extension if any in the grammar file.
	public static String getJavaConfigClass(String inputGrammarFile) {
		// TODO Auto-generated method stub
		return "test.testdata.Alcatel";
	}
	
	  public static void validateAndConvertToCsv(String[] args) {
			String wkDir = System.getProperty("user.dir");
			//System.out.printf("\n working Dir = %s", wkDir );
			
			String grammarFileStr = args[0];
			String mappingFileStr = args[1];
			String outputDirStr = args[2];
			String firstFileStr = args[3];
			
			 File grammarFile = new File( grammarFileStr );
			 if( !grammarFile.isFile() ) {
				 String s = "\n Input grammar file param ("+ grammarFileStr + ") is not a valid file on disk";
				 throw new ASNException(s);
			 } else {
				 //System.out.printf("\n Input grammar file param ("+ grammarFileStr + ") ... OK ");
			 }

			 /*File mappingFile = new File( mappingFileStr );
			 if( !mappingFile.isFile() ) {
					 String s = "\n Input mapping file param ("+ mappingFileStr + ") is not a valid file on disk";
					 throw new ASNException(s);				 
			 } else {
					 System.out.printf("\n Input mapping file param ("+ mappingFileStr + ") ... OK ");
			 }*/
			
			 File outputDir = new File( outputDirStr );
			 if( !outputDir.isDirectory() ) {
				 String s = "\n Input output directory param ("+ outputDirStr + ") is not a valid directory on disk.";
				 throw new ASNException(s);
			 } else {
				 //System.out.printf("\n Input output directory param (" +outputDirStr+ ") is OK ");
			 } 

			 for( int i=3;i<args.length;i++ ) {
				 File inputFile = new File( args[i] );
				 String csvFile = null;
				 if( !inputFile.isFile() ) {
						 String s = "\n Input param ("+ args[i] + ") is not a valid file on disk";
						 throw new ASNException(s);
				 } else {
						// System.out.printf("\n Input param ("+ args[i] + ") OK ");
						csvFile = outputDir.getAbsolutePath() + File.separator + inputFile.getName() + ".csv";
						 //System.out.printf("\n Constructed OutputFile: ("+ csvFile +")" );
				 }		

				 CsvUtil.toCSV( grammarFile.getAbsolutePath(), // Grammar File						 	      
				 				   mappingFileStr,                // Java File
						           csvFile,                        // Output File
						           inputFile.getAbsolutePath()   // Data File input
						          );
			 }		
		}

			public static void main(String[] args) {
				if( args.length < 4 ) {
					System.out.printf("\nUsage: asn2csv {grammarFile} {mapFile} {outputDir} [datafile1] [datafile2].. ");
					return;
				}		
				validateAndConvertToCsv( args );		
			}

}
