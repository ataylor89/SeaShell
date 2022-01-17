package seashell;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrewtaylor
 */
public class Config {
    private Properties properties;
    private Logger logger;
    
    public Config() {
        init();
    }
    
    private Properties getDefaults() {
        Properties defaultProps = new Properties();
        defaultProps.put("PATH", "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin");
        return defaultProps;
    }
 
    private void init() {
        logger = AppLogger.getLogger();
        properties = new Properties(getDefaults());
        String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".seashell";
        try (FileInputStream in = new FileInputStream(path)) {
            properties.load(in);
        } catch (FileNotFoundException ex) {
            logger.warning(ex.toString());
        } catch (IOException ex) {
            logger.warning(ex.toString());
        }
    }
    
    public String getPath() {
        return properties.getProperty("PATH");
    }
    
    public String[] getPaths() {
        String[] arr = getPath().split(":");
        String[] paths = new String[arr.length + 1];
        paths[0] = System.getenv("PWD");
        System.arraycopy(arr, 0, paths, 1, arr.length);
        return paths;
    }
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    public Properties getProperties() {
        return properties;
    }
}
