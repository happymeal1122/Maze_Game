import java.util.Random;

public class MazeGenerator {
    private final int rows, cols;
    private final int[][] maze;
    private final Random rand = new Random();
    private static final int WALL = 1;
    private static final int PATH = 0;

    private int playerRow = 1, playerCol = 1;  // 플레이어 시작 위치
    private int endRow, endCol;

    public MazeGenerator(int rows, int cols) {
        this.rows = rows % 2 == 0 ? rows + 1 : rows;
        this.cols = cols % 2 == 0 ? cols + 1 : cols;
        maze = new int[this.rows][this.cols];
        initializeMaze();
        generateMaze(1, 1);
        setExit(); // 도착점 설정
    }

    private void initializeMaze() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                maze[i][j] = WALL;
    }

    private void generateMaze(int r, int c) {
        maze[r][c] = PATH;
        int[][] dirs = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
        shuffle(dirs);

        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (inBounds(nr, nc) && maze[nr][nc] == WALL) {
                maze[(r + nr) / 2][(c + nc) / 2] = PATH;
                generateMaze(nr, nc);
            }
        }
    }

    private void setExit() {
        for (int i = rows - 2; i > 0; i--) {
            for (int j = cols - 2; j > 0; j--) {
                if (maze[i][j] == PATH) {
                    endRow = i;
                    endCol = j;
                    return;
                }
            }
        }
    }

    public void printMaze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == playerRow && j == playerCol)
                    System.out.print("@"); // 플레이어 위치
                else if (i == endRow && j == endCol)
                    System.out.print("E"); // 도착점
                else
                    System.out.print(maze[i][j] == WALL ? "█" : " ");
            }
            System.out.println();
        }
    }

    public boolean movePlayer(char direction) {
        int newRow = playerRow, newCol = playerCol;
        switch (Character.toUpperCase(direction)) {
            case 'W': newRow--; break;
            case 'S': newRow++; break;
            case 'A': newCol--; break;
            case 'D': newCol++; break;
            default: return false;
        }

        if (inBounds(newRow, newCol) && maze[newRow][newCol] == PATH) {
            playerRow = newRow;
            playerCol = newCol;
        }

        return hasReachedGoal(); // 도착 여부 반환
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    private void shuffle(int[][] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int[] tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    public boolean hasReachedGoal() {
        return playerRow == endRow && playerCol == endCol;
    }
}
