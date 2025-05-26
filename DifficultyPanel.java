import javax.swing.*;
import java.awt.*;

public class DifficultyPanel extends JPanel {
    public DifficultyPanel(JFrame parentFrame) {
        setLayout(new GridLayout(4, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("난이도를 선택하세요", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(label);

        JButton easyBtn = new JButton("Easy (30x30)");
        JButton normalBtn = new JButton("Normal (50x50)");
        JButton hardBtn = new JButton("Hard (80x80)");

        easyBtn.addActionListener(e -> launchGame(parentFrame, 30, 30));
        normalBtn.addActionListener(e -> launchGame(parentFrame, 50, 50));
        hardBtn.addActionListener(e -> launchGame(parentFrame, 80, 80));

        add(easyBtn);
        add(normalBtn);
        add(hardBtn);
    }

    private void launchGame(JFrame frame, int rows, int cols) {
        frame.dispose(); // 난이도 선택 창 닫기
        JFrame gameFrame = new JFrame("Maze Game - " + rows + "x" + cols);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setContentPane(new MazeGame(rows, cols));
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
}
