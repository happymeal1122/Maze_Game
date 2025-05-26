import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeGame extends JPanel {
    private final int[][] maze;
    private int playerRow = 1, playerCol = 1;
    private Direction playerDirection = Direction.DOWN;
    private final int endRow, endCol;
    private final int tileSize = 640;

    public MazeGame(int[][] maze, int endRow, int endCol) {
        this.maze = maze;
        this.endRow = endRow;
        this.endCol = endCol;

        setPreferredSize(new Dimension(tileSize, tileSize));
        setFocusable(true);
        setBackground(Color.BLACK);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> moveForward();
                    case KeyEvent.VK_S -> moveBackward();
                    case KeyEvent.VK_A -> turnLeft();
                    case KeyEvent.VK_D -> turnRight();
                }
                repaint();
                checkGoal();
            }
        });
    }

    private void moveForward() {
        int r = playerRow + playerDirection.dy;
        int c = playerCol + playerDirection.dx;
        if (isPath(r, c)) {
            playerRow = r;
            playerCol = c;
        }
    }

    private void moveBackward() {
        int r = playerRow - playerDirection.dy;
        int c = playerCol - playerDirection.dx;
        if (isPath(r, c)) {
            playerRow = r;
            playerCol = c;
        }
    }

    private void turnLeft() {
        playerDirection = playerDirection.left();
    }

    private void turnRight() {
        playerDirection = playerDirection.right();
    }

    private boolean isPath(int r, int c) {
        return r >= 0 && c >= 0 && r < maze.length && c < maze[0].length && maze[r][c] == 0;
    }

    private void checkGoal() {
        if (playerRow == endRow && playerCol == endCol) {
            JOptionPane.showMessageDialog(this, "도착했습니다", "도착", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        MazeAnalyzer analyzer = new MazeAnalyzer(maze);
        String structure = analyzer.getStructureType(playerRow, playerCol); // 현재 위치 기준 분석
        Image img = ImageUtil.getImage(structure + ".png");
        g.drawImage(img, 0, 0, tileSize, tileSize, null);
    }
}

enum Direction {
    UP( -1,  0),
    RIGHT( 0,  1),
    DOWN( 1,  0),
    LEFT( 0, -1);

    public final int dy, dx;
    Direction(int dy, int dx) {
        this.dy = dy;
        this.dx = dx;
    }

    public Direction left() {
        return values()[(this.ordinal() + 3) % 4];
    }

    public Direction right() {
        return values()[(this.ordinal() + 1) % 4];
    }
}
