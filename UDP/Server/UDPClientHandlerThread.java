package UDP.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class UDPClientHandlerThread implements Runnable
{
	private final String protocol = "UDP";
	String command;
	DatagramPacket returnPacket;
	DatagramSocket socket;
	
	public UDPClientHandlerThread(DatagramPacket clientPacket)
	{
		try{
			socket = new DatagramSocket();
		} catch(SocketException se) {
			se.printStackTrace();
		}
		returnPacket = clientPacket;
		returnPacket.setAddress(clientPacket.getAddress());
		returnPacket.setPort(clientPacket.getPort());
		command = new String(clientPacket.getData());
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
		StringTokenizer strtok=new StringTokenizer(command);
		String action=strtok.nextToken();
		byte[] rBuff=null;
		
		try {
			long startTime, endTime;
			if(action.equals("reserve"))
			{			
				startTime = getSystemTime();				
				System.out.println("In the " + action + " condition...");
				String name=strtok.nextToken();
				int count;
				count=Integer.parseInt(strtok.nextToken());
				int[] reservedSeats = UDPServer.reserveSeats(name, count);
				
				rBuff = Arrays.toString(reservedSeats).getBytes();
				endTime = getSystemTime();
				System.out.println("The time for the " + action + " function of the "+ protocol +" protocol is: " + (endTime-startTime) + " milliseconds.");
			}
			else if(action.equals("search"))
			{
				startTime = getSystemTime();				
				System.out.println("In the " + action + " condition...");
				String name=strtok.nextToken();
				int[] reservedSeats = UDPServer.searchSeats(name);
				
				rBuff = Arrays.toString(reservedSeats).getBytes();
				endTime = getSystemTime();
				System.out.println("The time for the " + action + " function of the "+ protocol +" protocol is: " + (endTime-startTime) + " milliseconds.");
			}
			else if(action.equals("delete"))
			{
				startTime = getSystemTime();				
				System.out.println("In the " + action + " condition...");
				String name=strtok.nextToken();
				int[] reservedSeats = UDPServer.deleteSeats(name);
				
				rBuff = Arrays.toString(reservedSeats).getBytes();
				endTime = getSystemTime();
				System.out.println("The time for the " + action + " function of the "+ protocol +" protocol is: " + (endTime-startTime) + " milliseconds.");
			}
			else if(action.equals("getinfo"))
			{
				startTime = getSystemTime();								
				System.out.println("In the " + action + " condition...");
				String movieInfo=UDPServer.getFilmInfo();
				rBuff = movieInfo.getBytes();
				endTime = getSystemTime();
				System.out.println("The time for the " + action + " function of the "+ protocol +" protocol is: " + (endTime-startTime) + " milliseconds.");
			}
			else if(action.equalsIgnoreCase("getSeatsLeft"))
			{
				System.out.println("In the " + action + " condition...");
				int seatsLeft=UDPServer.getSeatsLeft();
				rBuff = String.valueOf(seatsLeft).getBytes();
			}
			else
			{
				System.out.println("In the DEFAULT condition...");
				rBuff = (new String("Invalid Command: " + action)).getBytes();
			}
			returnPacket.setData(rBuff);
			returnPacket.setLength(rBuff.length);
			socket.send(returnPacket);
			socket.close();
		} catch (IOException e) {
			System.err.println(e);
		} 
	}
}