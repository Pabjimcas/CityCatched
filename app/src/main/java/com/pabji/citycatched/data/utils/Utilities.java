package com.pabji.citycatched.data.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Utilities {

	public static Bitmap getBitmapImage(File dir){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bp = BitmapFactory.decodeFile(dir.getPath(), options);
		return bp;
	}
	
	public static ArrayList<File> getJPGFiles(File dir) {
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return name.endsWith(".jpg");
			}
		});
		
		Arrays.sort(files);
		ArrayList<File> jpgFiles = new ArrayList<File>();

		for (File file : files) {
			jpgFiles.add(file);
		}
		
		return jpgFiles;
	}

	public static ArrayList<Mat> getImageMats(ArrayList<File> imageFiles) {
		ArrayList<Mat> imageMats = new ArrayList<Mat>();
		
		for (File image : imageFiles) {
			Mat fullSizeTrainImg = Imgcodecs.imread(image.getPath());
			Mat resizedTrainImg = new Mat();
			Imgproc.resize(fullSizeTrainImg, resizedTrainImg, new Size(640, 480), 0, 0, Imgproc.INTER_CUBIC);
			imageMats.add(resizedTrainImg);
		}
		
		return imageMats;
	}
	
	public static ArrayList<String> getFileNames(ArrayList<File> imageFiles) {
		ArrayList<String> fileNames = new ArrayList<String>();
		
		for (File image : imageFiles) {
			fileNames.add(image.getName().substring(0, image.getName().lastIndexOf(".")));
		}
		
		return fileNames;
	}


	public static void copyFile(File src, File dst) throws IOException {
		InputStream inputStream = new FileInputStream(src);

		OutputStream outputStream = new FileOutputStream(dst);

		byte[] buf = new byte[1024];
		int len;
		while ((len = inputStream.read(buf)) > 0) {
			outputStream.write(buf, 0, len);
		}
		inputStream.close();
		outputStream.close();
	}

	public static String encodeImage(byte[] imageByteArray) {
		return Base64.encodeToString(imageByteArray,Base64.DEFAULT);
	}

	public static byte[] decodeImage(String imageDataString){
		return Base64.decode(imageDataString,Base64.DEFAULT);
	}

	public static byte[] compressBitmap(ContentResolver contentResolver, Uri uri) throws IOException {
		Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
		Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/6, bitmap.getHeight()/6, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

}
