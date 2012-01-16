package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import jayu.*;

public class Test_NodeFactory implements TestConst {
	
	@Test
	public void test_scramble() {
		System.out.printf("\n test_scramble() start =========================");
		
		Node rootNode = null;
		rootNode = NodeFactory.parse (ALU_DATA_FILE, ALU_GRAMMAR_FILE);   		   				
   	    rootNode.scramble();
   	    rootNode.write( new File ( ALU_DATA_FILE+"a") );

		rootNode = NodeFactory.parse (ERI_DATA_FILE, ERI_GRAMMAR_FILE);   		   				
   	    rootNode.scramble();
   	    rootNode.write( new File ( ERI_DATA_FILE+"a") );

		rootNode = NodeFactory.parse (HUA_DATA_FILE, HUA_GRAMMAR_FILE);   		   				
   	    rootNode.scramble();
   	    rootNode.write( new File ( HUA_DATA_FILE+"a") );

		rootNode = NodeFactory.parse (ZTE_DATA_FILE, ZTE_GRAMMAR_FILE);   		   				
   	    rootNode.scramble();
   	    rootNode.write( new File ( ZTE_DATA_FILE+"a") );

		System.out.printf("\n test_scramble() end  =========================");		
	}
	
	@Test
	public void testChaining() {
		System.out.printf("\n testChaining() start =========================");
		
		Node rootNode = NodeFactory.parse (ALU_DATA_FILE, ALU_GRAMMAR_FILE);		
		
		Node callEventRecord = rootNode.subNodes[0];
		// Chaining test
		System.out.printf("\n 	mscIncomingTKGP=(%s)", callEventRecord.getSubNodeChoice().confirmType("MTCallRecord").getSubNode("mscIncomingTKGP")
	                                        .getSubNodeChoice().confirmType("INTEGER").getValue() );

   	    // General traversing test
   	    callEventRecord = rootNode.subNodes[3];   	    
   	    //System.out.printf("\n callEventRecord = (%s)", callEventRecord.confirmType("CallEventRecord"));
		Node mtCallRecord = callEventRecord.getSubNodeChoice().confirmType("MTCallRecord");		
		//System.out.printf("\n mtCallRecord = (%s)", mtCallRecord );		
		
		Node recExtn = mtCallRecord.getSubNode("recordExtensions");		
		Node[] recExtensions = mtCallRecord.getSubNodeAsArray("recordExtensions");
		//System.out.printf("\n recExtensions[0] = (%s)", recExtensions[0] );
		Node[] information = recExtensions[0].getSubNodeAsArray("information");
		//System.out.printf("\n information = (%s)", information[0] );
		Node[] subscribedOSSSCodesNEW = information[0].getSubNodeAsArray("subscribedOSSSCodesNEW");
		//System.out.printf("\n subscribedOSSSCodesNEW = (%s)", subscribedOSSSCodesNEW[0] );
		Node SubscribedOSSSCodes = subscribedOSSSCodesNEW[0];
		System.out.printf("\n SubscribedOSSSCodes = (%s)", SubscribedOSSSCodes  );
   
		System.out.printf("\n testChaining() ends ===============================");
	}
	
	

	//@Test
	public void memoryTestAlu() {
		 System.out.println("\n === About to execute memoryTestAlu() === ");

		// Pre-load the grammar file so that it uses up memory
		ASNClassFactory.getField(ALU_GRAMMAR_FILE);
		System.gc();
		long before = Util.getMemoryUsed();

		Node rootNode = parseAlu();

		long after = Util.getMemoryUsed();
		System.gc();
		after = Util.getMemoryUsed();
		 System.out.printf("\n After gc: before(%d) after(%d)  after-before(%d)",
		 before, after, after-before);
		byte[] arr = Util.FileToByteArray(ALU_DATA_FILE);
		System.out.printf("\n Size of ALU File on disk: %d MB ",
				arr.length / 1024 / 1024);
		System.out.printf("\n Size of ALU Node in Memory: %d MB ",
				(after - before) / 1024 / 1024);
		int numCDRs = rootNode.subNodes.length;
		System.out
				.printf("\n Number of ALU CDRs: %d  | Size/CDR Node: %d KB (%d bytes)\n",
						numCDRs, ((after - before) / numCDRs) / 1024,
						(after - before) / numCDRs);
		System.out.println("\n === Finished memoryTestAlu() === ");
	}

	public Node parseAlu() {

		Node rootNode = NodeFactory.parse(ALU_DATA_FILE, ALU_GRAMMAR_FILE);

		// Meta Data
		assertTrue(rootNode != null);
		assertTrue(rootNode.field.name.equals(Field.ROOTFIELD));
		assertTrue(rootNode.field.longName.equals(Field.ROOTFIELD));
		
		assertTrue(rootNode.field.pos == ASNConst.POS_NOT_SPECIFIED);
		assertTrue(rootNode.field.type.name.equals("CallEventRecord"));
		assertTrue(rootNode.field.isArray() == true);
		assertTrue(rootNode.field.isReference() == true);
		// Data
		assertTrue(rootNode.isPrimitive() == false);
		assertTrue(rootNode.subNodes.length == 4);
		//assertTrue(rootNode.subNodes.length == 6000);
		assertTrue(rootNode.isChoice() == false);
		

		Node callEventRecordNode1 = rootNode.subNodes[0];
		Node callEventRecordNode2 = rootNode.subNodes[2];
		//Node callEventRecordNode5999 = rootNode.subNodes[5999]; // I trimmed the test data to include less records.
		Node callEventRecordNode5999 = rootNode.subNodes[3];

		// Meta Data
		assertTrue(callEventRecordNode1 != null);
		assertTrue(callEventRecordNode1.field.name.equals(Field.ROOTFIELD));
		assertTrue(callEventRecordNode1.field.longName.equals(Field.ROOTFIELD));
		assertTrue(callEventRecordNode1.field.pos == ASNConst.POS_NOT_SPECIFIED);
		assertTrue(callEventRecordNode1.field.type.name.equals("CallEventRecord"));
		assertTrue(callEventRecordNode1.field.isArray() == false);
		assertTrue(callEventRecordNode1.field.isReference() == true);

		// Data
		assertTrue(callEventRecordNode1.isPrimitive() == false);
		assertTrue(callEventRecordNode1.subNodes.length == 1);
		assertTrue(callEventRecordNode1.isChoice() == true);

		// =============================================
		// ============== First CDR ===================
		// =============================================

		Node mtNode = callEventRecordNode1.getSubNodeChoice();

		// Meta Data
		assertTrue(mtNode != null);
		assertTrue(mtNode.field.name.equals("mtCallRecord"));
		assertTrue(mtNode.field.longName.equals(Field.ROOTFIELD
				+ ".mtCallRecord"));
		assertTrue(mtNode.field.pos == 1);
		assertTrue(mtNode.field.type.name.equals("MTCallRecord"));
		assertTrue(mtNode.field.isArray() == false);
		assertTrue(mtNode.field.isReference() == false);

		// Data
		assertTrue(mtNode.isPrimitive() == false);
		assertTrue(mtNode.subNodes.length == 25);
		assertTrue(mtNode.isChoice() == false);

		Node mt_RecordTypeNode = mtNode.subNodes[0];
		Node mt_ServedImsiNode = mtNode.subNodes[1];
		Node mt_mscIncomingTKGP = mtNode.subNodes[6];
		Node mt_systemType = mtNode.subNodes[24];

		// Pos 0 | 1st Field in MT (leaf node)
		assertTrue(mt_RecordTypeNode.field.longName.equals(Field.ROOTFIELD
				+ ".mtCallRecord.recordType"));
		assertTrue(mt_RecordTypeNode.field.pos == 0);
		assertTrue(mt_RecordTypeNode.field.type.name.equals("INTEGER")
				&& mt_RecordTypeNode.field.type.isPrimitive() == true);
		assertTrue(mt_RecordTypeNode.field.isArray() == false
				&& mtNode.field.isReference() == false);
		assertTrue(mt_RecordTypeNode.getValue().intValue == 1);

		// Pos 1 | 2nd Node in MT (leaf node)
		assertTrue(mt_ServedImsiNode.field.longName.equals(Field.ROOTFIELD
				+ ".mtCallRecord.servedIMSI"));
		assertTrue(mt_ServedImsiNode.field.pos == 1);
		assertTrue(mt_ServedImsiNode.field.type.name.equals("OCTET STRING")
				&& mt_ServedImsiNode.field.type.isPrimitive() == true);
		assertTrue(mt_ServedImsiNode.field.isArray() == false
				&& mtNode.field.isReference() == false);
		assertTrue(mt_ServedImsiNode.getValue().toString()
				.equals("405878122293893f"));

		// Pos 7 | 7th Node in MT (composite node)
		assertTrue(mt_mscIncomingTKGP.field.longName.equals(Field.ROOTFIELD
				+ ".mtCallRecord.mscIncomingTKGP"));
		assertTrue(mt_mscIncomingTKGP.field.pos == 7);
		assertTrue(mt_mscIncomingTKGP.field.type.name.equals("TrunkGroup")
				&& mt_mscIncomingTKGP.field.type.isPrimitive() == false);
		assertTrue(mt_mscIncomingTKGP.field.isArray() == false
				&& mt_mscIncomingTKGP.field.isReference() == true);
		assertTrue(mt_mscIncomingTKGP.subNodes.length == 1);

		// 61st Field | 25th subNode in MT (leaf node)
		assertTrue(mt_systemType.field.longName.equals(Field.ROOTFIELD
				+ ".mtCallRecord.systemType"));
		assertTrue(mt_systemType.field.pos == 61);
		assertTrue(mt_systemType.field.type.name.equals("ENUMERATED")
				&& mt_systemType.field.type.isPrimitive() == true);
		assertTrue(mt_systemType.field.isArray() == false
				&& mtNode.field.isReference() == false);
		assertTrue(mt_systemType.getValue().intValue == 2);

		// =============================================
		// =============== Second CDR =================
		// =============================================
		Node moNode = callEventRecordNode2.getSubNodeChoice();

		// Meta Data
		assertTrue(moNode != null);
		assertTrue(moNode.field.name.equals("moCallRecord"));
		assertTrue(moNode.field.longName.equals(Field.ROOTFIELD
				+ ".moCallRecord"));
		assertTrue(moNode.field.pos == 0);
		assertTrue(moNode.field.type.name.equals("MOCallRecord"));
		assertTrue(moNode.field.isArray() == false);
		assertTrue(moNode.field.isReference() == false);

		// Data
		assertTrue(moNode.isPrimitive() == false);
		assertTrue(moNode.subNodes.length == 32);
		assertTrue(moNode.isChoice() == false);

		return rootNode;
	}

	//@Test
	public void memoryTestEri() {
		System.out.println("\n === About to execute memoryTestEri() === ");

		// Pre-load the grammar file so that it uses up memory
		ASNClassFactory.getField(ERI_GRAMMAR_FILE);
		System.gc();
		long before = Util.getMemoryUsed();

		Node rootNode = NodeFactory.parse(ERI_DATA_FILE, ERI_GRAMMAR_FILE);

		long after = Util.getMemoryUsed();

		System.gc();
		after = Util.getMemoryUsed();
		System.out.printf("\n Size of ERI Node in Memory: %d MB | %d bytes",
				(after - before) / 1024 / 1024, (after - before));
		System.out.printf("\n Size of ERI Node in Memory: %d MB ",
				(after - before) / 1024 / 1024);
		int numCDRs = rootNode.subNodes.length;
		System.out.printf("\n %s file : %d  rootNode.subnodes.length", ERI_DATA_FILE, numCDRs);
	}

	//@Test
	public void printEriData() {
		byte[] arr = Util.FileToByteArray(ERI_DATA_FILE);
		System.out.printf("\n Size of ERI File on disk: %d MB ",
				arr.length / 1024 / 1024);
	}

	//@Test
	public void testHua() {
		System.out.println("\n === About to execute testHua() === ");
		Node rootNode = NodeFactory.parse(HUA_DATA_FILE, HUA_GRAMMAR_FILE);

	}

}
