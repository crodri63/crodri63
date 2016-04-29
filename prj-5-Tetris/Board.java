//
// Board:: Controls the behavior of the board,
//          signals when a row is full, when a piece rotates,
//          when the game is over, etc
//
// <<Filip Variciuc>> <<Constantino Rodriguez>> <<Lukasz Przybos>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.util.ArrayList;

class Board
{
    private Tetris mainframe;   //reference to the central object

    protected String[][] matrix;
    // keep track of center of the tetro grid
    private int activeWEcoord; // Active X coordinate of the Tetromino
    private int activeNScoord; // Active Y coordinate of the Tetromino
    private Tetro activeTetro; // The Tetromino Object itself
    protected Tetro nextTetro; // The next Tetromino Object


    public Board(Tetris mainframe)
    {
        this.mainframe = mainframe;
        matrix = new String[20][10];
    }


    public void add(Tetro t)
    {
        // add the brick to the list
        activeTetro = t;
        activeWEcoord = 3;
        activeNScoord = 0;
    }


    public void add()
    {
        activeTetro = new Tetro();
        activeWEcoord = 3;
        activeNScoord = 0;
    }


    public void next()
    {
        this.nextTetro = new Tetro();
    }


    //////////////////////////////////////////////////////////////////////////////
    // these are mostly deal with checking the rows if it full , then it delete
    ////////////////////////////////////////////////////////////////////////////// that
    ////////////////////////////////////////////////////////////////////////////// line
    public void checkRows()
    {
        ArrayList<Integer> rowsToClear = new ArrayList<>();
        for (int ns = 0; ns < matrix.length; ns++)
        {
            boolean clearQ = true;
            for (int we = 0; we < matrix[0].length; we++)
                if (matrix[ns][we] == null)
                    clearQ = false;
            if (clearQ)
                rowsToClear.add(ns);
        }
        if (rowsToClear.size() != 0)
            clearRows(rowsToClear);

        mainframe.sidebar.update_score(rowsToClear.size());
    }


    // Remove any completed rows.
    public void clearRows(ArrayList<Integer> rows)
    {
        for (int r : rows)
            for (int we = 0; we < matrix[0].length; we++)
                matrix[r][we] = null;
        settle(rows);
    }


    public void settle(ArrayList<Integer> rows)
    {
        for (int r : rows)
            for (int ns = r; ns > 0; ns--)
                System.arraycopy(matrix[ns - 1], 0, matrix[ns], 0, matrix[0].length);
    }


    // check for surrounding area of the active Tetromino
    public boolean collisionCheck(int dwe, int dns, Tetro t)
    {
        int[][] tMatrix = t.getCoord();
        int actwe = activeWEcoord, actns = activeNScoord;

        int length = tMatrix.length;

        for (int ns = 0; ns < length; ++ns)
            for (int we = 0; we < length; ++we)
                if (tMatrix[ns][we] == 1)
                    if (actwe + we + dwe < 0 // Left Board check
                        || actwe + we + dwe >= matrix[0].length // Right
                        || actns + ns + dns >= matrix.length // bottom Board
                        || matrix[actns + ns + dns][actwe + we + dwe] != null)
                        return true;
        return false;
    }


    // Checks predicted location of rotation. If it collides, avoid rotating.
    private boolean tetroCheck()
    {
        int[][] tMatrix = activeTetro.getCoord();
        int actwe = activeWEcoord, actns = activeNScoord, length = tMatrix.length;

        // 'I' special case
        if (length == 4)
        {
            if (activeTetro.vertical)
            {
                for (int i = 0; i < length; ++i)
                    if (matrix[actns][actwe + i] != null)
                        return true;
            }
            else
            {
                for (int i = 0; i < length; ++i)
                    if (matrix[actns + i][actwe] != null)
                        return true;
            }
        }
        else
        {
            for (int i = 0; i < length; ++i)
                for (int j = 0; j < length; ++j)
                    if (tMatrix[i][j] == 1)
                    {
                        int newY = length - i - 1;

                        if (tMatrix[j][newY] != 1)
                            if (matrix[actns + j][actwe + newY] != null)
                                return true;
                    }
        }
        return false;
    }


    // User wants to move the Tetromino left..
    public void left()
    {
        if (!collisionCheck(-1, 0, activeTetro))
            activeWEcoord--;
    }


    // User wants to move the Tetromino right..
    public void right()
    {
        if (!collisionCheck(1, 0, activeTetro))
            activeWEcoord++;
    }


    // check if we can safely rotate the Tetromino...
    public boolean checkRotation()
    {
        // Get current position of the core Tetromino piece
        int actwe = activeWEcoord, actns = activeNScoord;

        // If the piece is in a corner, we can't rotate it without array out of
        // bounds.
        if (actwe == -1 || actwe == 8 || actns == 18)
            return false;

        else if (activeTetro.getType().equals("I"))
        { // Tetromino 'I' has a
            // special extension of
            // 2 blocks.
            if ((collisionCheck(-1, 0, activeTetro) || (collisionCheck(2, 0, activeTetro))) && activeTetro.vertical)
                return false;
        }
        else if (tetroCheck())
            return false;

        return true;
    }


    // User wants to rotate the Tetromino...
    public void rotate()
    {
        activeTetro.rotateRight();
    }


    //advance the piece further down
    public void gravity()
    {
        if (!collisionCheck(0, 1, activeTetro))
            activeNScoord++;
        else if (activeNScoord < 3 && (activeWEcoord == 2 || activeWEcoord == 3))
            this.mainframe.gameOver = true;
        else
            lockTetro();
    }


    //prevent a tetro from moving after it has falled to the bottom
    public void lockTetro()
    {
        int[][] tMatrix = activeTetro.getCoord();
        for (int ns = 0; ns < tMatrix.length; ns++)
        {
            for (int we = 0; we < tMatrix[0].length; we++)
            {
                if (tMatrix[ns][we] == 1)
                {
                    matrix[activeNScoord + ns][activeWEcoord + we] = activeTetro.getType();
                }
            }
        }
        this.add(nextTetro);
        this.next();
        this.checkRows();
    }


    public void setTetro()
    {
        int[][] tMatrix = activeTetro.getCoord();
        int actwe = activeWEcoord, actns = activeNScoord;
        for (int ns = 0; ns < tMatrix.length; ns++)
        {
            for (int we = 0; we < tMatrix[0].length; we++)
            {
                if (tMatrix[ns][we] == 1)
                {
                    matrix[actns + ns][actwe + we] = activeTetro.getType();
                }
            }
        }
    }


    //delete a tetro from the board
    public void removeTetro()
    {
        int[][] tMatrix = activeTetro.getCoord();
        int actwe = activeWEcoord; // this.getActiveWE();
        int actns = activeNScoord;// this.getActiveNS();
        for (int ns = 0; ns < tMatrix.length; ns++)
        {
            for (int we = 0; we < tMatrix[0].length; we++)
            {
                if (tMatrix[ns][we] == 1)
                {
                    matrix[actns + ns][actwe + we] = null;
                }
            }
        }
    }


    //clear the board of all pieces, setting each position to null
    public void clearBoard()
    {
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 10; j++)
                matrix[i][j] = null;
    }
}
