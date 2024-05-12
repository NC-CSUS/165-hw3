import java.io.IOException;
import tage.networking.IGameConnection.ProtocolType;

public class NetworkingServer 
{
	private GameServerUDP thisUDPServer;
	private GameServerTCP thisTCPServer;
	private GameAIServerUDP UPDServerAI;
	private NPCcontroller npcCtrl;

	public NetworkingServer(int serverPort/* , String protocol*/) 
	{	/*try 					//Old networking code
		{	if(protocol.toUpperCase().compareTo("TCP") == 0)
			{	thisTCPServer = new GameServerTCP(serverPort);
			}
			else
			{	thisUDPServer = new GameServerUDP(serverPort);
			}
		} 
		catch (IOException e) 
		{	e.printStackTrace();
		}*/
		npcCtrl = new NPCcontroller();
		
		try{
			System.out.println("Starting server....");
			UPDServerAI = new GameAIServerUDP(serverPort, npcCtrl);
		}catch(IOException e){
			System.out.println("server did not start");
			e.printStackTrace();
		}
		npcCtrl.start(UPDServerAI);
	}

	public static void main(String[] args) 
	{	if(args.length == 1)
		{	NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]));
		}
	}

}
