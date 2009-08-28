package tbc.supercheck;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
 * change their arguments. If they do, data generation from the random seed will
 * no longer match what occured during the recording. Because of this, if the
 * type signatures of properties have changed between the time of recording and
 * playback, the playback will fail.
 * 
 * @author Karl Jonathan Ward <karl.j.ward@googlemail.com>
 */
public class Recording implements Serializable {
    private static final long serialVersionUID = 1L;

    private class TestEvent implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Method property;
        private long randomSeed;
        private int times;
        
        private TestEvent(Method property, long randomSeed, int times) {
            this.property = property;
            this.randomSeed = randomSeed;
            this.times = times;
        }
        
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeLong(randomSeed);
            out.writeInt(times);
            // TODO Serialise property signature
        }
        
        private void readObject(ObjectInputStream in) throws IOException, 
                                                      ClassNotFoundException {
            randomSeed = in.readLong();
            times = in.readInt();
            // TODO Recreate property from signature
        }
    }
    
    private ArrayList<TestEvent> testEvents = new ArrayList<TestEvent>();
    
    void addTestEvent(Method property, long randomSeed, int times) {
        testEvents.add(new TestEvent(property, randomSeed, times));
    }
    
    void playBack(TestRun testRun) {
        for (TestEvent e : testEvents) {
            testRun.runOn(e.property, e.times, e.randomSeed);
        }
    }
}
