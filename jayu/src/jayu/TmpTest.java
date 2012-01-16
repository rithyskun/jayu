package jayu;


public class TmpTest implements test.TestConst {

	public static void main(String[] args) {
		System.out.printf("\n CWD = (%s) ",System.getProperty("user.dir"));
		
		Node rootNode = NodeFactory.parse(ALU_DATA_FILE, ALU_GRAMMAR_FILE);
		ASCIIFormattable af = (ASCIIFormattable) ConfigurableCode.loadClass(ALU_MAPPING_FILE);
		rootNode.toASCII(new SomeAluMapper(), ALU_CSV_FILE);
		
		System.out.printf("\n CSV File %s written ", ALU_CSV_FILE );
	}
}

class SomeAluMapper implements ASCIIFormattable {
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
