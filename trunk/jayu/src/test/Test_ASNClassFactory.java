package test;

import java.util.Map;

import jayu.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class Test_ASNClassFactory implements TestConst {
	
	// @Test
	public void testAlu() {
		Field rootField = ASNClassFactory.getField(ALU_GRAMMAR_FILE);
		ASNClass asnClass = rootField.type;

		// System.out.printf("\n Alu ASNClass = \n %s",
		// asnClass.toStringTree(0));

		assertTrue(asnClass.name.equals("CallEventRecord"));
		assertTrue(asnClass.isArray() == true);
		assertTrue(asnClass.isReference() == true);
		assertTrue(asnClass.fields.length == 11);
		assertTrue(asnClass.fields[0].name.equals("moCallRecord"));
		assertTrue(asnClass.fields[10].name.equals("termCAMELRecord"));
		assertTrue(asnClass.fields[0].longName.equals(Field.ROOTFIELD
				+ ".moCallRecord"));
		assertTrue(asnClass.fields[10].longName.equals(Field.ROOTFIELD
				+ ".termCAMELRecord"));

		Field firstField = asnClass.fields[0];

		assertTrue(firstField.type.name.equals("MOCallRecord"));
		assertTrue(firstField.type.isArray() == false);
		assertTrue(firstField.type.isReference() == false);
		assertTrue(firstField.type.isSet() == true);
		assertTrue(firstField.type.fields.length == 53);
		assertTrue(firstField.type.fields[0].name.equals("recordType"));
		assertTrue(firstField.type.fields[52].name.equals("maximumBitRate"));
		assertTrue(firstField.type.fields[0].longName.equals(Field.ROOTFIELD
				+ ".moCallRecord.recordType"));
		assertTrue(firstField.type.fields[52].longName.equals(Field.ROOTFIELD
				+ ".moCallRecord.maximumBitRate"));

		Field lastField = asnClass.fields[10];
		assertTrue(lastField.type.name.equals("TermCAMELRecord"));
		assertTrue(lastField.type.isArray() == false);
		assertTrue(lastField.type.isReference() == false);
		assertTrue(lastField.type.isSet() == true);
		assertTrue(lastField.type.fields.length == 36);
		assertTrue(lastField.type.fields[0].name.equals("recordtype"));
		assertTrue(lastField.type.fields[35].name.equals("lrnQuryStatus"));
		assertTrue(lastField.type.fields[0].longName.equals(Field.ROOTFIELD
				+ ".termCAMELRecord.recordtype"));
		assertTrue(lastField.type.fields[35].longName.equals(Field.ROOTFIELD
				+ ".termCAMELRecord.lrnQuryStatus"));

		Field subField = firstField.type.fields[12];
		assertTrue(subField.name.equals("changeOfLocation"));
		assertTrue(subField.type.name.equals("LocationChange"));
		assertTrue(subField.pos == 13);
		assertTrue(subField.type.isArray() == true);
		assertTrue(subField.type.isReference() == false);
		assertTrue(subField.type.isSequence() == false);
		assertTrue(subField.type.isSet() == false);
	}

	// @Test
	public void testEri() {
		Field rootField = ASNClassFactory.getField(ERI_GRAMMAR_FILE);
		ASNClass asnClass = rootField.type;

		// System.out.printf("\n ERIASNClass = \n %s",
		// asnClass.toStringTree(0));

		assertTrue(asnClass.name.equals("CallDataRecord"));
		assertTrue(asnClass.isArray() == true);
		assertTrue(asnClass.isReference() == true);
		assertTrue(asnClass.fields.length == 2);
		assertTrue(asnClass.fields[0].name.equals("uMTSGSMPLMNCallDataRecord"));
		assertTrue(asnClass.fields[1].name.equals("compositeCallDataRecord"));
		assertTrue(asnClass.fields[0].longName.equals(Field.ROOTFIELD
				+ ".uMTSGSMPLMNCallDataRecord"));
		assertTrue(asnClass.fields[1].longName.equals(Field.ROOTFIELD
				+ ".compositeCallDataRecord"));

		Field firstField = asnClass.fields[0];

		assertTrue(firstField.type.name.equals("UMTSGSMPLMNCallDataRecord"));
		assertTrue(firstField.type.isArray() == false);
		assertTrue(firstField.type.isReference() == false);
		assertTrue(firstField.type.isSequence() == true);

		assertTrue(firstField.type.fields.length == 2);
		assertTrue(firstField.type.fields[0].name.equals("recordType"));
		assertTrue(firstField.type.fields[1].name.equals("eventModule"));
		assertTrue(firstField.type.fields[0].longName.equals(Field.ROOTFIELD
				+ ".uMTSGSMPLMNCallDataRecord.recordType"));
		assertTrue(firstField.type.fields[1].longName.equals(Field.ROOTFIELD
				+ ".uMTSGSMPLMNCallDataRecord.eventModule"));

		Field lastField = asnClass.fields[1];

		assertTrue(lastField.type.name.equals("UMTSGSMPLMNCallDataRecord"));
		assertTrue(lastField.type.isArray() == true);
		assertTrue(lastField.type.isReference() == false);
		assertTrue(lastField.type.isSequence() == false);

		assertTrue(lastField.type.fields.length == 2);
		assertTrue(lastField.type.fields[0].name.equals("recordType"));
		assertTrue(lastField.type.fields[1].name.equals("eventModule"));
		assertTrue(lastField.type.fields[0].longName.equals(Field.ROOTFIELD
				+ ".compositeCallDataRecord.recordType"));
		assertTrue(lastField.type.fields[1].longName.equals(Field.ROOTFIELD
				+ ".compositeCallDataRecord.eventModule"));

	}

	// @Test
	public void test_ErroneousEri() {
		boolean exceptionThrown = true;
		try {
			Field rootField = ASNClassFactory.getField(ERROR1_ERI_GRAMMAR_FILE);
			ASNClass asnClass = rootField.type;

			exceptionThrown = false;

		} catch (ASNException e) {
			// System.out.printf("\n ASNException: %s", e.getMessage() );
			assertTrue(exceptionThrown == true);
		}
		assertTrue(exceptionThrown == true);
	}

	@Test
	public void test_Zte() {
		System.out.printf("\n test_Zte() called ");
		/*Field rootField = ASNClassFactory.getField(ZTE_GRAMMAR_FILE);
		ASNClass rootASNClass = rootField.type;
		System.out.printf("\n rootASNClass = %s" , rootASNClass.getName() );
		ASNClass innerClass = rootASNClass.fields[0].type;
		System.out.printf("\n innerASNClass = %s" , innerClass.getName() );
		
		rootASNClass.blockSize = 2048;
		rootASNClass.paddingByte = (byte) 0xFF;
		byte b = (byte) 0xFF;
		System.out.printf("\n blockSize = %d paddingByte = 0x%X " , rootASNClass.blockSize, rootASNClass.paddingByte );
		
		byte[] byteArr = Util.FileToByteArray( ZTE_DATA_FILE );
		EBlock rootBlock = new EBlock(0,0,byteArr,0,0,byteArr.length);
		Node rootNode = NodeFactory.makeNode( rootField, rootBlock, new FilterClass(),-1, 0 );
		
		System.out.printf("rootNode has %d subNodes", rootNode.subNodes.length );

		Map<String,String> mapOptions = ASNClassFactory.loadControlFile(ZTE_GRAMMAR_FILE);
		
		System.out.printf("\n Number of Options = %d", mapOptions.size() );
		for (Map.Entry<String, String> entry : mapOptions.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    System.out.printf("\n key(%s) = value(%s) ",key,value);
		    
		    // ...
		}
		int blocksize = 0;
		byte paddingByte = 0;		
		final String OPTION_BLOCKSIZE = "BLOCKSIZE";
		final String OPTION_PADDINGBYTE = "PADDINGBYTE";
		
		if( mapOptions.containsKey(OPTION_BLOCKSIZE)) {
			blocksize = Integer.parseInt( mapOptions.get(OPTION_BLOCKSIZE) );
		}
		if( mapOptions.containsKey( OPTION_PADDINGBYTE )) {
			paddingByte = (byte) Integer.parseInt( mapOptions.get(OPTION_PADDINGBYTE),16 );
		}
	    System.out.printf("\n valueint(%s) valuebyte(%s) ", blocksize, paddingByte );
		*/
	
		Node rootNode = NodeFactory.parse(ZTE_DATA_FILE, ZTE_GRAMMAR_FILE);

	}

}
