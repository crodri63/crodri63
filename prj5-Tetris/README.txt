Program 5- Tetris
Filip Variciuc, Constantino Rodriguez, Lukasz Przybos

Design Patterns used:

Factory:

In the Tetro.java file, we used Tetro() to get a tetromino from setCoord(int type) which created many different types of tetrominos.

private int[][] setCoord(int type){
int[][] value = new int[3][3];
this.size = 3;
vertical = true;

switch (type){
case 0:
value = new int[4][4];
value[0][1] = value[1][1] = value[2][1] = value[3][1] = 1;
this.type = "I";
this.size = 4;
break;
case 1:
value[0][1] = value[1][1] = value[2][0] = value[2][1] = 1;
this.type = "J";
break;

…
}

 public Tetro()
    {
        // random tetro type
        int num = random.nextInt(7);
        coordinates = setCoord(num);
    }


Observer

Inside of sideBar.java, we used update_score(int lines_cleared) to update scores,  
reduce the number of current_goal and get the nextTetromino to be displayed on the sidebar. 
This method is called automatically when a row is cleared in the Board class, namely the checkRows() method.
