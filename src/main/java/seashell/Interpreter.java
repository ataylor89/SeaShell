package seashell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
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
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String finalize(String path) {
        if (path.equals("~"))
            return System.getProperty("user.home");
        if (path.equals("..") && workingDirectory.getParent() != null)
            return workingDirectory.getParent();
        if (path.startsWith("~"))
            return path.replaceFirst("~", System.getProperty("user.home"));
        if (path.startsWith("..") && workingDirectory.getParent() != null)
            return path.replaceFirst("..", workingDirectory.getParent());
        if (!path.startsWith(System.getProperty("file.separator")))
            return workingDirectory.getAbsolutePath() + System.getProperty("file.separator") + path;
        return path;
    }
    
    public void interpret(String line) {
        if (line == null || line.trim().length() == 0)
            return;       
        
        String[] args = line.split("\\s+");
        logger.info("args.length = " + args.length);
        logger.info("Interpreting command " + line);
 
        if (process != null && process.isAlive()) 
            write(line);
        else if (args[0].equals("cd") && args.length == 2) {
            File file = new File(finalize(args[1]));
            if (file.exists() && file.isDirectory())
                this.workingDirectory = file;
            
            display.startNewLine();
        }
        else {      
            String[] paths = config.getPaths();        
            for (String path : paths) {
                logger.info("Checking path " + path);
                String filePath = path + System.getProperty("file.separator") + args[0];
                logger.info("File path: " + filePath);
                File file = new File(filePath);
                if (file.exists() && !file.isDirectory()) {                                       
                    logger.info("Found file " + filePath);
                    run(args);
                    logger.info("Started process " + args[0]);
                    logger.info("Running command " + line);
                    break;
                }
            }
        }
    }
}
