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
import java.util.*;

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
    public static void createGuardPdf(String Username, String Tablename, String[][] Record, int month) {

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
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.MONTH, month);
            String date1 = DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("ru")).format(c.getTime());
            if (month<0) {
                c.add(Calendar.DAY_OF_WEEK, month);
                c.add(Calendar.MONTH, 1);
                date2 = DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("ru")).format(c.getTime());
            }
            document1.add(new Paragraph("За период: " + date1 + " - " + date2,f1));
            document1.add(new Paragraph("========================================================================\n\n"));
            int colCount = Record[0].length;
            int rowCount = Record.length;
            PdfPTable table = new PdfPTable(colCount);
            table.setHeaderRows(1);
            for (int i=0; i < rowCount; i++){
                for (int j=0; j < colCount; j++) {
                    table.addCell(new Paragraph(Record[i][j], f1));
                }
            }
            document1.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document1.close();

    }

    public static void createUniverTranskriptPdf(String Username, String Tablename, String[][] Record) {

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
            document1.add(new Paragraph("ФИО: " + Record[0][0] + "\n",f1));
            document1.add(new Paragraph("Факультет: " + Record[0][1] + "\n",f1));
            document1.add(new Paragraph("Специальность: " + Record[0][2] + "\n",f1));
            document1.add(new Paragraph("Курс: " + Record[0][3] + "\n",f1));
            document1.add(new Paragraph("========================================================================\n\n"));
            int colCount = Record[0].length - 1;
            int rowCount = Record.length;
            PdfPTable table = new PdfPTable(colCount);
            table.setHeaderRows(1);
            int creditCount = 0, AllCreditCount = 0;
            double Sum = 0, AllSum = 0;
            String semestr = Record[2][colCount];
            for (int i=1; i < rowCount; i++){
                if (i > 1) {
                    if (Record[i][colCount].equals(semestr)) {
                        creditCount = creditCount + Integer.parseInt(Record[i][1]);
                        Sum = Sum + Double.parseDouble(Record[i][4]) * Integer.parseInt(Record[i][1]);
                    } else {
                        PdfPCell cell = new PdfPCell(new Paragraph("GPA за " + semestr + " год: " + String.format("%f",Sum / creditCount), th));
                        cell.setColspan(colCount);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(BaseColor.YELLOW);
                        table.addCell(cell);
                        AllCreditCount = AllCreditCount + creditCount;
                        AllSum = AllSum + Sum;
                        semestr = Record[i][colCount];
                        creditCount = Integer.parseInt(Record[i][1]);
                        Sum = Double.parseDouble(Record[i][4]) * Integer.parseInt(Record[i][1]);
                    }
                }
                for (int j=0; j < colCount; j++) {
                    table.addCell(new Paragraph(Record[i][j], f1));
                }
            }
            PdfPCell cell = new PdfPCell(new Paragraph("GPA за " + semestr + " год: " + String.format("%f",Sum / creditCount), th));
            cell.setColspan(colCount);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.YELLOW);
            table.addCell(cell);
            PdfPCell cell1 = new PdfPCell(new Paragraph("GPA: " + String.format("%f", AllSum / AllCreditCount), th));
            cell1.setColspan(colCount);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(BaseColor.YELLOW);
            table.addCell(cell1);
            document1.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document1.close();

    }





}
