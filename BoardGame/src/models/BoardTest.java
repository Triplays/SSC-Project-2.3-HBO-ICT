package models;

import models.config.ReversiIndicatorSet;
import models.controller.LocalGameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.game.Field;
import models.game.GameInfo;
import models.player.ComputerPlayer;
import models.player.Player;

import java.util.ArrayList;

public class BoardTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {

        Player player1 = new ComputerPlayer("Computer BLACK", 8);

        ReversiIndicatorSet indicatorSet = new ReversiIndicatorSet(8);

        indicatorSet.setLineIndicator(0.9);

        Player player2 = new ComputerPlayer("Computer WHITE", indicatorSet);

        int bestScore = 0;
        double indicator = 0.0;

        for (double i = 0.0; i < 4.00; i+=0.1) {

            try {
                indicatorSet.setLineIndicator(i);

                System.out.println("-------------------------");
                System.out.println("Indicator: " + i);

                LocalGameController controller = new LocalGameController(GameInfo.REVERSI, player1, player2);
                Thread thread = new Thread(controller);
                thread.start();

                thread.join();

                int score = 0;
                for (Field field : controller.getGame().getBoard()) {
                    if(field == Field.WHITE) score++;
                }

                controller = new LocalGameController(GameInfo.REVERSI, player2, player1);
                thread = new Thread(controller);
                thread.start();

                thread.join();

                for (Field field : controller.getGame().getBoard()) {
                    if(field == Field.BLACK) score++;
                }

                score /= 2;

                System.out.println("Measured score: " + score);

                if (score > bestScore) {
                    bestScore = score;
                    indicator = i;
                }

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

        }

        System.out.println("Best indicator: " + indicator);
    }
}
