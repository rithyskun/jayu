package jayu;

/* An attribute of ASNClass. Strictly speaking the class may not
 * be needed, just a boolean attribute would have sufficed.
 * However it is kept from design perspective so have more functionality
 * can be added easily later if needed.
 * 
 * If at a later stage this class is found to be redundant having impact 
 * on performance due to it garbage collection of multiple objects, then 
 * it can replaced with a simple boolean attribute in ASNClass. 
 * */

public class ReferenceInfo {

	boolean isRef;
	
	public ReferenceInfo(boolean isRef) {
		this.isRef = isRef;
	}
	
	public void setReference(boolean b) {
		isRef = b;
	}
	
	public boolean isReference() {
		return false;
	}
	
	public ReferenceInfo clone() {
		return new ReferenceInfo( isRef );
	}
}
