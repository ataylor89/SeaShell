package seashell;

import java.util.Map;

/**
 *
 * @author andrewtaylor
 */
public class EnvironmentMap {
    private String title;
    private Map<String, String> map;
    
    public EnvironmentMap(Map<String, String> map) {
        this.title = title;
        this.map = map;
    }
    
    public EnvironmentMap(String title, Map<String, String> map) {
        this.title = title;
        this.map = map;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(title + "\n");
        for (String key : System.getenv().keySet())
            s.append("Environment variable: " + key + " Value: " + System.getenv().get(key) + "\n");
        return s.toString();
    }
}
