import tage.ai.behaviortrees.BTCondition;
import java.io.IOException;

import tage.ai.behaviortrees.BTCompositeType;
import tage.ai.behaviortrees.BTSequence;
import tage.ai.behaviortrees.BehaviorTree;
import tage.networking.IGameConnection.ProtocolType;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;

public class randomLocatePositioning extends BTCondition{
    NPC npc;
    NPCcontroller npcc;
    GameAIServerUDP server;
    boolean askOnce = false;
    private ConcurrentHashMap<UUID, IClientInfo> clients;
    

    Random rand = new Random();

    public randomLocatePositioning(GameAIServerUDP s, NPCcontroller c, NPC n, boolean toNegate){
        super(toNegate);
        server = s;
        npcc = c;
        npc = n;
    }

    protected boolean check(){
        /*server.sendCheckForAvatarNear();
        System.out.println("Flag: " + npcc.getNearFlag());
        return npcc.getNearFlag();*/
        clients = server.sendClientList();
        int cliSize = server.sendClientList().size();
        System.out.println("Size: " + cliSize);
        ArrayList<UUID> uuidList = new ArrayList<UUID>();

        if(cliSize != 0){
            //int randChoice = rand.nextInt(cliSize);
            //UUID ph = uidList.get(randChoice);
            //System.out.println(ph.toString());
            //System.out.println(clients.getKey());
            for(UUID k: clients.keySet()){
                //System.out.println(k.toString());
                uuidList.add(k);
            }
            System.out.println(uuidList);
            int randChoice = rand.nextInt(cliSize);
            server.npcNeedsClientInfo(uuidList.get(randChoice));
        }
        
        return true;

    }
}