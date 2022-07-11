package it.cgmconsulting.myblogc9.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
	
	@Value("${image.post.path}")
	private String pPath;
	
	public BufferedImage fromFileToBufferedImage(MultipartFile file) {
		BufferedImage bf = null;
		try {
			bf = ImageIO.read(file.getInputStream());
			return bf;
		} catch(IOException e) {
			return null;
		}
	}
	
	public boolean checkDimensions(BufferedImage bf, int height, int width) {
		if(bf == null)
			return false;
		if(bf.getHeight() > height || bf.getWidth() > width)
			return false;
		
		return true;
	}
	
	public boolean checkExtension(MultipartFile file, String[] extensions) {
		ImageInputStream img = null; 
		try {
			img = ImageIO.createImageInputStream(file.getInputStream());
		} catch (IOException e) {
			return false;
		}

		Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(img);

		while (imageReaders.hasNext()) {
			ImageReader reader = (ImageReader) imageReaders.next();
			try {
				for(int i = 0; i<extensions.length; i++) {
					if(reader.getFormatName().equalsIgnoreCase(extensions[i])) {
						return true;
					}
				}
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}
	
	public boolean checkSize(MultipartFile file, long size) {
		if(file.getSize() > size)
			return false;
		return true;
	}

	public boolean loadImage(MultipartFile file, long id, String newFileName) {
		
		Path path = Paths.get(pPath+newFileName);
		try {
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	public boolean removeImage(String fileToRemove) {
		Path path = Paths.get(pPath+fileToRemove);
		try {
			Files.delete(path);
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}

}
