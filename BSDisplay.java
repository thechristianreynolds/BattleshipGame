import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

public class BSDisplay extends JPanel {

    int xStart1 = 50;
    int yStart1 = 50;
    int xStart2 = 430;
    int yStart2 = 50;
    int cellSize = 30;
    double tokenScale = 0.70;
    int clickedRow, clickedCol;
    JRadioButton vertical = new JRadioButton("Vertical");
    JRadioButton horizontal = new JRadioButton("Horizontal");
    JButton ready = new JButton("Ready");
    JPanel southPanel = new JPanel();
    boolean boardWait = false;
    int waitTime = 1;

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
    
    public void addButton(){
        southPanel.add(ready, SwingConstants.CENTER);
        this.add(southPanel, BorderLayout.NORTH);
    }

    public void processClick(MouseEvent me) {
        int clicked_X = me.getX();
        int clicked_Y = me.getY();
        int boardAdjust = 2;
        boolean vert = vertical.isSelected();
        if (!game.getGameOver()){
            if (game.isDeploy()) {
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
                        game.fireRound(clickedRow, clickedCol);
                    }
                } else {
                    if (clicked_X < xStart1 || clicked_Y < yStart1 || clicked_X > (game.getCols() + boardAdjust) * cellSize
                            || clicked_Y > (game.getRows() + boardAdjust) * cellSize) {
                        System.err.println("You aren't on the board!");
                    } else {
                        clickedRow = (clicked_Y - yStart1) / cellSize;
                        clickedCol = (clicked_X - xStart1) / cellSize;
                        game.fireRound(clickedRow, clickedCol);
                    }
                }
            }
        }
        repaint();
    }

    public void processKey(KeyEvent ke) {
        int code = ke.getKeyCode();
        System.err.print("\n Key Pressed " + code);

        switch (code) {
        case KeyEvent.VK_S:
            clickedCol--;
            System.err.print("  save to file ");
            game.saveToFile("TrialGame.dat");
            return;
        case KeyEvent.VK_R:
            clickedCol--;
            System.err.print("  retrieve from file ");
            game.initFromFile("TrialGame.dat");
            repaint();
            return;
        }
        repaint();
    }

    public void sleepTime(){
        try{
            TimeUnit.SECONDS.sleep(waitTime);
            repaint();
        } catch (InterruptedException e){

        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x;
        int y = yStart1;
        boolean player1 = true;
        boolean player2 = false;
        int boardAdjust = 2;

        // if (boardWait){
        //     boardWait = false;
        //     for (int row = 0; row < game.getRows(); row++) {
        //         x = xStart1;
        //         for (int col = 0; col < game.getCols(); col++) {
        //             g.setColor(boardColor);
        //             g.drawRect(x, y, cellSize, cellSize);
        //             int tokenSize = (int) (cellSize * tokenScale);
        //             int tokenIndent = (cellSize - tokenSize) / boardAdjust;
        //             g.setColor(Color.white);
        //             g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
        //             x += cellSize;
        //         }
        //         y += cellSize;
        //     }
        //     y = yStart2;
        //     for (int row = 0; row < game.getRows(); row++) {
        //         x = xStart2;
        //         for (int col = 0; col < game.getCols(); col++) {
        //             g.setColor(boardColor);
        //             g.drawRect(x, y, cellSize, cellSize);
        //             int tokenSize = (int) (cellSize * tokenScale);
        //             int tokenIndent = (cellSize - tokenSize) / boardAdjust;
        //             g.setColor(Color.white);
        //             g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
        //             x += cellSize;
        //         }
        //         y += cellSize;
        //     }
        //     // sleepTime();
        //     addButton();
        //     // try{
        //     //     TimeUnit.SECONDS.sleep(waitTime);
        //     //     repaint();
        //     // } catch (InterruptedException e){
    
        //     // }
        // }else{
            // Draw the board during the Deployment phase.
            if (game.isDeploy()){
                // Draw the board for Player 1
                if (game.getTurn()){
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
                }
                // Draw the board for Player 2
                else{
                    y = yStart2;
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
                boardWait = true;
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
                boardWait = true;
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
        // }
    }
}