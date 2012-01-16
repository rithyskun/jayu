package test.testdata;
import java.util.ArrayList;

import jayu.*;

public class Ericsson implements ASCIIFormattable {
	public Node[] nodeToRecords(Node rootNode) {
		ArrayList retList = new ArrayList();
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
