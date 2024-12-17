package fields;

import java.io.IOException;

import io.Reader;

/** Field for a char primitive */
public class CharField extends Field {
	char fieldValue;
	
	public CharField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object getField() {
		return (Character) fieldValue;
	}
	
	@Override
	public void read() throws IOException {
		fieldValue = Reader.din.readChar();
	}

	@Override
	public CharField clone() {
		return new CharField(fieldName);
	}
}
