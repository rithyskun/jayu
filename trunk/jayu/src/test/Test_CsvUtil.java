package test;

import jayu.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class Test_CsvUtil implements TestConst {

	/**
	 *  Top level test case for AsnToCsv Utility.
	 */
	@Test
	public void test_main() { 
		String argStr = TestConst.ALU_GRAMMAR_FILE + " " + 
		                TestConst.ALU_MAPPING_FILE + " " + 
		                TestConst.OUTPUT_DIR + " " + 
		                TestConst.ALU_DATA_FILE;
		
		String[] args = argStr.split("\\s+");
		System.out.printf("\n Testing: Invoking command AsnToCsv(%s) | count = %d", argStr, args.length);				
		CsvUtil.main( args );
		System.out.printf("\n %s written", ALU_CSV_FILE );
	}
	
	/**
	 *  This test case is for ASNUtil.toCSV()
	 *  The AsnToCsv utility internally calls this method for all the work.
	 */
	//@Test
	public void test_Alu() {
		
		System.out.printf("\nCsvUtil.toCSV( %s %s %s %s )",ALU_GRAMMAR_FILE, ALU_MAPPING_FILE , ALU_CSV_FILE, ALU_DATA_FILE  );
		CsvUtil.toCSV(ALU_GRAMMAR_FILE, ALU_MAPPING_FILE , ALU_CSV_FILE, ALU_DATA_FILE );
		System.out.printf("\n %s written", ALU_CSV_FILE );
		
		//TODO:
		//TODO: Write a test case where ALU_MAPPING is not specified 
		//TODO: 
		assertTrue(true);
	}
	
	@Test
	public void test_Eri() {
		System.out.printf("\nCsvUtil.toCSV( %s %s %s %s )",ERI_GRAMMAR_FILE, ERI_MAPPING_FILE , ERI_CSV_FILE, ERI_DATA_FILE );
		CsvUtil.toCSV(ERI_GRAMMAR_FILE, ERI_MAPPING_FILE , ERI_CSV_FILE, ERI_DATA_FILE );
		System.out.printf("\n %s written", ERI_CSV_FILE );
	}
	
	@Test
	public void test_Eri2() {
		
		
		
		System.out.printf("\nCsvUtil.toCSV( %s %s %s %s )",ERI_GRAMMAR_FILE, ERI_MAPPING_FILE , ERI_CSV_FILE, ERI_DATA_FILE );
		CsvUtil.toCSV(ERI_GRAMMAR_FILE, ERI_MAPPING_FILE , ERI_CSV_FILE, ERI_DATA_FILE );
		System.out.printf("\n %s written", ERI_CSV_FILE );
	}
	
	
	

	@Test
	public void test_Hua() {
		System.out.printf("\nCsvUtil.toCSV( %s %s %s %s )",HUA_GRAMMAR_FILE, HUA_MAPPING_FILE , HUA_CSV_FILE, HUA_DATA_FILE );
		CsvUtil.toCSV(HUA_GRAMMAR_FILE, HUA_MAPPING_FILE , HUA_CSV_FILE, HUA_DATA_FILE );
		System.out.printf("\n %s written", HUA_CSV_FILE );
	}

	@Test
	public void test_Zte() {
		System.out.printf("\nCsvUtil.toCSV( %s %s %s %s )",ZTE_GRAMMAR_FILE, ZTE_MAPPING_FILE , ZTE_CSV_FILE, ZTE_DATA_FILE );
		CsvUtil.toCSV(ZTE_GRAMMAR_FILE, ZTE_MAPPING_FILE , ZTE_CSV_FILE, ZTE_DATA_FILE );
		System.out.printf("\n %s written", ZTE_CSV_FILE );
	}

}

class ASCIIFormatAdaptor implements ASCIIFormattable {
	
	/**
	 * class Blah extends ASCIIFormatAdaptor {
	 * 	super("root.moCallDataRecord");
	 * } 
	 *  new ASCIIFormatAdaptor( ConfigFile )
	 *  
	 *   root.uMTSGSMPLMNCallDataRecord.recordType
	 *   root.compositeCallDataRecord.recordType
	 *   
	 *   3 BARRIER OK.. CHECK all Barriers 1 by 1
	 *   
	 *   1st Barrier => root Typically they are always of type [] and sometimes []& 
	 *    
	 *   for all elements in root  # Get rid of []
	 *     if root is []& then getReferencedNode   # Get rid of []&
	 *     
		 *   for( barrier = 1 ; barrier < 2 ) {
		 *   	Get type of barrier, 
		 *      if no barrier break else
		 *      for( int i=0 ; i< barrier
		 *   }
	 *     
	 *   
	 *   
	 * 
	 * 
	 */

	public Node[] nodeToRecords(Node rootNode) {
		// TODO Auto-generated method stub
		return null;
	}

	public String recordToString(Node recordNode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}


