package tage;

import tage.GameObject;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

import a2.MyGame;

public class YawAction extends AbstractInputAction{
    private MyGame game;
    private float timePassed;
    private GameObject av;
    private float amount;
    //private Vector3f oldRo, newRo, oldUp;
    private Vector4f fwdDirection, oldUp;
    private Matrix4f rotAroundAvatarUp, oldRo, newRo;

    public YawAction(MyGame g){
        game = g;
        timePassed = 0;
    }

    
    @Override
    /**Perform turn (YAW) action */
    public void performAction(float time, Event e){
        float keyValue = e.getValue();
        float timeDifference = time-timePassed;
        if(keyValue > -0.2 && keyValue < 0.2){
            timePassed = time;
            return; //Deadzone
        }

        float amount = timeDifference*keyValue*-50;
        av = game.getAvatar();
        av.yaw(amount);
        /*oldRo = new Matrix4f(av.getLocalRotation());
        rotAroundAvatarUp = (new Matrix4f()).rotation((float)(org.joml.Math.toRadians(amount*50)), 0, 1, 0);
        newRo = oldRo.mul(rotAroundAvatarUp);
        av.setLocalRotation(newRo);*/
        timePassed = time;
    }

    /* 
    /**Perform turn action with specified amount 
    public void performAction(float time, Event e, float amount){
        float keyValue = e.getValue();
        if(keyValue > -0.2 && keyValue < 0.2){
            return; //Deadzone
        }
        av = game.getAvatar();
        oldRo = new Matrix4f(av.getWorldRotation());
        oldUp = new Vector4f(0f, 1f, 0f, 1f).mul(oldRo);
        rotAroundAvatarUp = new Matrix4f().rotation(amount, new Vector3f(oldUp.x(), oldUp.y(), oldUp.z()));
        newRo = oldRo;
        newRo.mul(rotAroundAvatarUp);
        av.setLocalRotation(newRo);

    }*/

}