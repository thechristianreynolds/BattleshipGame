
/* 
* Author: Austin Kibler, Samir Lamichhane, Christian Reynolds
* Purpose: This is the main window containing the main program function
* Date: 11/28/2018
*/

import java.awt.*;
import javax.swing.*;

public class BSWindow extends JFrame {

    private int winWidth = 800;
    private int winHeight = 450;
    int cellAcross = 10;
    int cellDown = 10;
    BSDisplay display;

    BSMenu menuBar = new BSMenu(this);

    public BSWindow() {
        this.setTitle("Battleship!");
        this.setSize(winWidth, winHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        display = new BSDisplay();
        this.add(display, BorderLayout.CENTER);

        this.setJMenuBar(menuBar);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        BSWindow aw = new BSWindow();
    }
}
