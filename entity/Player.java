package entity;

import main.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.nio.channels.InterruptedByTimeoutException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX; //buat posisi player di layar (always center)
    public final int screenY; //buat posisi player di layar (always center)

    public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2); //set player to center of screen
        screenY = gp.screenHeight/2 - (gp.tileSize/2); //set player to center of screen

        //tujuannya biar player nabrak collisionnya ga aneh (hanya beberapa bagian player yang nabrak)
        solidArea = new Rectangle();
        solidArea.x = 8; //x position of the solid area
        solidArea.y = 16; //y position of the solid area
        solidArea.width = 32; //width of the solid area
        solidArea.height = 32; //height of the solid area
        solidAreaDefaultX = solidArea.x; //default x position of the solid area
        solidAreaDefaultY = solidArea.y; //default y position of the solid area

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/resource/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/resource/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/resource/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/resource/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/resource/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resource/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/resource/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resource/player/boy_right_2.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void update(){
        //In Java the upper left corner is X:0 Y:0
        //X values increases to the right
        //Y values increases as they go down
        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){
            if(keyH.upPressed == true){
                direction = "up";
                // worldY -= speed;
            }
            else if(keyH.downPressed == true){
                direction = "down";
                // worldY += speed;
            }
            else if(keyH.leftPressed == true){
                direction = "left";
                // worldX -= speed;
            }
            else if(keyH.rightPressed == true){
                direction = "right";
                // worldX += speed;
            }
            
            // CHECK TILE COLLISION
            collision = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(collision == false){
                switch(direction){
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            // buat visualisasi gerakan player
            spriteCounter++;
            if(spriteCounter > 12){
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int i){
        if(i != 999){
            String objectName = gp.obj[i].name;
            switch(objectName){
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null; // obj jadi hilang
                    gp.ui.showMessage("You got a key!");
                    break;
                case "Boots":
                    gp.playSE(2);
                    speed += 2;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed up!");
                    break;
                case "Door":
                    if(hasKey > 0){
                            gp.playSE(3);
                            gp.obj[i] = null;
                            hasKey--;
                            gp.ui.showMessage("You opened the door!");
                    }
                    else{
                        gp.ui.showMessage("You need a key!");
                    }
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;
        switch(direction){
            case "up":
                if(spriteNum == 1){
                    image = up1;
                }
                if(spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2){
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
 