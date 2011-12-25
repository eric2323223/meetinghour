package eric.android.meetinghour.activity;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import eric.android.meetinghour.model.City;

class TimeZoneHandler extends DefaultHandler {

	private boolean isOffset;

	private City _data;

	public City getData() {
		return _data;
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

		if (localName.equals("offset")) {
			isOffset = true;
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		Log.v("endElement", localName);

		if (localName.equals("offset")) {
			isOffset = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		String chars = new String(ch, start, length);
		chars = chars.trim();

		if (isOffset) {
			_data.setOffset(new Integer(chars).intValue());
		}
	}
}
