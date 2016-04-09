//
// ClientFrontEnd:: Front-end of the client applet
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.DefaultCaret;

public class ClientFrontEnd implements ActionListener
{
    protected ClientBackEnd backend = new ClientBackEnd(this);

    protected JFrame frame = new JFrame();

    protected JTextArea msg_field = new JTextArea();
    protected JScrollPane msg_field_scroll_pane = new JScrollPane(msg_field);

    protected JTextArea chat_area = new JTextArea();
    protected JScrollPane chat_area_scroll_pane = new JScrollPane(chat_area);

    protected DefaultListModel<ClientInfo> online_list_model = new DefaultListModel<>();
    protected JList<ClientInfo> online_list = new JList<>(online_list_model);
    protected JScrollPane online_list_scroll_pane = new JScrollPane(online_list);

    protected JLabel online_label = new JLabel("Online:");

    protected JLabel ip_label = new JLabel("IP:");
    protected JTextField ip_txtfield = new JTextField(Config.DEFAULT_IP);

    protected JLabel port_label = new JLabel("Port:");
    protected JTextField port_txtfield = new JTextField(Config.DEFAULT_PORT);

    protected JLabel name_label = new JLabel("Name:");
    protected JTextField user_name_txtfield = new JTextField(Config.get_default_name());
    protected JButton connect_button = new JButton("Connect");

    protected JLabel send_to_label = new JLabel("send to:");
    protected JRadioButton send_all_radio = new JRadioButton("all");
    protected JRadioButton send_selected_radio = new JRadioButton("selected");
    protected ButtonGroup jbutton_group = new ButtonGroup();

    public JButton send_button = new JButton("SEND");


    /*constructor*/
    public ClientFrontEnd() /*Constructor*/
    {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle(Config.CLIENT_TITLE);
        frame.getContentPane().setBackground(Config.ROSE);

        send_selected_radio.setBackground(Config.ROSE);
        send_all_radio.setBackground(Config.ROSE);
        msg_field.setBackground(Config.GRAY);
        online_list.setBackground(Config.PEACH);
        port_txtfield.setBackground(Config.GRAY);
        ip_txtfield.setBackground(Config.GRAY);
        user_name_txtfield.setBackground(Config.GRAY);
        chat_area.setBackground(Config.PEACH);
        connect_button.setBackground(Config.GREEN);

        send_button.setFont(Config.UIfont);
        port_label.setFont(Config.UIfont);
        ip_label.setFont(Config.UIfont);
        connect_button.setFont(Config.UIfont);
        online_label.setFont(Config.UIfont);

        frame.setBounds(100, 100, 584, 332);
        chat_area_scroll_pane.setBounds(0, 24, 438, 193);

        connect_button.setBounds(442, 2, 122, 25);
        ip_label.setBounds(5, 3, 18, 16);
        ip_txtfield.setBounds(25, 0, 135, 22);
        port_txtfield.setBounds(205, 0, 65, 22);
        online_label.setBounds(470, 30, 72, 16);
        online_list_scroll_pane.setBounds(444, 46, 118, 171);
        msg_field_scroll_pane.setBounds(0, 223, 438, 64);
        send_to_label.setBounds(446, 218, 56, 16);
        send_all_radio.setBounds(446, 233, 41, 25);
        send_selected_radio.setBounds(489, 233, 75, 25);
        send_button.setBounds(444, 259, 122, 25);
        port_label.setBounds(169, 2, 32, 18);
        user_name_txtfield.setBounds(325, 0, 105, 22);
        name_label.setBounds(282, 3, 38, 16);

        chat_area_scroll_pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        online_list_scroll_pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        msg_field_scroll_pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        DefaultCaret caret = (DefaultCaret) chat_area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        jbutton_group.add(send_all_radio);
        jbutton_group.add(send_selected_radio);
        send_all_radio.setSelected(true);

        frame.setResizable(false);
        chat_area.setEditable(false);
        chat_area.setLineWrap(true);
        msg_field.setLineWrap(true);
        chat_area.setWrapStyleWord(true);
        msg_field.setWrapStyleWord(true);
        connect_button.setBorderPainted(false);
        connect_button.setOpaque(true);
        enable_or_disable_components(false);
        send_button.setEnabled(false);

        send_button.addActionListener(this);
        send_all_radio.addActionListener(this);
        send_selected_radio.addActionListener(this);
        connect_button.addActionListener(this);
        online_list.addListSelectionListener(ev -> check_if_should_enable_send_button());

        /*if user presses enter, send_message the message. if user holds down shift and presses enter, append new line*/
        msg_field.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown())
                {
                    e.consume();
                    if (check_if_should_enable_send_button())
                        backend.send_message(msg_field.getText());
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    msg_field.append("\n");
            }
        });

        /*disable the send button if the msg_field is empty*/
        msg_field.getDocument().addDocumentListener(new DocumentListener()
        {
            public void changedUpdate(DocumentEvent e)
            {
                check_if_should_enable_send_button();
            }


            public void removeUpdate(DocumentEvent e)
            {
                check_if_should_enable_send_button();
            }


            public void insertUpdate(DocumentEvent e)
            {
                check_if_should_enable_send_button();
            }
        });

        /*disconnect the client if the window is closed*/
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                backend.disconnect_client();
            }
        });

        frame.getContentPane().add(msg_field_scroll_pane);
        frame.getContentPane().add(online_list_scroll_pane);
        frame.getContentPane().add(online_label);
        frame.getContentPane().add(port_txtfield);
        frame.getContentPane().add(ip_txtfield);
        frame.getContentPane().add(port_label);
        frame.getContentPane().add(ip_label);
        frame.getContentPane().add(chat_area_scroll_pane);
        frame.getContentPane().add(connect_button);
        frame.getContentPane().add(send_to_label);
        frame.getContentPane().add(send_all_radio);
        frame.getContentPane().add(send_selected_radio);
        frame.getContentPane().add(user_name_txtfield);
        frame.getContentPane().add(send_button);
        frame.getContentPane().add(name_label);

        frame.setLocation(0, 600);
    }//end constructor


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == connect_button)
            if (!backend.connected) backend.connect_client();
            else backend.disconnect_client();
        else if (e.getSource() == send_button)
            backend.send_message(msg_field.getText());
        else if (e.getSource() == send_all_radio || e.getSource() == send_selected_radio)
            check_if_should_enable_send_button();
    }//end actionPerformed(...)


    /*check the three components to enable/disable the send button*/
    public boolean check_if_should_enable_send_button()
    {
        send_button.setEnabled(
                (send_all_radio.isSelected() &&
                 !msg_field.getText().isEmpty())
                ||
                (send_selected_radio.isSelected() &&
                 !msg_field.getText().isEmpty() &&
                 online_list.getSelectedIndices().length > 0)
        );
        return send_button.isEnabled();
    }//end check_if_should_enable_send_button()


    /*there are times when some components should toggle between enable/disable*/
    protected void enable_or_disable_components(boolean choice)
    {
        online_label.setEnabled(choice);
        ip_txtfield.setEditable(!choice);
        port_txtfield.setEditable(!choice);
        user_name_txtfield.setEditable(!choice);
        msg_field.setEditable(choice);
        msg_field_scroll_pane.setEnabled(choice);
        send_all_radio.setEnabled(choice);
        send_selected_radio.setEnabled(choice);
        send_to_label.setEnabled(choice);
    }//end enable_or_disable_components(...)
}//end class