/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account_login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import mainpanel.methods.Models;

/**
 * FXML Controller class
 *
 * @author Jonathan
 */
public class AccountController extends AnchorPane{
    @FXML Label lblTitle;
    @FXML TableView table;
    @FXML TableColumn colId, colUsername, colRole, colCity;
    @FXML TextField txtUsername1, searchBar;
    @FXML PasswordField txtPass1, txtRePass1, tfManaOldPass, tfManaNewPass, tfManaRePass;
    @FXML RadioButton rdoMan1, rdoAdmin1;
    @FXML ComboBox cboCity1;
    @FXML Button btnAdd, btnEdit, btnDel, btnUpdate, btnSave, btnManaChange;
    @FXML Pane AddAccount, accountPane;
    @FXML AnchorPane root1, managerAuth;
    
    static Connection con;
    private final account_login.Account account;
    private final int accRole;
    private final int accID;
    ResultSet rs;
    Statement stmt ;
    int id, cityid;
    String user, pass, role, city;
    TreeMap map1;
    FilteredList<Account> foundData;

    public AccountController(account_login.Account account, Connection conn) throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Account.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
        
        con = conn;
        this.account = account;
        this.accRole = account.getRole();
        this.accID = account.getId();
        
        if (accRole==1){
            starting();
            searchBarAction();
        }
        else{
            accountPane.setDisable(true);
            accountPane.setVisible(false);
            managerAuth.setVisible(true);
            managerPermission();
        }
    }
    private void starting() throws SQLException {
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        //ComboBox
        ObservableList list = FXCollections.observableArrayList();
        map1 = new TreeMap();
        
        sql = "select * from City";
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            list.add(rs.getString("City"));
            map1.put(rs.getString("City"), rs.getInt("CityID"));
        }
        cboCity1.setItems(list);
        
        //RadioButton
        ToggleGroup group = new ToggleGroup();
        rdoMan1.setToggleGroup(group);
        rdoMan1.setSelected(true);
        rdoAdmin1.setToggleGroup(group);
        
        //ComboBox
        try {
            sql = "select * from City";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                listCombo.add(rs.getString("City"));
            }
        } catch (SQLException ex) {
        }

        //TableView
        setTable();
        
        //RadioButton
        ToggleGroup group1 = new ToggleGroup();
    }
    
    public class Account {
        int id;
        String user, pass, role, city;


        public Account(int id, String user, String pass, String role, String city) {
            this.id = id;
            this.user = user;
            this.pass = pass;
            this.role = role;
            this.city = city;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
        
        
    }
    
    String sql;
    ObservableList listCombo = FXCollections.observableArrayList();
    ObservableList listTable = FXCollections.observableArrayList();
    
    private void setTable(){
        try {
            
            sql = "select Account.*, City.City from Account, City where Account.CityID = City.CityID";
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                id = rs.getInt("AccountID");
                user = rs.getString("Username");
                pass = rs.getString("Password");
                role = "Admin";
                if (rs.getInt("Role") == 2){
                    role = "Manager";
                }
                city = rs.getString("City");
                Account acc = new Account(id, user, pass, role, city);
                listTable.add(acc);
            }
            
        } catch (SQLException ex) {
        }
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("user"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        
        table.setItems(listTable);
        foundData = new FilteredList<>(listTable, p -> true);
    }
    
    private void searchBarAction(){
        searchBar.textProperty().addListener((observable, oldValue, enteredValue) -> {
            foundData.setPredicate(acc -> {
                if (enteredValue == null || enteredValue.isEmpty())
                    return true;
                String keyword = enteredValue.toLowerCase();
                String name = acc.getUser().toLowerCase();
                String accID = String.valueOf(acc.getId());
                if (name.contains(keyword))
                    return true;
                else if (accID.contains(keyword))
                    return true;
                
                return false;
            });
            SortedList<Account> sortedData = new SortedList<>(foundData);  
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
        });
    }
    
    @FXML private void mouseClicked(MouseEvent event){
        
        TablePosition pos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        
        Account item = (Account) table.getItems().get(row);
        
        id = item.id;
        user = item.user;
        role = item.role;
        city = item.city;
        pass = item.pass;
        
    }
    
    @FXML private void Add(ActionEvent event) throws IOException{
        txtUsername1.setDisable(false);
        AddAccount.setVisible(true);
        accountPane.setDisable(true);
        AddAccount.setDisable(false);
        lblTitle.setText("Add Model");
    }
    
    @FXML private void Back(ActionEvent event){
        AddAccount.setVisible(false);
        accountPane.setDisable(false);
        txtUsername1.setText("");
        txtPass1.setText("");
        txtRePass1.setText("");
        cboCity1.getSelectionModel().clearSelection();
        btnUpdate.setVisible(false);
        btnSave.setVisible(true);
    }
    
    @FXML private void Save(ActionEvent event) throws SQLException{
        user = txtUsername1.getText();
        pass = txtPass1.getText();
        String repass = txtRePass1.getText();
        city = (String) cboCity1.getSelectionModel().getSelectedItem();
        
        
        //ErrorHandler
        
        //username > 6    
        if(user.length()<6){        
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username must be greater than 6 characters!");
                alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtUsername1.requestFocus();
                return;
            }
        }
        //username no duplicate
        sql = "select * from Account";
        rs = stmt.executeQuery(sql);
        boolean check = false;
        while(rs.next()){
            if (user.equals(rs.getString("Username"))){
                check = true;
                break;
            } 
        }
        if(check==true){   
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username cannot be blank!");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    txtUsername1.requestFocus();
                    return;
                }
        }
        
        //password
        if(pass.length()<6){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password must be greater than 6 characters!");
                alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtPass1.requestFocus();
                return;
            }
        }
        if (!pass.matches(".*\\d+.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password must have at least a number!");
                alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtPass1.requestFocus();
                return;
            }
        }           
        if (!repass.equalsIgnoreCase(pass)){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Re-enter password is not equal to your password!");
                alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtRePass1.requestFocus();
                return;
            }
        }
       
        //city
        if (city == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a city!");
                alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                return;
            }
        }
        
        
        //Add to SQL
        int role = 1;
        if(rdoMan1.isSelected()){
            role = 2;
        }
        
        cityid = (int) map1.get(city);
        
        sql = "insert into Account values ('"+user+"','"+pass+"',"+role+","+cityid+")";
        stmt.executeUpdate(sql);
        
        AddAccount.setVisible(false);
        accountPane.setDisable(false);
        table.getItems().clear();
        setTable();
        
    }
    
    @FXML private void Delete(ActionEvent event) throws SQLException{
        try {
            TablePosition pos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            Account item = (Account) table.getItems().get(row);
            
            if (!item.getRole().equals("Admin") || (item.getRole().equals("Admin") && item.getUser().equals(account.getName())) ) {
                int id = item.id;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this record?");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    sql = "Delete from Account where AccountID = "+id;
                    stmt.executeUpdate(sql);
                    table.getItems().clear();
                    setTable();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You cannot delete other admin!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }
    
    @FXML private void Edit(ActionEvent event) throws SQLException{
        try {
            TablePosition pos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            Account item = (Account) table.getItems().get(row);

            if (!item.getRole().equals("Admin") || (item.getRole().equals("Admin") && item.getUser().equals(account.getName())) ) {
                txtUsername1.setDisable(true);
                AddAccount.setVisible(true);
                btnUpdate.setVisible(true);
                btnSave.setVisible(false);
                lblTitle.setText("Edit Model");
                txtUsername1.setText(item.user);
                txtPass1.setText("");
                if(item.role.equals("Admin")){
                    rdoAdmin1.setSelected(true);
                } else 
                    rdoMan1.setSelected(true);
                cboCity1.getSelectionModel().select(item.city);
                accountPane.setDisable(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You cannot edit other admin!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }
    
    @FXML private void Update(ActionEvent event) throws SQLException{
        TablePosition pos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Account item = (Account) table.getItems().get(row);
        id = item.id;
        pass = txtPass1.getText();
        city = (String) cboCity1.getSelectionModel().getSelectedItem();
        cityid = (int) map1.get(city);
        String repass = txtRePass1.getText();

        int role = 1;
        if(rdoMan1.isSelected()){
            role = 2;
        }

        //ErrorHandler
        //password
        if(pass.length()<6){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password must be greater than 6 characters!");
                alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtPass1.requestFocus();
                return;
            }
        }
        if (!pass.matches(".*\\d+.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password must have at least a number!");
                alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtPass1.requestFocus();
                return;
            }
        }           
        if (!repass.equalsIgnoreCase(pass)){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Re-enter password is not equal to your password!");
                alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                txtRePass1.requestFocus();
                return;
            }
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to update this record?");
                alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sql = "Update Account set Username='"+user+"', Password='"+pass+"', Role="+role+", CityID='"+cityid+"' where AccountID = "+id;
            stmt.executeUpdate(sql);
            btnSave.setVisible(true);
            btnUpdate.setVisible(false);
            txtUsername1.setText("");
            txtPass1.setText("");
            txtRePass1.setText("");
            cboCity1.getSelectionModel().clearSelection();
            AddAccount.setVisible(false);
            table.getItems().clear();
            setTable();
            accountPane.setDisable(false);
        }
    }
    
    private void managerPermission(){
        btnManaChange.setOnAction(event -> {
            String op, np, rp;
            try {
                PreparedStatement st = con.prepareStatement("SELECT * FROM Account WHERE AccountID = "+accID);
                ResultSet rs = st.executeQuery();
                op = tfManaOldPass.getText();
                rs.next();
                if ( !op.equals(rs.getString("Password")) ){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password does not match!");
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        tfManaOldPass.requestFocus();
                        return;
                    }
                }
                
                np = tfManaNewPass.getText();
                if(np.length()<6){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password must be greater than 6 characters!");
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        tfManaNewPass.requestFocus();
                        return;
                    }
                }
                if (!np.matches(".*\\d+.*")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password must have at least a number!");
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        tfManaNewPass.requestFocus();
                        return;
                    }
                }       
                
                rp = tfManaRePass.getText();
                if (!rp.equalsIgnoreCase(np)){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Re-enter password is not equal to your password!");
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        tfManaRePass.requestFocus();
                        return;
                    }
                }
                
                con.prepareStatement("UPDATE Account SET Password='"+np+"' WHERE AccountID = "+accID).executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your password has been updated!");
                alert.setHeaderText(null);
                alert.showAndWait();
                tfManaOldPass.setText("");
                tfManaNewPass.setText("");
                tfManaRePass.setText("");
            } catch (SQLException ex) {}
        });
    }
}
