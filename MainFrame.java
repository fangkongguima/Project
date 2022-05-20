import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class MainFrame extends JFrame implements KeyListener {

    //the game has 21 rows and 26 columns
    private static final int gameRow = 21;
    private static final int gameColumn = 26;
    //create a textarea
    private static JTextArea[][] text;
    //create two-dimensional array
    private int[][] data;
    //storage all the shapes in the int[] array
    private final int shapes[][][] = new int[][][]{
            //4 kinds of Line T shape
            {
                    {0,1,0,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0},
                    {1,1,1,0, 0,1,0,0, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 0,1,1,0, 0,1,0,0, 0,0,0,0}
            },
            //4 kinds of Line shape
            {
                    {0,0,0,0, 1,1,1,1, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0},
                    {0,0,0,0, 1,1,1,1, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0}
            },
            //4 kinds of S shape
            {
                    {0,1,1,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,0,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0},
                    {0,1,1,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,0,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0}
            },
            //4 kinds of Z
            {
                    {1,1,0,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 1,1,0,0, 1,0,0,0, 0,0,0,0},
                    {1,1,0,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 1,1,0,0, 1,0,0,0, 0,0,0,0}
            },
            //4 kinds of ML shape
            {
                    {0,1,0,0, 0,1,0,0, 1,1,0,0, 0,0,0,0},
                    {1,1,1,0, 0,0,1,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 1,0,0,0, 1,0,0,0, 0,0,0,0},
                    {1,0,0,0, 1,1,1,0, 0,0,0,0, 0,0,0,0}
            },
            //4 kinds of L shape
            {
                    {1,0,0,0, 1,0,0,0, 1,1,0,0, 0,0,0,0},
                    {0,0,1,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 0,1,0,0, 0,1,0,0, 0,0,0,0},
                    {1,1,1,0, 1,0,0,0, 0,0,0,0, 0,0,0,0}
            },
            //4 kinds of square shape
            {
                    {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0}
            }
    };
    private int rowRect = 4;
    private int colRect = 4;
    private int RectWidth = 20;

    private Timer timer;
    private int score = 0;//记录成绩
    Random random = new Random();
    private int curShapeType = -1;
    private int curShapeState = -1;//设置当前的形状类型和当前的形状状态
    private int nextShapeType = -1;
    private int nextShapeState = -1;//设置下一次出现的方块组的类型和状态

    private int posX = 0;
    private int posY = 0;

    //record the pause times
    private int pauseTimes;
    //judge pause
    private boolean gamePause;
    //show the game state
    private JLabel gameState;
    //show the score
    private JLabel gameScore;
    //judge running
    private static boolean judgeRun;
    //储存方块变量
    int rect;

    public void CreateRect() //创建方块---如果当前的方块类型和状态都存在就设置下一次的，如果不存在就设置当前的并且设置下一次的状态和类型
    {
        if(curShapeType == -1 && curShapeState == -1)//当前的方块状态都为1，表示游戏才开始
        {
            curShapeType = random.nextInt(shapes.length);
            curShapeState = random.nextInt(shapes[0].length);
        }
        else
        {
            curShapeType = nextShapeType;
            curShapeState = nextShapeState;
        }
        nextShapeType = random.nextInt(shapes.length);
        nextShapeState = random.nextInt(shapes[0].length);
        posX = 0;
        posY = 1;//墙的左上角创建方块
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
//        String filepath = "project\\src\\Tetris Background Music.wav";
        Music musicObject = new Music();
        musicObject.playMusic("project\\src\\Tetris Background Music.wav");
    }

    public void initiateFrame() {
        //initiateFrame title
        this.setTitle("Tetris");
        //set to release the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set the window size to be variable
        this.setResizable(false);
        //frame size
        setSize(1200, 800);
        //make the frame middle
        this.setLocationRelativeTo(null);
        //close the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //make it visible
        setVisible(true);
    }

    public void initialExplanationPanel() {
        //create a label on the left to add explanation
//        JPanel explanation_left = new JPanel();
//        explanation_left.setLayout(new GridLayout(4,1));

        //create a label on the right to add explanation
        JPanel explanation_right = new JPanel();
        explanation_right.setLayout(new GridLayout(2,1));
        explanation_right.setBackground(Color.WHITE);

//      add the control way on the right
//        explanation_left.add(new JLabel(" Move Left: 'A' or 'LEFT'"));
//        explanation_left.add(new JLabel(" Move Right: 'D' or 'RIGHT'"));
//        explanation_left.add(new JLabel(" Move Down: 'S' or 'DOWN'"));
//        explanation_left.add(new JLabel( " Rotate Clockwise: 'W' or 'UP'"));

//        explanation_left.setBackground(Color.WHITE);

//        for (int i = 0; i < 4; i++) {
//            explanation_left.getComponent(i).setForeground(Color.white);
//            explanation_left.getComponent(i).setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 16));
//        }

//        explanation_right.add(operation);

        //set font color
        gameScore.setForeground(Color.RED);
        gameScore.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 20));
        gameState.setForeground(Color.RED);
        gameState.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 20));

        //add gameState and gameScore to the left
        explanation_right.add(gameScore);
        explanation_right.add(gameState);

        //add the explanation_left to the left of frame
//        this.add(explanation_left,BorderLayout.WEST);

        //add the explanation_right to the right of frame
        this.add(explanation_right,BorderLayout.EAST);
    }

    public MainFrame () {
        text = new JTextArea[gameRow][gameColumn];
        data = new int[gameRow][gameColumn];
        gameState = new JLabel("Game State: Playing");
        gameScore = new JLabel("Game Score: 0 points");
        judgeRun = true;
        initiatePanel();
        initiateFrame();
        initialExplanationPanel();
    }

    private void initiatePanel() {
        //TODO: more about initiate
        JPanel gameArea = new JPanel();
        gameArea.setLayout(new GridLayout(gameRow, gameColumn, 2, 2));
        //initiate the panel
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                //set the number of rows and columns of textArea
                text[i][j] = new JTextArea(gameRow, gameColumn);
                //set the color of background
                text[i][j].setBackground(Color.white);
                //add keyboard listening events
                text[i][j].addKeyListener(this);
                //initialize game boundaries
                if (j == 0 || j == text[i].length-1 || i == text.length-1) {
                    text[i][j].setBackground(Color.black);
                    data[i][j] = 1;
                }
                //set 4 rows on the top as black
                if (i == 0 || i ==1 || i ==2 || i ==3){
                    text[i][j].setBackground(Color.black);
                }
                //set the text field to uneditable
                text[i][j].setEditable(false);
                //add text to frame
                gameArea.add(text[i][j]);
            }
        }
        //add to frame
        this.setLayout(new BorderLayout());
        this.add(gameArea, BorderLayout.CENTER);
    }

    public void gameStart() {
        while (true) {
            if (judgeRun) {
//                startRun();
            }
            else {
                break;
            }
        }
        gameState.setText("Game State: Game Over");
        gameState.setFont(new Font ("Verdana", Font.BOLD + Font.PLAIN, 18));
    }

    private void setPanel(JPanel panel1, JPanel panel2) {
        panel1.setVisible(false);
        add(panel2);
        panel2.setVisible(true);
        revalidate();
        repaint();
    }

    /*  private static JPanel initiatePanel(Tuple size, Color color) {
        JPanel panel = new JPanel();
        panel.setSize(size.x, size.y);
        panel.setBackground(color);
        //TODO: panel.setMore...
        return panel;
    }     */

    @Override
    public void keyTyped(KeyEvent e) {

    }

        @Override
        public void keyPressed (KeyEvent e){
            //control the pause of the game
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_P) {
                //judge whether the game is running
                if (!judgeRun) {
                    return;
                }
                pauseTimes++;
                //judge the pauseTimes, if pauseTimes == 1, pause
                if (pauseTimes == 1) {
                    gamePause = true;
                    gameState.setText("Game State: Pausing");
                }
                //judge the pauseTimes, if pauseTimes == 2, restart
                if (pauseTimes == 2) {
                    gamePause = false;
                    pauseTimes = 0;
                    gameState.setText("Game State: Playing");
                }
            }
            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {

            }
            if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {

            }
            if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {

            }
            if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {

            }
            if (keyCode == KeyEvent.VK_ENTER) {
                CreateRect();
            }
        }

        @Override
        public void keyReleased (KeyEvent e){

        }
}