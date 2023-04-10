package com.nineleaps.expensemanagementproject.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineleaps.expensemanagementproject.entity.FileResponse;
import com.nineleaps.expensemanagementproject.service.FileServiceImpl;
import com.nineleaps.expensemanagementproject.service.IFileService;
import com.nineleaps.expensemanagementproject.sharedobject.SharedObject;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private IFileService fileService;
	
	@Autowired
	private FileServiceImpl fileserviceimpl;

	@Value("${project.document}")
	private String path;

	Statement stmt = null; // for adding document name to table
	Connection conn = null;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ems";
	static final String USER = "root";
	static final String PASS = "P@rth1234";


	
	@PostMapping("/upload")///{expenseId}
	public ResponseEntity<FileResponse> fileUpload(@RequestParam("document") MultipartFile document)         // ,@PathVariable("expenseId") Long expenseId
	{
		String fileName = null;
		try {
			fileName = this.fileService.uploadDocument(path, document);

			// repository Call
			try {
				// Register JDBC driver
				Class.forName(JDBC_DRIVER);

				// Open a connection
				conn = DriverManager.getConnection(DB_URL, USER, PASS);

				// Execute a query to update the table
				stmt = conn.createStatement();

				String sql = "UPDATE expense SET supporting_document = ? WHERE expense_id = ?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				
//				FileServiceImpl fileService = new FileServiceImpl();
//		        String documentName = fileService.getFileName1();
				
				SharedObject sharedObject = SharedObject.getInstance();
		        String documentName = sharedObject.getFileName1();
		        
		        long expenseId = 3; ////////////////////////////////////////////////////////////////////////////////////
				
				pstmt.setString(1, documentName);
		        pstmt.setLong(2, expenseId);
				pstmt.executeUpdate();

				pstmt.close();
				conn.close();
//				String sql = "UPDATE expense " + "SET supporting_document = 'docc' " + "WHERE expense_id = 2";
//				stmt.executeUpdate(sql);  
//				stmt.close();
//				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {
				}
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			
			//
			
		} catch (IOException e) {

			e.printStackTrace();
			return new ResponseEntity<>(new FileResponse(null, "File not Uploaded due to some error!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(new FileResponse(fileName, "File Uploadded Successfully!"), HttpStatus.OK);

	}

}