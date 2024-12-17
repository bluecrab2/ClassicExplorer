package fields;

import java.io.IOException;

import io.Reader;

/** Field for a double primitive */
public class DoubleField extends Field {
	double fieldValue;
	
	public DoubleField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object getField() {
		return (Double) fieldValue;
	}
	
	@Override
	public void read() throws IOException {
		fieldValue = Reader.din.readDouble();
	}

	@Override
	public DoubleField clone() {
		return new DoubleField(fieldName);
	}
}
