package seashell;

import java.io.File;

/**
 *
 * @author andrewtaylor
 */
public class Path {
  
    private String path;
    
    public Path(String path) {
        this.path = substitute(path);
    }
    
    public Path(File parent, String path) {
        this.path = substitute(parent.getPath(), path);
    }
    
    public Path(String parent, String path) {
        this.path = substitute(parent, path);
    }
    
    private String substitute(String path) {
        if (path.equals("~"))
            path = System.getProperty("user.home");
        if (path.startsWith("~"))
            return path.replaceFirst("~", System.getProperty("user.home"));
        return path;
    }
    
    private String substitute(String parent, String path) {
        if (path.equals("~"))
            return System.getProperty("user.home");
        if (path.equals("..") && parent != null)
            return parent;
        if (path.startsWith("~"))
            return path.replaceFirst("~", System.getProperty("user.home"));
        if (path.startsWith("..") && parent != null)
            return path.replaceFirst("..", parent);
        return parent + System.getProperty("file.separator") + path;             
    }
    
    @Override
    public String toString() {
        return path;
    }
}
