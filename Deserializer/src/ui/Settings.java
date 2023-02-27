package ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Settings {
	public static File currentFileDirectory;
	public static boolean darkMode;
	public static boolean convertUnixTimestampToDate;
	public static boolean showFullClassNames;
	public static boolean showSerialVersionUID;
	public static int fontSize;
	
	public static final File settingsFile = new File("settings.txt");
	
	public static final String CURRENT_FILE_PARAM = "currentFilePath";
	public static final String DARK_MODE_PARAM = "darkMode";
	public static final String CONVERT_UNIX_TIME_PARAM = "convertUnixTimestampToDate";
	public static final String SHOW_FULL_CLASS_PARAM = "showFullClassNames";
	public static final String SHOW_SERIAL_PARAM = "showSerialVersionUID";
	public static final String FONT_SIZE_PARAM = "fontSize";
	
	/**
	 * Reads the settings from file
	 */
	public static void read() {
		try {
			Scanner sc = new Scanner(settingsFile);
			while(sc.hasNextLine()) {
				String currentSetting = sc.nextLine();
				updateSetting(currentSetting);
			}
			sc.close();
		} catch (Exception e) {
			loadDefaultSettings();
		}
	}

	/**
	 * Updates a single setting from a single line in the setting file
	 * @param currentSetting one line with a = to separate parameter and value
	 */
	private static void updateSetting(String currentSetting) {
		int equalDivider = currentSetting.indexOf('=');
		String settingName = currentSetting.substring(0, equalDivider);
		String settingValue = currentSetting.substring(equalDivider + 1);
		if(settingName.equals(CURRENT_FILE_PARAM)) {
			currentFileDirectory = new File(settingValue);
		} else if(settingName.equals(DARK_MODE_PARAM)) {
			darkMode = Boolean.parseBoolean(settingValue);
		} else if(settingName.equals(CONVERT_UNIX_TIME_PARAM)) {
			convertUnixTimestampToDate = Boolean.parseBoolean(settingValue);
		} else if(settingName.equals(SHOW_FULL_CLASS_PARAM)) {
			showFullClassNames = Boolean.parseBoolean(settingValue);
		} else if(settingName.equals(SHOW_SERIAL_PARAM)) {
			showSerialVersionUID = Boolean.parseBoolean(settingValue);
		} else if(settingName.equals(FONT_SIZE_PARAM)) {
			fontSize = Integer.parseInt(settingValue);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Loads the default settings if no settings file is found or it's corrupted
	 */
	private static void loadDefaultSettings() {
		currentFileDirectory = new File("%appdata%/.minecraft");
		darkMode = false;
		convertUnixTimestampToDate = false;
		showFullClassNames = false;
		showSerialVersionUID = false;
		fontSize = 10;
	}

	/**
	 * Writes settings to file
	 */
	public static void write() {
		try {
			FileWriter fw = new FileWriter(settingsFile);
			fw.write(CURRENT_FILE_PARAM + "=" + currentFileDirectory + "\n");
			fw.write(DARK_MODE_PARAM + "=" + darkMode + "\n");
			fw.write(CONVERT_UNIX_TIME_PARAM + "=" + convertUnixTimestampToDate + "\n");
			fw.write(SHOW_FULL_CLASS_PARAM + "=" + showFullClassNames + "\n");
			fw.write(SHOW_SERIAL_PARAM + "=" + showSerialVersionUID + "\n");
			fw.write(FONT_SIZE_PARAM + "=" + fontSize + "\n");
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
