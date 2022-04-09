package com.minesweeper.vinnsla;

public class Cell {
    private boolean bomb;

    public Cell(){
        this.bomb = false;
    }

    public void setBomb(boolean b){
        this.bomb = b;
    }

    public boolean getBomb(){
        return this.bomb;
    }
}
