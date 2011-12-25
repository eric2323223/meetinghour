package eric.android.meetinghour.activity;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import eric.android.meetinghour.model.City;

class GeoInfoHandler extends DefaultHandler {

	private boolean isLatitude, isLongitude, isCountry, isLine1, isLine2,
			isFound;
	private boolean notFound = true;

	private City _data;

	public City getData() {
		if (notFound) {
			return null;
		} else {
			return _data;
		}
	}

	@Override
	public void startDocument() throws SAXException {
		_data = new City();
	}

	@Override
	public void endDocument() throws SAXException {

	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		if (localName.equals("latitude")) {
			isLatitude = true;
		} else if (localName.equals("longitude")) {
			isLongitude = true;
		} else if (localName.equals("country")) {
			isCountry = true;
		} else if (localName.equals("line1")) {
			isLine1 = true;
		} else if (localName.equals("line2")) {
			isLine2 = true;
		} else if (localName.equals("Found")) {
			isFound = true;
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("latitude")) {
			isLatitude = false;
		} else if (localName.equals("longitude")) {
			isLongitude = false;
		} else if (localName.equals("country")) {
			isCountry = false;
		} else if (localName.equals("line1")) {
			isLine1 = false;
		} else if (localName.equals("line2")) {
			isLine2 = false;
		} else if (localName.equals("Found")) {
			isFound = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		String chars = new String(ch, start, length);
		chars = chars.trim();
		if (isFound) {
			if (chars.equals("0")) {
				notFound = true;
			} else {
				notFound = false;
			}
		} else if (isLatitude) {
			_data.setLatitude(new Float(chars).floatValue());
		} else if (isLongitude) {
			_data.setLongitude(new Float(chars).floatValue());
		} else if (isCountry) {
			_data.setCountry(chars);
		} else if (isLine1) {
			if (chars.length() > 0) {
				_data.setName(chars);
			}
		} else if (isLine2) {
			if (_data.getName() == null && chars.length() > 0) {
				_data.setName(chars);
			}
		}
	}
}

