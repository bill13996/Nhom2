package mainpanel;
	
import account_login.LoginController;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Start extends Application {
    @Override
    public void start(Stage stage) {
        try {
            ArrayList<String> lang = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader("lang.dat"));
                String line;
                while ((line = br.readLine()) != null)
                    lang.add(line);
            } catch (FileNotFoundException | IndexOutOfBoundsException e){e.printStackTrace();}
            
            if (lang.isEmpty()){
                lang.add("en");
                lang.add("US");
                lang.add("Localization/Aphrodite_en_US");
            }
            System.out.println(lang);
            Locale currentLocale = new Locale(lang.get(0), lang.get(1));
            ResourceBundle rb = ResourceBundle.getBundle(lang.get(2), currentLocale);
            LoginController Controller = new LoginController(currentLocale, rb);
            
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