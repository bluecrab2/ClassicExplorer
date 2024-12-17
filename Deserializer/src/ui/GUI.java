package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import fields.Field;
import fields.ArrayField;
import fields.Class;
import fields.ClassField;
import io.Reader;

/**
 * The GUI for the Classic Level Reader. Contains a toolbar at the
 * top and a tree representing all the information stored inside
 * of the file after the reading. Calls the {@link #Reader} class' read
 * method to complete all the reading functionality.
 * 
 * Contact info:
 * Discord - bluecrab2#1996
 * Email - bluecrab2mc@gmail.com
 * 
 * 
 * @author bluecrab2
 */
public class GUI extends JFrame implements ActionListener {
	/** Constant for Serializable interface that JFrame extends */
	private static final long serialVersionUID = 1L;
	/** Content plain of the GUI (BorderLayout format) */
	private Container c;
	
	/** Menu and menu item fields */
	private DarkModeMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openFile;
	private JMenuItem closeFile;
	private JMenu settingsMenu;
	private JCheckBoxMenuItem darkModeMenuItem;
	private JCheckBoxMenuItem convertUnixItem;
	private JCheckBoxMenuItem fullClassNamesItem;
	private JCheckBoxMenuItem showSerialItem;
	private JCheckBoxMenuItem skipBlocksItem;
	private JMenuItem zoomOut;
	private JMenuItem zoomIn;
	private JMenu aboutMenu;
	private JMenuItem aboutMenuItem;

	/** The tree that will stored the information of the classic file */
	private JTree tree;
	/** The scroll bar pane that contains the tree */
	private JScrollPane scrollPane;
	/** Vertical scroll bar in the scroll pane */
	private JScrollBar verticalScrollBar;
	/** Horizontal scroll bar in the scroll pane */
	private JScrollBar horizontalScrollBar;
	
	/** Color for the background of the GUI in dark mode */
	public static final Color DARK_MODE_BACKGROUND = new Color(60, 60, 60);
	/** Color for the foreground of the GUI in dark mode */
	public static final Color DARK_MODE_FOREGROUND = new Color(40, 40, 40);
	
	/** The last class read from a file */
	private static Class readClass;
	
	/** The string for the create time parameters that can be converted to a date in settings */
	public static final String CREATE_TIME = "createTime";

	/** Call for GUI to be created */
	public static void main(String [] args) {
		new GUI();
	}
	
	/** Create GUI */
	public GUI() {
		initializeGUI();
	}
	
	/** Initializes the GUI */
	private void initializeGUI() {
		//Look and feel
		try {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//Read settings from settings.txt file
		Settings.read();
        
		//Create main container
		c = getContentPane();
		setTitle("ClassicExplorer");
		setSize(400, 400);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		//Set icon
		ClassLoader classLoader = (new Reader()).getClass().getClassLoader();
		InputStream imageIO = classLoader.getResourceAsStream("images/ClassicExplorer Logo.png");
		Image img = null;
		try {
			if(imageIO != null)
				img = ImageIO.read(imageIO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(img != null) {
			setIconImage(img);
		}
		
		//setIconImage(Toolkit.getDefaultToolkit().getImage("images/ClassicExplorer Logo.png"));
		
		//////Create the tool bar
		menuBar = new DarkModeMenuBar();
		setJMenuBar(menuBar);
		
		//File menu
		fileMenu = new JMenu("File");
		fileMenu.addActionListener(this);
		openFile = new JMenuItem("Open File");
		openFile.addActionListener(this);
		fileMenu.add(openFile);
		closeFile = new JMenuItem("Close File");
		closeFile.addActionListener(this);
		fileMenu.add(closeFile);
		menuBar.add(fileMenu);
		
		//Settings menu
		settingsMenu = new JMenu("Settings");
		
		darkModeMenuItem = new JCheckBoxMenuItem("Dark Mode");
		darkModeMenuItem.setSelected(Settings.darkMode);
		darkModeMenuItem.addActionListener(this);
		settingsMenu.add(darkModeMenuItem);
		
		convertUnixItem = new JCheckBoxMenuItem("Convert Unix Timestamp to Date");
		convertUnixItem.setSelected(Settings.convertUnixTimestampToDate);
		convertUnixItem.addActionListener(this);
		settingsMenu.add(convertUnixItem);
		
		fullClassNamesItem = new JCheckBoxMenuItem("Show Full Class Names");
		fullClassNamesItem.setSelected(Settings.showFullClassNames);
		fullClassNamesItem.addActionListener(this);
		settingsMenu.add(fullClassNamesItem);

		showSerialItem = new JCheckBoxMenuItem("Show SerialVersionUID");
		showSerialItem.setSelected(Settings.showSerialVersionUID);
		showSerialItem.addActionListener(this);
		settingsMenu.add(showSerialItem);
		
		skipBlocksItem = new JCheckBoxMenuItem("Skip Blocks");
		skipBlocksItem.setSelected(Settings.skipBlocks);
		skipBlocksItem.addActionListener(this);
		settingsMenu.add(skipBlocksItem);
		
		zoomOut = new JMenuItem("- Zoom Out");
		zoomOut.addActionListener(this);
		settingsMenu.add(zoomOut);
		KeyStroke ctrlMinus = KeyStroke.getKeyStroke("control MINUS");
		zoomOut.setAccelerator(ctrlMinus);
		zoomIn = new JMenuItem("+ Zoom In");
		zoomIn.addActionListener(this);
		settingsMenu.add(zoomIn);
		KeyStroke ctrlPlus = KeyStroke.getKeyStroke("control EQUALS");
		zoomIn.setAccelerator(ctrlPlus);
		
		menuBar.add(settingsMenu);

		//About menu
		aboutMenu = new JMenu("About");
		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(this);
		aboutMenu.add(aboutMenuItem);
		menuBar.add(aboutMenu);
		
		//Update menu bar so it appears on start up
		menuBar.revalidate();
		
		//Change color scheme to dark mode if enabled
		updateDarkMode();
	}
	
	/** Calls the Reader class to read the file at the give file
	 * path and then updates display tree to show new file.
	 * */
	private void readFile(File readFile) throws IOException {
		readClass = Reader.read(readFile);
	}
	
	/**
	 * Displays the class in readClass as a tree structure. The
	 * output can be effected by the convertUnixTimestampToDate,
	 * showFullClassNames, and showSerialVersionUID settings.
	 */
	private void displayReadClass() {
		//Remove old file contents
		if(scrollPane != null)
			c.remove(scrollPane);
		
		//Create root node and tree by converting the readClass
		DefaultMutableTreeNode root = convertClassToNodeTree(readClass);
		tree = new JTree(root);
		
		//Create scroll pane to contain the tree
		scrollPane = new JScrollPane(tree);
		verticalScrollBar = new JScrollBar();
		verticalScrollBar.setUI(new DarkModeScrollBarUI());
		verticalScrollBar.setUnitIncrement(16);
		scrollPane.setVerticalScrollBar(verticalScrollBar);
		horizontalScrollBar = new JScrollBar();
		horizontalScrollBar.setOrientation(JScrollBar.HORIZONTAL);
		horizontalScrollBar.setUI(new DarkModeScrollBarUI());
		scrollPane.setHorizontalScrollBar(horizontalScrollBar);
		
		//Update dark mode settings
		if(Settings.darkMode)
			tree.setBackground(DARK_MODE_BACKGROUND);
		tree.setCellRenderer(new DarkModeCellRenderer());
		
		//Set font
		if(tree != null) {
			Font currentFont = tree.getFont();
			Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), Settings.fontSize);
			tree.setFont(newFont);
			tree.setRowHeight(tree.getRowHeight() + (Settings.fontSize - 10));
		}
		
		//Add the scroll pane and refresh the GUI
		c.add(scrollPane, BorderLayout.CENTER);
		revalidate();
	}

	/**
	 * Updates all contents of the GUI to the dark mode colors.
	 */
	private void updateDarkMode() {
		if(Settings.darkMode) {
			//Dark mode
			c.setBackground(DARK_MODE_BACKGROUND);
			menuBar.setColor(DARK_MODE_FOREGROUND);
			fileMenu.setForeground(Color.WHITE);
			settingsMenu.setForeground(Color.WHITE);
			aboutMenu.setForeground(Color.WHITE);
			if(tree != null)
				tree.setBackground(DARK_MODE_BACKGROUND);
		} else {
			//Light mode
			c.setBackground(Color.WHITE);
			menuBar.setColor(Color.WHITE);
			fileMenu.setForeground(Color.BLACK);
			settingsMenu.setForeground(Color.BLACK);
			aboutMenu.setForeground(Color.BLACK);
			if(tree != null)
				tree.setBackground(Color.WHITE);
		}
		
		//Update scroll bars
		if(verticalScrollBar != null)
			((DarkModeScrollBarUI) verticalScrollBar.getUI()).configureScrollBarColors();
		if(horizontalScrollBar != null)
			((DarkModeScrollBarUI) horizontalScrollBar.getUI()).configureScrollBarColors();
		
		//Update tree cells
		if(tree != null)
			tree.setCellRenderer(new DarkModeCellRenderer());
	}
	
	/**
	 * Converts the given class into a tree of nodes to represent
	 * all the contents of the class' fields.
	 */
	private DefaultMutableTreeNode convertClassToNodeTree(Class c) {
		//Create node of tree with the given class' name
		String displayClassName = c.getName();
		if(!Settings.showFullClassNames) {
			//Cut class name to only final part if setting is false
			int slashIdx = displayClassName.lastIndexOf('/');
			int dotIdx = displayClassName.lastIndexOf('.');
			int cutIdx = slashIdx > dotIdx ? slashIdx : dotIdx;
			if(cutIdx != -1)
				displayClassName = displayClassName.substring(cutIdx + 1);
		}
		if(Settings.showSerialVersionUID) {
			//Add serialVersionUID if enabled
			displayClassName += " (" + c.getSerialVersionUID() + ")";
		}
		//Create root of tree with the class' display name
		DefaultMutableTreeNode localRoot = new DefaultMutableTreeNode(displayClassName);
		
		//Add fields to class
		addFieldsToNode(localRoot, c.getFields());
		
		//Add super class to class
		Class superC = c.getSuperClass();
		if(superC != null) {
			localRoot.add(convertClassToNodeTree(superC));
		}
		
		return localRoot;
	}

	private void addFieldsToNode(DefaultMutableTreeNode node, ArrayList<Field> fields) {
		for(Field f : fields) {
			DefaultMutableTreeNode newNode;
			
			if(f != null) {
				if(Settings.convertUnixTimestampToDate && f.getFieldName().equals(CREATE_TIME)) {
					Date date = new Date((Long) f.getField());
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
					newNode = new DefaultMutableTreeNode(f.getFieldName() + ": " + dateFormat.format(date));
				} else {
					if(!(f instanceof ClassField)) {
						newNode = new DefaultMutableTreeNode(f.getFieldName() + ": " + f.getField());
					} else {
						String displayClassName = (String) f.getField();
						if(!Settings.showFullClassNames) {
							//Cut class name to only final part if setting is false
							int slashIdx = displayClassName.lastIndexOf('/');
							int dotIdx = displayClassName.lastIndexOf('.');
							int cutIdx = slashIdx > dotIdx ? slashIdx : dotIdx;
							if(cutIdx != -1)
								displayClassName = displayClassName.substring(cutIdx + 1);
						}
						if(Settings.showSerialVersionUID) {
							Class c = ((ClassField) f).getClassField();
							if(c != null)
								displayClassName += " (" + c.getSerialVersionUID() + ")";
						}
						
						newNode = new DefaultMutableTreeNode(f.getFieldName() + ": " + displayClassName);
					}
				}
			} else {
				newNode = new DefaultMutableTreeNode("null");
			}
				
			node.add(newNode);
			
			//Recursion to add the class or array's fields
			if(f instanceof ClassField) {
				ClassField cf = (ClassField) f;
				Class nextClass = cf.getClassField();
				if(nextClass != null)
					addFieldsToNode(newNode, nextClass.getFields());
				if(cf.isList()) {
					ArrayList<Class> classes = cf.getArrayList();
					for(Class c : classes)
						newNode.add(convertClassToNodeTree(c));
				} else if(cf.isString()) {
					String strNodeName = cf.getString();
					if(strNodeName != null) {
						newNode.add(new DefaultMutableTreeNode(strNodeName));
					} else {
						newNode.add(new DefaultMutableTreeNode("null"));
					}
				}
			} else if(f instanceof ArrayField) {
				ArrayField af = (ArrayField) f;
				addFieldsToNode(newNode, af.getArray());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object buttonSource = e.getSource();
		if(buttonSource == openFile) {
			//Open file selector
			JFileChooser fileChooser = new JFileChooser();
			if(Settings.currentFileDirectory != null)
				fileChooser.setCurrentDirectory(Settings.currentFileDirectory);
			int result = fileChooser.showOpenDialog(this);

			//Save directory in settings for next time
			Settings.currentFileDirectory = fileChooser.getCurrentDirectory();
			Settings.write();
			
			if(result == JFileChooser.APPROVE_OPTION) {
				//Read and display chosen file
				File selectedFile = fileChooser.getSelectedFile();
				try {
					readFile(selectedFile);
					displayReadClass();
				} catch(Exception e2) {
					e2.printStackTrace(); //TODO
					
					String errorMessage = "";
					if(e2.getMessage() != null) {
						errorMessage = e2.getMessage();
					}
		            JOptionPane.showMessageDialog(this, "Invalid file!\n" + errorMessage,
		               "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if(buttonSource == closeFile) {
			if(scrollPane != null)
				c.remove(scrollPane);
			c.repaint();
		} else if(buttonSource == darkModeMenuItem) {
			Settings.darkMode = darkModeMenuItem.isSelected();
			updateDarkMode();
			Settings.write();
		} else if(buttonSource == convertUnixItem) {
			Settings.convertUnixTimestampToDate = convertUnixItem.isSelected();
			if(readClass != null)
				displayReadClass();
			Settings.write();
		} else if(buttonSource == fullClassNamesItem) {
			Settings.showFullClassNames = fullClassNamesItem.isSelected();
			if(readClass != null)
				displayReadClass();
			Settings.write();
		} else if(buttonSource == showSerialItem) { 
			Settings.showSerialVersionUID = showSerialItem.isSelected();
			if(readClass != null)
				displayReadClass();
			Settings.write();
		} else if(buttonSource == skipBlocksItem) {
			Settings.skipBlocks = skipBlocksItem.isSelected();
			if(readClass != null)
				displayReadClass();
			Settings.write();
		} else if(buttonSource == zoomOut) {
			Font currentFont = tree.getFont();
			Font decreaseFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() - 5);
			if(tree != null) {
				tree.setFont(decreaseFont);
				tree.setRowHeight(tree.getRowHeight() - 5);
			}
			Settings.fontSize = tree.getFont().getSize();
			Settings.write();
		} else if(buttonSource == zoomIn) {
			Font currentFont = tree.getFont();
			Font increaseFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 5);
			if(tree != null) {
				tree.setFont(increaseFont);
				tree.setRowHeight(tree.getRowHeight() + 5);
			}
			Settings.fontSize = tree.getFont().getSize();
			Settings.write();
		} else if(buttonSource == aboutMenuItem) {
			JOptionPane.showMessageDialog(this, "ClassicExplorer was created by bluecrab2\n"
					+ "Version: 1.1\n"
					+ "Contact info:\n"
					+ "Discord - bluecrab2#1996\n"
					+ "Email - bluecrab2mc@gmail.com\n"
					+ "Also, check out my Youtube channel :D - www.youtube.com/@bluecrab2\n",
		               "About", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
