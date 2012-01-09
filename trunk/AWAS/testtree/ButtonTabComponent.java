package testtree;
import gui.WebTester;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.*;
import java.awt.event.*;

public class ButtonTabComponent extends JPanel {
 	private static final long serialVersionUID = 1L;
 
    public ButtonTabComponent(String title, String iconPath, boolean allowToClose, final TestPanel testPanel) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);
        JLabel label = new JLabel(title, WebTester.createImageIcon(iconPath, ""), SwingConstants.LEFT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(label);
        if (allowToClose){
            JButton button = new TabButton(testPanel);
        	add(button);
        }
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    /*
    private ImageIcon scale(Image src, double scale) {
        int w = (int)(scale*src.getWidth(this));
        int h = (int)(scale*src.getHeight(this));
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage dst = new BufferedImage(w, h, type);
        Graphics2D g2 = dst.createGraphics();
        g2.drawImage(src, 0, 0, w, h, this);
        g2.dispose();
        return new ImageIcon(dst);
    }
*/
    private class TabButton extends JButton implements ActionListener {
 		private static final long serialVersionUID = 1L;

 		private TestPanel testPanel;
 		
		public TabButton(TestPanel testPanel) {
			this.testPanel = testPanel;
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close tab");
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
        	testPanel.removeTestTree(ButtonTabComponent.this);
        }

        //don't want to update UI for this button
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(0.7f, 0.0f, 0.0f));
//            g2.setColor(Color.BLACK);
           if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}

