/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiemtraj2;

import java.util.Scanner;

/**
 *
 * @author XPS14
 */
public class Laptop {
    String code, manufacture, description;
    Scanner sc;
    public Laptop() {
        sc = new Scanner(System.in);
        System.out.print("Nhap code: ");
        this.code = sc.nextLine();
        System.out.print("Nhap manufacture: ");
        this.manufacture = sc.nextLine();
        System.out.print("Nhap description: ");
        this.description = sc.nextLine();
    }

    public String display() {
        return "Laptop - Code:"+code+" \tManufacture:"+manufacture+" \tDescription"+description;
    }
    
    //getters setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
