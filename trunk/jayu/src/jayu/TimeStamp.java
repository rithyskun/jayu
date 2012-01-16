package jayu;

public class TimeStamp {
	long start;
	public TimeStamp() {
        start = System.currentTimeMillis( );
	}
	long elapsed() {
		long end = System.currentTimeMillis( );
		return (end-start)/1000;		
	}
	public long elapsedMS() { return System.currentTimeMillis()-start; }
	
	public String toString() {
		double time = elapsedMS()/1000 ;
		String ret =  "" + time + " secs OR " + (time/60) + " min";
		return ret;
	}
	void reset() { start = System.currentTimeMillis( ); }
}
