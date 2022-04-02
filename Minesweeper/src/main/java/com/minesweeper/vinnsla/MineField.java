package com.minesweeper.vinnsla;

public class MineField {

    private boolean[][] field;

    private int numCols;
    private int numRows;

    private int numMines = 0;

    public MineField(int numCols, int numRows){
        field = new boolean[numCols][numRows];
        this.numCols = numCols;
        this.numRows = numRows;
        for (int i = 0; i<numCols;i++){
            for (int j = 0; j<numRows; j++){
                if (Math.random() > 0.9){
                    field[i][j] = true;
                    numMines += 1;
                }
                else{
                    field[i][j] = false;
                }
            }
        }
    }

    /**
     * get value from minefield
     * @param col column
     * @param row row
     * @return the value in column col and row row
     */
    public boolean getValue(int col, int row){
        return field[col][row];
    }

    /**
     * returns the amount of bombs in neighboring cells
     * @param col column index
     * @param row row index
     * @return number of bombs in the eight surrounding cells
     */
    public int getAmountOfAdjacentMines(int col, int row){
        int neighbors = 0;

        //So that we don't go out of bounds
        int minCol = Math.max(col-1, 0);
        int maxCol = Math.min(col+1, numCols-1);

        int minRow = Math.max(row-1, 0);
        int maxRow = Math.min(row+1, numRows-1);


        for (int c = minCol; c <= maxCol; c++){
            for(int r = minRow; r <= maxRow; r++){
                if (getValue(c,r)){
                    //There is a bomb in the cell, add 1 to neighbors
                    neighbors += 1;
                }
            }
        }
        return neighbors;
    }

    public int getNumMines(){
        return numMines;
    }

}
