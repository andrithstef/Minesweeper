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


public class MainController {
    @FXML
    private Button fxStartButton;

    @FXML
    private TextField fxCols;

    @FXML
    private TextField fxRows;


    public void startGame(ActionEvent actionEvent) throws IOException {

        String cols = fxCols.getText();
        String rows = fxRows.getText();

        if (cols.equals("")){
            cols = "0";
        }
        if (rows.equals("")){
            rows = "0";
        }

        int numCols = Integer.parseInt(cols);
        int numRows = Integer.parseInt(rows);

        int width = 25*numCols;
        int height = 26*numRows+50;


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("minesweeper-view.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent, width, height);
        scene.getStylesheets().add(getClass().getResource("minesweeper-look.css").toExternalForm());
        MinesweeperController controller = loader.getController();
        System.out.println(controller);
        controller.initData(numCols, numRows);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
