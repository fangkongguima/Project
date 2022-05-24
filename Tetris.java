import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Tetris extends JFrame implements KeyListener {
    //设置棋盘行列数，包括边界墙壁
    private static final int gameRow = 25;
    private static final int gameColumn = 15;
    //
    static JTextArea[][] text;
    static int[][] data;
    //定义游戏状态和游戏分数
    JLabel gameState;
    JLabel gameScore;
    static int millis = 1000;

    //定义初始量
    boolean isrunning;
    static boolean visible = false;
    int[] allRect;
    int rect;
    int time = 1000;
    int x;
    int y;
    int score = 0;
    boolean game_pause = false;
    int pause_times = 0;
    Color[][] color;
    boolean frame_on;

    //设置初始窗口
    public void initWindow() {
        //窗口大小
        this.setSize(750, 800);
        //窗口可视化
        this.setVisible(true);
        //窗口居中
        this.setLocationRelativeTo(null);
        //窗口可关闭
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口不可调整大小
        this.setResizable(false);
        //窗口命名
        this.setTitle("Tetris");
    }

    //设置游戏面板
    public void initGamePanel() {
        //定义新面板
        JPanel game_main = new JPanel();
        //画棋盘格子
        game_main.setLayout(new GridLayout(gameRow, gameColumn, 3, 3));
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                //游戏区域
                text[i][j] = new JTextArea(gameRow, gameColumn);
                text[i][j].setBackground(Color.GRAY);
                color[i][j] = Color.gray;
                text[i][j].addKeyListener(this);
                //左右下墙壁
                if (j == 0 || j == text[i].length - 1 || i == text.length - 1) {
                    text[i][j].setBackground(Color.black);
                    data[i][j] = 2;
                    color[i][j] = Color.black;
                }
                //上方墙壁
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

    //设置说明面板
    public void initExplainPanel() {
        //定义新面板
        JPanel explain_right = new JPanel();
        //说明行列数
        explain_right.setLayout(new GridLayout(9, 1));

        Button start = new Button("NEW GAME");
        start.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 24));
        start.setForeground(Color.red);
        explain_right.add(start);
        start.addActionListener(new Tetris.Monitor());

        Button load = new Button("LOAD");
        load.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 24));
        load.setForeground(Color.red);
        explain_right.add(load);
        load.addActionListener(new Tetris.Monitor());

        //显示游戏状态和游戏分数
        gameState.setForeground(Color.BLUE);
        gameScore.setForeground(Color.RED);
        explain_right.add(gameState);
        explain_right.add(gameScore);

        //显示游戏帮助
        explain_right.add(new JLabel(" HOW TO PLAY:"));
        explain_right.getComponent(4).setForeground(new Color(85,107,47));
        explain_right.getComponent(4).setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 22));
        explain_right.add(new JLabel(" Move Left: 'A' or 'LEFT'"));
        explain_right.add(new JLabel(" Move Right: 'D' or 'RIGHT'"));
        explain_right.add(new JLabel(" Move Down: 'S' or 'DOWN'"));
        explain_right.add(new JLabel(" Rotate Clockwise: 'W' or 'UP'"));
        for (int i = 4; i < 9; i++) {
            explain_right.getComponent(i).setForeground(new Color(199,21,133));
            explain_right.getComponent(i).setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 18));
        }
        //将说明面板加在右侧
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
        game_pause = true;

        //用十六进制表征所有形状，包括旋转后
        allRect = new int[]{
                //square
                0x0066, 0x0066, 0x0066, 0x0066,
                //line
                0x4444, 0x0f00, 0x4444, 0x0f00,
                //L
                0x0446, 0x00e8, 0x0c44, 0x02e0,
                //Mirrored-L
                0x0226, 0x0470, 0x0322, 0x0071,
                //Z
                0x0264, 0x00c6, 0x0264, 0x00c6,
                //S
                0x0462, 0x0036, 0x0462, 0x0036,
                //T
                0x0464, 0x00e4, 0x04c4, 0x04e0
        };
    }

    public static void main(String[] args) throws InterruptedException {
        frame frame = new frame();
        //设置音乐
        try {
            MusicPlayer player = new MusicPlayer("src\\Tetris Background Music.wav");
            player.setVolumn(6f).play();
            player.setLoop(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //开始游戏
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
            frame.setVisible(true);
            while (!frame.isStartGAME()) {
                tetris2.setVisible(false);
            }
            tetris2.setVisible(true);
            tetris2.game_begin();
            tetris2.dispose();
        }
    }

    public class Monitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == "NEW GAME") {
                //在此添加开始新游戏的方法
                game_pause = false;
            } else if (e.getActionCommand() == "LOAD") {
                if (game_pause) {
                    try {
                        load_panel();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    game_pause = false;
                }
            }
        }
    }

    //判断游戏是否开始
    public void game_begin() throws InterruptedException {
        //判断游戏是否进行
        while (true) {
            if (!isrunning) {
                break;
            }
            game_run();
        }
        //游戏失败
        gameState.setText("Game State: Game over");
        //失败窗口
        String [] options = {"RESTART","BACK TO MENU","QUIT THE GAME"};
        int n = JOptionPane.showOptionDialog(null,"YOUR SCORE: " + score," ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        //点击RESTART则重新开始
        if (n == 0) {
            //restart is realized in main
            visible = false;
        }
        //点击BACK TO MENU则回到菜单界面
        else if (n == 1){
            visible = true;
            frame.setStartGAME(false);
        }
        //点击QUIT THE GAME则关闭窗口
        else if (n == 2 || n == -1){
            System.exit(0);
        }
    }

    //随机生成不同形状方块
    public void ranRect() {
        Random random = new Random();
        rect = allRect[random.nextInt(28)];
    }

    //游戏进行
    public void game_run() throws InterruptedException {
        ranRect();
        x = 0;
        y = 5;
        for (int i = 0; i < gameRow; i++) {
            //方块下落速度
            Thread.sleep(millis);
            //如果游戏暂停，不下落
            if (game_pause) {
                i--;
            }
            //游戏进行
            else {
                //不能下落，则存储方块
                if (!canFall(x, y)) {
                    changeData(x, y);
                    //判断是否填满一整行
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
                    //判断是否游戏失败
                    for (int j = 1; j < (gameColumn - 1); j++) {
                        if (data[4][j] == 1) {
                            isrunning = false;
                            break;
                        }
                    }
                    break;
                }
                //方块正常掉落
                else {
                    x++;
                    fall(x, y);
                }
            }
        }
    }

    //判断方块是否能下落
    public boolean canFall(int m,int n) {
        //定义变量
        int temp = 0x8000;
        //遍历方块所在十六宫格
        a:for (int i=3;i>=0;i--){
            b:for (int j = 0; j < 4; j++){
            //判断此格有方块
                if ((temp&rect)!=0){
                    //判断此格下面是否有方块，如果下面已经有，则不能下落
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
        //方块可以下落
        return true;
    }

    //方块不能掉落后，存储方块数据
    public void changeData(int m,int n){
        //定义变量
        int temp = 0x8000;
        //存储方块颜色
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

        //遍历方块所在十六宫格，若有方块则存储数据
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

    //一层填满后消除
    public void removeRow(int m){
        //遍历填满后的行以及它上面的所有行
        for (int i=m; i>4;i--) {
            for (int j = 1; j < (gameColumn-1); j++) {
                //从下往上复制上一行的数据和颜色
                data[i][j]=data[i-1][j];
                color[i][j] = color[i-1][j];
            }
        }
        //刷新面板
        flash(m);
        //分数加100
        score = score + 100;
        //打印分数
        gameScore.setText("Game Score: " + score);
    }

    //刷新面板
    public void flash(int m){
        //遍历填满后的行以及它上面的所有行
        for (int i=m; i>3;i--) {
            for (int j = 0; j < (gameColumn-1); j++) {
                //重新打印颜色
                text[i][j].setBackground(color[i][j]);
            }
        }
    }

    //方块下落
    public void fall(int m, int n){
        //先清除当前所在位置
        if(m>0){
            clear(m-1,n);
        }
        //在下一行重新画出方块
        draw(m,n);
    }

    //清除方块
    public void clear(int m,int n){
        //定义变量
        int temp = 0x8000;
        //遍历方块所在十六宫格
        a:for (int i=0;i<4;i++){
            b:for (int j = 0; j < 4; j++){
            //方块所在位置颜色变为背景色
                if ((temp&rect)!=0&&m>3){
                    text[m][n].setBackground(Color.GRAY);
                    color[i][j] = Color.GRAY;
                }
                n++;
                temp >>=1;
            }
            m++;
            n=n-4;
        }
    }

    //画出方块
    public void draw(int m,int n){
        //定义变量
        int temp = 0x8000;
        //遍历方块所在十六宫格
        a:for (int i=3;i>=0;i--){
            b:for (int j = 0; j < 4; j++){
                //方块所在位置生成相应颜色
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
                    if (m>3) {
                        text[m][n].setBackground(color);
                    }
                }
                n++;
                temp >>=1;
            }
            m++;
            n=n-4;
        }
    }

    //判断方块是否能旋转
    public boolean canTurn(int a, int m, int n) {
        //定义变量
        int temp = 0x8000;
        //遍历方块所在十六宫格
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                //方块旋转后的位置中已经有方块，或者是墙壁，则不能旋转
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
        //否则旋转
        return true;
    }

    //暂停后，保存当前游戏
    public void save_panel() throws FileNotFoundException {
        //创建txt
        File file = new File("save_panel.txt");
        PrintWriter output = new PrintWriter(file);
        //遍历游戏棋盘
        for (int i=0;i<gameRow;i++) {
            for (int j = 0; j < gameColumn; j++) {
                //打印每格数据
                output.print(data[i][j]);
                output.print(' ');
            }
            output.println();
        }
        //用数字存储方块颜色
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
        //打印正在下落的方块形状
        output.println(rect);
        //打印正在下落的方块位置
        output.println(x+" "+ y);
        //打印当前游戏分数
        output.println(score);
        //打印当前游戏难度
        output.println(millis);
        output.close();
    }

    //恢复游戏后下载之前保存的游戏界面
    public void load_panel() throws FileNotFoundException {
        //调出txt
        File file = new File("save_panel.txt");
        Scanner input = new Scanner(file);
        int i=0, j=0;
        int t = 0;
        while(input.hasNext()){
            if (i < gameRow){
                //遍历游戏棋盘
                for (int l = 0;i<gameRow;i++){
                    for (int m = 0;j<gameColumn;j++){
                        //判断方格颜色
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
                if (t == 1) {
                    int a = input.nextInt();
                    String b = Integer.toHexString(a);
                    rect = Integer.parseInt(b);
                }
                if (t == 2){
                    x = input.nextInt();
                    y = input.nextInt();
                }
                if (t == 3) {
                    score = input.nextInt();
                }
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
            String [] options = {"RESUME","SAVE"};
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