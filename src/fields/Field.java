package fields;

import java.io.IOException;

/**
 * Abstract representation of a field including its name, a method
 * to get the value stored in it, a method to read the value into
 * the field, and a method to clone the field.
 * 
 * @author bluecrab2
 *
 */
public abstract class Field {
	/**
	 * The name of the field. For example,
	 * int x = 0; has a fieldName of "x". 
	 * Can be changed by subclasses.
	 */
	protected String fieldName;
	
	/** Get the fieldName */
	public String getFieldName() {
		return fieldName;
	}
	
	/** Set the fieldName */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	/** Get the object stored in the field. */
	public abstract Object getField();
	
	/** Use Reader's DataInputStream (din) to read 
	 * the value into the field. */
	public abstract void read() throws IOException;
	
	/** Create a clone of the field with the same name. */
	public abstract Field clone();
}
