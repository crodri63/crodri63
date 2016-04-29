//
// Tetris:: All components, including menu, sidebar, board class
//          and gravity that pulls pieces down
//
// <<Filip Variciuc>> <<Constantino Rodriguez>> <<Lukasz Przybos>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.concurrent.TimeUnit;
import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static javax.swing.JFrame.*;

public class Tetris
{
    public Menu menu;
    public SideBar sidebar;
    protected JFrame frame;
    private JPanel containPanels[][] = new JPanel[20][];
    protected Board gameboard;
    public Thread gravityFunction;
    public gravity func;
    public Clip clip;
    public boolean gameOver = false;
    protected Color theColors[] = {new Color(0xB5E5E5), new Color(0x9C9C9C), new Color(0xD63145), new Color(0xE5FF00),
                                   new Color(0xE01BA2), new Color(0x1B40E0), new Color(0x32C225)};


    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            // TODO Auto-generated method stub
            try
            {
                Tetris window = new Tetris();
                window.frame.setVisible(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }


    /**
     * Create the application.
     */
    public Tetris()
    {

        initialize();
        initializeMusic(); // Start the traditional Tetris theme music via .wav
        // file


        gravityFunction = new Thread(func);
        gravityFunction.start();
    }


    /**
     * Initialize the contents of the frame.
     */
    public void initialize()
    {

        func = new gravity(); // Start the gravity thread


        frame = new JFrame(("Tetris - CS 342, Project 5"));
        frame.setFocusable(true);
        frame.setBounds(100, 100, 600, 850);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);

        gameboard = new Board(this);
        gameboard.next();


        menu = new Menu(this);
        frame.setJMenuBar(menu);
        sidebar = new SideBar(this);
        sidebar.setBounds(400, 200, 200, 500);
        frame.add(sidebar);

        frame.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent arg0)
            {
                int key = arg0.getKeyCode();
                if (gameOver)
                {
                    gravityFunction.interrupt();
                    return;
                }
                if (key == 40)// down
                {
                    gravityFunction.interrupt();
                    gameboard.removeTetro();

                    gameboard.gravity();
                    gameboard.setTetro();
                    boardsetColor();
                    gravityFunction = new Thread(func);
                    gravityFunction.start();
                }
                if (key == 39)// right
                {
                    gameboard.removeTetro();
                    gameboard.right();
                    gameboard.setTetro();
                    boardsetColor();
                }
                if (key == 37) // left
                {
                    gameboard.removeTetro();
                    gameboard.left();
                    gameboard.setTetro();
                    boardsetColor();
                }
                if (key == 38)// rotate (up arrow)
                {
                    if (gameboard.checkRotation())
                    {
                        gravityFunction.interrupt();

                        gameboard.removeTetro();
                        gameboard.rotate();
                        gameboard.setTetro();
                        boardsetColor();

                        gravityFunction = new Thread(func);
                        gravityFunction.start();
                    }
                }
            }
        });

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(10, 48, 343, 703);
        panel_1.setBackground(Color.BLACK);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 323, 682);
        panel_1.add(panel);
        panel.setBackground(new Color(0x92B4F4));
        panel.setLayout(new GridLayout(20, 10, 1, 1));

        for (int i = 0; i < 20; ++i)
        {
            containPanels[i] = new JPanel[10];
            for (int j = 0; j < 10; ++j)
            {
                containPanels[i][j] = new JPanel();
                containPanels[i][j].setBackground(Color.WHITE);
                panel.add(containPanels[i][j]);
            }
        }


        (new Thread(new testRun())).start();
    }


    public class testRun implements Runnable            //running in its own thread
    {
        public void run()
        {
            boardsetColor();
        }
    }


    public void boardsetColor()
    {
        for (int x = 0; x < 20; x++)
            for (int y = 0; y < 10; y++)
                if (gameboard.matrix[x][y] == null)
                    containPanels[x][y].setBackground(Color.WHITE);
                else
                    switch (gameboard.matrix[x][y])
                    {
                        case "I":
                            containPanels[x][y].setBackground(theColors[0]);
                            break;
                        case "J":
                            containPanels[x][y].setBackground(theColors[1]);
                            break;
                        case "L":
                            containPanels[x][y].setBackground(theColors[2]);
                            break;
                        case "O":
                            containPanels[x][y].setBackground(theColors[3]);
                            break;
                        case "S":
                            containPanels[x][y].setBackground(theColors[4]);
                            break;
                        case "T":
                            containPanels[x][y].setBackground(theColors[5]);
                            break;
                        case "Z":
                            containPanels[x][y].setBackground(theColors[6]);
                            break;
                    }
    }


    private void initializeMusic()
    {
        File path = new File("tetris.wav");
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(path);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    // Gravity class keeps track that gravity actually happens.
    public class gravity implements Runnable
    {
        private int delay = 1000;


        public void run()
        {
            try
            {
                while (!gameOver)
                {
                    TimeUnit.MILLISECONDS.sleep(delay);
                    gameboard.removeTetro();
                    gameboard.gravity();
                    gameboard.setTetro();
                    boardsetColor();
                }
                sidebar.timer.stop();
                JOptionPane.showMessageDialog(null, "GAME OVER!");
            }
            catch (Exception ignored)
            {
            }
        }


        // Update the speed of the gravity as the levels get harder.
        // Max level is 24.
        public void updateTimer(int level)
        {
            if (level == 1)
                delay = 1000;
            else if (level < 24)
                delay *= .9;
        }
    }
}
