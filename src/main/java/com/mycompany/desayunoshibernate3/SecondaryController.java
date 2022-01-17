/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.desayunoshibernate3;

import com.mycompany.desayunoshibernate3.models.Pedido;
import com.mycompany.desayunoshibernate3.models.Producto;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;
import org.hibernate.Session;
import org.hibernate.query.Query;
/**
 * FXML Controller class
 *
 * @author hierr
 */
public class SecondaryController implements Initializable {


    @FXML
    private TableView<Producto> tabla;
    @FXML
    private TableColumn<Producto, Long> cID;
    @FXML
    private TableColumn<Producto, String> cNombre;
    @FXML
    private TableColumn<Producto, Double> cPrecio;
    @FXML
    private TableColumn<Producto, String> cDescripcion;
    @FXML
    private Button cVolver;
    /**
     * Initializes the controller class.
     */
    Session s = HibernateUtil.getSessionFactory().openSession();
    @FXML
    private Button btnInformeProductos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Stage stage= new Stage();
        stage.setFullScreen(true);
        ObservableList<Producto> carta = FXCollections.observableArrayList();
        cID.setCellValueFactory(new PropertyValueFactory<>("id"));
        cNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        cDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
       
        
        Query q=s.createQuery("From Producto");

        System.out.println(q.list().toString());
        carta.addAll(q.list());
        tabla.setItems(carta);
    }    
    
    @FXML
    private void clicTabla(MouseEvent event) {
    }

    @FXML
    private void volver(ActionEvent event) {
        try {
                App.setRoot("primary");
                
            } catch (IOException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    private void clicInformeProductos(ActionEvent event) {
                    String archivo = "Carta.jrxml";

        try {
            var parameters = new HashMap();
            parameters.put("Titulo", "Carta de productos");

            JasperReport informe = JasperCompileManager.compileReport(archivo);
            JasperPrint impresion = JasperFillManager.fillReport(informe, parameters, Conexion.getCon());

            JRViewer visor = new JRViewer(impresion);

            JFrame ventanaInforme = new JFrame("Carta");
            ventanaInforme.getContentPane().add(visor);
            ventanaInforme.pack();
            ventanaInforme.setVisible(true);

            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput( new SimpleExporterInput(impresion) );
            exportador.setExporterOutput( new SimpleOutputStreamExporterOutput("Carta.pdf") );

            var configuracion = new SimplePdfExporterConfiguration();
            exportador.setConfiguration(configuracion);

            exportador.exportReport();

        } catch (JRException ex) {
            System.out.println(ex);
        }
    }

}
