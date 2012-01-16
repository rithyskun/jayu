package test.testdata;
import jayu.*;

public class Zte implements ASCIIFormattable { 
	public Node[] nodeToRecords(Node rootNode) {

		return rootNode.subNodes;		
	}
	
	// Assumes Node is of type CallEventRecord& 
	public String recordToString(Node cdrNode) { 
		cdrNode = cdrNode.isChoice() ? cdrNode.getSubNodeChoice() : cdrNode;
		
		StringBuffer sb = new StringBuffer();
		sb.append( cdrNode.getTypeName() + "," );		
		
		for(int i=0;i<cdrNode.subNodes.length;i++) {
			
			String t = cdrNode.subNodes[i].getFieldName();
			String v = cdrNode.subNodes[i].getValue().toString();
			sb.append( t + "(" + v + ")");
			if( i+1 < cdrNode.subNodes.length ) {
				sb.append(",");
			}			
		}
		sb.append("\n");
		return sb.toString();	
	}
}
