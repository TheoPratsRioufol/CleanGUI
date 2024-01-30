package net.app;

import java.io.File;

import net.app.utils.IconAtlas;

public class App {

	//public static final GuiLogger GUI_LOGGER = new GuiLogger();

	private Config config;
	private File workingDir;
	private IconAtlas iconAtlas;

	public App() {

	}

	public void init() {
		iconAtlas = new IconAtlas();
		config = Config.loadConfig();
		
		WorkspaceDialog dialog = new WorkspaceDialog(this);
		dialog.setVisible(true);
		workingDir = new File(dialog.getSelectedWorkingDir());
		config.setCurrentWorkspace(workingDir.getAbsolutePath());
		config.save();
	}

	public Config getConfig() {
		return config;
	}
	
	public IconAtlas getIconAtlas() {
		return iconAtlas;
	}

}
