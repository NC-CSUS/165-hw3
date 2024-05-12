package tage;

import tage.GameObject;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

import a2.MyGame;

public class FwdAction extends AbstractInputAction{
    private MyGame game;
    private float timePassed;
    private GameObject av;
    private float amount;
    private Vector3f oldPos, newPos;
    private Vector4f fwdDirection;

    public FwdAction(MyGame g){
        game = g;
        timePassed = 0;
    }

    @Override
    /**Performs forward action */
    public void performAction(float time, Event e){ 
        av = game.getAvatar();
        float keyValue = e.getValue();
        float timeDifference = time - timePassed;
        if(keyValue > -0.2 && keyValue < 0.2){
            timePassed = time;
            return;
        }

        oldPos = av.getWorldLocation();
        fwdDirection = new Vector4f(0f, 0f, 1f, 1f);
        fwdDirection.mul(av.getWorldRotation());
        float amount = timeDifference*keyValue;
        if(amount < 0.2){
            amount *= -1;
        }
        fwdDirection.mul(amount*3);
        newPos = oldPos.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
        av.setLocalLocation(newPos);
        timePassed = time;
    }


    /* 
    /**Performs forward action with specified amount
    public void performAction(float time, Event e, float amount){
        av = game.getAvatar();
        oldPos = av.getWorldLocation();
        fwdDirection = new Vector4f(0f, 0f, 1f, 1f);
        fwdDirection.mul(av.getWorldRotation);
        fwdDirection.mul(amount);
        newPos = oldPos.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
        av.setLocalLocation(newPos);
    }*/
}