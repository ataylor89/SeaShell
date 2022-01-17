package seashell;

import java.awt.Color;
import javax.swing.JTextArea;

/**
 *
 * @author andrewtaylor
 */
public class SeaShellTab extends JTextArea {
    private Config config;
    private Interpreter interpreter;
    private String prefix = "& ";
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

    public void setStartingText(String startingText) {
        if (startingText != null && startingText.length() > 0)
            this.prefix = startingText;
    }

    public String getStartingText() {
        return prefix;
    }

    public void startNewLine() {
        if (!getText().endsWith("\n"))
            append("\n");
        append(prefix);
    }
}
