package vn.edu.hcmuaf.fit.UI;

import javax.swing.*;
import java.awt.*;

public class SymmetricPanel extends JPanel {

    public SymmetricPanel(){
        JLabel l = new JLabel();
        l.setText("Symmetric Panel");
        this.add(l);
        this.setBackground(new Color(227, 227, 227));
        this.setPreferredSize(new Dimension(1050, 700));
    }
}
