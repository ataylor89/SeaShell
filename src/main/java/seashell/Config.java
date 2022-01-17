package seashell;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author andrewtaylor
 */
public class Config {
    private Properties properties;
    private Logger logger;
    private Pattern colorPattern;
    
    public Config() {
        init();
    }
    
    private Properties getDefaults() {
        Properties defaultProps = new Properties();
        defaultProps.put("PATH", "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin");
        defaultProps.put("FGCOLOR", "rgba(0,0,0,255)");
        defaultProps.put("BGCOLOR", "rgba(255,255,255,255)");
        return defaultProps;
    }
 
    private void init() {
        logger = AppLogger.getLogger();
        properties = new Properties(getDefaults());
        String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".seashell";
        try (FileInputStream in = new FileInputStream(path)) {
            properties.load(in);
            logger.info("Loaded properties from path " + path);
            logger.info("Properties\n" + properties.toString());
        } catch (FileNotFoundException ex) {
            logger.warning(ex.toString());
        } catch (IOException ex) {
            logger.warning(ex.toString());
        }
        colorPattern = Pattern.compile("rgba\\((\\d+),\\s*(\\d+),\\s*(\\d+),\\s*(\\d+)\\)");
    }
    
    public String getPath() {
        return properties.getProperty("PATH");
    }
    
    public String[] getPaths() {
        return getPath().split(":");
    }
    
    public int[] getForegroundColor() {
        String fgcolor = properties.getProperty("FGCOLOR");
        logger.info("FGCOLOR=" + fgcolor);    
                
        Matcher matcher = colorPattern.matcher(fgcolor);
        if (matcher.matches()) {
            int[] rgba = new int[4];
            rgba[0] = Integer.parseInt(matcher.group(1));
            rgba[1] = Integer.parseInt(matcher.group(2));
            rgba[2] = Integer.parseInt(matcher.group(3));
            rgba[3] = Integer.parseInt(matcher.group(4));
            return rgba;
        }
        return new int[] {0, 0, 0, 255};
    }
    
    public int[] getBackgroundColor() {
        String bgcolor = properties.getProperty("BGCOLOR");
        logger.info("BGCOLOR=" + bgcolor);    
        
        Matcher matcher = colorPattern.matcher(bgcolor);
        if (matcher.matches()) {
            int[] rgba = new int[4];
            rgba[0] = Integer.parseInt(matcher.group(1));
            rgba[1] = Integer.parseInt(matcher.group(2));
            rgba[2] = Integer.parseInt(matcher.group(3));
            rgba[3] = Integer.parseInt(matcher.group(4));
            return rgba;
        }
        return new int[] {255, 255, 255, 255};
    }
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    public Properties getProperties() {
        return properties;
    }
}
