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

public class NPCcontroller{
    private NPC npc;
    Random rn = new Random();
    BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
    boolean nearFlag = false, bigFlag = false;
    long thinkStartTime, tickStartTime, cT;
    long lastThinkUpdateTime, lastTickUpdateTime;
    GameAIServerUDP server;
    double criteria = 2.0;

    public void updateNPCs(){
        npc.updateLocation();
    }

    public void start(GameAIServerUDP s){
        thinkStartTime = System.nanoTime();
        tickStartTime = System.nanoTime();
        lastThinkUpdateTime = thinkStartTime;
        lastTickUpdateTime = tickStartTime;
        server = s;
        setupNPCs();
        setupBehaviorTree();
        npcLoop();
    }

    public NPC getNPC(){
        return npc;
    }

    public void setupNPCs(){
        npc = new NPC();
        npc.randomizeLocation(rn.nextInt(40), rn.nextInt(40));
    }

    public long getCT(){return cT;}
    public double getCriteria(){return criteria;}
    public void setCriteria(double d){criteria = d;}
    //public boolean getBigFlag(){return bigFlag;}
    //public void setBigFlag(boolean b){bigFlag = b;}
    public boolean getNearFlag(){return nearFlag;}
    public void setNearFlag(boolean b){nearFlag = b;}

    public void setupBehaviorTree(){
        System.out.println("Setting up behavior tree");
        /*bt.insertAtRoot(new BTSequence(10));
        bt.insertAtRoot(new BTSequence(20));
        bt.insert(10, new FiveSecPassed(this, npc, false));
        bt.insert(10, new GetSmall(npc));
        bt.insert(20, new AvatarNear(server, this, npc, false));
        bt.insert(20, new GetBig(npc));*/
        bt.insertAtRoot(new BTSequence(10));
        bt.insertAtRoot(new BTSequence((20)));
        bt.insert(10, new FiveSecPassed(this, npc, false));
        bt.insert(10, new randomLocatePositioning(server, null, npc, false));
        bt.insert(20, new FiveSecPassed(this, npc, false));
    }

    public void npcLoop(){
        while(true){
            long currentTime = System.nanoTime();
            cT = currentTime;
            float elapsedThinkMilliSecs = (currentTime - lastThinkUpdateTime)/(1000000.0f);
            float elapsedTickMilliSecs = (currentTime - lastTickUpdateTime)/(1000000.0f);

            //Tick
            if(elapsedTickMilliSecs >= 25.0f){
                lastTickUpdateTime = currentTime;
                npc.updateLocation();
                server.sendNPCinfo();
            }

            //Think 
            if(elapsedThinkMilliSecs >= 250.0f){
                lastThinkUpdateTime = currentTime;
                bt.update(elapsedThinkMilliSecs);
            }
            Thread.yield();
        }
    }

}