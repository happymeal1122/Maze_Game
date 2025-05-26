import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MazeGame extends JPanel implements KeyListener {
    private final int rows = 25, cols = 25; // 정사각형 미로
    private final int cellSize = 25;
    private final int[][] maze = new int[rows][cols];
    private int playerRow = 1, playerCol = 1;
    private int endRow, endCol;

    // 방향 상태 추가
    private enum Direction { UP, DOWN, LEFT, RIGHT }
    private Direction playerDirection = Direction.DOWN;

    public MazeGame() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        generateMaze();
    }

    private void generateMaze() {
        for (int[] row : maze)
            java.util.Arrays.fill(row, 1); // 전체 벽 초기화
        dfsGenerate(1, 1);

        // 도착 지점 설정
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

        // 미로 그리기
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] == 1)
                    g.setColor(Color.BLACK); // 벽
                else
                    g.setColor(Color.WHITE); // 길
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }

        // 도착 지점 표시
        g.setColor(Color.GREEN);
        g.fillRect(endCol * cellSize, endRow * cellSize, cellSize, cellSize);

        // 회전 삼각형으로 플레이어 표시
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.BLUE);
        int cx = playerCol * cellSize + cellSize / 2;
        int cy = playerRow * cellSize + cellSize / 2;
        g2.translate(cx, cy);

        switch (playerDirection) {
            case UP:    g2.rotate(0); break;
            case RIGHT: g2.rotate(Math.PI / 2); break;
            case DOWN:  g2.rotate(Math.PI); break;
            case LEFT:  g2.rotate(-Math.PI / 2); break;
        }

        Polygon triangle = new Polygon(
            new int[] { 0, -8, 8 },
            new int[] { -10, 6, 6 },
            3
        );
        g2.fillPolygon(triangle);
        g2.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int newRow = playerRow, newCol = playerCol;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: newRow--; playerDirection = Direction.UP; break;
            case KeyEvent.VK_S: newRow++; playerDirection = Direction.DOWN; break;
            case KeyEvent.VK_A: newCol--; playerDirection = Direction.LEFT; break;
            case KeyEvent.VK_D: newCol++; playerDirection = Direction.RIGHT; break;
        }

        if (inBounds(newRow, newCol) && maze[newRow][newCol] == 0) {
            playerRow = newRow;
            playerCol = newCol;
            repaint();
        }

        if (playerRow == endRow && playerCol == endCol) {
            JOptionPane.showMessageDialog(this, "도착했습니다", "게임 종료", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
