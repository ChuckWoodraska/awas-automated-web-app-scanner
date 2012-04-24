package testtree;

import input.AutoFormNavigator;
import input.MyFirefoxDriver;
import input.MyHashTable;
import input.UserInputGUI;
import input.UserInputReader;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class TestPanel extends JPanel implements Serializable, TreeSelectionListener, TreeModelListener, ActionListener {
 	private static final long serialVersionUID = 1L;
 	
 	public static boolean hasBuiltInBrowser = false;
 	
    protected Hashtable links = new MyHashTable();
    private WebDriver driver = new MyFirefoxDriver();

// 	private static int MAX_LINKS_PER_PAGE = 20;

 	protected TestPanelInterface testPanelInterface;
 	
    protected ArrayList<JTree> testTrees;
	protected JTabbedPane tabbedPane;
   
    protected JTextArea traceArea;
    protected JTextArea webPageArea;
    
    public TestPanel(TestPanelInterface testPanelInterface) {
        super(new GridLayout(1,0));
        this.testPanelInterface = testPanelInterface;
        initializeWebPageArea();
        initializeTraceArea();
        initializeTestTrees();
        createLayout();
    }
    
    public void navigate(TestNode testNode, String urlString, boolean newTab){
		ProgressDialog progressFrame = new ProgressDialog(null, "Navigating", urlString+"...");
		Thread codeGenerationThread = new Thread(new NavigationThread(progressFrame, testNode, urlString, newTab));
		progressFrame.setWorkingThread(codeGenerationThread);
		codeGenerationThread.start();
    }

	class NavigationThread implements Runnable {
		private ProgressDialog progressDialog;
		private TestNode testNode;
		private String urlString;
		private boolean newTab;
		NavigationThread(ProgressDialog progressDialog, TestNode testNode, String urlString, boolean newTab) {
			this.progressDialog = progressDialog;
			this.testNode = testNode;
			this.urlString = urlString;
			this.newTab = newTab;
		}
		
		@Override
		public void run () {
	    	System.out.println("URL: "+urlString);
	    	testPanelInterface.updateUrlField(urlString);
	    	try {
		        driver.get(urlString);
/*		        for (WebElement element:       driver.findElements(By.tagName("a"))){
		        	System.out.println("text: "+element.getText());
		        	System.out.println("id: "+element.getAttribute("id"));
		        	System.out.println("class: "+element.getAttribute("class"));
		        	System.out.println("href: "+element.getAttribute("href"));
		        }	       
*/		         
	    	    TestNode currentRootNode = testNode==null? new TestNode(driver.getTitle()): testNode;
	    	    String parentID = testNode==null? "": testNode.getID()+".";
	    	    
	    	    navigateHyperLinks(currentRootNode, parentID);
	    	    navigateForms(currentRootNode, parentID);
	    	    presentTestTree(currentRootNode, testNode==null || newTab);
	    	    
	    	} catch (Exception ex){
		    	System.out.println(ex.toString());
	    	}
			progressDialog.dispose();
		}
	}

	private void navigateHyperLinks(TestNode currentRootNode, String parentID){
	    int index = currentRootNode.getChildCount()+1;
	    int count = 0;
	    System.out.println("Number of anchors: "+driver.findElements(By.tagName("a")).size());
	    for (WebElement element: driver.findElements(By.tagName("a"))){
	    	
	     	String hrefAttributeString = element.getAttribute("href");
	     	 System.out.println("After href"+count);
	    	if (isHyperLink(hrefAttributeString)){
	    	    String existingLink = (String)links.get(hrefAttributeString);
	    	    System.out.println("if");

	    	    if (existingLink==null || !existingLink.equalsIgnoreCase(hrefAttributeString)){
	    	        currentRootNode.add(new TestNode(element.getText(), hrefAttributeString, parentID+index, false));
	    	        index++;
	    	        links.put(hrefAttributeString, hrefAttributeString);
	    		    System.out.println("2nd if");

	    	    }
	    	}	
	    	count++;
	    }
	    System.out.println("Where are we");

	}
	
	private void navigateForms(TestNode currentRootNode, String parentID){
	    int index = currentRootNode.getChildCount()+1;
	    System.out.println("Number of forms: "+driver.findElements(By.tagName("form")).size());
	    for (WebElement element: driver.findElements(By.tagName("form"))){
	    	        currentRootNode.add(new TestNode(driver.getTitle(), driver.getCurrentUrl(), index+" "+element.getAttribute("id"), element.getAttribute("id"), true));
	    	        index++;

	    }
	}
	
    private boolean isHyperLink(String hrefAttribute){
	   try{ 
    	return hrefAttribute.startsWith("http");
	   }
	   catch (NullPointerException e){
		   return false;
	   }
    }
    
    @SuppressWarnings("unchecked")
	private void presentTestTree(TestNode currentRootNode, boolean isNewTree){
    	
	    if (currentRootNode.getChildCount()>0){
	    	if (isNewTree)
	    		createTestTree(currentRootNode);
	    	else {
	    		JTree currentTestTree = getCurrentTestTree();
				for (Enumeration<TestNode> e = currentRootNode.children(); e.hasMoreElements();) {
					TestNode node = e.nextElement();
					TreePath path = new TreePath(node.getPath());
					currentTestTree.expandPath(path);
    	            currentTestTree.scrollPathToVisible(path);
				}	    		
	    	}
	    }
    }
    
    private void initializeWebPageArea(){
    	if (hasBuiltInBrowser){
    		webPageArea = new JTextArea();
    		webPageArea.setEditable(false);
        }
    }

    private void initializeTraceArea(){
        traceArea = new JTextArea();
        traceArea.setEditable(false);
    }
    
    private void createLayout(){
		tabbedPane = new JTabbedPane();
//		tabbedPane.setPreferredSize(new Dimension(400, 300));   
        JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, new JScrollPane(traceArea));
        leftPane.setResizeWeight(0.85);
//        leftPane.setOneTouchExpandable(true);
        leftPane.setDividerLocation(0.85);
        if (!hasBuiltInBrowser)
        	add(leftPane);
        else {
        	JSplitPane webTreePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, new JScrollPane(webPageArea));
//        webTreePane.setOneTouchExpandable(true);
        	webTreePane.setDividerLocation(0.55);
        	webTreePane.setResizeWeight(0.55);
        	add(webTreePane);
        }
    }
    
    /////////////////////////////////////////////////////////////	
    
    // start - test tree operations
    
    private void initializeTestTrees(){
        createPopupMenu();
        testTrees = new ArrayList<JTree>();
    } 
    
    public void setTestTrees(ArrayList<JTree> tree){
        createPopupMenu();
        testTrees = new ArrayList<JTree>(tree);
    } 
    
    public JTree getCurrentTestTree(){
    	return testTrees==null ||  testTrees.size()==0? null: 
    	testTrees.get(tabbedPane.getSelectedIndex());
    }

    public TestNode getTreeRoot(JTree testTree){
    	DefaultTreeModel treeModel = (DefaultTreeModel)testTree.getModel();
    	return  (TestNode)treeModel.getRoot();
    }
    
    public void removeTestTree(ButtonTabComponent buttonTabComponent){
    	int treeIndex = tabbedPane.indexOfTabComponent(buttonTabComponent);
    	if (treeIndex>0){
			tabbedPane.remove(treeIndex);
			testTrees.remove(treeIndex);
			tabbedPane.setSelectedIndex(treeIndex-1);
    	}
    }

    public void removeAllTestTrees(){
    	testTrees.clear();
    	tabbedPane.removeAll();
    	links.clear();
    }
    
    private void createTestTree(TestNode rootNode){
    	//System.out.println(rootNode.getID());
    	DefaultTreeModel currentTreeModel = new DefaultTreeModel(rootNode);
        currentTreeModel.addTreeModelListener(this);
        JTree currentTestTree = new JTree(currentTreeModel);        
    	testTrees.add(currentTestTree);
    	System.out.println(rootNode.toString());
		tabbedPane.addTab(rootNode.toString(), new JScrollPane(currentTestTree));
		tabbedPane.setSelectedIndex(testTrees.size()-1);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, new ButtonTabComponent(rootNode.toString(), "tree.png", tabbedPane.getTabCount()>1, this));
		currentTestTree.addTreeSelectionListener(this);
        currentTestTree.setEditable(false);
    	currentTestTree.setRootVisible(false);
        currentTestTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        currentTestTree.setShowsRootHandles(true);
		currentTestTree.addMouseListener( new MouseAdapter() { 
			@Override
			public void mousePressed(MouseEvent e ) { 
				checkForTriggerEvent(e); 
			} 
			@Override
			public void mouseReleased(MouseEvent e ) { 
				checkForTriggerEvent(e); 
			}
			private void checkForTriggerEvent(MouseEvent e ) { 
				if (e.isPopupTrigger()) { 
					popupMenu.show( e.getComponent(), e.getX(), e.getY() );
				}
			}
		});
        expandAllPaths(currentTestTree, true);
    }
    
	public void expandAllPaths(JTree testTree, boolean expand) {
		TreeNode root = (TreeNode) testTree.getModel().getRoot();
		expandAllPaths(testTree, new TreePath(root), expand);
	}

	@SuppressWarnings("unchecked")
	public void expandAllPaths(JTree testTree, TreePath parent, boolean expand) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAllPaths(testTree, path, expand);
			}
		}
		// Expansion or collapse must be done bottom-up
		if (expand) {
			testTree.expandPath(parent);
		} else {
			testTree.collapsePath(parent);
		}
	}

	/*
	public void clearTestTree(JTree testTree) {
    	DefaultTreeModel treeModel = (DefaultTreeModel)testTree.getModel();
    	DefaultMutableTreeNode testTreeRoot =  (DefaultMutableTreeNode)treeModel.getRoot();
        testTreeRoot.removeAllChildren();
        treeModel.reload();
	}
	*/
	
    public void removeSelectedNode(JTree testTree) {
        TreePath currentSelection = testTree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
            	DefaultTreeModel currentTreeModel = (DefaultTreeModel)testTree.getModel();
                currentTreeModel.removeNodeFromParent(currentNode);
                return;
            }
        } 
        // Either there was no selection, or the root was selected.
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.beep();
    }

    /** Add child to the selected node. */
    public TestNode addChildNode(JTree testTree, TestNode childNode) {
        TestNode parentNode = null;
        TreePath parentPath = testTree.getSelectionPath();
        if (parentPath == null) {
            parentNode = getTreeRoot(testTree);
        } else {
            parentNode = (TestNode)parentPath.getLastPathComponent();
        }
        return addChildNode(testTree, parentNode, childNode);
    }

    /** Add child to a parent node. */
    public TestNode addChildNode(JTree testTree, TestNode parent, TestNode childNode){
        if (parent == null) {
            parent = getTreeRoot(testTree);
        }
        DefaultTreeModel currentTreeModel = (DefaultTreeModel) testTree.getModel();
        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        currentTreeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        //Make sure the user can see the new node.
        testTree.scrollPathToVisible(new TreePath(childNode.getPath()));
        return childNode;
    }
    // end - test tree operations
    
    /////////////////////////////////////////////////////////////	

    // implements TreeSelectionListener
	@Override
	public void valueChanged(TreeSelectionEvent event) {
		JTree currentTestTree = getCurrentTestTree();
		if (event.getSource() == currentTestTree) {
			TestNode mutableNode = (TestNode)(currentTestTree.getLastSelectedPathComponent());
			if (mutableNode != null) {
				if (mutableNode.isLeaf()) {
					// handle leaf
				} else {
					if (!mutableNode.isRoot()){
						
					}
				}
				traceArea.append("\n"+mutableNode.getFullNodeInfo());
			}
		}
	}
	
    /////////////////////////////////////////////////////////////	
    // start - implement TreeModelListener
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)(node.getChildAt(index));

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }
	@Override
	public void treeNodesInserted(TreeModelEvent e) {
    }
    @Override
	public void treeNodesRemoved(TreeModelEvent e) {
    }
    @Override
	public void treeStructureChanged(TreeModelEvent e) {
    }
    // end - implement TreeModelListener

    /////////////////////////////////////////////////////////////	

    // start popup menu        
    protected JPopupMenu popupMenu; 
    protected JMenuItem nevigateItem, nevigateNewTabItem, editTableItem, newSubTreeItem, closeSubTreeItem, viewHistoryItem, 
    deleteNodeItem, addNodeItem, clearTreeItem,  replayDFItem,
    expandPathsItem, collapsePathsItem;

    private JMenuItem createPopupMenuItem(String title, String command){
    	JMenuItem menuItem = popupMenu.add(title);
    	menuItem.setActionCommand(command);
    	menuItem.addActionListener(this);
    	return menuItem;
    }

    private void createPopupMenu() {
    	popupMenu = new JPopupMenu();
    	nevigateItem = createPopupMenuItem("Navigate", TestCommands.NAVIGATE_COMMAND);
    	nevigateNewTabItem = createPopupMenuItem("Navigate in New Tab", TestCommands.NAVIGATE_NEWTAB_COMMAND);
    	replayDFItem = createPopupMenuItem("Replay Depth First", TestCommands.REPLAYDF_COMMAND);
		editTableItem = createPopupMenuItem("Edit Table Data", TestCommands.EDIT_TABLE_COMMAND);
    	newSubTreeItem = createPopupMenuItem("New Subtree Tab", TestCommands.NEW_SUBTREE_COMMAND);
    	closeSubTreeItem = createPopupMenuItem("Close All Subtree Tabs", TestCommands.CLOSE_SUBTREE_COMMAND);
    	viewHistoryItem = createPopupMenuItem("View History", TestCommands.VIEW_HISTORY_COMMAND);
    	popupMenu.addSeparator();
//    	expandPathsItem = createPopupMenuItem("Expand Paths", TestCommands.EXPAND_PATHS_COMMAND);
    	collapsePathsItem = createPopupMenuItem("Collapse Paths", TestCommands.COLLAPSE_PATHS_COMMAND);
    	popupMenu.addSeparator();
    	addNodeItem = createPopupMenuItem("Add Node...", TestCommands.ADD_NODE_COMMAND);
    	deleteNodeItem = createPopupMenuItem("Delete Node", TestCommands.DELETE_NODE_COMMAND);
    }

    public TestNode getSelectedTestNode(){
    	JTree currentTestTree = getCurrentTestTree();
        TreePath parentPath = currentTestTree.getSelectionPath();
        return (parentPath == null)? null: (TestNode)parentPath.getLastPathComponent();
    }
    public Stack stack = new Stack();//navigate(rootNode, rootNode.getURL() ,false );
    public void replayDepthFirst(TestNode currentNode)
    {
    	TestNode rootNode = currentNode;
    	//Stack stack = new Stack();//navigate(rootNode, rootNode.getURL() ,false );
    	if(rootNode.getChildCount() > 0 || rootNode.recordedInputs.size() > 0)
    	{
    		int size = 1;
    		if(rootNode.getChildCount() == 0)
    		{
    			size = 1;
    		}
    		else
    		{
    			size = rootNode.getChildCount();
    		}
    		replayNavigate(rootNode);
    		for(int i = 0; i < size; ++i)
    		{
    			try{
    				TestNode childNode = (TestNode) rootNode.getChildAt(i);
    			
    			if(childNode.getChildCount() > 0 || childNode.recordedInputs.size() > 0)
    			{
    				
    				stack.push(childNode);    				
    				//System.out.println(childNode.toString());
    				replayDepthFirst(childNode);
    				for(int j = 0; j < stack.size(); ++j)
    				{
    					System.out.println(stack.get(j));
    				}
    				TestNode temp = (TestNode) stack.pop();
    				//System.out.println(temp.getFullNodeInfo());
    				
    				

    			}
    			}
    			catch(ArrayIndexOutOfBoundsException ex){}
    		}
    		    		
    	}
    }
    
    public void saveTree(){
    	
    	JTree tree = getCurrentTestTree();
    	File outputFile = new File("./savedTree.xls");
    	try {
			TestTreeFile.saveTestDataToExcelFile(tree, outputFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	

    }
    
    public void loadTree(){
    	File inputFile = new File("./savedTree.xls");
    	try {
    		TestNode node = TestTreeFile.loadTestDataFromExcelFile(inputFile);
    		System.out.println("Outside Load "+node.getTitle()+node.getChildCount());
    		//Thread.sleep(1000);
    		//presentTestTree(node,false);
			createTestTree(TestTreeFile.loadTestDataFromExcelFile(inputFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		getCurrentTestTree().updateUI();
    }
    
    private void replayNavigate(TestNode currentNode)
    {
    	
    	driver.get(currentNode.getURL());
    	//currentNode.removeAllChildren();
		if(currentNode.isForm)
		{
			System.out.println("Start Form");
		}
		else if(currentNode.recordedInputs.size() > 0)
		{
			System.out.println("Start Recorded Inputs");
			TestNode parentNode = (TestNode) currentNode.getParent();
			parentNode.convertVectorsToInput(parentNode);
			AutoFormNavigator afi = new AutoFormNavigator(parentNode.getCombinationalInput(),parentNode.getUserInput(),parentNode.getUserInputInvalid(), parentNode.getValidTestOracle(),parentNode.getInvalidTestOracle());
			
			int[] combination = new int[currentNode.getRecordedInputs().get(0).getDataCombos().size()];
				for(int i = 0; i < combination.length; ++i)
				{
					//System.out.println("" + child.getFullNodeInfo());
					combination[i] = currentNode.getRecordedInputs().get(0).getDataCombos().get(i).intValue();
				}

			
			
			List<WebElement> allForms = driver.findElements(By.tagName("form"));
			
	        WebElement form = allForms.get(currentNode.getRecordedInputs().get(0).getFormIndex());
			List<Object> formInputs = afi.collectAllFormInputElements(form);
			afi.replayNavigate(currentNode.getURL(), formInputs, combination, currentNode.getRecordedInputs().get(0).getSubmitButtonID());
		
//			int index = 1;
//		    for (TestNode node : afi.testNodes){
//		    	node.setId(index);
//    	        currentNode.add(node);
//    	        //navigate each node
//    	        ++index;
//		    }
		    
		}
		else
		{
			System.out.println("Start Links");
			 int index = currentNode.getChildCount()+1;
			    System.out.println("Number of anchors: "+driver.findElements(By.tagName("a")).size());
			    for (WebElement element: driver.findElements(By.tagName("a"))){
			     	String hrefAttributeString = element.getAttribute("href");
			    	if (isHyperLink(hrefAttributeString)){
			    	    String existingLink = (String)links.get(hrefAttributeString);
			    	    if (existingLink==null || !existingLink.equalsIgnoreCase(hrefAttributeString)){
			    	        currentNode.add(new TestNode(element.getText(), hrefAttributeString, ""+index, false));
			    	        index++;
			    	        links.put(hrefAttributeString, hrefAttributeString);
			    	    }
			    	}	
			    }
		}
		    getCurrentTestTree().updateUI();
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
    	String command = e.getActionCommand();
    	if (command == TestCommands.NAVIGATE_COMMAND || command == TestCommands.NAVIGATE_NEWTAB_COMMAND) { 
    		TestNode currentNode = getSelectedTestNode();
    		currentNode.removeAllChildren();
    		if(currentNode.isForm())
    		{
    			currentNode.convertVectorsToInput(currentNode);
    			AutoFormNavigator afi = new AutoFormNavigator(currentNode.getCombinationalInput(),currentNode.getUserInput(),currentNode.getUserInputInvalid(), currentNode.getValidTestOracle(),currentNode.getInvalidTestOracle());
    			afi.navigatePage(currentNode.getURL());
    			int index = 1;
    		    for (TestNode node : afi.testNodes){
    		    	node.setId(index);
	    	        currentNode.add(node);
	    	        //navigate each node
	    	        ++index;
    		    }
    		    

    		    getCurrentTestTree().updateUI();
    		    
    		   
    		}
    		else
    		{    			    		
	            if (currentNode!=null){
	            	boolean newTab = (command == TestCommands.NAVIGATE_NEWTAB_COMMAND);
	            	navigate(currentNode, currentNode.getURL(), newTab);
	            }
    		}
    	} else if (command == TestCommands.EDIT_TABLE_COMMAND) { 
    		TestNode currentNode = getSelectedTestNode();
    		String formName = currentNode.getFormName();
    		String url = currentNode.getURL();
    		if(currentNode.noTable)
    		{
    			System.out.println(currentNode.getTitle());
	    		//String xlsPath = "C:\\Users\\Chuck\\Desktop\\job\\Google.xls";
	    		String xlsPath = "C:\\Users\\Chuck\\Desktop\\job\\AutoNav\\"+currentNode.getTitle()+".xls";
	    		UserInputReader reader = new UserInputReader(xlsPath, 0);
	    		reader.readFromExcel(formName, url);
	    		currentNode.setUserInput(reader.getUserInput());
	    		currentNode.setUserInputInvalid(reader.getUserInputInvalid());
	    		currentNode.userInputCombos = reader.getCombinationalInput();
	    		currentNode.convertInputToVectors();
	    		currentNode.noTable = false;
    		}
    		UserInputGUI showTable = new UserInputGUI(currentNode);
    		showTable.showUserInput();
    	} else if (command == TestCommands.REPLAYDF_COMMAND) { 
    		TestNode currentNode = getSelectedTestNode();
            replayDepthFirst(currentNode);
    	} else if (command == TestCommands.NEW_SUBTREE_COMMAND) { 
    		TestNode currentNode = getSelectedTestNode();
            if (currentNode!=null && currentNode.getChildCount()>0)
            	createTestTree(currentNode);
    	} else if (command == TestCommands.CLOSE_SUBTREE_COMMAND) { 
    		for (int subtreeIndex=testTrees.size()-1; subtreeIndex>0; subtreeIndex--){
    			tabbedPane.remove(subtreeIndex);
    			testTrees.remove(subtreeIndex);
    		}
    	} else if (command == TestCommands.EXPAND_PATHS_COMMAND) {
    		JTree currentTestTree = getCurrentTestTree();
    		expandAllPaths(currentTestTree, true);
            ((DefaultTreeModel)currentTestTree.getModel()).reload();
    	} else if (command == TestCommands.COLLAPSE_PATHS_COMMAND) {
       		JTree currentTestTree = getCurrentTestTree();
    		expandAllPaths(currentTestTree, false);
            ((DefaultTreeModel)currentTestTree.getModel()).reload();
    	} else if (command == TestCommands.ADD_NODE_COMMAND) {
                traceArea.append("To add a node.");
        } else if (command == TestCommands.DELETE_NODE_COMMAND) { 
                removeSelectedNode(getCurrentTestTree());
        } else { 
    	}
    } 
    // end popup menu
    /////////////////////////////////////////////////////////////	

 }
