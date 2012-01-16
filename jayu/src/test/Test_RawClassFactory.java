package test;

import static org.junit.Assert.*;
import org.junit.Test;

import jayu.*;

public class Test_RawClassFactory implements TestConst {

	@Test
	public void getRawClassAlu() {

		/**
		 * CallEventRecord ::= CHOICE { moCallRecord [0] MOCallRecord, ...9
		 * fields here... termCAMELRecord [16] TermCAMELRecord }
		 */

		RawClass rc = RawClassFactory.getRawClass(ALU_GRAMMAR_FILE,
				"CallEventRecord");
		// MLFL
		assertTrue(rc.className.equals("CallEventRecord"));
		assertTrue(rc.fields != null && rc.fields.length == 11);
		assertTrue(rc.fileName.equals(ALU_GRAMMAR_FILE)
				&& rc.lineNumber == 9 - 1);
		assertTrue(rc.relation.equals(ASNConst.RELATION_CHOICE));

		// First Field
		assertTrue(rc.fields[0].equals("moCallRecord"));
		assertTrue(rc.pos[0] == 0);
		assertTrue(rc.type[0].equals("MOCallRecord"));

		// Last Field
		assertTrue(rc.fields[10].equals("termCAMELRecord"));
		assertTrue(rc.pos[10] == 16);
		assertTrue(rc.type[10].equals("TermCAMELRecord"));
	}

	@Test
	public void getRawClassAlu_NoPos_array() {
		/**
		 * MOCAlcatelManagedExtension ::= SEQUENCE { identifier OBJECT
		 * IDENTIFIER, information [2] SET OF MOCallRecordExtensionType }
		 */
		RawClass rc = RawClassFactory.getRawClass(ALU_GRAMMAR_FILE,
				"MOCAlcatelManagedExtension");
		// System.out.printf("%s", rc);

		// MLFL
		assertTrue(rc.className.equals("MOCAlcatelManagedExtension"));
		assertTrue(rc.fields != null && rc.fields.length == 2);
		assertTrue(rc.fileName.equals(ALU_GRAMMAR_FILE)
				&& rc.lineNumber == 81 - 1);
		assertTrue(rc.relation.equals(ASNConst.RELATION_SEQUENCE));

		// First Field
		assertTrue(rc.fields[0].equals("identifier"));
		assertTrue(rc.pos[0] == ASNConst.POS_NOT_SPECIFIED);
		assertTrue(rc.type[0].equals("OBJECT IDENTIFIER"));
		assertTrue(rc.arrInfo[0].isArray() == false);

		// Last Field
		assertTrue(rc.fields[1].equals("information"));
		assertTrue(rc.pos[1] == 2);
		assertTrue(rc.type[1].equals("MOCallRecordExtensionType"));
		assertTrue(rc.arrInfo[1].isArray() == true);
	}

	@Test
	public void getRawClassAlu_SingleLiner() {
		// SubscribedOSSSCodes ::= OCTET STRING
		RawClass rc = RawClassFactory.getRawClass(ALU_GRAMMAR_FILE,
				"SubscribedOSSSCodes");

		assertTrue(rc.className.equals("SubscribedOSSSCodes"));
		assertTrue(rc.relation.equals(RawClass.RELATION_NIL));
		assertTrue(rc.synonymn.equals("OCTET STRING"));
		assertTrue(rc.pos == null && rc.type == null && rc.arrInfo == null);

		rc = RawClassFactory.getRawClass(ALU_GRAMMAR_FILE, "BasicServices");
		// BasicServices ::= SET OF BasicServiceCode
		assertTrue(rc.className.equals("BasicServices"));
		assertTrue(rc.relation.equals(RawClass.RELATION_SET_OF));
		assertTrue(rc.synonymn.equals("BasicServiceCode"));
		assertTrue(rc.pos == null && rc.type == null && rc.arrInfo == null);
	}

	@Test
	public void getRawClassEricson() {
		/**
		 * MSOriginating ::= SET { tAC [0] IMPLICIT TAC OPTIONAL, ... fields
		 * here... iuCodec [153] IMPLICIT IuCodec OPTIONAL } }
		 */

		RawClass rc = RawClassFactory.getRawClass(ERI_GRAMMAR_FILE,
				"MSOriginating");
		// System.out.printf("%s", rc);
		// MLFL
		assertTrue(rc.className.equals("MSOriginating"));
		assertTrue(rc.fields != null && rc.fields.length == 154);
		assertTrue(rc.fileName.equals(ERI_GRAMMAR_FILE)
				&& rc.lineNumber == 164 - 1);
		assertTrue(rc.relation.equals(ASNConst.RELATION_SET));

		// First Field
		assertTrue(rc.fields[0].equals("tAC"));
		assertTrue(rc.pos[0] == 0);
		assertTrue(rc.type[0].equals("TAC"));

		// Last Field
		assertTrue(rc.fields[153].equals("iuCodec"));
		assertTrue(rc.pos[153] == 153);
		assertTrue(rc.type[153].equals("IuCodec"));

		// ------------------
	}

	@Test
	public void getRootClass() {
		assertTrue(RawClassFactory.getRootClassName(ALU_GRAMMAR_FILE).equals(
				"CallEventRecord"));
		assertTrue(RawClassFactory.getRootClassName(ERI_GRAMMAR_FILE).equals(
				"CallDataRecord"));
	}

	@Test
	public void getRawClass_Alu_ENUM() {
		/**
		 * InterfaceType ::= ENUMERATED { unknown (0), tDM-T1 (1), .... }
		 */
		RawClass rc = RawClassFactory.getRawClass(ALU_GRAMMAR_FILE,
				"InterfaceType");

		// System.out.printf("%s", rc);

		assertTrue(rc.className.equals("InterfaceType"));
		assertTrue(rc.singleLiner == true);
		assertTrue(rc.relation.equals(RawClass.RELATION_NIL));
		assertTrue(rc.synonymn.equals("ENUMERATED"));
		assertTrue(rc.pos == null && rc.type == null && rc.arrInfo == null);
	}

	@Test
	public void tmpTest() {
		RawClass rc = RawClassFactory.getRawClass(ERI_GRAMMAR_FILE,
				"INServiceDataEventModule");

		// System.out.printf("\n tmpTest\n(%s)", rc);
	}

}
