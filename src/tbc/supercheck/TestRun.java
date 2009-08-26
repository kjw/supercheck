package tbc.supercheck;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A TestRun object represents the ability to perform tests on one or a set of
 * properties (invariant predicates). A property can be defined in any class,
 * and can be run using the {@link #runOn(Class, String, int)} method of this 
 * class. Alternatively, all of the properties declared in a single class may 
 * be run using the overload {@link #runOn(Class, int)}.
 * 
 * @author Karl Jonathan Ward <karl.j.ward@googlemail.com>
 */
public class TestRun {
    
    private boolean continuePropAfterFail = false;
    
    private boolean printSuccessRuns      = false;
    
    /**
     * Set to true to make TestRun print out details of successful property 
     * tests, not just those that fail. E.g.:
     * 
     * <pre>new TestRun().setVerbose(true).runOn(SomeInvariants.class, 1000);</pre>
     */
    public TestRun setVerbose(boolean b) {
        printSuccessRuns = b;
        return this;
    }
    
    /**
     * Set to true to continue to test a property after it has failed for
     * some data. E.g.:
     * 
     * <pre>new TestRun().setContProp(true).runOn(SomeInvariants.class, 1000);</pre>
     */
    public TestRun setContProp(boolean b) {
        continuePropAfterFail = b;
        return this;
    }
	
    /**
     * Run arbitrary data through all the properties defined in a single class.
     * Any method in the class "invariants" whose simple name begins with the
     * prefix "prop_" will be considered as a test property, and will be executed
     * with arbitrary data, "timesForEach" times.
     */
    public void runOn(Class<?> invariants, int timesForEach) throws TestException {
        for (Method m : invariants.getMethods()) {
            if (m.getName().startsWith("prop_")) {
                runOn(m, timesForEach);
            }
        }
    }
    
    /**
     * Run arbitrary data through a particular property. The property name
     * specified should be the full simple name of the method that represents 
     * the property (including the "prop_" prefix). For example:
     * 
     * <pre>runOn(QuickSortInvariants.class, "prop_idempotent", 10000);</pre>
     */
    public void runOn(Class<?> invariants, String propName, int times) throws TestException {
        Method[] ms = invariants.getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().equals(propName)) {
                runOn(m, times);
            }
        }
    }

    /**
     * Similar to {@link #runOn(Class, String, int) except properties must be
     * specified by a reflected reference instead of a name.
     */
    public void runOn(Method prop, int times) throws TestException {
        System.out.println("Running " + prop.getName() + " " + times + " times...");
        
        Gen gen = new Gen();

        for (int i=0; i<times; i++) {
            Object[] params = new Object[prop.getParameterTypes().length];

            for (Class<?> paramT : prop.getParameterTypes()) {
                try {
                    Method maker = paramT.getDeclaredMethod("arbitrary", 
                            new Class[] { Gen.class });
                    params[0] = maker.invoke(null, new Object[] { gen });
                } catch (NoSuchMethodException e) {
                    throw new TestException("Missing arbitrary() definition on: "
                            + paramT.getName());
                } catch (InvocationTargetException e) {
                    throw new TestException(e.toString());
                } catch (IllegalAccessException e) {
                    throw new TestException(e.toString());
                }
            }

            if (!runOn(prop, params)) {
                System.out.println("\t! Failed for params: ");
                printParamList(params, System.out, "\t");
                System.out.println();
                
                if (!continuePropAfterFail) {
                    break;
                }
                
            } else if (printSuccessRuns) {
                System.out.println("\t* Passed for params: ");
                printParamList(params, System.out, "\t");
                System.out.println();
            }
        }

        System.out.println("\t" + prop.getName() + " done.");
    }

    private boolean runOn(Method prop, Object[] params) throws TestException {
        try {
            return (Boolean) prop.invoke(null, params);
        } catch (InvocationTargetException e) {
            throw new TestException(e.toString());
        } catch (IllegalAccessException e) {
            throw new TestException(e.toString());
        }
    }

    private void printParamList(Object[] ary, PrintStream out, String prefix) {
        for (int i=0; i<ary.length; i++) {
            out.println(prefix + (i+1) + ". " + ary[i]);
        }
    }
	
}
