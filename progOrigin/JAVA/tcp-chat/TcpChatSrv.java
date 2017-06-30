import java.io.*; 
import java.net.*; 
import java.util.concurrent.*;

class TcpChatSrv
{    
public static int MAXCLI=100;
public static Socket[] cliSock = new Socket[MAXCLI];
public static DataOutputStream[] sOut = new DataOutputStream[MAXCLI];
public static Boolean[] inUse= new Boolean[MAXCLI];
public static Semaphore changeLock = new Semaphore(1);

static ServerSocket sock;

public static void main(String args[]) throws Exception    
{       
int i;

try { sock = new ServerSocket(9999); }
catch(IOException ex) {
	System.out.println("Local port number not available.");
	System.exit(1); }

for(i=0; i<MAXCLI; i++) inUse[i]=false;
while(true)
	{
	changeLock.acquire();
	for(i=0; i<MAXCLI; i++) if(!inUse[i]) break; // find a free socket
	changeLock.release();
	cliSock[i]=sock.accept(); // wait for a new client connection request
	changeLock.acquire();
	inUse[i]=true;
	sOut[i]=new DataOutputStream(cliSock[i].getOutputStream());
	changeLock.release();
	new Thread(new TcpChatSrvClient(i)).start();
	}
} 
}



class TcpChatSrvClient implements Runnable 
{    
int myNum;
private DataInputStream sIn;

public TcpChatSrvClient(int cli_n) { myNum=cli_n;}

public void run()
{
int i, nChars;
byte[] data = new byte[300];

try {
sIn = new DataInputStream(TcpChatSrv.cliSock[myNum].getInputStream());
while(true)
   {
   nChars=sIn.read();
   if(nChars==0) break; // empty line
   sIn.read(data,0,nChars);
   TcpChatSrv.changeLock.acquire();
   for(i=0; i<TcpChatSrv.MAXCLI; i++) // retransmit the line
	if(TcpChatSrv.inUse[i])
		{
		TcpChatSrv.sOut[i].write(nChars);
		TcpChatSrv.sOut[i].write(data,0,nChars);
		}
   TcpChatSrv.changeLock.release();
   }
   // the client wants to exit
   TcpChatSrv.changeLock.acquire();
   TcpChatSrv.sOut[myNum].write(nChars);
   TcpChatSrv.inUse[myNum]=false;
   TcpChatSrv.cliSock[myNum].close();
   TcpChatSrv.changeLock.release();
} catch(IOException ex) { System.out.println("IOException"); }
  catch(InterruptedException ex) { System.out.println("Interrupted"); }
}
}
