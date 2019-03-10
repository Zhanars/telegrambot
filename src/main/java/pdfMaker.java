import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
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

    public static synchronized void createNewPdf(String Username, String Tablename, String[][] Record) {

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



    public static synchronized void createGuardPdf(String Username, String Tablename, String[][] Record, int month) {

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

    public static synchronized void createUniverTranskriptPdf(String Username, String Tablename, String[][] Record) {

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

    public static synchronized void createUniverExamSchudelePdf(String Username, String Tablename, String[][] Record){
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

            document1.add(new Paragraph(Tablename+" "+Username + "\n\n\n", f1));
            int colCount = Record[0].length -2;
            int rowCount = Record.length;

            PdfPTable table = new PdfPTable(colCount);
            table.setHeaderRows(1);
            for (int i=0; i < rowCount; i++){
                if (i > 0) {
                    PdfPCell cell = new PdfPCell(new Paragraph(Record[i][4], th));
                    cell.setColspan(colCount);

                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.YELLOW);
                    table.addCell(cell);

                }
                for (int j=0; j < colCount; j++) {
                    if (i>0){
                        if(j != 4)
                            table.addCell(new Paragraph(Record[i][j], f1));
                    }
                    else
                    if(j != 4)
                        table.addCell(new Paragraph(Record[i][j], f1));
                }
            }

            document1.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document1.close();

    }
    public static synchronized void createHostelPdf(String[][] Record){
        Calendar today = new GregorianCalendar();
        Document document1 = new Document();
        try {
            PdfWriter.getInstance(document1, new FileOutputStream("hostel_report.pdf"));
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

            document1.add(new Paragraph("Выписка по свободным местам за " + today.get(Calendar.DAY_OF_MONTH) + "-"+ (today.get(Calendar.MONTH) + 1) +"-" + today.get(Calendar.YEAR) + " день \n\n\n", f1));
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

    public static synchronized void Schedulepdf(String Username, String Tablename, String[][] Record,String Semestr){
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
        Font f2 = new Font(bf,11,Font.NORMAL,new BaseColor(69,75,149));
        Font th = new Font(bf,12,Font.BOLD, BaseColor.DARK_GRAY);
        try {
            document1.add(new Paragraph(Tablename+" "+Username, f1));
            document1.add(new Paragraph("Текущий семестр: " + Semestr + "\n",f1));
            document1.add(new Paragraph("========================================================================\n\n"));
            int colCount = Record[0].length;
            int rowCount = Integer.parseInt(Record[0][0])+1;
            PdfPTable table = new PdfPTable(colCount);
            table.setHeaderRows(1);
            for (int i=0; i < rowCount; i++){
                for (int j=0; j < colCount; j++) {

                    if((i==0) && (j==0)){
                        table.addCell(new Paragraph("", f1));
                    } else {
                        if(j==0){
                            PdfPCell cell = new PdfPCell(new Paragraph(Record[i][0], f1));
                            cell.setRotation(90);
                            cell.setBackgroundColor(new BaseColor(224,231,239));
                            table.addCell(cell);
                            table.setLockedWidth(false);
                        }else
                            if(i==0){
                                PdfPCell cell = new PdfPCell(new Paragraph(Record[0][j], f1));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setBackgroundColor(new BaseColor(224,231,239));
                                table.addCell(cell);

                            }else {
                            if(Record[i][j]==null) {
                                table.addCell(new Paragraph("", f2));
                            }else {
                                PdfPCell cell = new PdfPCell(new Paragraph(Record[i][j], f2));
                                cell.setBackgroundColor(new BaseColor(218, 233, 251));
                                table.addCell(cell);
                            }
                            }
                    }
                }
            }
            table.setWidthPercentage(100);
            float[] columnWidths = {5f, 19f, 19f,19f,19f,19f};
            table.setWidths(columnWidths);
            document1.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document1.close();
    }

    public static synchronized void AttendenceCatch(String Username, String Tablename, String[][] Record,String Semestr) {

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
            document1.add(new Paragraph("Текущий семестр: " + Semestr + "\n",f1));
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
}
