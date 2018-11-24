import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BSMenu extends JMenuBar{
    JMenu menu = new JMenu("File");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem load = new JMenuItem("Load");

    public BSMenu(BSWindow window){
        menu.add(load);
        menu.add(save);
        this.add(menu);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.game.saveToFile("save.txt");
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.game.initFromFile("save.txt");
            }
        });
    }



}
