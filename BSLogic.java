import java.io.*;
import java.util.*;
import javax.swing.*;

public class BSLogic {

    private int rows;
    private int cols;
    private int shipMax = 5;
    private int shipsDeployed = 0;
    public BSPlayer player1;
    public BSPlayer player2;
    private boolean deployPhase;
    private boolean p1Turn;
    private boolean gameOver = false;

    public BSLogic(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.player1 = new BSPlayer(rows, cols, shipMax);
        this.player2 = new BSPlayer(rows, cols, shipMax);
        this.deployPhase = true;
        this.p1Turn = true;
    }

    public void saveHighScore(String name, int score) {
        try {
            File records = new File("records.txt");

            ArrayList scores = new ArrayList<>();
            ArrayList newScore = new ArrayList<>();
            newScore.add(name);
            newScore.add(score);
            scores.add(newScore);
            int HSListMax = 10;
            Scanner scanner = new Scanner(records);
            for (int i = 0; i < HSListMax; i++) {
                if (scanner.hasNext()) {
                    ArrayList record = new ArrayList<>();
                    String nextName = scanner.next();
                    int nextScore = scanner.nextInt();
                    record.add(nextName);
                    record.add(nextScore);
                    scores.add(record);
                    System.err.println(nextName + " " + nextScore);
                }
            }
            if (scores.size() > 1) {
                scores = highScoreSort(scores);
            }
            if (scores.size() > HSListMax) {
                scores.remove(HSListMax);
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(records), "UTF-8"));
            for (int i = 0; i < scores.size(); i++) {
                ArrayList record = (ArrayList) scores.get(i);
                String recordName = (String) record.get(0);
                int recordScore = (int) record.get(1);
                writer.write(recordName + " " + recordScore + "\n");
            }
            scanner.close();
            writer.close();
        } catch (Exception e) {
            System.err.println("Issue saving new high score.");
        }
    }

    public ArrayList highScoreSort(ArrayList scores) {
        for (int i = 0; i < scores.size() - 1; i++) {
            int index = i;
            for (int j = index + 1; j < scores.size(); j++) {
                ArrayList indexList = (ArrayList) scores.get(index);
                ArrayList jList = (ArrayList) scores.get(j);
                int indexScore = (int) indexList.get(1);
                int jScore = (int) jList.get(1);
                if (jScore < indexScore) {
                    index = j;
                }
            }
            ArrayList smallerNumber = (ArrayList) scores.get(index);
            scores.remove(index);
            scores.add(index, (ArrayList) scores.get(i));
            scores.remove(i);
            scores.add(i, smallerNumber);
        }
        return scores;
    }

    public void saveToFile(String fName) {
        System.err.println("Saving...");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fName), "UTF-8"))) {
            writer.write(player1.toString() + "\n");
            writer.write(player2.toString() + "\n");
            if (p1Turn) {
                writer.write("1");
            } else {
                writer.write("0");
            }
        } catch (IOException ex) {

        }
    }

    public void initFromFile(File fName) {
        System.err.println("Loading...");
        String p1Board = "";
        int p1Shots = 0;
        String p2Board = "";
        int p2Shots = 0;
        String playerTurn = "";
        try {
            Scanner scan = new Scanner(fName);
            p1Board = scan.useDelimiter("\n").next();
            p1Shots = Integer.parseInt(scan.useDelimiter("\n").next());
            p2Board = scan.useDelimiter("\n").next();
            p2Shots = Integer.parseInt(scan.useDelimiter("\n").next());
            playerTurn = scan.useDelimiter("\n").next();
        } catch (IOException e) {

        }

        player1.setBoard(p1Board);
        player2.setBoard(p2Board);
        player1.setShotsTaken(p1Shots);
        player2.setShotsTaken(p2Shots);

        if (playerTurn == "1") {
            p1Turn = true;
            JOptionPane.showMessageDialog(null, "Player 1 may now fire!", "Successful Load", 1);
        } else {
            p1Turn = false;
            JOptionPane.showMessageDialog(null, "Player 2 may now fire!", "Successful Load", 1);
        }
    }

    public void deployShip(int row, int col, boolean vert) {
        if (p1Turn) {
            if (shipsDeployed < shipMax) {
                if (deploymentOnBoard(row, col, player1.getShipHP(), vert)) {
                    if (player1.deployShips(row, col, vert)) {
                        shipsDeployed = player1.getShipsDeployed() + player2.getShipsDeployed();
                        System.err.println("Player one ship deployed at: " + row + ", " + col);
                    } else {
                        JOptionPane.showMessageDialog(null, "A ship is already there!", "Deployment Error", 0);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "That ship is out of bounds!", "Deployment Error", 0);
                }
            }
            if (shipsDeployed == shipMax) {
                p1Turn = false;
                System.err.println("Player two may deploy ships!");
            }
        } else {
            if (shipsDeployed < shipMax * 2) {
                if (deploymentOnBoard(row, col, player2.getShipHP(), vert)) {
                    if (player2.deployShips(row, col, vert)) {
                        shipsDeployed = player1.getShipsDeployed() + player2.getShipsDeployed();
                        System.err.println("Player one ship deployed at: " + row + ", " + col);
                    } else {
                        JOptionPane.showMessageDialog(null, "A ship is already there!", "Deployment Error", 0);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "That ship is out of bounds!", "Deployment Error", 0);
                }
            }
            if (shipsDeployed == shipMax * 2) {
                deployPhase = false;
                p1Turn = true;
            }
        }
    }

    public boolean deploymentOnBoard(int row, int col, int shipLen, boolean vert) {
        int two = 2;
        int three = 3;
        int four = 4;
        int five = 5;
        if (vert) {
            if (shipLen == two) {
                if ((row + 1) > player1.getBoardLen() - 1) {
                    return false;
                }
            } else if (shipLen == three) {
                if ((row - 1) < 0 || (row + 1) > player1.getBoardLen() - 1) {
                    return false;
                }
            } else if (shipLen == four) {
                if ((row - 1) < 0 || (row + two) > player1.getBoardLen() - 1) {
                    return false;
                }
            } else if (shipLen == five) {
                if ((row - two) < 0 || (row + two) > player1.getBoardLen() - 1) {
                    return false;
                }
            }
        } else {
            if (shipLen == two) {
                if ((col + 1) > player1.getBoardLen() - 1) {
                    return false;
                }
            } else if (shipLen == three) {
                if ((col - 1) < 0 || (col + 1) > player1.getBoardLen() - 1) {
                    return false;
                }
            } else if (shipLen == four) {
                if ((col - 1) < 0 || (col + two) > player1.getBoardLen() - 1) {
                    return false;
                }
            } else if (shipLen == five) {
                if ((col - two) < 0 || (col + two) > player1.getBoardLen() - 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void fireRound(int row, int col) {
        if (p1Turn) {
            if (player2.checkHit(row, col)) {
                if (checkWinner()) {
                    JOptionPane.showMessageDialog(null, "Player 2 may now fire!", "Battleship Deployment Phase", 1);
                    p1Turn = false;
                } else {
                    String name = JOptionPane.showInputDialog(null, "Enter your name", "Scoreboard", 1);
                    saveHighScore(name, player1.getShotsTaken());
                }
            }
        } else {
            if (player1.checkHit(row, col)) {
                if (checkWinner()) {
                    JOptionPane.showMessageDialog(null, "Player 1 may now fire!", "Battleship Deployment Phase", 1);
                    p1Turn = true;
                } else {
                    String name = JOptionPane.showInputDialog(null, "Enter your name", "Scoreboard", 1);
                    saveHighScore(name, player2.getShotsTaken());
                }
            }
        }
    }

    public boolean checkWinner() {
        if (p1Turn) {
            if (findShipCount(player2) == 0) {
                JOptionPane.showMessageDialog(null,
                        "Player 1 has won! You fired " + player1.getShotsTaken() + " rounds!",
                        "Battleship Deployment Phase", 1);
                gameOver = true;
                return !gameOver;
            }
        } else {
            if (findShipCount(player1) == 0) {
                JOptionPane.showMessageDialog(null,
                        "Player 2 has won! You fired " + player2.getShotsTaken() + " rounds!",
                        "Battleship Deployment Phase", 1);
                gameOver = true;
                return !gameOver;
            }
        }
        return !gameOver;
    }

    public int findShipCount(BSPlayer player) {
        return player.countShips();
    }

    public String getTile(int row, int col, boolean player) {
        if (player) {
            return player1.getTile(row, col);
        } else {
            return player2.getTile(row, col);
        }
    }

    public void setDeploy(boolean deploy) {
        deployPhase = deploy;
    }

    public void setShipsDeployed(int shipsDeployed) {
        this.shipsDeployed = shipsDeployed * 2;
        player1.setShipsDeployed(shipsDeployed);
        player2.setShipsDeployed(shipsDeployed);
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

    public int getShipsDeployed() {
        return shipsDeployed;
    }

    public int getShipMax() {
        return shipMax;
    }

    public boolean getGameOver() {
        return gameOver;
    }
}
