//
// Tetro:: Handles positioning and rotation logic
//          for individual pieces
//
// <<Filip Variciuc>> <<Constantino Rodriguez>> <<Lukasz Przybos>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.util.*;

class Tetro
{
    private int[][] coordinates;
    private String type;
    private int size;
    public boolean vertical;
    Random random = new Random();

    /*
     * num: 0 1 2 3 4 5 6 To this shape: I J L O S T Z
	 */


    // randomly pick a tetro and randomly rotate it
    public Tetro()
    {
        // random tetro type
        int num = random.nextInt(7);
        coordinates = setCoord(num);
    }


    public void rotateRight()
    {
        // The 'O' does not need to waste time rotating.
        if (!Objects.equals(this.type, "O"))
        {
            if (!Objects.equals(this.type, "I"))
            {
                for (int i = 0; i <= (this.size - 1) / 2; i++)
                    for (int j = i; j < this.size - i - 1; j++)
                    {
                        int p1 = this.coordinates[i][j], p2 = this.coordinates[j][this.size - i - 1],
                                p3 = this.coordinates[this.size - i - 1][this.size - j - 1],
                                p4 = this.coordinates[this.size - j - 1][i];

                        this.coordinates[j][this.size - i - 1] = p1;
                        this.coordinates[this.size - i - 1][this.size - j - 1] = p2;
                        this.coordinates[this.size - j - 1][i] = p3;
                        this.coordinates[i][j] = p4;
                    }

                this.vertical = !this.vertical;
                return;
            }

            for (int i = 0; i < this.size; i++)
                for (int j = 0; j < this.size; j++)
                    this.coordinates[i][j] = 0;

            if (Objects.equals(this.type, "I"))
                if (vertical)
                    this.coordinates[2][0] = this.coordinates[2][1] = this.coordinates[2][2] = this.coordinates[2][3] = 1;
                else
                    this.coordinates[0][1] = this.coordinates[1][1] = this.coordinates[2][1] = this.coordinates[3][1] = 1;
            this.vertical = !this.vertical;
        }
    }


    // Set a Tetromino piece.
    private int[][] setCoord(int type)
    {
        int[][] value = new int[3][3];
        this.size = 3;
        vertical = true;
        switch (type)
        {
            // I shape
            case 0:
                value = new int[4][4];
                value[0][1] = value[1][1] = value[2][1] = value[3][1] = 1;

                this.type = "I";
                this.size = 4;
                break;
            // J shape
            case 1:
                value[0][1] = value[1][1] = value[2][0] = value[2][1] = 1;
                this.type = "J";
                break;
            // L shape
            case 2:
                value[0][1] = value[1][1] = value[2][1] = value[2][2] = 1;
                this.type = "L";
                break;
            // o shape
            case 3:
                value = new int[4][4];
                value[1][1] = value[1][2] = value[2][1] = value[2][2] = 1;
                this.type = "O";
                this.size = 4;
                break;
            // s shape
            case 4:
                vertical = false;
                value[0][1] = value[0][2] = value[1][1] = value[1][0] = 1;
                this.type = "S";
                break;
            // T shape
            case 5:
                vertical = false;
                value[0][1] = value[1][0] = value[1][1] = value[1][2] = 1;
                this.type = "T";
                break;
            // Z shape
            case 6:
                vertical = false;
                value[0][0] = value[0][1] = value[1][1] = value[1][2] = 1;
                this.type = "Z";
                break;
        }
        return value;
    }


    // return graph of the Tetromino
    public int[][] getCoord()
    {
        return coordinates;
    }


    // return type of Tetromino
    public String getType()
    {
        return type;
    }
} // end class
