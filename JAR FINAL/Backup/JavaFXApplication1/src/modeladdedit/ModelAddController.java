/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeladdedit;

import account_login.Account;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainpanel.ModelPaneController;
import static mainpanel.ModelPaneController.P_BODY;
import static mainpanel.ModelPaneController.SRC_DIR;
import mainpanel.methods.Models;

/**
 *
 * @author Jonathan
 */

public class ModelAddController extends AnchorPane{
    Connection con;
    ResultSet rs;
    Statement stmt ;
    String sql, url;
    String name, city, body, skill, image;
    int age;
    int gender = 99;
    int available = 0;
    final FileChooser fileChooser;
    String add;
    File file;
    public ObservableList listSkill, listSkill1;
    public ObservableList listLang, listLang1;
    public TreeMap mapCity, mapSkill, mapLang;
    
    @FXML public Button btnOk2, btnBack2;
    @FXML public TextField txtCreate;
    @FXML public Button btnNext1, btnPrev1, btnSave1, btnExit1, btnAdd1;
    @FXML public ListView listView, listView1;
    @FXML public Button btnCancle, btnSave, btnAddSkill, btnAddCity, btnImg, btnImgPrev, btnImgNext, btnImgDel, btnUpdate;
    @FXML public ComboBox<String> cboCity, cboGender;
    @FXML public TextField txtName, txtAge, txtBody1, txtBody2, txtBody3;
    @FXML public TextArea txaSkill, txaLang;
    @FXML public RadioButton rdoAvailable;
    @FXML public AnchorPane SkillAdd, ModelAdd, SkillCreate;
    @FXML public DatePicker txtDOB;
    @FXML public ImageView ava;
    @FXML public HBox btnControl, avaContainer;
    @FXML public Label lblError, lblTitle;
    public static final String IMG_DIR = System.getProperty("user.dir")+"\\src\\mainpanel\\images\\";
    ModelPaneController mpController;
    private final int accRole;
    private final String accCity;
    Locale currentLocale;
    ResourceBundle rb;
    
    public ModelAddController(ModelPaneController mpc, Account account, Connection conn, Locale currentLocale, ResourceBundle rb) throws IOException, SQLException {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/modeladdedit/ModelAdd.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
        
        mpController = mpc;
        con = conn;
        this.accRole = account.getRole();
        this.accCity = account.getCity();
        this.currentLocale = currentLocale;
        this.rb = rb;
        setLanguage();
        
        if (accRole == 2){
            cboCity.setDisable(true);
            cboCity.setValue(accCity);
            cboCity.setStyle("-fx-Opacity:1.0");
        }
        
        //DatabseConnection
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
        //Combobox Gender
        cboGender.getItems().setAll("Female", "Male");
        
        //Datepicker Value
        txtDOB.setValue(LocalDate.now().minusYears(18));
        txtDOB.getEditor().setEditable(false);
        
        //Combobox City
        ObservableList listCity = FXCollections.observableArrayList();
        mapCity = new TreeMap();
        sql = "select * from City";
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            listCity.add(rs.getString("City"));
            mapCity.put(rs.getString("City"), rs.getString("CityID"));
        }
        cboCity.setItems(listCity);
        
        //ListView Skill
        mapSkill = new TreeMap();
        listSkill  = FXCollections.observableArrayList();
        listSkill1  = FXCollections.observableArrayList();
        sql = "select * from Skills";
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            listSkill.add(rs.getString("Skill"));
            mapSkill.put(rs.getString("Skill"), rs.getString("SkillID"));
        }
        
        //ListView Language
        mapLang = new TreeMap();
        listLang  = FXCollections.observableArrayList();
        listLang1  = FXCollections.observableArrayList();
        sql = "select * from Language";
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            listLang.add(rs.getString("Language"));
            mapLang.put(rs.getString("Language"), rs.getString("LanguageID"));
        }
        ListViewClicked();
        ListView1Clicked();
        fileChooser = new FileChooser();
        configuringFileChooser(fileChooser);
        
       //ImageView
       avaContainer.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent e) {
               btnControl.setVisible(true);
           }
       });
       
       btnControl.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent e) {
               btnControl.setVisible(true);
           }
       });
       
       btnControl.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent e) {
               btnControl.setVisible(false);
           }
       });
       
       avaContainer.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent e) {
               btnControl.setVisible(false);
           }
       });
       btnImgPrev.setVisible(false);
       btnImgDel.setVisible(false);
       btnImgNext.setVisible(false);
    }
    public void clearTxt() throws SQLException{
        lblTitle.setText("ADD MODEL");
        txtName.setText("");
        txtBody1.setText("");
        txtBody2.setText("");
        txtBody3.setText("");
        cboGender.valueProperty().set(null);
        cboCity.valueProperty().set(null);
        txaLang.setText("");
        txaSkill.setText("");
        txtDOB.getEditor().clear();
        ava.setImage(new Image("/mainpanel/images/no image.png", 230, 230, true, true));
        listSkill = FXCollections.observableArrayList();
        listSkill1  = FXCollections.observableArrayList();
        listLang  = FXCollections.observableArrayList();
        listLang1  = FXCollections.observableArrayList();
        
        sql = "select * from Skills";
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            listSkill.add(rs.getString("Skill"));
            mapSkill.put(rs.getString("Skill"), rs.getString("SkillID"));
        }
        
        listLang  = FXCollections.observableArrayList();
        listLang1  = FXCollections.observableArrayList();
        sql = "select * from Language";
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            listLang.add(rs.getString("Language"));
            mapLang.put(rs.getString("Language"), rs.getString("LanguageID"));
        }
        
        btnImgPrev.setVisible(false);
        btnImgNext.setVisible(false);
        btnImgDel.setVisible(false);
        listImage = new ArrayList<>();
        
        btnUpdate.setVisible(false);
        btnSave.setVisible(true);
    }
    //-----------------------------Model Add------------------------------------
    @FXML public void cancle(ActionEvent event) throws IOException, SQLException{
        mpController.btnAddAction();
    }
    
    @FXML private void saveModel(ActionEvent event) throws SQLException, IOException{
        //Add to SQL
        if (validation()){
        String date = String.valueOf(txtDOB.getValue().getYear())+"-"+String.valueOf(txtDOB.getValue().getMonth())+"-"+String.valueOf(txtDOB.getValue().getDayOfMonth());
        gender = cboGender.getValue().equals("Female") ? 1 : 0 ;
            try {
                sql = "Insert into Model values('"+name+"', '"+date+"', "+gender+", '"+body+"', '"+mapCity.get(city)+"', "+available+")";
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sql = "select * from Model";
            rs = stmt.executeQuery(sql);
            rs.last();
            int modelID = rs.getInt("ModelID");

            for (int i = 0; i < listSkill1.size(); i++) {
                sql = "Insert into ModelSkill values ("+modelID+", '"+mapSkill.get(listSkill1.get(i))+"')";
                stmt.executeUpdate(sql);
            }
            for (int i = 0; i < listLang1.size(); i++) {
                sql = "Insert into ModelLanguage values ("+modelID+", "+mapLang.get(listLang1.get(i))+")";
                stmt.executeUpdate(sql);
            }

            if(listImage.size()>0){
                for (int i = 0; i < listImage.size(); i++) {
                    //Đổi lại thư mục ảnh người mẫu này.
                    File dest = new File( System.getProperty("user.dir")+"\\src\\mainpanel\\images\\"+modelID+"_"+i+".jpg");
                    //Copy vao thu muc mainpanel/images/
                    Files.copy(listImage.get(i).toPath(), dest.toPath());
                    //Luu URL vao SQL
                    sql = "insert into ModelImg values ('/mainpanel/images/"+modelID+"_"+i+".jpg', "+modelID+")";
                    stmt.executeUpdate(sql);
                }
                mpController.addOneModel("file:///"+listImage.get(0).getPath().replaceAll("\\\\", "/"));
            }
            else
                mpController.addOneModel("/mainpanel/images/default.png");
            mpController.btnAddAction();
        }
    }
    @FXML private void updateModel(ActionEvent event) throws SQLException, IOException{
        if (validation()) {
            List<String> album = new ArrayList<>();
            List<String> skills = new ArrayList<>();
            List<String> langs = new ArrayList<>();
            CheckBox genderCB = new CheckBox();
            String editBody = P_BODY.matcher(body).replaceAll("$2-$4-$6");
            
            int id = mpController.editModel.getId();
            String date = String.valueOf(txtDOB.getValue().getYear())+"-"+String.valueOf(txtDOB.getValue().getMonth())+"-"+String.valueOf(txtDOB.getValue().getDayOfMonth());
            gender = cboGender.getValue().equals("Female") ? 1 : 0 ;
            if (gender == 1)
                genderCB.setSelected(true);
            else
                genderCB.setSelected(false);
            
            
            sql = "Update Model set ModelName = '"+name+"', DOB = '"+date+"', Female = "+gender+", Body = '"+body+"', CityID = "+mapCity.get(city)+", Available = "+available+" where ModelID = "+id;
            stmt.executeUpdate(sql);
            sql = "select * from Model";
            rs = stmt.executeQuery(sql);
            rs.last();

            sql = "Delete from ModelSkill where ModelID = "+id;
            stmt.executeUpdate(sql);
            for (int i = 0; i < listSkill1.size(); i++) {
                sql = "Insert into ModelSkill values ("+id+", '"+mapSkill.get(listSkill1.get(i))+"')";
                stmt.executeUpdate(sql);
                skills.add(listSkill1.get(i).toString());
            }
            sql = "Delete from ModelLanguage where ModelID = "+id;
            stmt.executeUpdate(sql);
            for (int i = 0; i < listLang1.size(); i++) {
                sql = "Insert into ModelLanguage values ("+id+", "+mapLang.get(listLang1.get(i))+")";
                stmt.executeUpdate(sql);
                langs.add(listLang1.get(i).toString());
            }

            sql = "Delete from ModelImg where ModelID = "+id;
            stmt.executeUpdate(sql);
            String imgurl;
            if(listImage.size()>0){
                for (int i = 0; i < listImage.size(); i++) {
                    File dest = new File(IMG_DIR+id+"_"+i+".jpg");
                    try {
                        Files.copy(listImage.get(i).toPath(), dest.toPath());
                    } catch (FileAlreadyExistsException e) {
                        Files.delete(dest.toPath());
                        Files.copy(listImage.get(i).toPath(), dest.toPath());
                    }
                    
                    imgurl = "/mainpanel/images/"+id+"_"+i+".jpg";
                    album.add(imgurl);
                    sql = "insert into ModelImg values ('"+imgurl+"', "+id+")";
                    stmt.executeUpdate(sql);
                }
            }
            
            Models editedModel = new Models(id, album, name, genderCB, txtDOB.getValue(), editBody, city, skills, langs);
            clearTxt();
            mpController.btnAddAction();
            mpController.updateModel(editedModel);
        }
    }
    private boolean validation() {
        name = txtName.getText();
        LocalDate dateTxt = txtDOB.getValue();
        city = cboCity.getValue();
        int b, w, h;
        try {
            b = txtBody1.getText().isEmpty() ? 0 : Integer.parseInt(txtBody1.getText());
            w = txtBody2.getText().isEmpty() ? 0 : Integer.parseInt(txtBody2.getText());
            h = txtBody3.getText().isEmpty() ? 0 : Integer.parseInt(txtBody3.getText());
        } catch (NumberFormatException e) {
            lblError.setText(rb.getString("modelErrBody"));
            txtName.requestFocus();
            return false;
        }
        body = "b"+b+"w"+w+"h"+h;
        
        if (rdoAvailable.isSelected()){
            available = 1;
        }
        
        //ErrorHandling
        if (name.isEmpty()) {
            lblError.setText(rb.getString("modelErrNameBlank"));
            txtName.requestFocus();
            return false;
        } else if (name.matches(".*\\d.*")) {
            lblError.setText(rb.getString("modelErrNameNum"));
            txtName.requestFocus();
            return false;
        } else if (name.length() < 2 || name.length()> 150) {
            lblError.setText(rb.getString("modelErrNameRange"));
            txtName.requestFocus();
            return false;
        }
        
        if (cboGender.getValue()==null){
            lblError.setText(rb.getString("modelErrGen"));
            return false;
        }
        
        age = Period.between(dateTxt, LocalDate.now()).getYears();
        if (age<18){
            lblError.setText(rb.getString("modelErrAge"));
            return false;
        }
        if (city==null){
            lblError.setText(rb.getString("modelErrCity"));
            return false;
        }
        return true;
    }
    @FXML private void skillAdd(ActionEvent event) throws IOException, SQLException{
        listView.setItems(listSkill);
        listView1.setItems(listSkill1);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        add = "skill";
        txtCreate.setPromptText("Skill...");
        
        SkillAdd.setVisible(true);
        ModelAdd.setDisable(true);
    }
    @FXML private void languageAdd(ActionEvent event) throws IOException, SQLException{
        listView.setItems(listLang);
        listView1.setItems(listLang1);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        add = "lang";
        txtCreate.setPromptText("Language...");
        
        SkillAdd.setVisible(true);
        ModelAdd.setDisable(true);
    }
    
    //Image Controller------------
    public int img = 0;
    public ArrayList<File> listImage = new ArrayList<>();
    public void addImg(ActionEvent event){
        Stage stage = new Stage();
        file = fileChooser.showOpenDialog(stage);
        if (file!=null){
            listImage.add(file);
            Image image1 = new Image(file.toURI().toString());
            ava.setImage(image1);
            img = listImage.size()-1;
            btnImgDel.setVisible(true);
            btnImgNext.setVisible(false);
            if(listImage.size()==1){
                btnImgPrev.setVisible(false);
                btnImgDel.setVisible(true);
                btnImgNext.setVisible(false);
            } else {
                btnImgPrev.setVisible(true);
                btnImgDel.setVisible(true);
                btnImgNext.setVisible(false);
            }
        }
    }
    public void prevImg(ActionEvent event){
        img--;
        if(img==0){
            btnImgPrev.setVisible(false);
            btnImgDel.setVisible(true);
            btnImgNext.setVisible(true);
        }
        else {
            if(img>0);
                btnImgPrev.setVisible(true);
                btnImgDel.setVisible(true);
                btnImgNext.setVisible(true);
            
        }
        
        Image image1 = new Image(listImage.get(img).toURI().toString());
        ava.setImage(image1);
    }
    public void nextImg(ActionEvent event){
        img++;
        if(img==listImage.size()-1){
            btnImgPrev.setVisible(true);
            btnImgDel.setVisible(true);
            btnImgNext.setVisible(false);
        } else if (img<listImage.size()-1){
            btnImgPrev.setVisible(true);
            btnImgDel.setVisible(true);
            btnImgNext.setVisible(true);
        }
        Image image1 = new Image(listImage.get(img).toURI().toString());
        ava.setImage(image1);
    }
    public void deleteImg(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("confirmDelImg"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (listImage.size()==1){
                    listImage = new ArrayList<>();
                    File defaultImg = new File("/Users/macbook/Desktop/SourceCode/FXML/Application/src/nhom2/Icon/solid-cloud-gray-fabric_large.jpg");
                    Image image1 = new Image(defaultImg.toURI().toString());
                    ava.setImage(image1);
                    btnImgPrev.setVisible(false);
                    btnImgDel.setVisible(false);
                    btnImgNext.setVisible(false);
            } else {
                listImage.remove(img);
                if (img==0) {
                    Image image1 = new Image(listImage.get(img).toURI().toString());
                    ava.setImage(image1);
                    btnImgPrev.setVisible(false);
                    btnImgDel.setVisible(true);
                    btnImgNext.setVisible(true);
                    if (listImage.size()==1){
                        btnImgPrev.setVisible(false);
                        btnImgDel.setVisible(true);
                        btnImgNext.setVisible(false);
                    }
                }else {
                    if (img==listImage.size()-1){
                        btnImgPrev.setVisible(true);
                        btnImgDel.setVisible(true);
                        btnImgNext.setVisible(false);
                    }
                    if (listImage.size()==1){
                        btnImgPrev.setVisible(false);
                        btnImgDel.setVisible(true);
                        btnImgNext.setVisible(false);
                    }
                    img--;
                    Image image1 = new Image(listImage.get(img).toURI().toString());
                    ava.setImage(image1);

                }
            }
        }
    }
    private void configuringFileChooser(FileChooser fileChooser){
         fileChooser.setTitle("Select Pictures");
         fileChooser.getExtensionFilters().addAll(
               new FileChooser.ExtensionFilter("JPG", "*.jpg"), //
               new FileChooser.ExtensionFilter("PNG", "*.png"));
               
     }
    
    //-----------------------------Skill/Language Add------------------------------------
    @FXML public void next(ActionEvent event){
        try {
            if(add.equals("skill")) {
                listSkill1.addAll(listView.getSelectionModel().getSelectedItem().toString());
                listView1.setItems(listSkill1);

                listSkill.remove(listView.getSelectionModel().getSelectedIndex());
                listView.setItems(listSkill);
            }
            else {
                listLang1.addAll(listView.getSelectionModel().getSelectedItem().toString());
                listView1.setItems(listLang1);

                listLang.remove(listView.getSelectionModel().getSelectedIndex());
                listView.setItems(listLang);
            }
        } catch (NullPointerException e) {
        }
    }
    @FXML public void prev(ActionEvent event){
        try {
            if(add.equals("skill")) {
                listSkill.addAll(listView1.getSelectionModel().getSelectedItem().toString());
                listView.setItems(listSkill);

                listSkill1.remove(listView1.getSelectionModel().getSelectedIndex());
                listView1.setItems(listSkill1);
            }
            else{
                listLang.addAll(listView1.getSelectionModel().getSelectedItem().toString());
                listView.setItems(listLang);

                listLang1.remove(listView1.getSelectionModel().getSelectedIndex());
                listView1.setItems(listLang1);
            }
        } catch (NullPointerException e) {
        }
    }
    @FXML public void add(ActionEvent event) throws IOException{
        SkillCreate.setVisible(true);
        SkillAdd.setDisable(true);
        txtCreate.setText("");
    }
    @FXML public void save(ActionEvent event){
        if(add.equals("skill")){
            String skill = "";
            for (int i = 0; i < listSkill1.size(); i++) {
                skill += listSkill1.get(i);
                if (i!=(listSkill1.size()-1)) {
                    skill += ", ";
                } else skill += ".";
            }
            txaSkill.setText(skill);
        } else {
            if(add.equals("lang"));
            String lang = "";
            for (int i = 0; i < listLang1.size(); i++) {
                lang += listLang1.get(i);
                if (i!=(listLang1.size()-1)) {
                    lang += ", ";
                } else lang += ".";
            }
            txaLang.setText(lang);
        }
        SkillAdd.setVisible(false);
        ModelAdd.setDisable(false);
    }
    int click;
    String listItem;
    public void ListViewClicked(){
        listView.setOnMouseClicked(event -> {
            click = 0;
            listItem = listView.getSelectionModel().getSelectedItem().toString();
        });
    }
    public void ListView1Clicked(){
        listView1.setOnMouseClicked(event -> {
            click = 1;
            listItem = listView1.getSelectionModel().getSelectedItem().toString();
        });
    }
    @FXML public void del(ActionEvent event) throws SQLException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("confirmDelRec"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (add.equals("skill")) {
                if(click == 0){
                    listSkill.remove(listView.getSelectionModel().getSelectedIndex());
                    listView.setItems(listSkill);
                    sql = "Delete FROM Skills where SkillID = "+mapSkill.get(listItem);
                    stmt.executeUpdate(sql);

                }
                else if(click == 1){
                    listSkill1.remove(listView1.getSelectionModel().getSelectedIndex());
                    listView1.setItems(listSkill1);
                    sql = "Delete FROM Skills where SkillID = "+mapSkill.get(listItem);
                    stmt.executeUpdate(sql);
                    txaSkill.setText(name);
                    String skill = "";
                    for (int i = 0; i < listSkill1.size(); i++) {
                        skill += listSkill1.get(i);
                        if (i!=(listSkill1.size()-1)) {
                            skill += ", ";
                        } else skill += ".";
                    }
                    txaSkill.setText(skill);
                }
            }
            else {
                if(click == 0){
                    listLang.remove(listView.getSelectionModel().getSelectedIndex());
                    listView.setItems(listLang);
                    sql = "Delete FROM Language where LanguageID = "+mapLang.get(listItem);
                    stmt.executeUpdate(sql);
                }
                else if(click == 1){
                    listLang1.remove(listView1.getSelectionModel().getSelectedIndex());
                    listView1.setItems(listLang1);
                    sql = "Delete FROM Language where LanguageID = "+mapLang.get(listItem);
                    stmt.executeUpdate(sql);
                    String lang = "";
                    for (int i = 0; i < listLang1.size(); i++) {
                        lang += listLang1.get(i);
                        if (i!=(listLang1.size()-1)) {
                            lang += ", ";
                        } else lang += ".";
                    }
                    txaLang.setText(lang);
                }
            }
        }
    }

    @FXML public void Exit(ActionEvent event){
        SkillAdd.setVisible(false);
        ModelAdd.setDisable(false);
    }
    
    //-------------------------------Skill/Language Create-------------------------------
    @FXML public void OK(ActionEvent event) throws SQLException{
        String ele = txtCreate.getPromptText();
        if (ele.equals("Skill...")){
            String skill = txtCreate.getText();
            sql = "insert into Skills values ('"+skill+"')";
            stmt.executeUpdate(sql);
            listView.getItems().add(skill);
            
            sql = "select * from Skills";
            rs = stmt.executeQuery(sql);
            rs.last();
            mapSkill.put(skill, rs.getInt("SkillID"));
        }
        
        if (ele.equals("Language...")) {
             String lang = txtCreate.getText();
             sql = "insert into Language values ('"+lang+"')";
             stmt.executeUpdate(sql);
             listView.getItems().add(lang);
             
             sql = "select * from Language";
             rs = stmt.executeQuery(sql);
             rs.last();
             mapLang.put(lang, rs.getInt("LanguageID"));
         }
       
        SkillCreate.setVisible(false);
        SkillAdd.setDisable(false);
       
    }
    @FXML public void Back(ActionEvent event){
        SkillCreate.setVisible(false);
        SkillAdd.setDisable(false);
    }
    
    @FXML private Button btnExit;
    private void setLanguage() {
        btnImg.setText(rb.getString("addImg"));
        txtName.setPromptText(rb.getString("name"));
        txtDOB.setPromptText(rb.getString("dob"));
        txtBody1.setPromptText(rb.getString("bust"));
        txtBody2.setPromptText(rb.getString("waist"));
        txtBody3.setPromptText(rb.getString("hip"));
        txaSkill.setPromptText(rb.getString("skill")+"s...");
        txaLang.setPromptText(rb.getString("language")+"s...");
        cboGender.setPromptText(rb.getString("gender"));
        cboCity.setPromptText(rb.getString("city"));
        btnSave1.setText(rb.getString("save"));
        btnExit.setText(rb.getString("cancel"));
    }
}
