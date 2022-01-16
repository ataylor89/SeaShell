package seashell;

import javax.swing.JTextArea;

/**
 *
 * @author andrewtaylor
 */
public class SeaShellTab extends JTextArea {
    private Config config;
    private Interpreter interpreter;
    private String startingText = "& ";
    private int previousCaretPosition;

    public SeaShellTab() {
        config = new Config();
        interpreter = new Interpreter(this, config);
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setPreviousCaretPosition(int previousCaretPosition) {
        this.previousCaretPosition = previousCaretPosition;
    }

    public int getPreviousCaretPosition() {
        return previousCaretPosition;
    }

    @Override
    public void append(String text) {
        super.append(text);
        setCaretPosition(getText().length());
        setPreviousCaretPosition(getText().length());
    }

    public void setStartingText(String startingText) {
        this.startingText = startingText;
    }

    public String getStartingText() {
        return startingText;
    }

    public void startNewLine() {
        if (startingText != null & startingText.length() > 0)
            append(startingText);
    }
}
