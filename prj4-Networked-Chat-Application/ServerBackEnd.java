//
// ServerBackEnd :: Back-end of the server_connection_thread applet
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ServerBackEnd
{
    protected ServerFrontEnd front_end;           //Every backend has a reference to the front end
    protected ServerSocket server_socket;         //Every backend has its own socket that it interfaces
    protected boolean running;                    //Flag set when server starts/stops


    /*constructor*/
    public ServerBackEnd(ServerFrontEnd front_end)
    {
        this.front_end = front_end;
    }//end constructor


    /*start_server up the server_connection_thread*/
    protected void start_server()
    {
        if (running) return;
        running = true;

        ((DefaultListModel<ClientInfo>) front_end.online_list.getModel()).removeAllElements();
        front_end.online_label.setText("Online: " + front_end.online_list.getModel().getSize());

        front_end.enable_or_disable_components(false);
        front_end.start_server_button.setBackground(Config.RED);
        front_end.frame.setTitle(Config.SERVER_TITLE + " [ONLINE]");
        front_end.start_server_button.setText("STOP SERVER");
        new ConnectionThread(this);
    }//end start_server()


    /*shut down the server_connection_thread*/
    protected void stop_server() throws IOException
    {
        if (!running) return;
        running = false;

        while (front_end.online_list.getModel().getSize() > 0)
            remove_online_user(front_end.online_list.getModel().getElementAt(0));

        server_socket.close();  //this is what actually closes the server
        front_end.chat_area.append("-------------------------------------------\n" +
                                   "| <<SERVER TERMINATED>> |\n" +
                                   "-------------------------------------------\n");

        front_end.frame.setTitle(Config.SERVER_TITLE);
        front_end.enable_or_disable_components(true);
        front_end.start_server_button.setBackground(Config.GREEN);
        front_end.start_server_button.setText("START SERVER");
    }//end stop_server()


    /*remove an online client from the server by closing the client socket*/
    protected void remove_online_user(ClientInfo client_to_remove) throws IOException
    {
        client_to_remove.client_socket.close(); //terminate connection with the client, triggers client disconnect

        ((DefaultListModel<ClientInfo>) front_end.online_list.getModel()).removeElement(client_to_remove);
        front_end.online_label.setText("Online: " + front_end.online_list.getModel().getSize());

        front_end.chat_area.append("<<Client [" + client_to_remove + "] has disconnected>>\n");
        dispatch(new MessageInfo(front_end.online_list_model));
    }//end remove_online_users(...)


    /*notify all/some online users of the messages, connections, and disconnections*/
    protected void dispatch(MessageInfo message_info) throws IOException
    {
        if (message_info.message == null) //no message is sent if client connects/disconnects
            for (int i = 0; i < message_info.online_clients.size(); i++)
                message_info.online_clients.get(i).writer.writeObject(message_info);
        else //message are not null, display this message and send to each recipient
        {
            front_end.chat_area.append(message_info.message + "\n");

            for (int i = 0; i < front_end.online_list_model.getSize(); i++)
                for (int j = 0; j < message_info.recipients.size(); j++)
                    if (message_info.recipients.get(j).user_name.equals(front_end.online_list_model.get(i).user_name))
                        front_end.online_list_model.get(i).writer.writeObject(message_info);
        }
    }//end dispatch(...)
}//end class ServerBackEnd

//##########################################################################
/*One ConnectionThread per running server. This threads job is to sit in a while loop and service new online_clients*/
class ConnectionThread extends Thread
{
    protected ServerBackEnd back_end; //an access to the backend of the server


    public ConnectionThread(ServerBackEnd server_back_end) /*constructor*/
    {
        this.back_end = server_back_end;
        start();
    }//end constructor


    /*method invoked by start_server() call in constructor, manages adding clients and starting/stopping server thread*/
    @Override
    public void run()
    {
        try
        {
            back_end.server_socket = new ServerSocket(Integer.parseInt(back_end.front_end.port_txtfield.getText()));
            back_end.front_end.chat_area.append("-------------------------------------------\n" +
                                                "| <<SERVER STARTED>>        |\n" +
                                                "-------------------------------------------\n");
            while (back_end.running)
                add_incoming_clients(); //service any new incoming client until the server shuts down
        }
        catch (Exception ignored)
        {
        }
    }//end run()


    /*service incoming online_clients to the server_connection_thread*/
    protected void add_incoming_clients() throws Exception
    {
        ClientInfo new_client = new ClientInfo(back_end.server_socket.accept()); //accept() waits until client connects

        back_end.front_end.chat_area.append("<<Client [" + new_client.user_name + "] has connected.>>\n");
        ((DefaultListModel<ClientInfo>) back_end.front_end.online_list.getModel()).addElement(new_client);
        back_end.front_end.online_label.setText("Online: " + back_end.front_end.online_list.getModel().getSize());

        back_end.dispatch(new MessageInfo((DefaultListModel<ClientInfo>) back_end.front_end.online_list.getModel()));

        new CommunicationThread(this, new_client);
    }//end add_incoming_clients()
}//end class ConnectionThread

//##########################################################################
/*The client makes a thread that handles communication- one for every client connected*/
class CommunicationThread extends Thread
{
    private ConnectionThread server_connection_thread; //an access to the connection thread of backend of the server
    private ClientInfo this_client;                    //the client this particular thread deals with


    /*Constructor that starts reading in online_clients sockets output*/
    public CommunicationThread(ConnectionThread server_connection_thread, ClientInfo current_client)
    {
        this.server_connection_thread = server_connection_thread;
        this.this_client = current_client;
        start();
    }//end constructor


    /*method invoked by start_server() call in constructor, manages communication between every new this_client*/
    @Override
    public void run()
    {
        MessageInfo input;
        try
        {
            while ((input = (MessageInfo) this_client.reader.readObject()) != null)
                server_connection_thread.back_end.dispatch(input);
        }
        catch (Exception ex)
        {
            if (server_connection_thread.back_end.running)
                try
                {
                    server_connection_thread.back_end.remove_online_user(this_client);
                }
                catch (IOException ignored)
                {
                }
        }
    }//end run()
}//end class CommunicationThread
