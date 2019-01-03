//Name:Sagar Sharma
// UTA ID:1001626958
//References
//1.https://docs.oracle.com/javase/7/docs/api/java/io/package-summary.html
//2.https://docs.oracle.com/javase/7/docs/api/java/net/package-summary.html


//import all the built-in classes to be used in the program from java
import java.net.* ; //Provides for system input and output through data streams, serialization and the file system.
import java.io.* ; //Provides the classes for implementing networking applications.

//defining client class and initializing the variables
public class Client
 {
	final static String CRLF = "\r\n"; //CRLF is carriage return line feed used for getting data from the format accepted by server

	public static void main(String[] argv) throws Exception{

//IP address,port number and file_name format in the command given to server;either via cmd or browser
	  String file_name; //= argv[2]; //third keyword in the command in the cmd line or browser represents the file to look for in the server
		String IP_address = argv[0]; //initialization of ip address and port number
		int port_numb; //= Integer.parseInt(argv[1]); // second keyword in the command in cmd line or browser represents the port to be used//changed to integer type here

    if(argv.length < 2)//when the port number and the filename are not passed
    {
        port_numb = 8080;   //set the port number to be 8080
        file_name = "index.html"; //set the default file to be index.html
    }
    else
    {
       port_numb = Integer.parseInt(argv[1]);//else take whatever is passed for port number
       file_name = argv[2]; //else take whatever is passed for file name
    }

		Socket Socket_client = new Socket(IP_address, port_numb); //creating socket for the client to communicate with the server
		PrintWriter os = new PrintWriter(Socket_client.getOutputStream());//for the output bytes flowing from the client
		String response_buffer = ""; // buffer created for receiving data from the server response
		BufferedReader in = new BufferedReader(new InputStreamReader(Socket_client.getInputStream()));//for the incoming bytes to the client


		long start_time = System.currentTimeMillis();  //start time to calculate RTT
		long end_time_connection = System.currentTimeMillis(); //calc the end time for request
//get the info from the client to the server (connection parameters for the server)
    os.print("REQUEST_FROM_CLIENT:" + "GET /" + file_name + " HTTP/1.1" + CRLF + "Accept: text/html/htm,jpeg/jpg,gif" + CRLF + "Host Name:" + IP_address + ":" + port_numb  + CRLF + "User-Agent:Command Prompt" + CRLF + "Accept-Encoding: UTF-8/Unicode"  + CRLF + "Accept-Language: US eng" + CRLF + "Connection: keep-alive" + CRLF + "END TIME:" + end_time_connection + "ms" + CRLF + "peer name:" + IP_address + port_numb + CRLF +CRLF);
    os.flush();  //Flush the buffer


		// RTT for the request/response cycle

            long end_time   = System.currentTimeMillis();  //ened time for RTT
						long total_time = end_time - start_time; //calc the actual RTT for the cycle


// printing the data from the response buffer
        while( (response_buffer = in.readLine()) != null)
				{
        	System.out.println(response_buffer);
				}
				System.out.println("\nThe RTT for the request/respnse cycle is: " + total_time + "ms");
 }
}
//client program ends
