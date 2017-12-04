package mainpanel;

import account_login.*;
import contract.ContractController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import quanlykh.CustomerController;

public class MainPanelController extends AnchorPane{

    @FXML Label btnMenu, lblExitConfirm;
    @FXML AnchorPane sideBarContainer, topBar, contentPane, sideBar, exitConfirmContainer;
    @FXML Pane exitConfirmPane;
    @FXML MenuButton LangBtn;
    @FXML MenuItem langVie, langEng;
    @FXML Button btnExitYes, btnExitNo;
    Locale currentLocale;
    ResourceBundle rb;
    private final Account account;
    static Connection con;
    FXMLLoader fxmlLoader;
    ModelPaneController modelPaneController;
    ContractController contractController;
    CustomerController customerController;
    AccountController accountController;
    
    public MainPanelController(Account account, Connection conn,  Locale currentLocale, ResourceBundle rb) throws IOException, SQLException{
        fxmlLoader = new FXMLLoader(getClass().getResource("MainPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();        
        
        this.account = account;
        this.con = conn;
        this.currentLocale = currentLocale;
        this.rb = rb;
        setLanguage();
        
        languageOption();
        loadModelPane();
        
        sideBar.setLayoutX(-250);
        exitConfirmPane.setLayoutX(-200);
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
    @FXML private void getContent(Node node) throws IOException{
        contentPane.getChildren().clear();
        contentPane.getChildren().add(node);
        //contentPane.getChildren().add(FXMLLoader.load(getClass().getResource(filename)));
    }
    @FXML private void loadModelPane() throws IOException, SQLException{
        ModelPaneController modelPaneController = new ModelPaneController(account, con, currentLocale, rb);
        getContent(modelPaneController);
    }
    @FXML private void loadConPane() throws IOException, SQLException{
        contractController = new ContractController(account, con);
        getContent(contractController);
    }
    @FXML private void loadCusPane() throws IOException, SQLException{
        customerController = new CustomerController(account, con);
        getContent(customerController);
    }
    @FXML private void loadAccPane() throws IOException, SQLException{
        accountController = new AccountController(account, con);
        getContent(accountController);
    }
    @FXML private void logoutBtn() throws IOException{
        exitConfirm();
        lblExitConfirm.setText(rb.getString("confirmLogout"));
        btnExitYes.setOnAction(event -> {
            final Stage stageMain = (Stage) this.getScene().getWindow();
            try {
                PreparedStatement st = con.prepareStatement("UPDATE RemPass set RemStatus = 0");
                st.executeUpdate();
                st.close();
                LoginController loginController = new LoginController(currentLocale, rb);
                Stage stageLogin = new Stage();
                stageLogin.setTitle("Aphrodite");
                stageLogin.setScene(new Scene(loginController));
                stageLogin.show();
            } catch (IOException e) {} catch (Exception ex) {}
            stageMain.close();
        });
        
        btnExitNo.setOnAction(value -> {
            try { exitBtn();
            } catch (IOException ex) {}
        });
    }
    @FXML private void exitBtn() throws IOException{
        exitConfirm();
        lblExitConfirm.setText(rb.getString("confirmExit"));
        
        btnExitYes.setOnAction(value -> { System.exit(0); });
        
        btnExitNo.setOnAction(value -> {
            try { exitBtn();
            } catch (IOException ex) {}
        });
    }
    private void exitConfirm(){
        if (!exitConfirmContainer.isVisible()){
            exitConfirmContainer.setVisible(true);
        sidePaneAni(exitConfirmPane.layoutXProperty(), 0);
        } else {
            sidePaneAni(exitConfirmPane.layoutXProperty(), -exitConfirmPane.getWidth()).setOnFinished((ActionEvent event) -> {
                exitConfirmContainer.setVisible(false);
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
    private void languageOption(){
        ImageView icoVN = new ImageView(new Image("/mainpanel/icons/vi1.png", 100, 16, true, true));
        ImageView icoEN = new ImageView(new Image("/mainpanel/icons/uk1.png", 100, 16, true, true));
        ImageView icoLang = new ImageView(new Image("/mainpanel/icons/Language_48px.png", 100, 16, true, true));
        
        langVie.setGraphic(icoVN);
        langVie.setText("Tiếng Việt");
        langEng.setGraphic(icoEN);
        langEng.setText("English");
        LangBtn.setGraphic(icoLang);
    }    
    @FXML void langVI() throws IOException, SQLException{
        currentLocale = new Locale("vi", "VI");
        rb = ResourceBundle.getBundle("Localization/Aphrodite_vi_VN", currentLocale);
        setLanguage();
        loadModelPane();
        
        try(  PrintWriter out = new PrintWriter( "lang.dat" )  ){
            out.println("vi\r\n"+
                    "VI\r\n"+
                    "Localization/Aphrodite_vi_VN\r\n");
        } catch (FileNotFoundException ex) { ex.printStackTrace(); }
    }
    @FXML void langEN() throws IOException, SQLException{
        currentLocale = new Locale("en", "US");
        rb = ResourceBundle.getBundle("Localization/Aphrodite_en_US", currentLocale);
        setLanguage();
        loadModelPane();
        
        try(  PrintWriter out = new PrintWriter( "lang.dat" )  ){
            out.println("en\r\n"+
                    "US\r\n"+
                    "Localization/Aphrodite_en_US\r\n");
        } catch (FileNotFoundException ex) { ex.printStackTrace(); }
    }
    
    @FXML private Button btnExit, btnLogout, btnAccPane, btnCusPane, btnConPane, btnModelPane;
    private void setLanguage() {
        btnExit.setText(rb.getString("exit"));
        btnLogout.setText(rb.getString("logout"));
        btnAccPane.setText(rb.getString("accMana"));
        btnCusPane.setText(rb.getString("customers"));
        btnConPane.setText(rb.getString("contracts"));
        btnModelPane.setText(rb.getString("models"));
        btnExitYes.setText(rb.getString("yes"));
        btnExitNo.setText(rb.getString("no"));
    }
}
