MovieTix
========
A basic movie ticket reservation system containing a client and server with versions for both TCP and UDP.

This program contains a client and a server for a movie ticket reservation system. It is very basic and command-line based. The client can be augmented to provide a GUI allowing a more fancy system. This is just intended to be the fundamental stripped-down basics behind the concurrent and distributed nuances of such a system.

Instructions
============
To use this, download either the TCP folder or the UDP folder, depending on the protocol you want to use.
Start the server using "java TCP/Server/TCPServer" (or UDP/Server/UDPServer for the UDP version)
Next, start the client using "java TCP/Client/TCPClient"

Now, you can reserve movie tickets for yourself and friends based on your name. Each name can only reserve one set of tickets, but you can give up your tickets for a new set. There is logic that takes care of who buys tickets first for a system that could be under high-contention, ensuring that the system works properly when scaled to more than just 20 tickets and one that is probably running as a more available web service.

You can connect with multiple clients if needed (currently from the same machine). If you want to use this in a truly distributed manner you would need to slightly modify the client and the server to change the serverName and/or IP address (This program was made long before my intention to make this public, so I apologize in advance for the lack of maintenance of this code).

Technical Nuances
=================
The timings (in milliseconds) are mentioned after each command is issued on both the client side as well as the server side. It is more accurate to read it from the client side, because it includes the time taken to send the messages (elapsed time). Conversely, the timing in the server is only relating to it actually performing the particular action on the server.
Note: The timing is variable based on random factors (like say, locking), but a few commands will give a more accurate representation of the time taken.


The hostname of the server is currently set to "localhost", although if it is to be modified for testing purposes, then you can alter the 'serverName' variable in the client program.


I have added some extra features to the package than what was expected from the classroom assignment:
1) A shell interface with a prompt on the client side
2) an additional command to get the number of seats remaining (as described in the client shell)
3) The TCP Server uniquely identifies each client, and displays when a client connects and disconnects!


Here are some assumptions that I have made:
1) The reserve command does not check the validity of the number entered, i.e. if you enter an alphabet instead of a number it will throw a machine-generated error. Although, it does perform the tasks perfectly as asked in the problem, and is coherent with the goals of the assignment
2) The program assumes that the user will issue correct commands, and it has minimal error detection, since it is trivial and tedious to ensure perfect input from the user. (It does throw custom errors for faulty commands - such as 'RESERVE' or 'rEsERvE' etc).


-Nikhil Saraf
