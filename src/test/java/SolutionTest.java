import com.nir.ui.utils.SolutionApp;
import javafx.application.Application;
import javafx.stage.Stage;

public class SolutionTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new SolutionApp().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
