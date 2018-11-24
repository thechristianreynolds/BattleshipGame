import java.awt.*;
import javax.swing.*;

public class BSWindow extends JFrame {

    private int winWidth = 800;
    private int winHeight = 450;
    BSDisplay display;
    BSLogic game;
    int cellAcross = 10;
    int cellDown = 10;

    BSMenu menuBar = new BSMenu(this);

    public BSWindow() {
        this.setTitle("Battleship!");
        this.setSize(winWidth, winHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        game = new BSLogic(cellDown, cellAcross);
        display = new BSDisplay(game);
        this.add(display, BorderLayout.CENTER);

        this.setJMenuBar(menuBar);
        
        this.setVisible(true);
    }

    public static void main(String[] args) {
        BSWindow aw = new BSWindow();
    }
}
