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
 * <p>
 * Properties tested during the recording must be available on the classpath when
 * a recording is unserialised.
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
            out.writeUTF(property.getDeclaringClass().getCanonicalName());
            out.writeUTF(property.getName());
            out.writeInt(property.getParameterTypes().length);
            for (Class<?> paramT : property.getParameterTypes()) {
                out.writeUTF(paramT.getCanonicalName());
            }
        }
        
        private void readObject(ObjectInputStream in) throws IOException, 
                                                      ClassNotFoundException {
            randomSeed = in.readLong();
            times = in.readInt();
            String propName = in.readUTF();
            Class<?> declaringClass = Class.forName(in.readUTF());
            Class<?>[] paramTs = new Class<?>[in.readInt()];
            for (int pIdx=0; pIdx < paramTs.length; pIdx++) {
                paramTs[pIdx] = Class.forName(in.readUTF());
            }
            
            try {
                property = declaringClass.getMethod(propName, paramTs);
            } catch (NoSuchMethodException e) {
                throw new IOException("Can't recreate recording - no such property "
                        + declaringClass.getName() + "." + propName + " in classpath.");
            }
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
