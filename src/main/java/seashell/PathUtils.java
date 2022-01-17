package seashell;

/**
 *
 * @author andrewtaylor
 */
public class PathUtils {
    
    public static String path(String ... paths) {
        String path = paths[0];
        for (int i = 1; i < paths.length; i++) {
            path += System.getProperty("file.separator");
            path += paths[i];
        }
        return path;
    }
    
    public static String substitute(String path) {
        if (path.equals("~"))
            return System.getProperty("user.home");
        if (path.startsWith("~"))
            return path.replaceFirst("~", System.getProperty("user.home"));
        return path;
    }
    
    public static String substitute(String path, String parent) {
        if (path.equals("~"))
            return System.getProperty("user.home");
        if (path.equals("..") && parent != null)
            return parent;
        if (path.startsWith("~"))
            return path.replaceFirst("~", System.getProperty("user.home"));
        if (path.startsWith("..") && parent != null)
            return path.replaceFirst("..", parent);
        return path;
    }
    
    public static String absolute(String path, String parent) {
        path = substitute(path);
        if (path.startsWith(System.getProperty("file.separator")))
            return path;
        return path(parent, path);
    }   
}
