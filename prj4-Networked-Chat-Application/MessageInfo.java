//
// MessageInfo :: Two types of messages that can be sent over the stream:
//                      - Messages that hold just a list of clients
//                      - Messages that hold sender, recipients, and text
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class MessageInfo implements Serializable
{
    protected DefaultListModel<ClientInfo> online_clients = new DefaultListModel<>();     //A list of online clients


    /*MessageInfo that is used when client connects/disconnects*/
    public MessageInfo(DefaultListModel<ClientInfo> online_clients)
    {
        for (int i = 0; i < online_clients.getSize(); i++)
            this.online_clients.addElement(online_clients.elementAt(i));
    }//end constructor

    //##############################################################################################################

    protected String sender;                                            //The client who sends this message
    protected ArrayList<ClientInfo> recipients = new ArrayList<>();     //A list of clients whom receive a message
    protected String message = null;                                    //A message to be received by all recipients
    protected boolean is_private;                                       //TRUE when message is private


    /*MessageInfo that is used when client sends a message*/
    public MessageInfo(String sender, String message_contents, boolean is_private)
    {
        this.sender = sender;
        this.is_private = is_private;

        if (this.is_private)
            this.message = "[" + this.sender + "] whispers: " + message_contents;
        else
            this.message = "[" + this.sender + "]: " + message_contents;
    }//end constructor
}//end class
