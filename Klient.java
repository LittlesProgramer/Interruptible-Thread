import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Klient{
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Rameczka r = new Rameczka();
                r.setSize(new Dimension(300,300));
                r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                r.setVisible(true);
            }
        });
    }
}

class Rameczka extends JFrame{
    public Rameczka() {
        this.add(new Rysuj());
        pack();
    }
}

class Rysuj extends JButton{

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        //rectangle
        Rectangle2D rectangle2D = new Rectangle2D.Double(10,10,10,10);
        g2.draw(rectangle2D);
        //circle
        Ellipse2D circle = new Ellipse2D.Double(100,100,100,10);
        g2.draw(circle);
    }
}



