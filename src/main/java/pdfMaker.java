import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public  class pdfMaker {
    public static final String FONT = "C:\\Windows\\Fonts\\ARIAL.ttf";
    public static void createPdf() {

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font f1 = new Font(bf);
        try {
            document.add(new Paragraph("Здраствуйте", f1));
            document.add(new Paragraph(new Date().toString()));
            document.add(new Paragraph("========================================================================"));
            PdfPTable table = new PdfPTable(3);
            PdfPCell cell = new PdfPCell(new Paragraph("Strgdgdgdf",f1));
            cell.setColspan(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.YELLOW);
            table.addCell(cell);
            table.addCell("Item1");
            table.addCell("Item2");
            table.addCell("Item3");
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();

    }

}
