package main;

import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //this let window properlu close when user clicks the close ("x") button.
        window.setResizable(false);
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); //causes this Window to be sized to fit the preferred size and layouts of its subcomponents

        window.setLocationRelativeTo(null); //Not spesify the location of the window = the window will be displayed at the center of the screen
        window.setVisible(true); //visible = true, so we can see the display
        
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}