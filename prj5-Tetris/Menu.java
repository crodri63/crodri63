//
// Menu:: Allows the user to perform certain operations, including:
//                              starting/restarting
//                              quitting
//                              displaying help
//                              displaying information about the group
//
// <<Filip Variciuc>> <<Constantino Rodriguez>> <<Lukasz Przybos>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar implements ActionListener
{
    private Tetris mainframe;                   // reference to the frame

    private JMenu gameMenu;                     // menu with Game options
    private JMenuItem resetSubMenu;             // start/reset
    private JMenuItem quitSubMenu;              // quit the program

    private JMenu helpMenu;                     // menu with Help options
    private JMenuItem helpSubMenu;              // display how-to play
    private JMenuItem aboutSubMenu;             // display information about the programmers


    public Menu(Tetris mainframe)
    {
        this.mainframe = mainframe;

        gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');
        resetSubMenu = new JMenuItem("Start/Restart");
        resetSubMenu.setMnemonic('S');
        quitSubMenu = new JMenuItem("Quit");
        quitSubMenu.setMnemonic('Q');

        resetSubMenu.addActionListener(this);
        quitSubMenu.addActionListener(this);

        gameMenu.add(resetSubMenu);
        gameMenu.add(quitSubMenu);
        add(gameMenu);

        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        helpSubMenu = new JMenuItem("How to Play");
        helpSubMenu.setMnemonic('P');
        aboutSubMenu = new JMenuItem("About");
        aboutSubMenu.setMnemonic('A');

        helpSubMenu.addActionListener(this);
        aboutSubMenu.addActionListener(this);

        helpMenu.add(helpSubMenu);
        helpMenu.add(aboutSubMenu);
        add(helpMenu);
    }// end createMenuBar()


    public void actionPerformed(ActionEvent e)
    {
        /* Start/restart the game */
        if (e.getSource() == resetSubMenu)
        {
            mainframe.sidebar.reset();              //reset sidebar values
            mainframe.gameboard.clearBoard();       //reset playing board
            mainframe.boardsetColor();
            mainframe.gameOver = false;             //toggle gameOver to false
        }

		/* Exit */
        else if (e.getSource() == quitSubMenu)
            System.exit(0);

		/*
         * This can be a dialog box that describes the basics of your program.
		 * This should include a description of all menu, button and keyboard
		 * operations.
		 */
        else if (e.getSource() == helpSubMenu)
        {
            JOptionPane.showMessageDialog(null,
                    "Move the tetrominoes left and right with the arrow keys. Try to get the tetrominoes to fit \n"
                    + "holes. These holes can arise from the various pieces you've previously placed down. \n\n"
                    + "Every time a row of tetrominoes is filled, the row vanishes and your score gets bigger. \n"
                    + "Check upcoming pieces and plan for them.  \n\n The 'Game' menu allows you to restart or exit "
                    + "the game. The 'Help' menu allows you to \n"
                    + "view more information about the programmers or to display this information again. \n"
                    + "Have fun!");
        }

		/*
         * About menu item should give some information about the development
		 * team (name(s), user-id(s), etc) and course/project information.
		 */
        else if (e.getSource() == aboutSubMenu)
        {
            String classInfo = "UIC Spring 2016, CS 342: Software Design \n";
            String profInfo = "Professor: Pat Troy | TA: Muhammad Khan \n";
            String Names = "Programmers: Filip Variciuc | Constantino Rodriguez | Lukasz Przybos \n";
            String userIDs = "NetIDs: variciu2 | crodri63 | lprzyb3 \n";
            String projectInfo = "Project 5: Tetris, a spin-off programmed using Java Swing API";
            JOptionPane.showMessageDialog(null, classInfo + profInfo + Names + userIDs + projectInfo);
        }
    }// end ActionPerformed(...)
}// end class
