import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MazeGame extends JPanel implements KeyListener {

    private final int rows = 21, cols = 21;
    private final int cellSize = 25;
    private final int[][] maze = new int[rows][cols];

    private int playerRow = 1, playerCol = 1; // 🔹 플레이어 위치
    private int endRow, endCol;

    public MazeGame() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.WHITE);
        initializeMaze();
        dfsGenerate(1, 1);
        setEndPoint();
        setFocusable(true);
        addKeyListener(this); // 🔹 키 입력 처리
    }

    private void initializeMaze() {
        for (int[] row : maze) Arrays.fill(row, 1);
    }

    private void dfsGenerate(int r, int c) {
        maze[r][c] = 0;

        List<int[]> dirs = new ArrayList<>();
        dirs.add(new int[]{-2, 0});
        dirs.add(new int[]{2, 0});
        dirs.add(new int[]{0, -2});
        dirs.add(new int[]{0, 2});
        Collections.shuffle(dirs);


        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (inBounds(nr, nc) && maze[nr][nc] == 1) {
                maze[r + d[0] / 2][c + d[1] / 2] = 0;
                dfsGenerate(nr, nc);
            }
        }
    }


    private boolean inBounds(int r, int c) {
        return r > 0 && c > 0 && r < rows && c < cols;
    }

    private void setEndPoint() {
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

        // 미로 그리기
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] == 1) g.setColor(Color.BLACK);
                else g.setColor(Color.WHITE);
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }

        // 도착지점
        g.setColor(Color.GREEN);
        g.fillRect(endCol * cellSize, endRow * cellSize, cellSize, cellSize);

        // 플레이어
        g.setColor(Color.BLUE);
        g.fillOval(playerCol * cellSize, playerRow * cellSize, cellSize, cellSize);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int newRow = playerRow, newCol = playerCol;

        switch (key) {
            case KeyEvent.VK_W -> newRow--;
            case KeyEvent.VK_S -> newRow++;
            case KeyEvent.VK_A -> newCol--;
            case KeyEvent.VK_D -> newCol++;
        }

        if (inBounds(newRow, newCol) && maze[newRow][newCol] == 0) {
            playerRow = newRow;
            playerCol = newCol;
            repaint();
            
            // 도착 검사 추가
            if (playerRow == endRow && playerCol == endCol) {
                JOptionPane.showMessageDialog(this, "도착했습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0); // 게임 종료
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {
    	if (playerRow == endRow && playerCol == endCol) {
    	    JOptionPane.showMessageDialog(this, "도착했습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
    	    System.exit(0); // 또는 게임 재시작 로직으로 교체 가능
    	}
    }
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Step 5: 플레이어 이동");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MazeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
}
