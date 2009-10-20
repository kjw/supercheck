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

	private static final String[] TYPE_NAMES = {
		"String",
		"Float",
		"Int",
		"Boolean"
	};
	
	private enum ParamType {
		STRING,
		FLOAT,
		INT,
		BOOLEAN
	}

	private class ParamEntry {
		private Object value;
		private ParamType type;
		
		@Override
		public String toString() {
			return value.toString() + " :" + TYPE_NAMES[type.ordinal()];
		}
	}

	private Hashtable<String, ParamEntry> parameters = new Hashtable<String, ParamEntry>();
	
	@Override
	public String toString() {
		return parameters.toString();
	}

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
	
	public void setBoolean(String name, boolean val) {
		ParamEntry pe = new ParamEntry();
		pe.value = new Boolean(val);
		pe.type = ParamType.BOOLEAN;
		parameters.put(name, pe);
	}

	public float getFloat(String name, float otherwise) {
		ParamEntry pe = parameters.get(name);

		if (pe != null && pe.type == ParamType.FLOAT) {
			return (Float)pe.value;
		} else {
			return otherwise;
		}
	}

	public int getInt(String name, int otherwise) {
		ParamEntry pe = parameters.get(name);

		if (pe != null && pe.type == ParamType.INT) {
			return (Integer)pe.value;
		} else {
			return otherwise;
		}
	}

	public String getStr(String name, String otherwise) {
		ParamEntry pe = parameters.get(name);

		if (pe != null && pe.type == ParamType.STRING) {
			return (String)pe.value;
		} else {
			return otherwise;
		}
	}
	
	public boolean getBoolean(String name, boolean otherwise) {
		ParamEntry pe = parameters.get(name);

		if (pe != null && pe.type == ParamType.BOOLEAN) {
			return (Boolean)pe.value;
		} else {
			return otherwise;
		}
	}
}
