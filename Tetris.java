import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class Tetris extends JFrame implements KeyListener {

    private static final int gameRow = 25;
    private static final int gameColumn = 15;

    JTextArea[][] text;
    int[][] data;

    JLabel gameState;
    JLabel gameScore;

    boolean isrunning;
    int[] allRect;
    int rect;
    int time = 1000;
    int x;
    int y;
    int score = 0;
    boolean game_pause = false;
    int pause_times = 0;


    public void initWindow() {
        this.setSize(880, 1000);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Tetris");



       /*
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int height = this.getHeight();
        int width = this.getWidth();
        setLocation(screenWidth/2-width/2,screenHeight/2-height/2);
        */


        //close the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //make it visible
    }

    public void initGamePanel() {
        JPanel game_main = new JPanel();
        game_main.setLayout(new GridLayout(gameRow, gameColumn, 3, 3));
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                text[i][j] = new JTextArea(gameRow, gameColumn);
                text[i][j].setBackground(Color.GRAY);
                text[i][j].addKeyListener(this);
                if (j == 0 || j == text[i].length - 1 || i == text.length - 1) {
                    text[i][j].setBackground(Color.black);
                    data[i][j] = 1;
                }
                if (i == 0 || i == 1 || i == 2 || i == 3) {
                    text[i][j].setBackground(Color.black);
                }
                text[i][j].setEditable(false);
                game_main.add(text[i][j]);
            }
        }
        this.setLayout(new BorderLayout());
        this.add(game_main, BorderLayout.CENTER);
    }

    public void initExplainPanel() {
        JPanel explain_right = new JPanel();
        explain_right.setLayout(new GridLayout(10, 1));

        gameState.setForeground(Color.BLUE);
        gameScore.setForeground(Color.RED);
        explain_right.add(new JLabel(" "));
        explain_right.add(gameState);
        explain_right.add(gameScore);

        explain_right.add(new JLabel(" "));
        explain_right.add(new JLabel(" HOW TO PLAY:"));
        explain_right.getComponent(4).setForeground(new Color(85,107,47));
        explain_right.getComponent(4).setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 22));
        explain_right.add(new JLabel(" Move Left: 'A' or 'LEFT'"));
        explain_right.add(new JLabel(" Move Right: 'D' or 'RIGHT'"));
        explain_right.add(new JLabel(" Move Down: 'S' or 'DOWN'"));
        explain_right.add(new JLabel(" Rotate Clockwise: 'W' or 'UP'"));
        for (int i = 5; i < 9; i++) {
            explain_right.getComponent(i).setForeground(new Color(199,21,133));
            explain_right.getComponent(i).setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 18));
        }
        this.add(explain_right, BorderLayout.EAST);
    }


//    public void initStartGame() {
//        JPanel StartPanel = new JPanel();
//        JButton button = new JButton("选择文件");
//        // 监听button的选择路径
//        button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        StartPanel.add(button);
//    }

    public Tetris() {
        text = new JTextArea[gameRow][gameColumn];
        data = new int[gameRow][gameColumn];
        gameState = new JLabel("Game State: Playing");
        gameState.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 22));
        gameScore = new JLabel("Game Score: " + score);
        gameScore.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 22));
        initGamePanel();
        initExplainPanel();
        initWindow();
        isrunning = true;
        allRect = new int[]{0x0066, 0x4444, 0x0446, 0x0226, 0x0264, 0x0462, 0x0464};
    }

    public static void main(String[] args) throws InterruptedException {
        Tetris tetris = new Tetris();
        try {
            MusicPlayer player = new MusicPlayer("src\\Tetris Background Music.wav");
            player.setVolumn(6f).play();
            player.setLoop(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tetris.game_begin();
    }

    public void game_begin() throws InterruptedException {
        while (true) {
            if (!isrunning) {
                break;
            }
            game_run();
        }
        gameState.setText("Game State: Game over");
        String [] options = {"RESTART","BACK TO MENU","QUIT THE GAME"};
        int n = JOptionPane.showOptionDialog(null,"YOUR SCORE: " + score," ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        if (n == 0) {
            isrunning = true;
        }
        else if (n == 1){

        }
        else if (n == 2 || n == -1){

        }
    }

    public void ranRect() {
        Random random = new Random();
        rect = allRect[random.nextInt(7)];
    }

    public void game_run() throws InterruptedException {
        ranRect();
        x = 0;
        y = 5;
        for (int i = 0; i < gameRow; i++) {
            Thread.sleep(time);
            if (game_pause) {
                i--;
            } else {
                if (!canFall(x, y)) {
                    changeData(x, y);
                    for (int j = x; j < x + 4; j++) {
                        int sum = 0;

                        for (int k = 1; k < (gameColumn - 1); k++){
                            if (data[j][k] == 1) {
                                sum++;
                            }
                        }

                        if (sum == gameColumn - 2) {
                            removeRow(j);
                        }
                    }
                    for (int j = 1; j < (gameColumn - 1); j++) {
                        if (data[4][j] == 1) {
                            isrunning = false;
                            break;
                        }
                    }
                    break;
                } else {
                    x++;
                    fall(x, y);
                }
            }
        }
    }

    public boolean canFall(int m,int n) {
        int temp = 0x8000;
        a:for (int i=3;i>=0;i--){
            b:for (int j = 0; j < 4; j++){
                if ((temp&rect)!=0){
                    if (data[m+1][n] == 1) {
                        return false;
                    }
                }
                n++;
                temp >>=1;
            }
            m++;
            n=n-4;
        }
        return true;
    }

    public void changeData(int m,int n){
        int temp = 0x8000;
        a:for (int i=3;i>=0;i--){
            b:for (int j = 0; j < 4; j++){
                if ((temp&rect)!=0){
                    data[m][n] = 1;
                }
                n++;
                temp >>=1;
            }
            m++;
            n=n-4;
        }
    }

    public void removeRow(int m){
        for (int i=m; i>0;i--) {
            for (int j = 1; j < (gameColumn-1); j++) {
                data[i][j]=data[i-1][j];
//                text[i][j].getBackground() == text[i-1][j].getBackground();
            }
        }
        flash(m);
        score = score + 100;
        gameScore.setText("Game Score: " + score);
    }

    public void flash(int m){
        for (int i=m; i>3;i--) {
            for (int j = 0; j < (gameColumn-1); j++) {
                if (data[i][j] == 0){
                    text[i][j].setBackground(Color.GRAY);
                }
                else if (data[i][j] == 2) {
                    text[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    public void fall(int m, int n){
        if(m>0){
            clear(m-1,n);
        }
        draw(m,n);
    }

    public void clear(int m,int n){
        int temp = 0x8000;
        a:for (int i=0;i<4;i++){
            b:for (int j = 0; j < 4; j++){
                if ((temp&rect)!=0&&m>3){
                    text[m][n].setBackground(Color.GRAY);
                }
                else if((temp&rect)!=0&&m<4){
                    text[m][n].setBackground(Color.black);
                }
                n++;
                temp >>=1;
            }
            m++;
            n=n-4;
        }
    }

    public void draw(int m,int n){
        int temp = 0x8000;
        a:for (int i=3;i>=0;i--){
            b:for (int j = 0; j < 4; j++){
                if ((temp&rect)!=0){
                    Color color = Color.red;
                    if (rect == 0x0066){
                        color = Color.red;
                    }
                    else if (rect == 0x4444) {
                        color = Color.cyan;
                    }
                    else if (rect == 0x0446) {
                        color = Color.green;
                    }
                    else if (rect == 0x0226) {
                        color = Color.blue;
                    }
                    else if (rect == 0x0264) {
                        color = Color.orange;
                    }
                    else if (rect == 0x0462) {
                        color = Color.yellow;
                    }
                    else if (rect == 0x0464){
                        color = Color.magenta;
                    }
                    text[m][n].setBackground(color);
                }
                n++;
                temp >>=1;
            }
            m++;
            n=n-4;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==37||e.getKeyCode()==65){
            if (!isrunning||x<3||game_pause){
                return;
            }
            int temp = 0x8000;
            a:for (int i=x;i<x+4;i++){
                b:for (int j=y; j<y+4; j++){
                    if ((temp&rect)!=0){
                        if(data[i][j-1]==1){
                            return;
                        }
                    }
                    temp >>=1;
                }
            }
            clear(x,y);
            y--;
            draw(x,y);
        }
        if (e.getKeyCode()==39||e.getKeyCode()==68) {
            if (!isrunning||x<3||game_pause) {
                return;
            }
            int temp = 0x8000;
            a:
            for (int i = x; i < x + 4; i++) {
                b:
                for (int j = y; j < y + 4; j++) {
                    if ((temp & rect) != 0) {
                        if (data[i][j + 1] == 1) {
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }
            clear(x, y);
            y++;
            draw(x, y);
        }

        if (e.getKeyCode()==40||e.getKeyCode()==83) {
            if (!isrunning||game_pause) {
                return;
            }
            if (!canFall(x,y)){
                return;
            }
            clear(x, y);
            x++;
            draw(x, y);
        }

        if (e.getKeyCode()==KeyEvent.VK_P){
            if (!isrunning){
                return;
            }
            pause_times++;
            game_pause = true;
            gameState.setText("Game State: Pause");
            String [] options = {"RESUME","SAVE AND LOAD","BACK TO MENU"};
            int n = JOptionPane.showOptionDialog(null,"PAUSED"," ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if (n == 0 || n == -1) {
                game_pause = false;
                gameState.setText("Game State: Playing");
            }
            else if (n == 1){

            }
            else if (n == 2){

            }
        }

        if (e.getKeyCode()==38||e.getKeyCode()==87){
            if (!isrunning){
                return;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}