package com.printer;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.print.PrintService;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;

import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;


import com.github.anastaciocintra.output.PrinterOutputStream;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static String symbol = "Q";

    public static void printThermaldoc(String filePath, String printerName, Formater formater, PrinterXML printerXML,
            String symbol_currency) {

        if (filePath.equals("")) {
            System.out.println("Ingrese la ruta del archivo.");
            return;
        }
        if (printerName.equals("")) {
            System.out.println("Ingrese la impresora.");
            return;
        }

        Document doc = printerXML.paserXMLtoPrinter(filePath);

        // Se delclaran los estilos impresion;
        Style titleBold = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1).setBold(true)
                .setJustification(EscPosConst.Justification.Center);

        Style titleNormal = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setJustification(EscPosConst.Justification.Center);
        /*
        Style title3 = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1).setUnderline(Underline.OneDotThick)
                .setJustification(EscPosConst.Justification.Left_Default);
        Style title4 = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1);
        */
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        try {
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream);

            escpos.setCharacterCodeTable(CharacterCodeTable.ISO8859_2_Latin2); // Caracteres especiales en español            
            // Cabeceza
            List<String> emisonNombreComercialList = formater
                    .rowsString(printerXML.searchData(doc, "emisor-nombre-comercial").toUpperCase(), 33);
            for (String row : emisonNombreComercialList)
                escpos.writeLF(titleBold, row);

            List<String> emisorNombreList = formater
                    .rowsString(printerXML.searchData(doc, "emisor-nombre").toUpperCase(), 33);
            for (String row : emisorNombreList)
                escpos.writeLF(titleNormal, row);

            List<String> emisorDireccionList = formater
                    .rowsString(printerXML.searchData(doc, "emisor-direccion").toUpperCase(), 33);
            for (String row : emisorDireccionList)
                escpos.writeLF(titleNormal, row);
            escpos.feed(1);

            List<String> emisorMunicipioList = formater
                    .rowsString(printerXML.searchData(doc, "emisor-municipio").toUpperCase() + " "
                            + printerXML.searchData(doc, "emisor-departamento").toUpperCase(), 33);
            for (String row : emisorMunicipioList)
                escpos.writeLF(titleNormal, row);
            escpos.writeLF(titleNormal, "NIT: " + printerXML.searchData(doc, "emisor-nit"));
            escpos.feed(1);
            escpos.writeLF(titleBold, "Factura Electrónica");
            escpos.writeLF(titleNormal, "Autorización");

            escpos.writeLF(titleNormal, printerXML.searchData(doc, "numero-autorizacion"));
            escpos.writeLF(titleNormal, "Fecha de certificación:");
            escpos.writeLF(titleNormal, printerXML.searchData(doc, "fecha-hora-certificacion"));
            escpos.writeLF("SERIE: " + printerXML.searchData(doc, "serie"));
            escpos.writeLF("NO.:   " + printerXML.searchData(doc, "numero"));
            escpos.writeLF(titleNormal, "- - - DATOS DEL COMPRADOR - - -");
            escpos.writeLF("FECHA: " + printerXML.searchData(doc, "fecha-emision"));
            escpos.writeLF("NIT:   " + printerXML.searchData(doc, "receptor-nit"));

            List<String> nameCustomer = formater.rowsString(printerXML.searchData(doc, "receptor-nombre"), 33);
            escpos.writeLF("NOMBRE: ");
            for (int i = 1; i < nameCustomer.size(); i++) {
                escpos.writeLF(nameCustomer.get(i));
            }            
            //escpos.writeLF(titleNormal, printerXML.searchData(doc, "receptor-nombre"));


            escpos.writeLF("Dirección: " + printerXML.searchData(doc, "receptor-direccion"));
            // Finaliza cabecera
            escpos.feed(1);

            // Detalle d
            escpos.writeLF("UNI     DESCRIP   PRECIO U. TOTAL");
            escpos.writeLF(titleNormal, "---------------------------------");
            NodeList listDetalles = printerXML.searchList(doc, "detalles"); // Se obtienen todos los nodos de detalles.
            for (int temp = 0; temp < listDetalles.getLength(); temp++) {
                Node nNode = listDetalles.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    List<String> nameProduct = formater
                            .rowsString(eElement.getElementsByTagName("descripcion").item(0).getTextContent(), 10);
                    // Se imprime el detalle
                    escpos.writeLF(formater
                            .rightPad(formater.stringToCurrency(
                                    eElement.getElementsByTagName("cantidad").item(0).getTextContent(), ""), 8)
                            + formater.rightPad(nameProduct.get(0), 10)
                            + formater.leftPad(formater.stringToCurrency(
                                    eElement.getElementsByTagName("precio-unitario").item(0).getTextContent(), ""), 7)
                            + " " + formater.leftPad(formater.stringToCurrency(
                                    eElement.getElementsByTagName("total").item(0).getTextContent(), ""), 7));

                    // Se imprime renglones sobrantes del nombre
                    for (int i = 1; i < nameProduct.size(); i++) {
                        escpos.writeLF(formater.rightPad(" ", 8) + formater.rightPad(nameProduct.get(i), 10));
                    }
                }

            }

            escpos.writeLF(titleNormal, "---------------------------------");
            escpos.writeLF(titleNormal,
                    formater.leftPad("TOTAL" + formater.leftPad(
                            formater.stringToCurrency(printerXML.searchData(doc, "total-numeros"), symbol_currency),
                            15), 33));
            escpos.writeLF("En letras:");
            escpos.writeLF(printerXML.searchData(doc, "total-letras-round").toUpperCase());
            escpos.feed(1);
            escpos.writeLF("Vendedor:" + printerXML.searchData(doc, "vendedor").toUpperCase());
            escpos.writeLF("Observaciones:" + printerXML.searchData(doc, "vendedor").toUpperCase());
            escpos.writeLF(printerXML.searchData(doc, "observaciones"));
            escpos.feed(1);
            escpos.writeLF("Hora:" + printerXML.searchData(doc, "hora").toUpperCase());
            escpos.writeLF("No.:" + printerXML.searchData(doc, "prefno").toUpperCase());
            escpos.writeLF("Tipo de Venta:" + printerXML.searchData(doc, "documento").toUpperCase());
            escpos.feed(1);

            // Pie de Pagina
            escpos.writeLF(titleNormal, "Para cualquier cambio debe");
            escpos.writeLF(titleNormal, "presentar su factura dentro ");
            escpos.writeLF(titleNormal, "de los tres días a partir de");
            escpos.writeLF(titleNormal, "la fecha de compra, productos");
            escpos.writeLF(titleNormal, "en liquidación no tienen cambio.");
            escpos.feed(1);
            escpos.writeLF(titleBold, "Gracias por su compra, esperamos");
            escpos.writeLF(titleBold, "que vuelva pronto, estamos para ");
            escpos.writeLF(titleBold, "servirle.");
            escpos.feed(1);
            escpos.writeLF(titleNormal, "¨SUJETO A PAGOS TRIMESTRALES¨");
            escpos.feed(1);
            escpos.writeLF(titleNormal,
                    "CERTIFICADOR:" + printerXML.searchData(doc, "nombre-certificador").toUpperCase() + "NIT:"
                            + printerXML.searchData(doc, "nit").toUpperCase());
            escpos.writeLF(titleNormal, "Documento Tributario Electrónico");
            escpos.close();

            

            String qrCodeText = "https://report.feel.com.gt/ingfacereport/ingfacereport_documento?uuid="
                    + printerXML.searchData(doc, "numero-autorizacion") + "&formato=pdf&tipo_operacion=CERTIFICACION";
            String filePathQr = "c:\\Proacit\\qrcode2.png";
            int size = 300;
            String fileType = "png";
            File qrFile = new File(filePathQr);            
            TestPrint.createQRImage(qrFile, qrCodeText, size, fileType);  // crea el codigo QR
            TestPrint.merge("c:\\Proacit\\escanea.png", "c:\\Proacit\\qrcode2.png", "c:\\Proacit\\impresion.png"); // mezcla las imagenes para impresion final
            
            TestPrint.qrcode("c:\\Proacit\\impresion.png", printService); // imprime la imagen mezclada
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Formater formater = new Formater();
        formater.rowsString("TUBO PVC 1/2 315", 33);
        formater.rowsString("inversciones denche sociedad anonima", 33);

        //FormPrint formPrint = new FormPrint();
        //formPrint.isShowing();
    }
   
    
}
