package models;

import models.config.IndicatorSet;
import models.config.ReversiIndicatorSet;
import models.controller.LocalGameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.game.GameInfo;
import models.player.ComputerPlayer;
import models.player.Player;

public class BoardTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ReversiIndicatorSet indicatorSet = new ReversiIndicatorSet(8);

        indicatorSet.setLineIndicator(0.35);
        indicatorSet.setCornerIndicator(4.0);
        indicatorSet.setBorderFirstIndicator(3.5);

        Player player1 = new ComputerPlayer("Computer with weights", indicatorSet);

        LocalGameController controller = new LocalGameController(GameInfo.REVERSI, player1);
        Thread thread = new Thread(controller);
        thread.start();
        Scene scene = new Scene(controller.getGame().getDisplay().getWrapperPane());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
