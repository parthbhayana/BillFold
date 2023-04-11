package com.nineleaps.expensemanagementproject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nineleaps.expensemanagementproject.sharedobject.SharedObject;

import io.swagger.v3.oas.models.Paths;

@Service
public class FileServiceImpl implements IFileService {
	@Override
	public String uploadDocument(String path, MultipartFile file) throws IOException {
	    // File name
	    String name = file.getOriginalFilename();
	    
	    // Get file extension
	    String extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
	    
	    // Check if file extension is accepted
	    if (!extension.equals("png") && !extension.equals("pdf") && !extension.equals("jpeg") && !extension.equals("jpg")) {
	        throw new IllegalArgumentException("Only PNG, PDF, and JPEG files are accepted.");
	    }

	    //Changing file name		
	    String randomID = UUID.randomUUID().toString();
	    String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
	    
	    //Full path		
	    String filePath = path + File.separator + fileName1;

	    //Create folder if not created	
	    File f = new File(path);
	    if (!f.exists()) {
	        f.mkdir();
	    }

	    // file copy
	    Files.copy(file.getInputStream(), java.nio.file.Paths.get(filePath));
	    
	    // Store fileName1 in shared object
	    SharedObject sharedObject = SharedObject.getInstance();
	    sharedObject.setFileName1(fileName1);
	    
	    return name;
	}
}