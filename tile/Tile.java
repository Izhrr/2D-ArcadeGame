package tile;

import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision = false; //true if the tile has collision, false if not
    //collision is used to check if the player can walk through the tile or not
    //if true, player cannot walk through the tile
    //if false, player can walk through the tile
}
