package seashell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public Interpreter(SeaShellTab display, Config config) {
        this.display = display;
        this.config = config;
        logger = AppLogger.getLogger();
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
        try {
            process = processBuilder.start();
            read(process);
        } catch (IOException ex) {
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void interpret(String line) {
        if (line == null || line.trim().length() == 0)
            return;       
        
        logger.info("Interpreting command " + line);  
 
        if (process != null && process.isAlive()) 
            write(line);
        else {      
            String[] args = line.split("\\s+");
            logger.info("args.length = " + args.length);

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
