/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contract;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

/**
 *
 * @author ENS
 */
public class ContractModels {
    String name, city, body;
    List<String> skill, lang;
    CheckBox gender;
    int age, id;
    ImageView thumb;

    public ContractModels(int id, ImageView thumb, String name, CheckBox gender, int age, String body, String city, List<String> skill, List<String> lang) {
        this.id = id;
        this.thumb = thumb;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.body = body;
        this.city = city;
        this.skill = skill;
        this.lang = lang;
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

    public int getAge() {
        return age;
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

    public ImageView getThumb() {
        return thumb;
    }

    public void setThumb(ImageView thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
