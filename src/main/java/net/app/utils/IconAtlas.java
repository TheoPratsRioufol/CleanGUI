package net.app.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.app.Config;

public class IconAtlas {
	
	private static final Logger logger = LogManager.getLogger(IconAtlas.class);

	
	private Map<String, BufferedImage> atlas;
	
	public IconAtlas() {
		System.out.println("load icon");
		try {
			atlas = new HashMap<>();
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void load() throws Exception {
		String index = Utils.read(Utils.resolveRes("icons/index.json"));
		
		JsonObject json = JsonParser.parseString(index).getAsJsonObject();
		JsonArray iconslist = json.get("icons").getAsJsonArray();
		
		for (JsonElement e : iconslist) {
			String key = e.getAsString();
			BufferedImage img =  Utils.readImage(Utils.resolveRes("icons/"+key));
			if (img != null) {
				logger.info("Successfully loaded {}",key);
				atlas.put(key.split("\\.")[0], img);
			}
		}
	}
	
	public ImageIcon getIcon(String key, int size) {
		return new ImageIcon(atlas.get(key).getScaledInstance(size, size, BufferedImage.SCALE_SMOOTH));
	}

}
