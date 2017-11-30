package mainpanel;

import java.io.IOException;
import java.sql.SQLException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MainPanelController extends AnchorPane{

    @FXML Label btnMenu;
    @FXML AnchorPane sideBarContainer, topBar, contentPane, sideBar, exitConfirmContainer;
    @FXML Pane exitConfirmPane;
    @FXML MenuButton LangBtn;
    @FXML MenuItem LangVie, LangEng;
    @FXML Button btnExitYes, btnExitNo;
    
    public MainPanelController() throws IOException, SQLException, InterruptedException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();        
        languageOption();
        loadModelPane();
        exitConfirm();
        
        sideBar.setLayoutX(-250);
        exitConfirmPane.setLayoutX(-200);
    }

    private void languageOption(){
        ImageView icoVN = new ImageView(new Image("/mainpanel/icons/vi1.png", 100, 16, true, true));
        ImageView icoEN = new ImageView(new Image("/mainpanel/icons/uk1.png", 100, 16, true, true));
        ImageView icoLang = new ImageView(new Image("/mainpanel/icons/Language_48px.png", 100, 16, true, true));
        
        LangBtn.setGraphic(icoLang);
        /*LangVie.setGraphic(icoVN);
        LangVie.setText("Tiếng Việt");
        LangEng.setGraphic(icoEN);
        LangEng.setText("English");*/
    }
    @FXML private void setLangVie() {
    }
    
    @FXML private void sideMenu(){
        if (!sideBarContainer.isVisible()){
            sideBarContainer.setVisible(true);
            sidePaneAni(sideBar.layoutXProperty(), 0);
        } else {
            sidePaneAni(sideBar.layoutXProperty(), -sideBar.getWidth()).setOnFinished((ActionEvent event) -> {
                sideBarContainer.setVisible(false);
            });
        }
    }
    private Timeline sidePaneAni(DoubleProperty node, double x) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        final KeyValue kv = new KeyValue(node, x);
        final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
        return timeline;
    }
    
    
    @FXML private void getContent(Node node) throws IOException{
        contentPane.getChildren().clear();
        contentPane.getChildren().add(node);
        //contentPane.getChildren().add(FXMLLoader.load(getClass().getResource(filename)));
    }
    @FXML private void loadModelPane() throws IOException, SQLException, InterruptedException{
        ModelPaneController modelPaneController = new ModelPaneController();
        getContent(modelPaneController);
        //getContent("ModelPane.fxml");
    }
    @FXML private void loadConPane() throws IOException{
        contentPane.getChildren().clear();
    }
    @FXML private void loadCusPane() throws IOException{
        contentPane.getChildren().clear();
    }
    @FXML private void loadAccPane() throws IOException{
        contentPane.getChildren().clear();
    }
    @FXML private void exitBtn() throws IOException{
        if (!exitConfirmContainer.isVisible()){
            exitConfirmContainer.setVisible(true);
        sidePaneAni(exitConfirmPane.layoutXProperty(), 0);
        } else {
            sidePaneAni(exitConfirmPane.layoutXProperty(), -exitConfirmPane.getWidth()).setOnFinished((ActionEvent event) -> {
                exitConfirmContainer.setVisible(false);
            });
        }
    }
    private void exitConfirm(){
        btnExitYes.setOnAction(value -> {
            System.exit(0);
        });
        btnExitNo.setOnAction(value -> {
            try {
                exitBtn();
            } catch (IOException ex) {}
        });
    }
}
