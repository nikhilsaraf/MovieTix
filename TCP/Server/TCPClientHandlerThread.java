package TCP.Server;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TCPClientHandlerThread implements Runnable
{
	private final String protocol = "TCP";
	String command;
	BufferedReader input;
	PrintWriter output;
	Socket clientSocket;
	int clientID;
	
	public TCPClientHandlerThread(Socket clientSocket, int clientID)
	{		
		this.clientSocket = clientSocket;
		this.clientID = clientID;
		try
		{
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintWriter( clientSocket.getOutputStream() );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private long getSystemTime()
	{
		return System.currentTimeMillis();
	}
	
	public void run()
	{
		/*
		 * For now it assumes that only correct commands are issued
		 */		
		while(true)
		{
			try {
				System.out.println("Waiting to receive message from client" + clientID +": " + clientSocket.getInetAddress().getHostName() + " at " + clientSocket.getInetAddress().getHostAddress() + "...");
				
				// add timeout and check if client disconnected etc...
				command = input.readLine();
				if(command == null)
				{
					clientSocket.close();
					break;
				}
				StringTokenizer strtok = new StringTokenizer(command);
				String action=strtok.nextToken();
	
				long startTime, endTime;
				if(action.equals("reserve"))
				{			
					startTime = getSystemTime();				
					System.out.println("In the " + action + " condition...");
					String name=strtok.nextToken();
					int count;
					count=Integer.parseInt(strtok.nextToken());
					int[] reservedSeats = TCPServer.reserveSeats(name, count);
					
					output.println(Arrays.toString(reservedSeats));
					output.flush();
					endTime = getSystemTime();
					System.out.println("The time for the " + action + " function of the "+ protocol +" protocol is: " + (endTime-startTime) + " milliseconds.");
				}
				else if(action.equals("search"))
				{
					startTime = getSystemTime();				
					System.out.println("In the " + action + " condition...");
					String name=strtok.nextToken();
					int[] reservedSeats = TCPServer.searchSeats(name);
					
					output.println(Arrays.toString(reservedSeats));
					output.flush();
					endTime = getSystemTime();
					System.out.println("The time for the " + action + " function of the "+ protocol +" protocol is: " + (endTime-startTime) + " milliseconds.");
				}
				else if(action.equals("delete"))
				{
					startTime = getSystemTime();				
					System.out.println("In the " + action + " condition...");
					String name=strtok.nextToken();
					int[] reservedSeats = TCPServer.deleteSeats(name);
					
					output.println(Arrays.toString(reservedSeats));
					output.flush();
					endTime = getSystemTime();
					System.out.println("The time for the " + action + " function of the "+ protocol +" protocol is: " + (endTime-startTime) + " milliseconds.");
				}
				else if(action.equals("getinfo"))
				{
					startTime = getSystemTime();								
					System.out.println("In the " + action + " condition...");
					String movieInfo=TCPServer.getFilmInfo();
					output.println(movieInfo);
					output.flush();
					endTime = getSystemTime();
					System.out.println("The time for the " + action + " function of the "+ protocol +" protocol is: " + (endTime-startTime) + " milliseconds.");
				}
				else if(action.equalsIgnoreCase("getSeatsLeft"))
				{
					System.out.println("In the " + action + " condition...");
					int seatsLeft=TCPServer.getSeatsLeft();
					output.println(String.valueOf(seatsLeft));
					output.flush();
				}
				else
				{
					System.out.println("In the DEFAULT condition...");
					output.println("Invalid Command: " + action);
					output.flush();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} 
//			finally {
//				try {
//					clientSocket.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}	//while
		
		System.out.println("Client" + clientID + " Disconnected: " + clientSocket.getInetAddress().getHostName() + " at " + clientSocket.getInetAddress().getHostAddress());
	}
}