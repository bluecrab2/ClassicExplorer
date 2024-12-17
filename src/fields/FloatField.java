package fields;

import java.io.IOException;

import io.Reader;

/** Field for a float primitive */
public class FloatField extends Field {
	float fieldValue;
	
	public FloatField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object getField() {
		return (Float) fieldValue;
	}
	
	@Override
	public void read() throws IOException {
		fieldValue = Reader.din.readFloat();
	}
	
	@Override
	public FloatField clone() {
		return new FloatField(fieldName);
	}
}
