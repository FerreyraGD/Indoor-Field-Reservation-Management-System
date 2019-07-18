/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseproject;

import java.awt.Point;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author s0damachine
 */
public class DatabaseProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      //ViewReservation vr = new ViewReservation();
      //vr.setVisible(true);
      
      //ViewOpenings vo = new ViewOpenings();
      //vo.setVisible(true);
      
        ViewCustomer vc = new ViewCustomer();
        vc.setVisible(true);
    }
    
}
