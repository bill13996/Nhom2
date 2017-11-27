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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ContractController implements Initializable {
    DatabaseConn db = new DatabaseConn();
  
    @FXML private Button btnAdd, btnEdit, btnDel, btnN, btnY, btnEditModel;
    
    @FXML private TextField searchBar, contractId, customerName, file, contractAddress;

    @FXML private DatePicker dateStart, dateEnd, dateSign;

    @FXML private TableView<Contract> tbContract;
    @FXML private TableColumn<Contract, Integer> colID;
    @FXML private TableColumn<Contract, String> colName;
    @FXML private TableColumn<Contract, DatePicker> colStart;
    @FXML private TableColumn<Contract, DatePicker> colEnd;
    @FXML private TableView<Model> tbModel;
    @FXML private TableColumn<Model, Integer> colModID;
    @FXML private TableColumn<Model, String> colModName;
    @FXML private AnchorPane viewInfo;
    TreeMap map1, map2;
    
    ObservableList list = FXCollections.observableArrayList();
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

  
    public class Contract {
        private int ID, cusID;
        private String name, file, addr ;
        private java.sql.Date start, end, sign;
        //private LocalDate date;
        
        public Contract (int ID, int cusID, String name, java.sql.Date start, java.sql.Date end, java.sql.Date sign, String addr, String file){
            this.ID = ID;
            this.cusID = ID;
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
         * @param ID the ID to set
         */
        public void setID(int ID) {
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
    
    String sql;
    ObservableList listContract = FXCollections.observableArrayList();
    ObservableList listModel = FXCollections.observableArrayList();
    
    @FXML
    public void setTableContract(){
        try {
            
            sql = "select * from Contract";
            db.rs = db.stmt.executeQuery(sql);

            while(db.rs.next()){
                int Id = db.rs.getInt("ContractID");
                int cusId = db.rs.getInt("CusID");
                String name = db.rs.getString("ConName");
                String file = db.rs.getString("ConFile");
                String addr = db.rs.getString("ConAddr");
                java.sql.Date start = db.rs.getDate("ConStart");
                java.sql.Date end = db.rs.getDate("ConEnd");
                java.sql.Date sign = db.rs.getDate("ConSignDate");
                Contract contract = new Contract(Id, cusId, name, start, end, sign, addr, file);
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
    public void setTableModel(int contractID){
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
    public void getTableData(){
        tbContract.setOnMousePressed((MouseEvent me) -> {
            Contract Con = tbContract.getSelectionModel().getSelectedItem();
            if (Con != null){
                btnEdit.setDisable(false);
                btnDel.setDisable(false);
                contractId.setText(String.valueOf(Con.getID()));
                customerName.setText(Con.getName());
                file.setText(Con.getFile());
                contractAddress.setText(Con.getAddr());
                dateSign.setValue(Con.getSign().toLocalDate());
                dateStart.setValue(Con.getStart().toLocalDate());
                dateEnd.setValue(Con.getEnd().toLocalDate());
                setTableModel(Con.getID());
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnN.setVisible(false);
        btnY.setVisible(false);
        btnEditModel.setVisible(false);
        btnEdit.setDisable(true);
        btnDel.setDisable(true);
        Disable();
        setTableContract();
        getTableData();
    }
    
    public void Disable(){
        tbModel.setDisable(true);
        contractId.setDisable(true);
        customerName.setDisable(true);
        file.setDisable(true);
        dateStart.setDisable(true);
        dateEnd.setDisable(true);
        dateSign.setDisable(true);
        contractAddress.setDisable(true);
    }
    
    public void Add(MouseEvent event){
        tbContract.setDisable(true);
        btnN.setVisible(true);
        btnY.setVisible(true);
        customerName.setDisable(false);
        file.setDisable(false);
        btnEditModel.setVisible(true);
        btnEditModel.setText("Chọn người mẫu");
        tbModel.setVisible(false);
        dateStart.setDisable(false);
        dateEnd.setDisable(false);
        dateSign.setDisable(false);
        contractAddress.setDisable(false);
        contractId.setText("");
        file.setText("");
        contractAddress.setText("");
        customerName.setText("");
        contractId.setText("");
        dateStart.setValue(null);
        dateEnd.setValue(null);
        dateSign.setValue(null);
        btnY.setOnAction(eventS -> {
        });
    }
    
    public void Edit(MouseEvent event){
        btnN.setVisible(true);
        btnY.setVisible(true);
        btnEditModel.setVisible(true);
        contractAddress.setDisable(false);
        customerName.setDisable(false);
        file.setDisable(false);
        dateStart.setDisable(false);
        dateEnd.setDisable(false);
    }
    
    public void Delete(MouseEvent event){
        btnN.setVisible(true);
        btnY.setVisible(true);
    }
    
    public void viewInformation(MouseEvent event){
        viewInfo.setVisible(true);
    }
}
