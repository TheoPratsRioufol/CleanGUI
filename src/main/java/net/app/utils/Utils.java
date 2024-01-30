package net.app.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

public class Utils {

	public static final String read(InputStream stream) {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			StringBuilder stbuild = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				stbuild.append(line + "\n");
			}
			return stbuild.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static final BufferedImage readImage(InputStream stream) {
		try {
			return ImageIO.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final boolean write(String data, OutputStream stream) {

		try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(stream, StandardCharsets.UTF_8))) {

			wr.write(data);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return !true;
		}

	}
	
	public static final boolean write(String data, File file) {
		try {
			return write(data, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static final InputStream resolveRes(String path) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}

}
