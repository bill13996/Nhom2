/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kiemtraj2;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author XPS14
 */
public class KiemTraJ2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LaptopManager obj = new LaptopManager();
        Scanner sc;
        int opt;
        while (true){
            System.out.print(
                "\n**************************************\n" +
                "  1. New laptop.\n" +
                "  2. Display all laptops.\n" +
                "  3. Exit.\n" +
                "**************************************\n"
            );
            System.out.println("DIT ME GIT HAY VCL LUON!");
            System.out.println("Fuck afsocmk asdfakfsd");
            while(true){
                try {
                    System.out.print("Nhap: ");
                    sc = new Scanner(System.in);
                    opt = sc.nextInt();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.println("\t- Nhap lai.");
                }
            }
            switch (opt){
                case 1:
                    obj.add();
                    break;
                case 2: 
                    obj.displayAll();
                    break;
                case 3:
                    obj.exitSave();
                    System.exit(0);
                    break;
                default:
                    System.out.println("\t- Nhap lai.");
            }
        }
    }

}
