package seashell;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;

public class SeaShell extends JFrame implements ActionListener {

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newTab, closeTab, saveFile, openFile, exit;
    private JMenu colorsMenu;
    private JMenuItem setForegroundColor, 
            setBackgroundColor, 
            blackwhite, graywhite, 
            grayblue, 
            tealwhite, 
            purplewhite, 
            whiteblack, 
            whitegray, 
            bluegray, 
            whiteteal, 
            whitepurple,
            seaTheme;
    private JMenu settingsMenu;
    private JMenuItem setTitle, setPrefix;
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JFileChooser fileChooser;
    private Color foregroundColor, backgroundColor;
    private final Color purple = new Color(153, 0, 153, 255),
                        teal = new Color(0, 153, 153, 255),
                        gray = new Color(204, 204, 204, 255),
                        blue = new Color(0, 0, 204, 255),
                        lightblue = new Color(0, 153, 255, 255);
    private Logger logger;

    public SeaShell() {
        super("SeaShell");
    }
    
    public void init() {
        logger = AppLogger.getLogger();
        setLookAndFeel();
        logger.info(new EnvironmentMap("System environment", System.getenv()).toString());
    }
   
    public void createAndShowGui() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newTab = new JMenuItem("New tab");
        newTab.addActionListener(this);
        fileMenu.add(newTab);
        closeTab = new JMenuItem("Close tab");
        closeTab.addActionListener(this);
        fileMenu.add(closeTab);
        saveFile = new JMenuItem("Save file");
        saveFile.addActionListener(this);
        fileMenu.add(saveFile);
        openFile = new JMenuItem("Open file");
        openFile.addActionListener(this);
        fileMenu.add(openFile);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        fileMenu.add(exit);
        colorsMenu = new JMenu("Colors");
        setForegroundColor = new JMenuItem("Set foreground color");
        setForegroundColor.addActionListener(this);
        setBackgroundColor = new JMenuItem("Set background color");
        setBackgroundColor.addActionListener(this);
        blackwhite = new JMenuItem("Black white");
        blackwhite.addActionListener(this);
        graywhite = new JMenuItem("Gray white");
        graywhite.addActionListener(this);
        bluegray = new JMenuItem("Blue gray");
        bluegray.addActionListener(this);
        whiteteal = new JMenuItem("White teal");
        whiteteal.addActionListener(this);
        whitepurple = new JMenuItem("White purple");
        whitepurple.addActionListener(this);
        whiteblack = new JMenuItem("White black");
        whiteblack.addActionListener(this);
        whitegray = new JMenuItem("White gray");
        whitegray.addActionListener(this);
        grayblue = new JMenuItem("Gray blue");
        grayblue.addActionListener(this);
        tealwhite = new JMenuItem("Teal white");
        tealwhite.addActionListener(this);
        purplewhite = new JMenuItem("Purple white");
        purplewhite.addActionListener(this);
        seaTheme = new JMenuItem("Sea theme");
        seaTheme.addActionListener(this);
        colorsMenu.add(setForegroundColor);
        colorsMenu.add(setBackgroundColor);
        colorsMenu.add(blackwhite);
        colorsMenu.add(graywhite);
        colorsMenu.add(bluegray);
        colorsMenu.add(whiteteal);
        colorsMenu.add(whitepurple);
        colorsMenu.add(whiteblack);
        colorsMenu.add(whitegray);
        colorsMenu.add(grayblue);
        colorsMenu.add(tealwhite);
        colorsMenu.add(purplewhite);
        colorsMenu.add(seaTheme);
        settingsMenu = new JMenu("Settings");
        setTitle = new JMenuItem("Set tab title");
        setTitle.addActionListener(this);
        setPrefix = new JMenuItem("Set prefix");
        setPrefix.addActionListener(this);
        settingsMenu.add(setTitle);
        settingsMenu.add(setPrefix);
        menuBar.add(fileMenu);
        menuBar.add(colorsMenu);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(tabbedPane);
        add(panel);
        fileChooser = new JFileChooser();
        setVisible(true);
    }

    private void saveToFile() {
        int opt = fileChooser.showSaveDialog(this);

        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
                JTextArea ta = (JTextArea) sp.getViewport().getView();
                String text = ta.getText();
                writer.print(text);
            } catch (IOException e) {
                logger.warning(e.toString());
            }
        }
    }

    private void openFile() {
        int opt = fileChooser.showOpenDialog(this);

        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String text = reader.lines().reduce((partial, s) -> partial + s).toString();
                JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
                JTextArea ta = (JTextArea) sp.getViewport().getView();
                ta.setText(text);
            } catch (IOException e) {
                logger.warning(e.toString());
            }
        }
    }

    private void setForegroundColor(Color color) {
        foregroundColor = color;
        JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
        JTextArea ta = (JTextArea) sp.getViewport().getView();
        ta.setForeground(foregroundColor);
    }

    private void setBackgroundColor(Color color) {
        backgroundColor = color;
        JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
        JTextArea ta = (JTextArea) sp.getViewport().getView();
        ta.setBackground(backgroundColor);
    }

    private void setColors(Color fgColor, Color bgColor) {
        foregroundColor = fgColor;
        backgroundColor = bgColor;
        JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
        JTextArea ta = (JTextArea) sp.getViewport().getView();
        ta.setForeground(foregroundColor);
        ta.setBackground(backgroundColor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newTab) {
            String title = JOptionPane.showInputDialog(this, "Title:", "New tab", JOptionPane.QUESTION_MESSAGE);
            SeaShellTab seaShellTab = new SeaShellTab();
            seaShellTab.startNewLine();
            JScrollPane scrollPane = new JScrollPane(seaShellTab);
            tabbedPane.addTab(title, scrollPane);
        } else if (e.getSource() == closeTab) {
            int index = tabbedPane.getSelectedIndex();
            tabbedPane.removeTabAt(index);
        } else if (e.getSource() == saveFile) {
            saveToFile();
        } else if (e.getSource() == openFile) {
            openFile();
        } else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } else if (e.getSource() == setForegroundColor) {
            Color color = JColorChooser.showDialog(this, "Select a foreground color", foregroundColor);
            if (color != null) {
                foregroundColor = color;
            }
            setForegroundColor(foregroundColor);
        } else if (e.getSource() == setBackgroundColor) {
            Color color = JColorChooser.showDialog(this, "Select a background color", backgroundColor);
            if (color != null) {
                backgroundColor = color;
            }
            setBackgroundColor(backgroundColor);
        } else if (e.getSource() == blackwhite) {
            setColors(Color.BLACK, Color.WHITE);
        } else if (e.getSource() == graywhite) {
            setColors(Color.LIGHT_GRAY, Color.WHITE);
        } else if (e.getSource() == bluegray) {
            setColors(blue, gray);
        } else if (e.getSource() == whiteteal) {
            setColors(Color.WHITE, teal);
        } else if (e.getSource() == whitepurple) {
            setColors(Color.WHITE, purple);
        } else if (e.getSource() == whiteblack) {
            setColors(Color.WHITE, Color.BLACK);
        } else if (e.getSource() == whitegray) {
            setColors(Color.WHITE, Color.LIGHT_GRAY);
        } else if (e.getSource() == grayblue) {
            setColors(gray, blue);
        } else if (e.getSource() == tealwhite) {
            setColors(teal, Color.WHITE);
        } else if (e.getSource() == purplewhite) {
            setColors(purple, Color.WHITE);
        } else if (e.getSource() == seaTheme) {
            setColors(Color.WHITE, lightblue);
        } else if (e.getSource() == setTitle) {
            int index = tabbedPane.getSelectedIndex();
            if (index >= 0) {
                String title = JOptionPane.showInputDialog(this, "Set tab title: ", "Set tab title", JOptionPane.QUESTION_MESSAGE);
                if (title != null && title.trim().length() > 0)
                    tabbedPane.setTitleAt(index, title);
            }
        } else if (e.getSource() == setPrefix) {
            JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
            SeaShellTab seaShellTab = (SeaShellTab) scrollPane.getViewport().getView();
            String s = JOptionPane.showInputDialog(this, "Set starting text: ", "Set starting text", JOptionPane.QUESTION_MESSAGE);
            if (s != null && s.length() < 10) {
                seaShellTab.setPrefix(s.trim());
                seaShellTab.startNewLine();
            }
        } 
    }

    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            logger.warning(e.toString());
        }
    }
     
    public static void main(String[] args) {
        SeaShell seaShell = new SeaShell();
        seaShell.init();
        seaShell.createAndShowGui();
    }
}
