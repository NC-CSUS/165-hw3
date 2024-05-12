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

public class GetBig extends BTCondition{
    NPC npc;


    public GetBig(NPC n){
        super(false);
        npc = n;
    }

    protected boolean check(){
        npc.getBig();
        return npc.getBigFlag();
    }

}
