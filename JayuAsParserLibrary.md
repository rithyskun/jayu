#This tutorial covers how to programatically use, JAYU ASN Parser library.

# JAYU ASN Parser #

## Pre-requisites ##

  1. The ASN parser library - jayu.jar file
  1. ASN Grammar File & ASN Data File. (Available in the download)
  1. Basic knowledge of Java programming.

## How to use Jayu as a library ##

If you need to write a program to decode ASN data programatically then JAYU is the tool for you.

We will now develop a simple program that will make use of jayu.jar library to parse ASN data. The parsing can be achieved using a single of code in your java program !! as shown below -

```
Node rootNode = NodeFactory.parse(inputDataFileName, inputGrammarFileName);
```

All the parsed information is stored in the returned Node object. A Node object is either a Primitive or a Composite. A Composite Node contains an array of Nodes. The Node thus is a recursive tree. A Primitive Node contains data that may be - int, float, String, byte[.md](.md). The Primitive node is a leaf node in the tree.

```
class Node {	
	Node[] subNodes;            // Only if not a Primitive
	boolean isPrimitive();      
	PrimitiveClass getValue();  // Only if Primitive 
	...
}
```

PrimitiveClass is simple wrapper around primitive types like int,float,String,byte[.md](.md).
All ASN Primitives are mapped to any one of these 4 java types.

Node has few more useful methods to get meta information about the node

```
class Node {
    ...
	String getTypeName();
	String getFieldName();
	String toString();
    ...
}
```

The type is a String that corresponds to a ASN Class defined in "inputGrammarFile".
The fieldName is a String that corresponds to the field name in "inputGrammarFile"
The toString method is handy to debug and print all information related to the node.

## Example with Grammar File ##

If a Type in grammar file is defined as follows.
```
===========================START OF GRAMMER FILE=============================

MOCallRecord 	::= SET
{
	recordType 		[0] INTEGER,  
	location 		[12] LocationAreaAndCell OPTIONAL,	
	supplServicesUsed 	[17] SEQUENCE OF SuppServiceUsed OPTIONAL,
	...
}

-- Comment: Now say the type 'MOCallRecord' can used with a fieldName in some other differnt Type. 

CallEventRecord		::= CHOICE
{
    ...
	moCallRecord 		[0] MOCallRecord,    # THE NODE IN LIMELIGHT #
    ...
}
========================== END OF GRAMMAR FILE ================================
```

Say we have a Node n corresponding to "moCallRecord" then

```
n.getTypeName() will return string 'MOCallRecord'	
n.getFieldName() will return "moCallRecord" the name of the field	
n.isPrimitive() will return false since this Type is a composite Type
n.subNodes variable is an array of Node[] containing child nodes like recordType, supplServicesUsed etc.
```


### Root Node - Starting Point of Navigation ###

To begin traversing the Node Tree use -
```
Node rootNode = NodeFactory.parse(inputDataFile, inputGrammarFile);
Node[] callEventRecord = rootNode.subNodes;
```
The `rootNode.subNodes` always provides an Array of Nodes whose type corresponds to the Top Most ASNClass in the Grammar File. In our example it corresponds to ASNClass CallEventRecord.

### Useful methods to Navigate Node Tree ###

To Navigate, it is assumed that the programmer has the ASN Grammar File and understands the hierarchy of ASN Classes.

```
//when you know the child Node is a Composite defined by SET/SEQUENCE 
Node getSubNode(fieldName)         

//when you know the child Node is an Array defined by SET OF/SEQUENCE OF.
Node[] getSubNodeAsArray(fieldName)  

//when you don't know what the child node type is!, as it defined by CHOICE and depends on data
Node getSubNodeChoice()         

// when there is no child Node! The current node itself is a Primitive
// like INTEGER,OCTET_STRING etc. and you want to look at the information. 
PrimitiveClass getValue()       

```

### Putting it all together ###

The below code traverses the Node tree, assuming the data is as per the above grammar file.

```
Node rootNode = NodeFactory.parse(inputDataFile, inputGrammarFile);  // Points to rootNode
Node[] callEventRecord = rootNode.subNodes; // rootNode.subNodes is always an array of the TopMost ASNClass defined in Grammar file.

callEventRecord[0].isChoice();      // returns true since CallEventRecord" is defined as a CHOICE in Grammar File
	 
Node choiceNode = callEventRecord[0].getSubNodeChoice();   // returns the sub node which may be "MOCallRecord" or any other choice.

if( choiceNode.getTypeName().equals("MOCallRecord") ) {    // Confirm that we are indeed working with "MOCallRecord"
    Node MOCallRecordNode = choiceNode;			    
}

Node recordTypeNode = MOCallRecordNode.getSubNode("recordType");

System.out.printf("Retrieved recordType %s ", recordTypeNode.getValue() ); // Use getValue() on leaf Nodes 

Node compositeNode = MOCallRecordNode.getSubNode("location");      // Since we know that location is a composite from Grammar File.

if ( compositeNode != Node.NULL_NODE ) {
	// Work further with compositeNode here..
}

Node[] nodeArray = MOCallRecordNode.getSubNodeAsArray("supplServicesUsed");  // Since we know that supplServicesUsed is an Array from Grammar File.
if ( nodeArray != null ) {
	// Work furthur the nodeArray 
}
```