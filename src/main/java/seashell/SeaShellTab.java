package seashell;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

/**
 *
 * @author andrewtaylor
 */
public class SeaShellTab extends JTextArea implements KeyListener {
    
    private Config config;
    private String prefix = "&";
    private int prefixPosition;
    private Process process;
    private File workingDirectory;
    private Logger logger;

    public SeaShellTab(Config config) {
        this.config = config;
        logger = AppLogger.getLogger();
        workingDirectory = new File(System.getProperty("user.dir"));
        init();
    }

    private void init() {
        int[] fg = config.getForegroundColor();
        int[] bg = config.getBackgroundColor();
        setForeground(new Color(fg[0], fg[1], fg[2], fg[3]));
        setBackground(new Color(bg[0], bg[1], bg[2], bg[3]));
        setLineWrap(true);
        setPrefix(config.getPrefix().trim());
        addKeyListener(this);
        setupKeyStrokes();
    }
    
    public void setPrefixPosition(int prefixPosition) {
        this.prefixPosition = prefixPosition;
    }

    public int getPrefixPosition() {
        return prefixPosition;
    }

    @Override
    public void append(String text) {
        super.append(text);
        setPrefixPosition(getText().length());
        setCaretPosition(getText().length());
    }

    public void setPrefix(String prefix) {
        if (prefix != null && prefix.length() > 0)
            this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void startNewLine() {
        String text = getText();
        if (!text.isEmpty() && !text.endsWith("\n"))
            append("\n");
        append(prefix + " ");
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = getText();
            int textLength = text.length();
            if (prefixPosition < textLength) {
                String code = text.substring(prefixPosition, textLength);
                interpret(code);
            }
        }
    }
    
    private void setupKeyStrokes() {
        InputMap im = getInputMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "ctrl+c");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), "ctrl+d");
        ActionMap am = getActionMap();
        Action keyboardInterrupt = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeRunningProcess();
            }
        };
        am.put("ctrl+c", keyboardInterrupt);
        am.put("ctrl+d", keyboardInterrupt);
    }
    
    private void write(String line) {
        BufferedWriter writer = process.outputWriter();
        try {
            writer.write(line);
            writer.flush();
        } catch (IOException ex) {
            logger.warning(ex.toString());
        }
    }
    
    private void read(Process process) {
        Thread thread = new Thread(() -> {
            try {
                BufferedReader reader = process.inputReader();
                char[] buf = new char[10000];
                while (reader.ready() || process.isAlive()) {
                    int count = reader.read(buf, 0, 10000);
                    if (count > 0)
                        append(new String(buf, 0, count));
                    Thread.sleep(2);
                }                
            } catch (IOException | InterruptedException ex) {
                logger.warning(ex.toString());
            } finally { 
                this.process = null;
                startNewLine();
            }
        });
        thread.start();
    }

    private void run(String[] args) {        
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.directory(workingDirectory);
        try {
            process = processBuilder.start();
            read(process);
        } catch (IOException ex) {
            logger.warning(ex.toString());
        }
    }

    public void interpret(String line) {
        if (line == null || line.trim().length() == 0)
            return;       
        
        String[] args = line.split("\\s+");
        
        if (hasRunningProcess()) 
            write(line);
        else if (args[0].equals("cd") && args.length == 2) {
            String filePath = new Path(workingDirectory, args[1]).toString();
            File file = new File(filePath);
            if (file.exists() && file.isDirectory())
                this.workingDirectory = file;
            
            startNewLine();
        }
        else if (args[0].equals("clear") && args.length == 1) {
            setText("");
            startNewLine();
        }
        else {    
            String[] paths = config.getPaths();        
            for (String path : paths) {
                File file = new Path(path, args[0]).toFile(); 
                if (file.exists() && !file.isDirectory()) {
                    run(args);
                    break;
                }
            }
        }
    }
    
    public boolean hasRunningProcess() {
        return process != null && process.isAlive();
    }
    
    public boolean closeRunningProcess() {
        if (hasRunningProcess())
            process.destroy();
        return process.isAlive();
    }
}
