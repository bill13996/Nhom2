/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhom2;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ContractController implements Initializable {
    DatabaseConn db = new DatabaseConn();
  
    @FXML private Button btnAdd, btnEdit, btnDel, btnN, btnY, btnEditModel, btnAddCus;
        
    @FXML private TextField searchBar, contractId, contractName, file, contractAddress, customerName;

    @FXML private Label valMess;
    
    @FXML private DatePicker dateStart, dateEnd, dateSign;
    
    @FXML private ComboBox<String> choiceCus;
    
    @FXML private TableView<Contract> tbContract;
    @FXML private TableColumn<Contract, Integer> colID;
    @FXML private TableColumn<Contract, String> colName;
    @FXML private TableColumn<Contract, DatePicker> colStart;
    @FXML private TableColumn<Contract, DatePicker> colEnd;
    
    @FXML private TableView<Model> tbModel;
    @FXML private TableColumn<Model, Integer> colModID;
    @FXML private TableColumn<Model, String> colModName;
    
    TreeMap map1, map2;
    
    public ContractController() {
    }

    public class Model {
        private int ID,cityID;
        private String name, body;
        private java.sql.Date birth;
        private boolean gender, avaiable;
        //private LocalDate date;
        
        public Model (int ID, String name){
            this.ID = ID;
            this.name = name;
            this.birth = birth;
            this.gender = gender;
            this.body = body;
            this.cityID = cityID;
            this.avaiable = avaiable;
        }

        /**
         * @return the ID
         */
        public int getID() {
            return ID;
        }

        /**
         * @param ID the ID to set
         */
        public void setID(int ID) {
            this.ID = ID;
        }

        /**
         * @return the cityID
         */
        public int getCityID() {
            return cityID;
        }

        /**
         * @param cityID the cityID to set
         */
        public void setCityID(int cityID) {
            this.cityID = cityID;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the body
         */
        public String getBody() {
            return body;
        }

        /**
         * @param body the body to set
         */
        public void setBody(String body) {
            this.body = body;
        }

        /**
         * @return the birth
         */
        public java.sql.Date getBirth() {
            return birth;
        }

        /**
         * @param birth the birth to set
         */
        public void setBirth(java.sql.Date birth) {
            this.birth = birth;
        }

        /**
         * @return the gender
         */
        public boolean isGender() {
            return gender;
        }

        /**
         * @param gender the gender to set
         */
        public void setGender(boolean gender) {
            this.gender = gender;
        }

        /**
         * @return the avaiable
         */
        public boolean isAvaiable() {
            return avaiable;
        }

        /**
         * @param avaiable the avaiable to set
         */
        public void setAvaiable(boolean avaiable) {
            this.avaiable = avaiable;
        }
        
    }

    public class Customer{
        private String name;
        private int ID;
        
        public Customer(int ID, String name){
            this.name = name;
            this.ID = ID;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the ID
         */
        public int getID() {
            return ID;
        }

        /**
         * @param ID the ID to set
         */
        public void setID(int ID) {
            this.ID = ID;
        }
    }
    
    public class Contract {
        private int ID, cusID;
        private String name, file, addr ;
        private java.sql.Date start, end, sign;
        //private LocalDate date;
        
        public Contract (int Id, String name, int cusId, String file, String addr, java.sql.Date start, java.sql.Date end, java.sql.Date sign){
            this.ID = Id;
            this.cusID = cusId;
            this.name = name;
            this.end = end;
            this.start = start;
            this.sign = sign;
            this.addr = addr;
            this.file = file;
        }
        
        /**
         * @return the ID
         */
        public int getID() {
            return ID;
        }

        /**
         * @param Id the ID to set
         */
        public void setID(int Id) {
            this.ID = Id;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the start
         */
        public Date getStart() {
            return start;
        }

        /**
         * @param start the start to set
         */
        public void setStart(java.sql.Date start) {
            this.start = start;
        }

        /**
         * @return the end
         */
        public java.sql.Date getEnd() {
            return end;
        }

        /**
         * @param end the end to set
         */
        public void setEnd(java.sql.Date end) {
            this.end = end;
        }

        /**
         * @return the sign
         */
        public java.sql.Date getSign() {
            return sign;
        }

        /**
         * @param sign the sign to set
         */
        public void setSign(java.sql.Date sign) {
            this.sign = sign;
        }

        /**
         * @return the file
         */
        public String getFile() {
            return file;
        }

        /**
         * @param file the file to set
         */
        public void setFile(String file) {
            this.file = file;
        }

        /**
         * @return the address
         */
        public String getAddr() {
            return addr;
        }

        /**
         * @param addr the address to set
         */
        public void setAddr(String addr) {
            this.addr = addr;
        }

        /**
         * @return the cusID
         */
        public int getCusID() {
            return cusID;
        }

        /**
         * @param cusID the cusID to set
         */
        public void setCusID(int cusID) {
            this.cusID = cusID;
        }
    }
    
    String customerID;
    String sql;
    ObservableList listContract = FXCollections.observableArrayList();
    ObservableList listModel = FXCollections.observableArrayList();
    ObservableList<String>  choiceValue = FXCollections.observableArrayList();
    
    @FXML
    private void setTableContract(){
        tbContract.getItems().clear();
        try {            
            sql = "select * from Contract";
            db.rs = db.stmt.executeQuery(sql);

            while(db.rs.next()){
                int Id = db.rs.getInt("ContractID");
                String name = db.rs.getString("ConName");
                int cusId = db.rs.getInt("CusID");                
                String file = db.rs.getString("ConFile");
                String addr = db.rs.getString("ConAddr");
                java.sql.Date start = db.rs.getDate("ConStart");
                java.sql.Date end = db.rs.getDate("ConEnd");
                java.sql.Date sign = db.rs.getDate("ConSignDate");
                Contract contract = new Contract(Id, name, cusId, file, addr, start, end, sign );
                listContract.add(contract);
            }
            
        } catch (SQLException ex) {
        }
        
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        
        tbContract.setItems(listContract);
    }
    @FXML
    private void setTableModel(int contractID){
        tbModel.getItems().clear();
        try {
            
            sql = "select Model.*, ModelContract.ContractID from Model, ModelContract where  ModelContract.ModelID = Model.ModelID and ModelContract.ContractID = " + contractID;
            db.rs = db.stmt.executeQuery(sql);

            while(db.rs.next()){
                int Id = db.rs.getInt("ModelID");
                String name = db.rs.getString("ModelName");
                Model model = new Model(Id, name);
                listModel.add(model);
            }
            
        } catch (SQLException e) {
        }
        
        colModID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colModName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tbModel.setItems(listModel);
    }
    @FXML
    private void setChoiceBox(){
        choiceCus.getItems().clear();
        try {            
            sql = "select * from Customer";
            db.rs = db.stmt.executeQuery(sql);

            while(db.rs.next()){
                choiceValue.add(db.rs.getString("CusName"));
            }
            choiceCus.setItems(choiceValue);
            
        } catch (SQLException e) {
        }
    }
    @FXML
    private void setCustomerName(int CusID){
        try {
            sql = "SELECT CusName FROM Customer WHERE CusID ="+CusID;
            db.rs = db.stmt.executeQuery(sql);
            while(db.rs.next()){
                customerName.setText(db.rs.getString("CusName"));
                choiceCus.setValue(db.rs.getString("CusName"));
            }
            
        } catch (SQLException e) {
        }
    }
    @FXML
    private void getTableData(){
        tbContract.setOnMousePressed((MouseEvent me) -> {
            Contract Con = tbContract.getSelectionModel().getSelectedItem();
            if (Con != null){
                btnEdit.setDisable(false);
                btnDel.setDisable(false);
                contractId.setText(String.valueOf(Con.getID()));
                contractName.setText(Con.getName());
                file.setText(Con.getFile());
                contractAddress.setText(Con.getAddr());
                dateSign.setValue(Con.getSign().toLocalDate());
                dateStart.setValue(Con.getStart().toLocalDate());
                dateEnd.setValue(Con.getEnd().toLocalDate());            
                setTableModel(Con.getID());
                setCustomerName(Con.getCusID());
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEditModel.setVisible(false);
        valMess.setText("");
        btnEdit.setDisable(true);
        btnDel.setDisable(true);
        Visiable(false,true);
        Disable(true);
        setTableContract();
        getTableData();
        Add();
        Edit();
        Delete();
        setChoiceBox();
    }
    
    private void Add(){
        btnAdd.setOnAction(eventA -> {
            Disable(false);            
            contractId.setDisable(true);
            Visiable(true,false);
            choiceCus.setPromptText("Chọn khách hàng");
            btnEditModel.setText("Chọn người mẫu");
            tbContract.setDisable(true);
            renew();
            btnY.setOnAction(eventY -> {
                if(textfieldValidation()){
                try {
                    sql = "Select CusID from Customer where CusName ='"+choiceCus.getValue()+"';";
                    db.rs = db.stmt.executeQuery(sql);
                    while(db.rs.next()){
                        customerID = String.valueOf(db.rs.getInt("CusID"));
                    }                    
                    sql="INSERT INTO Contract VALUES ('"+contractName.getText()+"',"+customerID+",'"+file.getText()+"','"
                            +contractAddress.getText()+"','"+dateSign.getValue()+"','"+dateStart.getValue()+"','"+dateEnd.getValue()+"')";
                    db.rs = db.stmt.executeQuery(sql);
                } catch (SQLException e) {
                    
                }
                setTableContract();
                setChoiceBox();
                renew();
                tbContract.setDisable(false);
                Visiable(false,true);
                Disable(true);
                Alert("Thêm thành công");
                }
            });
            btnN.setOnAction(eventC ->{
                renew();
                Disable(true);
                Visiable(false,true);
                tbModel.setVisible(true);
                tbContract.setDisable(false);
            });
        });
    }
    
    private void Edit(){
        btnEdit.setOnAction(eventE -> {            
            Contract Con = tbContract.getSelectionModel().getSelectedItem();
            setCustomerName(Con.getCusID());
            Visiable(true,false);
            Disable(false);
            contractId.setDisable(true);
            dateSign.setDisable(true);
            btnEditModel.setText("Chỉnh sửa người mẫu");
            btnY.setOnAction(eventY -> {
                if(textfieldValidation()){
                        try {
                        sql = "Select CusID from Customer where CusName ='"+choiceCus.getValue()+"';";
                        db.rs = db.stmt.executeQuery(sql);
                        while(db.rs.next()){
                            customerID = String.valueOf(db.rs.getInt("CusID"));
                        }
                        Alert(customerID);
                        /*sql = "UPDATE Contract SET ConName = '"+contractName.getText()+"', CusID = "+customerID
                                +", ConFile='"+file.getText()+"', ConAddr='"+contractAddress.getText()
                                +"',  ConSignDate='"+dateSign.getValue()+"',ConStart='"+dateStart.getValue()+"',ConEnd='"+dateEnd.getValue()+"' WHERE ContractID="+Con.getID()+";";
                        db.rs = db.stmt.executeQuery(sql);*/
                    } catch (SQLException ex) {
                        Logger.getLogger(ContractController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    setTableContract();
                    setChoiceBox();
                    renew();
                    Visiable(false,true);
                    Disable(true);
                    Alert("Cập nhật thành công");
                }
            });
            btnN.setOnAction(eventC ->{
                Disable(true);
                Visiable(false,true);
                renew();
                tbModel.setVisible(true);
                tbContract.setDisable(false);
            });
        });
    }
    
    private void Delete(){
        btnDel.setOnAction(eventD -> {
            Contract Con = tbContract.getSelectionModel().getSelectedItem();
            btnEdit.setVisible(false);
            btnAdd.setVisible(false);
            btnDel.setVisible(false);
            btnY.setVisible(true);
            btnN.setVisible(true);
            btnY.setOnAction(eventY -> {
                try {
                    sql = "DELETE FROM Contract WHERE ContractID="+Con.getID();
                    db.rs = db.stmt.executeQuery(sql);
                } catch (SQLException e) {}
                renew();
                Disable(true);
                Visiable(false,true);
                setTableContract();
                Alert("Xóa thành công");
            });
            btnN.setOnAction(eventC ->{
                Disable(true);
                Visiable(false,true);
                renew();
                btnEditModel.setVisible(false);
                tbModel.setVisible(true);
                tbContract.setDisable(false);
            });
        });
    }  
    
    private void Alert(String a){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("THÔNG BÁO");
        alert.setHeaderText(null);
        alert.setContentText(a);
        alert.showAndWait();
    }
    
    private void Visiable(boolean a, boolean b){
        tbModel.setVisible(b);
        btnN.setVisible(a);
        btnY.setVisible(a);
        valMess.setVisible(a);
        choiceCus.setVisible(a);
        btnAddCus.setVisible(a);
        customerName.setVisible(b);
        btnEditModel.setVisible(a);
        btnAdd.setVisible(b);
        btnEdit.setVisible(b);
        btnDel.setVisible(b);
    }
    
    private void Disable(boolean c){
        tbModel.setDisable(c);
        contractId.setDisable(c);
        contractName.setDisable(c);
        customerName.setDisable(c);
        file.setDisable(c);
        dateStart.setDisable(c);
        dateEnd.setDisable(c);
        dateSign.setDisable(c);
        contractAddress.setDisable(c);
        choiceCus.setDisable(c);
    }
    
    private void renew(){
        contractId.setText("");
        file.setText("");
        contractAddress.setText("");
        contractName.setText("");
        contractId.setText("");
        dateStart.setValue(null);
        dateEnd.setValue(null);
        dateSign.setValue(null);
        customerName.setText("");
        tbModel.getItems().clear();
    }
    
    private boolean textfieldValidation() {
        String name, addr, cus;
        LocalDate sign, end, start;
        LocalDate current = LocalDate.now();
        
        valMess.setText("");
        valMess.setStyle("-fx-text-fill: red;");
        
        name = contractName.getText();
        if(name.isEmpty()){
            contractName.requestFocus();
            valMess.setText("Tên hợp đồng không thể trống!");
            return false;
        }else if(name.length()<3){
            contractAddress.requestFocus();
            valMess.setText("Tên hợp đồng không thể dưới 3 ký tự!");
            return false;
        }
        
        if(choiceCus.getSelectionModel().isEmpty()){
            valMess.setText("Hãy chọn khách hàng hoặc thêm mới!");
            return false;
        }
        
        sign = dateSign.getValue();
        start = dateStart.getValue();
        end = dateEnd.getValue();
        if(sign == null || sign == start || end == null){
            valMess.setText("Ngày không được trống!");
            return false;
        }
        if(sign.isAfter(current)){
            dateSign.requestFocus();
            valMess.setText("Chỉ nhập những hợp đồng đã được ký!");
            return false;
        }else if(start.isBefore(sign)){
            dateStart.requestFocus();
            valMess.setText("Ngày bắt đầu không thể trước ngày ký!");
            return false;
        }else if(end.isBefore(start)){
            dateStart.requestFocus();
            valMess.setText("Ngày kết thúc không thể trước ngày bắt đầu!");
            return false;
        }
        
        addr = contractAddress.getText();
        if(addr.isEmpty()){
            contractAddress.requestFocus();
            valMess.setText("Địa chỉ không thể để trống!");
            return false;
        }else if(addr.length()<3){
            contractAddress.requestFocus();
            valMess.setText("Địa chỉ không thể dưới 3 ký tự!");
            return false;
        }

        return true;
    
    }
}