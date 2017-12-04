/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpanel.methods;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static mainpanel.ModelPaneController.SRC_DIR;

/**
 *
 * @author ENS
 */
public class Models {
    String name, city, body;
    List<String> skill, lang, album;
    CheckBox gender;
    int age, id;
    LocalDate dob;

    public Models(int id, List<String> album, String name, CheckBox gender, LocalDate dob, String body, String city, List<String> skill, List<String> lang) {
        this.id = id;
        this.album = album;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.body = body;
        this.city = city;
        this.skill = skill;
        this.lang = lang;
    }

    public List<String> getAlbum() {
        return album;
    }

    public void setAlbum(List<String> album) {
        this.album = album;
    }

    public ImageView getThumb() {
        String url = !album.isEmpty() ? "file:///"+SRC_DIR+album.get(0) : "/mainpanel/images/default.png" ;
        return new ImageView(new Image(url, 100, 50, true, true));
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLang() {
        return lang.stream().collect(Collectors.joining(", "));
    }
    
    public List<String> getLangAsList() {
        return lang;
    }

    public void setLang(List<String> lang) {
        this.lang = lang;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSkill() {
        return skill.stream().collect(Collectors.joining(", "));
    } 
    
    public List<String> getSkillList() {
        return skill;
    }

    public void setSkill(List<String> skill) {
        this.skill = skill;
    }

    public CheckBox getGender() {
        return gender;
    }

    public void setGender(CheckBox gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    
    public int getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
