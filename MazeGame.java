import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class MazeGame extends JPanel {

    private final int rows = 21, cols = 21;
    private final int cellSize = 25;
    private final int[][] maze = new int[rows][cols];

    private int endRow, endCol; // 🔹 도착지 위치

    public MazeGame() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.WHITE);
        initializeMaze();
        dfsGenerate(1, 1);
        setEndPoint(); // 🔹 도착지점 설정
    }

    private void initializeMaze() {
        for (int[] row : maze) {
            Arrays.fill(row, 1);
        }
    }

    private void dfsGenerate(int r, int c) {
        maze[r][c] = 0;

        List<int[]> directions = Arrays.asList(
            new int[]{-2, 0}, new int[]{2, 0}, new int[]{0, -2}, new int[]{0, 2}
        );
        Collections.shuffle(directions);

        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            if (inBounds(nr, nc) && maze[nr][nc] == 1) {
                maze[r + dir[0] / 2][c + dir[1] / 2] = 0;
                dfsGenerate(nr, nc);
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r > 0 && c > 0 && r < rows && c < cols;
    }

    private void setEndPoint() {
        // 🔹 가장 오른쪽 아래에 가까운 길을 도착지점으로
        for (int r = rows - 2; r > 0; r--) {
            for (int c = cols - 2; c > 0; c--) {
                if (maze[r][c] == 0) {
                    endRow = r;
                    endCol = c;
                    return;
                }
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
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }

        // 🔹 도착지점 녹색으로 표시
        g.setColor(Color.GREEN);
        g.fillRect(endCol * cellSize, endRow * cellSize, cellSize, cellSize);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Step 4: 도착지점");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MazeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
