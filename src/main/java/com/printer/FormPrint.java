package com.printer;

import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class FormPrint extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JComboBox<String> cmbPrinters;
    private JTextField txtFilePath;
    private JButton btPrint;
    public FormPrint() {
        initComponents();

    }
    
    private void initComponents() {

        this.setTitle("Impresión Factura Electrónica desde XML");
        JLabel lbPathFile = new JLabel("Ingrese la ruta del archivo XML:");
        JLabel lbPrinter = new JLabel("Seleccione la impresora:");
        JLabel lbCopy = new JLabel("Aplicaión XML to Printer DEMOSTRACIÓN");
        JLabel lbCopy2 = new JLabel("Desarrollado por: lahernandezr@outlook.com");
        
        txtFilePath = new JTextField("");
        btPrint = new JButton("Imprimir");
        cmbPrinters = new JComboBox<String>();

        txtFilePath.setText("C:\\Proacit\\formato2.xml");

        lbPathFile.setBounds(50,50,200,30);
        this.add(lbPathFile);
              

        txtFilePath.setBounds(50,80,200,30);
        this.add(txtFilePath);
             
        
        lbPrinter.setBounds(50,120,200,30);
        this.add(lbPrinter);

        lbCopy.setBounds(10,270,400,100);
        this.add(lbCopy);
        lbCopy2.setBounds(10,290,400,100);
        this.add(lbCopy2);
        cmbPrinters.removeAllItems();
        cmbPrinters.addItem("[ Predeterminada ]");
        List<String> impresoras = UtilidadesImpresora.listarImpresoras();
        for (String nombreImpresora : impresoras) {
            cmbPrinters.addItem(nombreImpresora);
        }

        cmbPrinters.setBounds(50,160,200,30);
        this.add(cmbPrinters);
        

        btPrint.setBounds(50,200,85,30);
        this.add(btPrint);
        this.setSize(300,400);
        this.setLayout(null);
        this.setVisible(true);
        this.setResizable(false);
        btPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPrintActionPerformed(evt);
            }
        });

    }

    private void btPrintActionPerformed(java.awt.event.ActionEvent evt) {
        String rutaPdf = txtFilePath.getText().trim();
         String nombreImpresora = null;
         if (cmbPrinters.getSelectedIndex() > 0)
             nombreImpresora = cmbPrinters.getItemAt(cmbPrinters.getSelectedIndex());

        
         File f = new File(rutaPdf);
         if(f.exists() && f.isFile()) { 

            if(nombreImpresora == null){
                JOptionPane.showMessageDialog(this, "Seleccione la impresora", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Formater format = new Formater();
            PrinterXML printerXML = new PrinterXML();             
            App.printThermaldoc(rutaPdf,nombreImpresora, format,printerXML, "Q");
            JOptionPane.showMessageDialog(this, "Imprimiendo archivo en "+nombreImpresora, "Ok", JOptionPane.INFORMATION_MESSAGE);
         }
         else {
             JOptionPane.showMessageDialog(this, "El archivo indicado no existe.", "Error", JOptionPane.WARNING_MESSAGE);
         }
     }
}
