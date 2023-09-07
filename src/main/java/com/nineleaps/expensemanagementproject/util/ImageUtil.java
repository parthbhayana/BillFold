<<<<<<< Updated upstream
package com.nineleaps.expensemanagementproject.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import com.nineleaps.expensemanagementproject.entity.Expense;

public class ImageUtil {

	public static BufferedImage convertToBufferedImage(Expense expense) throws IOException {

		byte[] imageData = expense.getFile();
		InputStream in = new ByteArrayInputStream(imageData);
		BufferedImage image = ImageIO.read(in);
		return image;
	}

}
=======
//package com.nineleaps.expensemanagementproject.util;
//
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import javax.imageio.ImageIO;
//import com.nineleaps.expensemanagementproject.entity.Expense;
//
//public class ImageUtil {
//
//	public static BufferedImage convertToBufferedImage(Expense expense) throws IOException {
//
//		List<byte[]> imageData = expense.getSupportingDocuments();
//		InputStream in = new ByteArrayInputStream(imageData);
//		BufferedImage image = ImageIO.read(in);
//		return image;
//	}
//
//}
>>>>>>> Stashed changes
