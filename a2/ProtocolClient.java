package a2;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;
import org.joml.*;

import tage.*;
import tage.networking.client.GameConnectionClient;

public class ProtocolClient extends GameConnectionClient
{
	private MyGame game;
	private GhostManager ghostManager;
	private UUID id;
	private GhostNPC ghostNPC;
	
	public ProtocolClient(InetAddress remoteAddr, int remotePort, ProtocolType protocolType, MyGame game) throws IOException 
	{	super(remoteAddr, remotePort, protocolType);
		this.game = game;
		this.id = UUID.randomUUID();
		ghostManager = game.getGhostManager();
	}
	
	public UUID getID() { return id; }
	
	@Override
	protected void processPacket(Object message)
	{	String strMessage = (String)message;
		//System.out.println("message received -->" + strMessage);
		String[] messageTokens = strMessage.split(",");
		
		// Game specific protocol to handle the message
		if(messageTokens.length > 0)
		{
			// Handle JOIN message
			// Format: (join,success) or (join,failure)
			if(messageTokens[0].compareTo("join") == 0)
			{	if(messageTokens[1].compareTo("success") == 0)
				{	System.out.println("join success confirmed");
					game.setIsConnected(true);
					sendCreateMessage(game.getPlayerPosition());
				}
				if(messageTokens[1].compareTo("failure") == 0)
				{	System.out.println("join failure confirmed");
					game.setIsConnected(false);
			}	}
			
			// Handle BYE message
			// Format: (bye,remoteId)
			if(messageTokens[0].compareTo("bye") == 0)
			{	// remove ghost avatar with id = remoteId
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				ghostManager.removeGhostAvatar(ghostID);
			}
			
			// Handle CREATE message
			// Format: (create,remoteId,x,y,z)
			// AND
			// Handle DETAILS_FOR message
			// Format: (dsfr,remoteId,x,y,z)
			if (messageTokens[0].compareTo("create") == 0 || (messageTokens[0].compareTo("dsfr") == 0))
			{	// create a new ghost avatar
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));

				try
				{	ghostManager.createGhostAvatar(ghostID, ghostPosition);
				}	catch (IOException e)
				{	System.out.println("error creating ghost avatar");
				}
			}
			
			// Handle WANTS_DETAILS message
			// Format: (wsds,remoteId)
			if (messageTokens[0].compareTo("wsds") == 0)
			{
				// Send the local client's avatar's information
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				sendDetailsForMessage(ghostID, game.getPlayerPosition());
			}
			
			// Handle MOVE message
			// Format: (move,remoteId,x,y,z)
			if (messageTokens[0].compareTo("move") == 0)
			{
				// move a ghost avatar
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
				
				ghostManager.updateGhostAvatar(ghostID, ghostPosition);
			}	

			//Client received yaw message
			//Format: yaw, id, amount
			if(messageTokens[0].compareTo("yaw") == 0){
				UUID ghostID = UUID.fromString(messageTokens[1]);
				float amount = Float.parseFloat(messageTokens[2]);
				ghostManager.yawGhostAvatar(ghostID, amount);
			}

			//Create NPC
			//Format (createNPC, clientID, x, y, z)
			if(messageTokens[0].compareTo("createNPC") == 0){
				//Parse out ID
				UUID cliID = UUID.fromString(messageTokens[1]);
				//Parse out position into vector
				Vector3f npcPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
				
					try
					{	
						createGhostNPC(npcPosition);
					}	catch (IOException e){	
						System.out.println("error creating ghost npc - protcli");
					}
			}

			//Need NPC also moves/updates things
			//Format (npcinfo, x, y, z, size)
			if(messageTokens[0].compareTo("npcinfo") == 0){
				Vector3f npcPosition = new Vector3f(
					Float.parseFloat(messageTokens[1]), 
					Float.parseFloat(messageTokens[2]), 
					Float.parseFloat(messageTokens[3]));
				double npcSize = Double.parseDouble(messageTokens[4]);
				try{
					updateGhostNPC(npcPosition, npcSize);
				}catch(IOException e){
					System.out.println("Could not successfully update npc ghost position - protCli");
					e.printStackTrace();
				}
				
			}

			//Is near check for npc
			//Format isnr, x, y, z, criteria
			if(messageTokens[0].compareTo("isnr")==0){
				Vector3f npcPosition = new Vector3f(
					Float.parseFloat(messageTokens[1]),
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]));
				double npcCriteria = Double.parseDouble(messageTokens[4]);
				Vector3f avPos = game.getAvatar().getWorldLocation();
				if(npcPosition.distance(avPos) < 3){
					System.out.println("AV is close  -protCli");
					sendIsNear(id, true);
				}else{
					sendIsNear(id, false);
				}

			}
			
			//Give server information of a client
			//Format nnci
			if(messageTokens[0].compareTo("nnci")==0){
				sendClientInfoNPC(game.getPlayerPosition());
			}
			
			//Recieving Physics Toggle Update
			//Format: phys, id, true/false
			if(messageTokens[0].compareTo("phys") == 0){
				//0 = phys, 1 = UUID, 2 = toggle
				if(messageTokens[2].equalsIgnoreCase("false")){
					game.setPhysics(false);
				} else if (messageTokens[2].equalsIgnoreCase("true")){
					game.setPhysics(true);
				} else {
					game.togglePhysics();
				}
			}
		
			//Play Ghost Animations
			//Format: anim, id, "name"
			if(messageTokens[0].compareTo("anim") == 0){
				UUID ghostID = UUID.fromString(messageTokens[1]);
				ghostManager.playGhostAnimation(ghostID, messageTokens[2]); //GhostID & name of animation
			}
			
			if(messageTokens[0].compareTo("light") == 0){
				UUID ghostID = UUID.fromString(messageTokens[1]);
				ghostManager.toggleFlashlight(ghostID, messageTokens[2]); 
			}
			
			if(messageTokens[0].compareTo("model") == 0){
				UUID ghostID = UUID.fromString(messageTokens[1]);
				ghostManager.changeModel(ghostID, messageTokens[2]); 
			}

		}	

}
	
	// The initial message from the game client requesting to join the 
	// server. localId is a unique identifier for the client. Recommend 
	// a random UUID.
	// Message Format: (join,localId)
	
	public void sendJoinMessage()
	{	
		System.out.println("Attempting Join");
		try 
		{	sendPacket(new String("join," + id.toString()));
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs the server that the client is leaving the server. 
	// Message Format: (bye,localId)

	public void sendByeMessage()
	{	try 
		{	sendPacket(new String("bye," + id.toString()));
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs the server of the client�s Avatar�s position. The server 
	// takes this message and forwards it to all other clients registered 
	// with the server.
	// Message Format: (create,localId,x,y,z) where x, y, and z represent the position

	public void sendCreateMessage(Vector3f position)
	{	try 
		{	String message = new String("create," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			
			sendPacket(message);
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs the server of the local avatar's position. The server then 
	// forwards this message to the client with the ID value matching remoteId. 
	// This message is generated in response to receiving a WANTS_DETAILS message 
	// from the server.
	// Message Format: (dsfr,remoteId,localId,x,y,z) where x, y, and z represent the position.

	public void sendDetailsForMessage(UUID remoteId, Vector3f position)
	{	try 
		{	String message = new String("dsfr," + remoteId.toString() + "," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			
			sendPacket(message);
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs the server that the local avatar has changed position.  
	// Message Format: (move,localId,x,y,z) where x, y, and z represent the position.

	public void sendMoveMessage(Vector3f position)
	{	try 
		{	String message = new String("move," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			
			sendPacket(message);
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}


	//Informs server of rotation message
	//Format rotate, id, amount
	public void sendYawMessage(float amount){
		try{
			String message = new String("yaw," + id.toString());
			message += "," + amount;
			sendPacket(message);
		}catch(IOException e){
			System.out.println("Could not send yaw message to server - c");
			e.printStackTrace();
		}
	}

	//Lets server know an npc is needed
	public void sendNeedNPC(){
		System.out.println("Sending needNPC");
		try{
			String message = new String("needNPC" + id.toString());
			sendPacket(message);
		}catch (IOException e){
			System.out.println("Error sending needNPC - protCli");
			e.printStackTrace();
		}
	}

	public void sendIsNear(UUID clientID, boolean b){
		try{
			String message = new String("isnear");
			message += "," + clientID.toString();
			message += "," + b;
			System.out.println(message);
			sendPacket(message);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void sendPhysicsToggleMessage(String toggle){
		System.out.println("Toggle Server-Client Physics to: " + toggle);
		try{
			String message = new String("phys," + id.toString());
			message += "," + toggle;
			System.out.println(message);
			
			sendPacket(message);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void sendAnimationMessage(String animationName){
		try{
			String message = new String("anim," + id.toString());
			message += "," + animationName;
			System.out.println(message);
			
			sendPacket(message);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void sendModelMessage(String model){
		try{
			String message = new String("model," + id.toString());
			message += "," + model;
			System.out.println(message);
			
			sendPacket(message);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void toggleFlashlight(String t){
		try{
			String message = new String("light," + id.toString());
			message += "," + t;
			System.out.println(message);
			
			sendPacket(message);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//Ghost NPC Section
	private void createGhostNPC(Vector3f position) throws IOException{
		if(ghostNPC == null){
			ghostNPC = new GhostNPC(0, game.getNPCShape(), game.getNPCTexture(), position);
		}
	}

	private void updateGhostNPC(Vector3f position, double gsize) throws IOException{
		boolean gs;
		if(ghostNPC == null){
			try{
				createGhostNPC(position);
			}catch(IOException e){
				System.out.println("error creating npc -protcli");
			}
		}
		ghostNPC.setPosition(position);
		game.setNPCLoc(position, ghostNPC);
		if(gsize == 1.0){
			gs = false;
		}else{
			gs = true;
		}
		ghostNPC.setSize(gs);
	}

	private void sendClientInfoNPC(Vector3f position){
		System.out.println("Position of client: " + position);
		try 
		{	
			String message = new String("scinpc");
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			System.out.println(message);
			
			sendPacket(message);
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	
	
}
