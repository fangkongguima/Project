import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Tetris extends JFrame implements KeyListener {

    private static final int gameRow = 25;
    private static final int gameColumn = 15;

    static JTextArea[][] text;
    static int[][] data;

    JLabel gameState;
    JLabel gameScore;

    boolean isrunning;
    int[] allRect;
    int rect;
    int x;
    int y;
    int score = 0;
    boolean game_pause = false;
    int pause_times = 0;
    Color[][] color;
    static int millis = 1000;

    public void initWindow() {
        this.setSize(750, 800);
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
                color[i][j] = Color.gray;
                text[i][j].addKeyListener(this);
                if (j == 0 || j == text[i].length - 1 || i == text.length - 1) {
                    text[i][j].setBackground(Color.black);
                    data[i][j] = 2;
                    color[i][j] = Color.black;
                }
                if (i == 0 || i == 1 || i == 2 || i == 3) {
                    text[i][j].setBackground(Color.black);
                    color[i][j] = Color.black;
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
        explain_right.getComponent(3).setForeground(new Color(55, 47, 107));
        explain_right.getComponent(3).setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 22));
        explain_right.add(new JLabel(" Press P to Pause"));
        explain_right.getComponent(4).setForeground(new Color(85,107,47));
        explain_right.getComponent(4).setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 18));
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

    public Tetris() {
        text = new JTextArea[gameRow][gameColumn];
        data = new int[gameRow][gameColumn];
        color = new Color[gameRow][gameColumn];
        gameState = new JLabel("Game State: Playing");
        gameState.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 22));
        gameScore = new JLabel("Game Score: " + score);
        gameScore.setFont(new Font ("Verdana", Font.BOLD+ Font.ITALIC, 22));
        initGamePanel();
        initExplainPanel();
        initWindow();
        isrunning = true;
        allRect = new int[]{
                0x0066, 0x0066, 0x0066, 0x0066,
                0x4444, 0x0f00, 0x4444, 0x0f00,
                0x0446, 0x00e8, 0x0c44, 0x02e0,
                0x0226, 0x0470, 0x0322, 0x0071,
                0x0264, 0x00c6, 0x0264, 0x00c6,
                0x0462, 0x0036, 0x0462, 0x0036,
                0x0464, 0x00e4, 0x04c4, 0x04e0
        };
    }

    public static void main(String[] args) throws InterruptedException {
        frame frame = new frame();
        try {
            MusicPlayer player = new MusicPlayer("src\\Tetris Background Music.wav");
            player.setVolumn(6f).play();
            player.setLoop(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tetris tetris = new Tetris();
        while (!frame.isStartGAME()) {
            tetris.setVisible(false);
        }
        frame.setVisible(false);
        tetris.setVisible(true);
        tetris.game_begin();
        while (true) {
            tetris.dispose();
            Tetris tetris2 = new Tetris();
            tetris2.game_begin();
        }
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
            //restart is realized in main
        }
        else if (n == 1){

        }
        else if (n == 2 || n == -1){
            System.exit(0);
        }
    }

    public void ranRect() {
        Random random = new Random();
        rect = allRect[random.nextInt(28)];
    }

    public void game_run() throws InterruptedException {
        ranRect();
        x = 0;
        y = 5;
        for (int i = 0; i < gameRow; i++) {
            Thread.sleep(millis);
            if (game_pause) {
                i--;
            } else {
                if (!canFall(x, y)) {
                    changeData(x, y);
                    for (int j = x; j < Math.min(x + 4,gameRow-1); j++) {
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
                    if (data[m+1][n] == 1||data[m+1][n] == 2) {
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
        Color t = Color.red;
        if (rect == 0x0066) {
            t = Color.red;
        } else if (rect == 0x4444||rect==0x0f00) {
            t = Color.cyan;
        } else if (rect == 0x0446||rect==0x00e8||rect==0x0c44||rect==0x02e0) {
            t = Color.green;
        }
        else if (rect == 0x0226||rect==0x0470||rect==0x0322||rect==0x0071) {
            t = Color.blue;
        }
        else if (rect == 0x0264||rect==0x00c6) {
            t = Color.orange;
        }
        else if (rect == 0x0462||rect==0x0036) {
            t = Color.yellow;
        }
        else if (rect == 0x0464||rect==0x00e4||rect==0x04c4||rect==0x04e0){
            t = Color.magenta;
        }

        a:for (int i=3;i>=0;i--){
            b:for (int j = 0; j < 4; j++){
                if ((temp&rect)!=0){
                    data[m][n] = 1;
                    color[m][n] = t;
                }
                n++;
                temp >>=1;
            }
            m++;
            n=n-4;
        }
    }

    public void removeRow(int m){
        for (int i=m; i>4;i--) {
            for (int j = 1; j < (gameColumn-1); j++) {
                data[i][j]=data[i-1][j];
                color[i][j] = color[i-1][j];
            }
        }
        flash(m);
        score = score + 100;
        gameScore.setText("Game Score: " + score);
    }

    public void flash(int m){
        for (int i=m; i>3;i--) {
            for (int j = 0; j < (gameColumn-1); j++) {
                /*if (data[i][j] == 0){
                    text[i][j].setBackground(Color.GRAY);
                }*/
                text[i][j].setBackground(color[i][j]);
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
                    color[i][j] = Color.GRAY;
                }
                else if((temp&rect)!=0&&m<4){
                    text[m][n].setBackground(Color.black);
                    color[i][j] = Color.black;
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
                    else if (rect == 0x4444||rect==0x0f00) {
                        color = Color.cyan;
                    }
                    else if (rect == 0x0446||rect==0x00e8||rect==0x0c44||rect==0x02e0) {
                        color = Color.green;
                    }
                    else if (rect == 0x0226||rect==0x0470||rect==0x0322||rect==0x0071) {
                        color = Color.blue;
                    }
                    else if (rect == 0x0264||rect==0x00c6) {
                        color = Color.orange;
                    }
                    else if (rect == 0x0462||rect==0x0036) {
                        color = Color.yellow;
                    }
                    else if (rect == 0x0464||rect==0x00e4||rect==0x04c4||rect==0x04e0){
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

    public boolean canTurn(int a, int m, int n) {
        //create temp
        int temp = 0x8000;
        //for each to loop all
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((a & temp) != 0) {
                    if (data[m][n] == 1||data[m][n]==2) {
                        return false;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n -4;
        }
        //if can turn
        return true;
    }

    public void save_panel() throws FileNotFoundException {
        File file = new File("save_panel.txt");
        PrintWriter output = new PrintWriter(file);
        for (int i=0;i<gameRow;i++) {
            for (int j = 0; j < gameColumn; j++) {
                output.print(data[i][j]);
                output.print(' ');

            }
            output.println();
        }
        for (int i = 0;i<gameRow;i++){
            for (int j = 0;j<gameColumn;j++){
                if (Color.gray.equals(color[i][j])) {
                    output.print(0);
                } else if (Color.black.equals(color[i][j])) {
                    output.print(1);
                } else if (Color.red.equals(color[i][j])) {
                    output.print(2);
                } else if (Color.cyan.equals(color[i][j])) {
                    output.print(3);
                } else if (Color.green.equals(color[i][j])) {
                    output.print(4);
                } else if (Color.blue.equals(color[i][j])) {
                    output.print(5);
                } else if (Color.orange.equals(color[i][j])) {
                    output.print(6);
                } else if (Color.yellow.equals(color[i][j])) {
                    output.print(7);
                } else if (Color.magenta.equals(color[i][j])) {
                    output.print(8);
                }
                output.print(' ');
            }
            output.println();
        }

        output.println(rect);
        output.println(x+' '+ y);
        output.println(score);
        output.close();
        //output.println(speed);
    }

    public void load_panel() throws FileNotFoundException {
        File file = new File("save_panel.txt");
        Scanner input = new Scanner(file);
        int i=0, j=0;
        int t = 0;
        while(input.hasNext()){
            if (i < gameRow){
                // Color类存入txt时是什么格式？如何读取txt中的Color类？
                for (int l = 0;i<gameRow;i++){
                    for (int m = 0;j<gameColumn;j++){
                        t = input.nextInt();
                        switch (t){
                            case 0: color[i][j] = Color.gray;
                            case 1: color[i][j] = Color.black;
                            case 2: color[i][j] = Color.red;
                            case 3: color[i][j] = Color.cyan;
                            case 4: color[i][j] = Color.green;
                            case 5: color[i][j] = Color.blue;
                            case 6: color[i][j] = Color.orange;
                            case 7: color[i][j] = Color.yellow;
                            case 8: color[i][j] = Color.magenta;
                        }
                    }
                }
                if (j++ == gameColumn){
                    j--;
                    i++;
                }
            }
            else {
                if (t == 0){
                    i = 0;
                    j = 0;
                }
                //检测这里是否是16进制
                if (t == 1) {
                    rect = input.nextInt();
                }
                if (t == 2){
                    x = input.nextInt();
                    y = input.nextInt();
                }
                if (t == 3) {
                    score = input.nextInt();
                }
                //if (t == 4) speed = input.nextInt();
                t++;
            }
        }
        input.close();
    }

    public static void setMillis(int millis) {
        Tetris.millis = millis;
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
                        if(data[i][j-1]==1||data[i][j-1]==2){
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
                        if (data[i][j + 1] == 1||data[i][j+1]==2) {
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
            else if (n == 1) {
                try {
                    save_panel();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            else if (n == 2) {
                //add back to menu
            }
        }

        if (e.getKeyCode()==38||e.getKeyCode()==87){
            if (!isrunning){
                return;
            }
            int old;
            for (old=0; old<allRect.length; old++){
                if (rect==allRect[old]){
                    break;
                }
            }
            int next;
            clear(x,y);
            for (int i=0; i<28; i++){
                if (old == i){
                    if (i%4==3){
                        next = allRect[i-3];
                        if (canTurn(next, x, y)) {
                            rect = next;
                        }
                    }
                    else {
                        next = allRect[i+1];
                        if (canTurn(next, x, y)) {
                            rect = next;
                        }
                    }
                    break;
                }
            }
            draw(x,y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}