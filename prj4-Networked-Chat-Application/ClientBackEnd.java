//
// ClientBackEnd :: Back-end of the client applet
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import javax.swing.*;
import java.io.*;
import java.net.*;

public class ClientBackEnd
{
    protected ClientFrontEnd front_end;        //Every backend has a reference to the front end
    protected ClientInfo client;               //Information stored about this client
    protected boolean connected;               //Flag set when client connects/disconnects


    /*Constructor*/
    public ClientBackEnd(ClientFrontEnd client_front_end)
    {
        this.front_end = client_front_end;
    }//end constructor


    /*connect to the server*/
    public void connect_client()
    {
        if (!connected) //check the flag just in case- threads may change this variable unexpectedly
            try
            {
                clean_list();

                String server_ip = front_end.ip_txtfield.getText();
                int server_port = Integer.parseInt(front_end.port_txtfield.getText());
                front_end.chat_area.append("<<Trying to connect to " + "[" + server_ip + ":" + server_port + "]>>\n");

                Socket client_socket = new Socket();
                client_socket.connect(new InetSocketAddress(server_ip, server_port), 500);
                front_end.chat_area.append("<<Connection Successful.>>\n");
                front_end.connect_button.setText("Disconnect");

                client = new ClientInfo(client_socket, front_end.user_name_txtfield.getText());

                front_end.connect_button.setBackground(Config.RED);
                front_end.frame.setTitle(Config.CLIENT_TITLE + " - [" + client.user_name + "]");
                front_end.enable_or_disable_components(true);

                connected = true;
                new ServerHandler(this);
            }
            catch (IOException e)
            {
                front_end.chat_area.append("<<Attempt failed.>>\n\n");
                disconnect_client();
            }
    }//end disconnect()


    /*disconnect from the server*/
    public void disconnect_client()
    {
        if (connected) //check the flag just in case- threads may change this variable unexpectedly
            try
            {
                clean_list(); //remove clients from list and set online_count to 0
                front_end.msg_field.setText("");
                front_end.connect_button.setText("Connect");
                front_end.chat_area.append("<<Client Terminated Connection.>>\n");

                front_end.frame.setTitle(Config.CLIENT_TITLE);
                front_end.connect_button.setBackground(Config.GREEN);
                front_end.enable_or_disable_components(false);

                client.client_socket.close(); //close this connection with the server
                connected = false;
            }
            catch (IOException ignored)
            {
            }
    }//end disconnect()


    /*send a message and a list of recipients to the client*/
    public void send_message(String message)
    {
        try
        {
            MessageInfo output = new MessageInfo(client.user_name, message,
                    front_end.send_selected_radio.isSelected());

            if (output.is_private) //sending to some
                for (int i = 0; i < front_end.online_list.getSelectedIndices().length; i++)
                    output.recipients.add(front_end.online_list.getModel().getElementAt(
                            front_end.online_list.getSelectedIndices()[i]));
            else                   //sending to all
                for (int i = 0; i < front_end.online_list.getModel().getSize(); i++)
                    output.recipients.add(front_end.online_list.getModel().getElementAt(i));

            client.writer.writeObject(output);  //send this MessageInfo over the stream
            client.writer.flush();              //clean out any junk
            front_end.msg_field.setText("");

            if (output.is_private)
                for (int i = 0; i < output.recipients.size(); i++)
                    front_end.chat_area.append("To [" + output.recipients.get(i).user_name + "]: " + message + "\n");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//end send_message()


    /*removes all clients from the online_list and sets the counter of online clients to reflect this change*/
    public void clean_list()
    {
        ((DefaultListModel<ClientInfo>) front_end.online_list.getModel()).removeAllElements();
        front_end.online_label.setText("Online: " + front_end.online_list.getModel().getSize());
    }//end clean_list()
}//end class ClientBackEnd

//##########################################################################
class ServerHandler extends Thread
{
    private ClientBackEnd back_end;     //an access to the backend of the client


    public ServerHandler(ClientBackEnd client_back_end)
    {
        this.back_end = client_back_end;
        start();
    }//end constructor


    /*method invoked by start_server() call in constructor, manages communication between client and client*/
    public void run()
    {
        MessageInfo input;
        try
        {
            while ((input = (MessageInfo) back_end.client.reader.readObject()) != null)
            {
                if (input.online_clients.getSize() > 0) //received an updated client list
                {
                    back_end.clean_list(); //remove clients from list and set online_count to 0

                    for (int i = 0; i < input.online_clients.getSize(); i++)
                        back_end.front_end.online_list_model.addElement(input.online_clients.get(i));

                    back_end.front_end.online_label.setText(
                            "Online: " + back_end.front_end.online_list.getModel().getSize());
                }
                else if (input.message != null)         //received some text in the message
                    back_end.front_end.chat_area.append(input.message + "\n");
            }
        }
        catch (Exception ignored)
        {
            back_end.disconnect_client(); //something went wrong, abort
        }
    }//end run()
}//end class ServerHandler