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
import java.util.LinkedList;
import java.util.Iterator;
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
    private LinkedList<Spatial> poolFloor ; 
    //private LinkedList<Spatial> floor ;
    public MainGame(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        flyCamera = app.getFlyByCamera();
        camera = app.getCamera();
        poolFloor = new LinkedList<>();
       
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        //System 3-person harus mematikan flycamera
       flyCamera.setEnabled(false);
       // sebenernya ini gw ga tau fungsinya , tapi tadi di tutorial dia ngomong
       // biar character dan lantai bisa saling kenal jadinya si character bisa
       // berdiri tepat diatas si lantai
       bulletAppState = new BulletAppState();
       bulletAppState.setDebugEnabled(true);
       //bawaan harus dimasukin 
       stateManager.attach(bulletAppState);
       // memasukan semua object ke root node -> kumpulan object utama
       rootNode.attachChild(localRootNode);
       //load scenenya ( tanah dan character)
       Spatial scene = assetManager.loadModel("Scenes/LandmarkScene.j3o");
       // karena ini adalah state bermain jadi dimasukin ke local root
       localRootNode.attachChild(scene);
       // ada 2 lantai, untuk membedakan , ambil ke 2nya
       floor = localRootNode.getChild("Lantai");
       floor2 = localRootNode.getChild("Lantai2");
       //masukin ke linkedlist biar gampang di updatenya
       poolFloor.addFirst(floor);
       poolFloor.addFirst(floor2);
       //buat kedua lantai saling kenal dengan character
       bulletAppState.getPhysicsSpace().add(floor.getControl(RigidBodyControl.class));
       bulletAppState.getPhysicsSpace().add(floor2.getControl(RigidBodyControl.class));
       //load player
       player = localRootNode.getChild("Player");
       BoundingBox bb =(BoundingBox)player.getWorldBound();
       float r = bb.getXExtent();
       float h =bb.getYExtent();
       CapsuleCollisionShape ccs = new CapsuleCollisionShape(r, h);
       playerControl = new CharacterControl(ccs,1.0f);
       //masukan player control agar dia ga terlalu dumb
       player.addControl(playerControl);
       //buat character kenal dengan lantai
       bulletAppState.getPhysicsSpace().add(playerControl);
       //fly camera diganti sama chase camera , targetnya player
       chaseCamera = new ChaseCamera(camera, player,inputManager);
       //rotasi ini membuat camera tepat dibelakang player
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
         Iterator<Spatial> it = poolFloor.iterator();
     
         while(it.hasNext()){
            
             Spatial s = it.next();
             s.move(-20*tpf,0,0);
             if(s.getLocalTranslation().getX()<=-100){
                 s.getLocalTranslation().setX(s.getLocalTranslation().getX()+200);
             }
         }
    }
    
    
    
    }
    
    
    

