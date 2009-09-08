package tbc.supercheck;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * A recording remembers the history of test events that occur during a test run.
 * It does not record the actual data passed to each property, but instead records:
 * which properties were executed and in what order, the random seed used to
 * generate data for each property and the number of times each property was
 * executed.
 * <p>
 * A recording is valid so long as the properties it records events about do not
 * change their method signatures. If they do, data generation from the random seed 
 * will no longer match what occured during the recording. Because of this, if the
 * type signatures of properties have changed between the time of recording and
 * playback, the playback will fail.
 * 
 * @author Karl Jonathan Ward <karl.j.ward@googlemail.com>
 */
public class Recording implements Serializable {
    private static final long serialVersionUID = 1L;

    private class TestEvent implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String containingClass;
        private String property;
        private String[] params;
        private long randomSeed;
        private int times;
        
        private TestEvent(String containingClass,
        		          String property,
        		          String[] params,
        		          long randomSeed, 
        		          int times) {
            this.containingClass = containingClass;
            this.property = property;
            this.params = params;
            this.randomSeed = randomSeed;
            this.times = times;
        }
        
        private Method getPropertyMethod() throws ClassNotFoundException, 
                                                  NoSuchMethodException {
        	Class<?> containingT = Class.forName(containingClass);
	    	Class<?>[] paramTs = new Class<?>[params.length];
	    	for (int pIdx = 0; pIdx < paramTs.length; pIdx++) {
	    		paramTs[pIdx] = Class.forName(params[pIdx]);
	    	}
	    	return containingT.getMethod(property, paramTs);
        }
    }
    
    private ArrayList<TestEvent> testEvents = new ArrayList<TestEvent>();
    
    void addTestEvent(Method property, long randomSeed, int times) {
    	String propertyName = property.getName();
    	String containingClass = property.getDeclaringClass().getName();
    	Class<?>[] paramTs = property.getParameterTypes();
    	String[] params = new String[paramTs.length];
    	for (int pIdx = 0; pIdx < params.length; pIdx++) {
    		params[pIdx] = paramTs[pIdx].getName();
    	}
    	
        testEvents.add(new TestEvent(containingClass,
        		                     propertyName, 
        		                     params,
        		                     randomSeed, 
        		                     times));
    }
    
    void playBack(TestRun testRun) {
    	for (TestEvent te : testEvents) {
    		try {
    			testRun.runOn(te.getPropertyMethod(), te.times, te.randomSeed);
    		} catch (NoSuchMethodException e) {
    			System.out.println("Cannot run recording for " 
    					           + te.property
    					           + ". Either it no longer exists or" 
    					           + " its signature has changed.");
    			return;
    		} catch (ClassNotFoundException e) {
    			System.out.println("Cannot run recording for "
    					           + te.property
    					           + ". Its containing class, "
    					           + te.containingClass
    					           + " is missing.");
    			return;
    		}
    	}
    }
    
    /**
     * Get a written description of recorded events. Note that the return string
     * will have many new line chars.
     * 
     * @return a long string description
     */
    public String toDescription() {
        String d = new String();
        d += testEvents.size() + " test events:\n";
        for (TestEvent e : testEvents) {
            d += e.property 
              + " runs=" + e.times 
              + " seed=" + e.randomSeed + "\n";
        }
        return d;
    }
}
