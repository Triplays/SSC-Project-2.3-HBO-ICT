package models;

import models.gamecontroller.LocalGameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.game.GameInfo;

public class BoardTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        LocalGameController controller = new LocalGameController(GameInfo.REVERSI, 6, 3);
        Thread thread = new Thread(controller);
        thread.start();

        Scene scene = new Scene(controller.getDisplay());
        primaryStage.setScene(scene);
        primaryStage.show();

//        int bestScore = 0;
//        double indicator = 0.0;
//
//        for (double i = 0.0; i < 4.00; i+=0.1) {
//
//            try {
//                indicatorSet.setLineIndicator(i);
//
//                System.out.println("-------------------------");
//                System.out.println("Indicator: " + i);
//
//                LocalGameController controller = new LocalGameController(GameInfo.REVERSI, player1, player2);
//                Thread thread = new Thread(controller);
//                thread.start();
//
//                thread.join();
//
//                int score = 0;
//                for (Field field : controller.getGame().getBoard()) {
//                    if(field == Field.WHITE) score++;
//                }
//
//                controller = new LocalGameController(GameInfo.REVERSI, player2, player1);
//                thread = new Thread(controller);
//                thread.start();
//
//                thread.join();
//
//                for (Field field : controller.getGame().getBoard()) {
//                    if(field == Field.BLACK) score++;
//                }
//
//                score /= 2;
//
//                System.out.println("Measured score: " + score);
//
//                if (score > bestScore) {
//                    bestScore = score;
//                    indicator = i;
//                }
//
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//
//        }
//
//        System.out.println("Best indicator: " + indicator);
    }
}
