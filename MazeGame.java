
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeGame extends JPanel {
    private final int[][] maze; // 실제 게임 중 사용하는 미로
    private final int[][] originalMaze; // MazeGenerator로부터 복사된 원본 미로
    private int playerRow = 1, playerCol = 1;
    private Direction playerDirection = Direction.DOWN;
    private final int endRow, endCol;
    private final int tileSize = 640;

    private boolean showMiniMap = false; // 미니맵 표시 여부를 결정하는 플래그

    public MazeGame(int[][] maze, int endRow, int endCol) {
        this.maze = maze;
        this.originalMaze = copyMaze(maze); // 미니맵 출력용 원본 미로 복사 저장
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
                    case KeyEvent.VK_M -> toggleMiniMap(); // m 키 입력 시 미니맵 상태 토글
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
        showMiniMap = !showMiniMap; // 미니맵 표시 상태를 전환
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
            drawMiniMap(g); // 미니맵 출력
        } else {
            drawFirstPersonView(g); // 1인칭 시점 이미지 출력
        }
    }

    private void drawFirstPersonView(Graphics g) {
        MazeAnalyzer analyzer = new MazeAnalyzer(maze);
        String structure = analyzer.getStructureType(playerRow, playerCol); // 현재 위치 기준 분석
        Image img = ImageUtil.getImage(structure + ".png");
        g.drawImage(img, 0, 0, tileSize, tileSize, null);
    }

    private void drawMiniMap(Graphics g) {
        int mapCellSize = Math.min(tileSize / originalMaze.length, tileSize / originalMaze[0].length);
        for (int r = 0; r < originalMaze.length; r++) {
            for (int c = 0; c < originalMaze[0].length; c++) {
                if (originalMaze[r][c] == 1) {
                    g.setColor(Color.DARK_GRAY); // 벽
                } else {
                    g.setColor(Color.WHITE); // 길
                }
                g.fillRect(c * mapCellSize, r * mapCellSize, mapCellSize, mapCellSize);
            }
        }

        g.setColor(Color.BLUE); // 도착 지점 표시
        g.fillRect(endCol * mapCellSize, endRow * mapCellSize, mapCellSize, mapCellSize);

        // 플레이어 방향 삼각형 표시
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        int cx = playerCol * mapCellSize + mapCellSize / 2;
        int cy = playerRow * mapCellSize + mapCellSize / 2;
        int size = (int)(mapCellSize * 0.9); // 삼각형 크기 확대

        Polygon triangle = new Polygon();
        triangle.addPoint(0, -size / 2); // 꼭짓점
        triangle.addPoint(-size / 3, size / 3); // 좌하단
        triangle.addPoint(size / 3, size / 3); // 우하단

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
