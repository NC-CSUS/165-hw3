import java.io.IOException;

import tage.ai.behaviortrees.BTCompositeType;
import tage.ai.behaviortrees.BehaviorTree;
import tage.networking.IGameConnection.ProtocolType;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import org.joml.*;

import tage.*;
import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;

public class NPC{
    double locationX, locationY, locationZ, waypointX, waypointY, waypointZ;
    double xdir = 0.1, ydir = 0.1, zdir = 0.1;
    double size = 1.0;

    public NPC(){
        locationX = 0.0;
        locationY = 0.0;
        locationZ = 0.0;
    }

    public void randomizeLocation(int seedX, int seedZ){
        locationX = 0;//((double)seedX)/4.0 - 5.0;
        locationY = 3;
        locationZ = 0;
    }

    public double getX(){return locationX;}
    public double getY(){return locationY;}
    public double getZ(){return locationZ;}
    public void setX(double n){locationX = n;}
    public void setY(double n){locationY = n;}
    public void setZ(double n){locationZ = n;}
    
    public void setWaypoint(Vector3f position){
        waypointX = position.x();
        waypointY = position.y();
        waypointZ = position.z();
    }

    public String checkWP(){
        return "waypointX " + " " + waypointY + " " + waypointZ ;
    }

    public void getBig(){size = 2.0;}
    public void getSmall(){size = 1.0;}
    public boolean getBigFlag(){
        if(size == 2.0){
            return true;
        }else{
            return false;
        }
    }
    public boolean getSmallFlag(){
        if(size == 1.0){
            return true;
        }else{
            return false;
        }
    }
    public double getSize(){return size;}

    public void updateLocation(){
        /*if(locationX > 10){
            dir = -0.1;
        }
        if(locationX < -10){
            dir = 0.1;
        }
        locationX = locationX + dir;*/
        if(locationX < waypointX){
            xdir = 0.1;
        }else{
            xdir = -0.1;
        }
        if(locationY < waypointY){
            ydir = 0.1;
        }else{
            ydir = -0.1;
        }
        if(locationZ < waypointZ){
            zdir = 0.1;
        }else{
            zdir = -0.1;
        }
        Vector3f v1 = new Vector3f(
            (float)locationX, (float)locationY, (float)locationZ);
        Vector3f v2 = new Vector3f(
            (float)waypointX, (float)waypointY, (float)waypointZ);
        
        if(v1.distance(v2) < 6){

        }else{
            locationX += xdir;
            //locationY += ydir;
            locationZ += zdir;
        }
    }

    public String toString(){
        String ret = "";
        ret += "NPC currently at: " + getX() + " " + getY() + " " + getZ();
        return ret;
    }

}