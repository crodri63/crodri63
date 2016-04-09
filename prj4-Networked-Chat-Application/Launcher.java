//
// Launcher :: Main driver, allows user to start_server either server or client(s)
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import javax.swing.*;
import java.awt.*;

public class Launcher
{
    private JFrame frame_launcher = new JFrame();
    private JButton launch_server = new JButton("Launch Server");
    private JButton launch_client = new JButton("Launch Client");
    private JLabel creators = new JLabel("<html><center>powered by</center><br>" +
                                         "Filip Variciuc & Constantino Rodriguez</html>");


    /*Create an instance of launcher*/
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            try
            {
                Launcher window = new Launcher();
                window.frame_launcher.setVisible(true);
            }
            catch (Exception ignored)
            {
            }
        });
    }//end main(...)


    /*Launcher constructor lets user start_server a client or a server*/
    public Launcher()
    {
        frame_launcher.setResizable(false);
        frame_launcher.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame_launcher.getContentPane().setLayout(null);
        frame_launcher.getContentPane().setBackground(Config.PISS);
        creators.setFont(Config.UIfontSmall);

        frame_launcher.setBounds(100, 100, 190, 160);
        launch_server.setBounds(30, 13, 129, 25);
        launch_client.setBounds(30, 51, 129, 25);
        creators.setBounds(20, 75, 141, 38);

        launch_server.addActionListener(arg0 -> EventQueue.invokeLater(() -> {
            try
            {
                ServerFrontEnd window = new ServerFrontEnd();
                window.frame.setVisible(true);
            }
            catch (Exception ignored)
            {
            }
        }));

        launch_client.addActionListener(arg0 -> EventQueue.invokeLater(() -> {
            try
            {
                ClientFrontEnd window = new ClientFrontEnd();
                window.frame.setVisible(true);
            }
            catch (Exception ignored)
            {
            }
        }));

        frame_launcher.getContentPane().add(launch_client);
        frame_launcher.getContentPane().add(launch_server);
        frame_launcher.getContentPane().add(creators);
        frame_launcher.setLocation(50, 50);
    }//end constructor
}//end class