package com.nineleaps.expensemanagementproject.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineleaps.expensemanagementproject.entity.FileResponse;
import com.nineleaps.expensemanagementproject.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@PostMapping("/upload")
	public ResponseEntity<FileResponse> fileUpload(@RequestParam("image") MultipartFile image) {
		String fileName = null;
		try {
			fileName = this.fileService.uploadImage(path, image);
			
			//repository Call
			//file name save (using getter)
			
			
			
		} catch (IOException e) {

			e.printStackTrace();
			return new ResponseEntity<>(new FileResponse(null, "Image not Uploaded due to some error!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(new FileResponse(fileName, "Image Uploadded Successgully!"), HttpStatus.OK);

	}

}
