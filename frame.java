import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class frame extends JFrame {

    int gameRow = 25,gameColumn = 21;
    int[][] d = new int[gameRow][gameColumn];
    Color[][] color = new Color[gameRow][gameColumn];
    int rect,x,y,score;
    static boolean startGAME = false;

    public void initWindow() {
        this.setSize(750, 800);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Tetris");
    }

    public frame() {
        initWindow();
        JPanel jPanel = new JPanel(new GridLayout(4, 1));
        JPanel jPanel1 = new JPanel();
        JLabel label1 = new JLabel("TETRIS");
        label1.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 60));
        label1.setForeground(Color.red);
        jPanel1.add(label1);

        JPanel jPanel2 = new JPanel();
        Button label2 = new Button("START GAME");
        label2.setForeground(Color.black);
        label2.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 30));
        jPanel2.add(label2);

        JPanel jPanel3 = new JPanel();
        Button label3 = new Button("CHANGE LEVEL");
        label3.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 30));
        label3.setForeground(Color.black);
        jPanel3.add(label3);

//        ImageIcon imageIcon = new ImageIcon("Project Test\\src\\Tetris Start Image.png");
//        //要设置的背景图片
//        JLabel imageLabel = new JLabel(imageIcon);
//        //将背景图放在标签里。
//        this.getLayeredPane().add(imageLabel, new Integer(Integer.MIN_VALUE));
//        //将背景标签添加到frame的LayeredPane面板里。
//        imageLabel.setBounds(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
//        // 设置背景标签的位置
//        Container contain = this.getContentPane();
//        ((JPanel) contain).setOpaque(false);

        jPanel.add(jPanel1);
        jPanel.add(jPanel2);
        jPanel.add(jPanel3);
        add(jPanel);
        label2.addActionListener(new Monitor());
        label3.addActionListener(new Monitor());
    }


    public static void main(String[] args) {
        frame main = new frame();
    }

    public static void setStartGAME(boolean startGAME) {
        frame.startGAME = startGAME;
    }

    public boolean isStartGAME() {
        return startGAME;
    }



    private class Monitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == "START GAME") {
//                System.out.println("START GAME is pressed");
                //在此添加开始游戏的方法
                setStartGAME(true);
                setVisible(false);
            } else if (e.getActionCommand() == "CHANGE LEVEL") {
//                System.out.println("CHANGE LEVEL is pressed");
                //在此添加转换游戏难度
                String[] options = {"EASY", "COMMON", "HARD", "HELL"};
                int n = JOptionPane.showOptionDialog(null, "PAUSED", " ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    Tetris.setMillis(1000);
                } else if (n == 1) {
                    Tetris.setMillis(700);
                } else if (n == 2) {
                    Tetris.setMillis(400);
                } else if (n == 3) {
                    Tetris.setMillis(100);
                }
            }
        }
    }
}

