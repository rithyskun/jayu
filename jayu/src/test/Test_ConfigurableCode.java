package test;


import jayu.*;

import static org.junit.Assert.*;
import org.junit.Test;

import org.codehaus.janino.*;
import org.codehaus.janino.util.resource.*;


public class Test_ConfigurableCode implements TestConst {

	public static void main(String args[]) {
	      org.junit.runner.JUnitCore.main("test.Test_ConfigurableCode");
	}


	@Test
	public void testAlu() {		
		//System.out.printf("\n CWD = (%s) ",System.getProperty("user.dir"));		
		Node rootNode = NodeFactory.parse(ALU_DATA_FILE, ALU_GRAMMAR_FILE);
		ASCIIFormattable af = (ASCIIFormattable) ConfigurableCode.loadClass(ALU_MAPPING_FILE);
		rootNode.toASCII(new AluMapper(), ALU_CSV_FILE);
		
		System.out.printf("\n CSV File %s written ", ALU_CSV_FILE );
	}

	public void testEri() {		
		//System.out.printf("\n CWD = (%s) ",System.getProperty("user.dir"));		
		Node rootNode = NodeFactory.parse(ERI_DATA_FILE, ERI_GRAMMAR_FILE);
		ASCIIFormattable af = (ASCIIFormattable) ConfigurableCode.loadClass(ERI_MAPPING_FILE);
		rootNode.toASCII(new AluMapper(), ERI_CSV_FILE);
		
		System.out.printf("\n CSV File %s written ", ERI_CSV_FILE );
	}
	
	@Test
	public void testHua() {		
		//System.out.printf("\n CWD = (%s) ",System.getProperty("user.dir"));		
		Node rootNode = NodeFactory.parse(HUA_DATA_FILE, HUA_GRAMMAR_FILE);
		ASCIIFormattable af = (ASCIIFormattable) ConfigurableCode.loadClass(HUA_MAPPING_FILE);
		rootNode.toASCII(new HuaMapper(), HUA_CSV_FILE);
		
		System.out.printf("\n CSV File %s written ", HUA_CSV_FILE );
	}
	
}
