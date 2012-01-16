package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import jayu.*;

public class Test_ASCIIFormattable implements TestConst {
	
	public static void main(String args[]) {
	      org.junit.runner.JUnitCore.main("test.Test_ASCIIFormattable");
	}

	@Test
	public void testAlu() {
		System.out.printf("\n Running Test_ASCIIFormattable");
		Node rootNode = NodeFactory.parse(ALU_DATA_FILE, ALU_GRAMMAR_FILE);
		rootNode.toASCII(new AluMapper(), ALU_CSV_FILE);

		System.out.printf("\n CSV File %s written ", ALU_CSV_FILE);
	}

	//@Test
	public void testEri() {
		Node rootNode = NodeFactory.parse(ERI_DATA_FILE, ERI_GRAMMAR_FILE);
		rootNode.toASCII(new EriMapper(), ERI_CSV_FILE);
		System.out.printf("\n CSV File %s written ", ERI_CSV_FILE);
	}

	@Test
	public void testHua() {
		Node rootNode = NodeFactory.parse(HUA_DATA_FILE, HUA_GRAMMAR_FILE);
		rootNode.toASCII(new HuaMapper(), HUA_CSV_FILE);
		System.out.printf("\n CSV File %s written ", HUA_CSV_FILE);
	}

	//@Test
	public void testZte() {
		Node rootNode = NodeFactory.parse(ZTE_DATA_FILE, ZTE_GRAMMAR_FILE);
		rootNode.toASCII(new ZteMapper(), ZTE_CSV_FILE);
		System.out.printf("\n CSV File %s written ", ZTE_CSV_FILE);
	}

	
}

class EriMapper implements ASCIIFormattable {
	public Node[] nodeToRecords(Node rootNode) {
		ArrayList<Node> retList = new ArrayList<Node>();
		for (int i = 0; i < rootNode.subNodes.length; i++) {
			Node n = rootNode.subNodes[i]; // root CallDataRecord&
			n = n.getSubNodeChoice(); // Type should be
										// uMTSGSMPLMNCallDataRecord OR
										// uMTSGSMPLMNCallDataRecord[]

			if (n.isArray()) { // UMTSGSMPLMNCallDataRecord[]
				for (int j = 0; j < n.subNodes.length; j++) {
					Node umt = n.subNodes[j]; // UMTSGSMPLMNCallDataRecord
					Node recType = umt.getSubNode("recordType"); // RecordType
					retList.add(recType);
				}
			} else { // uMTSGSMPLMNCallDataRecord [0] UMTSGSMPLMNCallDataRecord
				Node recType = n.getSubNode("recordType"); // RecordType
				retList.add(recType);
			}
		}
		Node[] ret = new Node[retList.size()];
		retList.toArray(ret);
		return ret;
	}

	// Assumes only UMTSGSMPLMNCallDataRecord Node is passed.
	public String recordToString(Node cdrNode) {
		// RecordType => MO | MT | ...
		cdrNode = cdrNode.isChoice() ? cdrNode.getSubNodeChoice() : cdrNode;
		if (cdrNode == null)
			return "";

		StringBuffer sb = new StringBuffer();
		sb.append(cdrNode.getTypeName() + ",");

		for (int i = 0; i < cdrNode.subNodes.length; i++) {

			String t = cdrNode.subNodes[i].getFieldName();
			String v = cdrNode.subNodes[i].getValue().toString();
			sb.append(t + "(" + v + ")");
			if (i + 1 < cdrNode.subNodes.length) {
				sb.append(",");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
}

class AluMapper implements ASCIIFormattable {
	public Node[] nodeToRecords(Node rootNode) {
		return rootNode.subNodes;
	}

	// Assumes Node is of type CallEventRecord&
	public String recordToString(Node cdrNode) {
		cdrNode = cdrNode.isChoice() ? cdrNode.getSubNodeChoice() : cdrNode;

		StringBuffer sb = new StringBuffer();
		sb.append(cdrNode.getTypeName() + ",");

		for (int i = 0; i < cdrNode.subNodes.length; i++) {

			String t = cdrNode.subNodes[i].getFieldName();
			String v = cdrNode.subNodes[i].getValue().toString();
			sb.append(t + "(" + v + ")");
			if (i + 1 < cdrNode.subNodes.length) {
				sb.append(",");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
}

class ZteMapper implements ASCIIFormattable {
	public Node[] nodeToRecords(Node rootNode) {
		return rootNode.subNodes;
	}

	// Assumes Node is of type CallEventRecord&
	public String recordToString(Node cdrNode) {
		cdrNode = cdrNode.isChoice() ? cdrNode.getSubNodeChoice() : cdrNode;

		StringBuffer sb = new StringBuffer();
		sb.append(cdrNode.getTypeName() + ",");

		for (int i = 0; i < cdrNode.subNodes.length; i++) {

			String t = cdrNode.subNodes[i].getFieldName();
			String v = cdrNode.subNodes[i].getValue().toString();
			sb.append(t + "(" + v + ")");
			if (i + 1 < cdrNode.subNodes.length) {
				sb.append(",");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
}

class HuaMapper implements ASCIIFormattable {
	public Node[] nodeToRecords(Node rootNode) {
		Node headerNode = rootNode.subNodes[0];
		Node callEventRecords = headerNode.getSubNode("callEventRecords");
		return callEventRecords.subNodes;
	}

	// Assumes Node is of type CallEventRecord&
	public String recordToString(Node cdrNode) {
		cdrNode = cdrNode.isChoice() ? cdrNode.getSubNodeChoice() : cdrNode;

		StringBuffer sb = new StringBuffer();
		sb.append(cdrNode.getTypeName() + ",");

		for (int i = 0; i < cdrNode.subNodes.length; i++) {

			String t = cdrNode.subNodes[i].getFieldName();
			String v = cdrNode.subNodes[i].getValue().toString();
			sb.append(t + "(" + v + ")");
			if (i + 1 < cdrNode.subNodes.length) {
				sb.append(",");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
}
