package mainpanel;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import modeladdedit.ModelAddController;


public class Start extends Application {
    @Override
    public void start(Stage stage) {
        try {
            //MainPanelController Controller = new MainPanelController();
            ModelPaneController Controller = new ModelPaneController();
            //ModelAddController Controller = new ModelAddController(); 
            stage = new Stage();
            stage.setTitle("Aphrodite");
            stage.setScene(new Scene(Controller));
            stage.show();
        } catch(Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
 }