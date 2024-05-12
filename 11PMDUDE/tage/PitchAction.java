package tage;

import tage.GameObject;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

import spiderisland.MyGame;

public class PitchAction extends AbstractInputAction{
    private MyGame game;
    private float timePassed;
    private GameObject av;
    private float amount;
    private Vector3f right;
    Matrix4f yRot, currRoto, newRoto;

    public PitchAction(MyGame g){
        game = g;
        timePassed = 0;
    }

    @Override
    /**Perform Pitch action */
    public void performAction(float time, Event e){
        float keyValue = e.getValue();
        float timeDifference = time - timePassed;
        if(keyValue > -0.2 && keyValue < 0.2){
            timePassed = time;
            return;
        }

        av = game.getAvatar();
        float amount = timeDifference * keyValue * 20;
        av.pitch(amount);
        /*if(amount < -0.2){
            amount *= -1;
        }
        currRoto = new Matrix4f(av.getWorldRotation());
        right = av.getLocalRightVector();
        yRot = (new Matrix4f()).rotation((float)(org.joml.Math.toRadians(amount*2)), right);
        newRoto = yRot.mul(currRoto);
        av.setLocalRotation(newRoto);*/
        timePassed = time;
    }
}