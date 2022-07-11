package it.cgmconsulting.myblogc9.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import it.cgmconsulting.myblogc9.entity.Post;

@Service
public class PdfService {
	
	@Value("${image.post.path}")
	private String pathImage;
	
	private static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, new BaseColor(0,102,204));
	private static Font FONT_CONTENT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	private static Font FONT_AUTHOR = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);
	private static Font FONT_DATE = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.DARK_GRAY);
	
	public InputStream createPdfFromPost(Post p) throws DocumentException, MalformedURLException, IOException {
		
		// popolare il pdf con contenuti
		// title, image, content, author, updatedAt
		String title = p.getTitle();
		String content = p.getContent();
		String image = p.getImage();
		String author = p.getAuthor().getUsername();
		String updatedAt = p.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
				
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// GENERAZIONE PDF
		// creazione pdf vuoto con impostazioni di pagina
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, out);
		document.open();
		
		
		// TITLE
		Paragraph pTitle = new Paragraph(title, FONT_TITLE);
		pTitle.setAlignment(Element.ALIGN_LEFT); 
		document.add(pTitle);
		
		// IMAGE
		if(image != null) {
			String img = pathImage+image;
			Image pImage = Image.getInstance(img);
			document.add(pImage);
		}
		
		// CONTENT
		Paragraph pContent = new Paragraph(content, FONT_CONTENT);
		pTitle.setAlignment(Element.ALIGN_LEFT);
		addEmptyLines(pContent, 3);
		document.add(pContent);
		
		// UPDATED AT
		Paragraph pUpdtatedAt = new Paragraph("Data: "+updatedAt, FONT_DATE);
		pUpdtatedAt.setAlignment(Element.ALIGN_RIGHT);
		document.add(pUpdtatedAt);
		
		Paragraph pAuthor = new Paragraph("Autore: "+author, FONT_AUTHOR);
		pAuthor.setAlignment(Element.ALIGN_RIGHT);
		document.add(pAuthor);
		
		addMetaData(document, title, author);
		
		document.close();
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		
		return in;
	}
	
	private static void addEmptyLines(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	private void addMetaData(Document document, String title, String author) {
		document.addTitle(title);
		document.addAuthor(author);
	}

}
