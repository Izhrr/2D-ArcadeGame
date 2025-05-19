package entity;
import main.*;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Entity {
    public int worldX,worldY; //akan menggunakan 2 jenis x dan y yaitu world dan screen
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;

    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false; //true if the entity has collision, false if not
}
