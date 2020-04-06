package ce326.hw3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class EventHandling extends JFrame {
    JFrame frame;
    JPanel framePanel;
    JPanel textFieldPanel;
    JButton button;
    JTextField actionEventTextField;
    JTextField keyEventTextField;
    JTextField mouseMotionEventTextField;
    int buttonClickedCount;
    private static int TEXT_FIELD_SIZE = 20;

    public EventHandling() {
        frame = new JFrame("CE325 - Event Handling Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button = new JButton("  Put Cursor!  ");
        button.setBackground(new Color(0xe9,0xe9,0xe9));
        button.setFont(new Font("Courier", Font.BOLD, 15));
        Border buttonBorder = BorderFactory.createRaisedBevelBorder();
        button.setBorder(buttonBorder);

        buttonClickedCount = 0;
        button.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonClickedCount++;
                actionEventTextField.setText("Button clicked "+buttonClickedCount+" times!");
            }
        });

        button.addMouseListener( new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
                button.setText("  Press  me!   ");
            }
            public void mouseExited(MouseEvent e) {
                button.setText("  Put Cursor!  ");
            }
            public void mousePressed(MouseEvent e) {
                button.setText("  Release Me!  ");
            }
            public void mouseReleased(MouseEvent e) {
                button.setText("  Press again  ");
            }
        });

        frame.add(button, BorderLayout.EAST);

        textFieldPanel = new JPanel(new GridLayout(5, 2, 3, 3));

        textFieldPanel.add(new JLabel("Button Clicks:"));
        actionEventTextField = new JTextField("", TEXT_FIELD_SIZE);
        textFieldPanel.add(actionEventTextField);

        textFieldPanel.add(new JLabel("Keyboard Input:"));
        keyEventTextField = new JTextField("", TEXT_FIELD_SIZE);
        keyEventTextField.addKeyListener( new KeyListener() {
            public void keyTyped(KeyEvent e) {
                keyEventTextField.setText("Character \""+e.getKeyChar()+"\" was pressed");
            }
            public void keyPressed(KeyEvent e) {
                keyEventTextField.setText("");
            }
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SHIFT ||
                        e.getKeyCode() == KeyEvent.VK_CONTROL ||
                        e.getKeyCode() == KeyEvent.VK_ENTER
                )
                    return;
                String str = keyEventTextField.getText();
                str = str.substring(0,str.length()-1);
                keyEventTextField.setText(str);
            }
        });
        textFieldPanel.add(keyEventTextField);

        textFieldPanel.add(new JLabel("Mouse current position on window:"));
        mouseMotionEventTextField = new JTextField("", TEXT_FIELD_SIZE);
        frame.addMouseMotionListener( new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
            }
            public void mouseMoved(MouseEvent e) {
                mouseMotionEventTextField.setText("Cursor position at "+e.getX()+", "+e.getY());
            }
        });
        textFieldPanel.add(mouseMotionEventTextField);

        frame.add(textFieldPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String []args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EventHandling();
            }
        });
    }
}