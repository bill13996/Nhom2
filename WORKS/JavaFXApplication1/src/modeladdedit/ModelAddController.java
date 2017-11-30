/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeladdedit;

import DBConnection.DBConnection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

/**
 *
 * @author Jonathan
 */

public class ModelAddController extends AnchorPane{
    Connection con = DBConnection.getConnect();
    ResultSet rs;
    Statement stmt ;
    String sql, url;
    String name, city, body, skill, image;
    int age;
    int gender = 2;
    int available = 0;
    final FileChooser fileChooser;
    String add;
    File file;
    ObservableList listSkill, listSkill1;
    ObservableList listLang, listLang1;
    TreeMap mapCity, mapSkill, mapLang;
    
    @FXML Button btnOk2, btnBack2;
    @FXML TextField txtCreate;
    @FXML Button btnNext1, btnPrev1, btnSave1, btnExit1, btnAdd1;
    @FXML ListView listView, listView1;
    @FXML Button btnCancle, btnSave, btnAddSkill, btnAddCity, btnImg;
    @FXML ComboBox<String> cboCity, cboGender;
    @FXML TextField txtName, txtAge, txtBody1, txtBody2, txtBody3;
    @FXML TextArea txaSkill, txaLang;
    @FXML RadioButton rdoAvailable;
    @FXML AnchorPane SkillAdd, ModelAdd, SkillCreate;
    @FXML DatePicker txtDOB;
    @FXML ImageView ava;
    @FXML HBox btnControl;
    ModelPaneController mpController;
    
    public ModelAddController(ModelPaneController mpc) throws IOException, SQLException {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/modeladdedit/ModelAdd.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
        mpController = mpc;
        //DatabseConnection
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
        //Combobox Gender
        cboGender.getItems().setAll("Female", "Male");
        
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
        fileChooser = new FileChooser();
        configuringFileChooser(fileChooser);
        
       //ImageView
       
       ava.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
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
       
       ava.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent e) {
               btnControl.setVisible(false);
           }
       });
       
       btnImgPrev.setVisible(false);
       btnImgDel.setVisible(false);
       btnImgNext.setVisible(false);
    }

    
    //-----------------------------Model Add------------------------------------
    @FXML
    public void cancle(ActionEvent event) throws IOException, SQLException{
        mpController.btnAddAction();
    }
    
    @FXML
    public void saveModel(ActionEvent event) throws SQLException, IOException, InterruptedException{
        name = txtName.getText();
        String genderTxt = cboGender.getValue();
        LocalDate dateTxt = txtDOB.getValue();
        city = cboCity.getValue();
        String b, w, h;
        b = txtBody1.getText().isEmpty() ? "0" : txtBody1.getText();
        w = txtBody2.getText().isEmpty() ? "0" : txtBody2.getText();
        h = txtBody3.getText().isEmpty() ? "0" : txtBody3.getText();
        body = "b"+b+"w"+w+"h"+h;
        if (rdoAvailable.isSelected()){
            available = 1;
        }
        
        //ErrorHandling
        if (name.length()==0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Name cannot be blank!");
            Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    txtName.requestFocus();
                    return;
                }
        }
        if (genderTxt==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a gender!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                    return;
                }
        }
        if (dateTxt==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a DOB!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                    return;
                }
        }
        age = Period.between(dateTxt, LocalDate.now()).getYears();
        if (age<18){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model must be above 18 years old. Please re-select a DOB!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                    return;
                }
        }
        if (city==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a city!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                    return;
                }
        }
        String date = String.valueOf(txtDOB.getValue().getYear())+"-"+String.valueOf(txtDOB.getValue().getMonth())+"-"+String.valueOf(txtDOB.getValue().getDayOfMonth());
        if (genderTxt.equalsIgnoreCase("Female")) {
            gender = 1;
        } else gender = 0;
        
        
        //Add to SQL
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to add this model?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
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
            }
            mpController.btnAddAction();
        }
    }

    public void skillAdd(ActionEvent event) throws IOException, SQLException{
        listView.setItems(listSkill);
        listView1.setItems(listSkill1);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        add = "skill";
        txtCreate.setPromptText("Skill...");
        
        SkillAdd.setVisible(true);
        ModelAdd.setDisable(true);
    }
    
    
    public void languageAdd(ActionEvent event) throws IOException, SQLException{
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
    @FXML Button btnImgPrev, btnImgNext, btnImgDel;
    
    int img = 0;
    
    ArrayList<File> listImage = new ArrayList<File>();
    public void addImg(ActionEvent event){
          Stage stage = new Stage();
          file = fileChooser.showOpenDialog(stage);
          if (file!=null){
                listImage.add(file);
                Image image1 = new Image(file.toURI().toString());
                ava.setImage(image1);
                img = listImage.size()-1;
                System.out.println("img = " +img);
                System.out.println("size = " +listImage.size());
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
        System.out.println(img);
        
    }
    
    public void nextImg(ActionEvent event){
        img++;
        if(img==listImage.size()-1){
            btnImgPrev.setVisible(true);
            btnImgDel.setVisible(true);
            btnImgNext.setVisible(false);
        }
        else {
            if(img<listImage.size()-1);
                btnImgPrev.setVisible(true);
                btnImgDel.setVisible(true);
                btnImgNext.setVisible(true);
        }
        Image image1 = new Image(listImage.get(img).toURI().toString());
        ava.setImage(image1);
        System.out.println(img);
    }
    
    public void deleteImg(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.ERROR, "Are your sure to delete this image?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (listImage.size()==1){
                    listImage = new ArrayList<File>();
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
     
    public void next(ActionEvent event){
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
    }
    
    @FXML
    public void prev(ActionEvent event){
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
    }
    
    @FXML
    public void add(ActionEvent event) throws IOException{
        SkillCreate.setVisible(true);
        SkillAdd.setDisable(true);
        txtCreate.setText("");
    }
    
    @FXML
    public void save(ActionEvent event){
        if(add.equals("skill")){
            String skill = "";
            for (int i = 0; i < listSkill1.size(); i++) {
                skill += listSkill1.get(i);
                if (i!=(listSkill1.size()-1)) {
                    skill += ", ";
                } else skill += ".";
            }
            txaSkill.setText(skill);
        }
        
        else {
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
    
    @FXML
    public void Exit(ActionEvent event){
        SkillAdd.setVisible(false);
        ModelAdd.setDisable(false);
    }
    
    
    //-------------------------------Skill/Language Create-------------------------------
     @FXML
    public void OK(ActionEvent event) throws SQLException{
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
    
    @FXML
    public void Back(ActionEvent event){
        SkillCreate.setVisible(false);
        SkillAdd.setDisable(false);
    }
}
