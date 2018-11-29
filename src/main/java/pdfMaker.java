import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public  class pdfMaker {
    public static final String FONT = "C:\\Windows\\Fonts\\ARIAL.ttf";
    public static void createPdf(String Username, String Tablename, int colCount) {

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
            document.add(new Paragraph(Username, f1));
            document.add(new Paragraph(new Date().toString()));
            document.add(new Paragraph(Tablename, f1));
            document.add(new Paragraph("========================================================================"));
            PdfPTable table = new PdfPTable(colCount);
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
    public static void createGuardPdf(String Username, String Tablename, String[][] Record) {

        Document document1 = new Document();
        try {
            PdfWriter.getInstance(document1, new FileOutputStream(Username+".pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document1.open();
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font f1 = new Font(bf);
        Font th = new Font(bf,12,Font.BOLD, BaseColor.DARK_GRAY);
        try {
            document1.add(new Paragraph(Tablename+" "+Username, f1));
            Calendar c = new GregorianCalendar();
            String date2 = DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("ru")).format(c.getTime());
            c.add(Calendar.MONTH, -1);
            String date1 = DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("ru")).format(c.getTime());
            document1.add(new Paragraph("За период: " + date1 + " - " + date2,f1));
            document1.add(new Paragraph("========================================================================"));
            PdfPTable table = new PdfPTable(3);
            table.addCell(new Paragraph("№",th));
            table.addCell(new Paragraph("Дата и время",th));
            table.addCell(new Paragraph("Вход",th));
            table.addCell(new Paragraph("Выход",th));
            table.setHeaderRows(1);
            int colCount = Record.length;
            for (int i=0; i<colCount; i++){
                int k=i+1;
                table.addCell(new Paragraph(String.valueOf(k),f1));
                table.addCell(new Paragraph(Record[i][0],f1));
                table.addCell(new Paragraph(Record[i][1],f1));
                table.addCell(new Paragraph(Record[i][2],f1));
            }
            document1.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document1.close();

    }

}
