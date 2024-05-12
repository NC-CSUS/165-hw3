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

public class FiveSecPassed extends BTCondition{
    NPC npc;
    NPCcontroller npcc;
    GameAIServerUDP server;
    boolean counting = false;
    long holdTime = 0;

    public FiveSecPassed(NPCcontroller c, NPC n, boolean toNegate){
        super(toNegate);
        npcc = c;
        npc = n;
    }

    public long getHold(){return holdTime;}
    public void setHold(long l){holdTime = l;}

    protected boolean check(){
        /*System.out.println("Starting time test at " + npcc.getCT());
        try{
            wait(5000);
        }catch(InterruptedException e){
            System.out.println("Ended at " + npcc.getCT());
        }*/
        /*long holdTime = npcc.getCT();
        long changeTime = holdTime;
        System.out.println("Start: " + npcc.getCT());
        while((changeTime - holdTime) < 5.0){
            changeTime = npcc.getCT();
            System.out.println(npcc.getCT());
        }
        System.out.println("End: " + npcc.getCT());
        
        return true;*/
        if(!counting){
            counting = true;
            setHold(System.currentTimeMillis());
        }
        long test = System.currentTimeMillis();
        //Number of seconds
        int numSec = 3;
        if(test >= (getHold() + numSec * 1000)){
            System.out.println("From 3s Passed" + System.currentTimeMillis());
            counting = false;
            return true;
        }
        return false;
    }
}

/*   
        } */