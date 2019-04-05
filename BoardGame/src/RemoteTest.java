import controller.RemoteReversiController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

    public class RemoteTest extends Application {

        public static void main(String[] args) {
            Application.launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            RemoteReversiController controller = new RemoteReversiController();
            Thread thread = new Thread(controller);
            thread.start();
            Scene scene = new Scene(controller.getGame().getDisplay().getWrapperPane());
            primaryStage.setScene(scene);
            primaryStage.show();
        }
}
