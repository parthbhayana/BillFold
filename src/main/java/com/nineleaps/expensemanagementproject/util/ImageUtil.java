package com.nineleaps.expensemanagementproject.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import com.nineleaps.expensemanagementproject.entity.Expense;

public class ImageUtil {

	public static BufferedImage convertToBufferedImage(Expense expense) throws IOException {

		byte[] imageData = expense.getSupportingDocuments();
		InputStream in = new ByteArrayInputStream(imageData);
		BufferedImage image = ImageIO.read(in);
		return image;
	}

}