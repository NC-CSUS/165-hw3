package tage;

import tage.*;
import tage.shapes.*;

public class ManualDiamond extends ManualObject{
    private float[] vertices = new float[]{
        -1f, 0f, 1f,    1f, 0f, 1f,     0f, 1f, 0f, //Front
        1f, 0f, 1f,     1f, 0f, -1f,    0f, 1f, 0f, //Right
        1f, 0f, -1f,    -1f, 0f, -1f,   0f, 1f, 0f, //Back
        -1f, 0f, -1f,   -1f, 0f, 1f,    0f, 1f, 0f, //Left
        -1f, 0f, 1f,    1f, 0f, 1f,     0f, -1f, 0f, //BotFront
        1f, 0f, 1f,     1f, 0f, -1f,    0f, -1f, 0f, //BotRight
        1f, 0f, -1f,     -1f, 0f, -1f,    0f, -1f, 0f, //BotBack
        -1f, 0f, -1f,   -1f, 0f, 1f,     0f, -1f, 0f //BotLeft
        };

    private float[] texcoords = new float[]{
        0f, 1f,     1f, 1f,     0.5f, 1f, 
        0f, 1f,     1f, 1f,     0.5f, 1f, 
        0f, 1f,     1f, 1f,     0.5f, 1f, 
        0f, 1f,     1f, 1f,     0.5f, 1f, 
        0f, 0f,     1f, 0f,     0.5f, 1f,
        0f, 0f,     1f, 0f,     0.5f, 1f,
        0f, 0f,     1f, 0f,     0.5f, 1f,
        0f, 0f,     1f, 0f,     0.5f, 1f
    };

    private float[] normals = new float[]{
        0f, 1f, 1f,     0f, 1f, 1f,     0f, 1f, 1f,
        1f, 1f, 0f,     1f, 1f, 0f,     1f, 1f, 0f,
        0f, 1f, -1f,    0f, 1f, -1f,    0f, 1f, -1f,
        -1f, 1f, 0f,    -1f, 1f, 0f,    -1f, 1f, 0f,
        0f, -1f, 1f,    0f, -1f, 1f,    0f, -1f, 1f,
        1f, -1f, 0f,    1f, -1f, 0f,    1f, -1f, 0f, 
        0f, -1f, -1f,   0f, -1f, -1f,   0f, -1f, -1f, 
        -1f, -1f, 0f,  -1f, -1f, 0f,    -1f, -1f, 0f   
    };

    public ManualDiamond(){
        super();
        setNumVertices(24);
        setVertices(vertices);
        setTexCoords(texcoords);
        setNormals(normals);

        setMatAmb(Utils.goldAmbient());
        setMatDif(Utils.goldDiffuse());
        setMatSpe(Utils.goldSpecular());
        setMatShi(Utils.goldShininess());
    }
}