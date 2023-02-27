package fields;

import java.io.IOException;

import io.Reader;

/** Field for an int primitive */
public class IntField extends Field {
	int fieldValue;
	
	public IntField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object getField() {
		return (Integer) fieldValue;
	}
	
	@Override
	public void read() throws IOException {
		fieldValue = Reader.din.readInt();
	}

	@Override
	public IntField clone() {
		return new IntField(fieldName);
	}
}
