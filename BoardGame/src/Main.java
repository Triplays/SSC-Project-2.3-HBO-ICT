import algorithm.Algorithm;
import config.IndicatorSet;
import controller.LocalReversiContoller;
import engine.Board;
import game.Field;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import player.ArPlayer;
import player.Player;
import player.UiPlayer;

public class Main extends Application
{

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try {

            Player player1 = new UiPlayer("Henk de Vries", Field.BLACK);
            Player player2 = new ArPlayer(Field.WHITE);

            //LocalTicTacToeController controller = new LocalTicTacToeController();
            LocalReversiContoller controller = new LocalReversiContoller(player1, player2);

            Thread thread = new Thread(controller);
            thread.start();
            Scene scene = new Scene(controller.getGame().getDisplay().getWrapperPane());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
