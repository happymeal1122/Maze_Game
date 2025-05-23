import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    private final int rows;
    private final int cols;
    private final int[][] maze;
    private final boolean[][] visited;
    private final Random rand = new Random();

    private static final int WALL = 1;
    private static final int PATH = 0;

    // 미로 크기를 지정할 수 있도록 수정
    public MazeGenerator(int rows, int cols) {
        this.rows = rows % 2 == 0 ? rows + 1 : rows; // 짝수면 홀수로 변환 (미로 구조 유지를 위해)
        this.cols = cols % 2 == 0 ? cols + 1 : cols;
        maze = new int[this.rows][this.cols];
        visited = new boolean[this.rows][this.cols];
        initializeMaze();
        generateMaze(1, 1); // 항상 내부에서 시작
    }

    private void initializeMaze() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                maze[r][c] = WALL;
    }

    private void generateMaze(int startRow, int startCol) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startRow, startCol});
        maze[startRow][startCol] = PATH;
        visited[startRow][startCol] = true;

        int[][] directions = {
            {-2, 0}, {2, 0}, {0, -2}, {0, 2}
        };

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int row = current[0];
            int col = current[1];

            boolean moved = false;
            shuffleArray(directions);

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (isValid(newRow, newCol)) {
                    maze[newRow][newCol] = PATH;
                    maze[(row + newRow) / 2][(col + newCol) / 2] = PATH; // 벽 제거
                    visited[newRow][newCol] = true;
                    stack.push(new int[]{newRow, newCol});
                    moved = true;
                    break;
                }
            }

            if (!moved) {
                stack.pop();
            }
        }
    }

    private boolean isValid(int r, int c) {
        return r > 0 && r < rows - 1 && c > 0 && c < cols - 1 && !visited[r][c];
    }

    // 방향 배열을 무작위로 섞기
    private void shuffleArray(int[][] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public void printMaze() {
        for (int[] row : maze) {
            for (int cell : row) {
                System.out.print(cell == WALL ? "█" : " "); // 벽 가시성 개선
            }
            System.out.println();
        }
    }

    public int[][] getMaze() {
        return maze;
    }
}
