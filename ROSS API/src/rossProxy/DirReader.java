/*
 * Ross Proxy project
 * 
 * @author Andy Wu
 * 
 */
package rossProxy;

import java.io.*;

public class DirReader {
	private String dirName = null;
	private File dir = null;

	public DirReader(String d) {
		dirName = d;
	}

	public String processDir() {
		File file = null;
		String content = "";
		try {
			dir = new File(dirName);
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					file = files[i];
					String filename = file.getName();
					if (file.isDirectory()) {
//						System.err.println("<DIR>" + filename);
						;
					} else if (file.isFile() && filename.endsWith(".xml")) {
//						System.err.println(dir.getCanonicalPath()+System.getProperty("file.separator")+filename);
						XMLReader xr = new XMLReader(dir.getCanonicalPath()+System.getProperty("file.separator")+filename);
						content+=XMLReader.getRossSpaceFromXML(XMLReader.replaceIP(xr.getContent()))+"\n";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public static void main(String[] argv) {
		DirReader dr = new DirReader("./xml");
		dr.processDir();
	}
}
