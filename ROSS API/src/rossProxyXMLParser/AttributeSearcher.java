package rossProxyXMLParser;

import java.util.TreeMap;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AttributeSearcher extends DefaultHandler
{
	public String tagName, tagID;
	
	public AttributeSearcher(String tname, String tID)
	{
		tagName = tname;
		tagID = tID;
	}
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		if(qName.equalsIgnoreCase(tagName))
		{
			
		}
		
	}
}
