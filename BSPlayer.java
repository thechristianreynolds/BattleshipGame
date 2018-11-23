import java.util.*;
import javax.swing.*;

/**
 * BSPlayer
 */
public class BSPlayer {
    private String[][] board;
    private int[] ships;
    private int shipsDeployed = 0;
    private int shipHP = 3;

    public BSPlayer(int rows, int cols, int ships) {
        this.board = new String[rows][cols];
        this.ships = new int[ships];
        for (String[] row : board) {
            Arrays.fill(row, "o");
        }
    }
    
    public boolean deployShips(int row, int col, boolean vert) {
        int[][] tileSet = convShipTiles(row, col, vert);
        if (!shipAlreadyThere(tileSet)) {
            for (int x = 0; x < tileSet.length; x++) {
                board[tileSet[x][0]][tileSet[x][1]] = Integer.toString(shipsDeployed);
            }
            ships[shipsDeployed] = shipHP;
            shipsDeployed++;
            return true;
        } else{
            return false;
        }
    }

    public int[][] convShipTiles(int row, int col, boolean vert) {
        if (vert) {
            int[] tile1 = new int[] { row + 1, col };
            int[] tile2 = new int[] { row, col };
            int[] tile3 = new int[] { row - 1, col };
            int[][] tileSet = new int[][] { tile1, tile2, tile3 };
            return tileSet;
        } else {
            int[] tile1 = new int[] { row, col + 1 };
            int[] tile2 = new int[] { row, col };
            int[] tile3 = new int[] { row, col - 1 };
            int[][] tileSet = new int[][] { tile1, tile2, tile3 };
            return tileSet;
        }
    }

    public Boolean shipAlreadyThere(int[][] tileSet) {
        Boolean shipThere = false;
        for (int x = 0; x < tileSet.length; x++) {
            if (!board[tileSet[x][0]][tileSet[x][1]].equals("o")) {
                shipThere = true;
                return shipThere;
            }
        }
        return shipThere;
    }

    public String getTile(int row, int col) {
        return board[row][col];
    }

    public void fireRound(int row, int col) {
        checkHit(row, col);
    }

    public boolean checkHit(int row, int col) {
        String tile = board[row][col];
        if (tile.equals("o")) {
            board[row][col] = "m";
            JOptionPane.showMessageDialog(null, "No ship there!", "Battleship Fire Phase",1);
            return true;
        } else if (tile.equals("x") || tile.equals("m")) {
            JOptionPane.showMessageDialog(null, "This tile has already been hit!", "Battleship Fire Phase",1);
            return false;
        } else {
            int shipIndex = Integer.parseInt(tile);
            shipHit(shipIndex);
            board[row][col] = "x";
            return true;
        }
    }

    public void shipHit(int ship) {
        ships[ship] -= 1;
        if (ships[ship] == 0){
            JOptionPane.showMessageDialog(null, "A ship has been sunk!", "Battleship Fire Phase",1);
            return;
        }
        JOptionPane.showMessageDialog(null, "You hit something!", "Battleship Fire Phase",1);
    }

    public boolean checkShips(){
        boolean shipsLeft = false;
        for (int x = 0; x < ships.length; x++){
            if (ships[x] != 0){
                shipsLeft = true;
            }
        }
        return shipsLeft;
    }
}