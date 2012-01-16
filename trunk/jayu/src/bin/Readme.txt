= Jayu - Free ASN Parser = 

== About Jayu == 
  Jayu is a Free ASN Parser written in Java. It can be used as on command line or as a library.
 
== Pre-requisites ==
  JRE 1.5 or above (It has been tested on JRE1.7)
 
== Jayu as command line tool ==
Usage: asn2csv {grammarFile} {mapFile} {outputDir} [datafile1] [datafile2]..

Conversion of ASN file to CSV file happens in 2 steps. 
In the first step the tool uses the {grammarFile} and parses it. 
In the second step the tool uses the {mapFile} and converts the parsed data to a csv.

The {grammarFile} is a file describing ASN data structure in a standard ASN Grammar file syntax.
In order to write {mapFile} some understanding of ASN Data and basic Java is required.

Don't be dis-heartened at the mention of 'Java' a look at the sample data files and examples will show you how easy it is to use. 
The good part is you don't need to compile or have JDK to use this utility. The tool itself contains a inbuilt compiler which will compile the java file into byte code at runtime. Only JRE will be needed to execute the program.

The {mapFile} is the fully qualified name of a Java class implementing ASCIIFormattable interface.
Eg. If {mapFile} is "HelloWorld" then it refers to a file "HelloWorld.java"
    If {mapFile} is "package1.package2.HelloWorld" then it refers to a file "package1/package2/HelloWorld.java"

The Java Class should implement the below interface.

public interface ASCIIFormattable {
	public Node[] nodeToRecords(Node rootNode);
	public String recordToString(Node recordNode);
}
The nodeToRecords receives 'rootNode' which contains parsed Data and returns an array of 'Nodes' in which we are interested.
The recordToString interface is used to convert each of the 'Nodes' of interest to a CSV string.

---------------------------------------------------------
== Examples ==

Download jayu.zip the from http://jayu.googlecode.com

Create a working directory on your computer say c:\jayu 
I will refer to this directory as 'WORKING_DIR'

Unzip 'jayu.zip' to WORKING_DIR.

You should now see a directory structure like this.

+ jayu
     |--Readme.txt           (This file)
     |--License.txt
     |--commons-compiler.jar
     |--janino.jar
     |--jayu.jar             (Free ASN parser)
     |--AsnToCsv.jar         (Command line Tool)
     +--test                 (Contains test data for examples)
         |
         + testdata         
              |
              Alcatel.txt   (Grammar File)
              alu.dat       (ASN Data File)
              Alcatel.java  {mapFile}
              ...

Now to convert ASN data File 'alu.dat' to a csv File run the below command.

cd jayu
asn2csv   test/testdata/Alcatel.txt test.testdata.Alcatel    .   test/testdata/alu.dat
               |                           |                 |               |
           {grammarFile}                 {mapFile}        {outputDir}      {dataFile}              

This will create a csv file in WORKING_DIR called "alu.dat.csv"

Similarly you can convert other test data file to csv using below commands.

asn2csv  test/testdata/Ericsson.txt test.testdata.Ericsson    .  test/testdata/eri.dat
asn2csv  test/testdata/Huawie.txt   test.testdata.Huawie      .  test/testdata/hua.dat
asn2csv  test/testdata/Zte.txt      test.testdata.Zte         .  test/testdata/zte.dat

---------------------------------------------------------
Example on Unix to convert all Alcatel ASN data files in a directory the command would be - 
cd jayu
ls $ASN_DATA_DIR/*.dat | xargs java -cp "./*.jar:."  Path/To/Alcatel.txt  test.testdata.Alcatel $OUTPUT_DIR

Note: If {mapFile} "test/testdata/Alcatel.java" is not in current working dir, then it should be in the classpath
Eg - java -cp "*.jar;$PATH_TO_TEST_DIR" Alcatel.txt test.testdata.Alcatel  . Path/To/alu.dat

---------------------------------------------------------
Have a look at the {mapFile} in the examples to understand how to write/tweak one.

Good luck. I will appreciate if you let me know in case the code is helpful to you.
For any suggestion/feedback please feel free to reach me at jayu.dukii@gmail.com
           





















