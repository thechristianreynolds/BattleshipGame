import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

public class BSMenu extends JMenuBar {
    JMenu menu = new JMenu("File");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem load = new JMenuItem("Load");
    JMenuItem qload = new JMenuItem("Load Last");
    JMenuItem records = new JMenuItem("Records");
    JFileChooser fileChooser = new JFileChooser();
    File selectedFile;

    public BSMenu(BSWindow window) {
        menu.add(load);
        menu.add(qload);
        menu.add(save);
        menu.add(records);
        this.add(menu);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (window.game.isDeploy()) {
                    JOptionPane.showMessageDialog(null, "All player must deploy ships before saving.", "Save Error", 0);
                } else {
                    window.game.saveToFile("save.txt");
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
                    if (window.game.isDeploy()) {
                        window.game.setDeploy(false);
                        window.display.setP1FirstFire(false);
                        window.game.setShipsDeployed(window.game.getShipMax());
                    }
                    selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    window.game.initFromFile(selectedFile);
                    window.repaint();
                } else {
                    JOptionPane.showMessageDialog(window, "No file selected.", "Error", 1);
                }
            }
        });

        qload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fName = new File("save.txt");
                if (fName.exists()) {
                    window.display.setBlankDraw(true);
                    window.repaint();
                    if (window.game.isDeploy()) {
                        window.game.setDeploy(false);
                        window.display.setP1FirstFire(false);
                        window.game.setShipsDeployed(window.game.getShipMax());
                    }
                    window.game.initFromFile(fName);
                    window.repaint();
                } else {
                    JOptionPane.showMessageDialog(window, "A previous save does not exist.", "Error", 0);
                }
            }
        });

        records.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    File records = new File("records.txt");
                    Scanner scanner = new Scanner(records);
                    String message = "High Scores\n";
                    if (records.exists()) {
                        while (scanner.hasNext()) {
                            message += scanner.nextLine() + "\n";
                        }
                        JOptionPane.showMessageDialog(null, message, "Records", 1);
                    }
                } catch (Exception e) {
                    System.err.println("Issue loading records");
                }
            }
        });
    }
}
