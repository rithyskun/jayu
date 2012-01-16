package jayu;

import java.util.ArrayList;
import java.util.Map;

public class NodeFactory {

	/** Converts a ASN data file to a Node */
	public static Node parse(String dataFile, String grammarFile) {
		Field rootField = ASNClassFactory.getField( grammarFile );
		byte[] byteArr = Util.FileToByteArray( dataFile );
		EBlock rootBlock = new EBlock(0,0,byteArr,0,0,byteArr.length);
	
		Node topNode = NodeFactory.makeNode( rootField, rootBlock, new FilterClass(),-1, 0 );
		// Assumption is that for all Grammar files the topNode's type is always an array
		// ie.   topNode.getType().isArray() == true
		//return topNode.subNodes;
		return topNode;
	}
	
	static byte[] dummyArr = new byte[10];	 
	static EBlock dummyBlock = new EBlock(0,8,dummyArr,0,2,10);
	
	/** Creates a dummy Node associated with given asnField */
	public static Node createDummyNode(Field asnField) {		
		Node retNode = new Node( asnField, null, dummyBlock );
		return retNode;
	}

	/** Creates a int from a Block */
	static int makeInteger(EBlock b) { return Util.byteArrayToInt( b.fileBytes, b.valStart, b.valEnd ); }
	
	/** Creates a byte[] from a Block */
	static byte[] makeByteArray(EBlock b) { return b.getValue();  }

	// TODO: remove the dummy implementation for this methods.
	static boolean makeBoolean(EBlock b) { return true; }	
	
	// TODO: remove the dummy implementation for this methods.
	static double makeReal(EBlock b) { return 3.1415; }	
	
	/**
	 * Creates a Node out of a primitive ASNBlock.
	 *  
	 * @param b - The block to be converted to Node. 
	 * @param asnField - Type information corresponding to the block
	 * @throws ASNException - If block b is not a primitive. 
	 */
	public static Node createPrimitiveNode(final EBlock b,Field asnField) { // TODO: get appropriate primitive
		if( !b.isPrimitive ) throw new ASNException("Defensive check failed");
		if( asnField.type.isArray() ) throw new ASNException("Defensive check failed. This is array ");
		
		Node retNode = new Node(asnField, null,b );
		return retNode;
	}

	/**
	 * @return - If subBlock is Universal 16/17 and field f has a array subfield then it creates and returns a node 
	 * else it returns null. 
	 * @throws ASNException if subBlock is Universal 16/17 but there is no array child field.
	 * 
	 *  Q. What if field f has multiple array subfields?
	 *  A. We are interested only in array subfields with ASNConst.POS_NOT_SPECIFIED There can only be
	 *     one such array subfield in any ASNClass, otherwise it will lead to ambiguous situation.
	 *     The code doesn't do any check but simply returns the first occurrence of subfield that is 
	 *     an array subfield and whose pos is ASNConst.POS_NOT_SPECIFIED. 
	 *      
	 */
	private static Node makeUniversalSetSeqNode(Field f, EBlock subBlock , FilterClass filter, int maxBlocks, int depth ) {
		if( subBlock.isUniversalSetSeq() ) { // composite | array
			Field farrChild = f.getArrayChildWithoutPos();
			if ( farrChild != null ) {
				return makeNode( farrChild, subBlock, filter, maxBlocks , depth+1 );
			} else {
				throw new ASNException("" + f + " " + subBlock + "Field does not have a array subField, but encountered Universal tag 16|17");
			}
		} 
		return null;
	}
	
	public static Node makeReferenceNode(Field f, Node referencedNode ) {
		Node[] subNodes = new Node[1];
		subNodes[0] = referencedNode;
		Node retNode = new Node( f, subNodes, null ); 
		//retNode.isChoice = true; | commented as a part of Bug fix #1
		return retNode;
	}
	public static int numCalls = 0;	
	public static Node makeNode(Field f, EBlock b, FilterClass filter,int maxBlocks, int depth  ) {
		numCalls++;
		if( numCalls > 646500 ) {
			//System.out.printf("\n %d makeNode("+f + "," + b + ")", numCalls );
		}
		//System.out.printf("\n makeNode("+f + "," + b + ")" );
		// Recursive makeNode(f,b)
		Node[] subNodes = null; 
		try {
			if ( b.isLeaf() ) { 
				try {
				return createPrimitiveNode(b,f);
				} catch( OutOfMemoryError e ) {
					System.out.printf("\n ### %s ", e.getMessage());
					String s = "Error in createPrimitive() " + f + " " + b + " : "; 
					throw new ASNException("makeNode", s + e.getMessage() );
				}
			} 
			if( f.isArray()) {
				return makeNodeArray( f, b, filter, maxBlocks , depth );
			} 
			// ==== Composite and Non Array ==== 
			ArrayList<EBlock> subBlocks = b.getSubBlocks(EBlock.MAX_BLOCKS, 0 , (byte) 0); // Non Arrays don't worry about Fixed Blocks.		
			subNodes = new Node[subBlocks.size()];
			
			for( int i=0; i<subBlocks.size(); i++) {
				EBlock subBlock = subBlocks.get(i); 
				Node univSetSeqNode = makeUniversalSetSeqNode( f, subBlock , filter , maxBlocks , depth  );
				if( univSetSeqNode != null ) {
					subNodes[i] = univSetSeqNode;
					continue;
				}  
				int sbpos = subBlock.tag;
				Field childField = f.getChildField( sbpos );
				if( childField != null ) {
					subNodes[i] = makeNode(childField, subBlock, filter, maxBlocks , depth+1 );					
				} else { // -- composite | not array | choice
					Field[] retFieldArr = f.getGrandChildField( sbpos );
					if( retFieldArr == null ) {
						String str = "Unable to find child field or grandchild field for given tag. subBlock.tag(" + sbpos + ") field(" + f + ")";
						throw new ASNException("makeNode",str);
					} else {
						childField = retFieldArr[0];
						Field grandChildField = retFieldArr[1];						
						Node grandChildNode = makeNode(grandChildField, subBlock, filter, maxBlocks, depth+1 );
						Node childNode = makeReferenceNode( childField , grandChildNode );
						subNodes[i] = childNode;
					}								
				}
			} // for ( subBlocks ) 
		} catch( ASNException e ) { // Catch Exception if thrown by recursive makeNode(), append stack Info and re throw. 
			if( e.isType("makeNode") ) {
				throw new ASNException( "makeNode", "\n makeNode("+ f +","+ b + ") " + e.getMessage() );
			} 
			if( e.isType("makeNodeArray")) {
				throw new ASNException( "makeNodeArray", "\n makeNodeArray("+ f +","+ b + ") " + e.getMessage() );
			} 
			throw e;
		} 
		return new Node( f , subNodes, b );
	}
	
	private static Node makeNodeArray(Field f, EBlock b, FilterClass filter,int maxBlocks, int depth ) {
		
		
		ArrayList<EBlock> subBlocks = b.getSubBlocks(EBlock.MAX_BLOCKS, f.type.blockSize, f.type.paddingByte );
		Node[] subNodes = new Node[subBlocks.size()];
		
		if( f.isReference() ) { // Heterogeneous Array
			for( int i=0; i<subBlocks.size(); i++) {
				EBlock subBlock = subBlocks.get(i);
				Node univSetSeqNode = makeUniversalSetSeqNode( f, subBlock , filter , maxBlocks, depth );
				if( univSetSeqNode != null ) {
					subNodes[i] = univSetSeqNode;
					continue;
				}
				int sbpos = subBlock.tag;
				Field refChildField = f.getChildField( sbpos );
				if ( refChildField == null ) {
					throw new ASNException("makeNodeArray","No child field at pos 'sbpos' for Reference Array.");
				}else {
					Node refChildNode = NodeFactory.makeNode(refChildField, subBlock, filter, maxBlocks, depth+1 );
					Field f_nav = f.getCachedCloneNoneArray();
					Node childNode = makeReferenceNode( f_nav , refChildNode);
					subNodes[i] = childNode;
				}				
			}						
		} else { // Homogeneous Array
			Field f_nav = f.getCachedCloneNoneArray();
			for( int i=0; i<subBlocks.size(); i++) {
				EBlock subBlock = subBlocks.get(i);
				subNodes[i] = makeNode( f_nav , subBlock, filter,maxBlocks , depth+1 );
				
				// ------------------Defensive Checks ------------------
				if ( f_nav.type.isAssociatedWithTag() ) { // Array of Primitives ??
					if( subBlock.isUniversalSetSeq() ) { throw new ASNException("makeNodeArray","Unexpected Universal 16|17 Tag "); }
				}else { // Array of Composite
					//int sbpos = subBlock.tag;
					if( !subBlock.isUniversalSetSeq() ) { // Furthermore the subBlock.tag = 16 or 17 based on f_nav.relation (SEQ/SET) 
						throw new ASNException("makeNodeArray","!subBlock.isUniversalSetSeq()");
					}
				} // -------------End of Defensive checks------------				
			}
		}
		Node retNode = new Node( f , subNodes, b);
		// Commented as a part of Bug Fix #1
		//if ( f.isReference() ) {  retNode.isChoice = true; }    
		return retNode;
	}
	
	
	// ===================================================================================================
	// ======================================== OLD CODE BELOW  =========================================
	// ===================================================================================================
	
/*	public static Node toNode(Field asnField, Block b, FilterClass filter,int maxBlocks ) {
		//System.out.printf("\n>toNode(asnField(%s|%s),Block(start:%s,size:%s,length:%s)) called",asnField.type.getName(),asnField.name,b.blockStart,b.blockSize,b.length);
		if( asnField == null || asnField.type == null ) {
			throw new ASNException("ASNField/Class is null.");
		}
		if( asnField.type.arrInfo.isArray() ) {
			return toNodeArray( asnField, b , filter, maxBlocks );
		}
		if( asnField.type.isPrimitive() ) {
			if (!b.isPrimitive ) {
				throw new ASNException("Inconsistant asnClass(PRIMITIVE) and block(COMPOSITE) ");
			}		
			return createPrimitiveNode(b, asnField);
		}
		// Lets get started. We have blocks and need to build nodes.
		//Block[] subBlocks = Block.getSubBlocks( b.value , maxBlocks); // Raw material for building subNodes and thus this node.
		Block[] subBlocks = b.getSubBlocks( maxBlocks); // Raw material for building subNodes and thus this node.
		Node[] subNodes = new Node[subBlocks.length];
		
		for(int i=0;i<subBlocks.length;i++) { // For each subBlock
			int tag = subBlocks[i].tag;	
			
			Field childASNField = asnField.type.getChild(tag);;
			
			boolean addDummyNode = (filter != null && filter.toBefiltered(tag));
			if( childASNField == null ) {
				System.out.printf("\n childASNField ==null  ASNClass = %s isArray = %s ", asnField.type.getName(), asnField.type.arrInfo.isArray() );				
				if( subBlocks[i].isUniversalSetSeq() ) { // Check if Node is an Array Element
					System.out.printf("\n Hit Universal 16 Block  ASNClass = %s isArray = %s ", asnField.type.getName(), asnField.type.arrInfo.isArray()  );
					childASNField = asnField.cloneNonArray();
					childASNField.pos = tag;
					childASNField.name = "field_" + i;
					//subBlocks[i] = subBlocks[i].
				} else {			
					String str = "Mismatch in ASNClass/Block. ASNClass="+asnField.type.getName()+" arrInfo="+ asnField.type.arrInfo.isArray()+" Tag="+tag+" but no child ASNClass found at Tag. subBlocks["+i+"] = " + subBlocks[i];
					//throw new ASNException(str);
					System.out.printf("\n Warning: %s",str);
					addDummyNode = true;
					childASNField = new Field("longdummyname","dummyname",1000,ASNClassFactory.getPrimitiveASNClass("INTEGER") );//					
				}			
			}else {
				//if( asnField.type.name.equals("UMTSGSMPLMNCallDataRecord") ) {
				    //System.out.printf("\n ChildASNField != null  ASNClass = %s", childASNField.type.getName() );
				//}
			}
			
			if( addDummyNode ) {				
				subNodes[i] = NodeFactory.createDummyNode(childASNField);
			} else {				
				subNodes[i] = toNode( childASNField , subBlocks[i] , null, maxBlocks);
			}
			//System.out.printf("\n curBlock = %d , TotalBlocks = %d ", i, subBlocks.length );
		}
		return new Node( asnField, subNodes );
	}
	
	static Node toNodeArray(Field asnField, Block b, FilterClass filter, int maxBlocks) {
		//System.out.printf("\n<toNodeArray(asnField(%s),Block(start:%s,size:%s)) called",asnField.name,b.blockStart,b.blockSize);

		ASNClass asnClass = asnField.type;
		if( !asnClass.arrInfo.isArray() ) {
			throw new ASNException("Defensive Check failed: asnClass is not an array ");
		}
		
		Block[] subBlocks = null; 
		Node[] subNodes = null; 
		// We know that we (read asnClass) are an Array and our job
		// is to populate/read subNodes correctly
		boolean POS = asnClass.arrInfo.getType( ArrayInfo.POS );
		boolean TAG = asnClass.arrInfo.getType( ArrayInfo.TAG );
		boolean SEQ = asnClass.arrInfo.getType( ArrayInfo.SEQ );
		boolean CHO = asnClass.arrInfo.getType( ArrayInfo.CHO );
		
		// Important thing is to determine the Child (Array content's type) correctly

		//subBlocks = Block.getSubBlocks( b.value, maxBlocks ); // Raw material for building subNodes and thus this node.
		subBlocks = b.getSubBlocks( maxBlocks ); // Raw material for building subNodes and thus this node.
		subNodes = new Node[subBlocks.length];
		Node retNode = null;
		// The whole array existing inside a Block with tag POS. 
		// The value of this Block's Tag is shouldn't matter as it is controlled by Parent ASNClass.		
		// If We know that a Block is of certain ASNClass, then the Block's tag is of no use to us!		
		// Eg. If we know a block is a MOCallRecord, then its tag is usually 0. 
		// However if we know for sure that the block is MOCallRecord, then there is no point in checking for tag==0.
		// since the tag check responsibility is for the parent holding MOCallRecord.
		// Typically a block's tag is used to find the ASNClass in ASNViewer, but in our case we already know the ASNClass so tag is of no use! 

		// If the current Block is a part of ASNClass then use its tag to determine
		// next ASNClass, however if the current Block is a part of ASNClass[] then
		// you already know the ASNClass! You don't need to determine it you just need
		// to perhaps crosscheck if it is used with 16/17 or used with its own associatedTag.
		
		// Apart from SEQ we have pow(2,3) ie 8 combinations		
		if ( !CHO  ) { // NORMAL CASE			
					for(int i=0;i<subBlocks.length;i++) {
						int blockTag = subBlocks[i].tag;
						int asnTag = asnClass.getAssociatedTag();
						ASNClass childClass = null;
						if ( TAG ) { // Defensive safety checks
							if( blockTag != asnTag ) { // i believe i have fixed ASNClass(Primitives) to return correct tagValue as per the block. 
								throw new ASNException("\n Mismatch b/w ASNClass and block. blockTag != asnTag.  asnClass = " + asnClass.getName() + " asnTag = " + asnClass.getAssociatedTag() + " blockTag = " + blockTag + " subBlock[" + i + "] = " + subBlocks[i] );								
							}							
						} else { // NO TAG , we don't need to strip 16/17 since they act like POS
							if( (blockTag != 16) && (blockTag != 17) ) {
								throw new ASNException("\n Mismatch b/w ASNClass and block. blockTag != 16 or 17. asnClass = " + asnClass.getName() + " blockTag = " + blockTag + " subBlock[" + i + "] = " + subBlocks[i] );
							}
						}
						childClass = asnClass.getNonArrayClone();
						String childFieldName = "field_" + i;
						String childLongFieldName = asnField.longName + "." + childFieldName;
						int childFieldPos = -3;
						Field childField = new Field( childLongFieldName, childFieldName, childFieldPos, childClass );
						subNodes[i] = toNode( childField, subBlocks[i],filter, maxBlocks );
					}
					retNode = new Node( asnField, subNodes );*/
}
