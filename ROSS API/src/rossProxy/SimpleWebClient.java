package rossProxy;
import java.io.*;
import java.net.*;

/**
 * An application that opens a connection to a Web server and reads
 * a single Web page from the connection.
*/
public class SimpleWebClient {
    public static void main(String args[])
    {
        try
        {
            // Open a client socket connection
            Socket clientSocket1 = new Socket("www.google.com", 80);
            System.err.println("Client1: " + clientSocket1);

            // Get a Web page
            getPage(clientSocket1);
        }
        catch (UnknownHostException uhe)
        {
            System.err.println("UnknownHostException: " + uhe);
        }
        catch (IOException ioe)
        {
            System.err.println("IOException: " + ioe);
        }
    }

    /**
     * Request a Web page using the passed client socket.
     * Display the reply and close the client socket.
     */
    public static void getPage(Socket clientSocket)
    {
        try
        {
            // Acquire the input and output streams
            DataOutputStream outbound = new DataOutputStream(
                clientSocket.getOutputStream() );
            DataInputStream inbound = new DataInputStream(
                clientSocket.getInputStream() );

            // Write the HTTP request to the server
            outbound.writeBytes("GET / HTTP/1.0\r\n\r\n");

            // Read the response
            String responseLine;
            while ((responseLine = inbound.readLine()) != null)
            {
                // Display each line to the console
                System.err.println(responseLine);

                // This code checks for EOF.  There is a bug in the
                // socket close code under Win 95.  readLine() will
                // not return null when the client socket is closed
                // by the server.
                if ( responseLine.indexOf("</HTML>") != -1 )
                    break;
            }

            // Clean up
            outbound.close();
            inbound.close();
            clientSocket.close();
        }
        catch (IOException ioe)
        {
            System.err.println("IOException: " + ioe);
        }
    }
}