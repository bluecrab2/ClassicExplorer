package fields;

import java.io.IOException;
import java.util.ArrayList;

import io.Reader;

public class ClassField extends Field {
	private String className;
	private Class classField;
	
	//Special case string for String class
	private String s;
	
	//Special case list for List class
	private ArrayList<Class> l = new ArrayList<Class>();
	
	public static final String STRING = "java/lang/String";
	public static final String LIST = "java/util/List";
	public static final String ARRAY_LIST = "java/util/ArrayList";
	public static final String RANDOM = "java/util/Random";
	
	public ClassField(String iFieldName, String iClassName) {
		fieldName = iFieldName;
		className = iClassName;
	}

	@Override
	public Object getField() {
		return className;
	}
	
	public Class getClassField() {
		return classField;
	}
	
	public ArrayList<Class> getArrayList() {
		return l;
	}
	
	public void setString(String s) {
		this.s = s;
	}
	
	public String getString() {
		return s;
	}
	
	public boolean isList() {
		return LIST.equals(className) || ARRAY_LIST.equals(className);
	}
	
	public boolean isArrayList() {
		return ARRAY_LIST.equals(className);
	}
	
	public boolean isString() {
		return STRING.equals(className);
	}
	
	@Override
	public void read() throws IOException {
		if(isString()) {
			//Special case to read a string
			Class readObject = Reader.readObject();
			s = readObject.getName();
		} else if(isList()) {
			//Special case to read a list
			classField = Reader.readContent();
			
			//Stop reading if it's a null reference
			if(classField == null)
				return;
			
			int blockData = Reader.din.readUnsignedByte();
			if(blockData != Reader.TC_BLOCKDATA) {
				for(int i = 0; i < 5; i++) {
					System.out.println("nextbyte: " + Reader.din.readUnsignedByte());
				}
				throw new IllegalArgumentException("Block data begins incorrectly. Starts with: " + blockData);
			}
			
			int externalizable = Reader.din.readUnsignedByte();
			if(externalizable != Reader.SC_EXTERNALIZABLE) {
				throw new IllegalArgumentException("List is not externalizable.");
			}
			
			//Size of list
			int size = (Integer) classField.getFields().get(0).getField();
			
			//Read in unneeded int in ArrayList
			Reader.din.readInt();
			
			//Read each class
			for(int i = 0; i < size; i++) {
				Class readClass = Reader.readObject();
				l.add(readClass);
			}
			
			int endBlockData = Reader.din.readUnsignedByte();
			if(endBlockData != Reader.TC_ENDBLOCKDATA) {
				throw new IllegalArgumentException("Block data not properly ending. Final byte was: " + endBlockData);
			}
		} else if(RANDOM.equals(className)) {
			//Special case to read random
			classField = Reader.readContent();
			
			int endBlockData = Reader.din.readUnsignedByte();
			if(endBlockData != Reader.TC_ENDBLOCKDATA) {
				throw new IllegalArgumentException("Block data not properly ending");
			}
		} else {
			classField = Reader.readContent();
		}
	}
	
	@Override
	public ClassField clone() {
		ClassField cf = new ClassField(fieldName, className);
		cf.setClassField(classField);
		cf.setString(s);
		
		return cf;
	}
	
	public void setClassField(Class inputClass) {
		classField = inputClass;
	}
}
