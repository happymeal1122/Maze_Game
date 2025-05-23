public class Main {
    public static void main(String[] args) {
        // 미로 생성기 인스턴스 생성 (크기 지정 가능)
        MazeGenerator mg = new MazeGenerator(31, 31); // 홀수 크기 권장

        // 콘솔에 미로 출력 (벽은 █, 길은 공백으로 가시성 향상됨)
        mg.printMaze();
    }
}
