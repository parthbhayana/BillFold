package com.nineleaps.expensemanagementproject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nineleaps.expensemanagementproject.entity.Expense;

@Service
public class FileServiceImpl implements IFileService {

	@Autowired
	private IExpenseService expenseServices;

	@SuppressWarnings("unused")
	@Override
	public String uploadDocument(String path, MultipartFile file, Long expenseId) throws IOException {
		// File name
		String name = file.getOriginalFilename();
		Expense em = expenseServices.getExpenseById(expenseId);
		// Get file extension
		String extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();

		// Check if file extension is accepted
		if (!extension.equals("png") && !extension.equals("pdf") && !extension.equals("jpeg")
				&& !extension.equals("jpg")) {
			throw new IllegalArgumentException("Only PNG, PDF, and JPEG files are accepted.");
		}

		// Changing file name
		String randomID = UUID.randomUUID().toString();
		String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
		expenseServices.updateSupportingDocument(fileName1, expenseId);
		System.out.println(fileName1);
		// Full path
		String filePath = path + File.separator + fileName1;

		// Create folder if not created
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}

		// file copy
		Files.copy(file.getInputStream(), java.nio.file.Paths.get(filePath));

		return name;
	}
}