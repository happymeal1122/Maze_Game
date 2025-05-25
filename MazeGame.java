import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MazeGame extends JPanel implements KeyListener {
    private final int rows = 31, cols = 31;
    private final int cellSize = 31;
    private final int[][] maze = new int[rows][cols];
    private int playerRow = 1, playerCol = 1;
    private int endRow, endCol;

    public MazeGame() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        generateMaze();
    }

    private void generateMaze() {
        for (int[] row : maze) {
            java.util.Arrays.fill(row, 1); // 벽 초기화
        }
        dfsGenerate(1, 1);

        // 도착 지점 설정 (오른쪽 아래)
        for (int i = rows - 2; i > 0; i--) {
            for (int j = cols - 2; j > 0; j--) {
                if (maze[i][j] == 0) {
                    endRow = i;
                    endCol = j;
                    return;
                }
            }
        }
    }

    private void dfsGenerate(int r, int c) {
        maze[r][c] = 0;
        int[][] dirs = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
        shuffle(dirs);

        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (inBounds(nr, nc) && maze[nr][nc] == 1) {
                maze[(r + nr) / 2][(c + nc) / 2] = 0;
                dfsGenerate(nr, nc);
            }
        }
    }

    private void shuffle(int[][] arr) {
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int[] tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && c >= 0 && r < rows && c < cols;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] == 1) {
                    g.setColor(Color.BLACK); // 벽
                } else {
                    g.setColor(Color.WHITE); // 길
                }
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }

        // 도착 지점
        g.setColor(Color.GREEN);
        g.fillRect(endCol * cellSize, endRow * cellSize, cellSize, cellSize);

        // 플레이어
        g.setColor(Color.BLUE);
        g.fillOval(playerCol * cellSize + 5, playerRow * cellSize + 5, cellSize - 10, cellSize - 10);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int newRow = playerRow;
        int newCol = playerCol;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: newRow--; break;
            case KeyEvent.VK_S: newRow++; break;
            case KeyEvent.VK_A: newCol--; break;
            case KeyEvent.VK_D: newCol++; break;
        }

        if (inBounds(newRow, newCol) && maze[newRow][newCol] == 0) {
            playerRow = newRow;
            playerCol = newCol;
            repaint();
        }

        if (playerRow == endRow && playerCol == endCol) {
            JOptionPane.showMessageDialog(this, "🎉 도착했습니다!", "게임 종료", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
