/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.desayunoshibernate3;

import com.mycompany.desayunoshibernate3.models.Pedido;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.swing.JFrame;

/**
 * FXML Controller class
 *
 * @author hierr
 */
public class PrimaryController implements Initializable {

    @FXML
    private Label txtFechaHora;
    @FXML
    private Label pedidosPendientes;
    @FXML
    private Button btnVerCarta;
    @FXML
    private TableView<Pedido> tabla;
    @FXML
    private TableColumn<Pedido, Long> cId;
    @FXML
    private TableColumn<Pedido, String> cIdProducto;
    @FXML
    private TableColumn<Pedido, String> cFecha;
    @FXML
    private TableColumn<Pedido, String> cEstado;
    /**
     * Initializes the controller class.
     */
    Session s = HibernateUtil.getSessionFactory().openSession();
    @FXML
    private Button btnInformeComandas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Pedido> pedidos = FXCollections.observableArrayList();
        
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cIdProducto.setCellValueFactory(
                (var row) -> {
                    String salida = (row.getValue().getProducto().getNombre());
                    return new SimpleStringProperty(salida);
                }
        );
        cFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        cEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        Query q = s.createQuery("From Pedido");

        System.out.println(q.list().toString());
        pedidos.addAll(q.list());
        tabla.setItems(pedidos);
               

        Query q2 = s.createQuery("Select count(p) from Pedido p where p.estado=:cadenaEstado and p.fecha=current_date()");
        q2.setParameter("cadenaEstado", "En espera");
        String cantidad = q2.list().toString();
        pedidosPendientes.setText("Quedan " + cantidad + " pedidos pendientes de hoy");

        Date fecha = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        txtFechaHora.setText(formatter.format(fecha));
        System.out.println(formatter.format(fecha));

    }

    @FXML
    private void VerCarta(ActionEvent event) {
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicTabla(MouseEvent event) {
        Pedido p = tabla.getSelectionModel().getSelectedItem();
        p.setEstado("Entregado");
        Transaction tr = s.beginTransaction();
        s.save(p);
        tr.commit();
    }

    @FXML
    private void clicInformeComandas(ActionEvent event) {
            String archivo = "Pedidos.jrxml";

        try {
            var parameters = new HashMap();
            parameters.put("Titulo", "Pedidos");

            JasperReport informe = JasperCompileManager.compileReport(archivo);
            JasperPrint impresion = JasperFillManager.fillReport(informe, parameters, Conexion.getCon());

            JRViewer visor = new JRViewer(impresion);

            JFrame ventanaInforme = new JFrame("Pedido");
            ventanaInforme.getContentPane().add(visor);
            ventanaInforme.pack();
            ventanaInforme.setVisible(true);

            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput( new SimpleExporterInput(impresion) );
            exportador.setExporterOutput( new SimpleOutputStreamExporterOutput("Pedido.pdf") );

            var configuracion = new SimplePdfExporterConfiguration();
            exportador.setConfiguration(configuracion);

            exportador.exportReport();

        } catch (JRException ex) {
            System.out.println(ex);
        }
    }
}


