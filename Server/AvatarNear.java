import tage.ai.behaviortrees.BTCondition;
import java.io.IOException;

import tage.ai.behaviortrees.BTCompositeType;
import tage.ai.behaviortrees.BTSequence;
import tage.ai.behaviortrees.BehaviorTree;
import tage.networking.IGameConnection.ProtocolType;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;

public class AvatarNear extends BTCondition{
    NPC npc;
    NPCcontroller npcc;
    GameAIServerUDP server;

    public AvatarNear(GameAIServerUDP s, NPCcontroller c, NPC n, boolean toNegate){
        super(toNegate);
        server = s;
        npcc = c;
        npc = n;
    }

    protected boolean check(){
        server.sendCheckForAvatarNear();
        System.out.println("Flag: " + npcc.getNearFlag());
        return npcc.getNearFlag();
    }
}