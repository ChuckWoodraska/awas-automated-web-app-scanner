package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import testtree.TestCommands;
import testtree.TestPanel;
import testtree.TestPanelInterface;

public class WebTester extends JFrame implements Serializable, TestPanelInterface, ActionListener {
 	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar = new JMenuBar();
	private	JMenu fileMenu, recentProjectsMenu, editMenu, runMenu, helpMenu; 
//	private JMenuItem openItem, newItem; 

	private JToolBar toolBar;
    private TestPanel testPanel;
    
    private ArrayList<String> recentProjects;
    
    public WebTester(String title) {
        super(title);
        initProjects();
        createTestPanel();
        createMenuBar();
        createToolBar();
        setContentPane(makeContentPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initProjects(){
    	recentProjects = new ArrayList<String>();
    	recentProjects.add("Project 1");
    	createRecentProjectsMenu();
    }
    
	private JMenuItem createMenuItem(JMenu menu, String title, String command, ActionListener listener){
		JMenuItem menuItem = menu.add(title);
		menuItem.setActionCommand(command);
		menuItem.setName(command);		// use the command to name the menu item
		menuItem.addActionListener(listener);
		return menuItem;
	}
	
	private void createRecentProjectsMenu(){
		if (recentProjects.size()>0) {
			recentProjectsMenu = new JMenu("Recent Projects");
			for (int projectIndex=0; projectIndex < recentProjects.size(); projectIndex++){
				createMenuItem(recentProjectsMenu, recentProjects.get(projectIndex), projectIndex+"", this);			
			}
		}
	}
	
	private void createFileMenu() {
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		createMenuItem(fileMenu, "Open...", MenuCommands.OPEN_COMMAND, this);
		createMenuItem(fileMenu, "New", MenuCommands.NEW_COMMAND, this);
		createMenuItem(fileMenu, "Save", MenuCommands.SAVE_COMMAND, this);
		createMenuItem(fileMenu, "Save As...", MenuCommands.SAVEAS_COMMAND, this);
		createMenuItem(fileMenu, "Switch Workspace...", MenuCommands.SWITCH_WORKSPACE_COMMAND, this);
		createMenuItem(fileMenu, "Exit", MenuCommands.EXIT_COMMAND, this);
		if (recentProjectsMenu!=null){
			fileMenu.addSeparator();
			fileMenu.add(recentProjectsMenu);
		}
	}

	private void createTestMenu() {
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic('T');
		createMenuItem(editMenu, "Navigate", TestCommands.NAVIGATE_COMMAND, testPanel);
		createMenuItem(editMenu, "Navigate in New Tab", TestCommands.NAVIGATE_NEWTAB_COMMAND, testPanel);

		createMenuItem(editMenu, "New Subtree Tab", TestCommands.NEW_SUBTREE_COMMAND, testPanel);
		createMenuItem(editMenu, "Close Subtree Tabs", TestCommands.CLOSE_SUBTREE_COMMAND, testPanel);
		createMenuItem(editMenu, "View History", TestCommands.VIEW_HISTORY_COMMAND, testPanel);
		editMenu.addSeparator();
		createMenuItem(editMenu, "Add Node...", TestCommands.ADD_NODE_COMMAND, testPanel);
		createMenuItem(editMenu, "Delete Node", TestCommands.DELETE_NODE_COMMAND, testPanel);
		editMenu.addSeparator();
		createMenuItem(editMenu, "Preferences...", TestCommands.PREFERENCES_COMMAND, testPanel);
	}

	private void createRunMenu() {
		runMenu = new JMenu("Run");
		runMenu.setMnemonic('R');
		createMenuItem(runMenu, "Replay All Tests", MenuCommands.RUN_ALLTESTS_COMMAND, this);
		createMenuItem(runMenu, "Replay From Selected Node", MenuCommands.RUN_SELECTEDTESTS_COMMAND, this);
	}

	private void createHelpMenu() {
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		createMenuItem(helpMenu, "About WebTester", MenuCommands.ABOUT_COMMAND, this);
	}
	
	private void createMenuBar() {
		createFileMenu();
		menuBar.add(fileMenu);
//		fileMenu.setBorder(BorderFactory.createEmptyBorder(1,3,1,3));
		createTestMenu();
		menuBar.add(editMenu);
		createRunMenu();
		menuBar.add(runMenu);
		createHelpMenu();
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}

	private JTextField urlField;
	
    private void createToolBar() {
        toolBar = new JToolBar("Draggable");
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        JButton button = null;
        button = makeToolBarButton("new.gif", MenuCommands.NEW_COMMAND,
                "Create a new Project",
                "New");
        toolBar.add(button);
        button = makeToolBarButton("open.gif", MenuCommands.OPEN_COMMAND,
                                      "Open a project",
                                      "Open");
        toolBar.add(button);
        urlField = new JTextField();
        urlField.setColumns(80);
        urlField.setEditable(false);
        urlField.addActionListener(this);
//        urlField.setActionCommand(TEXT_ENTERED);
        toolBar.add(urlField);
    }

 	@Override
	public void updateUrlField(String urlString){
		if (urlField!=null)
			urlField.setText(urlString);
 	
 	}

	protected JButton makeToolBarButton(String imageName,
            String actionCommand,
            String toolTipText,
            String altText) {
    	JButton button = new JButton();
    	button.setActionCommand(actionCommand);
    	button.setToolTipText(toolTipText);
    	button.addActionListener(this);
    	ImageIcon imageIcon = createImageIcon(imageName, altText);
    	if (imageIcon != null) {                      //image found
    		button.setIcon(imageIcon);
    	} else {                                     //no image found
    		button.setText(altText);
    		System.err.println("Resource not found: " + imageName);
    	}
    	return button;
	}

	public static ImageIcon createImageIcon(String imageName, String altText){
    	URL imageURL = WebTester.class.getResource(imageName);
    	return imageURL != null? new ImageIcon(imageURL, altText): null;
	}
	
	private void createTestPanel(){
        testPanel = new TestPanel(this);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		testPanel.setPreferredSize(new Dimension((int)screenSize.getWidth()-10, (int)screenSize.getHeight()-110));   
        testPanel.setOpaque(true);
	}
	
	private JPanel makeContentPanel(){
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(toolBar, BorderLayout.PAGE_START);
		contentPanel.add(testPanel, BorderLayout.CENTER);
		return contentPanel;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (MenuCommands.OPEN_COMMAND.equals(cmd)) {
        	testPanel.loadTree();

        

        } 
        else if (MenuCommands.NEW_COMMAND.equals(cmd)) { 
        	testPanel.removeAllTestTrees();        	        
        	//testPanel.navigate(null, "http://www.google.com/", false);
        	//testPanel.navigate(null, "http://localhost:61730/AWASSandbox/Default.aspx", false);
        	//testPanel.navigate(null, "http://demo.magentocommerce.com/", false);
        	testPanel.navigate(null, "http://24.111.169.202", false);
        	//testPanel.navigate(null, "http://www.amazon.com", false);
        
         
//        testPanel.createSampleTree();

        } 
        else if (MenuCommands.SAVE_COMMAND.equals(cmd)) { 
        	testPanel.saveTree();


        }
        else {
        	try {
        		int projectIndex = Integer.parseInt(cmd);
        		if (projectIndex>=0 && projectIndex<recentProjects.size()){
        			// open project 
        		}
        	} catch (Exception ex) {
        		
        	}
        }
	}
	
    private static void createAndShowGUI() {
        JFrame frame = new WebTester("WebTester");
        frame.pack();
        frame.setVisible(true);
    }

	public static void setLookAndFeel() {
		if (System.getProperty("os.name").contains("Windows")) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch(Exception e) {
			}
		}
	}
    
    public static void main(String[] args) {
    	setLookAndFeel();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
			public void run() {
                createAndShowGUI();
            }
        });
    }
}
