package UDP.Server;

class Node{
	int occupied;
	String name;
	int seatNo;
	
	public Node(int seatNo)
	{
		this.seatNo=seatNo;
		occupied=0;
	}
	
	void occupy(String name)
	{
		this.name=name;
		occupied=1;
	}
	
	void unOccupy()
	{
		occupied=0;
	}
	
	String getName()
	{
		return name;
	}
	
	int isOccupied()
	{
		return occupied;
	}
	
	int getSeatNo()
	{
		return seatNo;
	}
}