/*
 *project: Minesweeper
 * name:
 * Constantino Rodriguez
 * Nerijus Gelezinis
 * Giovanni Valencia
  * 
  * image used from http://www.spriters-resource.com/pc_computer/minesweeper/sheet/19849/
 * splice by Daniel Hajnos
  */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * Class tile.
 * 
 */
public class tile extends JToggleButton
{
 boolean mine = false;
 int mineneighbor = 0;
 boolean clicked = false;
 int flag = 0;
 int xcoord;
 int ycoord;

 public tile( String text)
 {
  super.setText( text );
 }

 /**
  * sets the size, set location coordinate
  */
 public tile( int newx, int newy )
 {
  this.setPreferredSize( new Dimension( 25,25 ) );
  this.setFocusPainted( false );
  xcoord = newx;
  ycoord = newy;
 }

 /*
  * Returns number of neighbors that have bomb
  */
 public int getnumneighbor()
 {
  return mineneighbor;
 }

 /*
  * Sets number of neighboring that have bomb
  */
 public void setnumneighbor( int newneighbor )
 {
  mineneighbor = newneighbor;
   
 }

 /*
  *  true if there is mine in the tile
  */
 public boolean istheremine()
 {
  return mine;
 }

 /*
  * Sets mine in the tile
  */
 public void setmine()
 {
  mine = true;
 }

 /*
  * add number of neighbor with bomb
  */
 public void addneighbor(int number)
 {
  mineneighbor += number;
 }

 /*
  * Mark tile as clicked
  */
 public void setclicked()
 {
  this.setEnabled( false );
 }

 /**
  * Check if the tile was clicked
  */
 public boolean isclicked()
 {
  return clicked;
 }
 /*
  * resets a tile
  */
 public void reset()
 {
  setText( "" );
  flag = 0;
  mine = false;
  mineneighbor = 0;
  clicked = false;
  setEnabled( true );
  this.setSelected( false );
  setIcon( null );
 }

 /*
  * Check what flag is on tile
  */
 public int getFlag()
 {
  return flag;
 }

 /*
  * Get x position of tile
  */
 public int getx()
 { 
  return xcoord;
 }

 /*
  * Get y position of tile
  */
 public int gety()
 {
  return ycoord;
 }

 /*
  * Handle left click on this tile
  */
 public void leftClick()
 {
  // set image of number to button
  switch( mineneighbor )
  { 
    
    case 0:
      setIcon( new ImageIcon( "button_pressed.gif" ) );
      setDisabledIcon( new ImageIcon( "button_pressed.gif" ) );
      setEnabled( false );
      break;
      
    case 1:
      setIcon( new ImageIcon( "button_1.gif" ) );
      setDisabledIcon( new ImageIcon( "button_1.gif" ) );
      setEnabled( false );
      break;
   case 2:
     setIcon( new ImageIcon( "button_2.gif" ) );
     setDisabledIcon( new ImageIcon( "button_2.gif" ) );
     setEnabled( false );
    break;

   case 3:
     setIcon( new ImageIcon( "button_3.gif" ) );
     setDisabledIcon( new ImageIcon( "button_3.gif" ) );
     setEnabled( false );
    break;

   case 4:
     setIcon( new ImageIcon( "button_4.gif" ) );
     setDisabledIcon( new ImageIcon( "button_4.gif" ) );
     setEnabled( false );
    break;
    
   case 5:
     setIcon( new ImageIcon( "button_5.gif" ) );
     setDisabledIcon( new ImageIcon( "button_5.gif" ) );
     setEnabled( false );
    break;

    
   case 6:
     setIcon( new ImageIcon( "button_6.gif" ) );
     setDisabledIcon( new ImageIcon( "button_6.gif" ) );
     setEnabled( false );
    break;
    
   case 7:
    setIcon( new ImageIcon( "button_7.gif" ) );
     setDisabledIcon( new ImageIcon( "button_7.gif" ) );
     setEnabled( false );
    break;
    
   case 8:
   setIcon( new ImageIcon( "button_8.gif" ) );
     setDisabledIcon( new ImageIcon( "button_8.gif" ) );
     setEnabled( false );
    break;
   default:
    break;
  } 

  if( clicked )
  {
   setSelected( true );
   return;
  }

  // if flag is activate , it can't be click
  if( flag == 1)
   setSelected( false );
  else
  {
   setSelected( true );
   clicked = true;

   
  }
 }
 
 /*
  * Handle right click on that square
  */
 public int rightClick()
 {
  if( !clicked )
  {
   switch( flag )
   {
    // flag a tile
    case 0:
     flag = 1;
     setIcon( new ImageIcon( "button_flag.gif" ) );
     setDisabledIcon( new ImageIcon( "button_flag.gif" ) );
     setEnabled( false );
     return -1;

    // mark a tile with ?
    case 1:
     flag = 2;
     setIcon( new ImageIcon( "button_question.gif" ) );
     setDisabledIcon( new ImageIcon( "button_question.gif" ) );
     setEnabled( false );
     return 1;

    // make it empty tile
    case 2:
     flag = 0;
     setIcon( null );
     setEnabled( true );
     return 0;

    default:
     break;
   }
  }
  return 0;
 }
}