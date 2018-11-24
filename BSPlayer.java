import java.util.*;
import java.io.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
            MediaPlayer mediaPlayer = playSound(true);
            mediaPlayer.play();
            JOptionPane.showMessageDialog(null, "A ship has been sunk!", "Battleship Fire Phase",1);
            return;
        }
        MediaPlayer mediaPlayer = playSound(false);
        mediaPlayer.play();
        JOptionPane.showMessageDialog(null, "You hit something!", "Battleship Fire Phase",1);
    }

    public MediaPlayer playSound(boolean sink){
        JFXPanel panel = new JFXPanel();
        // Unique sounds for sinking a ship should be added
        if (sink){
            Random rand = new Random();
            int randNum = rand.nextInt(4)+1;
            String bip = "Sounds/explosion" + randNum + ".mp3";
            Media hit = new Media(new File(bip).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            return mediaPlayer;
        } else {
            Random rand = new Random();
            int randNum = rand.nextInt(4)+1;
            String bip = "Sounds/explosion" + randNum + ".mp3";
            Media hit = new Media(new File(bip).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            return mediaPlayer;
        }
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

    public void setBoard(String saveBoard){
        int x = 0;
        int ship0 = 0;
        int ship1 = 0;
        int ship2 = 0;
        for(int j = 0; j < board.length; j++){
            for(int k = 0; k < 10; k++){
                if (saveBoard.charAt(x) == '0'){
                    ship0 += 1;
                } else if (saveBoard.charAt(x) == '1'){
                    ship1 += 1;
                } else if (saveBoard.charAt(x) == '2'){
                    ship2 += 1;
                }
                board[j][k] = Character.toString(saveBoard.charAt(x));
                x++;
            }
        }
        loadShips(ship0, ship1, ship2);
    }

    public void loadShips(int ship0, int ship1, int ship2){
        ships[0] = ship0;
        ships[1] = ship1;
        ships[2] = ship2;
    }    

    @Override
    public String toString() {
        String string = "";
        for(int i = 0; i < board.length; i ++){
            for(int j = 0; j < 10; j++){
                string += board[i][j];
            }
        }
        return string;
    }
}