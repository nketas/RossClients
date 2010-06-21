package rossProxy;

import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class DeviceProfileHandler extends DefaultHandler {
	private StringBuffer tempBuffer;
	private ArrayList<String> mediaObjectName;
	private String imageName;
	private int x;
	private int y;
	private int z;
	private int size;

	private int fiducial;
	private boolean inPiece = false;
	private boolean inImageName = false;
	private boolean inCollagepiece = false;
	private boolean inMediaObject = false;
	private boolean inX = false;
	private boolean inY = false;
	private boolean inZ = false;
	private boolean inSize = false;

	private boolean inFiducial = false;
	public void startElement(String uri, String lName, String qName, Attributes attrs) {
		System.err.println("startElement uri = "+uri);
		System.err.println("startElement lName = "+lName);
		System.err.println("startElement qName = "+qName);
		System.err.println("startElement attrs : "+attrs.getLocalName(0)+"="+attrs.getValue(0));
		try {

			if (lName.equals("rossXML")) {
				inImageName = true;
				tempBuffer = new StringBuffer();
			}
			if (lName.equals("collagepiece")) {
				inCollagepiece = true;
				tempBuffer = new StringBuffer();
			}
			if (lName.equals("scenes")) {
				mediaObjectName = new ArrayList<String>();
			}
			if (lName.equals("mediaobject")) {
				inMediaObject = true;
				tempBuffer = new StringBuffer();
			}
			if (lName.equals("x")) {
				inX = true;
				tempBuffer = new StringBuffer();
			}
			if (lName.equals("y")) {
				inY = true;
				tempBuffer = new StringBuffer();
			}
			if (lName.equals("z")) {
				inZ = true;
				tempBuffer = new StringBuffer();
			}
			if (lName.equals("size")) {
				inSize = true;
				tempBuffer = new StringBuffer();
			}
			if (lName.equals("fiducial")) {
				inFiducial = true;
				tempBuffer = new StringBuffer();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void endElement(String uri, String lName, String qName) throws SAXException {
		System.err.println("endElement uri = "+uri);
		System.err.println("endElement lName = "+lName);
		System.err.println("endElement qName = "+qName);
		if (lName.equals("piece")) {
			inPiece = false;
		}
		if (lName.equals("imagename")) {
			inImageName = false;
			imageName = tempBuffer.toString();
			imageName = "Images/" + imageName;
		}
		if (lName.equals("mediaobject")) {
			inMediaObject = false;
			mediaObjectName.add(tempBuffer.toString());
		}
		if (lName.equals("collagepiece")) {
			inCollagepiece = false;

		}
		if (lName.equals("x")) {
			inX = false;
			x = Integer.parseInt(tempBuffer.toString());
		}
		if (lName.equals("y")) {
			inY = false;
			y = Integer.parseInt(tempBuffer.toString());
		}
		if (lName.equals("z")) {
			inZ = false;
			z = Integer.parseInt(tempBuffer.toString());

		}
		if (lName.equals("size")) {
			inSize = false;
			size = Integer.parseInt(tempBuffer.toString());
		}
		if (lName.equals("fiducial")) {
			inFiducial = false;
			fiducial = Integer.parseInt(tempBuffer.toString());
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (inPiece) {
			if (inImageName) {
				for (int x = 0; x < length; x++) {
					tempBuffer.append(ch[x + start]);
				}
			}
			if (inMediaObject) {
				for (int x = 0; x < length; x++) {
					tempBuffer.append(ch[x + start]);
				}
			}
			if (inX) {
				for (int x = 0; x < length; x++) {
					tempBuffer.append(ch[x + start]);
				}
			}
			if (inCollagepiece) {
				for (int x = 0; x < length; x++) {
					tempBuffer.append(ch[x + start]);
				}
			}
			if (inY) {
				for (int x = 0; x < length; x++) {
					tempBuffer.append(ch[x + start]);
				}
			}
			if (inZ) {
				for (int x = 0; x < length; x++) {
					tempBuffer.append(ch[x + start]);
				}
			}
			if (inSize) {
				for (int x = 0; x < length; x++) {
					tempBuffer.append(ch[x + start]);
				}
			}
			if (inFiducial) {
				for (int x = 0; x < length; x++) {
					tempBuffer.append(ch[x + start]);
				}
			}
		}
	}

	public ArrayList<String> getMediaObjectName() {
		return mediaObjectName;
	}
}
