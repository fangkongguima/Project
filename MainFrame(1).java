//import javafx.scene.media.AudioClip;
//import java.io.File;
//import javafx.scene.media.AudioClip;
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.ArrayList;
//
//public class MainFrame extends JFrame implements KeyListener {
//
//    //the game has 21 rows and 22 columns
//    private static final int gameRow = 21;
//    private static final int gameColumn = 22;
//    //create a textarea
//    private static JTextArea[][] text;
//    //create two-dimensional array
//    private int[][] data;
//    //storage all the shapes in the int[] array
//    //allShapes = new int[] {, };
//    //show the game state
//    private JLabel gameState;
//    //show the score
//    private JLabel gameScore;
//    //show the operation
//    private JLabel operation;
//    //judge running
//    private static boolean judgeRun;
//
//    public MainFrame () {
//        text = new JTextArea[gameRow][gameColumn];
//        data = new int[gameRow][gameColumn];
//        gameState = new JLabel("Game State: Playing");
//        gameScore = new JLabel("Game Score: 0 points");
////        add an image to the label
////        Icon icon = new ImageIcon("E:\\学习资料\\Java\\Java code\\exercise\\project\\src\\Tetris Start Image.png");
////        operation = new JLabel();
////        operation.setIcon(icon);
//        judgeRun = true;
//        initiatePanel();
//        initiateFrame();
//        initialExplanationPanel();
//    }
//
//    public void initiateFrame() {
//        //initiateFrame title
//        this.setTitle("Tetris");
//        //frame size
//        setSize(1500, 960);
//        //make the frame middle
//        this.setLocationRelativeTo(null);
//        //set to release the frame
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        //Set the window size to be variable
//        this.setResizable(false);
//        //make it visible
//        setVisible(true);
//    }
//
//    public void initialExplanationPanel() {
//        JPanel explanation_right = new JPanel();
//        explanation_right.setLayout(new GridLayout(4,1));
//        //add explanation on the right and left
//        JPanel explanation_left = new JPanel();
//        explanation_left.setLayout(new GridLayout(2,1));
//
////      add the control way on the right
//        explanation_right.add(new JLabel(" Move Left: 'A' or 'LEFT'"));
//        explanation_right.add(new JLabel(" Move Right: 'D' or 'RIGHT'"));
//        explanation_right.add(new JLabel(" Move Down: 'S' or 'DOWN'"));
//        explanation_right.add(new JLabel( " Rotate Clockwise: 'W' or 'UP'"));
////        explanation_right.add(operation);
//        explanation_left.setBackground(Color.BLACK);
//        explanation_right.setBackground(Color.BLACK);
//        for (int i = 0; i < 4; i++) {
//            explanation_right.getComponent(i).setForeground(Color.white);
//            explanation_right.getComponent(i).setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 16));
//        }
//
//        //set font color
//        gameScore.setForeground(Color.RED);
//        gameScore.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 20));
//        gameState.setForeground(Color.RED);
//        gameState.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 20));
//
//        //add gameState and gameScore to the left
//        explanation_left.add(gameScore);
//        explanation_left.add(gameState);
//
//        //add the explanation_left to the left of frame
//        this.add(explanation_left,BorderLayout.EAST);
//
//        //add the explanation_right to the right of frame
//        this.add(explanation_right,BorderLayout.WEST);
//    }
//
//    private void initiatePanel() {
//        //TODO: more about initiate
//        JPanel gameArea = new JPanel();
//        gameArea.setLayout(new GridLayout(gameRow, gameColumn, 1, 1));
//        //初始化游戏面板
//        for (int i = 0; i < text.length; i++) {
//            for (int j = 0; j < text[i].length; j++) {
//                //set the number of rows and columns of textArea
//                text[i][j] = new JTextArea(gameRow, gameColumn);
//                //set the color of background
//                text[i][j].setBackground(Color.white);
//                //add keyboard listening events
//                text[i][j].addKeyListener(this);
//                //initialize game boundaries
//                if (j == 0 || j == text[i].length-1 || i == text.length-1) {
//                    text[i][j].setBackground(Color.cyan);
//                    data[i][j] = 1;
//                }
//                //set the text field to uneditable
//                text[i][j].setEditable(false);
//                //add text to frame
//                gameArea.add(text[i][j]);
//            }
//        }
//        //add to frame
//        this.setLayout(new BorderLayout());
//        this.add(gameArea, BorderLayout.CENTER);
//    }
//
//    public static void main(String[] args) {
//        MainFrame mainFrame = new MainFrame();
//        String filepath = "E:\\学习资料\\Java\\Java code\\exercise\\project\\src\\Tetris Background Music.wav";
//        Music musicObject = new Music();
//        musicObject.playMusic(filepath);
//    }
//
//    public void gameStart() {
//        while (true) {
//            if (judgeRun) {
////                startRun();
//            }
//            else {
//                break;
//            }
//        }
//        gameState.setText("Game State: Game Over");
//        gameState.setFont(new Font ("Verdana", Font.BOLD + Font.PLAIN, 18));
//    }
//
////    public void generate() {
////        //random generate Shape
////        int number = (int)(Math.random()*7);
////        switch (number) {
////            case 0: return Square;
////            case 1: return Line;
////            case 2: return Tshape;
////            case 3: shapes.add(MLshape);return MLshape;
////            case 4: shapes.add(Lshape);return Lshape;
////            case 5: shapes.add(Sshape);return Sshape;
////            case 6: shapes.add(Zshape);return Zshape;
////            default: return null;
////        }
////    }
//
//
//
//    private void setPanel(JPanel panel1, JPanel panel2) {
//        panel1.setVisible(false);
//        add(panel2);
//        panel2.setVisible(true);
//        revalidate();
//        repaint();
//    }
//
//
////    private static JPanel initiatePanel(Tuple size, Color color) {
////        JPanel panel = new JPanel();
////        panel.setSize(size.x, size.y);
////        panel.setBackground(color);
////        //TODO: panel.setMore...
////        return panel;
////    }
//
//
//    @Override
//    public void keyTyped(KeyEvent e) {
//
//    }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//
//    }
//}
