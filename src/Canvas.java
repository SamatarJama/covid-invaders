
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.event.*;
import java.util.List;
import java.util.Scanner;

import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;


public class Canvas  extends JPanel implements Runnable
{
    // make the size of the frame into variables
    int CANVAS_WIDTH=750;
    int CANVAS_HEIGHT=750;
    //the boolean to check if the game is still going
    boolean ingame = true;
    private Dimension d;
    //the score tracker of the whole game
    int score = 0;
    //loads all the images that are used
    final BufferedImage Shots = ImageIO.read(new File("Shots.png"));
    final BufferedImage covidImage = ImageIO.read(new File("alien.png"));
    final BufferedImage handSan = ImageIO.read(new File("hand-san.png"));
    final BufferedImage gameOver = ImageIO.read(new File("game-over.png"));
    private Thread animator;
    //initialize my objects that i am going to use
    Player[] p = new Player[1];
    ArrayList<Covid> c = new ArrayList();
    ArrayList<Shots> shots = new ArrayList();
    int time= 0;
    int timeTwo = 0;
    int covidTimer = 1500;


    public Canvas() throws IOException {
        addKeyListener(new KeyboardListener(this));
        addMouseListener(new MouseClickListener(this));
        setFocusable(true);
        d = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
        p[0] = new Player(CANVAS_WIDTH/2, CANVAS_HEIGHT-80, 5);
        int cx = 10;
        int cy = 10;
            for (int i = 0; i < 10; i++) {
                c.add( new Covid(cx, cy, 6));
                    cx += 40;
                    if (i == 4) {
                        cx = 10;
                        cy += 40;
                    }
            }

        setBackground(Color.black);


        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }

        setDoubleBuffered(true);

    }


    public void paint(Graphics g)
    {
        super.paint(g);
        if (ingame) {
            //all the variables that increase for every repaint
            time++;
            timeTwo++;
            score++;
        }

        int cx= 10;
        int cy = 10;
        //creates multiple covids with the usage of a timer to increase the difficulty over time, but only if ingame is true
        if (ingame) {
            while (timeTwo == covidTimer) {
                covidTimer -= 100;
                for (int i = 0; i < 10; i++) {
                    timeTwo = 0;
                    c.add(new Covid(cx, cy, 6));
                    cx += 40;
                    if (i == 4) {
                        cx = 10;
                        cy += 40;
                    }
                }
            }
        }

        //change color and add a background
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, d.width, d.height);

        //draws out the player
        g.drawImage(handSan, p[0].x, p[0].y, 60, 60, null);

        //checks if the moveright is true and then moves the hand sanitizer
        if(p[0].moveRight==true)
            p[0].x+= p[0].velocity;

        //checks if the moveleft is true and then moves the hand sanitizer
        if(p[0].moveLeft==true)
            p[0].x-= p[0].velocity;

        for (int i=0; i<shots.size();i++) {
            if (shots.get(i).moveUp == true)
                shots.get(i).y -= shots.get(i).velocity;

        }
        //calling important functions for the game
        collision();
        moveCovid();

        // creates the covid and positions them with a loop
        for(int i=0; i<c.size();i++){
                g.drawImage(covidImage, c.get(i).x, c.get(i).y, 30, 30, null);
        }
        // creates the shots and positions them with a loop
        for (int i=0; i<shots.size();i++) {
                g.drawImage(Shots, shots.get(i).x, shots.get(i).y, 10, 20, null);
        }

        //alters the style of the font and displays in the game
        Font font = new Font("Ariel", Font.BOLD, 16);
        FontMetrics metrics = this.getFontMetrics(font);
        g.setColor(Color.black);
        g.setFont(font);
        g.drawString(String.valueOf(score), 10, d.height-60);

        //checks for if the inGame variable is true the draws the game over image
        if (!ingame) {
            g.drawImage(gameOver, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
            //new string for the score
            Font font2 = new Font("Ariel", Font.BOLD, 16);
            FontMetrics metrics2 = this.getFontMetrics(font2);
            g.setColor(Color.white);
            g.setFont(font2);
            g.drawString("Score: " + String.valueOf(score), 10, d.height - 60);

        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void moveCovid() {
        //checks if the covid should move to the left, if so it will move it with the speed of it's velocity
        for (int i = 0; i < c.size(); i++) {
            if (c.get(i).moveLeft == true) {
                c.get(i).x -= c.get(i).velocity / 1.5;
            }
            //checks if the covid should move to the right, if so it will move it with the speed of it's velocity
            if (c.get(i).moveRight == true) {
                c.get(i).x += c.get(i).velocity / 1.5;
            }
        }
        //checks if the covids hits the right side of the canvas and then moves it down and makes moveleft true
        for (int i = 0; i < c.size(); i++) {
            if (c.get(i).x > CANVAS_WIDTH - 40) {
                for (int j = 0; j < c.size(); j++) {
                    c.get(j).moveLeft = true;
                    c.get(j).moveRight = false;
                    c.get(j).y += 45;
                }
            }
        }
        //checks if the covids hits the left side of the canvas and then moves it down and makes moveright true
        for (int i = 0; i < c.size(); i++) {
            if (c.get(i).x < 0) {
                for (int j = 0; j < c.size(); j++) {
                    c.get(j).moveRight = true;
                    c.get(j).moveLeft = false;
                    c.get(j).y += 45;
                }
            }
        }
    }

    public void collision(){
        //checks if the any of the covids go past the hand sanitizer so that the game is not ingame anymore
        for (int i = 0; i < c.size(); i++) {
            if (c.get(i).y>=p[0].y) {
                ingame = false;
            }
        }

        //Loops through every shot and covid object to look if any one of them collides with into each other.
        // If they do the that specific covid and shot are removed from the list
        for (int i = 0;i<shots.size();i++){
            for (int j = 0; j < c.size(); j++) {
                if(shots.get(i).x<c.get(j).x&&shots.get(i).x+80> c.get(j).x) {
                    if (shots.get(i).y<c.get(j).y&&shots.get(i).y+80> c.get(j).y) {
                        c.remove(j);
                        shots.remove(i);
                        return;
                    }
                }
            }
        }
    }


    public void run() {


        int beforeTime;

        //controls for the repaint and constant running of the game on the canvas it self
        beforeTime = (int) System.currentTimeMillis();
        int Delay = 10;
        long time = System.currentTimeMillis();
        while (true) {// constantly looping
            repaint();
            try {
                time += Delay;
                Thread.sleep(Math.max(0,time -
                        System.currentTimeMillis()));
            }catch (InterruptedException e) {}
        }


    }


}

class KeyboardListener implements KeyListener {

    private Canvas canvas; // game passed through to allow for game manipulation

    public KeyboardListener(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (canvas.ingame) {
            //makes moveRight true for the sanitizer if the right key has been pressed
            int key = e.getKeyCode();
            if (key == VK_RIGHT) {
                canvas.p[0].moveRight = true;
            }
            //makes moveLeft true for the sanitizer if the left key has been pressed
            if (key == VK_LEFT) {
                canvas.p[0].moveLeft = true;
            }
            //checks if space have been clicked if so it will create the object shot and put into shots and makes moveUp true
            if (key == KeyEvent.VK_SPACE) {
                if (canvas.time > 30) {
                    Shots shot = new Shots(canvas.p[0].x + 50, canvas.p[0].y, 3);
                    canvas.shots.add(shot);
                    shot.moveUp = true;
                    canvas.time = 0;
                }
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        //stops moving right or left when the key is released
        int key = e.getKeyCode();
        canvas.p[0].moveRight = false;
        canvas.p[0].moveLeft =false;
    }
}



class MouseClickListener implements MouseListener {

    private Canvas canvas; // canvas passed through to allow for game manipulation

    public MouseClickListener(Canvas canvas) {
        this.canvas = canvas;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //makes sure to only happen when the user clicks after the game is over.
        if (!canvas.ingame) {
            List<Integer> topFive = new ArrayList<Integer>();
            //call methods in scores in order to get and write down the scores
            Scores s = new Scores(canvas.score);
            s.writeScoreToFile();
            s.getTopFiveScores();
            //read file topfive.txt and puts the content in an arraylist
            try {
                Scanner input = new Scanner(new File("topfive.txt"));

                while (input.hasNextLine()) {
                    String[] x = input.nextLine().split("\r\n");
                    if (Integer.parseInt(x[0])!=0) {
                        topFive.add(Integer.parseInt(x[0]));
                    }
                }
                input.close();
            } catch (FileNotFoundException F) {}
            JLabel headLine = new JLabel("Scoreboard:");
            headLine.setBounds(375, 10, 100,100);
                JTextField textArea = new JTextField();
                //checks if the array list has 5 elements so that it can insert them into the text field.
                if(topFive.size()<=4){
                    textArea.setText("There needs to be at least 5 scores to showcase the scoreboard");
                }
                else{
                    textArea.setText(String.valueOf("1: " + topFive.get(0)) + "\r\n" + "2: " + topFive.get(1) + "\r\n" + "3: " + topFive.get(2) + "\r\n" + "4: " + topFive.get(3) + "\r\n" + "5: " + topFive.get(4) + "\r\n");
                }
                    textArea.selectAll();
                    textArea.setBounds(100, 100, 200, 200);
                    textArea.setBackground(Color.lightGray);
            JFrame frame = new JFrame("Scoreboard");
            JPanel panel = new JPanel();
            panel.setBackground(Color.black);
            panel.setLayout(new FlowLayout());
            panel.setBounds(0,0,750,750);
            panel.add(headLine);
            panel.add(textArea);
            frame.add(panel);
            frame.setSize(750, 750);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            canvas.setVisible(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
