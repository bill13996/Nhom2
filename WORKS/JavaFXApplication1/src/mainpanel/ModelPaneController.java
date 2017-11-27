package mainpanel;

import DBConnection.DBConnection;
import java.io.IOException;
import mainpanel.methods.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import modeladdedit.ModelAddController;

public class ModelPaneController extends AnchorPane{
    @FXML private TableView<Models> modelTable;
    @FXML private TableColumn<Models, ImageView> tabThumb;
    @FXML private TableColumn<Models, CheckBox> tabGender;
    @FXML private TableColumn<Models, String> tabName, tabAge, tabLoca, tabSkill, tabLang, tabID, tabBody;
    @FXML private Button btnEdit, btnDel, btnDelNo, btnDelYes, btnFilDone, btnFilReset, btnFilCancel, btnDetailNext, btnDetailPrev;
    @FXML private Label lblAgeTo, lblBusTo, lblWaiTo, lblHipTo;
    @FXML private ComboBox cbbAgeSign, cbbBusSign, cbbWaiSign, cbbHipSign, cbbAgeVal1, cbbBusVal1, cbbWaiVal1, cbbHipVal1, 
            cbbAgeVal2, cbbBusVal2, cbbWaiVal2, cbbHipVal2, cbbCity;
    @FXML private TextField searchBar;
    @FXML private RadioButton radGenAll, radGenMal, radGenFem, radDisAll, radDisAv, radDisUn;
    @FXML public Pane addModal, delModal, filModal;
    @FXML private FlowPane skillPane, langPane;
    @FXML private AnchorPane topContainer, containerMain;
    @FXML private StackPane avatarsPane;
    //@FXML private Pagination pagination;
    
    ObservableList<Models> dataList;
    ObservableList<CheckBox> filterSkillList, filterLangList;
    List<String> selectedSkill, selectedLang;
    List<Integer> modelOnContract;
    Connection con = DBConnection.getConnect();
    ModelAddController modelAdd;
    private static final Pattern P_BODY = Pattern.compile("^(b(\\d+))(w(\\d+))(h(\\d+))");
    //private final static int rowsPerPage = 20;
    
    public ModelPaneController() throws IOException, SQLException, InterruptedException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainpanel/ModelPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
            
        btnDel.setDisable(true);
        btnEdit.setDisable(true);
        addModal.setLayoutY(-500);
        delModal.setLayoutY(-100);
        filModal.setLayoutY(-250);
        
        getModelOnContractList();
        setModelTable();
        filterSetup();
        setAddModal(fxmlLoader.getController());
        showModelDetail();
        filterAction();
        filterReset();
        searchBarAction();
    }
    
    private void showModelDetail(){
        modelTable.setOnMousePressed((MouseEvent me) -> {
            Models model = modelTable.getSelectionModel().getSelectedItem();
            if (me.getClickCount() == 2 && model != null){
                try {
                    setModelDetail(model.getId());
                } catch (SQLException ex) {}
            }
        });
    }
    private void setModelDetail(int id) throws SQLException {
        PreparedStatement st = con.prepareStatement("SELECT ImgURL FROM ModelImg" +
                " WHERE ModelID = "+id, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery();
        avatarsPane.getChildren().clear();
        ImageView img = new ImageView();
        rs.afterLast();
        while (rs.previous()){
            img = new ImageView(new Image(rs.getString("ImgURL"), 250, 250, true, true));
            img.setVisible(false);
            avatarsPane.getChildren().add(img);
        }
        st.close();
        img.setVisible(true);
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
    
    public void setModelTable() throws SQLException, InterruptedException{
        PreparedStatement st = con.prepareStatement("SELECT Model.*, City.City FROM Model LEFT JOIN City" +
            " ON Model.CityID = City.CityID");
        ResultSet rs = st.executeQuery();
    //Add models to row list
        dataList = FXCollections.observableArrayList();
        
        while (rs.next()){
            int modelID = rs.getInt("ModelID"), age;
            String name, city, body, imgURL="/mainpanel/images/default.jpg";
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
                imgURL = rs2.getString("ImgURL");

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
            thumb = new ImageView(new Image(imgURL, 100, 50, true, true));
            gender.setSelected(rs.getBoolean("female"));
            body = rs.getString("Body")!=null ? P_BODY.matcher(rs.getString("Body")).replaceAll("$2-$4-$6") : "" ;
            //age = rs.getString("DoB")!=null ? sqlDateCal(rs.getString("DoB")).getYears() : 0 ;
            age = rs.getDate("DoB")!=null ? Period.between(rs.getDate("DoB").toLocalDate(), LocalDate.now()).getYears() : 0 ;
            city = rs.getString("City")!=null ? rs.getString("City") : "" ;
            name = rs.getString("ModelName")!=null ? rs.getString("ModelName") : "" ;

            dataList.add(new Models(modelID, thumb, name, gender, age, body, city, skill, lang));
        }
        st.close();
    //Add row list to table
        modelTable.setItems(dataList);
    //Set opacity for busy model    
        modelTable.setRowFactory(tv -> new TableRow<Models>() {
            @Override public void updateItem(Models model, boolean empty) {
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
    @FXML private void tableCellClicked(){
        if (modelTable.getSelectionModel().getSelectedItem() != null){
            btnDel.setDisable(false);
            btnEdit.setDisable(false);
        }
    }
    
    public void setAddModal(ModelPaneController controller) throws IOException, SQLException {
        modelAdd = new ModelAddController(controller);
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
            cbbCity.getItems().add(0, "All");
            cbbCity.setValue("Select a city");
            
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
        if (city.equals("Select a city") || city.equals("All"))
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
            cbbCity.setValue("Select a city");
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
        FilteredList<Models> foundData = new FilteredList<>(dataList, p -> true);
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
    @FXML public void btnAddAction(){
        topModalAction(addModal);
    }
    @FXML void btnEditAction(){
    }
    @FXML void btnDelAction(){
        btnDelYes.setOnAction(event -> {
            int rowIndex = modelTable.getSelectionModel().getSelectedIndex();
            int modelID = dataList.get(rowIndex).getId();
            try {
                PreparedStatement st = con.prepareStatement("DELETE FROM Model WHERE Model.ModelID = "+modelID);
                st.executeUpdate();
                dataList.remove(rowIndex);
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
    }
    private void topModalAction(Pane p){
        if (!topContainer.isVisible()){
            topContainer.setVisible(true);
            p.setVisible(true);
            topModalAni(p, 0);
        } else {
            topModalAni(p, -p.getHeight()).setOnFinished((ActionEvent event) -> {
                topContainer.setVisible(false);
                p.setVisible(false);
            });
        }
    }
    private Timeline topModalAni(Pane p, double y){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        final KeyValue kv = new KeyValue(p.layoutYProperty(), y);
        final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
        return timeline;
    }
    
    /*
    private AnchorPane dataPerPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, dataList.size());
        modelTable.setItems(FXCollections.observableArrayList(dataList.subList(rowsPerPage, rowsPerPage)));
        return containerMain;
    }
    */
}
