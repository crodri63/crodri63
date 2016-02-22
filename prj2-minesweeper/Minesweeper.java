/*
 *project: Minesweeper
 * name:
 * progammmer:Constantino Rodriguez
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
import java.io.*;

public class Minesweeper extends JFrame implements MouseListener, ActionListener
{
 private int rows = 10;
 private int columns = 10;
 private int mines = 10;
 
 private JLabel minelefttxt;
 private JLabel timetxt;
 private JButton startbutton;
 
 private tile [][] tiles = new tile[rows][columns];
 
 private boolean started = false;
 private boolean finished = false;
 private int currentTime = 0;
 private javax.swing.Timer timer;
 
 private int minesLeft = mines;
 private int fieldsLeft = (rows * columns) - minesLeft;
 

 
 private JMenuItem newGameitem;
 private JMenuItem quitItem;
 private JMenuItem itemHelp;
 private JMenuItem itemAbout;
 private JMenuItem itemScore;
 private JMenuItem resetScore;
 
 String filename;
 private String [] names = new String [10];
 private int [] scores = new int[10];
 boolean addNewScore;
 

  private JPanel field;

 /*
  * minesweeper gui 
  */
 Minesweeper()
 {
  Container contPanel = getContentPane();
  getContentPane().setLayout( new BorderLayout() );
  this.setTitle( "MineSweeper " );

  // GridLayout
  JPanel scoresPanel = new JPanel( new GridLayout( 2, 3, 5, 3 ) );

   // mine left label and add it to gridlayout
   JLabel mineleftlabel = new JLabel("Mines Left");
   mineleftlabel.setHorizontalAlignment( JLabel.CENTER );
   scoresPanel.add( mineleftlabel );

   // restart button / smiley face and add it to gridlayout
   startbutton = new JButton( new ImageIcon("smile_button.gif") );
   startbutton.setPreferredSize( new Dimension( 25, 25 ) );
   startbutton.addMouseListener( this );
   scoresPanel.add( startbutton );

   // time label and add it to layout
   JLabel timelabel = new JLabel( "Time" );
   timelabel.setHorizontalAlignment( JLabel.CENTER );
   scoresPanel.add( timelabel, 2 );
   
   // number of mine left and add it to layout
   minelefttxt = new JLabel( "" + minesLeft );
   minelefttxt.setHorizontalAlignment( JLabel.CENTER );
   minelefttxt.setForeground( Color.pink );
   minelefttxt.setFont( new Font( "TimesRoman", Font.BOLD, 20 ) );
   scoresPanel.add( minelefttxt );

   scoresPanel.add( new JLabel("") );

   // show time watch and add it to gridlayout
   timetxt = new JLabel( "ready" );
   timetxt.setHorizontalAlignment( JLabel.CENTER );
   timetxt.setForeground( Color.pink );
   timetxt.setFont( new Font( "TimesRoman", Font.BOLD, 20 ) );
   scoresPanel.add( timetxt );

  contPanel.add( scoresPanel, BorderLayout.NORTH );
  Createminefield();

  // create timer object to measure time
  timer = new javax.swing.Timer( 1000, this );
  
  //fills up the array in case there is no file
  for (int i = 0; i < 10; i++){
    names[i] = "__________";
    scores[i] = 999;
  }
  //if the scores file exists it tries to take in the information from the scores file
  filename = "scores.txt";
  String line = null;
  int i = 0;
  try 
  {
    FileReader fileReader = new FileReader(filename);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    while((line = bufferedReader.readLine()) != null) 
    {
      Scanner scan = new Scanner(line);
      if (i < 10)
      {
        if (scan.hasNext())
        {
          String s = scan.next();
          if (scan.hasNextInt())
          {
            names[i] = s;
            scores[i] = scan.nextInt();
          }
        }
      }
      scan.close();
      i++;
    }
    bufferedReader.close();
  }
  catch(FileNotFoundException ex) 
  {
  }
  catch(IOException ex) 
  {
  }
  addNewScore = true;

 }

 /*
  * 2d array of tile
  */
 private void Createminefield()
 {
  super.setSize( (int)(25 * columns) , (int)(30 * rows) );

  field = new JPanel( new GridLayout( rows, columns, 1, 1 ) );
  
  // two loop that create a minefield made  of tile
  for( int i = 0 ;  i < rows ; i++ )
   for( int j = 0 ;  j < columns ; j++)
   {
    tiles[i][j] = new tile( i, j );
    tiles[i][j].addMouseListener( this );
    field.add( tiles[i][j] );
   }  

  getContentPane().add( field, BorderLayout.CENTER );
 }

 /**
  * reset the minefield
  */
 private void resetField()
 {
  for( int i = 0 ; i < rows ; i++ )
   for( int j = 0 ; j < columns ; j++)
   {
    tiles[i][j].reset();
   }  
  
  started = false;
  finished = false;
  currentTime = 0;
  minesLeft = mines;
  fieldsLeft = rows * columns - minesLeft;
  minelefttxt.setText( "" + minesLeft );
  timetxt.setText( "00" + currentTime );
  startbutton.setIcon( new ImageIcon("smile_button.gif") );
  addNewScore = true;

 }


 /*
  * randomized mines and add number of neighbor with bomb
  * 
  */
 private void RandomMine()
 {
  Random randInt = new Random();
  int randCol, randRow;
  int i, j;

  // place mines in random order
  for( i = 0 ; i < mines ; i++ )
  {
   randCol = randInt.nextInt( rows );
   randRow = randInt.nextInt( columns );
   
   // check if mine  exist on that x and y , then decrease number of mine to give
   if( !tiles[ randCol ][ randRow ].istheremine() && !tiles[ randCol ][ randRow ].isSelected() )
    tiles[ randCol ][ randRow ].setmine();
   else
    i--;
  }

  //marking the neighbor mine
  for( i = 0 ; i < rows ; i++ )
   for( j = 0 ; j < columns ; j++)
   {
    if( tiles[i][j].istheremine() )
    {
     if( i - 1 >= 0 && j - 1 >= 0 ) // upper left square
     {
      tiles[i - 1][j - 1].addneighbor( 1 );
     }
     if( i - 1 >= 0 && j >= 0 ) // upper middle square
     {
      tiles[i - 1][j].addneighbor( 1 );
     }
     if( i - 1 >= 0 && j + 1 < columns ) // upper right square
     {
      tiles[i - 1][j + 1].addneighbor( 1 );
     }
     if( i >= 0 && j - 1 >= 0 ) // middle left square
     {
      tiles[i][j - 1].addneighbor( 1 );
     }
     if( i >= 0 && j + 1 < columns ) // middle right square
     {
      tiles[i][j + 1].addneighbor( 1 );
     }
     
     if( i + 1 < rows && j - 1 >= 0 ) // lower left square
     {
      tiles[i + 1][j - 1].addneighbor( 1 );
     }
     if( i + 1 < rows && j >= 0 ) // lower middle square
     {
      tiles[i + 1][j].addneighbor( 1 );
     }
     if( i + 1 < rows && j + 1 < columns ) // lower left square
     {
      tiles[i + 1][j + 1].addneighbor( 1 );
     }
    }
   }
   }

 
 /* 
  *  menubar
  */
 private JMenuBar menu()
 {
  JMenuBar menubar = new JMenuBar();

  JMenu menugame = new JMenu( "Game" );
  
  // Alt + N for New Game hotkey
   newGameitem = new JMenuItem( "New Game", KeyEvent.VK_N );
   newGameitem.addActionListener( this );
   newGameitem.setMnemonic(KeyEvent.VK_N);
   newGameitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
   
   //Alt + Q =  Quit hotkey
   quitItem = new JMenuItem( "Quit", KeyEvent.VK_Q );
   quitItem.addActionListener( this );
   quitItem.setMnemonic(KeyEvent.VK_Q);
   quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
   
   //Alt + S = display scores hotkey
   itemScore = new JMenuItem( "Score", KeyEvent.VK_S );
   itemScore.addActionListener( this );
   itemScore.setMnemonic(KeyEvent.VK_S);
   itemScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
   
   //Alt + R = Reset Scores hotkey
   resetScore = new JMenuItem("Reset Scores", KeyEvent.VK_R);
   resetScore.addActionListener( this );
   resetScore.setMnemonic(KeyEvent.VK_R);
   resetScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
   
   menugame.add( newGameitem );
   menugame.add( quitItem); 
   menugame.add( itemScore );
   menugame.add( resetScore );

  JMenu menuHelp = new JMenu( "Help" );
  //Alt + L = Instructions hotkey
   itemHelp = new JMenuItem( "Instructions", KeyEvent.VK_L );
   itemHelp.addActionListener( this );
   itemHelp.setMnemonic(KeyEvent.VK_L);
   itemHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
   
   //Alt + A = About hotkey
   itemAbout = new JMenuItem( "About", KeyEvent.VK_A );
   itemAbout.addActionListener( this );
   itemAbout.setMnemonic(KeyEvent.VK_A);
   itemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
   menuHelp.add( itemHelp );
   menuHelp.add( itemAbout );

  menubar.add( menugame );  
  menubar.add( menuHelp );
  

  return menubar;
 }

 /*
  *check the end game condition
  */
 private void endgame( boolean lost )
 {
 
  ImageIcon winIcon = new ImageIcon( "button_flag.gif" );
  ImageIcon lostIcon = new ImageIcon( "button_bomb_pressed.gif" );
   // stop the time counter
  finished = true;
  timer.stop();
  
  if( lost )
   startbutton.setIcon( new ImageIcon( "head_dead.gif"));
                          
  // show all mine and disable button
  for( int i = 0 ; i < rows ; i++ )
   for( int j = 0 ; j < columns ; j++ )
   {
    //  the game is lost
    if( lost )
    {
     // show the wrong flag marked title
     if( tiles[i][j].getFlag() == 1 )
     {
      tiles[i][j].setIcon( new ImageIcon( "button_bomb_x.gif" ) );
      tiles[i][j].setDisabledIcon( new ImageIcon("button_bomb_x.gif") );
     }

     // all mines shown
     if( tiles[i][j].istheremine() )
     {
      tiles[i][j].setIcon( new ImageIcon("button_bomb_pressed.gif") );
      tiles[i][j].setDisabledIcon( new ImageIcon("button_bomb_pressed.gif") );
      tiles[i][j].setEnabled( false );
     }
     else
      tiles[i][j].setEnabled( false );
    }

    // the game is won
    else
    {
     // mark all tile with mine with a flag
     if( tiles[i][j].istheremine() )
     {
      tiles[i][j].setIcon( winIcon );
      tiles[i][j].setDisabledIcon( winIcon );
      tiles[i][j].setEnabled( false );
     }
     else
     {
      tiles[i][j].setEnabled( false );
     }
     if (addNewScore)
     {
       String playerName = (String) JOptionPane.showInputDialog("Name: ");
       addScore(playerName, currentTime);
       writeScores();
     }
     //////////////////////
     ////////////////////
     //////////////////////
     ////////////////////
     /////////////////////
     /////////////////////
     /////////////////////
     ////////////////////
     //////////////////////
     /////////////////////
    }
   }
 }

 /*
  * reveal all zero tile
  */
 private void showZeros( tile theSquare )
 {
  int posX = theSquare.getx();
  int posY = theSquare.gety();

  checkForZeros( posX, posY );
   return;
 }

 /*
  * go through all tile with zero and mark them clicked
  * 
  */
 private void checkForZeros( int x, int y )
 {
  if( x < rows && y < columns && x >= 0 && y >= 0 && !tiles[x][y].isclicked() )
  {
   tiles[x][y].leftClick();
   fieldsLeft--;

   if( tiles[x][y].getnumneighbor() == 0 )
   {
    checkForZeros( x - 1, y - 1 );
    checkForZeros( x , y - 1 );
    checkForZeros( x + 1, y - 1 );
    checkForZeros( x - 1, y  );
    checkForZeros( x + 1, y  );
    checkForZeros( x - 1, y + 1 );
    checkForZeros( x , y + 1 );
    checkForZeros( x + 1, y + 1 );
   }
  }
 }

/*
 * 
 * MOUSE HANDLER
 *//////
 /*
  * changes an icon on the start button 
  * when the left mouse button is pressed
  */
 public void mousePressed(MouseEvent m)
 {
  startbutton.setIcon( new ImageIcon("head_o.gif") );
 }

 /**
  * changes an icon on the start button 
  * when the left mouse button is released
  */
 public void mouseReleased(MouseEvent m)
 {
  startbutton.setIcon( new ImageIcon("smile_button.gif") );
 }

 public void mouseEntered(MouseEvent m)
 {}

 public void mouseExited(MouseEvent m)
 {}

 /**
  *  Handles left and right clicks of a mouse
  */
 public void mouseClicked(MouseEvent m)
 {
  int button = m.getButton();

  // if start button  clicked,  stop the timer and reset the minefield
  if( m.getSource() == (JButton)startbutton )
  {
   timer.stop();
   rows = 10;
   columns = 10;
   resetField();
   return;
  }

  tile ti = (tile)m.getSource();

  //  left mouse button was clicked
  if( button == 1 && !finished )
  {
   // if in the clicked tile is mine and it is not flagged -> end game
   if( ti.istheremine() && ti.getFlag() != 1 )
   {
    ti.setSelected( false );
    endgame( true );    
    return;
   }

   // if  clicked the first tile after start the game
   if( !started )
   {
    RandomMine();
    started = true;
    timer.start();
   }

   // not clicked and not flagged --> tile is clicked
   if( !ti.isclicked() && ti.getFlag() != 1 )
   {   
    if( ti.getnumneighbor() == 0 )
     showZeros( ti );
    else
     fieldsLeft--;
   }
   // let tile click
   if( !ti.isclicked() && ti.getFlag() != 1 )
    ti.leftClick();
   
  }
  // right button click
  else if( button == 3 )
  {
   minesLeft += ti.rightClick();
  }

  // no empty tile -> victory
  if( fieldsLeft == 0 )
  {
   finished = true;
   startbutton.setIcon( new ImageIcon( "head_glasses.gif" ) );
   timer.stop();
   endgame( false );
  }

  minelefttxt.setText( "" + minesLeft );
 }

 /**
  *  menu selections
  */
 public void actionPerformed( ActionEvent a )
 {
  Object clicksource = a.getSource();
  String help = "Left click: uncovers a field (or multiple fields) \n" + "as long the tile is not flag \n" + " Right Click: mark a tile with flag , ? or clear flag.\n" + " win: if uncovered all the field that have no mines. \n" + " Numbers show amount neighbor that have mine \n";
  String author = " Programmers: Constantino Rodriguez, Nerijus Gelezinis, and Giovanni Valencia";
  String score = "";
  for (int i = 0; i < 10; i++){
    score = score + this.names[i] + ": " + this.scores[i] + "\n";
  }

  if( clicksource == itemAbout )
  {
   JOptionPane.showMessageDialog(itemAbout, author, "About", JOptionPane.INFORMATION_MESSAGE );
  }
  
  if( clicksource == resetScore )
  {
    for(int i = 0; i < 10; i++)
     {
       names[i] = "Player";
       scores[i] = 0;
     }
    writeScores();
  }

  else if( clicksource == itemHelp )
  {
   JOptionPane.showMessageDialog( itemHelp, help, "Help", JOptionPane.INFORMATION_MESSAGE );
  }
  
  else if( clicksource == itemScore )
  {
   JOptionPane.showMessageDialog( itemScore, score, "Score", JOptionPane.INFORMATION_MESSAGE );
  }

  else if( clicksource == quitItem )
  {
   System.exit( 0 );
  }

  else if( clicksource == newGameitem )
  {
   timer.stop();
   resetField();
   return;
  }
  // timer increase every one second
  else if( clicksource == timer )
  {
   currentTime++;

   if( currentTime < 10 )
    timetxt.setText( "00" + currentTime );
   else if( currentTime < 100 )
    timetxt.setText( "0" + currentTime );
   else
    timetxt.setText( "" + currentTime );
  }
 }
 
 //private String askName()
 //{
 //  String playerName;
 //}
 
 private void writeScores()
 {
   try
   {
     FileWriter fileWriter = new FileWriter(filename);
     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
     for(int i = 0; i < 10; i++)
     {
       bufferedWriter.write(names[i] + " " + scores[i] + "\n");
     }
     bufferedWriter.close();
   }
   catch(IOException ex)
   {
     
   }
 }
 
 private void addScore(String newName, int newScore)
 {
   int i = 0;
   while (newScore >= scores[i])
   {
     i++;
   }
   int tempScore;
   String tempName;
   for (; i < 10; i++)
   {
     tempScore = scores[i];
     tempName = names[i];
     scores[i] = newScore;
     names[i] = newName;
     newScore = tempScore;
     newName = tempName;
     System.out.print (tempScore + " " + scores[i] + " " + newScore + "\n");
   }
   addNewScore = false;
   
 }
 /*
  * Main method to start the game
  */
 public static void main( String args[] )
 {
  Minesweeper minesweepers = new Minesweeper();
  minesweepers.setJMenuBar( minesweepers.menu() );
  minesweepers.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
  minesweepers.setVisible( true );
  minesweepers.setResizable( false );
 }

}
