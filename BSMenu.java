
/* 
* Author: Austin Kibler, Samir Lamichhane, Christian Reynolds
* Purpose: This class is responsible for the menu, containing the save and load buttons as well as highscores and new game
* Date: 11/28/2018
*/

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

public class BSMenu extends JMenuBar {
    JMenu menu = new JMenu("File");
    JMenu game = new JMenu("Game");
    JMenuItem newGame = new JMenuItem("New Game");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem load = new JMenuItem("Load");
    JMenuItem qload = new JMenuItem("Load Last");
    JMenuItem records = new JMenuItem("Records");
    JFileChooser fileChooser = new JFileChooser();
    File selectedFile;

    public BSMenu(BSWindow window) {
        menu.add(newGame);
        menu.add(save);
        menu.add(load);
        menu.add(qload);
        this.add(menu);

        game.add(records);
        this.add(game);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (window.display.game.isDeploy()) {
                    JOptionPane.showMessageDialog(null, "All player must deploy ships before saving.", "Save Error", 0);
                } else {
                    window.display.game.saveToFile("save.txt");
                }
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // choose file
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(window);
                if (result == JFileChooser.APPROVE_OPTION) {
                    window.display.setBlankDraw(true);
                    window.repaint();
                    if (window.display.game.isDeploy()) {
                        window.display.game.setDeploy(false);
                        window.display.setP1FirstFire(false);
                        window.display.game.setShipsDeployed(window.display.game.getShipMax());
                    }
                    selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    window.display.game.initFromFile(selectedFile);
                    window.repaint();
                } else {
                    JOptionPane.showMessageDialog(window, "No file selected.", "Error", 1);
                }
            }
        });

        qload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showOptionDialog(null, "Are you sure you would like load a game?", "Records",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (choice == JOptionPane.YES_OPTION) {
                    File fName = new File("save.txt");
                    if (fName.exists()) {
                        window.display.setBlankDraw(true);
                        window.repaint();
                        if (window.display.game.isDeploy()) {
                            window.display.game.setDeploy(false);
                            window.display.setP1FirstFire(false);
                            window.display.game.setShipsDeployed(window.display.game.getShipMax());
                        }
                        window.display.game.initFromFile(fName);
                        window.repaint();
                    } else {
                        JOptionPane.showMessageDialog(window, "A previous save does not exist.", "Error", 0);
                    }
                }
            }
        });

        records.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                window.display.showHighScores();
            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                int choice = JOptionPane.showOptionDialog(null, "Are you sure you would like to restart?", "Records",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (choice == JOptionPane.YES_OPTION) {
                    window.display.newGame();
                }
            }
        });
    }
}
