package TCP.Client;

import java.io.*;
import java.net.*;

public class TCPClient {
	
	String serverName="localhost";
	final int port=2014;
	final int localPort=2000;
	final int length=1024*50; // 5KB plot max length
	final int TIMEOUT = 3000;// 3 seconds
	
	int sendCounter;	
	Socket datasocket;
	InetAddress ia;
	private final String shellPrompt = "TCPMovieClient$ ";
	
	BufferedReader input;
	PrintWriter output;
	
	public TCPClient()
	{
		sendCounter=0;
		try{
			ia = InetAddress.getByName(serverName);
			datasocket=new Socket(ia, port);
			datasocket.setReceiveBufferSize(length);
			datasocket.setSoTimeout(TIMEOUT);
			input = new BufferedReader(new InputStreamReader(datasocket.getInputStream()));
			output = new PrintWriter( datasocket.getOutputStream() );
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} 
	}
	
	public void startShell()
	{
		System.out.println("\n\n\n");	//clear screen
		System.out.print(
				"*********************************************************************************************\n" +
				"**************************** Welcome to the TCP Movie Client 1.0 ****************************\n" +
				"*********************************************************************************************\n" +
				"*********************************************************************************************\n" +
				"***     Please enter one of the following commands:                                       ***\n" +
				"***          reserve <name> <count>                                                       ***\n" +
				"***          search <name>                                                                ***\n" +
				"***          delete <name>                                                                ***\n" +
				"***          getinfo                                                                      ***\n" +
				"***          getSeatsLeft                                                                 ***\n" +
				"*********************************************************************************************\n" +
				"*********************************************************************************************\n" +
				"*********************************************************************************************\n" +
				"*********************************************************************************************\n" +
				"\n\n"
				);
		
		BufferedReader bin=new BufferedReader(new InputStreamReader(System.in));	//for user keyboard input
		while (true)
		{		
			long startTime = System.currentTimeMillis();
			try{				
				System.out.print(shellPrompt);
				System.out.flush();
				String sendString=bin.readLine();
				if(sendString.trim().equals(""))
					continue;
				else
					sendString = sendString+" ";
				
				System.out.println("Sending String: "+sendString);
				System.out.flush();
				output.println(sendString);
				output.flush();
				System.out.println("Packet Sent! ");
				System.out.flush();
				sendCounter++;
				
				if(sendString.equals("exit"))
					return;
				
				System.out.println("Receiving Message...");
				System.out.flush();
				
				String receiveMessage = null; 
				if(sendString.trim().equalsIgnoreCase("getinfo"))
				{
					System.out.println("Message Received:");
					do {
						try{
							receiveMessage = input.readLine();
							System.out.println(receiveMessage);
						} catch (SocketTimeoutException ste) {
							receiveMessage=null;
							System.out.flush();
						}
					} while(receiveMessage != null);
				}
				else
				{
					receiveMessage = input.readLine();
					System.out.println("Message Received:\n" + receiveMessage);
				}	
				System.out.flush();
			} catch (IOException e) {
				System.err.println(e);
			}
			long endTime = System.currentTimeMillis();
			System.out.println(
					"time for the above command: " + (endTime - startTime) + " milliseconds.\n" +
					"-------------------------------------------------------------------------"
					);
		} //while
	}
	
	public void close()
	{
		try
		{
			datasocket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting TCP Client...");
		TCPClient client=new TCPClient();
		System.out.println("TCP Client Started.");
		
		client.startShell();
		
		client.close();
		System.out.println("TCP Client Aborted Cleanly");
	}
}
