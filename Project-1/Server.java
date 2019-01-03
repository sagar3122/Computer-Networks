//Name:Sagar Sharma
//UTA ID:1001626958
//References
//1.Skeleton code provided for Server in java
//2.https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html
//3.https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
//4.https://docs.oracle.com/javase/7/docs/api/java/nio/file/Path.html
//5.https://docs.oracle.com/javase/7/docs/api/java/nio/file/Paths.html
//6.https://docs.oracle.com/javase/7/docs/api/java/util/StringTokenizer.html
//7.https://docs.oracle.com/javase/7/docs/api/java/io/FileInputStream.html
//8.https://docs.oracle.com/javase/7/docs/api/java/io/FileNotFoundException.html
//9.https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html
//10.https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
//11.https://docs.oracle.com/javase/7/docs/api/java/io/OutputStream.html
//12.https://docs.oracle.com/javase/7/docs/api/java/net/InetAddress.html
//13.https://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html
//14.https://docs.oracle.com/javase/7/docs/api/java/io/DataInputStream.html
//15.https://docs.oracle.com/javase/7/docs/api/java/io/DataOutputStream.html

//import all the built-in classes to be used in the program from java

import java.net.ServerSocket;  // implements server sockets
import java.net.Socket;   //implements client sockets
import java.nio.file.Path;   //represent a system dependent file path
import java.nio.file.Paths;  // return a Path by converting a path string or URI
import java.util.StringTokenizer; //for b_reaking strings into Tokens
import java.io.FileInputStream;//obtains input bytes from a file in a file system
import java.io.FileNotFoundException;// Signals that an attempt to open the file denoted by a specified pathname has failed
import java.io.InputStream;//representing an input stream of bytes
import java.io.InputStreamReader; // reads bytes and decodes them into characters using a specified charset
import java.io.OutputStream;  //represents an output stream of bytes
import java.net.InetAddress;  // represents an Internet Protocol (IP) address
import java.io.BufferedReader;//Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines
import java.io.DataInputStream;// lets an application read primitive Java data types from an underlying input stream in a machine-independent way
import java.io.DataOutputStream;// lets an application write primitive Java data types to an output stream in a portable way

// defining Server class and initializing variables

public class Server
 {

//main
	public static void main(String args[]) throws Exception{

		int port_numb = 0;//initializing to zero(port number for the communication)

		if(args.length<1)
		{
			port_numb = 8080;  //if length of command in cmd is less than 1;set the port number to be default 8080
		}
		else
		{
			port_numb = Integer.parseInt(args[0]);//otherwise port number is set to number passed in the cmd
		}

		ServerSocket Socket_server = new ServerSocket(port_numb);  //connecting socket to the network and server is ready to receive client's  http request messages
		System.out.println("Server is waiting for client's http requests....");
		System.out.println("----------------------------------------------------------------------------------");

//client's http request messages handling
		while(true)//indefinite loop
		{
			Socket server = Socket_server.accept(); //connection requests for TCP connection by client handling
			HttpRequest request = new HttpRequest(server);//object to handle client's http request
			Thread thread = new Thread(request); //thread to handle client's http requests
			thread.start(); // thread initiates
			}
	}

	public static class HttpRequest implements Runnable{  // implementing multithreading for server

		final static String CRLF = "\r\n";   //CRLF = carriage return line feed
		Socket socket;
    int port_numb = 8080;

		public HttpRequest(Socket socket) throws Exception{  ////initializing constructor for client's http request
			this.socket = socket;
		}

		private static String format_type(String file_name)   //stating the format of the file to be served to the client
		{
				if(file_name.endsWith(".htm") || file_name.endsWith(".html") || file_name.endsWith(".txt"))  //if text or html file format
				return "text/html";
				else if(file_name.endsWith(".jpeg") || file_name.endsWith(".jpg"))  //if picture formats//if jpeg or jpg
				return "image/jpeg";
				else if(file_name.endsWith(".gif") )
				return "image/gif";    //if gif format
				return "some uncommon file format";  //other file format
		}



		public void run()   //For interface:runnable//run() method
		{
			try
			 {
				request_process();
			 }
			catch (Exception error)
			{
				System.out.println(error);
			}
		}

		private static void bytes_send(FileInputStream f_i_s, OutputStream os) throws Exception{
				   byte[] buffer = new byte[1024];  // creating 1024 bytes sized buffer
				   int bytes = 0;

	   	 			while((bytes = f_i_s.read(buffer))!=-1) // client file request sent to socket's output stream
					 	{
				      os.write(buffer, 0, bytes);
				   	}
		}

		private void request_process() throws Exception
		{

			InputStream is =  new DataInputStream(socket.getInputStream());   //I/O stream reference for Socket
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());

			BufferedReader b_r = new BufferedReader(new InputStreamReader(is));  //input stream

			String line_header = "";  //initializing null values for line_header and requested data lines
			String line_request = "";

			int line_counter=0;  //extracting header and request lines
			while ((line_header = b_r.readLine()).length() != 0)
			{
			System.out.println(line_header);
			line_counter++;
			if(line_counter==1)
				line_request=line_header;
			}
			StringTokenizer Tokens = new StringTokenizer(line_request);  //file_name extractiion performed
			Tokens.nextToken();
			String file_name = Tokens.nextToken();
			Path current_relative_path = Paths.get("");  //file to be found to be in the same directory as the server file
			String s = current_relative_path.toAbsolutePath().toString(); //setting the absolute path for the requested file and extracting the same file
			file_name = s + file_name;
			System.out.println(file_name); //print the whole address of the file to be transmitted over the network
			FileInputStream f_i_s = null; //client's requested file to be opened
			boolean file_exists = true;
			try
			{
				f_i_s = new FileInputStream(file_name);
			}
			catch (FileNotFoundException error)
			{
				System.out.println(error);
				file_exists = false;
			}

//Response message to be displayed
			String line_status = "";   //displaying the current status
			String type_line_format = "";  //displaying the format or type of line
			String body_entity = "";  //displaying the body entity
//connection paramters to be printed at the client side
			if (file_exists)//when true
			{
				line_status = "HTTP/1.1 200 OK" + CRLF;
				type_line_format = "format-type: "+ format_type(file_name) + CRLF;
			}
			else//  when false
			{
				line_status ="HTTP/1.1 404 NOT FOUND"+CRLF ;
				type_line_format = "format-type: NOT FOUND" + CRLF;
			}

			os.writeBytes(line_status);  //displaying line status
			os.writeBytes(type_line_format);  //displaying format type
			InetAddress socketInetAddress = socket.getInetAddress();
			String host_Name = socketInetAddress.getHostName();
			//connection paramters to be printed at the client side
			os.writeBytes("Connection: Closed" + CRLF + "Host Name:" + host_Name + CRLF); //connection status
			String IP_Type = "HTTP OVER TCP"; //protocol used
			os.writeBytes("Protocol: "+IP_Type +CRLF); //displayed
      os.writeBytes("peer name: Server" + ":" + port_numb + CRLF); //peer name for client
			String socket_Type = "Connection"; //name for socket type
            os.writeBytes("Socket Type: " +socket_Type +CRLF); //displayed
						String socket_Family = "AF_INET"; //name for socket family
            os.writeBytes("Socket Family: "+socket_Family +CRLF); //displayed
						os.writeBytes(CRLF);  //ending header line with a carriage return and line feed
		  if (file_exists) //body content displayed
			  {
				bytes_send(f_i_s, os);
				f_i_s.close();
				}
			else
			{
				os.writeBytes("<HTML><TITLE>404 NOT FOUND</TITLE><BODY>404 FILE NOT FOUND - UNABLE TO FIND FILE</BODY></HTML>");
			}
			//putting try/catch blocks around ports closing code
			try
			{
				os.close();
				b_r.close();
				socket.close();
			}
			catch(Exception e)
			{
				System.out.println("error closing port");
			}
			System.out.println("---------------------------------------------------------------------------------");
		}

	}
}
//server code ends
