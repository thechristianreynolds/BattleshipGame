import java.io.*;
import java.util.*;
import javax.swing.*;

public class BSLogic {

    private int rows;
    private int cols;
    private int ships = 3;
    private int shipsDeployed = 0;
    public BSPlayer player1;
    public BSPlayer player2;
    private boolean deployPhase;
    private boolean p1Turn;
    private boolean gameOver = false;

    public BSLogic(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.player1 = new BSPlayer(rows, cols, ships);
        this.player2 = new BSPlayer(rows, cols, ships);
        this.deployPhase = true;
        this.p1Turn = true;
    }

    /**
     * @TODO Update to save player boards
     */
    public void saveToFile(String fName) {

    }

    /**
     * @TODO Update to load player boards
     */
    public void initFromFile(String fName) {

    }

    public void deployShip(int row, int col, boolean vert) {
        if (deploymentOnBoard(row, col, vert)) {
            if (shipsDeployed < ships) {
                if (player1.deployShips(row, col, vert)){
                    shipsDeployed++;
                    System.err.println("Player one ship deployed at: " + row + ", " + col);
                } else{
                    System.err.println("A ship is already there!");
                }
            } else if (shipsDeployed < (ships * 2)) {
                if (player2.deployShips(row, col, vert)){
                    shipsDeployed++;
                    System.err.println("Player two ship deployed at: " + row + ", " + col);
                } else{
                    System.err.println("A ship is already there!");
                }
            }
            if (shipsDeployed == (ships * 2)) {
                deployPhase = false;
                p1Turn = true;
                System.err.println("Player one may now fire!");
            } else if (shipsDeployed == ships) {
                p1Turn = false;
                System.err.println("Player two may deploy ships!");
            }
        } else {
            System.err.println("That ship is out of bounds!");
        }
    }

    public boolean deploymentOnBoard(int row, int col, boolean vert) {
        if (vert) {
            if ((row - 1) < 0 || (row + 1) > 9) {
                return false;
            }
        } else {
            if ((col - 1) < 0 || (col + 1) > 9) {
                return false;
            }
        }
        return true;
    }

    public void fireRound(int row, int col) {
        if (p1Turn) {
            if (player2.checkHit(row, col)){
                if (checkWinner()){
                    JOptionPane.showMessageDialog(null, "Player 2 may now fire!", "Battleship Deployment Phase",1);
                    p1Turn = false;
                }
            }
        } else{
            if (player1.checkHit(row, col)){
                if (checkWinner()){
                    JOptionPane.showMessageDialog(null, "Player 1 may now fire!", "Battleship Deployment Phase",1);
                    p1Turn = true;
                }
            }
        }
    }

    public boolean checkWinner(){
        if (p1Turn){
            if (!player2.checkShips()){
                JOptionPane.showMessageDialog(null, "Player 2 has won!", "Battleship Deployment Phase",1);
                gameOver = true;
                return !gameOver;
            }
        } else{
            if (!player1.checkShips()){
                JOptionPane.showMessageDialog(null, "Player 2 has won!", "Battleship Deployment Phase",1);
                gameOver = true;
                return !gameOver;
            }
        }
        return !gameOver;
    }

    public String getTile(int row, int col, boolean player) {
        if (player) {
            return player1.getTile(row, col);
        } else {
            return player2.getTile(row, col);
        }
    }

    public void setTurn(boolean turn) {
        p1Turn = turn;
    }

    public boolean getTurn() {
        return p1Turn;
    }

    public boolean isDeploy() {
        return deployPhase;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getShipCount() {
        return ships;
    }

    public boolean getGameOver(){
        return gameOver;
    }
}
