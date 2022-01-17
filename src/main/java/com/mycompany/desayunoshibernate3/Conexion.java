/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.desayunoshibernate3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hierr
 */
public class Conexion {
     private static Connection conexion;
    static{
        String url="jdbc:mysql://localhost:3305/desayunos?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user="root";
        String pass="";
        /*String url="jdbc:postgresql://localhost:5432/odoo";
        String user="openpg";
        String pass="openpgpwd";*/
        
        try {
            conexion=DriverManager.getConnection(url,user,pass);
            System.out.println("Conexion realizada con exito");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConexion() {
        return conexion;
    }

    static Connection getCon() {
       return conexion;
    }
}
