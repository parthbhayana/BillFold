package com.nineleaps.expensemanagementproject.controller;

import java.io.IOException;
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
import com.nineleaps.expensemanagementproject.service.IFileService;

@RestController
@RequestMapping("/file")
public class FileController {
	@Autowired
	private IFileService fileService;
	@Value("${project.document}")
	private String path;

	@PostMapping("/upload/{expense_id}")
	public ResponseEntity<FileResponse> fileUpload(@RequestParam("document") MultipartFile document,
			@PathVariable(name = "expenseid") Long expenseid) {
		String fileName = null;
		try {
			fileName = this.fileService.uploadDocument(path, document, expenseid);
			// repository Call
			// file name save (using getter)
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(new FileResponse(null, "Document not Uploaded due to some error!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new FileResponse(fileName, "Document Uploadded Successgully!"), HttpStatus.OK);
	}
}