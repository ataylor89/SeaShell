package seashell;

import java.io.File;

/**
 *
 * @author andrewtaylor
 */
public class Path {
  
    private String path;
    public static final String SEPARATOR = System.getProperty("file.separator");
    
    public Path(String path) {
        substitute(path);
    }
    
    public Path(File parent, String child) {
        substitute(parent.getParent(), parent.getPath(), child);
    }
    
    public Path(String parent, String child) {
        this(new File(parent), child);
    }
    
    private void substitute(String path) {
        if (path.equals("~"))
            setPath(System.getProperty("user.home"));
        else if (path.startsWith("~"))
            setPath(path.replaceFirst("~", System.getProperty("user.home")));
        else
            setPath(path);
    }
    
    private void substitute(String grandparent, String parent, String child) {
        if (child.equals("~"))
            setPath(System.getProperty("user.home"));
        else if (child.startsWith("~"))
            setPath(child.replaceFirst("~", System.getProperty("user.home")));
        else if (child.equals("..") && grandparent != null)
            setPath(grandparent);
        else if (child.startsWith("../") && grandparent != null)
            setPath(child.replaceFirst("..", grandparent));
        else if (isAbsolute(child))
            setPath(child);
        else
            setPath(parent + SEPARATOR + child);             
    }
    
    private boolean isAbsolute(String path) {
        return path.startsWith("/");
    }
    
    private void setPath(String path) {
        this.path = path;
    }
    
    public File toFile() {
        return new File(path);
    }
    
    @Override
    public String toString() {
        return path;
    }
}
