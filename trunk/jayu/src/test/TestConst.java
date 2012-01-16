package test;

public interface TestConst {

	final static String ALU_GRAMMAR_FILE = "test\\testdata\\Alcatel.txt";
	final static String ERI_GRAMMAR_FILE = "test\\testdata\\Ericsson.txt";
	final static String HUA_GRAMMAR_FILE = "test\\testdata\\Huawie.txt";
	final static String ZTE_GRAMMAR_FILE = "test\\testdata\\Zte.txt";

	final static String ALU_DATA_FILE = "test\\testdata\\alu.dat";
	final static String ERI_DATA_FILE = "test\\testdata\\eri.dat";
	final static String HUA_DATA_FILE = "test\\testdata\\hua.dat";
	final static String ZTE_DATA_FILE = "test\\testdata\\zte.dat";

	final static String ALU_CSV_FILE = "tmp\\alu_csv.txt";
	final static String ERI_CSV_FILE = "tmp\\eri_csv.txt";
	final static String HUA_CSV_FILE = "tmp\\hua_csv.txt";
	final static String ZTE_CSV_FILE = "tmp\\zte_csv.txt";

	// Note - .java extension is intentionally missing. The file Alcatel.java should be on classpath
	// In this case the test directory should be on classpath
	final static String ALU_MAPPING_FILE = "test.testdata.Alcatel"; //
	final static String ERI_MAPPING_FILE = "test.testdata.Ericsson"; //
	final static String HUA_MAPPING_FILE = "test.testdata.Huawie"; //
	final static String ZTE_MAPPING_FILE = "test.testdata.Zte";
	
	final static String OUTPUT_DIR = "tmp\\";
	final static String ALU_CSV_SHORTNAME = "alu_csv.txt";
	final static String ERI_CSV_SHORTNAME = "eri_csv.txt";
	final static String HUA_CSV_SHORTNAME = "hua_csv.txt";
	final static String ZTE_CSV_SHORTNAME = "zte_csv.txt";
		
	final static String ERROR1_ERI_GRAMMAR_FILE = "src\\test\\testdata\\Error1_Ericsson.txt";


	

}