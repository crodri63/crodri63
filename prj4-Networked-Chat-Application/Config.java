//
// Config :: Contains all defaults and other "hard coded" values in the program in one spot for easy
//             maintenence/modification
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.awt.*;
import java.net.UnknownHostException;

public class Config
{
    public static final Color RED = new Color(205, 92, 92);
    public static final Color GREEN = new Color(60, 179, 113);
    public static final Color BLUE = new Color(100, 149, 237);
    public static final Color TEAL = new Color(0, 139, 139);
    public static final Color GRAY = new Color(192, 192, 192);
    public static final Color ROSE = new Color(188, 143, 143);
    public static final Color PEACH = new Color(222, 184, 135);
    public static final Color PISS = new Color(240, 230, 140);

    public static final Font UIfont = (new Font("Segoe UI Symbol", Font.BOLD, 13));
    public static final Font UIfontSmall = (new Font("Segoe UI Symbol", Font.PLAIN, 8));

    public static final String DEFAULT_IP = get_default_ip();
    public static final String DEFAULT_PORT = "8080";
    public static final String CLIENT_TITLE = "CLIENT";
    public static final String SERVER_TITLE = "SERVER";

    public static String DEFAULT_CLIENT_NAME = "TEST_NAME_";
    public static Integer DEFAULT_CLIENT_FREQ = 0;


    /*adds 1 to the client_frequency and displays this string in a new client window*/
    public static String get_default_name()
    {
        return DEFAULT_CLIENT_NAME + DEFAULT_CLIENT_FREQ++;
    }//end get_default_name()


    /*grabs the local IP address of the machine running the program*/
    public static String get_default_ip()
    {
        try
        {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return null;
    }//end get_default_ip()
}//end class
