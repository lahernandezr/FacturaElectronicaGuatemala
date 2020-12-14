package com.printer;

import java.awt.print.PrinterJob;
import javax.print.PrintService;
import java.util.List;
import java.util.ArrayList;
import javax.print.PrintServiceLookup;

public final class UtilidadesImpresora 
{
    public static PrintService impresoraDefault()
    {
        return PrintServiceLookup.lookupDefaultPrintService();
    }

    public static PrintService buscarImpresora(String nombreImpresora)
    {
        nombreImpresora = nombreImpresora.toLowerCase();

        PrintService service = null;

        PrintService[] services = PrinterJob.lookupPrintServices();

        for (int index = 0; service == null && index < services.length; index++) {

            if (services[index].getName().toLowerCase().indexOf(nombreImpresora) >= 0) {
                service = services[index];
            }
        }

        return service;
    }

    public static List<String> listarImpresoras() 
    {
        PrintService[] services = PrinterJob.lookupPrintServices();
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < services.length; i++) {
            list.add(services[i].getName());
        }

        return list;
    }
}