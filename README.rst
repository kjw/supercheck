SuperCheck - A Java implementation of QuickCheck
=================================================================================

SuperCheck is a Java 1.5 and greater implementation of QuickCheck. QuickCheck is
a Haskell testing library that tests invariants with arbitrary data.

How does it work?
=================================================================================

With SuperCheck, as with QuickCheck, one tests code by defining invariants -
predicates that must always hold true for a piece of code, for any data the
code may operate on. During a test run, each declared invariant (called a 
property in SuperCheck) is executed multiple times, with randomly generated data.

Defining arbitrary() constructors
=================================================================================

To test a class we need to let SuperCheck know how to create a random instance.
We do this by defining an arbitrary() static method in the class. This is an
example for a possible "Point2D" class::

  public static Point2D arbitrary(Gen gen) {
      switch (gen.select(0.1, 0.9)) {
      case 0:
          return Point2D.ZERO;
      case 1: default:
          return new Point2D(gen.arbNFloat(), gen.arbNFloat());
      }
  }
  
This arbitrary definition returns a "zero" point with at least 10% probability 
(actually a tiny bit higher because the second case may inadvertently return
a zero point). Having zero points turn up 10% of the time will improve the chances
of a test hitting a boundry case. If our arbitrary method consisted only of the
body of the second case then we would get very few zero points.

All arbitrary methods must be called ``arbitrary``, must be public, must be static
and must take a Gen object as their first and only argument. Their return type
must be their enclosing type.

Arbitrary definitions may of course invoke other arbitrary methods if the class
type being constructed is a compound of other class types. Arbitrary primitive
values can be generated using the Gen instance passed to the method. The Gen
instance also provides methods to ease the construction of arbitrary objects
(the example above makes use of select()).

Defining properties
=================================================================================

Once we have arbitrary method definitions for classes we want to test, we can
define invariants for the class's code. Let us imagine that the Point2D class
from the example in the section above defines add(), subtract(), isZero(),
negative() and equals() methods. We could write a few invariants about the 
mathematics of Point2Ds::

  Point2DInvariants.java
  ----------------------
  
  public static boolean prop_addSubEquality(Point2D one) {
      return one.add(one).subtract(one).equals(one);
  }
    
  public static boolean prop_subAddEquality(Point2D one) {
      return one.subtract(one).add(one).equals(one);
  }
    
  public static boolean prop_negNegEquality(Point2D one) {
      return one.negative().negative().equals(one);
  }
    
  public static boolean prop_subSelfZero(Point2D one) {
      return one.subtract(one).isZero();
  }
  
Properties can be declared anywhere, in any class, but they must follow some 
rules. Though they can take any number of arguments, all arguments they do take 
must be classes that declare arbitrary methods. The property must have a name
beginning with ``prop_``, must be static, must be public and must return a boolean.
A property should return true it it holds for the given arguments. If it does not
hold, it should return false.

Executing a test run
=================================================================================

We can run the invariants declared in the Point2DInvariants class above by
invoking a single Java line::

  new TestRun().runOn(Point2DInvariants.class, 10000);
  
This will run each property found in the Point2DInvariants class 10,000 times,
and will output something like::

  Running prop_addSubEquality 10000 times...
      prop_addSubEquality done.
  Running prop_subAddEquality 10000 times...
      prop_subAddEquality done.
  Running prop_negNegEquality 10000 times...
      prop_negNegEquality done.
  Running prop_subSelfZero 10000 times...
      prop_subSelfZero done.
      
Any failure will result in the tests for the failed property to stop, and the
data with which the property failed is printed, using toString(). (So it is a
good idea for classes that implement arbitrary() to also have a good implementation
of toString() that produces a concice and identifiable representation.) TestRun
can be told to print out the data from successful property executions, like so::

  new TestRun().setVerbose(true).runOn(Point2DInvariants.class, 10000);
  
It can also be told to continue testing a property even after it has failed for
some data::

  new TestRun().setcontProp(true).runOn(Point2DInvariants.class, 10000);
  
Recording a test run
=================================================================================

Test runs are recorded automatically and these recording can be retrieved via the 
``getRecord()`` method on ``TestRun``. This will return a serializable ``Recording``
object which can be used to play back the test run. Playback will interrogate the 
same properties, in the same order, using the same data::

  TestRun trOne = new TestRun();
  trOne.runOn(Point2DInvariants.class, 10000);
  trOne.runOn(QuickSortInvariants.class, 10000);
  
  Recording recording = trOne.getRecording();
  TestRun trTwo = new trTwo();
  trTwo.runRecording(recording);
  
In this example, ``trOne`` and ``trTwo`` perform the same tests.