package UDP.Client;

import java.io.*;
import java.net.*;

public class UDPClient {
	
	String serverName="localhost";
	final int port=2014;
	final int localPort=2000;
	final int length=1024*5; // 5KB plot max length
	final int TIMEOUT = 3000;// 3 seconds
	
	int sendCounter;	
	DatagramSocket datasocket;
	InetAddress ia;
	final int PACKETS_TO_SEND=5;
	private final String shellPrompt = "UDPMovieClient$ ";
	
	
	public UDPClient()
	{
		sendCounter=0;
		try{
			ia = InetAddress.getByName(serverName);
			datasocket=new DatagramSocket(localPort);
			datasocket.setSoTimeout(TIMEOUT);
		} catch(SocketException se) {
			se.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void startShell()
	{
		System.out.println("\n\n\n");	//clear screen
		System.out.print(
				"*********************************************************************************************\n" +
				"**************************** Welcome to the UDP Movie Client 1.4 ****************************\n" +
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
		
		BufferedReader bin=new BufferedReader(new InputStreamReader(System.in));
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
				
				DatagramPacket sendPacket=new DatagramPacket(
						sendString.getBytes(),
						sendString.getBytes().length,
						ia,
						port
				);
				System.out.println("Sending String: "+sendString);
				datasocket.send(sendPacket);
				System.out.println("Packet Sent: " + (new String(sendPacket.getData())));
				sendCounter++;
				
				if(sendString.equals("exit"))
					return;
				
				System.out.println("Receiving Message...");
				byte[] receiveBuffer = new byte[length];
				DatagramPacket receivePacket=new DatagramPacket(receiveBuffer, receiveBuffer.length);
				datasocket.receive(receivePacket);
				System.out.println("Message Received:\n" + (new String(receivePacket.getData())));
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
		datasocket.close();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting UDP Client...");
		UDPClient client=new UDPClient();
		System.out.println("UDP Client Started.");
		
		client.startShell();
		
		client.close();
		System.out.println("UDP Client Aborted Cleanly");
	}
}
