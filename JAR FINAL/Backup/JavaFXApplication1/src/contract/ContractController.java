/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contract;

import account_login.Account;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TablePosition;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.util.Callback;
import static mainpanel.ModelPaneController.SRC_DIR;

public class ContractController extends AnchorPane {
  
    @FXML private Button btnAdd, btnEdit, btnDel, btnN, btnY, btnEditModel, btnAddCus, btnAddY, btnAddN;
        
    @FXML private TextField searchBar, contractId, contractName, file, contractAddress, customerName, cusAddName, cusAddPhone, cusAddEmail,cusAddAddress;

    @FXML private Label valMess, valAddMess;
    
    @FXML private DatePicker dateStart, dateEnd, dateSign;
    
    @FXML private ComboBox<String> choiceCus;
    
    @FXML private Pane customerPane;
    
    @FXML AnchorPane PaneContractForm, PaneModelPicker, PaneModelTable;
    
    @FXML private TableView<Contract> tbContract;
    @FXML private TableColumn<Contract, Integer> colID;
    @FXML private TableColumn<Contract, String> colName;
    @FXML private TableColumn<Contract, DatePicker> colStart;
    @FXML private TableColumn<Contract, DatePicker> colEnd;
    
    @FXML private TableView<Model> tbModel;
    @FXML private TableColumn<Model, Integer> colModID;
    @FXML private TableColumn<Model, String> colModName;
    
    TreeMap map1, map2;
    
    
    //TruongAnh
   
    @FXML Button btnActionTable, btnExitTable;
    @FXML private TableView<ContractModels> modelTable;
    @FXML private TableColumn<ContractModels, ImageView> tabThumb;
    @FXML private TableColumn<ContractModels, CheckBox> tabGender;
    @FXML private TableColumn<ContractModels, String> tabName, tabAge, tabLoca, tabSkill, tabLang, tabID, tabBody;
    ObservableList<ContractModels> dataList;
    List<String> selectedSkill, selectedLang;
    List<Integer> modelOnContract;
    FilteredList<Contract> foundData;
    Pattern P_BODY = Pattern.compile("^(b(\\d+))(w(\\d+))(h(\\d+))");
    
    Connection con;
    private final int accRole;
    ResultSet rs;
    Statement stmt;
    public ContractController(Account account, Connection conn) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Contract.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
        
        con = conn;
        this.accRole = account.getRole();
        
        if (accRole == 2){
            btnAdd.setDisable(true);
            btnEdit.setDisable(true);
            btnDel.setDisable(true);
        }
        
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        btnEditModel.setVisible(false);
        valMess.setText("");
        btnEdit.setDisable(true);
        btnDel.setDisable(true);
        customerPane.setVisible(false);
        
        colModID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colModName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        Visiable(false,true);
        Disable(true);
        
        tbModel.setStyle("-fx-opacity: 1;");
        contractName.setStyle("-fx-opacity: 1;");
        customerName.setStyle("-fx-opacity: 1;");
        file.setStyle("-fx-opacity: 1;");
        dateStart.setStyle("-fx-opacity: 1;");
        dateEnd.setStyle("-fx-opacity: 1;");
        dateSign.setStyle("-fx-opacity: 1;");
        contractAddress.setStyle("-fx-opacity: 1;");
        choiceCus.setStyle("-fx-opacity: 1;");
        
        setTableContract();
        getTableData();
        Add();
        Edit();
        Delete();
        searchBarAction();
        setChoiceBox();
        customerValidation();
        AddNewCustomer();
        AddNewModel();
        btnActionTableAction();
        btnExitTableAction();
        btnActionTable.setDisable(true);
        if (contractId.getText().length()==0){
            btnEdit.setDisable(true);
            btnDel.setDisable(true);
        }
    }

    public class Model {
        private int ID;
        private String name;
        //private LocalDate date;
        public Model (int ID, String name){
            this.ID = ID;
            this.name = name;
        }
        public int getID() {
            return ID;
        }
        public void setID(int ID) {
            this.ID = ID;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
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
        public int getID() {
            return ID;
        }
        public void setID(int Id) {
            this.ID = Id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Date getStart() {
            return start;
        }
        public void setStart(java.sql.Date start) {
            this.start = start;
        }
        public java.sql.Date getEnd() {
            return end;
        }
        public void setEnd(java.sql.Date end) {
            this.end = end;
        }
        public java.sql.Date getSign() {
            return sign;
        }
        public void setSign(java.sql.Date sign) {
            this.sign = sign;
        }
        public String getFile() {
            return file;
        }
        public void setFile(String file) {
            this.file = file;
        }
        public String getAddr() {
            return addr;
        }
        public void setAddr(String addr) {
            this.addr = addr;
        }
        public int getCusID() {
            return cusID;
        }
        public void setCusID(int cusID) {
            this.cusID = cusID;
        }
    }
    
    Pattern P_EMAIL = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);
    String customerID;
    String sql;
    ObservableList listContract = FXCollections.observableArrayList();
    ObservableList listModel = FXCollections.observableArrayList();
    ObservableList<String>  choiceValue = FXCollections.observableArrayList();
    
    @FXML private void setTableContract(){
        tbContract.getItems().clear();
        try {            
            rs = stmt.executeQuery("select * from Contract");

            while(rs.next()){
                int Id = rs.getInt("ContractID");
                String name = rs.getString("ConName");
                int cusId = rs.getInt("CusID");                
                String file = rs.getString("ConFile");
                String addr = rs.getString("ConAddr");
                java.sql.Date start = rs.getDate("ConStart");
                java.sql.Date end = rs.getDate("ConEnd");
                java.sql.Date sign = rs.getDate("ConSignDate");
                Contract contract = new Contract(Id, name, cusId, file, addr, start, end, sign );
                listContract.add(contract);
                foundData = new FilteredList<>(listContract, p -> true);
            }
            
        } catch (SQLException ex) {
        }
        
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        
        tbContract.setItems(listContract);
    }
    @FXML private void setTableModel(int contractID){
        tbModel.getItems().clear();
        try {
            
            sql = "select Model.*, ModelContract.ContractID from Model, ModelContract where  ModelContract.ModelID = Model.ModelID and ModelContract.ContractID = " + contractID;
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                int Id = rs.getInt("ModelID");
                String name = rs.getString("ModelName");
                Model model = new Model(Id, name);
                listModel.add(model);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tbModel.setItems(listModel);
    }
    @FXML private void setChoiceBox(){
        choiceCus.getItems().clear();
        try {            
            sql = "select * from Customer";
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                choiceValue.add(rs.getString("CusName"));
            }
            choiceCus.setItems(choiceValue);
            
        } catch (SQLException e) {
        }
    }
    @FXML private void setCustomerName(int CusID){
        try {
            sql = "SELECT CusName FROM Customer WHERE CusID ="+CusID;
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                customerName.setText(rs.getString("CusName"));
                choiceCus.setValue(rs.getString("CusName"));
            }
            
        } catch (SQLException e) {
        }
    }
    @FXML private void getTableData(){
        tbContract.setOnMousePressed((MouseEvent me) -> {
            Contract Con = tbContract.getSelectionModel().getSelectedItem();
            if (Con != null){
                if (accRole == 1){
                    btnEdit.setDisable(false);
                    btnDel.setDisable(false);
                }
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
    
    
    private void Add(){
        btnAdd.setOnAction(eventA -> {
            Disable(false);            
            contractId.setDisable(true);
            Visiable(true,false);
            tbModel.setDisable(false);
            tbModel.setVisible(true);
            choiceCus.setPromptText("Chọn khách hàng");
            btnEditModel.setText("Chọn người mẫu");
            tbContract.setDisable(true);
            renew();
            
            btnY.setOnAction(eventY -> {
                if(textfieldValidation()){
                try {
                    sql = "Select CusID from Customer where CusName ='"+choiceCus.getValue()+"';";
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        customerID = String.valueOf(rs.getInt("CusID"));
                    }                    
                    sql="INSERT INTO Contract VALUES ('"+contractName.getText()+"',"+customerID+",'"+file.getText()+"','"
                            +contractAddress.getText()+"','"+dateSign.getValue()+"','"+dateStart.getValue()+"','"+dateEnd.getValue()+"')";
                    stmt.executeUpdate(sql);
                    
                    sql = "select ContractID from Contract";
                    rs = stmt.executeQuery(sql);
                    rs.last();
                    int conID = rs.getInt("ContractID");
                    if(tbModel.getItems().size()>0){
                            for (int i = 0; i < tbModel.getItems().size(); i++) {
                                 int ModelID = tbModel.getItems().get(i).ID;
                                 sql = "Insert into ModelContract values("+conID+", "+ModelID+")";
                                 stmt.executeUpdate(sql);
                            }
                        }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                setTableContract();
                setChoiceBox();
                renew();
                btnEdit.setDisable(true);
                btnDel.setDisable(true);
                tbContract.setDisable(false);
                Visiable(false,true);
                Disable(true);
                Alert("Thêm thành công");
                }
            });
            btnN.setOnAction(eventC ->{
                Disable(true);
                Visiable(false,true);
                tbModel.setVisible(true);
                tbContract.setDisable(false);
                renew();
            });
        });
    }
    
    private void Edit(){
        btnEdit.setOnAction(eventE -> {
            Contract Con = tbContract.getSelectionModel().getSelectedItem();
            if (Con != null){
                setCustomerName(Con.getCusID());
                Visiable(true,false);
                Disable(false);
                tbContract.setDisable(true);
                tbModel.setVisible(true);
                contractId.setDisable(true);
                dateSign.setDisable(true);
                btnEditModel.setText("Chỉnh sửa người mẫu");
                btnY.setOnAction(eventY -> {
                    if(textfieldValidation()){
                            try {
                            sql = "Select CusID from Customer where CusName ='"+choiceCus.getValue()+"';";
                            rs = stmt.executeQuery(sql);
                            while(rs.next()){
                                customerID = String.valueOf(rs.getInt("CusID"));
                            }
                            sql = "UPDATE Contract SET ConName = '"+contractName.getText()+"', CusID = "+customerID
                                    +", ConFile='"+file.getText()+"', ConAddr='"+contractAddress.getText()
                                    +"',  ConSignDate='"+dateSign.getValue()+"',ConStart='"+dateStart.getValue()+"',ConEnd='"+dateEnd.getValue()+"' WHERE ContractID="+Con.getID()+";";
                            stmt.executeUpdate(sql);

                            sql = "Delete from ModelContract where ContractID = "+Con.getID();
                            stmt.executeUpdate(sql);
                            if(tbModel.getItems().size()>0){
                                for (int i = 0; i < tbModel.getItems().size(); i++) {
                                     int ModelID = tbModel.getItems().get(i).ID;
                                     sql = "Insert into ModelContract values("+Con.getID()+", "+ModelID+")";
                                     stmt.executeUpdate(sql);
                                }
                            }

                        } catch (SQLException ex) {
                            Logger.getLogger(ContractController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        setTableContract();
                        setChoiceBox();
                        Visiable(false,true);
                        Disable(true);
                        Alert("Cập nhật thành công");
                    }
                });
                btnN.setOnAction(eventC ->{
                    Disable(true);
                    Visiable(false,true);
                    tbModel.setVisible(true);
                    tbContract.setDisable(false);
                    tbContract.setDisable(false);
                    renew();
                });
            }
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
                    rs = stmt.executeQuery(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                renew();
                btnEdit.setDisable(true);
                btnDel.setDisable(true);
                Disable(true);
                Visiable(false,true);
                setTableContract();
                Alert("Xóa thành công");
            });
            btnN.setOnAction(eventC ->{
                Disable(true);
                Visiable(false,true);
                btnEditModel.setVisible(false);
                tbModel.setVisible(true);
                tbContract.setDisable(false);
            });
        });
    }
    
    //TA
    ArrayList<Integer> listID; 
    private void AddNewModel(){
        btnEditModel.setOnAction(event ->{
            boolean check = true;
            LocalDate sign, start, end;
            sign = dateSign.getValue();
            start = dateStart.getValue();
            end = dateEnd.getValue();
            valMess.setStyle("-fx-text-fill: red;");
            if(sign == null || sign == start || end == null){
                valMess.setText("Ngày không được trống!");
                check = false;
            }
            else if(start.isBefore(sign)){
                dateStart.requestFocus();
                valMess.setText("Ngày bắt đầu không thể trước ngày ký!");
                check = false;
            }else if(end.isBefore(start)){
                dateStart.requestFocus();
                valMess.setText("Ngày kết thúc không thể trước ngày bắt đầu!");
                check = false;
            }
            if(check){
                valMess.setStyle("-fx-text-fill: white;");
                PaneModelTable.setVisible(true);
                PaneContractForm.setDisable(true);
                listID = new ArrayList<>();
                try {
                    setModelTable();
                } catch (SQLException ex) {
                    Logger.getLogger(ContractController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void AddNewCustomer(){
        btnAddCus.setOnAction(eventA -> {
            customerPane.setVisible(true);
            cusAddName.requestFocus();
            valAddMess.setText("");
            Disable(true);
            btnAddY.setOnAction(eventS -> {
                if (customerValidation()){
                    sql="INSERT INTO Customer VALUES('"+cusAddName.getText()+"','"+cusAddAddress.getText()+"','"+cusAddPhone.getText()+"','"+cusAddEmail.getText()+"')";  
                    try {
                        stmt.executeUpdate(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(ContractController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                setChoiceBox();
                Alert("Đã thêm thành công");
                customerPane.setVisible(false);
                Disable(false);
                }
            });
            // sá»± kiá»‡n nÃºt cancel
            btnAddN.setOnAction(eventC -> {
                customerPane.setVisible(false);
                Disable(false);
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
        }else if (name.length() < 2 || name.length() > 140){
            valMess.setText("Tên phải lớn hơn 1 và nhỏ hớn 140 kí tự");
            contractName.requestFocus();
            return false;
        } else if (name.matches(".*\\d.*")) {
            valMess.setText("Tên không thể có kí tự đặc biệt");
            contractName.requestFocus();
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
    
    private boolean customerValidation(){
        String name, com, phone, mail;
        name = cusAddName.getText();
        if (name.isEmpty()){
            valAddMess.setText("Please enter customer's name");
            cusAddName.requestFocus();
            return false;
        } else if (name.length() < 2 || name.length() > 140){
            valAddMess.setText("Name must be greater than 1 and less than 140 characters");
            cusAddName.requestFocus();
            return false;
        } else if (name.matches(".*\\d.*")) {
            valAddMess.setText("Name cannot contain number");
            cusAddName.requestFocus();
            return false;
        }
  
        com = cusAddAddress.getText().isEmpty() ? "" :  cusAddAddress.getText();
        if (com.length() > 30){
            valAddMess.setText("Company name must be less than 30 characters");
            cusAddAddress.requestFocus();
            return false;
        }
        
        phone = cusAddPhone.getText();  
        if (phone.isEmpty()){
            valAddMess.setText("Please enter customer's phone");
            cusAddPhone.requestFocus();
            return false;
        } else if (!phone.matches("^\\d+$")){
            valAddMess.setText("Phone number can only contain numbers");
            cusAddPhone.requestFocus();
            return false;
        } else if (phone.length() < 6 || phone.length() > 30){
            valAddMess.setText("Phone number must be greater than 6 and less than 30 digits");
            cusAddPhone.requestFocus();
            return false;
        }
        
        mail = cusAddEmail.getText();
        if (mail.isEmpty()){
            valAddMess.setText("Please enter customer's email address");
            cusAddEmail.requestFocus();
            return false;
        } else if (!P_EMAIL.matcher(mail).matches()) {
            valAddMess.setText("Invalid email format");
            cusAddEmail.requestFocus();
            return false;
        } else if (mail.length() > 30){
            valAddMess.setText("Email must be less than 30 digits");
            cusAddEmail.requestFocus();
            return false;
        }
        valAddMess.setText("");
        return true;
    }
    
    private void searchBarAction(){
        searchBar.textProperty().addListener((observable, oldValue, enteredValue) -> {
            foundData.setPredicate(model -> {
                if (enteredValue == null || enteredValue.isEmpty())
                    return true;
                String keyword = enteredValue.toLowerCase();
                String name = model.getName().toLowerCase();
                String id = String.valueOf(model.getID());
                if (name.contains(keyword))
                    return true;
                else if (id.contains(keyword))
                    return true;
                
                return false;
            });
            SortedList<Contract> sortedData = new SortedList<>(foundData);// 3. Wrap the FilteredList in a SortedList.    
            sortedData.comparatorProperty().bind(tbContract.comparatorProperty());// 4. Bind the SortedList comparator to the TableView comparator.
            tbContract.setItems(sortedData);// 5. Add sorted (and filtered) data to the table.
        });
    }
    
    //-----------------------ModelTablePicker-----------------------------------------
    public void btnExitTableAction(){
        btnExitTable.setOnAction(event ->{
        PaneModelTable.setVisible(false);
        PaneContractForm.setDisable(false);
        });
    }
    
    String modelIDSQL = "";
    public void setModelTable() throws SQLException{
        int modelID;
        String dateFrom = String.valueOf(dateStart.getValue().getYear())+"-"+String.valueOf(dateStart.getValue().getMonth())+"-"+String.valueOf(dateStart.getValue().getDayOfMonth());
        String dateTo = String.valueOf(dateEnd.getValue().getYear())+"-"+String.valueOf(dateEnd.getValue().getMonth())+"-"+String.valueOf(dateEnd.getValue().getDayOfMonth());
        
        sql = "select ModelID from Model";
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            modelID = rs.getInt("ModelID");
            listID.add(modelID);
        }
        //Lọc khi có bảng edit.
        if(tbModel.getItems().size()>0){
            for (int i = 0; i < tbModel.getItems().size(); i++) {
                for (int j = 0; j < listID.size(); j++) {
                    if(listID.get(j)==tbModel.getItems().get(i).ID){
                        listID.remove(j);
                    }
                }
            }
        }
        
        //Lọc ngày làm việc
        sql = "select a.ContractID, b.ModelID, a.ConStart, a.ConEnd from Contract a, ModelContract b "
                + "where ((ConStart between '"+dateFrom+"' and '"+dateTo+"') or (ConEnd between '"+dateFrom+"' and '"+dateTo+"')) or ('"+dateFrom+"' between ConStart and ConEnd) and a.ContractID = b.ContractID";
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            for (int i = 0; i < listID.size(); i++) {
                if(listID.get(i)==rs.getInt("ModelID")){
                    listID.remove(i);
                }
            }
        }
        setTable(listID);
    }
    
    public void setTable(ArrayList<Integer> listID) throws SQLException{
        int modelID;
        modelIDSQL = "";
        for (int i = 0; i < listID.size(); i++) {
            modelID = listID.get(i);
            modelIDSQL += modelID;
            if(i!=listID.size()-1) {
                modelIDSQL += ", ";
            } 
        }
        //Son ModelTable
        PreparedStatement st = con.prepareStatement("SELECT Model.*, City.City FROM Model LEFT JOIN City" +
            " ON Model.CityID = City.CityID WHERE ModelID IN ("+modelIDSQL+")");
        ResultSet rs = st.executeQuery();
    //Add models to row list
        dataList = FXCollections.observableArrayList();
        while (rs.next()){
            modelID = rs.getInt("ModelID");
            int age;
            String name, city, body, imgURL="file:///"+SRC_DIR+"/mainpanel/images/default.png";
            List<String> skill = new ArrayList<>();
            List<String> lang = new ArrayList<>();
            ImageView thumb;
            CheckBox gender = new CheckBox();
            gender.setDisable(true);
            gender.setStyle("-fx-opacity: 1");
            ResultSet rs2;
            
            //Get all model's pictures
            st = con.prepareStatement("SELECT ImgURL FROM ModelImg" +
                " WHERE ModelID = "+modelID);
            rs2 = st.executeQuery();
            if (rs2.next())
                imgURL = "file:///"+SRC_DIR+rs2.getString("ImgURL");

            //Get all model's skills
            st = con.prepareStatement("SELECT B.Skill FROM ModelSkill A INNER JOIN Skills B" +
                " ON A.ModelID = "+modelID+" AND A.SkillID = B.SkillID");
            rs2 = st.executeQuery();
            while (rs2.next())
                skill.add(rs2.getString("Skill"));
                //skill += rs2.getString("Skill") + ", ";

            //Get all model's languages
            st = con.prepareStatement("SELECT B.Language FROM ModelLanguage A INNER JOIN Language B" +
                " ON A.ModelID = "+modelID+" AND A.LanguageID = B.LanguageID");
            rs2 = st.executeQuery();
            while (rs2.next())
                lang.add(rs2.getString("Language"));
                //lang += rs2.getString("Language") + ", ";

            //Extract data and add to row
            //thumb = null;
            thumb = new ImageView(new Image(imgURL, 100, 50, true, true));
            gender.setSelected(rs.getBoolean("female"));
            body = rs.getString("Body")!=null ? P_BODY.matcher(rs.getString("Body")).replaceAll("$2-$4-$6") : "" ;
            //age = rs.getString("DoB")!=null ? sqlDateCal(rs.getString("DoB")).getYears() : 0 ;
            age = rs.getDate("DoB")!=null ? Period.between(rs.getDate("DoB").toLocalDate(), LocalDate.now()).getYears() : 0 ;
            city = rs.getString("City")!=null ? rs.getString("City") : "" ;
            name = rs.getString("ModelName")!=null ? rs.getString("ModelName") : "" ;

            dataList.add(new ContractModels(modelID, thumb, name, gender, age, body, city, skill, lang));
        }
        st.close();
    //Add row list to table
        modelTable.setItems(dataList);
    //Set opacity for busy model    
        tabID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabThumb.setCellValueFactory(new PropertyValueFactory<>("thumb"));
        tabName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tabAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        tabBody.setCellValueFactory(new PropertyValueFactory<>("body"));
        tabLoca.setCellValueFactory(new PropertyValueFactory<>("city"));
        tabSkill.setCellValueFactory(new PropertyValueFactory<>("skill"));
        tabLang.setCellValueFactory(new PropertyValueFactory<>("lang"));
        wrapTableColumn(tabSkill);
        wrapTableColumn(tabLang);
    }
    
    public int id1, id2;
    @FXML private void tableCellClicked(MouseEvent event){
        if (modelTable.getSelectionModel().getSelectedItem() != null) {
            btnActionTable.setText("Thêm");
            btnActionTable.setDisable(false);
        }
        TablePosition pos = (TablePosition) modelTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        
        ContractModels item = modelTable.getItems().get(row);
        
        id1 = item.getId();
    }
    
    int row1;
    @FXML private void tableCellClicked1(MouseEvent event){
        if (tbModel.getSelectionModel().getSelectedItem() != null){
            btnActionTable.setText("Loại");
            btnActionTable.setDisable(false);
        }
        TablePosition pos = (TablePosition) tbModel.getSelectionModel().getSelectedCells().get(0);
        row1 = pos.getRow();
        
        Model item = tbModel.getItems().get(row1);
        id2 = item.getID();
        
    }
    
    ArrayList<Integer> listID2 = new ArrayList<>();
    public void btnActionTableAction(){
        btnActionTable.setOnAction(event -> {
           String label = btnActionTable.getText();
           if(label.equalsIgnoreCase("Thêm")){
               sql = "Select ModelID, ModelName from Model where ModelID = "+id1;
               PreparedStatement st;
                try {
                    st = con.prepareStatement(sql);
                    ResultSet rs = st.executeQuery();
                    while(rs.next()){
                        listModel.add(new Model(rs.getInt("ModelID"), rs.getString("ModelName")));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
                tbModel.setItems(listModel);
                
                listID2 = listID;
                for (int i = 0; i < listID2.size(); i++) {
                    if(listID2.get(i) == id1){
                        listID2.remove(i);
                    }
                }
                modelIDSQL = "";
                try {
                    setTable(listID2);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                btnActionTable.setDisable(true);
            }
           //Loai Model
           else{
               if(label.equalsIgnoreCase("Loại"));
               sql = "Select ModelID, ModelName from Model where ModelID = "+id2;
               PreparedStatement st;
                try {
                    st = con.prepareStatement(sql);
                    ResultSet rs = st.executeQuery();
                    while(rs.next()){
                        listModel.remove(row1);
                    }
                    tbModel.setItems(listModel);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
               listID2.add(id2);
               modelIDSQL = "";
               try {
                   setTable(listID2);
               } catch (SQLException ex) {
                   ex.printStackTrace();
               }
               btnActionTable.setDisable(true);
           }
        });
        
    }
    public void wrapTableColumn(TableColumn column) {
        column.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn arg0) {
                TableCell cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(50);
                text.wrappingWidthProperty().bind(column.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell ;
            }
        });
    }
}