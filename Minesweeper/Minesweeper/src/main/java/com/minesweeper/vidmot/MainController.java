package com.minesweeper.vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.HashMap;


public class MainController {
    private final int EASY = 1;
    private final int MEDIUM = 2;
    private final int HARD = 3;
    private final int CUSTOM = 0;

    @FXML
    private Button fxStartButton, fxEasy, fxMedium, fxHard, fxCustom;

    @FXML
    private TextField fxCols, fxRows, fxMines;

    private int gameType;

    public void startGame(ActionEvent actionEvent) throws IOException {
        HashMap<String, Integer> data = getGameData();

        if (data == null){
            return;
        }

        int numCols = data.get("cols");
        int numRows = data.get("rows");
        int numMines = data.get("mines");

        int width = 25*numCols;
        int height = 26*numRows+50;


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("minesweeper-view.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent, width, height);
        scene.getStylesheets().add(getClass().getResource("minesweeper-look.css").toExternalForm());
        MinesweeperController controller = loader.getController();

        controller.initData(numCols, numRows, numMines);
        Stage stage = (Stage) fxCols.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private HashMap<String, Integer> getGameData(){
        HashMap<String, Integer> data = new HashMap<>();
        if (gameType == EASY){
            data.put("rows", 10);
            data.put("cols", 10);
            data.put("mines", 10);
        }
        else if (gameType == MEDIUM){
            data.put("rows", 20);
            data.put("cols", 20);
            data.put("mines", 60);
        }
        else if (gameType == HARD){
            data.put("rows", 30);
            data.put("cols", 40);
            data.put("mines", 400);
        }
        else if (gameType == CUSTOM){
            String cols = fxCols.getText();
            String rows = fxRows.getText();
            String mines = fxMines.getText();

            try {
                int numRows = Integer.parseInt(rows);
                int numCols = Integer.parseInt(cols);
                int numMines = Integer.parseInt(mines);
                data.put("rows", numRows);
                data.put("cols", numCols);
                data.put("mines", numMines);
            }catch (Exception e){
                System.out.println("Must insert numbers");
                return null;
            }

        }
        return data;
    }

    public void clearButtons(ActionEvent actionEvent) {
        disableButtons();
        gameType = CUSTOM;
    }

    private void disableButtons(){
        fxEasy.setDisable(false);
        fxMedium.setDisable(false);
        fxHard.setDisable(false);
        fxCustom.setDisable(false);
    }

    public void setDifficulty(ActionEvent actionEvent) {
        Button b = (Button)actionEvent.getSource();
        disableButtons();
        b.setDisable(true);
        if (b.getText().equals("Easy")){
            gameType = EASY;
        }
        else if (b.getText().equals("Medium")){
            gameType = MEDIUM;
        }
        else if (b.getText().equals("Hard")){
            gameType = HARD;
        }
        else{
            gameType = CUSTOM;
        }
    }
}
