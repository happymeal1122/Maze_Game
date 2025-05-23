import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class MazeGame extends JPanel {

    private final int rows = 21, cols = 21; // 홀수 권장
    private final int cellSize = 25;
    private final int[][] maze = new int[rows][cols];

    public MazeGame() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.WHITE);
        initializeMaze();
        dfsGenerate(1, 1); // DFS로 길 생성 시작
    }

    private void initializeMaze() {
        for (int[] row : maze) {
            Arrays.fill(row, 1); // 전체 벽
        }
    }

    private void dfsGenerate(int r, int c) {
        maze[r][c] = 0; // 현재 칸을 길로 지정

        List<int[]> directions = Arrays.asList(
            new int[]{-2, 0}, // 위
            new int[]{2, 0},  // 아래
            new int[]{0, -2}, // 왼쪽
            new int[]{0, 2}   // 오른쪽
        );
        Collections.shuffle(directions); // 방향 무작위 섞기

        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            if (inBounds(nr, nc) && maze[nr][nc] == 1) {
                maze[r + dir[0] / 2][c + dir[1] / 2] = 0; // 중간 벽 제거
                dfsGenerate(nr, nc);
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r > 0 && c > 0 && r < rows && c < cols;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Step 3: DFS 미로 생성");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MazeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
