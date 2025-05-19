package main;

import entity.*;
import object.SuperObject;
import tile.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.nio.channels.InterruptedByTimeoutException;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile, default size of player character or any pcs and map tiles
    final int scale = 3; //scaling, supaya character tidak terlalu kecil

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels


    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    //FPS
    int FPS = 60;

    //SYSTEM

    TileManager tileM = new TileManager(this); // ini msh aga gangerti knp param "this"

    
    //Collision Checker
    public CollisionChecker cChecker = new CollisionChecker(this);
    
    //Asset Setter
    public AssetSetter aSetter = new AssetSetter(this);
    
    //UI
    public UI ui = new UI(this);

    //KeyHandler
    KeyHandler keyH = new KeyHandler();
    
    //SOUND
    Sound music = new Sound();
    Sound se = new Sound();


    //Starting the Game Clock
    //once a thread started, it keeps the program running until stopped
    Thread gameThread;

    //OBJECT AND ENTITY

    //Bikin player buat apa?
    public Player player = new Player(this, keyH);

    //objects
    public SuperObject obj[] = new SuperObject[10]; //array of objects


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); //set the size of this class (JPanel)
        this.setBackground(Color.black);

        this.setDoubleBuffered(true); 
        //if set to true, all the drawing from this component will be done in an offscreen painting buffer
        //in short, enabling this can improve game's rendering performance

        this.addKeyListener(keyH);

        //with this, this GamePanel can be "focused" to receive key input
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject(); //set the objects in the game
        //this method is called in the constructor of GamePanel
        //so that when the game starts, the objects are already set

        playMusic(0);
    }

    public void startGameThread(){
        gameThread = new Thread(this); //passing GamePanel
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = Math.pow(10,9)/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer > Math.pow(10,9)){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        player.update();
    }

    //standard methods to draw things on JPanel
    // Graphics = a class that has many functions to draw objects on the screen
    @Override
    public void paintComponent(Graphics g){ //g berasal dari javaSwing
        super.paintComponent(g); //clear area

        //Graphics 2D = class extends the Graphics class to provice more sophisticated control over geometry,
        //coordinate transformations, color management, and text layout
        Graphics2D g2 = (Graphics2D) g;
        
        //TILE
        tileM.draw(g2);

        //OBJECTS
        for(int i = 0; i < obj.length; i++){
            if(obj[i] != null){
                obj[i].draw(g2, this);
            }
        }
        //PLAYER
        player.draw(g2);

        //UI
        ui.draw(g2);
        
        // dispose() -> dispose of this graphics context and release any system that is using
        g2.dispose();
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}

// @Override
// public void run(){

//     double drawInterval = 1000000000 / FPS; // 0.01666 seconds
//     double nextDrawTime = System.nanoTime() + drawInterval;

//     //this means, as long as this gameThread exist,
//     //it repeats the process that's written inside of these brackets {}
//     while(gameThread != null){
//         // System.out.println("The game loop is running");

//         long currentTime = System.nanoTime(); //return current value of the running JVM high-resolution time source, in nanoseconds
//         // System.out.println("current Time: " + currentTime);
//         // 1. UPDATE: update information such as character positions
//         update();

//         // 2. DRAW: draw the screen with the updated information
//         //kenapa pake repaint?
//         repaint();

//         //kenapa pake try catch
//         try{
//             double remainingTime = nextDrawTime - System.nanoTime();
//             remainingTime = remainingTime / 1000000;

//             if(remainingTime < 0){
//                 remainingTime = 0;
//             }
//             Thread.sleep((long)remainingTime);

//             nextDrawTime += drawInterval;
//         }
//         catch (InterruptedException e){
//             e.printStackTrace();
//         }
//     }

// }