package rossProxy;

import rossProxyNetworking.FinishXMLCollecting;

public class FinishXMLNotifier {
	   private FinishXMLCollecting fc;
	    private boolean somethingHappened; 
	    public FinishXMLNotifier (FinishXMLCollecting event)
	    {
	    // Save the event object for later use.
	    fc = event; 
	    // Nothing to report yet.
	    somethingHappened = false;
	    } 
	    //...  
	    public void doWork ()
	    {
	    // Check the predicate, which is set elsewhere.
	    if (somethingHappened)
	        {
	        // Signal the even by invoking the interface's method.
	        //fc.interestingEvent ();
	        }
	    //...
	    } 
	    // ...
}
