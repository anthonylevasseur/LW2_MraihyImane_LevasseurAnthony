package validation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleErrorHandler implements ErrorHandler {
	
	private boolean errorOccured = false;
	
	public boolean hasError() {
		return errorOccured;
	}

	public void warning(SAXParseException exception) throws SAXException {
		errorOccured = true;
	}

	public void error(SAXParseException exception) throws SAXException {
		errorOccured = true;
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		errorOccured = true;
	}

}
