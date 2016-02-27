/*
 *project: Minesweeper
 * name:
 * progammmer:Constantino Rodriguez
 * Nerijus Gelezinis
 * Giovanni Valencia
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class tileTest 
{
  tile button = new tile ( 5, 2);
  tile button2 =  new tile ( 10 , 5);
  
  @Test
  public void testreturnXpos()
  {
    assertEquals(" button x position should be 5: " , 5 , button.getx());
    assertEquals(" button2 x position should be 10: " , 10 , button2.getx());
  }
  
  @Test 
  public void testreturnYpos()
  {
    assertEquals("button y position should be 2: " , 2 , button.gety() );
    assertEquals("button2 y position should be 5: " , 5 , button2.gety() );
  }
  
  @Test 
  public void testsetmine()
  {
    assertEquals(" button should not have mine  " ,false , button.istheremine());
    button.setmine();
    assertEquals(" button should have bomb after setmine " , true, button.istheremine() );
  }
  
  @Test
  public void testsetneighbor()
  {
    button.setnumneighbor(5);
    button2.setnumneighbor (4);
    assertEquals(" button should have 5 mine neighbor " , 5 , button.getnumneighbor());
    button.addneighbor(3);
    assertEquals(" button should have 8 mine neighbot " ,8, button.getnumneighbor());
    assertNotSame(" the two button should have different number of mine neighbor " , button.getnumneighbor(), button2.getnumneighbor());
  }
  
  @Test
  public void testrightclick()
  {
    assertEquals("button should be blank tile " , 0 , button.getFlag() );
    button.rightClick();
    assertEquals("button should be flag tile after rightclick " , 1, button.getFlag() );
    button.rightClick();
    assertEquals(" button should be question mark tile after right click again " , 2 , button.getFlag());
    button.rightClick();
    assertEquals (" button should be blank tile after rightclick " , 0, button.getFlag() );
  }
  
  @Test
  public void testleftclick()
  {
    assertEquals (" button should not be click " , false , button.isclicked() );
    button.leftClick();
    assertEquals(" button should be click ", true , button.isclicked()  );
    button.leftClick();
    assertEquals(" button should remain click after clicking it twice" , true , button.isclicked() );                
  }
  
  @Test
  public void testResetTile()
  {
    tile button3 =  new tile ( 5, 2);
    tile button4 = new tile (5,2);
    assertSame(" button3 and button4 should have no mine " , button3.istheremine() , button4.istheremine());
    button3.setmine();
    assertNotSame("button3 have mine  and button4  have no mine " , button3.istheremine() , button4.istheremine());
    button3.reset();
    assertSame(" button3 and button4 should have no mine after reset() " , button3.istheremine() , button4.istheremine()); 
  }
  
  
  
}
                       