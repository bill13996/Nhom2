/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account_login;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import mainpanel.MainPanelController;

/**
 *
 * @author Thanh Dien
 */
public class LoginController extends AnchorPane{
    @FXML private TextField txtUser, tfDBName, tfDBServer, tfDBPort, tfDBLogin;
    @FXML private PasswordField txtPass, tfDBPass;
    @FXML private Label lblInfo;
    @FXML private Button loginBtn, btnConfig, btnDBCancel, btnDBSave, btnDBTest;
    @FXML private ToggleButton remBtn;
    @FXML private AnchorPane configBar, configBackground;
    
    Connection con;
    ResultSet rs;
    Statement stmt;
    String sql;
    private Account account;
    String DBName, DBServer, DBPort, DBLogin, DBPass;
    String DBName1, DBServer1, DBPort1, DBLogin1, DBPass1;
    Locale currentLocale;
    ResourceBundle rb;

    public LoginController(Locale currentLocale, ResourceBundle rb) throws IOException, Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
        configBar.setLayoutX(-225);
        this.currentLocale = currentLocale;
        this.rb = rb;
        setLanguage();
        
        setDBOnStartUp();
        dbConfig();//remember me here
        dbConfigButtons();
        if (con != null)
            rememberMe();
    }
    
    public static Connection DBConnection(String server, String port, String db, String account, String password) {
        Connection conn = null;

        try {  
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            String dbURL = "jdbc:sqlserver://"+server+":"+port+";" +  
                        "databaseName="+db+
                        ";user="+account+
                        ";password="+password+"";
            conn = DriverManager.getConnection(dbURL,account,password);
        } catch (SQLException | ClassNotFoundException ex) {}
        
        return conn;
    }
    
    private void setDBOnStartUp() throws IOException {
        try {
            BufferedReader br = new BufferedReader(new FileReader("dbconfig.dat"));
            ArrayList<String> input = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null)
              input.add(line);
            setDBInput(input.get(0), input.get(1), input.get(2), input.get(3), input.get(4));
        } catch (FileNotFoundException | IndexOutOfBoundsException e) {}
    }
    
    private void setDBInput(String ser, String por, String name, String log, String pass) {
        tfDBServer.setText(ser);
        tfDBPort.setText(por);
        tfDBName.setText(name);
        tfDBLogin.setText(log);
        tfDBPass.setText(pass);
    }
    private void getDBInput() {
        DBServer = tfDBServer.getText();
        DBPort = tfDBPort.getText();
        DBName = tfDBName.getText();
        DBLogin = tfDBLogin.getText();
        DBPass = tfDBPass.getText();
    }
    private void dbConfig() throws Exception{
        getDBInput();
        con = DBConnection(DBServer, DBPort, DBName, DBLogin, DBPass);
        if (con == null){
            lblInfo.setText(rb.getString("loginDBError"));
            lblInfo.setStyle("-fx-text-fill: red");
            loginBtn.setDisable(true);
        } else {
            lblInfo.setText("");
            loginBtn.setDisable(false);
        }
    }
    private void dbConfigButtons() {
        btnConfig.setOnAction(Event -> {
            DBServer1 = DBServer;
            DBPort1 = DBPort;
            DBName1 = DBName;
            DBLogin1 = DBLogin;
            DBPass1 = DBPass;
            configPaneAction();
        });
        
        btnDBSave.setOnAction(Event -> {
            try { dbConfig();
            } catch (Exception ex) {}
            configPaneAction();
            try(  PrintWriter out = new PrintWriter( "dbconfig.dat" )  ){
                out.println(DBServer+"\r\n"+
                        DBPort+"\r\n"+
                        DBName+"\r\n"+
                        DBLogin+"\r\n"+
                        DBPass);
            } catch (FileNotFoundException ex) {}
        });
        
        btnDBTest.setOnAction(Event -> {
            getDBInput();
            Connection conTest = DBConnection(DBServer, DBPort, DBName, DBLogin, DBPass);
            
            if (conTest == null){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Connection Test");
                alert.setHeaderText(null);
                alert.setContentText(rb.getString("loginDBError"));
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Connection Test");
                alert.setHeaderText(null);
                alert.setContentText(rb.getString("loginDBSuccess"));
                try {
                    conTest.close();
                } catch (SQLException ex) {}
                alert.showAndWait();
            }
        });
        
        btnDBCancel.setOnAction(Event -> {
            configPaneAction();
            setDBInput(DBServer1, DBPort1, DBName1, DBLogin1, DBPass1);
            getDBInput();
        });
    }
    
    private void configPaneAction() {
        if (!configBar.isVisible()){
            configBackground.setVisible(true);
            configBar.setVisible(true);
            sidePaneAni(configBar.layoutXProperty(), 0);
        } else {
            sidePaneAni(configBar.layoutXProperty(), -configBar.getWidth()).setOnFinished((ActionEvent event) -> {
                configBackground.setVisible(false);
                configBar.setVisible(false);
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
    
    private void rememberMe() throws SQLException, Exception {
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT TOP 1 * FROM RemPass");
        if (rs.next()){
            if (rs.getBoolean("RemStatus")){
                rs = stmt.executeQuery("SELECT * FROM Account WHERE AccountID = "+rs.getInt("AccountID"));
                rs.next();
                txtUser.setText(rs.getString("Username"));
                txtPass.setText(rs.getString("Password"));
                remBtn.setSelected(true);
                Login();
            }
        }
    }
    @FXML private void Login() throws SQLException, IOException, Exception {
        boolean check = false;
        String user = txtUser.getText();
        String pass = txtPass.getText();
        sql = "SELECT * FROM Account";
        stmt = con.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            if ( user.equalsIgnoreCase(rs.getString("Username")) 
                    && pass.equals(rs.getString("Password")) ){
                check = true;
                break;
            }
        }
        
        if (check){
            if (remBtn.isSelected()){
                PreparedStatement st = con.prepareStatement("UPDATE RemPass SET RemStatus = 1, AccountID = "+rs.getInt("AccountID"));
                st.executeUpdate();
                st.close();
            }
            
            rs = stmt.executeQuery("SELECT A.*, C.City  FROM Account A INNER JOIN City C" +
                    " ON A.CityID = C.CityID AND A.AccountID = " + rs.getInt("AccountID"));
            rs.next();
            account = new Account(rs.getInt("AccountID"), rs.getString("Username"), rs.getString("City"), rs.getInt("Role"));
            
            MainPanelController mainPanelController = new MainPanelController(account, con, currentLocale, rb);
            Stage stageMain = new Stage();
            stageMain.setTitle("Aphrodite");
            stageMain.setScene(new Scene(mainPanelController));
            stageMain.show();
            
            Stage stageLogin = (Stage) loginBtn.getScene().getWindow();
            stageLogin.close();
            
        } else {
            Alert alert = new Alert(AlertType.ERROR, rb.getString("loginWrong"));
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK)
                txtUser.requestFocus();
        }
    }
    @FXML private void Exit(ActionEvent event){
        System.exit(0);   
    }
    
    
    @FXML void langVI(){
        currentLocale = new Locale("vi", "VI");
        rb = ResourceBundle.getBundle("Localization/Aphrodite_vi_VN", currentLocale);
        setLanguage();
        
        try(  PrintWriter out = new PrintWriter( "lang.dat" )  ){
            out.println("vi\r\n"+
                    "VI\r\n"+
                    "Localization/Aphrodite_vi_VN\r\n");
        } catch (FileNotFoundException ex) { ex.printStackTrace(); }
    }
    @FXML void langEN(){
        currentLocale = new Locale("en", "US");
        rb = ResourceBundle.getBundle("Localization/Aphrodite_en_US", currentLocale);
        setLanguage();
        
        try(  PrintWriter out = new PrintWriter( "lang.dat" )  ){
            out.println("en\r\n"+
                    "US\r\n"+
                    "Localization/Aphrodite_en_US\r\n");
        } catch (FileNotFoundException ex) { ex.printStackTrace(); }
    }
    
    @FXML private Button btnExit;
    @FXML private Label lbDBPass, lbDBName, lbDBServer, lbDBPort, lbDBLogin;
    private void setLanguage() {
        btnExit.setText(rb.getString("exit"));
        txtUser.setPromptText(rb.getString("username"));
        txtPass.setPromptText(rb.getString("password"));
        loginBtn.setText(rb.getString("login"));
        remBtn.setText(rb.getString("remember"));
        btnDBSave.setText(rb.getString("save"));
        btnDBTest.setText(rb.getString("test"));
        lbDBName.setText(rb.getString("dbName"));
        lbDBServer.setText(rb.getString("server"));
        lbDBPort.setText(rb.getString("port"));
        lbDBLogin.setText(rb.getString("login"));
        lbDBPass.setText(rb.getString("password"));
    }
}
