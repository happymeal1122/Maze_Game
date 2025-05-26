import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MazeGame extends JPanel implements KeyListener {
	private final int rows, cols;
	private final int cellSize;
	private final int panelSize = 800;  // 🔒 패널 크기 고정
    private final int[][] maze;
    private int playerRow = 1, playerCol = 1;
    private int endRow, endCol;

    private enum Direction { UP, DOWN, LEFT, RIGHT }
    private Direction playerDirection = Direction.DOWN;

    public MazeGame(int rows, int cols) {
        this.rows = rows % 2 == 0 ? rows + 1 : rows;
        this.cols = cols % 2 == 0 ? cols + 1 : cols;

        //  미로 크기에 맞춰 cell 크기 자동 조절
        this.cellSize = Math.min(panelSize / this.rows, panelSize / this.cols);

        this.maze = new int[this.rows][this.cols];

        // 패널 크기 고정
        setPreferredSize(new Dimension(panelSize, panelSize));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        generateMaze();
    }


    private void generateMaze() {
        for (int[] row : maze)
            java.util.Arrays.fill(row, 1);
        dfsGenerate(1, 1);

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
                g.setColor(maze[r][c] == 1 ? Color.BLACK : Color.WHITE);
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }

        g.setColor(Color.GREEN);
        g.fillRect(endCol * cellSize, endRow * cellSize, cellSize, cellSize);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.BLUE);
        int cx = playerCol * cellSize + cellSize / 2;
        int cy = playerRow * cellSize + cellSize / 2;
        g2.translate(cx, cy);

        switch (playerDirection) {
            case UP: g2.rotate(0); break;
            case RIGHT: g2.rotate(Math.PI / 2); break;
            case DOWN: g2.rotate(Math.PI); break;
            case LEFT: g2.rotate(-Math.PI / 2); break;
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
            JOptionPane.showMessageDialog(this, "🎉 도착했습니다!", "게임 종료", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
