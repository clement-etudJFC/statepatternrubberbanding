package rubberbanding;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

public class RubberBandingPanel
        extends javax.swing.JPanel {

    // Les variables globales du StateChart
    private Point myFirst, myLast;

    // State Class : ici la classe State reçoit les mêmes événements qu'un MouseInputAdapter
    // C'est à dire ceux de MouseListener plus ceux de MouseMotionListener
    private class AbstractState extends MouseInputAdapter {
    };

    // State Objects
    // 1) Pour chaque état on crée une classe interne anonyme qui étend State
    // 2) On surcharge la ou les méthodes qu'on veut traiter
    //            (ici mousePressed )
    // 3) On crée une instance de cette classe anonyme
    private AbstractState Idle = new AbstractState() {
        @Override
        public void mousePressed(MouseEvent e) {
            initLine(e);
            currentState = Drawing;
        }
    };
    private AbstractState Drawing = new AbstractState() {
        @Override
        public void mouseReleased(MouseEvent e) {
            saveLine();
            currentState = Idle;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            moveLastPoint(e);
        }
    };

    // State variable
    private AbstractState currentState = Idle;

    // On délègue la gestion des événements à l'état courant
    MouseInputAdapter eventHandler = new MouseInputAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            currentState.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            currentState.mouseReleased(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            currentState.mouseDragged(e);
        }
    };

    public RubberBandingPanel() {
        // On enregistre le listener pour traiter les événements
        addMouseListener(eventHandler);
        addMouseMotionListener(eventHandler);
        setPreferredSize(new Dimension(200, 200));
    }

    private void saveLine() { // Rien pour le moment 
        // TODO : mémoriser les lignes pour pouvoir les redessiner
    }

    // Initialiser le trait
    // Les deux points sont au même endroit
    private void initLine(MouseEvent e) {
        myFirst = e.getPoint();
        myLast = myFirst;
    }

    // Déplacer le dernier point de la ligne
    private void moveLastPoint(MouseEvent e) {
        Graphics g = this.getGraphics();
        g.setXORMode(getBackground());
        // On efface le trait précédent
        g.drawLine(
                myFirst.x,
                myFirst.y,
                myLast.x,
                myLast.y
        );

        myLast = e.getPoint();
        // On trace le nouveau trait
        g.drawLine(
                myFirst.x,
                myFirst.y,
                myLast.x,
                myLast.y
        );
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawString("Repaint me !", 50, 50);
        // TODO : Redessiner les lignes
    }

}
