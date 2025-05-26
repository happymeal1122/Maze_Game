

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ImageUtil {
    private static final String BASE_PATH = "C:\\Users\\pak41\\Desktop\\OSS\\teammate\\maze_generator\\MazeImage\\";
    private static final HashMap<String, Image> cache = new HashMap<>();

    public static Image getImage(String name) {
        return cache.computeIfAbsent(name, key -> new ImageIcon(BASE_PATH + key).getImage());
    }
}
