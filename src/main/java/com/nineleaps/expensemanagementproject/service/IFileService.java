package com.nineleaps.expensemanagementproject.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	String uploadDocument(String path, MultipartFile file, Long employeeId) throws IOException;
}