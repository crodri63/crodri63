//
// ClientInfo :: Information about some client, including username and socket input/output streams
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.io.*;
import java.net.Socket;

public class ClientInfo implements Serializable
{
    protected String user_name;                     //This clients's name displayed by server and client apps
    protected transient Socket client_socket;       //This client's designated socket
    protected transient ObjectOutputStream writer;  //This client's socket's output stream
    protected transient ObjectInputStream reader;   //This client's sockets's input stream


    /*when client side creates a new client, store the reader/writer and username, and send user_name to the server*/
    public ClientInfo(Socket client_socket, String user_name) throws IOException
    {
        this.client_socket = client_socket;
        this.writer = new ObjectOutputStream(client_socket.getOutputStream());
        this.reader = new ObjectInputStream(client_socket.getInputStream());
        this.user_name = user_name;
        this.writer.writeObject(this.user_name);
    }//end constructor


    /*when server side creates a new client, store the reader/writer and read the user_name from the client*/
    public ClientInfo(Socket client_socket) throws IOException, ClassNotFoundException
    {
        this.client_socket = client_socket;
        this.writer = new ObjectOutputStream(client_socket.getOutputStream());
        this.reader = new ObjectInputStream(client_socket.getInputStream());
        this.user_name = (String) this.reader.readObject();
    }//end constructor


    @Override
    public String toString()
    {
        return this.user_name;
    }//end toString()
}//end class