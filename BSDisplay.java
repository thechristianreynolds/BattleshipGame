import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BSDisplay extends JPanel {

    int xStart1 = 50;
    int yStart1 = 50;
    int xStart2 = 430;
    int yStart2 = 50;
    int cellSize = 30;
    int shipsDeployed = 0;
    boolean blankDraw = false;
    double tokenScale = 0.70;
    int clickedRow, clickedCol;
    JRadioButton vertical = new JRadioButton("Vertical");
    JRadioButton horizontal = new JRadioButton("Horizontal");

    Color boardColor = Color.BLACK;
    Color shipColor = Color.RED;
    Color hitColor = Color.GREEN;
    Color missColor = Color.BLACK;

    BSLogic game;

    public BSDisplay(BSLogic game) {
        this.game = game;
        initWindow();

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                processClick(me);
            }
        });

        this.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent ke) {
                processKey(ke);
            }

            public void keyReleased(KeyEvent ke) {
            }

            public void keyTyped(KeyEvent ke) {
            }
        });

        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        JOptionPane.showMessageDialog(null, "Player 1 may now deploy ships", "Battleship Deployment Phase",1);
    }
    
    public void initWindow(){
        ButtonGroup deployOrientation = new ButtonGroup();
        
        horizontal.doClick();
        deployOrientation.add(vertical);
        deployOrientation.add(horizontal);
        JPanel northPanel = new JPanel();
        northPanel.add(horizontal, SwingConstants.CENTER);
        northPanel.add(vertical, SwingConstants.CENTER);

        this.add(northPanel, BorderLayout.NORTH);
    }

    public void processClick(MouseEvent me) {
        int clicked_X = me.getX();
        int clicked_Y = me.getY();
        int boardAdjust = 2;
        boolean vert = vertical.isSelected();
        if (!game.getGameOver()){
            if (game.isDeploy()) {
                shipsDeployed += 1;
                if (game.getTurn()) {
                    if (clicked_X < xStart1 || clicked_Y < yStart1 || clicked_X > (game.getCols() + boardAdjust) * cellSize
                            || clicked_Y > (game.getRows() + boardAdjust) * cellSize) {
                        System.err.println("You aren't on the board!");
                    } else {
                        clickedRow = (clicked_Y - yStart1) / cellSize;
                        clickedCol = (clicked_X - xStart1) / cellSize;
                        game.deployShip(clickedRow, clickedCol, vert);
                    }
                } else {
                    if (clicked_X < xStart2 || clicked_Y < yStart2
                            || clicked_X > ((game.getCols() + boardAdjust) * cellSize) + xStart2
                            || clicked_Y > (game.getRows() + boardAdjust) * cellSize) {
                        System.err.println("You aren't on the board!");
                    } else {
                        clickedRow = (clicked_Y - yStart2) / cellSize;
                        clickedCol = (clicked_X - xStart2) / cellSize;
                        game.deployShip(clickedRow, clickedCol, vert);
                    }
                }
            } else {
                if (game.getTurn()) {
                    if (clicked_X < xStart2 || clicked_Y < yStart2
                            || clicked_X > ((game.getCols() + boardAdjust) * cellSize) + xStart2
                            || clicked_Y > (game.getRows() + boardAdjust) * cellSize) {
                        System.err.println("You aren't on the board!");
                    } else {
                        clickedRow = (clicked_Y - yStart2) / cellSize;
                        clickedCol = (clicked_X - xStart2) / cellSize;
                        blankDraw = true;
                        repaint();
                        game.fireRound(clickedRow, clickedCol);
                    }
                } else {
                    if (clicked_X < xStart1 || clicked_Y < yStart1 || clicked_X > (game.getCols() + boardAdjust) * cellSize
                            || clicked_Y > (game.getRows() + boardAdjust) * cellSize) {
                        System.err.println("You aren't on the board!");
                    } else {
                        clickedRow = (clicked_Y - yStart1) / cellSize;
                        clickedCol = (clicked_X - xStart1) / cellSize;
                        blankDraw = true;
                        repaint();
                        game.fireRound(clickedRow, clickedCol);
                    }
                }
            }
        }
        if (shipsDeployed == 3){
            repaint();
            JOptionPane.showMessageDialog(null, "Player 2 may now deploy ships", "Battleship Deployment Phase",1);
            return;
        } else if (shipsDeployed == 6){
            blankDraw = true;
            repaint();
            shipsDeployed += 1;
            JOptionPane.showMessageDialog(null, "Player 1 may now fire!", "Battleship Deployment Phase",1);
        }
        repaint();
    }

    public void processKey(KeyEvent ke) {
        int code = ke.getKeyCode();
        System.err.print("\n Key Pressed " + code);

        // switch (code) {
        // case KeyEvent.VK_S:
        //     if (game.isDeploy()){
        //         JOptionPane.showMessageDialog(null, "All player must deploy ships before saving.", "Save Error",0);
        //     } else {
        //         clickedCol--;
        //         System.err.print("  save to file ");
        //         game.saveToFile("save.txt");
        //         return;
        //     }
        // case KeyEvent.VK_R:
        //     blankDraw = true;
        //     if (shipsDeployed >= 6){
        //         repaint();
        //         clickedCol--;
        //         System.err.print("  retrieve from file ");
        //         game.initFromFile("save.txt");
        //         repaint();
        //         return;
        //     } else {
        //         repaint();
        //         shipsDeployed = 7;
        //         clickedCol--;
        //         System.err.print("  retrieve from file ");
        //         game.initFromFile("save.txt");
        //         repaint();
        //         return;
        //     }
        // }
        // repaint();
    }

    public void setBlankDraw(boolean bool){
        blankDraw = bool;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x;
        int y = yStart1;
        boolean player1 = true;
        boolean player2 = false;
        int boardAdjust = 2;

        // Draw an empty board
        if (blankDraw){
            blankDraw = false;
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart1;
                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);

                    x += cellSize;
                }
                y += cellSize;
            }
            y = yStart2;
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart2;
                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);

                    x += cellSize;
                }
                y += cellSize;
            }
        }
        // Draw the board during the Deployment phase.
        else if (game.isDeploy()){
            // Draw the board for Player 1
            if (game.getTurn()){
                for (int row = 0; row < game.getRows(); row++) {
                    x = xStart1;
                    for (int col = 0; col < game.getCols(); col++) {
                        g.setColor(boardColor);
                        g.drawRect(x, y, cellSize, cellSize);

                        String cell = game.getTile(row, col, player1);
                        if (cell.equals("o")){
                            
                        } else{
                            int tokenSize = (int) (cellSize * tokenScale);
                            int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                            g.setColor(shipColor);
                            g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                        }
                        x += cellSize;
                    }
                    y += cellSize;
                }
            }
            // Draw the board for Player 2
            else{
                x = xStart2;
                for (int row = 0; row < game.getRows(); row++) {
                    x = xStart2;
                    for (int col = 0; col < game.getCols(); col++) {
                        g.setColor(boardColor);
                        g.drawRect(x, y, cellSize, cellSize);

                        String cell = game.getTile(row, col, player2);
                        if (cell.equals("o")){
                            
                        } else{
                            int tokenSize = (int) (cellSize * tokenScale);
                            int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                            g.setColor(shipColor);
                            g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                        }
                        x += cellSize;
                    }
                    y += cellSize;
                }
            }
        }
        // Draw the board after the game is over.
        else if (game.getGameOver()){
            // Draw the board for Player 1
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart1;
                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player1);
                    if (cell.equals("o")){

                    } else if (cell.equals("x")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if(cell.equals("m")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else{
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(shipColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    }
                    x += cellSize;
                }
                y += cellSize;
            }
            y = yStart2;
            // Draw the board for Player 2
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart2;

                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player2);
                    if (cell.equals("o")){
                        
                    } else if (cell.equals("x")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if(cell.equals("m")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else{
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(shipColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    }
                    x += cellSize;
                }
                y += cellSize;
            }
        } 
        // Draw the board during the Player 1 Fire phase.
        else if (game.getTurn()){
            // Draw the board for Player 1
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart1;
                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player1);
                    if (cell.equals("o")){

                    } else if (cell.equals("x")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if(cell.equals("m")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else{
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(shipColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    }
                    x += cellSize;
                }
                y += cellSize;
            }
            y = yStart2;
            // Draw the board for Player 2
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart2;
                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player2);
                    if (cell.equals("x")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if(cell.equals("m")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    }
                    x += cellSize;
                }
                y += cellSize;
            }
        } 
        // Draw the board during the Player 2 Fire phase.
        else if (!game.getTurn()){
            // Draw the board for Player 1
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart1;

                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player1);
                    if (cell.equals("x")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if(cell.equals("m")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    }
                    x += cellSize;
                }
                y += cellSize;
            }
            y = yStart2;
            // Draw the board for Player 2
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart2;

                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player2);
                    if (cell.equals("o")){
                        
                    } else if (cell.equals("x")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if(cell.equals("m")){
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else{
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(shipColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    }
                    x += cellSize;
                }
                y += cellSize;
            }
        }
    }
}