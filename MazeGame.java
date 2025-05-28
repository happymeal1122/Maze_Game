
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeGame extends JPanel {
    private final int[][] maze;
    private final int[][] originalMaze;
    private int playerRow = 1, playerCol = 1;
    private Direction playerDirection = Direction.DOWN;
    private final int endRow, endCol;
    private final int tileSize = 640;

    private boolean showMiniMap = false;

    public MazeGame(int[][] maze, int endRow, int endCol) {
        this.maze = maze;
        this.originalMaze = copyMaze(maze);
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
                    case KeyEvent.VK_M -> toggleMiniMap();
                }
                repaint();
                checkGoal();
            }
        });
    }

    private int[][] copyMaze(int[][] source) {
        int[][] copy = new int[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, source[i].length);
        }
        return copy;
    }

    private void toggleMiniMap() {
        showMiniMap = !showMiniMap;
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

        if (showMiniMap) {
            drawMiniMap(g);
        } else {
            drawFirstPersonView(g);
        }
    }

    private void drawFirstPersonView(Graphics g) {
        int frontRow = playerRow + playerDirection.dy;
        int frontCol = playerCol + playerDirection.dx;

        String structure;
        if (!isInBounds(frontRow, frontCol) || maze[frontRow][frontCol] == 1) {
            structure = "wall";
        } else {
            int backRow = frontRow - playerDirection.dy;
            int backCol = frontCol - playerDirection.dx;
            int leftRow = frontRow + playerDirection.left().dy;
            int leftCol = frontCol + playerDirection.left().dx;
            int rightRow = frontRow + playerDirection.right().dy;
            int rightCol = frontCol + playerDirection.right().dx;

            boolean front = isPath(frontRow + playerDirection.dy, frontCol + playerDirection.dx);
            boolean back = isPath(backRow, backCol);
            boolean left = isPath(leftRow, leftCol);
            boolean right = isPath(rightRow, rightCol);

            if (!front && !left && !right && back) structure = "deadend";
            else if (!front && left && !right && back) structure = "left";
            else if (!front && !left && right && back) structure = "right";
            else if (front && back && !left && !right) structure = "up";
            else if (front && back && left && !right) structure = "t_left";
            else if (front && back && right && !left) structure = "t_right";
            else if (back && left && right && !front) structure = "T";
            else if (front && back && left && right) structure = "+";
            else structure = "up";
        }

        Image img = ImageUtil.getImage(structure + ".png");

        if (img == null || img.getWidth(null) == -1) {
            System.out.println("⚠️ " + structure + ".png 로딩 실패 → wall.png로 대체");
            img = ImageUtil.getImage("wall.png");
        }

        if (img == null || img.getWidth(null) == -1) {
            System.out.println("❌ wall.png 로딩 실패 → 검은 화면 출력");
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, tileSize, tileSize);
        } else {
            g.drawImage(img, 0, 0, tileSize, tileSize, null);
        }
    }

    private boolean isInBounds(int r, int c) {
        return r >= 0 && c >= 0 && r < maze.length && c < maze[0].length;
    }

    private void drawMiniMap(Graphics g) {
        int mapCellSize = Math.min(tileSize / originalMaze.length, tileSize / originalMaze[0].length);
        for (int r = 0; r < originalMaze.length; r++) {
            for (int c = 0; c < originalMaze[0].length; c++) {
                g.setColor(originalMaze[r][c] == 1 ? Color.DARK_GRAY : Color.WHITE);
                g.fillRect(c * mapCellSize, r * mapCellSize, mapCellSize, mapCellSize);
            }
        }

        g.setColor(Color.BLUE);
        g.fillRect(endCol * mapCellSize, endRow * mapCellSize, mapCellSize, mapCellSize);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        int cx = playerCol * mapCellSize + mapCellSize / 2;
        int cy = playerRow * mapCellSize + mapCellSize / 2;
        int size = (int)(mapCellSize * 0.9);

        Polygon triangle = new Polygon();
        triangle.addPoint(0, -size / 2);
        triangle.addPoint(-size / 3, size / 3);
        triangle.addPoint(size / 3, size / 3);

        double angle = switch (playerDirection) {
            case UP -> 0;
            case RIGHT -> Math.PI / 2;
            case DOWN -> Math.PI;
            case LEFT -> -Math.PI / 2;
        };

        g2.translate(cx, cy);
        g2.rotate(angle);
        g2.fillPolygon(triangle);
        g2.rotate(-angle);
        g2.translate(-cx, -cy);
    }
}

enum Direction {
    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

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
