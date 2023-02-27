package fields;

import java.io.IOException;

import io.Reader;

/** Field for a boolean primitive */
public class BooleanField extends Field {
	boolean fieldValue;
	
	public BooleanField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object getField() {
		return (Boolean) fieldValue;
	}
	
	@Override
	public void read() throws IOException {
		fieldValue = Reader.din.readBoolean();
	}
	
	@Override
	public BooleanField clone() {
		return new BooleanField(fieldName);
	}
}
