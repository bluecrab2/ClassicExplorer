package fields;

import java.io.IOException;
import java.util.ArrayList;

public class Class {
	/** Name of the class, unless its a string in which case it will be
	 * the string. */
	private String name;
	/** All the fields of the class. */
	private ArrayList<Field> fields;
	/** The super class of the class. */
	private Class superClass;
	/** The serialVersionUID of the class*/
	private long serialVersionUID;

	/**
	 * Construct the class with the name string
	 */
	public Class(String inputName) {
		name = inputName;
		fields = new ArrayList<Field>();
	}
	
	/**
	 * Add the given field to the list of fields.
	 */
	public void addField(Field f) {
		fields.add(f);
	}
	
	/**
	 * Read the data values into the class' fields and
	 * its super class.
	 */
	public void read() throws IOException {
		//Read super class values first
		if(superClass != null) {
			superClass.read();
		}
		
		//Read in field values
		for(Field f : fields) {
			f.read();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}
	
	public Class getSuperClass() {
		return superClass;
	}
	
	public void setSuperClass(Class superClass) {
		this.superClass = superClass;
	}

	public long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public void setSerialVersionUID(long serialVersionUID) {
		this.serialVersionUID = serialVersionUID;
	}
	
	/**
	 * Makes a new class with same name and fields 
	 */
	public Class clone() {
		Class clone = new Class(this.getName());
		ArrayList<Field> fields = this.getFields();
		for(Field f : fields) {
			clone.addField(f.clone());
		}
		clone.setSuperClass(superClass);
		clone.setSerialVersionUID(serialVersionUID);
		
		return clone;
	}
}
