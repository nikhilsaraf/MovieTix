package TCP.Server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TCPServer
{
	ServerSocket serverSocket;
	int port=2014;
	//int length=1024;
	
	private int clientNumber;
	private static final int c=20;	// number of initial seats in the system
	private static int seatsLeft;
	private static ArrayList<Node> seatsReserved;
	private static ArrayList<Node> seatsRemaining;
	private static String MovieInfo =  "Movie: The Dark Knight\n" + 
	"Actors: Christian Bale, Michael Caine, Heath Ledger, Gary Oldman, Aaron Eckhart, Maggie Gyllenhaal, and Morgan Freeman\n" +
	"Rating: 8.9 / 10\n" +
	"Plot: Batman, Gordon and Harvey Dent are forced to deal with the chaos unleashed by an anarchist mastermind known only as the Joker, as he drives each of them to their limits. (imdb.com)\n";								
	
	public TCPServer()
	{
		clientNumber=0;
		seatsLeft=c;
		seatsRemaining = new ArrayList<Node>();
		seatsReserved = new ArrayList<Node>();
		try{
			serverSocket=new ServerSocket(port); 
		} catch (IOException se) {
			System.err.println(se);
		}
		
		for(int i=0;i<c;i++)
		{
			seatsRemaining.add(new Node(i+1));
		}
	}
	
	public static synchronized int[] reserveSeats(String name, int seatsToBeReserved)
	{
		for(Node res : seatsReserved)
		{
			if(res.getName().equals(name))
				return (new int[]{-2});
		}
		if(seatsToBeReserved<=seatsLeft)
		{
			int[] retVal = new int[seatsToBeReserved];
			for(int i=0;i<seatsToBeReserved;i++)
			{	
				Node addNode=seatsRemaining.get(0);
				seatsRemaining.remove(0);
				retVal[i]=addNode.getSeatNo();
				addNode.occupy(name);
				seatsReserved.add(addNode);
			}
			
			seatsLeft-=seatsToBeReserved;
			return retVal;
		}
		else
			return (new int[]{-1});
	}
	
	public static synchronized int[] searchSeats(String name)
	{
		int[] retVal = new int[c];
		int i;
		int counter=0;
		int valid=0;
		for(i=0;i<seatsReserved.size();i++)
		{
			if(seatsReserved.get(i).getName().equals(name))
			{
				retVal[counter] = seatsReserved.get(i).getSeatNo();
				valid=1;
				counter++;
			}
		}
		if (valid==1)
		{
			int[] returnThis = new int[counter];
			for(int j=0;j<counter;j++)
			{
				returnThis[j]=retVal[j];
			}
			return returnThis;
		}
		else
			return (new int[]{-1});		 
	}
	
	public static synchronized int[] deleteSeats(String name)
	{
		int[] retVal = new int[c];
		int i;
		int counter=0;
		int valid=0;
		
		for(i=0;i<seatsReserved.size();i++)
		{
			if(seatsReserved.get(i).getName().equals(name))
			{
				retVal[counter] = seatsReserved.get(i).getSeatNo();
				seatsRemaining.add(seatsReserved.remove(i));
				i--;
				counter++;
				valid=1;
			}
		}
		if (valid==1)
		{
			seatsLeft+=counter;
			int[] returnThis = new int[counter];
			for(int j=0;j<counter;j++)
			{
				returnThis[j]=retVal[j];
			}
			return returnThis;
		}
		else
			return (new int[]{-1});			
	}
	
	public static String getFilmInfo()		// not synchronized because only a read
	{
		return MovieInfo;
	}
	
	public static synchronized int getSeatsLeft()
	{
		return seatsLeft;
	}
	
	public void runServer()
	{
		while(true)
		{
			try{
				System.out.println("Waiting for a new client to connect...");
				Socket newClient = serverSocket.accept();
				clientNumber++;
				System.out.println("Client "+ clientNumber +" connected: " + newClient.getInetAddress().getHostName() + " at " + newClient.getInetAddress().getHostAddress() + "\n\n");
				(new Thread( new TCPClientHandlerThread(newClient, clientNumber) )).start();				
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
	public void close()
	{
		try
		{
			serverSocket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println("Starting TCP Sever...");
		TCPServer server=new TCPServer();
		System.out.println("TCP Server Started.\n");
		
		server.runServer();
		server.close();
		
		System.out.println("TCP Server Aborted Cleanly");
	}
}

