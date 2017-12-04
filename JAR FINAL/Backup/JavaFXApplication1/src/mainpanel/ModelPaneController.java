package mainpanel;

import account_login.Account;
import java.io.File;
import java.io.IOException;
import mainpanel.methods.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import modeladdedit.ModelAddController;

public class ModelPaneController extends AnchorPane{
    @FXML private TableView<Models> modelTable;
    @FXML private TableColumn<Models, ImageView> tabThumb;
    @FXML private TableColumn<Models, CheckBox> tabGender;
    @FXML private TableColumn<Models, String> tabName, tabAge, tabLoca, tabSkill, tabLang, tabID, tabBody;
    @FXML private TableView<ModelContract> tbDetailCon;
    @FXML private TableColumn<ModelContract, String> colDCID, colDCName, colDCStart, colDCEnd;
    @FXML private Button btnEdit, btnDel, btnDelNo, btnDelYes, btnFilDone, btnFilReset, btnFilCancel, btnDetailNext, btnDetailPrev;
    @FXML private Label lblAgeTo, lblBusTo, lblWaiTo, lblHipTo, lblDetailID;
    @FXML private ComboBox cbbAgeSign, cbbBusSign, cbbWaiSign, cbbHipSign, cbbAgeVal1, cbbBusVal1, cbbWaiVal1, cbbHipVal1, 
            cbbAgeVal2, cbbBusVal2, cbbWaiVal2, cbbHipVal2, cbbCity;
    @FXML private TextField searchBar;
    @FXML private RadioButton radGenAll, radGenMal, radGenFem, radDisAll, radDisAv, radDisUn;
    @FXML public Pane addModal, delModal, filModal;
    @FXML private FlowPane skillPane, langPane;
    @FXML private AnchorPane topContainer, modelDetailPanel;
    @FXML private StackPane avatarsPane;
    @FXML private TextField tfDetailName, tfDetailAge, tfDetailCity, tfDetailGen, tfDetailBus, tfDetailWai, tfDetailHip;
    @FXML private VBox vbDetailSkill, vbDetailLang;
    //@FXML private Pagination pagination;
    
    static Connection con;
    private final Account account;
    private final int accRole;
    private final String accCity;
    Locale currentLocale;
    ResourceBundle rb;
    
    ModelAddController modelAdd;
    ObservableList<Models> dataList;
    ObservableList<CheckBox> filterSkillList, filterLangList;
    FilteredList<Models> foundData ;
    List<String> selectedSkill, selectedLang;
    List<Integer> modelOnContract;
    public static final Pattern P_BODY = Pattern.compile("^(b(\\d+))(w(\\d+))(h(\\d+))");
    public static final String SRC_DIR = System.getProperty("user.dir").replaceAll("\\\\", "/")+"/src";
    public Models editModel;
    public int editRowIndex;
    //private final static int rowsPerPage = 20;
    
    public ModelPaneController(Account account, Connection conn, Locale currentLocale, ResourceBundle rb) throws IOException, SQLException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainpanel/ModelPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
        
        con = conn;
        this.account = account;
        this.accRole = account.getRole();
        this.accCity = account.getCity();
        this.currentLocale = currentLocale;
        this.rb = rb;
        setLanguage();
        
        btnDel.setDisable(true);
        btnEdit.setDisable(true);
        addModal.setLayoutY(-500);
        delModal.setLayoutY(-100);
        filModal.setLayoutY(-250);
        modelDetailPanel.setLayoutX(1000);
        
        getModelOnContractList();
        setModelTable();
        filterSetup();
        setAddModal(fxmlLoader.getController());
        tableOnClickAction();
        filterAction();
        filterReset();
        searchBarAction();
        
        permission();
    }
    
    private void permission() {
        if (accRole == 2){
            cbbCity.setValue(account.getCity());
            FilteredList<Models> filteredData = new FilteredList<>(dataList, p -> true);
            filteredData.setPredicate(model -> {
                return filterCheckLocation(model);
            });
            modelTable.setItems(filteredData);
        }
    }
    private void tableOnClickAction(){
        modelTable.setOnMousePressed((MouseEvent me) -> {
            Models model = modelTable.getSelectionModel().getSelectedItem();
            if (model != null){
                if (accRole == 1 || (accRole == 2 && accCity.equals(model.getCity()))){
                    btnDel.setDisable(false);
                    btnEdit.setDisable(false);
                } else {
                    btnDel.setDisable(true);
                    btnEdit.setDisable(true);
                }
                if (me.getClickCount() == 2){
                    try {
                        setModelDetail(model);
                        modelDetailPane(modelDetailPanel);
                    } catch (SQLException ex) {}
                }
            }
        });
    }
    
    private void setModelDetail(Models model) throws SQLException {
        /*
        PreparedStatement st = con.prepareStatement("SELECT ImgURL FROM ModelImg" +
                " WHERE ModelID = "+model.getId(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery();
        avatarsPane.getChildren().clear();
        ImageView img = new ImageView();
        rs.afterLast();
        while (rs.previous()){
            img = new ImageView(new Image("file:///"+SRC_DIR+rs.getString("ImgURL"), 250, 250, true, true));
            img.setVisible(false);
            avatarsPane.getChildren().add(img);
        }
        st.close();
        img.setVisible(true);
        */
        avatarsPane.getChildren().clear();
        List<String> album = model.getAlbum();
        ImageView img = new ImageView(new Image("file:///"+SRC_DIR+"/mainpanel/images/default.png", 250, 250, true, true));
        if (album.isEmpty())
            avatarsPane.getChildren().add(img);
        else{
            for (int i = album.size()-1; i >= 0; i--) {
                img = new ImageView(new Image("file:///"+SRC_DIR+album.get(i), 250, 250, true, true));
                if (i != 0)
                    img.setVisible(false);
                avatarsPane.getChildren().add(img);
            }
        }
        modelDetailAlbum();
        lblDetailID.setText(String.valueOf(model.getId()));
        tfDetailName.setText(model.getName());
        tfDetailAge.setText(String.valueOf(model.getAge()));
        tfDetailCity.setText(model.getCity());
        String gender = model.getGender().isSelected() ? "Female" : "Male";
        tfDetailGen.setText(gender);
        String[] body = model.getBody().split("-");
        tfDetailBus.setText(body[0]);
        tfDetailWai.setText(body[1]);
        tfDetailHip.setText(body[2]);
        
        vbDetailSkill.getChildren().clear();
        model.getSkillList().forEach((skill) -> {
            vbDetailSkill.getChildren().add(new Label(skill+" -"));
        });
        vbDetailLang.getChildren().clear();
        model.getLangAsList().forEach((lang) -> {
            vbDetailLang.getChildren().add(new Label("- "+lang));
        });
        
        ObservableList modelContractList = FXCollections.observableArrayList();
        PreparedStatement st = con.prepareStatement("SELECT C.ContractID, ConName, ConStart, ConEnd FROM ModelContract M INNER JOIN Contract C" +
            " ON M.ContractID = C.ContractID AND M.ModelID = "+model.getId());
        ResultSet rs = st.executeQuery();
        while (rs.next()){
            modelContractList.add(new ModelContract(rs.getInt("ContractID"), rs.getNString("ConName"), rs.getDate("ConStart").toLocalDate(), rs.getDate("ConEnd").toLocalDate()));
        }
        st.close();
        tbDetailCon.setItems(modelContractList);
        colDCID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDCName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDCStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colDCEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
    }
    private void modelDetailAlbum() {
        ObservableList<Node> nodeList = avatarsPane.getChildren();
        if (nodeList.size() > 1) {
            btnModelDetailDisable(false);
            
            btnDetailNext.setOnAction((ActionEvent event) -> { 
                Node topNode = nodeList.get(nodeList.size()-1);
                topNode.setVisible(false);
                topNode.toBack();

                Node newTopNode = nodeList.get(nodeList.size()-1);
                newTopNode.setVisible(true);
            });
            btnDetailPrev.setOnAction((ActionEvent event) -> { 
                Node topNode = nodeList.get(nodeList.size()-1);
                topNode.setVisible(false);

                Node newTopNode = nodeList.get(nodeList.size()-nodeList.size());
                newTopNode.setVisible(true);
                newTopNode.toFront();
            });
        } else {
            btnModelDetailDisable(true);
        }
    }
    private void btnModelDetailDisable(boolean boo) {
        btnDetailNext.setDisable(boo);
        btnDetailPrev.setDisable(boo);
        String cap = boo ? "-fx-opacity: 0.2" : "-fx-opacity: 1";
        btnDetailNext.setStyle(cap);
        btnDetailPrev.setStyle(cap);
    }
    private void modelDetailPane(AnchorPane ap){
        if (!topContainer.isVisible()){
            topContainer.setVisible(true);
            ap.setVisible(true);
            topModalAni(ap.layoutXProperty(), 600);
        } else {
            topModalAni(ap.layoutXProperty(), 1000).setOnFinished((ActionEvent event) -> {
                topContainer.setVisible(false);
                ap.setVisible(false);
            });
        }
    }
    
    public  void setModelTable() throws SQLException{
        //Add models to row list
        dataList = FXCollections.observableArrayList();
        PreparedStatement st = con.prepareStatement("SELECT Model.*, City.City FROM Model LEFT JOIN City" +
            " ON Model.CityID = City.CityID");
        ResultSet rs = st.executeQuery();
        while (rs.next())
            addDataList(rs.getInt("ModelID"), rs, "/mainpanel/images/default.png");
        st.close();
        //Add row list to table
        modelTable.setItems(dataList);
        //Set opacity for busy model
        modelTable.setRowFactory(tv -> new TableRow<Models>() {
            @Override
            public void updateItem(Models model, boolean empty) {
                super.updateItem(model, empty);
                if (model == null)
                    setStyle("");
                else {
                    modelOnContract.stream().forEach(id -> {
                        if (id == model.getId())
                            setStyle("-fx-opacity: 0.4;");
                    });
                }
            }
        });
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
        
        /*
        pagination.setPageCount((dataList.size() / rowsPerPage + 1));
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return dataPerPage(pageIndex);
            }
        });
        */
    }
    private void addDataList(int id, ResultSet rs, String url) throws SQLException {
            int modelID = id;
            String name, city, body, imgURL=url;
            List<String> album = new ArrayList<>();
            List<String> skill = new ArrayList<>();
            List<String> lang = new ArrayList<>();
            LocalDate dob;
            CheckBox gender = new CheckBox();
            gender.setDisable(true);
            gender.setStyle("-fx-opacity: 1");
            PreparedStatement st;
            ResultSet rs2;

            //Get all model's pictures
            st = con.prepareStatement("SELECT ImgURL FROM ModelImg" +
                " WHERE ModelID = "+modelID);
            rs2 = st.executeQuery();
            while (rs2.next())
                album.add(rs2.getString("ImgURL"));

            //Get all model's skills
            st = con.prepareStatement("SELECT B.Skill FROM ModelSkill A INNER JOIN Skills B" +
                " ON A.ModelID = "+modelID+" AND A.SkillID = B.SkillID");
            rs2 = st.executeQuery();
            while (rs2.next())
                skill.add(rs2.getString("Skill"));

            //Get all model's languages
            st = con.prepareStatement("SELECT B.Language FROM ModelLanguage A INNER JOIN Language B" +
                " ON A.ModelID = "+modelID+" AND A.LanguageID = B.LanguageID");
            rs2 = st.executeQuery();
            while (rs2.next())
                lang.add(rs2.getString("Language"));
            st.close();
            
            //Extract data and add to row
            gender.setSelected(rs.getBoolean("female"));
            body = rs.getString("Body")!=null ? P_BODY.matcher(rs.getString("Body")).replaceAll("$2-$4-$6") : "" ;
            dob = rs.getDate("DoB")!=null ? rs.getDate("DoB").toLocalDate() : LocalDate.now() ;
            city = rs.getNString("City")!=null ? rs.getString("City") : "" ;
            name = rs.getNString("ModelName")!=null ? rs.getString("ModelName") : "" ;

            dataList.add(new Models(modelID, album, name, gender, dob, body, city, skill, lang));
            foundData = new FilteredList<>(dataList, p -> true);
    }
    public  void addOneModel(String url) throws SQLException{
        PreparedStatement st = con.prepareStatement("SELECT Model.*, City.City FROM Model LEFT JOIN City" +
            " ON Model.CityID = City.CityID", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery();
        rs.last();
        addDataList(rs.getInt("ModelID"), rs, url);
        st.close();
        modelTable.refresh();
    }
    public  void updateModel(Models editedModel) throws SQLException{
        dataList.set(editRowIndex, editedModel);
        foundData = new FilteredList<>(dataList, p -> true);
        modelTable.refresh();
    }
    private void getModelOnContractList() throws SQLException {
        PreparedStatement st = con.prepareStatement("SELECT M.ModelID, C.ContractID, C.ConStart, C.ConEnd FROM Contract C INNER JOIN ModelContract M" +
                " ON C.ContractID = M.ContractID");
        ResultSet rs = st.executeQuery();
        modelOnContract = new ArrayList<>();
        
        while (rs.next()){
            LocalDate today, startDate, endDate;
            today = LocalDate.now();
            startDate = rs.getDate("ConStart").toLocalDate();
            endDate = rs.getDate("ConEnd").toLocalDate();

            if (today.isAfter(startDate) && today.isBefore(endDate)){
                modelOnContract.add(rs.getInt("ModelID"));
            }
        }
        st.close();
    }
    private void wrapTableColumn(TableColumn column) {
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
    
    public void setAddModal(ModelPaneController controller) throws IOException, SQLException {
        modelAdd = new ModelAddController(controller, account, con, currentLocale, rb);
        addModal.getChildren().clear();
        addModal.getChildren().add(modelAdd);
    }

    private void filterSetup() throws SQLException{
        filterSetupCbb1(cbbAgeSign, cbbBusSign, cbbWaiSign, cbbHipSign);
        filterSetupCbb2(18, 50, 1, cbbAgeVal1);
        filterSetupCbb2(18, 50, 1, cbbAgeVal2);
        filterSetupCbb2(5, 150, 5, cbbBusVal1, cbbWaiVal1, cbbHipVal1);
        filterSetupCbb2(5, 150, 5, cbbBusVal2, cbbWaiVal2, cbbHipVal2);
        filterSetupCbb3(cbbAgeSign, cbbAgeVal2, lblAgeTo);
        filterSetupCbb3(cbbBusSign, cbbBusVal2, lblBusTo);
        filterSetupCbb3(cbbWaiSign, cbbWaiVal2, lblWaiTo);
        filterSetupCbb3(cbbHipSign, cbbHipVal2, lblHipTo);
        filterSetupCbb4(cbbAgeVal1, cbbAgeVal2);
        filterSetupCbb4(cbbBusVal1, cbbBusVal2);
        filterSetupCbb4(cbbWaiVal1, cbbWaiVal2);
        filterSetupCbb4(cbbHipVal1, cbbHipVal2);
        
        PreparedStatement st;
        ResultSet rs;
        
            st = con.prepareStatement("SELECT DISTINCT City FROM Model A INNER JOIN City B ON A.CityID = B.CityID");
            rs = st.executeQuery();
            while (rs.next())
                cbbCity.getItems().add(rs.getString("City"));
            cbbCity.getItems().add(0, rb.getString("all"));
            cbbCity.setValue(rb.getString("selectCity"));
            
            filterSkillList = FXCollections.observableArrayList();
            st = con.prepareStatement("SELECT DISTINCT Skill FROM ModelSkill A INNER JOIN Skills B ON A.SkillID = B.SkillID ORDER BY Skill ASC");
            rs = st.executeQuery();
            while (rs.next())
                filterSkillList.add(new CheckBox(rs.getString("Skill")));
            skillPane.getChildren().addAll(filterSkillList);
            
            filterLangList = FXCollections.observableArrayList();
            st = con.prepareStatement("SELECT DISTINCT Language FROM ModelLanguage A INNER JOIN Language B ON A.LanguageID = B.LanguageID ORDER BY Language ASC");
            rs = st.executeQuery();
            while (rs.next())
                filterLangList.add(new CheckBox(rs.getString("Language")));
            langPane.getChildren().addAll(filterLangList);
            
            st.close();
            
    }
    private void filterSetupCbb1(ComboBox... cbbSign) {
        for (ComboBox cbb : cbbSign){
            cbb.getItems().addAll(">", "≥", "<", "≤", "=", "Between");
            cbb.setValue(">");
        }
    }
    private void filterSetupCbb2(int f, int l, int icr, ComboBox... cbbSign) {
        for (ComboBox cbb : cbbSign){
            for (int i = f; i <= l; i+=icr)
                cbb.getItems().add(i);
            cbb.setValue("");
        }
    }
    private void filterSetupCbb3(ComboBox cbbSign, ComboBox cbbVal2, Label lblTo) {
        cbbSign.setOnAction((event) -> {
            String x = cbbSign.getSelectionModel().getSelectedItem().toString();
            if (x.equals("Between")){
                cbbVal2.setDisable(false);
                cbbVal2.setStyle("-fx-opacity: 1");
                lblTo.setStyle("-fx-opacity: 1");
            } else {
                cbbVal2.setDisable(true);
                cbbVal2.setStyle("-fx-opacity: 0.3");
                lblTo.setStyle("-fx-opacity: 0.3");
            }
        });
    }
    private void filterSetupCbb4(ComboBox cbbVal1, ComboBox cbbVal2) {
        cbbVal1.setOnAction((event) -> {
            String sv1 = cbbVal1.getValue().toString();
            if (sv1.isEmpty())
                return;
            String sv2 = cbbVal2.getValue().toString();
            int v1 = Integer.parseInt(sv1);
            int v2 = sv2.isEmpty() ? 0 : Integer.parseInt(sv2);
                if (v1 > v2)
                    cbbVal2.setValue(v1);
        });
        cbbVal2.setOnAction((event) -> {
            String sv2 = cbbVal2.getValue().toString();
            if (sv2.isEmpty())
                return;
            String sv1 = cbbVal1.getValue().toString();
            int v2 = Integer.parseInt(sv2);
            int v1 = sv1.isEmpty() ? 0 : Integer.parseInt(sv1);
                if (v1 > v2 || v1 == 0)
                    cbbVal1.setValue(v2);
        });
    }
   
    private void filterAction(){
        FilteredList<Models> filteredData = new FilteredList<>(dataList, p -> true);
        btnFilDone.setOnAction(event -> {
            selectedSkill = new ArrayList<>();
            selectedLang = new ArrayList<>();
            filteredData.setPredicate(model -> {
                if (!filterCheckAgeOrBody(model.getAge(), cbbAgeSign, cbbAgeVal1, cbbAgeVal2))
                    return false;
                if (!filterCheckLocation(model))
                    return false;
                if (!filterCheckGender(model))
                    return false;
                if (!filterCheckAvaiability(model))
                    return false;
                if (!filterCheckSkillOrLang(model.getSkillList(), selectedSkill, filterSkillList))
                    return false;
                if (!filterCheckSkillOrLang(model.getLangAsList(), selectedLang, filterLangList))
                    return false;
                
                if (!model.getBody().isEmpty()){
                    String[] body = model.getBody().split("-");
                    if (!filterCheckAgeOrBody(Integer.parseInt(body[0]), cbbBusSign, cbbBusVal1, cbbBusVal2))
                        return false;
                    if (!filterCheckAgeOrBody(Integer.parseInt(body[1]), cbbWaiSign, cbbWaiVal1, cbbWaiVal2))
                        return false;
                    if (!filterCheckAgeOrBody(Integer.parseInt(body[2]), cbbHipSign, cbbHipVal1, cbbHipVal2))
                        return false;
                }
                //If model pass all the checks
                return true;
            });
            topModalAction(filModal);
            modelTable.setItems(filteredData);// 5. Add sorted (and filtered) data to the table.
            
        });
        //SortedList<Models> sortedData = new SortedList<>(filteredData);// 3. Wrap the FilteredList in a SortedList.    
        //sortedData.comparatorProperty().bind(modelTable.comparatorProperty());// 4. Bind the SortedList comparator to the TableView comparator.
    }
    private boolean filterCheckAgeOrBody(int model, ComboBox cbbSign, ComboBox cbbVal1, ComboBox cbbVal2){
        String age1s, sign;
        int age1, age2;
        age1s = cbbVal1.getValue().toString();
        age1 = !age1s.isEmpty() ? Integer.parseInt(age1s) : 0 ;
        sign = cbbSign.getValue().toString();
        if (age1 == 0)
            return true;
        else {
            switch (sign){
                case ">":
                    if (model > age1)
                        return true;
                    break;
                case "≥":
                    if (model >= age1)
                        return true;
                    break;
                case "<":
                    if (model < age1)
                        return true;
                    break;
                case "≤":
                    if (model <= age1)
                        return true;
                    break;
                case "=":
                    if (model == age1)
                        return true;
                    break;
                case "Between":
                    age2 = Integer.parseInt(cbbVal2.getValue().toString());
                    if (model >= age1 && model <= age2)
                        return true;
                    break;
            }
        }
        //If no value selected
        return false;
    }
    private boolean filterCheckLocation(Models model){
        String city = cbbCity.getValue().toString();
        if (city.equals(rb.getString("selectCity")) || city.equals(rb.getString("all")))
            return true;
        else if ( city.equals( model.getCity()) ) {
            return true;
        }
        return false;
    }
    private boolean filterCheckGender(Models model){
        if (radGenAll.isSelected())
            return true;
        else{
            Boolean isFemale = model.getGender().isSelected();
            if (radGenFem.isSelected() && isFemale)
                return true;
            else if (radGenMal.isSelected() && !isFemale)
                return true;
        }
        return false;
    }
    private boolean filterCheckAvaiability(Models model){
        if (radDisAll.isSelected())
            return true;
        else{
            Boolean isBusy = modelOnContract.contains(model.getId());
            if (radDisAv.isSelected() && !isBusy)
                return true;
            else if (radDisUn.isSelected() && isBusy)
                return true;
        }
        return false;
    }
    private boolean filterCheckSkillOrLang(List<String> modelList, List<String> filList, ObservableList<CheckBox> obList){
        for (int i = 0; i < obList.size(); i++) {
            CheckBox cb = obList.get(i);
            if (cb.isSelected())
                filList.add(cb.getText());
        }
        if (filList.isEmpty())
            return true;
        else 
            return modelList.containsAll(filList);
    }
    
    private void filterReset() {
        btnFilReset.setOnAction(event -> {
            List<ComboBox> cbbS = Arrays.asList(cbbAgeSign, cbbBusSign, cbbWaiSign, cbbHipSign);
            cbbS.stream().forEach(c -> {
                    c.setValue(">");
            });
            List<ComboBox> cbbV = Arrays.asList(cbbAgeVal1, cbbBusVal1, cbbWaiVal1, cbbHipVal1,
                    cbbAgeVal2, cbbBusVal2, cbbWaiVal2, cbbHipVal2);
            cbbV.stream().forEach(c -> {
                    c.setValue("");
            });
            cbbCity.setValue(rb.getString("selectCity"));
            radGenAll.setSelected(true);
            radDisAll.setSelected(true);
            List<ObservableList<CheckBox>> obList = Arrays.asList(filterSkillList, filterLangList);
            obList.stream().forEach(ob -> {
                for (int i = 0; i < ob.size(); i++) {
                CheckBox cb = ob.get(i);
                if (cb.isSelected())
                    cb.setSelected(false);
                }
            });
            modelTable.setItems(dataList);
        });
    }
    private void searchBarAction(){
        searchBar.textProperty().addListener((observable, oldValue, enteredValue) -> {
            foundData.setPredicate(model -> {
                if (enteredValue == null || enteredValue.isEmpty())
                    return true;
                String keyword = enteredValue.toLowerCase();
                String name = model.getName().toLowerCase();
                String id = String.valueOf(model.getId());
                if (name.contains(keyword))
                    return true;
                else if (id.contains(keyword))
                    return true;
                
                return false;
            });
            SortedList<Models> sortedData = new SortedList<>(foundData);// 3. Wrap the FilteredList in a SortedList.    
            sortedData.comparatorProperty().bind(modelTable.comparatorProperty());// 4. Bind the SortedList comparator to the TableView comparator.
            modelTable.setItems(sortedData);// 5. Add sorted (and filtered) data to the table.
        });
    }
    
    @FXML private void btnFilAction(){
        topModalAction(filModal);
    }
    @FXML public  void btnAddAction() throws SQLException{
        modelAdd.clearTxt();
        topModalAction(addModal);
    }
    @FXML private void btnEditAction() throws IOException, SQLException{
        editModel = modelTable.getSelectionModel().getSelectedItem();
        editRowIndex = modelTable.getSelectionModel().getSelectedIndex();
        if (editModel!=null){
            modelAdd.btnUpdate.setVisible(true);
            modelAdd.btnSave.setVisible(false);
            modelAdd.lblTitle.setText("EDIT MODEL "+editModel.getId());
            modelAdd.txtName.setText(editModel.getName());
            String gender = editModel.getGender().isSelected() ? "Female" : "Male";
            modelAdd.cboGender.getSelectionModel().select(gender);
            modelAdd.cboCity.getSelectionModel().select(editModel.getCity());
            modelAdd.txtDOB.setValue(editModel.getDob());

            String[] body = editModel.getBody().split("-");
            modelAdd.txtBody1.setText(body[0]);
            modelAdd.txtBody2.setText(body[1]);
            modelAdd.txtBody3.setText(body[2]);

            modelAdd.listSkill1  = FXCollections.observableArrayList();
            modelAdd.listSkill1.addAll(editModel.getSkillList());
            modelAdd.listSkill.removeAll(editModel.getSkillList());
            modelAdd.txaSkill.setText(editModel.getSkill());

            modelAdd.listLang1  = FXCollections.observableArrayList();
            modelAdd.listLang1.addAll(editModel.getLangAsList());
            modelAdd.listLang.removeAll(editModel.getLangAsList());
            modelAdd.txaLang.setText(editModel.getLang());

            modelAdd.listImage = new ArrayList<>();
            for (int i = 0; i < editModel.getAlbum().size(); i++) {
                modelAdd.listImage.add(new File(SRC_DIR+editModel.getAlbum().get(i)));
            }
            modelAdd.img = 0;
            if (modelAdd.listImage.isEmpty())
                modelAdd.ava.setImage(new Image("file:///"+SRC_DIR+"/mainpanel/images/no image.png"));
            else
                modelAdd.ava.setImage(new Image(modelAdd.listImage.get(modelAdd.img).toURI().toString()));
            modelAdd.btnImgDel.setVisible(true);
            modelAdd.btnImgNext.setVisible(false);

            if(modelAdd.listImage.size()==1){
                modelAdd.btnImgPrev.setVisible(false);
                modelAdd.btnImgDel.setVisible(true);
                modelAdd.btnImgNext.setVisible(false);
            } else {
                modelAdd.btnImgPrev.setVisible(false);
                modelAdd.btnImgDel.setVisible(true);
                modelAdd.btnImgNext.setVisible(true);
            }

            modelAdd.btnUpdate.setVisible(true);
            topModalAction(addModal);
        }
    }
    @FXML private void btnDelAction(){
        btnDelYes.setOnAction(event -> {
            int rowIndex = modelTable.getSelectionModel().getSelectedIndex();
            int modelID = dataList.get(rowIndex).getId();
            PreparedStatement st;
            try {
                st = con.prepareStatement("SELECT ImgURL FROM ModelImg" +
                    " WHERE ModelID = "+modelID);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    File file = new File(SRC_DIR+rs.getString("ImgURL"));
                    file.delete();
                }
                st = con.prepareStatement("DELETE FROM Model WHERE Model.ModelID = "+modelID);
                st.executeUpdate();
                st.close();
                dataList.remove(rowIndex);
                foundData = new FilteredList<>(dataList, p -> true);
                modelTable.refresh();
                topModalAction(delModal);
            } catch (SQLException e){e.printStackTrace();}
        });
        
        btnDelNo.setOnAction(event -> { topModalAction(delModal); });
        
        topModalAction(delModal);
    }
    @FXML private void backgroundClickAction(){
        if (addModal.isVisible())
            topModalAction(addModal);
        else if (filModal.isVisible())
            topModalAction(filModal);
        else if (delModal.isVisible())
            topModalAction(delModal);
        else if (modelDetailPanel.isVisible())
            modelDetailPane(modelDetailPanel);
    }
    private void topModalAction(Pane p){
        if (!topContainer.isVisible()){
            topContainer.setVisible(true);
            p.setVisible(true);
            topModalAni(p.layoutYProperty(), 0);
        } else {
            topModalAni(p.layoutYProperty(), -p.getHeight()).setOnFinished((ActionEvent event) -> {
                topContainer.setVisible(false);
                p.setVisible(false);
            });
        }
    }
    private Timeline topModalAni(DoubleProperty node, double pos){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        final KeyValue kv = new KeyValue(node, pos);
        final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
        return timeline;
    }
    
    void langVI(){
        currentLocale = new Locale("vi", "VI");
        rb = ResourceBundle.getBundle("Localization/Aphrodite_vi_VN", currentLocale);
        setLanguage();
    }
    void langEN(){
        currentLocale = new Locale("en", "US");
        rb = ResourceBundle.getBundle("Localization/Aphrodite_en_US", currentLocale);
        setLanguage();
    }
    
    @FXML private Button btnFil, btnAdd;
    @FXML private Label lbAge, lbCity, lbGen, lbDisplay, lbSkill, lbLang, lbBus, lbWai, lbHip, lbComfirmDel;
    private void setLanguage() {
        searchBar.setPromptText(rb.getString("searchModel"));
        btnFil.setText(rb.getString("filter"));
        btnAdd.setText(rb.getString("add"));
        btnEdit.setText(rb.getString("edit"));
        btnDel.setText(rb.getString("delete"));
        
        tabName.setText(rb.getString("name"));
        tabAge.setText(rb.getString("age"));
        tabLoca.setText(rb.getString("city"));
        tabSkill.setText(rb.getString("skill"));
        tabLang.setText(rb.getString("language"));
        tabBody.setText(rb.getString("body"));
        tabGender.setText(rb.getString("female"));
        //lblAgeTo, lblBusTo, lblWaiTo, lblHipTo
        lbAge.setText(rb.getString("age"));
        lbCity.setText(rb.getString("city"));
        lbGen.setText(rb.getString("gender"));
        lbDisplay.setText(rb.getString("display"));
        lbSkill.setText(rb.getString("skill"));
        lbLang.setText(rb.getString("language"));
        lbBus.setText(rb.getString("bust"));
        lbWai.setText(rb.getString("waist"));
        lbHip.setText(rb.getString("hip"));
        lblAgeTo.setText(rb.getString("to"));
        lblBusTo.setText(rb.getString("to"));
        lblWaiTo.setText(rb.getString("to"));
        lblHipTo.setText(rb.getString("to"));
        radGenAll.setText(rb.getString("all"));
        radGenMal.setText(rb.getString("male"));
        radGenFem.setText(rb.getString("female"));
        radDisAll.setText(rb.getString("all"));
        radDisAv.setText(rb.getString("avai"));
        radDisUn.setText(rb.getString("unavai"));
        btnFilDone.setText(rb.getString("done"));
        btnFilReset.setText(rb.getString("reset"));
        btnFilCancel.setText(rb.getString("cancel"));
        
        lbComfirmDel.setText(rb.getString("confirmDelModel"));
        btnDelYes.setText(rb.getString("yes"));
        btnDelNo.setText(rb.getString("no"));
        
        colDCName.setText(rb.getString("conName"));
        colDCStart.setText(rb.getString("startDate"));
        colDCEnd.setText(rb.getString("endDate"));
    }
}
