import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BSMenu extends JMenuBar{
    JMenu menu = new JMenu("File");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem load = new JMenuItem("Load");
    JFileChooser fileChooser = new JFileChooser();
    File selectedFile;

    public BSMenu(BSWindow window){
        menu.add(load);
        menu.add(save);
        this.add(menu);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (window.game.isDeploy()){
                    JOptionPane.showMessageDialog(null, "All player must deploy ships before saving.", "Save Error",0);
                } else {
                    window.game.saveToFile("save.txt");
                }
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //choose file
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(window);
                if(result == JFileChooser.APPROVE_OPTION){
                    int shipMax = 6;
                    window.display.setBlankDraw(true);
                    window.repaint();
                    if (window.game.isDeploy()){
                        window.game.setDeploy(false);
                        window.game.setShipsDeploy(shipMax);
                    }
                    selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    window.game.initFromFile(selectedFile);
                    window.repaint();
                } else {
                    JOptionPane.showMessageDialog(window, "No file selected", "Error", 1);
                }



            }
        });
    }



}
