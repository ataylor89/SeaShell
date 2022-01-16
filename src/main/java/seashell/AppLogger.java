package seashell;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 *
 * @author andrewtaylor
 */
public class AppLogger {
    private static Logger logger;
    
    private static void init() {
        logger = Logger.getLogger("SeaShell");
        logger.setLevel(Level.ALL);
        logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        try {
            logger.addHandler(new FileHandler("SeaShell.log", true));
            logger.info("Set up file logging");
        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }
    
    public static Logger getLogger() { 
        if (logger == null) 
            init();
        return logger;
    }
}
