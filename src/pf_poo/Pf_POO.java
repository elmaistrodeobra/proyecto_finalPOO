/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pf_POO;

import pkg_controlador.hospitalControl;
import pkg_vista.vistaMenu;

/**
 *
 * @author uli08
 */
public class Pf_POO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       vistaMenu menu = new vistaMenu();
        new hospitalControl(menu);
        menu.setVisible(true);
    }
    
}
