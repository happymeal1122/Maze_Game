

public class MazeAnalyzer {
    private final int[][] maze;

    public MazeAnalyzer(int[][] maze) {
        this.maze = maze;
    }

    public String getStructureType(int row, int col) {
        boolean up = isPath(row - 1, col);
        boolean down = isPath(row + 1, col);
        boolean left = isPath(row, col - 1);
        boolean right = isPath(row, col + 1);

        int openCount = (up ? 1 : 0) + (down ? 1 : 0) + (left ? 1 : 0) + (right ? 1 : 0);

        return switch (openCount) {
            case 1 -> "deadend";
            case 2 -> {
                if (up && down) yield "up";
                if (left && right) yield "left";  // or "right", same image assumed
                if (up && right) yield "corner_upleft";
                if (up && left) yield "corner_upright";
                if (down && left) yield "corner_downright";
                if (down && right) yield "corner_downleft";
                yield "T"; // fallback
            }
            case 3 -> "T";
            case 4 -> "plus";
            default -> "wall";
        };
    }

    private boolean isPath(int r, int c) {
        return r >= 0 && c >= 0 && r < maze.length && c < maze[0].length && maze[r][c] == 0;
    }
}
