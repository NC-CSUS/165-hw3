package a2;

import java.util.UUID;

import tage.*;
import org.joml.*;
import tage.shapes.*;
import tage.physics.*;
import tage.physics.JBullet.*;

// A ghost MUST be connected as a child of the root,
// so that it will be rendered, and for future removal.
// The ObjShape and TextureImage associated with the ghost
// must have already been created during loadShapes() and
// loadTextures(), before the game loop is started.

public class GhostAvatar extends GameObject
{
	UUID uuid;
	Light light;
	PhysicsObject physObj;

	public GhostAvatar(UUID id, AnimatedShape s, TextureImage t, Vector3f p) 
	{	super(GameObject.root(), s, s, t);
		uuid = id;
		setPosition(p);
		light = null;
	}
	
	public GhostAvatar(UUID id, AnimatedShape s, TextureImage t, Vector3f p, Light light) 
	{	super(GameObject.root(), s, s, t);
		uuid = id;
		setPosition(p);
		this.light = light;
	}
	
	public UUID getID() { return uuid; }
	public void setPosition(Vector3f m) { setLocalLocation(m); }
	public Vector3f getPosition() { return getWorldLocation(); }
	public Light getLight(){ return light; }
	public void setLight(Light l){ light = l;}
}
