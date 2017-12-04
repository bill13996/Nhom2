
package quanlykh;
import account_login.Account;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 *
 * @author thanhdien
 */
// Phần điều khiển 
public class CustomerController extends AnchorPane{
    // Khai báo các tên bên FXML java 
    @FXML ComboBox cboSearch;
    @FXML TextField txtID, txtName, txtCompany, txtPhone, txtEmail, searchBar;
    @FXML Label lblInfo;
    @FXML TableView<Customer> tableCustomer;
    @FXML TableColumn<Customer, Integer> colID;
    @FXML TableColumn<Customer, String> colName, colCompany, colPhone, colEmail;
    @FXML Button btnAdd, btnEdit, btnDel, btnSave, btnCancel, btnSearch;
    // Chuổi kết nói databas
    Connection con;
    // data chính
    ObservableList<Customer> dataCus;
    // Data lọc
    FilteredList<Customer> foundData;
    // Biên soạn cấu trúc Email
    Pattern P_EMAIL = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);
    // Khởi tạo chạy các sự kiện method
    Timeline timeline = new Timeline();
    
    private final int accRole;
    private final String accCity;
    
    public CustomerController(Account account, Connection conn) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Customer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();   
        
        con = conn;
        this.accRole = account.getRole();
        this.accCity = account.getCity();
        
        txtID.setDisable(true);
        textfieldDisable(true);
        setTable();
        getTableData();
        cbbSearch();
        searchBar();
        btnAddAction();
        btnEditAction();
        btnDelAction();
        
        permission();
    } 
    private void permission() {
        if (accRole == 2){
            btnAdd.setDisable(true);
            btnEdit.setDisable(true);
            btnDel.setDisable(true);
            btnSave.setDisable(true);
            btnCancel.setDisable(true);
        }
    }

    // Bảng combobox lượt tìm kiếm trong Search (ID, name, compady, phone, Email)
    private void cbbSearch() {
       cboSearch.getItems().addAll( "ID", "Tên", "Công ty", "Số ĐT", "Email" );
       cboSearch.setValue("Tên");
    }
    // Thanh điền tìm kiếm 
    private void searchBar() {
        searchBar.textProperty().addListener((observable, oldValue, enteredValue) -> {
            foundData.setPredicate(cus -> {
                String keyword = enteredValue.toLowerCase();
                if (keyword.isEmpty())
                    return true;
                
                String cboVal = cboSearch.getValue().toString();
                switch (cboVal){
                    case "Tên":
                        String name = cus.getName().toLowerCase();
                        if (name.contains(keyword))
                            return true;
                        break;
                    case "ID":
                        String id = String.valueOf(cus.getId());
                        if (id.contains(keyword))
                            return true;
                        break;
                    case "Công ty":
                        String comp = cus.getComp().toLowerCase();
                        if (comp.contains(keyword))
                            return true;
                        break;
                    case "Số ĐT":
                        String phone = cus.getPhone();
                        if (phone.contains(keyword))
                            return true;
                        break;
                    case "Email":
                        String mail = cus.getMail().toLowerCase();
                        if (mail.contains(keyword))
                            return true;
                        break;
                }
                
                return false;
            });
            SortedList<Customer> sortedData = new SortedList<>(foundData);
            sortedData.comparatorProperty().bind(tableCustomer.comparatorProperty());
            tableCustomer.setItems(sortedData);
        });
    }
    // làm lại giá trị bảng table lấy lên từ databas
    private void setTable() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("comp"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        
        try {
            dataCus = FXCollections.observableArrayList();
            PreparedStatement st =  con.prepareStatement("SELECT * FROM Customer");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String name, com, phone, mail;
                int id = rs.getInt("CusID");
                name = rs.getString("CusName");
                com = rs.getString("Company");
                phone = rs.getString("Phone");
                mail = rs.getString("Email");
                //System.out.println(id+" - "+name+" - "+com+" - "+phone+" - "+mail);
                dataCus.add(new Customer(id, name, com, phone, mail));
            }
            
        st.close();
        tableCustomer.setItems(dataCus);
        foundData = new FilteredList<>(dataCus, p -> true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // phần hiện thị qua bảng thông tin chi tiết
    private void getTableData() {
        tableCustomer.setOnMousePressed((MouseEvent me) -> {
            Customer cus = tableCustomer.getSelectionModel().getSelectedItem();
            if (cus != null){
                setTextfield(String.valueOf(cus.getId()),
                        cus.getName(),
                        cus.getComp(),
                        cus.getPhone(),
                        cus.getMail());
            }
        });
    }
    // Phần vô hiệu hóa các Textfield
    private void textfieldDisable(boolean b) {
        txtName.setDisable(b);
        txtCompany.setDisable(b);
        txtPhone.setDisable(b);
        txtEmail.setDisable(b);
        
    }
    //  Phần đặt lại giá trị của các Textfild
    private void setTextfield(String i, String n, String c, String p, String e) {
        txtID.setText(i);
        txtName.setText(n);
        txtCompany.setText(c);
        txtPhone.setText(p);
        txtEmail.setText(e);
    }
   // Phần clear cách textfield
    private void clearTextfield() {
        setTextfield("", "", "", "", "");
    }
    // Sự kiện button Add
    private void btnAddAction() {
        btnAdd.setOnAction(eventA -> {
            switchSideBtn(false, true);
            tableCustomer.setDisable(true);
            setTextfield("", "", "", "", "");
            textfieldDisable(false);
            lblInfo.setText("");
            txtName.requestFocus();
            // Sự kiện nút save
            btnSave.setOnAction(eventS -> {
                if (textfieldValidation()){
                    try {
                        PreparedStatement st = con.prepareStatement("INSERT INTO Customer VALUES(?,?,?,?)");
                        st.setString(1, txtName.getText());
                        st.setString(2, txtCompany.getText());
                        st.setString(3, txtPhone.getText());
                        st.setString(4, txtEmail.getText());
                        st.executeUpdate();
                    } catch (SQLException e) { e.printStackTrace(); }  

                    textfieldDisable(true);
                    clearTextfield();                        
                    switchSideBtn(true, false);   
                    tableCustomer.setDisable(false);
                    setTable();
                    lblInfo.setStyle("-fx-text-fill: green;");
                    lblInfo.setText("New customer has been added");
                    fadeText();
                }
            });
            // sự kiện nút cancel
            btnCancel.setOnAction(eventC -> {
                clearTextfield();
                textfieldDisable(true);
                switchSideBtn(true, false);
                tableCustomer.setDisable(false);
                lblInfo.setText("");
            });
        });
    }
    // Sự kiện EditAction
    private void btnEditAction() {
        btnEdit.setOnAction(eventE -> {
            Customer cus = tableCustomer.getSelectionModel().getSelectedItem();
            if (cus != null && !txtID.getText().isEmpty()){
                switchSideBtn(false, true);
                tableCustomer.setDisable(true);
                textfieldDisable(false);
                txtName.requestFocus();
                lblInfo.setText("");
                 // sự kiện nút save của Edit
                btnSave.setOnAction(eventS -> { 
                    if (textfieldValidation()){
                        try {
                            PreparedStatement st = con.prepareStatement("UPDATE Customer SET CusName = ?, Company = ?, Phone = ?, Email = ? "+
                                    "WHERE CusID = ?");
                            st.setString(1, txtName.getText());
                            st.setString(2, txtCompany.getText());
                            st.setString(3, txtPhone.getText());
                            st.setString(4, txtEmail.getText());
                            st.setString(5, String.valueOf(cus.getId()));
                            st.executeUpdate();
                        } catch (SQLException e) { e.printStackTrace(); }
                        textfieldDisable(true);                   
                        switchSideBtn(true, false);
                         tableCustomer.setDisable(false);
                        setTable();
                        lblInfo.setStyle("-fx-text-fill: green;");
                        lblInfo.setText("Customer info has been updated");
                        fadeText();
                    }
                });
                // sự kiện cancel Edit
                btnCancel.setOnAction(eventC -> {                
                    setTextfield(String.valueOf(cus.getId()),
                        cus.getName(),
                        cus.getComp(),
                        cus.getPhone(),
                        cus.getMail());
                    textfieldDisable(true);
                    switchSideBtn(true, false);
                    tableCustomer.setDisable(false);
                    lblInfo.setText("");
                });
            } else {
                lblInfo.setStyle("-fx-text-fill: orange;");
                lblInfo.setText("Please select a customer");
                fadeText();
            }
        });
    }
    // Sự kiện nút DEl
    private void btnDelAction() {
        btnDel.setOnAction(eventD -> {
            Customer cus = tableCustomer.getSelectionModel().getSelectedItem();
            if (cus != null&& !txtID.getText().isEmpty()){
                switchSideBtn(false, true);
                tableCustomer.setDisable(true);
                textfieldDisable(true);
                lblInfo.setStyle("-fx-text-fill: red;");
                lblInfo.setText("Are you sure to delete this Customer ?");
                // sự kiện yes of Del
                btnSave.setOnAction(eventS -> { 
                    try {
                        PreparedStatement st = con.prepareStatement("DELETE FROM Customer WHERE CusID = "+cus.getId());
                        st.executeUpdate();
                    } catch (SQLException e) { e.printStackTrace(); }
                    switchSideBtn(true, false);
                    tableCustomer.setDisable(false);
                    clearTextfield();
                    setTable();
                    lblInfo.setStyle("-fx-text-fill: green;");
                    lblInfo.setText("Customer has been deleted");
                    fadeText();
                });
                // sự kiện của No of Del
                btnCancel.setOnAction(eventC -> {
                    switchSideBtn(true, false);
                    tableCustomer.setDisable(false);
                    lblInfo.setText("");
                });
            } else {    
                lblInfo.setStyle("-fx-text-fill: orange;");
                lblInfo.setText("Please select a customer");
                fadeText();
            }
        });
    }
    // Phần bắt lỗi validation
    private boolean textfieldValidation() {
//        txtName.textProperty().addListener((z, x, enteredText) -> {
//            if (enteredText.matches(".*\\d.*")){
//                lblInfo.setText("Name cannot contain number");
//            } else {
//                lblInfo.setText("");
//            }
//        });
        fadeTextStop();
        lblInfo.setStyle("-fx-text-fill: red;");
        String name, com, phone, mail;
        name = txtName.getText();   
        if (name.isEmpty()){
            lblInfo.setText("Please enter customer's name");
            txtName.requestFocus();
            return false;
        } else if (name.matches(".*\\d.*")) {
            lblInfo.setText("Name cannot contain number");
            txtName.requestFocus();
            return false;
        } else if (name.length() < 2 || name.length() > 140){
            lblInfo.setText("Name must be greater than 1 and fewer than 140 characters");
            txtName.requestFocus();
            return false;
        }
  
        com = txtCompany.getText().isEmpty() ? "" :  txtCompany.getText();
        if (com.length() > 30){
            lblInfo.setText("Company name must be less than 30 characters");
            txtCompany.requestFocus();
            return false;
        }
        
        phone = txtPhone.getText();  
        if (phone.isEmpty()){
            lblInfo.setText("Please enter customer's phone");
            txtPhone.requestFocus();
            return false;
        } else if (!phone.matches("^\\d+$")){
            lblInfo.setText("Phone number can only contain numbers");
            txtPhone.requestFocus();
            return false;
        } else if (phone.length() < 6 || phone.length() > 30){
            lblInfo.setText("Phone number must be greater than 6 and less than 30 digits");
            txtPhone.requestFocus();
            return false;
        }
        
        mail = txtEmail.getText();
        if (mail.isEmpty()){
            lblInfo.setText("Please enter customer's email address");
            txtEmail.requestFocus();
            return false;
        } else if (!P_EMAIL.matcher(mail).matches()) {
            lblInfo.setText("Invalid email format");
            txtEmail.requestFocus();
            return false;
        } else if (mail.length() > 30){
            lblInfo.setText("Email must be less than 30 digits");
            txtEmail.requestFocus();
            return false;
        }
        
        lblInfo.setText("");
        return true;
    }
    // Sự kiện chuyển đổi giữa các nút b1 và b2( Add và cancel )
    private void switchSideBtn(boolean b1, boolean b2) {
        btnAdd.setVisible(b1);
        btnEdit.setVisible(b1);
        btnDel.setVisible(b1);
        btnSave.setVisible(b2);
        btnCancel.setVisible(b2);
    }
    // Sự kiện làm mờ chữ 
    private void fadeText() {
        fadeTextStop();
        timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(1500),
                       new KeyValue (lblInfo.opacityProperty(), 0)); 
        timeline.getKeyFrames().add(key);
        timeline.play();
    }
    // sự kiện dừng thời gian lập lại sự kiện
    private void fadeTextStop() {
        timeline.stop();
        lblInfo.opacityProperty().set(1);
    }
    
}
