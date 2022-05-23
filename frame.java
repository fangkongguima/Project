import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

public class frame extends JFrame{

    boolean startGAME = false;

    public void initWindow() {
        this.setSize(750, 800);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Tetris");
    }
    public frame(){
        initWindow();
        JPanel jPanel = new JPanel(new GridLayout(4,1));
        JPanel jPanel1 = new JPanel();
        JLabel label1 = new JLabel("TETRIS");
        label1.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 60));
        label1.setForeground(Color.red);
        jPanel1.add(label1);

        JPanel jPanel2 = new JPanel();
        Button label2 = new Button("START NEW GAME");
        label2.setForeground(Color.black);
        label2.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 30));
        jPanel2.add(label2);

        JPanel jPanel3 = new JPanel();
        Button label3 = new Button("CHANGE LEVEL");
        label3.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 30));
        label3.setForeground(Color.black);
        jPanel3.add(label3);

        JPanel jPanel4 = new JPanel();
        Button label4 = new Button("CONTINUE");
        label4.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 30));
        label4.setForeground(Color.black);
        jPanel4.add(label4);

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
        jPanel.add(jPanel4);
        add(jPanel);
        label2.addActionListener(new Monitor());
        label3.addActionListener(new Monitor());
        label4.addActionListener(new Monitor());
    }


    public static void main (String[]args) {
        frame main = new frame();

//        Panel p = new Panel();
//        //Button b =new Button("START");
//        //Button c = new Button("change level");
//        p.add(new Button("START"));
//        p.add(new Button("change level"));
//
//        TextField tf = new TextField(20);
//        TextArea textArea = new TextArea(10, 40);
//
//        main.add(p);
//        //main.add(c);
//        main.add(tf);
//        main.add(textArea);
//
//        b.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.exit(0);
//            }
//        });
//
//
//        private void initMenu () {
//            jmb = new JMenuBar();
//            JMenu jm1 = new JMenu("game");
//            jm1.setFont(new Font("Calibri", Font.BOLD, 15));
//            JMenu jm2 = new JMenu("level");
//            jm2.setFont(new Font("Calibri", Font.BOLD, 15));
//            JMenu jm3 = new JMenu("help");
//            jm3.setFont(new Font("Calibri", Font.BOLD, 15));
//
//            JMenuItem jmi1 = new JMenuItem("Start New Game");
//            JMenuItem jmi2 = new JMenuItem(" Quit");
//            jmi1.setFont(new Font("Calibri", Font.BOLD, 15));
//            jmi2.setFont(new Font("Calibri", Font.BOLD, 15));
//
//            JMenuItem jmi3 = new JMenuItem("low");
//            JMenuItem jmi4 = new JMenuItem("middle");
//            JMenuItem jmi5 = new JMenuItem("high");
//            jmi3.setFont(new Font("Calibri", Font.BOLD, 15));
//            jmi4.setFont(new Font("Calibri", Font.BOLD, 15));
//            jmi5.setFont(new Font("Calibri", Font.BOLD, 15));
//
//            jm1.add(jmi1);
//            jm1.add(jmi2);
//            jm2.add(jmi3);
//            jm2.add(jmi4);
//            jm2.add(jmi5);
//
//            jmb.add(jm1);
//            jmb.add(jm2);
//            mainFrame.setJMenuBar(jmb);
//        }
    }

    public void setStartGAME(boolean startGAME) {
        this.startGAME = startGAME;
    }

    public boolean isStartGAME() {
        return startGAME;
    }


    private class Monitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == "START NEW GAME") {
//                System.out.println("START GAME is pressed");
                //在此添加开始游戏的方法
                setStartGAME(true);
            }
            else if (e.getActionCommand() == "CHANGE LEVEL") {
//                System.out.println("CHANGE LEVEL is pressed");
                //在此添加转换游戏难度
                String [] options = {"EASY","COMMON","HARD","HELL"};
                int n = JOptionPane.showOptionDialog(null,"PAUSED"," ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (n == 0) {
                    Tetris.setMillis(1000);
                }
                else if (n == 1) {
                    Tetris.setMillis(750);
                }
                else if (n == 2) {
                    Tetris.setMillis(500);
                }
                else if (n == 3) {
                    Tetris.setMillis(300);
                }
            }
            else if (e.getActionCommand() == "CONTINUE") {

            }
        }
    }
}

