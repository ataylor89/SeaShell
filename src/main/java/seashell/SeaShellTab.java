package seashell;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    private Interpreter interpreter;
    private String prefix = "&";
    private int prefixPosition;

    public SeaShellTab() {
        config = new Config();
        interpreter = new Interpreter(this, config);
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
    
    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public Interpreter getInterpreter() {
        return interpreter;
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
                interpreter.interpret(code);
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
                interpreter.closeRunningProcess();
            }
        };
        am.put("ctrl+c", keyboardInterrupt);
        am.put("ctrl+d", keyboardInterrupt);
    }
}
