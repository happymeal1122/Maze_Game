import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MazeGenerator maze = new MazeGenerator(21, 41); // 미로 크기 지정
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 미로 게임 시작! W/A/S/D 로 이동하세요 ===");

        while (true) {
            maze.printMaze();  // 미로 출력
            System.out.print("입력 > ");
            String input = scanner.nextLine();
            if (input.isEmpty()) continue;

            char command = input.charAt(0);
            boolean finished = maze.movePlayer(command); // 입력 처리

            if (finished) {
                maze.printMaze();
                System.out.println("도착했습니다! 게임 종료.");
                System.out.println("병합 표시용 코드");
                break;
            }
        }
    }
}
