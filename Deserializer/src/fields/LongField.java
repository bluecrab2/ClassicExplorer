package fields;

import java.io.IOException;

import io.Reader;

/** Field for a long primitive */
public class LongField extends Field {
	long fieldValue;
	
	public LongField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object getField() {
		return (Long) fieldValue;
	}

	@Override
	public void read() throws IOException {
		fieldValue = Reader.din.readLong();
	}

	@Override
	public LongField clone() {
		return new LongField(fieldName);
	}
}
