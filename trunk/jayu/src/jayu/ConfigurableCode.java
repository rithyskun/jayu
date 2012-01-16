
package jayu;
import java.io.*;
import java.util.Collection;
import java.util.Vector;
//import org.codehaus.janino.util.resource;
//import org.codehaus.janino.util.resource.ResourceFinder;

import org.codehaus.janino.*;
import org.codehaus.janino.util.resource.*;
//import org.junit.Test;


/**
 * Creates a Class object from a Java source file, there by 
 * making code configurable.
 * 
 * Only method of interest is static Object loadClass(className);
 * the className is a fully qualified java path. It is used 
 * to find the java source file.
 * 
 * E.g. if Alacatel.java is kept in a folder grammar which in classpath of executing program
 * then 
 *  loadClass("grammar.Alcatel");
 * would return an instance of class Alcatel.
 *    
 */

// TODO: Lots of cleanup required on below code.

public class ConfigurableCode {

	public static MultiResourceFinder getMultiResourceFinder() {
		Collection resourceFinders = new Vector();
		resourceFinders.add( new DirectoryResourceFinder(new File(".")) );
		resourceFinders.add( new CustomResourceFinder() );
		return new MultiResourceFinder( resourceFinders );
	}
	
	public static MultiResourceFinder msf = getMultiResourceFinder(); 
	
	
	public static ClassLoader dynamicClassLoader = new JavaSourceClassLoader( // Also see CachingJavaSourceClassLoader
			ClassLoader.getSystemClassLoader()				// parentClassLoader    
			, msf 	// resourcefinder
			//,(java.io.File[]) null   			// optionalSourcePath
			,(String) null    					// optionalCharacterEncoding
			//	(java.io.File) null 			// Required for CachingJavaSourceClassLoader
			); 

	public static Class getSpriteClass(String className) throws ClassNotFoundException {
		return dynamicClassLoader.loadClass(className);
		// return ClassLoader.getSystemClassLoader(); // The default ClassLoader 
	}
	
	//final public static String JAVAFILE = "src\\test\\testdata\\Alcatel.java";
	//final public static String JAVAFILE = "Alcatel";
		//static String ALU_GRAMMAR_FILE = "src\\test\\testdata\\Alcatel.txt";
	
	
	public static Object loadClass(String className) {
		
		//System.out.printf("\n simpleTest() invoked() ");
		//String className = JAVAFILE;
        try {
        	
        	
			Class c = getSpriteClass(className); 
			if( className == null ) {
				throw new RuntimeException(" spawnClass(null) invoked in Sprite.spawnClass() " );
			}
			if( c == null ) {
				throw new RuntimeException(" Sprite.getSpriteClass("+className+") returned null in spawnClass() " );
			}
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ about to spawn a Sprite "+className+ " @@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			Object obj =  c.newInstance();
			//System.out.println("--------------------- Hoorey success in spawn of Sprite "+className+ " ------------------------");
			return obj;
        }
		   catch( RuntimeException e ) {					
				throw new ASNException( "RuntimeException.." + e.getMessage() );
		   }
           catch( ExceptionInInitializerError e ) {
                  String s = className + ":ExceptionInInitializerError: " + e.getCause();
                  throw new ASNException(s);
           }
           catch( LinkageError e ) {
                  String s = className + ":LinkageError: " + e.getCause();
                  throw new ASNException(s);
           }
           catch( ClassNotFoundException e ) {
                  String s = "("+className+")" + ":ClassNotFoundException: " + e.getCause();
                  throw new ASNException(s);
           }
           catch( Throwable e ) {
                  String s = className + ":Throwable: " + e.getClass().getName() + " e.getCause() " + e.getCause() ;
                  throw new ASNException( s );
           }
		
	}

}


class CustomResourceFinder extends org.codehaus.janino.util.resource.ResourceFinder {

public CustomResourceFinder() {
	//System.out.println("CustomResourceFinder Invoked --------------------------------");
} 
																	//Eg. FullyQualifiedClassName= tankattack.sprites.Tank
public org.codehaus.janino.util.resource.Resource findResource(String filename) { 
	//	if( fullyQualifiedClassName == null ) {
	//		throw new RuntimeException("CustomResourceFinder.findResource(null string) invoked.");
	//	}
		//System.out.println("fullyQualifiedClassName=("+fullyQualifiedClassName+")");
	//	String javaFileName = fullyQualifiedClassName.replace('.' , '/') + ".java";  // javaFileName = tankattack/sprites/Tank.java		
	//	System.out.println("In CustomResourceFinder.findResource("+filename+")  Will now open it internally and check **" );
		org.codehaus.janino.util.resource.Resource res = new CompilerResource( filename );
		// Let us not return a resource unless we are capable of handling it. 
		InputStream is = null;
		try {
			is = res.open();
		}
		catch( IOException e) { 
		//System.out.println("Internal Check CustomResourceFinder.findResource("+filename+").open() threw IOException. So return null resfinder");
			return null;
		} 		
		if ( is == null ) {
		//System.out.println("Internal Check CustomResourceFinder.findResource("+filename+").open() returned null. So returning null resfinder");
			return null;
		} else {
		//System.out.println("Internal Check CustomResourceFinder.findResource("+filename+").open() returned something. So return resfinder");
			try { is.close(); } catch (IOException ex) { }						
		}
		return res;				
}

//public InputStream findResourceAsStream(String resourceName) throws IOException{
  //        return null;
//}

}

class CompilerResource implements org.codehaus.janino.util.resource.Resource {
	
	String filename;

	public CompilerResource(String filename_) {
		filename = filename_;
	}
	
	 public String getFileName() { 
			return filename;
	 }
 
	 // Returns the time of the last modification, in milliseconds since 1970, or 0L if the time cannot be determined. 
	 public long lastModified() {  
		return 0L;
	 }
	public java.io.InputStream open() throws IOException {
		//if( filename.equals("tankattack/sprites/Sprite.java" ) ) {
		//	return Application.getResourceAsStream("gamepack/Sprite.class");
		//}
		//System.out.println(" About to open inputstream : "+ filename );
		
		java.io.InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);		
		//java.io.InputStream is = Application.getResourceAsStream(filename);
		if( is == null ) {
			//System.out.println("Please check if \""+ filename + "\" exists. Application.getResourceAsSream("+filename+") returned null Inputstream.");
		}
		return is;
	}
}


