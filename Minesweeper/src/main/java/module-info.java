module com.vidmot.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.minesweeper.vidmot to javafx.fxml;
    exports com.minesweeper.vidmot;
}