import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class TicTacToe extends Application {

    private StackPane stackPane;
    private GridPane gridPane;
    private boolean playerOneTurn = true;
    private boolean gameOver = false;
    private Tile[][] board = new Tile[3][3];
    private List<Combo> combos = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        for(int i =  0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                gridPane.add(tile, j, i);
                board[j][i] = tile;
            }
        }

        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }


        // get vertical win combos
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        // get diagonal win combos
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        stackPane = new StackPane(gridPane);
        stage.setScene(new Scene(stackPane, 700, 700));
        stage.show();

    }

    private void checkState() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                gameOver = true;
                playWinAnimation(combo);
                break;
            }
        }
    }

    private void playWinAnimation(Combo combo) {

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.7), event -> {

            for(Tile tile : combo.tiles)
                tile.letterText.setVisible(!tile.letterText.isVisible());

        }
        ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    private class Combo {
        private Tile[] tiles;
        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete () {
            if (tiles[0].getLetter().isEmpty())
                return false;

            return tiles[0].getLetter().equals(tiles[1].getLetter()) && tiles[0].getLetter().equals(tiles[2].getLetter());
        }
    }
    private class Tile extends StackPane {

        private Text letterText = new Text();
        private Rectangle rectangle = new Rectangle(200, 200, Color.TRANSPARENT);
        private boolean clicked = false;

        private Tile() {

            letterText.setFont(Font.font(100));
            letterText.setFill(Color.INDIGO);

            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(5);

            getChildren().add(letterText);
            getChildren().add(rectangle);

            setOnMouseClicked(e -> {

                if(!clicked && !gameOver) {

                    if (playerOneTurn) {
                        letterText.setFill(Color.MIDNIGHTBLUE);
                        letterText.setText("X");
                        playerOneTurn = false;
                    }

                    else {
                        letterText.setFill(Color.CRIMSON);
                        letterText.setText("O");
                        playerOneTurn = true;
                    }

                    clicked = true;
                    checkState();
                }
            });

        }


        private String getLetter() {
            return letterText.getText();
        }
    }

}
