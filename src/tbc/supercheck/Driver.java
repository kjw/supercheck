package tbc.supercheck;

public class Driver
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        new TestRun().runOn(Driver.class, 1000);
    }
    
    public static boolean prop_checkArbArray(Driver driver) {
        return true;
    }
    
    public static Driver arbitrary(Gen gen) {
        Other[] others = gen.arbArray(Other.class);
        return new Driver();
    }
    
    

}

class Other {
    public static Other arbitrary(Gen gen) {
        return new Other();
    }
}
