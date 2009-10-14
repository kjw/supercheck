package tbc.supercheck;

import java.util.Hashtable;

/**
 * A ParamBunch is a mutable set of paramters - string, value pairs, where the
 * value can be of different types. Parameters must be accessed via the getter
 * of the same type as the setter used to set them. Only one parameter for
 * a given name may exist, regardless of typing.
 * 
 * @author Karl Jonathan Ward <karl.j.ward@googlemail.com>
 */
public class ParameterBunch {

	private enum ParamType {
		STRING,
		FLOAT,
		INT
	}
	
	private class ParamEntry {
		private Object value;
		private ParamType type;
	}
	
	private Hashtable<String, ParamEntry> parameters = new Hashtable<String, ParamEntry>();
	
	public void setFloat(String name, float val) {
		ParamEntry pe = new ParamEntry();
		pe.value = new Float(val);
		pe.type = ParamType.FLOAT;
    	parameters.put(name, pe);
    }
	
	public void setInt(String name, int val) {
		ParamEntry pe = new ParamEntry();
		pe.value = new Integer(val);
		pe.type = ParamType.INT;
    	parameters.put(name, pe);
    }
	
	public void setStr(String name, String val) {
		ParamEntry pe = new ParamEntry();
		pe.value = val;
		pe.type = ParamType.STRING;
    	parameters.put(name, pe);
    }
    
    public float getFloat(String name) {
    	ParamEntry pe = parameters.get(name);
    	
    	if (pe != null && pe.type == ParamType.FLOAT) {
    		return (Float)pe.value;
    	} else {
    		return 0.0f;
    	}
    }
    
    public int getInt(String name) {
    	ParamEntry pe = parameters.get(name);
    	
    	if (pe != null && pe.type == ParamType.INT) {
    		return (Integer)pe.value;
    	} else {
    		return 0;
    	}
    }
    
    public String getStr(String name) {
    	ParamEntry pe = parameters.get(name);
    	
    	if (pe != null && pe.type == ParamType.STRING) {
    		return (String)pe.value;
    	} else {
    		return "";
    	}
    }
}
