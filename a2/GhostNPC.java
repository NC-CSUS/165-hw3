package a2;


import java.util.UUID;

import tage.*;


import org.joml.*;

public class GhostNPC extends GameObject{
    private int id;
    
    public GhostNPC(int id, ObjShape s, TextureImage t, Vector3f p){
        super(GameObject.root(), s, t);
        this.id = id;
        setPosition(p);
    }

    public void setSize(boolean big){
        Matrix4f scale; 
        if (!big){
            scale = (new Matrix4f()).scaling(0.5f);
            this.setLocalScale(scale);
        }else{
            scale = (new Matrix4f()).scaling(2.0f);
            this.setLocalScale(scale);
        }
    }

    public int getID() { return id; }
	public void setPosition(Vector3f m) { setLocalLocation(m); }
	public Vector3f getPosition() { return getWorldLocation(); }
}
