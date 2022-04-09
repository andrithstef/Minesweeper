package com.minesweeper.vidmot;

import com.minesweeper.vinnsla.MineField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MinesweeperController{
    @FXML
    private GridPane fxMineField;

    @FXML
    private Label fxNumFlags, fxDead, fxTimer;


    //So that I can effectively fetch buttons using their indexes
    private Button[][] buttons;

    private boolean[][] flagged;

    private int numCols;
    private int numRows;
    private int numMines;

    private int numFlags;

    private boolean dead = false;

    private MineField mineField;

    private Timeline t;
    private int time = 0;

    public void initData(int numCols, int numRows, int numMines) {
        this.time = 0;
        this.numCols = numCols;
        this.numRows = numRows;
        this.numMines = numMines;
        this.dead = false;

        buttons = new Button[numCols][numRows];
        flagged = new boolean[numCols][numRows];

        for (int i = 0; i<numCols; i++){
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0/numCols);
            fxMineField.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i<numRows; i++){
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0/numRows);
            fxMineField.getRowConstraints().add(rowConst);
        }
        for (int i = 0; i<numCols; i++){
            for (int j = 0; j<numRows; j++){
                Button b = new Button();
                b.getStyleClass().add("button_normal");
                b.setMinHeight(25);
                b.setMinWidth(25);
                b.setMaxWidth(25);
                b.setMaxHeight(25);
                b.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        handleClick(mouseEvent);
                    }
                });

                fxMineField.add(b,i,j);
                buttons[i][j] = b;
                fxNumFlags.setText(Integer.toString(numMines));
            }
        }
    }

    private void handleClick(MouseEvent mouseEvent){
        if (dead){
            return;
        }
        if (mouseEvent.getButton()==MouseButton.SECONDARY){
            Button pressed = (Button)mouseEvent.getSource();
            Integer colIndex = GridPane.getColumnIndex(pressed);
            Integer rowIndex = GridPane.getRowIndex(pressed);
            flagged[colIndex][rowIndex] = !flagged[colIndex][rowIndex];
            pressed.getStyleClass().clear();
            if (flagged[colIndex][rowIndex]){
                pressed.getStyleClass().add("button_flagged");
                int currFlags = Integer.parseInt(fxNumFlags.getText())-1;
                String newFlags = Integer.toString(currFlags);
                fxNumFlags.setText(newFlags);
            }
            else{
                pressed.getStyleClass().add("button_normal");
                int currFlags = Integer.parseInt(fxNumFlags.getText())+1;
                String newFlags = Integer.toString(currFlags);
                fxNumFlags.setText(newFlags);
            }
        }
        else{
            pressCell(mouseEvent);
        }
    }

    private void pressCell(MouseEvent mouseEvent){
        Button pressed = (Button) mouseEvent.getSource();
        Integer colIndex = GridPane.getColumnIndex(pressed);
        Integer rowIndex = GridPane.getRowIndex(pressed);

        if(mineField == null){
            mineField = new MineField(numCols, numRows, numMines, rowIndex, colIndex);
            numFlags = mineField.getNumMines();
            String newFlags = Integer.toString(numFlags);
            fxNumFlags.setText(newFlags);
            stillaTimeline();
        }



        if (flagged[colIndex][rowIndex]){
            return;
        }

        openCell(colIndex, rowIndex);
    }

    private void openCell(int colIndex, int rowIndex){
        Button pressed = buttons[colIndex][rowIndex];

        boolean mine = mineField.getValue(colIndex,rowIndex);
        pressed.setDisable(true);
        if (mine){
            explode();
        }
        else{
            safeOpen(pressed, colIndex, rowIndex);
        }

    }

    private void safeOpen(Button pressed, int colIndex, int rowIndex){
        pressed.getStyleClass().clear();
        pressed.getStyleClass().add("button_pressed");
        int numNeighbors = mineField.getAmountOfAdjacentMines(colIndex, rowIndex);

        //There are no neighboring bombs, so we open all surrounding cells
        if (numNeighbors != 0){
            String neighbors = Integer.toString(numNeighbors);
            pressed.setText(neighbors);
            return;
        }
        openSurroundingCells(colIndex, rowIndex);

    }

    private void openSurroundingCells(int col, int row){
        //So that we don't go out of bounds
        int minCol = Math.max(col-1, 0);
        int maxCol = Math.min(col+1, numCols-1);

        int minRow = Math.max(row-1, 0);
        int maxRow = Math.min(row+1, numRows-1);

        for (int c = minCol; c <= maxCol; c++){
            for(int r = minRow; r <= maxRow; r++){
                if(!buttons[c][r].isDisable() && !flagged[c][r]){
                    openCell(c, r);
                }
            }
        }
    }

    private void explode(){
        this.dead = true;
        t.pause();
        fxDead.setText("You died");
    }

    /**
     * Update timer each second
     */
    private void stillaTimeline(){
        KeyFrame k = new KeyFrame(Duration.millis(1000),
                e->{
                    updateTimer();
                });
        t = new Timeline(k);
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }

    private void updateTimer(){
        this.time += 1;
        fxTimer.setText(Integer.toString(this.time));
    }

    public void backToMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainmenu-view.fxml"));

        Parent parent = loader.load();
        Scene scene = new Scene(parent, 300, 300);

        Stage stage = (Stage) fxMineField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void restart(ActionEvent actionEvent) throws IOException {
        int width = 25*numCols;
        int height = 26*numRows+50;


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("minesweeper-view.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent, width, height);
        scene.getStylesheets().add(getClass().getResource("minesweeper-look.css").toExternalForm());
        MinesweeperController controller = loader.getController();

        controller.initData(numCols, numRows, numMines);
        Stage stage = (Stage) fxTimer.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}