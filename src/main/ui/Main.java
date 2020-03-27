package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("checkstyle:RightCurly")
// Represents the entry point for the gui application
public class Main extends Application {

    private Stage mainStage;

    // EFFECTS: Overriding default implementation of Application class to customize application's layout before starting
    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;
        this.mainStage.setTitle("ChaChing");
        initRootLayout();
    }
    // EFFECTS: Load fxml file resource and setup new scene and controller responsible for responding to UI actions

    public void initRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 640, 480);
        Controller controller = loader.getController();
        controller.setStage(mainStage);
        scene.setFill(Color.TRANSPARENT);
        mainStage.setScene(scene);
        mainStage.show();
    }
    // EFFECTS: Start the application

    public static void main(String[] args)  {
        launch(args);
    }
}
