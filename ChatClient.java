import java.io.*;
import java.net.*;
//Chat client that creates a MulticastServer and allows a user to write text to all users connected to the server
class ChatClient
{
	//Declaring constants for server address and port number
	final static String INET_ADDRESS = "239.0.202.1";
	final static int PORT_NUMBER = 40202;
	
	//Main method to get everything running
	public static void main(String args[])
    	{
		if(args.length != 0) {
			/* always check the input to the program! */
			System.err.println("Doesn't take any parameters.");
			return;
		}
		
		System.out.println("Started up ChatClient.");
		
		//Create a new multicast socket
		try
		{
			//Creates the group that clients will connect to
			InetAddress group = InetAddress.getByName(INET_ADDRESS);
			//Creates the multicastsocket on the port number given
			MulticastSocket clientSocket = new MulticastSocket(PORT_NUMBER);
			//Joins the multicastsocket to the group
			clientSocket.joinGroup(group);
			//Creates a new multicastServer class which takes the group it will relay messages to and from
			ListenerThread server = new ListenerThread(clientSocket);
			new Thread(server).start(); //starts the thread and calls the run method in multicastServer class
			
			//Creates the bReader that reads from the console window
			BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
			
			while(true)
			{
				//System.out.println("UH OH ");
				String msg = bReader.readLine();
				DatagramPacket messagePkt = new DatagramPacket(msg.getBytes(),
				msg.getBytes().length, group, PORT_NUMBER);
				
				clientSocket.send(messagePkt);
			}		
		}
		catch(IOException e)
		{
			System.err.println("Chatclient main error, " + e.toString());
		}
	}
	
}

//MulticastServerThread that extends thread.
//Relays any message recived from a client to all clients connected to the server
class ListenerThread extends Thread
{
	private MulticastSocket serverSocket;
	
	public ListenerThread(MulticastSocket ss)
	{
		//Passing the server the socket that the clients will be on
		serverSocket = ss;
	}
	
	//This will be called when a new Thread is started
	public void run()
	{
		//System.out.println("ChatServer started.");
		try
		{
			while(true)
			{
				//Create a buffer of bytes to store incoming info
				byte[] buffer = new byte[256];
				//Create the datagrampkt which takes in the buffer and buffer length
				DatagramPacket messagePkt = new DatagramPacket(buffer, buffer.length);

				//The MulticastSocket will take the messagePkt and write any message recieved to the buffer
				serverSocket.receive(messagePkt);
				//Extract the string from the buffer
				String msg = new String(buffer, 0, buffer.length);
				//Get the ipAddress of the client that is sending us the message
				//getHostAddress gets the IP address without the slash at the front
				String ipAddress = messagePkt.getAddress().getHostAddress().toString();				

				System.out.println(ipAddress + ": " + msg);
			}
		}
		catch(IOException e)
		{
			System.err.println("MulticastSocket error, " + e.toString());
		}
	}
}
