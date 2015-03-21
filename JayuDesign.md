# Data Structure and Parse Logic #

## Key Classes used for Data Structure ##

### EBlock ###
Models a [TLV](http://en.wikipedia.org/wiki/Type-length-value) Block of bytes. Also can be thought of as modelling ASN Data, since all ASN Data is made up of TLV (tag,length,value).
```
class EBlock {
    int getTag()
    int getLength()
    byte[] getValue()
    
    // [--0- ----] is tag's 6th bit is unset
    isPrimitive() // is Leaf         

    // [001- ---*] is 6th bit set and 7th,8th bit unset
    isUniversalSetSeq() // is composite and element of array. 

    EBlock[] getSubBlocks() // Only if not leaf 
}
```
Since EBlock structure is recursive, hundreds of EBlock objects get created during decoding. For a low memory footprint, the EBlock is implemented such that all EBlock objects maintain a reference to a single byte array containing ASNData loaded from a file. Each  EBlock object keeps track of the offset in byte array to work with the data of its interest.

### ASNClass ###
Models an ASNClass found in Grammar Files, or more precisely it models the concept of a Type.
In general `int, int[], int*, int&` are examples of a type in C++. Similarly `String, String[]` are examples of a type in Java. Similarly the ASNClass supports the notion of an array, reference through its members.
```
class ASNClass {
    String name;          // Eg. INTEGER, OCTET STRING, MOCallRecord etc
    boolean isArray();    // SET OF/SEQ OF are modeled as array.
    boolean isReference();// CHOICE is modeled as a reference/Pointer
    boolean isPrimitive();// Primitive or User-Defined
    Fields[] fields;      // For User-Defined ASNClass, the fields go here.
}
```
### Field ###
Models a Fields of an ASNClass
```
class Field {
    String name;    // Name of field
    int pos;        // Position of field
    ASNClass type;  // Type of field
}
```
Eg. If a Grammar file contains a class defined as -
```
CallEventRecord ::= CHOICE
{
    moCallRecord  [0]  MOCallRecord, 
    mtCallRecord  [1]  MTCallRecord, 
}
```
then moCallRecord and mtCallRecord are modeled as fields of ASNClass CallEventRecord. The top level ASNClass in a grammar file is presumed to be defined in a imaginary field called "root".
`root [0] CallEventRecord`

### Node ###
Models Decoded/Parsed Information Data structure.
```
class Node
{
    boolean isPrimitive()      // Is leaf node
    PrimitiveClass getValue();// To get value for leaf Nodes
    Field field;             // To get Type information of this Node.
    Node[] subNodes;        // To get sub nodes for non leaf nodes.

    boolean isReference;  // Is this a reference/Pointer Node?
}
```
The Node is a recursive data structure to store parsed information.
All leaf Nodes store decoded information in the PrimitiveClass object
retrievable via getValue(). The PrimitiveClass is simply a wrapper around Java primitive types.

The Node tree structure's size and depth is exactly the same as the ASNClass tree structure. At every level in the Node tree, it contains
  1. The decoded information in PrimitiveClass (for leaf nodes)
  1. The ASNClass object to provide Meta-data for decoded information.

This implies that for every ASNClass object in the ASNTree, there must be an equivalent Node object in the Node tree. In order to make both ASNClass and Node tree similar, the notion of "Reference Node" is introduced.

The "Reference Node" is like a pointer/reference. It contains no data, but just points to another Node accessible via `getReferencedNode()`
The Reference Node's counter part in the Grammar file is the ASNClass defined using `CHOICE` relation which pretty much acts like a pointer/reference too.

### PrimitiveClass ###
This is thin wrapper around Java Primitives, so that they can be treated in a uniform manner, irrespective of the underlying type.
```
public class PrimitiveClass  {

    // Type denotes the actual Primitive stored in this object.
    int type; // 1=int, 2=double, 3=byte[], 4=String, 5=boolean

    int intValue;
    double doubleValue;
    byte[] byteArray;	
    String stringValue;	 
    boolean boolValue;
}
```
A primitive class object only contains only 1 primitive value which is denoted by the `type` attribute.

---


## ASN Parse Pseudo-Logic ##
Given a ASN Grammar and ASN Data File, create a Node
which contains the parsed information.
```
EBlock b = <build EBlock from ASN Data file>
Field  f = <build Field from Grammar File>

Node parsedInfo = NodeFactory.makeNode( b, f ) {
   if ( f.isPrimitive() ) { decode b and return a leaf Node object }
   else { // recursion
      Node[] subNodes = new Node[f.type.fields.length]; 
      for ( int i=0; i< subNodes.length; i++) {
          subNodes[i] = NodeFactory.makeNode( b.getSubBlocks()[i], f.type.fields[i] );
      }
      return new Node( f, subNodes ); // Simple Node contructor
   }
}
```


---


## Mapping ASN.1 Structures to "Programming Language Constructs" ##

Developers are familiar with concepts like
  1. Arrays
  1. Pointers/Reference
  1. Primitive Data types like int, byte[.md](.md) (for efficiency)

Any programming task at hand, always will needs to implemented using these concepts. So it is necessary to have a clean Mapping of ASN.1 structures to these programmer's tools.

```
ASN.1   <-----mapping----->    Java
-----------------------------------------
BOOLEAN                        Boolean
INTEGER                        Integer
BIT STRING                     Byte[]
OCTET STRING 		       Byte[]
NULL                           Byte[]
OBJECT IDENTIFIER 	       Byte[]
DOUBLE                         Double
ENUMERATED                     Integer
NumericString 		       Byte[]
PrintableString 	       Byte[]
TeletexString 		       Byte[]
VideotexString 		       Byte[]
IA5String                      Byte[]
GraphicString 		       Byte[]
VisibleString 		       Byte[]
GraphicString2 		       Byte[]		

SEQUENCE/SET             Standard Java Classes
SET OF/SEQUENCE OF       Standard Java Array
CHOICE                   Reference, Similar to Java/C++ reference.
```

There are utility methods available in class `Utility` to convert byte array to int, String or perform nibble swap on byte array.