package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
	
	static FileWriter log = null;
	static BufferedWriter out = null;
	public Log() 
	{
		try{
			
		
		log = new FileWriter("C:\\Ross\\client.log",true);
		out = new BufferedWriter(log);
		}
		catch(IOException e)
		{
			
		}
	}
	
	public static void debug(String error)
	{
		try {
			
			if(out != null)
			{
				
				out.write(error);
				out.write("\n");
				out.flush();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("BufferedWriter is null");
			e.printStackTrace();
		}
	}
}
