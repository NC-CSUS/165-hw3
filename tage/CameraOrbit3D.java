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
import net.java.games.input.Event;
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

/**Camera Orbit 3D is a orbit controller class that allows the synching of a camera set up in the engine */
public class CameraOrbit3D{
    private Engine engine;
    private Camera camera;
    private GameObject avatar;
    private float cameraAzimuth; //Rotation around Y
	private float cameraElevation; //Elevation above target
	private float cameraRadius; //Distance between camera and target
	private boolean movementLock=false;

	/**Constructor class for CameraOrbit3D */
    public CameraOrbit3D(Camera cam, GameObject av, String gpName, Engine e){
        engine = e;
        camera = cam;
        avatar = av;
        cameraAzimuth = 0.0f; //Start behind and above target
		cameraElevation = 25.0f; //Elevation in degrees
		cameraRadius = 5.0f;  //Distance between camera and avatar
        setUpInputs(gpName);
        updateCameraPosition();
    }

	/**Method to set up the inputs that will control the orbital camera */
    private void setUpInputs(String gp){
		OrbitAzimuthAction orbitAz = new OrbitAzimuthAction();
		OrbitRadiusAction orbitRa = new OrbitRadiusAction();
		OrbitElevationAction orbitEl = new OrbitElevationAction();
		OrbitElevationActionUp orbitElUp = new OrbitElevationActionUp();
		OrbitElevationActionDown orbitElDown = new OrbitElevationActionDown();
		InputManager im = engine.getInputManager();
		//im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y, orbitEl, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.RZ, orbitRa, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Z, orbitAz, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.RX, orbitElUp, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.RY, orbitElDown, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	
		//Adding forward motion to the player avatar here and chaning it so left stick "Y" is forward
		//Ps4 Controller
		//X & Y = Left Stick (As you expect) 
		//Z is Horizontal Right stick, RZ is Vertical Right Stick
		//RX is Left Trigger (L2 / LT), RY is Right Trigger (R2 / RT)
	}

	/**Updates the camera position */ 
    public void updateCameraPosition(){
		Vector3f avatarRot = avatar.getWorldForwardVector();
		double avatarAngle = Math.toDegrees((double)avatarRot.angleSigned(new Vector3f(0, 0, -1), new Vector3f(0, 1, 0)));
		float totalAz = cameraAzimuth - (float)avatarAngle;
		double theta = Math.toRadians(totalAz);
		double phi = Math.toRadians(cameraElevation);
		float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
		float y = cameraRadius * (float)(Math.sin(phi));
		float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));
		if(y < 0.8){
			movementLock = true;
			if(y>0.8){
				camera.setLocation(new Vector3f(x, y, z).add(avatar.getWorldLocation()));
			}else if(movementLock==true && y<0.8){
				camera.setLocation(new Vector3f(x, 0, z).add(avatar.getWorldLocation()));
			}
		}else{
				movementLock = false;
				camera.setLocation(new Vector3f(x, y, z).add(avatar.getWorldLocation()));
		}
		camera.lookAt(avatar);
	}

	/**Azimuthal rotation action for orbital camera */
    private class OrbitAzimuthAction extends AbstractInputAction{
        @Override
		public void performAction(float time, Event event){
			float rotAmount;
			if(event.getValue() < -0.2){
				rotAmount = -1f;
			}else{
				if(event.getValue() > 0.2){
					rotAmount = 1f;
				}else{
					rotAmount = 0.0f;
				}
			}
			cameraAzimuth += rotAmount;
			cameraAzimuth = cameraAzimuth % 360;
			updateCameraPosition();
		}
	}

	/**Radial movement for orbital camera */
	private class OrbitRadiusAction extends AbstractInputAction{
        
		public void performAction(float time, Event event){
			float rotAmount;
			if(event.getValue() < -0.2){
				rotAmount = -0.05f;
			}else{
				if(event.getValue() > 0.2){
					rotAmount = 0.05f;
				}else{
					rotAmount = 0.0f;
				}
			}
			cameraRadius += rotAmount;
			cameraRadius = cameraRadius % 360;
			updateCameraPosition();
		}
	}

	/**Elevation for orbital camera */
	private class OrbitElevationAction extends AbstractInputAction{
       
		public void performAction(float time, Event event){
			float rotAmount;
			if(event.getValue() < -0.2){
				rotAmount = 1f;
			}else{
				if(event.getValue() > 0.2){
					rotAmount = -1f;
				}else{
					rotAmount = 0.0f;
				}
			}
			cameraElevation += rotAmount;
			cameraElevation = cameraElevation % 360;
			updateCameraPosition();
		}
	}
	
	/**Elevation for orbital camera */
	private class OrbitElevationActionUp extends AbstractInputAction{
       
		public void performAction(float time, Event event){
			//System.out.println("El Up: " + event.getValue());
			System.out.println("camera Elevation u: "+cameraElevation);
			float rotAmount;
			if(event.getValue() < -0.8f){
				rotAmount = 0f;
			}else{
				if(event.getValue() > -0.8f){
					rotAmount = 1f;
				}else{
					rotAmount = 0.0f;
				}
			}
			if(cameraElevation >= -10f && cameraElevation <= 88f){
				cameraElevation += rotAmount;
				cameraElevation = cameraElevation % 360;
				updateCameraPosition();
			}
			
			
		}
	}
	
	/**Elevation for orbital camera */
	private class OrbitElevationActionDown extends AbstractInputAction{
		
       
		public void performAction(float time, Event event){
			//System.out.println("El Down: " + event.getValue());
			System.out.println("camera Elevation d: "+cameraElevation);
			float rotAmount;
			if(event.getValue() < -0.8f){
				rotAmount = 0f;
			}else{
				if(event.getValue() > -0.8f){
					rotAmount = -1f;
				}else{
					rotAmount = 0.0f;
				}
			}
			if(cameraElevation >= 1 && cameraElevation <= 90f){
				cameraElevation += rotAmount;
				cameraElevation = cameraElevation % 360;
				updateCameraPosition();
			}
		}
	}


}
