
import javax.swing.*;
import java.awt.*;

public class ImageUtil {
    public static Image getImage(String fileName) {
        try {
            String path = "C:\\Users\\pak41\\Desktop\\OSS\\teammate\\maze_generator\\MazeImage\\" + fileName;
            ImageIcon icon = new ImageIcon(path);

            // 이미지 로딩 실패 감지
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.err.println("❌ 로딩 실패: " + path);
                return null;
            }

            return icon.getImage();
        } catch (Exception e) {
            System.err.println("❌ 예외 발생: " + fileName + " → " + e.getMessage());
            return null;
        }
    }
}
