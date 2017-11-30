/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpanel.methods;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author ENS
 */
public class Languages {
    Image img;
    ImageView thumb;
    String name;

    public Languages() {}
    public Languages(String u, String name) {
        this.img = new Image(u, 20, 20, true, true);
        this.thumb = new ImageView(img);
        this.name = name;
    }
    
    public Languages getLanguage(String l) {
        Languages obj = new Languages();
        switch (l){
            case "vi": obj = new Languages("/mainpanel/images/vi1.png", "Việt Nam");
                break;
            case "en": obj = new Languages("/mainpanel/images/uk1.png", "Việt Nam");
                break;
        };
        return obj;
    }
    

    public ImageView getThumb() {
        return thumb;
    }

    public void setThumb(ImageView thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
