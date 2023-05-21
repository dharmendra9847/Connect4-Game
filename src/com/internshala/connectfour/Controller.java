package com.internshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Controller implements Initializable {

    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static final int CIRCLE_DIAMETER = 80;
    private static final String discColor1 = "#24303E";
    private static final String discColor2 = "#4CAA88";

    private static String PLAYER_ONE = "Player One";
    private static String PLAYER_TWO = "Player Two";

    private boolean isPlayerOneTurn = true;

    private Disc[][] insertDiscArray = new Disc[ROWS][COLUMNS];   //For Structural Change: For The Developer

    @FXML
    public GridPane rootGridPane;


    @FXML
    public Pane insertDiscsPane;

    @FXML
    public Label playerNameLabel;

    @FXML
    public TextField playerOneTextField, playerTwoTextField;

    @FXML
    public Button setNamesButton;


    private boolean isAllowedToInsert = true;    //Flag to avoid same col


    public void createPlayGround(){

        Shape rectangleWithHoles = createGameStructuralGrid();

        rootGridPane.add(rectangleWithHoles, 0, 1);

        List<Rectangle> rectangleList = createClickableColumns();

        for (Rectangle rectangle: rectangleList) {
            rootGridPane.add(rectangle, 0, 1);
        }

    }

    private Shape createGameStructuralGrid(){

        Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);

        for(int row = 0; row < ROWS; row++){
            for (int col = 0; col < COLUMNS; col++){
                Circle circle = new Circle();
                circle.setRadius(CIRCLE_DIAMETER / 2);
                circle.setCenterX(CIRCLE_DIAMETER / 2);
                circle.setCenterY(CIRCLE_DIAMETER / 2);
                circle.setSmooth(true);

                circle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
                circle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }
        }

        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;

    }

    private List<Rectangle> createClickableColumns(){

        List<Rectangle> rectangleList = new ArrayList<>();

        for (int col = 0; col < COLUMNS; col++) {

            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

            //Here Use Lambda Expression
            rectangle.setOnMouseClicked(event -> {
                rectangle.setFill(Color.valueOf("#eeeeee26"));
            });
            rectangle.setOnMouseClicked(event -> {
                rectangle.setFill(Color.TRANSPARENT);
            });

            final int column = col;
            rectangle.setOnMouseClicked(event -> {
                if(isAllowedToInsert) {
                    isAllowedToInsert = false;   //When Disc is being dropped than no more disc will be inserted
                    insertDisc(new Disc(isPlayerOneTurn), column);
                }
            });

            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    private void insertDisc(Disc disc, int column){

        int row = ROWS - 1;
        while (row >= 0){
            if (getDiscIfPresent(row, column) == null)
                break;

            row--;
        }

        if (row < 0)      //if it is full, we cannot insert anymore disc
            return;

        insertDiscArray[row][column] = disc;             //For Structural Change: For The Developer
        insertDiscsPane.getChildren().add(disc);
        disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

        int currentRow = row;

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
        translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

        //Here we use lambda expression
        translateTransition.setOnFinished(event -> {

            isAllowedToInsert = true;    //Finally, When Disc id dropped allow next player to insert disc
            if(gameEnded(currentRow, column)){
                //Do something
                gameOver();
            }

            isPlayerOneTurn = !isPlayerOneTurn;

            playerNameLabel.setText(isPlayerOneTurn? PLAYER_ONE : PLAYER_TWO);

        });

        translateTransition.play();
    }

    private boolean gameEnded(int row, int column){
        //TODO
        //For Vertical Points 1st
        /* Vertical Points. Player has inserted his last Disc at row = 2, column = 3;
        * Range of row values : 0, 1, 2, 3, 4, 5
        * Index of each element present in column [row][column] : 0,3  1,3  2,3  3,3  4,3  5,3(Class: Point2D class
        * is that's to holds the value of X & Y coordinates)
        * */

        //Here WE use IntStream
        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3, row + 3)    //Range of row values : 0, 1, 2, 3, 4, 5
                                       .mapToObj(r -> new Point2D(r, column))    //0,3  1,3  2,3  3,3  4,3  5,3(Class: Point2D class
                                                                                 //* is that's to holds the value of X & Y coordinates)
                                       .collect(Collectors.toList());

        //For Horizontal Points 2st
        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3)  //Range of column values : 0, 1, 2, 3, 4, 5
                                        .mapToObj(col -> new Point2D(row, col))         //0,3  1,3  2,3  3,3  4,3  5,3(Class: Point2D class
                                                                                        //* is that's to holds the value of X & Y coordinates)
                                        .collect(Collectors.toList());

        //For Diagonal1 Points 3rd
        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
                                        .mapToObj(i -> startPoint1.add(i, -i))
                                        .collect(Collectors.toList());

        //For Diagonal2 Points 4rd
        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint2.add(i, i))
                .collect(Collectors.toList());


        boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
                          || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);

        return isEnded;
    }

    private boolean checkCombinations(List<Point2D> points) {

        int chain = 0;

        for (Point2D point: points) {

            int rowIndexArray = (int) point.getX();
            int columnIndexArray = (int) point.getY();

            Disc disc = getDiscIfPresent(rowIndexArray, columnIndexArray);

            if(disc != null && disc.isPlayerOneMove == isPlayerOneTurn){ //if the last inserted Disc belongs to the current player

                chain++;

                if(chain == 4){
                    return true;
                }
            }else{
                chain = 0;
            }
        }
        return false;
    }

    private Disc getDiscIfPresent(int row, int column){     //To Prevents ArrayIndexOutOfBoundExceptions

        if(row >= ROWS || row < 0 || column >= COLUMNS || column < 0){   //If row and column is invalid
            return null;
        }else{
            return insertDiscArray[row][column];
        }

    }

    private void gameOver(){       //Helps us to Resolve IllegalStateException

        String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
        System.out.println("Winner is : "+ winner);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect4 Game");
        alert.setHeaderText("Congratulations! "+ winner +" We're so very proud of you!");
        alert.setContentText("Would you like to Play again? ");

        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType noBtn = new ButtonType("No, Exit");
        alert.getButtonTypes().setAll(yesBtn, noBtn);

        Platform.runLater(() ->{

            Optional<ButtonType> btnClicked = alert.showAndWait();
            if(btnClicked.isPresent() && btnClicked.get() == yesBtn){
                //...User choose YES so RESET the Game
                resetGame();
            }else {
                //...User choose NO so EXIT the Game
                Platform.exit();
                System.exit(0);
            }

        });

    }

    public void resetGame() {
        //TODO
        insertDiscsPane.getChildren().clear();    //Remove all inserted Disc from Pane

        for (int row = 0; row < insertDiscArray.length; row++) { //Structurally, Makes all elements of insertDiscArray[][] to Null

            for (int col = 0; col < insertDiscArray[row].length; col++) {
                insertDiscArray[row][col] = null;
            }
        }
        isPlayerOneTurn = true;  //Let Player start the game
        playerNameLabel.setText(PLAYER_ONE);

        createPlayGround();      //prepare a fresh playground
    }

    private static class Disc extends Circle{

        private final boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove){

            this.isPlayerOneMove = isPlayerOneMove;

            setRadius(CIRCLE_DIAMETER / 2);
            setFill(isPlayerOneMove? Color.valueOf(discColor1) : Color.valueOf(discColor2));
            setCenterX(CIRCLE_DIAMETER / 2);
            setCenterY(CIRCLE_DIAMETER / 2);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setNamesButton.setOnAction(event -> {

            String input1 = playerOneTextField.getText();
            String input2 = playerTwoTextField.getText();

            PLAYER_ONE = input1;
            PLAYER_TWO = input2;

            if (input1.isEmpty())
                PLAYER_ONE = "Player One";

            if (input2.isEmpty())
                PLAYER_TWO = "Player Two";

            // isPlayerOneTurn = !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn? PLAYER_ONE : PLAYER_TWO);

        });

    }
}
