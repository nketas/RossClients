package rossProxyXMLParser;


public class Ross {
	private String Id; 
	private String Description;
	private String Location;
	private String Keywords;
	private String RossType;
	public String getID() {
		return Id;
	}

	public void setID(String iD) {
		Id = iD;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getKeywords() {
		return Keywords;
	}

	public void setKeywords(String keywords) {
		Keywords = keywords;
	}

	public Ross(String type)
	{
		if(type.equalsIgnoreCase("robject")||type.equalsIgnoreCase("rsurface")||type.equalsIgnoreCase("rspace"))
			setRossType(type);
		else
			System.err.println("Invalid Ross type");
	}

	@Override
	public String toString() {
		return "ID: " + Id;
	}

	public void setRossType(String rossType) {
		RossType = rossType;
	}

	public String getRossType() {
		return RossType;
	}
}
