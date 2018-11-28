
/* 
* Author: Austin Kibler, Samir Lamichhane, Christian Reynolds
* Purpose: The display class is responsible for displaying the game graphics also telling each player when it is their turn
* Date: 11/28/2018
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class BSDisplay extends JPanel {

    private int xStart1 = 50;
    private int yStart1 = 50;
    private int xStart2 = 430;
    private int yStart2 = 50;
    private int cellAcross = 10;
    private int cellDown = 10;
    private int cellSize = 30;
    private double tokenScale = 0.70;
    private int clickedRow, clickedCol;
    private boolean blankDraw = false;
    private boolean p1FirstFire = true;
    private boolean p2FirstDeploy = true;

    private JRadioButton vertical = new JRadioButton("Vertical");
    private JRadioButton horizontal = new JRadioButton("Horizontal");
    private JLabel p1ShipCount = new JLabel();
    private JLabel p2ShipCount = new JLabel();
    private JPanel orientationPanel = new JPanel();
    private JPanel scorePanel = new JPanel();

    private Color boardColor = Color.BLACK;
    private Color shipColor = Color.RED;
    private Color hitColor = Color.GREEN;
    private Color missColor = Color.BLACK;

    BSLogic game;

    public BSDisplay() {
        this.game = new BSLogic(cellAcross, cellDown);
        initWindow();

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                processClick(me);
            }
        });

        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        JOptionPane.showMessageDialog(null, "Player 1 may now deploy ships", "Battleship Deployment Phase", 1);
    }

    public void initWindow() {
        ButtonGroup deployOrientation = new ButtonGroup();

        horizontal.doClick();
        deployOrientation.add(vertical);
        deployOrientation.add(horizontal);
        orientationPanel.add(horizontal, SwingConstants.CENTER);
        orientationPanel.add(vertical, SwingConstants.CENTER);

        this.add(orientationPanel, BorderLayout.NORTH);
    }

    public void newGame() {
        this.remove(scorePanel);
        this.blankDraw = false;
        this.p1FirstFire = true;
        this.p2FirstDeploy = true;
        // BSLogic nextGame = new BSLogic(cellAcross, cellDown);
        this.game = new BSLogic(cellAcross, cellDown);
        initWindow();
        repaint();
        JOptionPane.showMessageDialog(null, "Player 1 may now deploy ships", "Battleship Deployment Phase", 1);
    }

    public void updateWindow() {
        this.remove(orientationPanel);
        String p1Label = "Player 1: " + game.findShipCount(game.player1);
        p1ShipCount.setText(p1Label);
        String p2Label = "Player 2: " + game.findShipCount(game.player2);
        p2ShipCount.setText(p2Label);

        scorePanel.add(p2ShipCount, SwingConstants.CENTER);
        scorePanel.add(p1ShipCount, SwingConstants.CENTER);
        this.add(scorePanel, BorderLayout.NORTH);
    }

    public void processClick(MouseEvent me) {
        int clicked_X = me.getX();
        int clicked_Y = me.getY();
        int boardAdjust = 2;
        boolean vert = vertical.isSelected();
        if (!game.getGameOver()) {
            if (game.isDeploy()) {
                if (game.getTurn()) {
                    if (clicked_X < xStart1 || clicked_Y < yStart1
                            || clicked_X > (game.getCols() + boardAdjust) * cellSize
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
                    if (clicked_X < xStart1 || clicked_Y < yStart1
                            || clicked_X > (game.getCols() + boardAdjust) * cellSize
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
        if (game.getShipsDeployed() == (game.getShipMax()) && p2FirstDeploy) {
            p2FirstDeploy = false;
            repaint();
            JOptionPane.showMessageDialog(null, "Player 2 may now deploy ships", "Battleship Deployment Phase", 1);
        } else if (game.getShipsDeployed() == game.getShipMax() * 2) {
            updateWindow();
            if (p1FirstFire) {
                p1FirstFire = false;
                blankDraw = true;
                repaint();
                JOptionPane.showMessageDialog(null, "Player 1 may now fire!", "Battleship Deployment Phase", 1);
            }
        }
        if (game.getGameOver()) {
            showHighScores();
        }
        repaint();
    }

    public void showHighScores() {
        try {
            File records = new File("records.txt");
            String message = "High Scores\n";
            if (!game.getGameOver()) {
                if (records.exists()) {
                    Scanner scanner = new Scanner(records);
                    while (scanner.hasNext()) {
                        message += scanner.nextLine() + "\n";
                    }
                    JOptionPane.showMessageDialog(null, message, "Records", 1);
                } else {
                    JOptionPane.showMessageDialog(null, "Records not found.", "Records", 0);
                }
            } else {
                if (records.exists()) {
                    Scanner scanner = new Scanner(records);
                    while (scanner.hasNext()) {
                        message += scanner.nextLine() + "\n";
                    }
                    JOptionPane.showMessageDialog(null, message, "Records", 1);
                    int choice = JOptionPane.showOptionDialog(null, "Would you like to play again?", "Records",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (choice == JOptionPane.YES_OPTION) {
                        newGame();
                    } else {
                        System.exit(0);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Records not found.", "Records", 0);
                }
            }
        } catch (Exception e) {
            System.err.println("Issue loading records");
        }
    }

    public BSLogic getGame() {
        return game;
    }

    public void setBlankDraw(boolean bool) {
        blankDraw = bool;
    }

    public void setP1FirstFire(boolean p1FirstFire) {
        this.p1FirstFire = p1FirstFire;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x;
        int y = yStart1;
        boolean player1 = true;
        boolean player2 = false;
        int boardAdjust = 2;

        // Draw an empty board
        if (blankDraw) {
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
        else if (game.isDeploy()) {
            // Draw the board for Player 1
            if (game.getTurn()) {
                for (int row = 0; row < game.getRows(); row++) {
                    x = xStart1;
                    for (int col = 0; col < game.getCols(); col++) {
                        g.setColor(boardColor);
                        g.drawRect(x, y, cellSize, cellSize);

                        String cell = game.getTile(row, col, player1);
                        if (cell.equals("o")) {

                        } else {
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
            else {
                x = xStart2;
                for (int row = 0; row < game.getRows(); row++) {
                    x = xStart2;
                    for (int col = 0; col < game.getCols(); col++) {
                        g.setColor(boardColor);
                        g.drawRect(x, y, cellSize, cellSize);

                        String cell = game.getTile(row, col, player2);
                        if (cell.equals("o")) {

                        } else {
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
        else if (game.getGameOver()) {
            // Draw the board for Player 1
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart1;
                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player1);
                    if (cell.equals("o")) {

                    } else if (cell.equals("x")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if (cell.equals("m")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else {
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
                    if (cell.equals("o")) {

                    } else if (cell.equals("x")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if (cell.equals("m")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else {
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
        else if (game.getTurn()) {
            // Draw the board for Player 1
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart1;
                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player1);
                    if (cell.equals("o")) {

                    } else if (cell.equals("x")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if (cell.equals("m")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else {
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
                    if (cell.equals("x")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if (cell.equals("m")) {
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
        else if (!game.getTurn()) {
            // Draw the board for Player 1
            for (int row = 0; row < game.getRows(); row++) {
                x = xStart1;

                for (int col = 0; col < game.getCols(); col++) {
                    g.setColor(boardColor);
                    g.drawRect(x, y, cellSize, cellSize);
                    String cell = game.getTile(row, col, player1);
                    if (cell.equals("x")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if (cell.equals("m")) {
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
                    if (cell.equals("o")) {

                    } else if (cell.equals("x")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(hitColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else if (cell.equals("m")) {
                        int tokenSize = (int) (cellSize * tokenScale);
                        int tokenIndent = (cellSize - tokenSize) / boardAdjust;
                        g.setColor(missColor);
                        g.fillOval(x + tokenIndent, y + tokenIndent, tokenSize, tokenSize);
                    } else {
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