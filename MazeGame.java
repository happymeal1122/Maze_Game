import javax.swing.*;
import java.awt.*;

public class MazeGame extends JPanel {

    private final int rows = 21, cols = 21; // 홀수 크기 권장
    private final int cellSize = 25;
    private final int[][] maze = new int[rows][cols];

    public MazeGame() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.WHITE);
        initializeMaze();
    }

    private void initializeMaze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 1; // 벽으로 초기화
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Step 2: 초기화");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MazeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
