package seashell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author andrewtaylor
 */
public class Interpreter {

    private SeaShellTab display;
    private Config config;
    private Process process;
    private Logger logger;
    private File workingDirectory;
        
    public Interpreter(SeaShellTab display, Config config) {
        this.display = display;
        this.config = config;
        logger = AppLogger.getLogger();
        workingDirectory = new File(System.getProperty("user.dir"));
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
                        display.append(new String(buf, 0, count));
                    Thread.sleep(2);
                }                
            } catch (IOException | InterruptedException ex) {
                logger.warning(ex.toString());
            } finally { 
                this.process = null;
                display.startNewLine();
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
            
            display.startNewLine();
        }
        else if (args[0].equals("clear") && args.length == 1) {
            display.setText("");
            display.startNewLine();
        }
        else {    
            String[] paths = config.getPaths();        
            for (String path : paths) {
                String filePath = new Path(path, args[0]).toString();
                File file = new File(filePath);
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
