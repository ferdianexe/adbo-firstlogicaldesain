/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Ferdian
 */
public class MainGame extends AbstractAppState {

    private final Node rootNode;
    private final Node localRootNode = new Node("Main Game");
    private final AssetManager assetManager;
    private BulletAppState bulletAppState;
    private CharacterControl playerControl;
    private Spatial player;
    private final InputManager inputManager;
    private final FlyByCamera flyCamera;
    private final Camera camera;
    private ChaseCamera chaseCamera;
    private final Vector3f playerWalkDirection = Vector3f.ZERO;
    private CameraNode cameraNode;
    private Spatial floor,floor2;
    
    //private LinkedList<Spatial> floor ;
    public MainGame(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        flyCamera = app.getFlyByCamera();
        camera = app.getCamera();
       
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
       flyCamera.setEnabled(false);
       bulletAppState = new BulletAppState();
       bulletAppState.setDebugEnabled(true);
       stateManager.attach(bulletAppState);
       rootNode.attachChild(localRootNode);
       Spatial scene = assetManager.loadModel("Scenes/LandmarkScene.j3o");
       localRootNode.attachChild(scene);
       floor = localRootNode.getChild("Lantai");
       floor2 = localRootNode.getChild("Lantai2");
       bulletAppState.getPhysicsSpace().add(floor.getControl(RigidBodyControl.class));
       bulletAppState.getPhysicsSpace().add(floor2.getControl(RigidBodyControl.class));
       //load player
       player = localRootNode.getChild("Player");
       BoundingBox bb =(BoundingBox)player.getWorldBound();
       float r = bb.getXExtent();
       float h =bb.getYExtent();
       CapsuleCollisionShape ccs = new CapsuleCollisionShape(r, h);
       playerControl = new CharacterControl(ccs,1.0f);
       player.addControl(playerControl);
       bulletAppState.getPhysicsSpace().add(playerControl);
       chaseCamera = new ChaseCamera(camera, player,inputManager);    
       chaseCamera.setDefaultHorizontalRotation(-3.2f);
       chaseCamera.setDefaultVerticalRotation(0.3f);
  
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        super.cleanup(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(float tpf) {
//         Iterator<Spatial> it = floor.iterator();
//     
//         while(it.hasNext()){
//            
//             Spatial s = it.next();
//             s.move(-2*tpf,0,0);
//             if(s.getLocalTranslation().getX()<=-20){
//                 s.getLocalTranslation().setX(s.getLocalTranslation().getX()+40);
//             }
    
      floor.move(-20*tpf, 0, 0);
      floor2.move(-20*tpf,0, 0);
      if(floor.getLocalTranslation().x<=-100){
          floor.setLocalTranslation(floor.getLocalTranslation().setX(floor.getLocalTranslation().x+200));
        }
      if(floor2.getLocalTranslation().x<=-100){
        floor2.setLocalTranslation(floor2.getLocalTranslation().setX(floor2.getLocalTranslation().x+200));
          
      }
    
    }
    
    
    
}
