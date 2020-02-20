package main;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.text.pdf.BaseFont;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

class PDFCreator {
    static void generatePDF(ArrayList<DictionaryElement> list) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter("files/result.pdf"));
        Document doc = new Document(pdfDoc);

        try {
            PdfFont font5 = PdfFontFactory.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.CACHED);
            doc.setFont(font5);
        }
        catch (Exception ignored) {}

        //doc.add(new Paragraph(begin + "-" + end + " słów, wersja 1"));

        float[] columnWidths = {1, 1, 4};
        Table table = new Table(UnitValue.createPointArray(columnWidths));

        for (DictionaryElement elem : list) {
            table.addCell(elem.englishWord);

            try {
                ImageData data = ImageDataFactory.create("files/pronunciation/" + elem.englishWord + ".png");
                Image image = new Image(data);
                table.addCell(image.setMaxWidth(100));
            } catch (Exception ignored) {
                table.addCell("");
            }
            table.addCell(elem.meanings);
        }

        doc.add(table);
        doc.close();
    }
}