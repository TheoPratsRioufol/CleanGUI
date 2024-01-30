package net.app;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.app.utils.Utils;

public class Config {
	
	private static final Logger logger = LogManager.getLogger(Config.class);


	private List<String> previousWorkSpaces;

	private Config() {
		previousWorkSpaces = new ArrayList<>();
	}

	public static Config loadConfig() {
		try {
		File configDir, configFile;
		logger.info("Config initializing");
		JsonObject data = null;

		configDir = new File(System.getenv("APPDATA") + "/cleangui");
		if (!configDir.exists()) {
			logger.info("Config directory doesn't exit... making new one at {}",configDir.getAbsolutePath());
			configDir.mkdir();
		}
		configFile = new File(configDir.getPath() + "/config.json");
		if (!configFile.exists()) {
			configFile.createNewFile();
			data = new JsonObject();
			logger.info("Config file doesn't exit... making new one at {}",configFile.getAbsolutePath());
		} else {
			try {
				String configData = Utils.read(new FileInputStream(configFile));
				data = JsonParser.parseString(configData).getAsJsonObject();
			} catch (Exception e) {
				logger.error(e);
				data = new JsonObject();
			}
		}
		
		final Gson gson = new Gson();
        return gson.fromJson(data, Config.class);
		} catch (Exception e) {
			logger.error(e);
			return new Config();
		}
	}
	
	public void setCurrentWorkspace(String wsp) {
		previousWorkSpaces.remove(wsp);
		previousWorkSpaces.add(0, wsp);
	}

	public List<String> getPreviousWorkSpaces() {
		return previousWorkSpaces;
	}

	public void setPreviousWorkSpaces(List<String> previousWorkSpaces) {
		this.previousWorkSpaces = previousWorkSpaces;
	}

	public void save() {
		try {
			File configDir, configFile;
			
			configDir = new File(System.getenv("APPDATA") + "/cleangui");
			if (!configDir.exists()) {
				configDir.mkdir();
			}
			configFile = new File(configDir.getPath() + "/config.json");
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			Gson gson = new Gson();
			String json = gson.toJson(this);
			
			if (!Utils.write(json, configFile)) {
				logger.error("Unable to save config file at ", configFile);
			} else {
				logger.info("Config file saved at ", configFile);
			}
			
			} catch (Exception e) {
				logger.error(e);
		}
		
	}
}
