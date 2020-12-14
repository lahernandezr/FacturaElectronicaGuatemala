package com.printer;

import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterResolution;

import java.awt.print.PageFormat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.AlphaComposite;


/**
    Libreria para creacion de recursos
    
    @version 1
    @author Luis Angel Hernandez Rodriguez
    
    Correo: lahernandezr@outlook.com
*/
public class TestPrint  {

    /**
     * Funcion que imprime una imagen
     * 
     * @param image la ruta de la imagen 
     * @param impresora el servicio de impresion
     */
    public static void qrcode(String image, PrintService impresora) throws Exception {

        PageFormat format = new PageFormat();
        format.setOrientation(PageFormat.REVERSE_LANDSCAPE);
      
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        pras.add(OrientationRequested.PORTRAIT);
        pras.add(MediaSizeName.ISO_A7);        
        pras.add(new PrinterResolution(100, 100, PrinterResolution.DPI));       
        pras.add(new MediaPrintableArea(0, 0, 80, 50, MediaPrintableArea.MM));        
        System.out.println("Printing to " + impresora);
        DocPrintJob job = impresora.createPrintJob();
        FileInputStream fin = new FileInputStream(image);
        Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.PNG, null);                
        job.print(doc, pras);
        //job.print(doc, pras);

        fin.close();
      }

    /**
     * Funcion crea una imagen QRCODE
     * 
     * @param qrFile la ruta del archivo donde se guardara la imagen
     * @param qrCodeText texto que guardara el codigo qr
     * @param size tamaño del codigo qr
     * @param fileType tipo de archivo a generar (PNG, JPG)
     */      
      public static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
			throws WriterException, IOException {
          // Create the ByteMatrix for the QR-Code that encodes the given String
          Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
          hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
          QRCodeWriter qrCodeWriter = new QRCodeWriter();
          BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
          // Make the BufferedImage that are to hold the QRCode
          int matrixWidth = byteMatrix.getWidth();
          BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
          image.createGraphics();

          Graphics2D graphics = (Graphics2D) image.getGraphics();
          graphics.setColor(Color.WHITE);
          graphics.fillRect(0, 0, matrixWidth, matrixWidth);
          // Paint and save the image using the ByteMatrix
          graphics.setColor(Color.BLACK);

          for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
              if (byteMatrix.get(i, j)) {
                graphics.fillRect(i, j, 1, 1);
              }
            }
          }
          ImageIO.write(image, fileType, qrFile);
        }
        
    /**
     * Funcion crea una imagen QRCODE
     * 
     * @param path1 la ruta del archivo de la imagen1 a unir
     * @param path2 la ruta del archivo de la imagen2 a unir
     * @param output ruta de salida de la imagen mezclada
     
     */  
        public static void merge(String path1, String path2, String output){    
          try {          
            File background = new File(path1);                            
            BufferedImage im = ImageIO.read(background);
            File qrcode = new File(path2);                                        
            BufferedImage im2 = ImageIO.read(qrcode);
            Graphics2D g = im.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g.drawImage(im2, 146, 19, null);
            g.dispose();              
          
              ImageIO.write(im, "png", new File(output));
            } catch (IOException e) {
              e.printStackTrace();
            }
        }
}