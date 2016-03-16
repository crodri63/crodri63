//
// RedException :: Inherits from Exception - Responsible for throwing exceptions with descriptive error messages
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import javax.swing.*;
import java.awt.*;

public class RedException extends Exception
{
    /*Display a JOptionPane with a RED background (indicating an error)
      coupled with an appropriate error description*/
    public RedException(String description)
    {
        Object paneBG = UIManager.get("OptionPane.background");
        Object panelBG = UIManager.get("Panel.background");
        UIManager.put("OptionPane.background", new Color(200, 50, 0));
        UIManager.put("Panel.background", new Color(200, 50, 0));

        JOptionPane.showMessageDialog(null, "**FATAL**\n" + description);

        UIManager.put("OptionPane.background", paneBG);
        UIManager.put("Panel.background", panelBG);
    }//end constructor
}//end class()