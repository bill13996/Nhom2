/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiemtraj2;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author XPS14
 */
public class LaptopManager {
    TreeMap<String, Laptop> listLaptop;
    Laptop laptop;
    Set keySet;
    String key;
    Iterator ite;
    public LaptopManager() {
        listLaptop = new TreeMap<>();
    }
    
    public void add() {
        laptop = new Laptop();
        listLaptop.put(laptop.getCode(), laptop);
        System.out.println("Da them laptop");
    }
    
    public void displayAll() {
        boolean notfound = true;
        keySet=listLaptop.keySet();
        ite=keySet.iterator();
        while (ite.hasNext()) {
            System.out.println(ite.hasNext()+"\t");
            key = (String)ite.next();
            System.out.println(listLaptop.get(key).display());
            if (ite.hasNext())
                notfound = false;
        }
        if (notfound)
            System.out.println("Ko tim thay.");
    }
    
    public void exitSave() {
        keySet=listLaptop.keySet();
        ite=keySet.iterator();
        try {
            FileOutputStream fos = new FileOutputStream("Laptops.txt");
            DataOutputStream dos = new DataOutputStream(fos);
            while (ite.hasNext()) {
                dos.writeUTF(listLaptop.get(key).display()+"\r\n");
            }
            System.out.println("Da save file.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
