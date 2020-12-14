package com.printer;

import java.awt.image.*;
import java.awt.print.*;
import java.awt.Graphics2D;
import java.awt.Graphics;


public class ImgPrinter implements Printable {

    BufferedImage  img;

    public ImgPrinter(BufferedImage  img) {
        this.img = img;
    }

    public int print(Graphics pg, PageFormat pf, int pageNum) {
        if (pageNum != 0) {
            return Printable.NO_SUCH_PAGE;
        }

        //BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
        //img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        //bufferedImage.getGraphics().drawImage(img, 0, 0, null);

        Graphics2D g2 = (Graphics2D) pg;
        g2.translate(pf.getImageableX(), pf.getImageableY());
        g2.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
        return Printable.PAGE_EXISTS;
    }

    public void printPage() throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        boolean ok = job.printDialog();
        if (ok) {
            job.setJobName("TEST JOB");
            job.setPrintable(this);
            job.print();
        }
    }
}