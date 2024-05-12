package tage;

//Imports
import tage.*;
import tage.shapes.*;

import tage.input.*;
import tage.input.action.*;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import tage.GameObject;
import tage.input.action.AbstractInputAction;

import org.joml.*;
import java.lang.Math;
import java.util.Random;
//import java.awt.*;
import java.awt.event.*;
import java.io.*;
//import java.util.*;
import javax.swing.*;
import org.joml.*;
//import org.w3c.dom.events.Event;

/**TAGE class that allows ease of control of singular or multiple cameras */
public class CameraController{
    private Engine engine;
    private Camera camera;
    private GameObject avatar;
    private float x, y, z;
    Vector3f loc, newLocation, fwd, up, right;

    /**Constructor for camera controller class */
    public CameraController(Camera cam, Engine e){
        engine = e;
        camera = cam;
        setUpInputs();
        setFirstLocation();
        //updateCameraPosition();
    }

    /**Sets initial location */
    private void setFirstLocation(){
        camera.setLocation(new Vector3f(0, 8, 0));
        camera.setU(new Vector3f(-1, 0, 0));
        camera.setV(new Vector3f(0, 0, 1));
        camera.setN(new Vector3f(0, -1, 0));
    }

    /**Sets up inputs to be aligned. Different cameras would need to be specified here */
    private void setUpInputs(){
        ZoomInAction zoomAc = new ZoomInAction();
        PanAction panAc = new PanAction();
        InputManager im = engine.getInputManager();
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.I, zoomAc, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.O, zoomAc, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.U, panAc, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.H, panAc, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.J, panAc, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.K, panAc, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}

    /**Zoom in action that does not allow you to go through floors */
	private class ZoomInAction extends AbstractInputAction{
        @Override
        public void performAction(float time, Event event){
            String cmd = event.getComponent().toString();
            loc = camera.getLocation();
            
            switch(cmd){
                case "I":
                    up = (new Vector3f(0, -1, 0));
                    break;
                case "O":
                    up = (new Vector3f(0, 1, 0));
                    break;
            }
            newLocation = loc.add(up.mul(0.1f));
            if(newLocation.y() < 1.5){
                if(newLocation.y() >= 1.5){
                    camera.setLocation(loc.add(up.mul(0.1f)));
                }
            }else{
                camera.setLocation(loc.add(up.mul(0.1f)));
            }

            
    }
}

    /**Allows you to with the sky camera */
    private class PanAction extends AbstractInputAction{
        @Override
        public void performAction(float time, Event event){
            String cmd = event.getComponent().toString();
            loc = camera.getLocation();

            switch(cmd){
                case "U":
                    fwd = (new Vector3f(0, 0, 1));
                    break;
                
                case "H":
                    fwd = (new Vector3f(1, 0, 0));
                    break;
                
                case "J":
                    fwd = (new Vector3f(0, 0, -1));
                    break;
                
                case "K":
                    fwd = (new Vector3f(-1, 0, 0));
                    break;
            }
            camera.setLocation(loc.add(fwd.mul(0.1f)));
        }
    }


		
    
    /*public void updateCameraPosition(){
        /*Camera cam = (engine.getRenderSystem().getViewport("MAIN").getCamera()); //Sets up camera object
			loc = dol.getWorldLocation(); //Sets location to dolphin location
			fwd = dol.getWorldForwardVector(); //Sets forward vector to world's  forward vector
			up = dol.getWorldUpVector(); //Sets up to world up vector
			right = dol.getWorldRightVector(); //Sets right to world right vector
			cam.setU(right); //Sets U right right vector
			cam.setV(up); //Sets V to up vector
			cam.setN(fwd); //Sets N to forward vector
			cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f))); //Sets location to on top of the dolphin*/ 
		/*Vector3f avatarRot = avatar.getWorldForwardVector();
		double avatarAngle = Math.toDegrees((double)avatarRot.angleSigned(new Vector3f(0, 0, -1), new Vector3f(0, 1, 0)));
		float totalAz = cameraAzimuth - (float)avatarAngle;
		double theta = Math.toRadians(totalAz);
		double phi = Math.toRadians(cameraElevation);
		float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
		float y = cameraRadius * (float)(Math.sin(phi));
		float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));
		camera.setLocation(new Vector3f(x, y, z).add(avatar.getWorldLocation()));
		camera.lookAt(avatar);
	}*/

}