package com.matrix.focus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

public class JasReport{
  private JRResultSetDataSource jrDataSource;
  private net.sf.jasperreports.engine.JasperReport jasperReport;
  private JasperPrint jasperPrint;
  private JRViewer jrView;  
  //private JRLoader jrLoader;
 
  public JasReport(String xmlFile,ResultSet rs,HashMap parameters){
    try{ 
      jrDataSource = new JRResultSetDataSource(rs);
      //jrLoader = new JRLoader();
      jasperReport = JasperCompileManager.compileReport(new FileInputStream(new File(xmlFile)));
      jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,jrDataSource);
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
  
  public JasReport(String xmlFile, HashMap parameters, Connection connection){
        try {
            jasperReport = JasperCompileManager.compileReport(new FileInputStream(new File(xmlFile)));
            jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,connection);
        } 
        catch(Exception e){
            e.printStackTrace();
        }
    }
  
  public JPanel getPrintView(){
    jrView = new JRViewer(jasperPrint);
    return jrView;
  }
  
  public boolean writeToPDF(File pdfFile) {
        boolean success = false;
        try{
            FileOutputStream pdfFileStream = new FileOutputStream(pdfFile);
            JasperExportManager.exportReportToPdfStream(jasperPrint,pdfFileStream);
            pdfFileStream.close();
            success = true;
        } 
        catch(Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
  }
  
}
