package jayu;

import java.io.File;


public class AsnToCsv {

  public static void convertToCsv(String[] args) {
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
				 	       inputFile.getAbsolutePath(),   // Data File input
		 				   mappingFileStr,                // Java File
				           csvFile                        // Output File				            
				          );
	 }		
}

	public static void main(String[] args) {
		if( args.length < 4 ) {
			System.out.printf("\nUsage: ASNToCSV {grammarFile} {mapFile} {outputDir} [datafile1] [datafile2].. ");
			return;
		}		
		convertToCsv( args );		
	}
}
