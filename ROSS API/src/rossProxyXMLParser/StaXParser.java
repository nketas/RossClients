package rossProxyXMLParser;

				


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import log.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class StaXParser extends DefaultHandler
{

	static final String DATE = "date";
	static final String ITEM = "item";
	static final String MODE = "mode";
	static final String UNIT = "unit";
	static final String CURRENT = "current";
	static final String INTERACTIVE = "interactive";
	static final String ROBJECT = "robject";
	static final String RXML = "rossXML";
	static final String RossFile = "Ross.xml";
	URL rossServer;
	JTree RossTree;
	int NestingLevel;
	private String tempVal;
	XMLEventReader EventReader;
	XMLInputFactory InputFactory;
	MutableTreeNode Tree;
	//RossBranchNode RObj;
	Ross tempObject;
	Stack<Ross> RossStack;
	ArrayList<Ross> RossList;
	
	private String CurrentRossType;
	
	
	public StaXParser(InetAddress RossDeviceIP)
	{
		
		
		NestingLevel = 0;
		RossStack = new Stack();
		RossList = new ArrayList();
		try {
		rossServer = new URL("http://" + RossDeviceIP.getHostAddress()+ ":7033/");
		URLConnection rossFile = rossServer.openConnection();
		BufferedReader in  = new  BufferedReader(new InputStreamReader(rossFile.getInputStream()));
		BufferedWriter out = new BufferedWriter(new FileWriter("./Ross.xml"));	
		String RossFileContents = "";
	
		if(in.ready())
		{
			String line = null;
			while((line = in.readLine()) != null)
			{
				String NextLine = line;
			//	System.out.println(NextLine);
				RossFileContents = RossFileContents + NextLine + "\n";
			}
			in.close();
			Log.debug(RossFileContents);
			out.write(RossFileContents);
			out.close();
		}
		
		//	InputStream In = in.//new FileInputStream(RossFile);
		
	
		Tree = new DefaultMutableTreeNode();
		RossTree = new JTree(Tree);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			
		}
		
		//parseRoss();
	}
	
	public StaXParser()
	{
		NestingLevel = 0;
		RossStack = new Stack();
		RossList = new ArrayList();

	}
		public  void parseRoss(String XMLContent) {
			
			//get a factory
			SAXParserFactory spf = SAXParserFactory.newInstance();
			try {
			
				//get a new instance of parser
				SAXParser sp = spf.newSAXParser();
				
				//parse the file and also register this class for call backs
				sp.parse(XMLContent, this);
			
			}catch(SAXException se) {
				se.printStackTrace();
			}catch(ParserConfigurationException pce) {
				pce.printStackTrace();
			}catch (IOException ie) {
				ie.printStackTrace();
			}
		}
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			tempVal = new String(ch,start,length);
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			//reset
			
			
			
			//System.out.println(NestingLevel);
			if(qName.equalsIgnoreCase("rxml")) {
				//System.out.println("If 1");
				//System.out.println("rossxml found");
				
				//create a new instance of employee
				//tempEmp = new Employee();
				//tempEmp.setType(attributes.getValue("type"));
				
			}
			else if(qName.equalsIgnoreCase("robject"))
			{
				//System.out.println("If 2");
				//System.out.println("robject found");
				if(NestingLevel >= 1 && tempObject != null)
				{
					//System.out.println("If 2-1");
					RossStack.push(tempObject);
					RossList.add(tempObject);
					//System.out.println(tempObject.getID());
					tempObject = null;
				}
				CurrentRossType = "robject";
				tempObject = new Ross("robject");
				NestingLevel++;
			}
			else if(qName.equalsIgnoreCase("rsurface"))
			{
				
				//System.out.println("If 3");
				if(NestingLevel >= 1 && tempObject != null)
				{
					//System.out.println("If 3-1");
					RossStack.push(tempObject);
					RossList.add(tempObject);
					//System.out.println(tempObject.getID());
					tempObject = null;
				}
				//System.out.println("rsurfacefound");
				CurrentRossType = "rsurface";
				tempObject = new Ross("rsurface");
				NestingLevel++;
			}
			else if(qName.equalsIgnoreCase("rspace"))
			{
				//System.out.println("If 4");
				//System.out.println("rspace found");
				if(NestingLevel >= 1 && tempObject != null)
				{
					//System.out.println("If 4-1");
					RossStack.push(tempObject);
					RossList.add(tempObject);
					//System.out.println(tempObject.getID());
					tempObject = null;
				}
				CurrentRossType = "rspaces";
				tempObject = new Ross("rspace");
				NestingLevel++;
			}
			
			
		}
		public void endElement(String uri, String localName, String qName) throws SAXException {
			
		
			if(qName.equalsIgnoreCase("id"))
			{
				//System.out.println("If 5");
				//System.out.println("id found");
				tempObject.setID(tempVal);
				//System.out.println(tempVal);
			}
			else if(qName.equalsIgnoreCase("description"))
			{
				//System.out.println("If 6");
				//System.out.println("description found");
				tempObject.setDescription(tempVal);
			}
			else if(qName.equalsIgnoreCase("keywords"))
			{
				//System.out.println("If 7");
				//System.out.println("keywords found");
				tempObject.setKeywords(tempVal);
			}
			else if(qName.equalsIgnoreCase("location"))
			{
				//System.out.println("If 8");
				//System.out.println("location found");
				tempObject.setLocation(tempVal);
			}
			else if (qName.equalsIgnoreCase("rxml") || qName.equalsIgnoreCase("robject") || qName.equalsIgnoreCase("rsurface") || qName.equalsIgnoreCase("rspace"))
			{
				//System.out.println("If 9");
				NestingLevel--;
				
			}
			if(NestingLevel == 0)
			{
				//System.out.println("If 10");
				RossList.add(tempObject);
				if(RossStack.isEmpty())
				{}	
				else
				{
					//System.out.println("If 11");
					//tempObject = RossStack.pop();
					//System.out.println(tempObject.getID());
					for(int i = 0; i < RossList.size(); i++)
						System.out.println(RossList.get(i).getID());
				}
			}
		}
}






