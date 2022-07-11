package it.cgmconsulting.myblogc9.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.payload.response.PostXlsResponse;
import it.cgmconsulting.myblogc9.payload.response.ReaderXlsResponse;
import it.cgmconsulting.myblogc9.payload.response.UserXlsResponse;
import it.cgmconsulting.myblogc9.repository.PostRepository;
import it.cgmconsulting.myblogc9.repository.UserRepository;

@Service
public class XlsService {
	
	@Autowired PostRepository postRepository;
	@Autowired UserRepository userRepository;
	
	public InputStream createReport() throws Exception{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// creazione xls
		HSSFWorkbook workBook = new HSSFWorkbook();
		
		// creazione sheet
		createAuthorReport(workBook);
		createReaderReport(workBook);
		createPostReport(workBook);
		
		workBook.write(out);
		workBook.close();
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		
		return in;
	}
	
	public void createReaderReport(HSSFWorkbook workBook) {
		
		HSSFSheet sheet = workBook.createSheet("Reader Report");
		
		Cell cell;
		Row row;
		int rownum = 1; int column = 0;
		
		row = sheet.createRow(rownum);
		
		// INTESTAZIONI COLONNE
		// Id
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Reader Id");
		
		// Username
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Username");
		
		// Nr. Comments written
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Nr. Comments written");
		
		// Nr. di ban ricevuti
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Nr. Bans");
		
		// Enabled (Y/N)
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Enabled");
		
		// DEVONO COMPARIRE ANCHE READER CHE NON HANNO SCRITTO ANCORA NEANCHE UN COMMENTO
		List<ReaderXlsResponse> users = userRepository.getReportReader();
		
		for(ReaderXlsResponse u : users) {
			
			column = 0;
			rownum++;
			row = sheet.createRow(rownum);
		
			// Comment Author Id
			cell = row.createCell(column++, CellType.NUMERIC);
			cell.setCellValue(u.getId());
			
			// Username
			cell = row.createCell(column++, CellType.STRING);
			cell.setCellValue(u.getUsername());
			
			// Nr. Comments written
			cell = row.createCell(column++, CellType.NUMERIC);
			cell.setCellValue(u.getCommentsWritten());
			
			// Nr. Bans
			cell = row.createCell(column++, CellType.NUMERIC);
			cell.setCellValue(u.getBans());
			
			cell = row.createCell(column++, CellType.STRING);
			cell.setCellValue(u.isEnabled() ? "Y" : "N");
		}
		
	}

	public void createAuthorReport(HSSFWorkbook workBook) {
		
		HSSFSheet sheet = workBook.createSheet("Author Report");
		
		Cell cell;
		Row row;
		int rownum = 1; int column = 0;
		
		row = sheet.createRow(rownum);
		
		// INTESTAZIONI COLONNE
		// Id
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Author Id");
		
		// Username
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Username");
		
		// Nr. Posts written
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Nr. Posts written");
		
		// Average (media dei voti di TUTTI i post scritti dall'author)
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Average");
		
		// DEVONO COMPARIRE ANCHE GLI AUTORI CHE NON HANNO SCRITTO ANCORA NEANCHE UN POST
		
		List<UserXlsResponse> users = userRepository.getReportAuthor();
		for(UserXlsResponse u : users) {
			
			column = 0;
			rownum++;
			row = sheet.createRow(rownum);
			
			// Id
			cell = row.createCell(column++, CellType.NUMERIC);
			cell.setCellValue(u.getId());
			
			// username
			cell = row.createCell(column++, CellType.STRING);
			cell.setCellValue(u.getUsername());
			
			// nr. post written
			cell = row.createCell(column++, CellType.NUMERIC);
			cell.setCellValue(u.getPostsWritten());
			
			// Average
			cell = row.createCell(column++, CellType.NUMERIC);
			cell.setCellValue(u.getTotalAverage());
			
		}
}
	
	public void createPostReport(HSSFWorkbook workBook) {
		
		HSSFSheet sheet = workBook.createSheet("Post Report");
		
		Cell cell;
		Row row;
		int rownum = 1; int column = 0;
		
		row = sheet.createRow(rownum);
		
		// INTESTAZIONI COLONNE
		// Id
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Post Id");
		
		// Title
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Title");
		
		// Average
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Avg");
		
		// Published (Y / N)
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Published");
		
		// Author (username)
		cell = row.createCell(column++, CellType.STRING);
		cell.setCellValue("Author");
		
		// CONTENUTO REPORT
		List<PostXlsResponse> posts = postRepository.getPostReport();
		int columnPositionTitle = 0;
		int columnPositionPublished = 0;
		int countPublished = 0;
		for(PostXlsResponse p : posts) {
			
			column = 0;
			rownum++;
			row = sheet.createRow(rownum);
			
			// Id
			cell = row.createCell(column++, CellType.NUMERIC);
			cell.setCellValue(p.getId());
			
			// Title
			cell = row.createCell(column++, CellType.STRING);
			cell.setCellValue(p.getTitle());
			columnPositionTitle = column-1;
			
			// Average
			cell = row.createCell(column++, CellType.NUMERIC);
			cell.setCellValue(p.getAverage());
			
			// Published
			cell = row.createCell(column++, CellType.STRING);
			cell.setCellValue(p.isPublished() ? "Y" : "N");
			columnPositionPublished = column-1;
			if(p.isPublished()) countPublished++;
			
			// Author
			cell = row.createCell(column++, CellType.STRING);
			cell.setCellValue(p.getAuthor());
			
		}
		
		// TOTALI
		rownum++;
		row = sheet.createRow(rownum);
		
		// numero totale post
		cell = row.createCell(columnPositionTitle, CellType.NUMERIC);
		cell.setCellValue(posts.size());	
		
		// numero totale post pubblicati
		cell = row.createCell(columnPositionPublished, CellType.NUMERIC);
		cell.setCellValue(countPublished);
				
	}

}
