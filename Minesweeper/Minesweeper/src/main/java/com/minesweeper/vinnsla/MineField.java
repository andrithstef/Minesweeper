package com.minesweeper.vinnsla;

import java.util.ArrayList;
import java.util.Collections;

public class MineField {

    private Cell[][] field;


    private int numCols;
    private int numRows;

    private int numMines;


    /**
     * initialize a minefield with a spceific amount of cols, rows, and mines
     * @param numCols desired number of columns
     * @param numRows desired rows
     * @param numMines desired mines
     * @param pressedRow the row that the user pressed first
     * @param pressedCol the col the user pressed first
     */
    public MineField(int numCols, int numRows, int numMines, int pressedRow, int pressedCol){
        field = new Cell[numCols][numRows];
        this.numCols = numCols;
        this.numRows = numRows;
        this.numMines = numMines;

        //each cell gets designated a Cell object, and they are put in an array
        ArrayList<Cell> cells = new ArrayList<>();

        for (int i = 0; i<numCols;i++){
            for (int j = 0; j<numRows; j++){
                Cell c = new Cell();
                field[i][j] = c;
                cells.add(c);
            }
        }

        //array is shuffled, and the first *numMines* items in that shuffled list will contain a bomb
        Collections.shuffle(cells);
        for(int i = 0; i < numMines; i++) {
            Cell c = cells.get(i);
            c.setBomb(true);
        }

        //We don't want the cell we initially pressed to have a bomb
        //Check if it has a bomb, and if it does we remove it and add a new bomb somewhere else
        Cell pressed = field[pressedCol][pressedRow];
        if (pressed.getBomb()){
            pressed.setBomb(false);
            Cell nextCell = cells.get(numMines);
            nextCell.setBomb(true);
        }
    }

    /**
     * get value from minefield
     * @param col column
     * @param row row
     * @return the value in column col and row row
     */
    public boolean getValue(int col, int row){
        return field[col][row].getBomb();
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
