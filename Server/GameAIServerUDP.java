import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import org.joml.*;

import tage.*;
import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;
//import tage.rml.Vector3f;

public class GameAIServerUDP extends GameConnectionServer<UUID>{
    NPCcontroller npcCtrl;

    public GameAIServerUDP(int localPort, NPCcontroller npc) throws IOException {
        super(localPort, ProtocolType.UDP);
        npcCtrl = npc;
    }



     @Override
     public void processPacket(Object o, InetAddress senderIP, int port){

        String message = (String)o;
		String[] messageTokens = message.split(",");
		
		if(messageTokens.length > 0)
		{	// JOIN -- Case where client just joined the server
			// Received Message Format: (join,localId)
			if(messageTokens[0].compareTo("join") == 0)
			{	try 
				{	IClientInfo ci;					
					ci = getServerSocket().createClientInfo(senderIP, port);
					UUID clientID = UUID.fromString(messageTokens[1]);
					addClient(ci, clientID);
					System.out.println("Join request received from - " + clientID.toString());
					sendJoinedMessage(clientID, true);
				} 
				catch (IOException e) 
				{	e.printStackTrace();
			}	}
			
			// BYE -- Case where clients leaves the server
			// Received Message Format: (bye,localId)
			if(messageTokens[0].compareTo("bye") == 0)
			{	UUID clientID = UUID.fromString(messageTokens[1]);
				System.out.println("Exit request received from - " + clientID.toString());
				sendByeMessages(clientID);
				removeClient(clientID);
			}
			
			// CREATE -- Case where server receives a create message (to specify avatar location)
			// Received Message Format: (create,localId,x,y,z)
			if(messageTokens[0].compareTo("create") == 0)
			{	UUID clientID = UUID.fromString(messageTokens[1]);
				String[] pos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				sendCreateMessages(clientID, pos);
				sendWantsDetailsMessages(clientID);
			}
			
			// DETAILS-FOR --- Case where server receives a details for message
			// Received Message Format: (dsfr,remoteId,localId,x,y,z)
			if(messageTokens[0].compareTo("dsfr") == 0)
			{	UUID clientID = UUID.fromString(messageTokens[1]);
				UUID remoteID = UUID.fromString(messageTokens[2]);
				String[] pos = {messageTokens[3], messageTokens[4], messageTokens[5]};
				sendDetailsForMessage(clientID, remoteID, pos);
			}
			
			// MOVE --- Case where server receives a move message
			// Received Message Format: (move,localId,x,y,z)
			if(messageTokens[0].compareTo("move") == 0)
			{	UUID clientID = UUID.fromString(messageTokens[1]);
				String[] pos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				sendMoveMessages(clientID, pos);
            }
                
            //Server receives request for NPC - Format: (needNPC, id)
            if(messageTokens[0].compareTo("needNPC") == 0){
                System.out.println("server got a needNPC meessage");
                UUID clientID = UUID.fromString(messageTokens[1]);
                sendNPCstart(clientID);
            }

            //Server receives notice that an avatar is close - Format: (isnear, id)
            if(messageTokens[0].compareTo("isnear") == 0){
                UUID clientID = UUID.fromString(messageTokens[1]);
				boolean checkB = Boolean.parseBoolean(messageTokens[2]);
				handleNearTiming(clientID, checkB);
				
				//npcCtrl.getNPC().getBig();
            }

			//Server receives info on client, used for npc
			//Format scinpc, id, x, y, z 
			if(messageTokens[0].compareTo("scinpc") == 0){
				//System.out.println(message);
				//UUID clientId = UUID.fromString(messageTokens[1]);
				Vector3f clientPosition = new Vector3f(
					Float.parseFloat(messageTokens[1]), 
					Float.parseFloat(messageTokens[2]), 
					Float.parseFloat(messageTokens[3]));
					System.out.println(clientPosition);
					sendClientDetails(clientPosition);

			}
			
			//Server receives yaw from client
			//Format yaw, id, amount
			if(messageTokens[0].compareTo("yaw") == 0){
				UUID clientID = UUID.fromString(messageTokens[1]);
				float amount = Float.parseFloat(messageTokens[2]);
				sendYawMessage(clientID, amount);
			}
			
			//Global Toggle Physics
			if(messageTokens[0].compareTo("phys") == 0){
				UUID clientID = UUID.fromString(messageTokens[1]);
				sendPhysicsToggleMessage(clientID, messageTokens[2]);
			}
			
			//Play Animation
			if(messageTokens[0].compareTo("anim") == 0){
				UUID clientID = UUID.fromString(messageTokens[1]);
				sendAnimationMessage(clientID, messageTokens[2]);
			}
			
			//Light
			if(messageTokens[0].compareTo("light") == 0){
				UUID clientID = UUID.fromString(messageTokens[1]);
				sendLightMessage(clientID, messageTokens[2]);
			}
			
			//Choosing Model
			if(messageTokens[0].compareTo("model") == 0){
				UUID clientID = UUID.fromString(messageTokens[1]);
				sendModelMessage(clientID, messageTokens[2]);
			}
        }
    }

    public void handleNearTiming(UUID clientID, boolean b){
        npcCtrl.setNearFlag(b);
    }

    //NPC Messages
	
    //Additional protocols for npcs
    public void sendCheckForAvatarNear(){
        try{
            String message = new String("isnr");
            message += "," + (npcCtrl.getNPC()).getX();
            message += "," + (npcCtrl.getNPC()).getY();
            message += "," + (npcCtrl.getNPC()).getZ();
            message += "," + (npcCtrl.getCriteria());
            sendPacketToAll(message);
        }
        catch(IOException e){
            System.out.println("couldn't send message for near -s");
            e.printStackTrace();
        }
    }
    
    /*public void sendCheckSize(){
        try{
            String message = new String("chsize");
            message += "," + (npcCtrl.getNPC()).getSize();
            sendPacketToAll(message);
        }catch(IOException e){
            System.out.println("couldn't find message for size -s");
            e.printStackTrace();
        }
    }*/

	
    
     public void sendNPCinfo(){
        try{
            String message = new String("npcinfo");
            message += "," + (npcCtrl.getNPC()).getX();
            message += "," + (npcCtrl.getNPC()).getY();
            message += "," + (npcCtrl.getNPC()).getZ();
            message += "," + (npcCtrl.getNPC()).getSize();
            //message += "," + (npcCtrl.getCriteria());
            sendPacketToAll(message);
        }catch(IOException e){
            System.out.println("could not send npc info -s");
        }
    }
      
     public void sendNPCstart(UUID clientID){   //Handle creating the npc?
        String position[] = {"0", "0", "0"};
        sendCreateNPCmsg(clientID, position);
    }
     
    public void sendCreateNPCmsg(UUID clientID, String[] position){
		
        try{
            System.out.println("server telling clients about an NPC");
            String message = new String("createNPC," + clientID.toString());
            message += "," + position[0];
            message += "," + position[1];
            message += "," + position[2];
            forwardPacketToAll(message, clientID);
        }catch(IOException e){ e.printStackTrace(); }
    }

    // Informs the client who just requested to join the server if their if their 
	// request was able to be granted. 
	// Message Format: (join,success) or (join,failure)
	
	public void sendJoinedMessage(UUID clientID, boolean success)
	{	try 
		{	System.out.println("trying to confirm join");
			String message = new String("join,");
			if(success)
				message += "success";
			else
				message += "failure";
			sendPacket(message, clientID);
		} 
		catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs a client that the avatar with the identifier remoteId has left the server. 
	// This message is meant to be sent to all client currently connected to the server 
	// when a client leaves the server.
	// Message Format: (bye,remoteId)
	
	public void sendByeMessages(UUID clientID)
	{	try 
		{	String message = new String("bye," + clientID.toString());
			forwardPacketToAll(message, clientID);
		} 
		catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs a client that a new avatar has joined the server with the unique identifier 
	// remoteId. This message is intended to be send to all clients currently connected to 
	// the server when a new client has joined the server and sent a create message to the 
	// server. This message also triggers WANTS_DETAILS messages to be sent to all client 
	// connected to the server. 
	// Message Format: (create,remoteId,x,y,z) where x, y, and z represent the position

	public void sendCreateMessages(UUID clientID, String[] position)
	{	try 
		{	String message = new String("create," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];	
			forwardPacketToAll(message, clientID);
		} 
		catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs a client of the details for a remote client�s avatar. This message is in response 
	// to the server receiving a DETAILS_FOR message from a remote client. That remote client�s 
	// message�s localId becomes the remoteId for this message, and the remote client�s message�s 
	// remoteId is used to send this message to the proper client. 
	// Message Format: (dsfr,remoteId,x,y,z) where x, y, and z represent the position.

	public void sendDetailsForMessage(UUID clientID, UUID remoteId, String[] position)
	{	try 
		{	String message = new String("dsfr," + remoteId.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];	
			sendPacket(message, clientID);
		} 
		catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs a local client that a remote client wants the local client�s avatar�s information. 
	// This message is meant to be sent to all clients connected to the server when a new client 
	// joins the server. 
	// Message Format: (wsds,remoteId)
	
	public void sendWantsDetailsMessages(UUID clientID)
	{	try 
		{	String message = new String("wsds," + clientID.toString());	
			forwardPacketToAll(message, clientID);
		} 
		catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs a client that a remote client�s avatar has changed position. x, y, and z represent 
	// the new position of the remote avatar. This message is meant to be forwarded to all clients
	// connected to the server when it receives a MOVE message from the remote client.   
	// Message Format: (move,remoteId,x,y,z) where x, y, and z represent the position.

	public void sendMoveMessages(UUID clientID, String[] position)
	{	try 
		{	String message = new String("move," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			forwardPacketToAll(message, clientID);
		} 
		catch (IOException e) 
		{	e.printStackTrace();
	}	}

	//Informs client that an avatar has changed rotation
	//Format (yaw, id, amount)
	public void sendYawMessage(UUID clientID, float amount){
		try{
			String message = new String("yaw," + clientID.toString());
			message += "," + amount;
			forwardPacketToAll(message, clientID);
		}catch(IOException e){
			System.out.println("Error sending client yaw confirmation - s");
			e.printStackTrace();
		}
	}
	
	//Sends out global message to toggle physics
	public void sendPhysicsToggleMessage(UUID clientID, String toggle){
		try{
			String message = new String("phys," + clientID.toString());
			message += "," + toggle;
			forwardPacketToAll(message, clientID);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//Send out global message to update ghost UUID to play animation
	public void sendAnimationMessage(UUID clientID, String name){
		try{
			String message = new String("anim," + clientID.toString());
			message += "," + name;
			forwardPacketToAll(message, clientID);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//Send out global message to update ghost UUID to toggle flashlight
	public void sendLightMessage(UUID clientID, String name){
		try{
			String message = new String("light," + clientID.toString());
			message += "," + name;
			forwardPacketToAll(message, clientID);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//Send out global message to update ghost UUID to change ghost model
	public void sendModelMessage(UUID clientID, String name){
		try{
			System.out.println("Request to Change Model Recieved");
			String message = new String("model," + clientID.toString());
			message += "," + name;
			forwardPacketToAll(message, clientID);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void npcNeedsClientInfo(UUID clientID){
		try{
			String message = new String("nnci");
			System.out.println("Sending client info request");
			sendPacket(message, clientID);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void sendClientDetails(Vector3f position){
		npcCtrl.getNPC().setWaypoint(position);
		System.out.println(npcCtrl.getNPC().checkWP());
		System.out.println("Sending client pos: : " + position);
		System.out.println(npcCtrl.getNPC().getX() + npcCtrl.getNPC().getY() + npcCtrl.getNPC().getZ());
	}

    //Move npc message
    //Format (movenpc, npc, x, y, z)
    /*public void sendNPCMoveMessage(NPC n, String[] position){
        try{
            String message = new String("movenpc");
            message += "," + n.toString();
            message += "," + position[0];
            message += "," + position[1];
            message += "," + position[2];
            forwardPacketToAll(message, );
        }
    }*/

}


