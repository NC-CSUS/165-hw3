package tage.nodeControllers;
import tage.*;
import org.joml.*;
import tage.GameObject;
import tage.NodeController;
/**Invisibility controller allows objects to have their invisibility toggled based on the amount of time passed */
public class InvisibilityController extends NodeController{

    private float flipCounter;
    private boolean toggleFlip;
    private Engine engine;
    private int counter;
    


    /**Creates an invisibility controller with toggle invisbility  on/off based on amount of time specified */
    public InvisibilityController(Engine e, float speed){
        super();
        toggleFlip = false;
        counter = 0;
        engine = e;
        flipCounter = speed * 60;

    }

    public void apply(GameObject obj){
        counter++;
            if(counter > flipCounter){
                if(toggleFlip == false){
                    toggleFlip = true;
                    obj.getRenderStates().disableRendering();
                }else{
                    toggleFlip = false;
                    obj.getRenderStates().enableRendering();
                }
                counter = 0;
            }
    }

}