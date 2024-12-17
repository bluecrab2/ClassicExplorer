package fields;

import java.io.IOException;

import io.Reader;

/** Field for a short primitive */
public class ShortField extends Field {
	short fieldValue;
	
	public ShortField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object getField() {
		return (Short) fieldValue;
	}
	
	@Override
	public void read() throws IOException {
		fieldValue = Reader.din.readShort();
	}

	@Override
	public ShortField clone() {
		return new ShortField(fieldName);
	}
}
