package fields;

import java.io.IOException;
import java.util.ArrayList;

import io.Reader;

public class ArrayField extends Field {
	String arrayDescriptor;
	ArrayList<Field> arrayContents;
	
	public ArrayField(String iFieldName, String iFieldValue) {
		fieldName = iFieldName;
		arrayDescriptor = iFieldValue;
		arrayContents = new ArrayList<Field>();
	}

	@Override
	public Object getField() {
		return arrayDescriptor + " (Array)";
	}
	
	public ArrayList<Field> getArray() {
		return arrayContents;
	}
	
	public void addField(Field f) {
		arrayContents.add(f);
	}
	
	@Override
	public void read() throws IOException {
		//Read the newArray from the grammar
		int tcArray = Reader.din.readUnsignedByte();
		if(tcArray != Reader.TC_ARRAY) {
			for(int i = 0; i < 10; i++) {
				System.out.println(Reader.din.readUnsignedByte() + ",");
			}
			throw new IllegalArgumentException("Invalid stater of array. Was: " + tcArray);
		}
		
		//Read classDesc
		Class c = Reader.readClassDesc();
		
		//Read 
		Reader.handles.add(c);
		
		//Get the type of field from the second character (first is [)
		char type = arrayDescriptor.charAt(1);
		
		//Get the array size
		int count = Reader.din.readInt();
		
		//Stores the type of field that must be read
		Field f;
		
		if('B' == type) {
			f = new ByteField(fieldName);
		} else if('C' == type) {
			f = new CharField(fieldName);
		} else if('D' == type) {
			f = new DoubleField(fieldName);
		} else if('F' == type) {
			f = new FloatField(fieldName);
		} else if('I' == type) {
			f = new IntField(fieldName);
		} else if('J' == type) {
			f = new LongField(fieldName);
		} else if('S' == type) {
			f = new ShortField(fieldName);
		} else if('Z' == type) {
			f = new BooleanField(fieldName);
		} else if('L' == type) {
			f = new ClassField(fieldName, arrayDescriptor.substring(2, arrayDescriptor.length() - 1));
			((ClassField) f).setClassField(c);
		} else {
			//Not handling arrays of arrays (type = [) since they're not found in classic files
			throw new IllegalArgumentException("Invalid array descriptor. Was: " + type);
		}

		for(int i = 0; i < count; i++) {
			Field currentF = f.clone();
			currentF.setFieldName(currentF.getFieldName() + "[" + i + "]");
			currentF.read();
			arrayContents.add(currentF);
		}
	}

	@Override
	public ArrayField clone() {
		return null; //Stop infinite clone loop
	}
}
