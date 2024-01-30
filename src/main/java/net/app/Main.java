package net.app;

import com.formdev.flatlaf.FlatLightLaf;

public class Main {

	public static void main(String[] args) {
		FlatLightLaf.setup();
		App app = new App();
		app.init();
	}

}