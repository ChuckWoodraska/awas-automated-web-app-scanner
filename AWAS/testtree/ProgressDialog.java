package testtree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel progressLabel;
	private Thread workingThread = null;
	
	public ProgressDialog(Frame frame, String title, String message) {
		super(frame, title, false);
		
		progressLabel = new JLabel(message);
        progressLabel.setPreferredSize(new Dimension(350,30));
		
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(false);
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(300,20));
		progressBar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BorderLayout());
		progressPanel.add(progressLabel, BorderLayout.CENTER);
        progressPanel.add(progressBar, BorderLayout.SOUTH);
		progressPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		setContentPane(progressPanel);
		
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	if (workingThread!=null)
	        		workingThread.stop();
	        }
	      });
	    
		setFocusable(true);
		setAlwaysOnTop(true);
		pack();
		setVisible(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int ly=((int)screenSize.getHeight()-(int)getSize().getHeight() -30)/2;
		int lx=((int)screenSize.getWidth()-(int)getSize().getWidth())/2;
		setLocation(lx, ly);

	}

	public void setWorkingThread(final Thread thread) {
		workingThread = thread;
	}
	
	public void setMessage(String message) {
		progressLabel.setText(message);
	}
	
	public void done(String message){
		setVisible(false);
        setModal(true);
        
        progressLabel = new JLabel(message, JLabel.CENTER);

        JButton confirm = new JButton("OK");
        confirm.setActionCommand("OK");
        confirm.addActionListener(this);
        
		JPanel confirmPanel = new JPanel();
		confirmPanel.add(confirm);
        confirmPanel.setPreferredSize(new Dimension(300,40));
		confirmPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BorderLayout());
		progressPanel.add(progressLabel, BorderLayout.CENTER);
        progressPanel.add(confirmPanel, BorderLayout.SOUTH);
		progressPanel.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));

        setContentPane(progressPanel);
		setVisible(true);
        pack();
	}
	
	public void actionPerformed(ActionEvent e) {
		if ("OK".equals(e.getActionCommand())) {
			dispose();
		}
	}
}
