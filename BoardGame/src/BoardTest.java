import controller.LocalTicTacToeController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BoardTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LocalTicTacToeController controller = new LocalTicTacToeController();
        Scene scene = new Scene(controller.getGame().getDisplay().getPane());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
