//
// SideBar:: Controls and displays information for :
//                               time elapsed
//                               current level
//                               current score
//                               goal for next level
//                               upcoming piece
//
// <<Filip Variciuc>> <<Constantino Rodriguez>> <<Lukasz Przybos>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SideBar extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Tetris mainframe;           //reference to the central object

    private JButton mute;               //mute button
    private Icon speaker_play;          //speaker icon with sound waves coming out
    private Icon speaker_mute;          //speaker icon with slash through it

    private JLabel time;                //display the time
    private JLabel time_text;
    private int current_time;
    protected Timer timer;              //create a timer object to update current_time

    private JLabel level;               //keep track of the current level
    private JLabel level_text;
    private int current_level;

    private JLabel score;               //keeping track of the score
    private JLabel score_text;
    private int current_score;

    private JLabel goal;                //show user how many rows need to be clear until the next level
    private JLabel goal_text;
    private int current_goal;

    private JLabel next;                //show the next piece in a small jpanel
    private JPanel nextcontainPanels[][] = new JPanel[6][6];
    private Tetro nexttetro;


    /* constructor */
    public SideBar(Tetris mainframe)
    {
        this.mainframe = mainframe;
        setLayout(null);

        speaker_play = new ImageIcon(getClass().getClassLoader().getResource("speaker_play.png"));
        speaker_mute = new ImageIcon(getClass().getClassLoader().getResource("speaker_mute.png"));

        mute = new JButton();
        mute.setIcon(speaker_play);
        mute.setBounds(15, 15, 20, 20);
        add(mute);
        mute.addActionListener(this);

        time = new JLabel("TIME");
        time.setFont(new Font("Arial Black", Font.PLAIN, 16));
        time.setBounds(42, 13, 45, 23);
        add(time);

        timer = new Timer(1000, this);

        time_text = new JLabel("0");
        time_text.setFont(new Font("Arial Black", Font.PLAIN, 16));
        time_text.setBounds(53, 41, 45, 30);
        add(time_text);

        level = new JLabel("LEVEL");
        level.setBounds(37, 77, 58, 23);
        level.setFont(new Font("Arial Black", Font.PLAIN, 16));
        add(level);

        level_text = new JLabel();
        level_text.setFont(new Font("Arial Black", Font.PLAIN, 16));
        level_text.setBounds(57, 113, 30, 23);
        add(level_text);

        score = new JLabel("SCORE");
        score.setFont(new Font("Arial Black", Font.PLAIN, 16));
        score.setBounds(35, 144, 61, 23);
        add(score);

        score_text = new JLabel();
        score_text.setFont(new Font("Arial Black", Font.PLAIN, 16));
        score_text.setBounds(50, 180, 60, 23);
        add(score_text);

        goal = new JLabel("GOAL");
        goal.setFont(new Font("Arial Black", Font.PLAIN, 16));
        goal.setBounds(39, 218, 49, 23);
        add(goal);

        goal_text = new JLabel();
        goal_text.setFont(new Font("Arial Black", Font.PLAIN, 16));
        goal_text.setBounds(50, 254, 30, 23);
        add(goal_text);

        next = new JLabel("NEXT");
        next.setFont(new Font("Arial Black", Font.PLAIN, 16));
        next.setBounds(39, 290, 49, 23);
        add(next);

        JPanel nextpanel = new JPanel();
        nextpanel.setBounds(25, 325, 100, 100);
        add(nextpanel);
        nextpanel.setBackground(Color.BLACK);
        nextpanel.setLayout(new GridLayout(4, 4, 1, 1));

        for (int i = 0; i < 4; ++i)             //generate panel
        {
            nextcontainPanels[i] = new JPanel[4];
            for (int j = 0; j < 4; ++j)
            {
                nextcontainPanels[i][j] = new JPanel();
                nextcontainPanels[i][j].setBackground(Color.WHITE);
                nextpanel.add(nextcontainPanels[i][j]);
            }
        }
        reset();
    }// end constructor


    /* set default values when starting/restarting the game */
    public void reset()
    {
        timer.start();
        current_time = 0;
        current_level = 1;
        current_score = 0;
        current_goal = 10;
        this.mainframe.gameOver = false;

        if (nexttetro != mainframe.gameboard.nextTetro)
        {
            nexttetro = mainframe.gameboard.nextTetro;
            nextDisplay();
        }

        mainframe.gameboard.add();
        mainframe.gameboard.setTetro();
        mainframe.gameboard.next();

        mainframe.func.updateTimer(current_level);
        update_components();
    }// end reset()


    /*
     * given the number of lines cleared, update the score and level if
     * necessary
     */
    public void update_score(int lines_cleared)
    {
        int score_gained = 0;

        if (lines_cleared == 1)
            score_gained = 40 * current_level;
        else if (lines_cleared == 2)
            score_gained = 100 * current_level;
        else if (lines_cleared == 3)
            score_gained = 300 * current_level;
        else if (lines_cleared == 4)
            score_gained = 1200 * current_level;

        current_score += score_gained;

        // check if level is beaten (goal <= 0)
        if ((current_goal -= lines_cleared) <= 0)
        {
            int x = current_goal * -1;
            current_goal = 10 - x;
            current_level++;

            mainframe.func.updateTimer(current_level); // change the falling speed
        }

        nexttetro = mainframe.gameboard.nextTetro;
        nextDisplay();

        update_components();
    }// end update_score(...)


    /* update the JLabel for the appropriate component */
    private void update_components()
    {
        level_text.setText(Integer.toString(current_level));
        score_text.setText(Integer.toString(current_score));
        goal_text.setText(Integer.toString(current_goal));
    }// end update_components()


    private void nextDisplay()
    {
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                nextcontainPanels[i][j].setBackground(Color.WHITE);

        int[][] tMatrix = nexttetro.getCoord();
        String color = nexttetro.getType();
        for (int i = 0; i < tMatrix.length; i++)
            for (int j = 0; j < tMatrix[0].length; j++)
                if (tMatrix[i][j] == 1)
                    switch (color)
                    {
                        case "I":
                            nextcontainPanels[i][j].setBackground(mainframe.theColors[0]);
                            break;
                        case "J":
                            nextcontainPanels[i][j].setBackground(mainframe.theColors[1]);
                            break;
                        case "L":
                            nextcontainPanels[i][j].setBackground(mainframe.theColors[2]);
                            break;
                        case "O":
                            nextcontainPanels[i][j].setBackground(mainframe.theColors[3]);
                            break;
                        case "S":
                            nextcontainPanels[i][j].setBackground(mainframe.theColors[4]);
                            break;
                        case "T":
                            nextcontainPanels[i][j].setBackground(mainframe.theColors[5]);
                            break;
                        case "Z":
                            nextcontainPanels[i][j].setBackground(mainframe.theColors[6]);
                            break;
                    }
    }


    /* update the JLabel for the timer */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == timer)
            time_text.setText(Integer.toString(++current_time));

        else if (e.getSource() == mute)
        {
            if (mainframe.clip.isActive()) // playing, so stop
            {
                mainframe.clip.stop();
                mute.setIcon(speaker_mute);
            }
            else // stopped, so play
            {
                mainframe.clip.loop(10);
                mute.setIcon(speaker_play);
            }
            mainframe.frame.requestFocus();
        } // end if(mute)
    }// end actionPerformed(...)
}// end class
