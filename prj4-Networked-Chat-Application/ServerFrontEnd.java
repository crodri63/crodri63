//
// ServerFrontEnd :: Front-end of the server applet
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.DefaultCaret;

public class ServerFrontEnd implements ActionListener
{
    protected ServerBackEnd backend = new ServerBackEnd(this);

    protected JFrame frame = new JFrame();

    protected JTextArea chat_area = new JTextArea();
    protected JScrollPane chat_area_scroll_pane = new JScrollPane(chat_area);

    protected DefaultListModel<ClientInfo> online_list_model = new DefaultListModel<>();
    protected JList<ClientInfo> online_list = new JList<>(online_list_model);
    protected JScrollPane online_list_scroll_pane = new JScrollPane(online_list);

    protected JLabel online_label = new JLabel("Online:");

    protected JTextField port_txtfield = new JTextField(Config.DEFAULT_PORT);
    protected JLabel ip_label = new JLabel("IP: " + Config.DEFAULT_IP);
    protected JLabel port_label = new JLabel("Port:");

    protected JButton start_server_button = new JButton("START SERVER");


    /*Constructor*/
    public ServerFrontEnd()
    {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle(Config.SERVER_TITLE);
        frame.getContentPane().setBackground(Config.TEAL);

        port_txtfield.setBackground(Config.TEAL);
        online_list.setBackground(Config.BLUE);
        chat_area.setBackground(Config.BLUE);
        start_server_button.setBackground(Config.GREEN);

        ip_label.setFont(Config.UIfont);
        port_label.setFont(Config.UIfont);
        online_label.setFont(Config.UIfont);

        start_server_button.setFont(Config.UIfont);
        start_server_button.setBounds(300, -2, 140, 25);
        online_list_scroll_pane.setBounds(444, 46, 118, 236);
        online_label.setBounds(472, 30, 72, 16);
        port_txtfield.setBounds(205, 0, 81, 22);
        port_label.setBounds(169, 2, 32, 18);
        ip_label.setBounds(5, 3, 152, 16);
        chat_area_scroll_pane.setBounds(0, 27, 438, 255);
        frame.setBounds(100, 100, 584, 332);

        online_list_scroll_pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chat_area_scroll_pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        DefaultCaret caret = (DefaultCaret) chat_area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        frame.setResizable(false);
        chat_area.setLineWrap(true);
        chat_area.setAutoscrolls(true);
        chat_area.setEditable(false);
        start_server_button.setBorderPainted(false);
        start_server_button.setOpaque(true);
        enable_or_disable_components(true);

        start_server_button.addActionListener(this);

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    backend.stop_server();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        frame.getContentPane().add(start_server_button);
        frame.getContentPane().add(chat_area_scroll_pane);
        frame.getContentPane().add(ip_label);
        frame.getContentPane().add(port_txtfield);
        frame.getContentPane().add(online_label);
        frame.getContentPane().add(online_list_scroll_pane);
        frame.getContentPane().add(port_label);

        frame.setLocation(0, 250);
    }//end constructor


    /*action listener for the START/STOP button*/
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == start_server_button)
            if (!backend.running) backend.start_server();
            else try
            {
                backend.stop_server();
            }
            catch (IOException ignored)
            {
            }
    }//end actionPerformed(...)


    /*there are times when some components should toggle between enable/disable*/
    protected void enable_or_disable_components(boolean choice)
    {
        online_label.setEnabled(!choice);
        online_list_scroll_pane.setEnabled(!choice);
        port_txtfield.setEnabled(choice);
        chat_area_scroll_pane.setEnabled(!choice);
    }//end enable_or_disable_components(...)
}//end class
