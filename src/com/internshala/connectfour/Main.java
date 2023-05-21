package com.internshala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Dell on 08-May-23.
 */
public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        GridPane rootGridPane = loader.load();

        controller = loader.getController();
        controller.createPlayGround();

        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);

        Scene scene = new Scene(rootGridPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect4 Game");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private MenuBar createMenu() {
        //File menu
        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New Game");
        //Using Lambda Expression Here
        newGame.setOnAction(event -> {
            controller.resetGame();
        });

        MenuItem resetGame = new MenuItem("Reset Game");
        //Using Lambda Expression Here
        resetGame.setOnAction(event -> {
            controller.resetGame();
        });

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("Exit Game");
        //Using Lambda Expression Here
        exitGame.setOnAction(event -> {
            exitGame();
        });

        fileMenu.getItems().addAll(newGame, resetGame, separatorMenuItem, exitGame);


        //Help menu
        Menu helpMenu = new Menu("Help");

        MenuItem aboutConnectGame = new MenuItem("About Connect4 Game");

        aboutConnectGame.setOnAction(event -> {
            aboutConnectGame();
        });

        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(event -> {
            aboutMe();
        });

        helpMenu.getItems().addAll(aboutConnectGame, separator, aboutMe);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }

    private void aboutMe() {
        //TODO
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("Dharmendra Kumar");
        alert.setContentText("I love to play around with code and create Game. " +
                "Connect4 Game is one of the my favourite JavaFx game Application." +
                "In free time");

        alert.show();
    }

    private void aboutConnectGame() {
        //TODO
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect4 Game");
        alert.setHeaderText("How to play Connect4 Game?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, " +
                "six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. " +
                "The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. " +
                "Connect Four is a solved game. The first player can always win by playing the right moves.");

        alert.show();
    }

    private void exitGame() {
        //TODO
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        //TODO
    }

    public static void main(String[] args) {
        launch();
    }
}
