package essentials;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * 
 * A simple class for keeping a Properties object in sync with a .properties or
 * .xml file
 * 
 * @author Maximilian von Gaisberg
 *
 */
public class Settings {
	File file;
	Properties settings = new Properties();
	Properties defaultValues;
	SimpleLog log;
	boolean useXML;
	String comment, filename;

	/**
	 * Use this to read a config file.
	 * 
	 * 
	 * @param file
	 *            The file to be read
	 * @param defaultValues
	 *            The values that should be used if the file is broken or
	 *            missing
	 * @param useXML
	 *            You can choose between .xml and .properties files
	 * @param log
	 *            The log that should be logged to
	 */

	public Settings(File file, Properties defaultValues, boolean useXML,
			SimpleLog log) {
		this.defaultValues = defaultValues != null ? defaultValues : new Properties();
		this.file = file;
		this.useXML = useXML;
		this.log = log != null ? log : new SimpleLog();

		filename = file.getName();
		log.debug("Reading " + filename);
		try {
			if (!file.canRead())
				if (file.exists()) {
					log.fatal("Can't read '" + filename + "'");
					System.exit(1);
				} else {
					log.warning("'" + filename + "' doesn't exist! Using default values");
					file.createNewFile();
					settings = defaultValues;
					if (!useXML)
						settings.store(new FileOutputStream(file), null);
					else
						settings.storeToXML(new FileOutputStream(file), null);
				}
			else
				try {
					if (!useXML)
						settings.load(new FileInputStream(file));
					else
						settings.loadFromXML(new FileInputStream(file));
					log.info("Loaded " + filename);
				} catch (InvalidPropertiesFormatException e) {
					log.error("Invalid properties format in '" + filename + "' Resetting '" + filename
							+ "' to default values.");
					settings = defaultValues;
					if (!useXML)
						settings.store(new FileOutputStream(file), null);
					else
						settings.storeToXML(new FileOutputStream(file), null);
				}

		} catch (FileNotFoundException e) {

			log.fatal("FileNotFoundException while reading '" + filename + "'"
					+ e.getMessage());
			log.logStackTrace(e);
			System.exit(1);

		} catch (IOException e) {
			log.fatal("IOException while reading '" + filename + "'"
					+ e.getMessage());
			log.logStackTrace(e);
			System.exit(1);
		}
	}

	/**
	 * Get a setting from the config file
	 * 
	 * @param key
	 *            The key of the value that should be read in
	 * @return The value of the given setting
	 */
	public String getSetting(String key) {
		return settings.getProperty(key);
	}

	/**
	 * Set a setting. The config file will be automatically updated
	 * 
	 * @param key
	 *            The key of the setting
	 * @param value
	 *            The value of the setting
	 */
	public void setSetting(String key, String value) {
		settings.setProperty(key, value);
		try {
			if (!useXML)
				settings.store(new FileOutputStream(this.file), null);
			else
				settings.storeToXML(new FileOutputStream(this.file), null);
		} catch (IOException e) {
			log.fatal("IOException while saving\n" + e.getMessage());
			log.logStackTrace(e);
			System.exit(1);
		}
	}

	/**
	 * Reads a String from the file and splits it into pieces to fit in an
	 * array. Strings will be split on linebreaks, punctuation characters or
	 * whitespace characters
	 * 
	 * @param key
	 *            The key that represents the value
	 * @return The <code>String[]</code> that was stored in the Settings file
	 */
	public String[] getArray(String key) {

		return settings.getProperty(key).split(
				"[\\r\\n\\p{Punct}\\p{Blank}\\s]+");

	}

	/**
	 * Add a comment to the beginning of the file. Will only be added if you
	 * call .setSetting() or .writeToFile() afterwards or set 'updateNow' to
	 * true
	 * 
	 * @param comment
	 *            The comment that should be written in the head of the file
	 * @param updateNow
	 *            if <code>true</code>, the comment will be written to the file
	 *            right now
	 * @return success
	 */
	public boolean setComment(String comment, boolean updateNow) {
		this.comment = comment;
		if (updateNow)
			try {
				if (!useXML)
					settings.store(new FileOutputStream(this.file), comment);
				else
					settings.storeToXML(new FileOutputStream(this.file), comment);
			} catch (IOException e) {
				log.fatal("IOException while saving\n" + e.getMessage());
				log.logStackTrace(e);
				return false;
			}
		return true;
	}

	/**
	 * Get the properties object. If you modify values in this object it won't
	 * be automatically synced to the file. Use .writeToFile() to do so
	 * 
	 * @return The <code>Properties</code> object
	 */
	public Properties getProperties() {
		return settings;
	}

	/**
	 * Set the properties object. It will be synced to the file if you use
	 * writeToFile();
	 * 
	 * @param p
	 *            The properties object ot be synced
	 */
	public void setProperties(Properties p) {
		this.settings = p;
	}

	/**
	 * Sync all values to the file. Not necessary if you use .setSetting()
	 * 
	 * @return success
	 */
	public boolean writeToFile() {
		try {
			if (!useXML)
				settings.store(new FileOutputStream(this.file), comment);
			else
				settings.storeToXML(new FileOutputStream(this.file), comment);
		} catch (IOException e) {
			log.fatal("IOException while saving\n" + e.getMessage());
			log.logStackTrace(e);
			return false;
		}
		return true;
	}

	/**
	 * Reloads the file manually
	 */
	public void reload() {
		log.debug("Reloading " + filename);
		try {
			if (!file.canRead())
				if (file.exists()) {
					log.fatal("Can't read '" + filename + "'");
					System.exit(1);
				} else {
					log.warning("'" + filename + "' doesn't exist! Using default values");
					file.createNewFile();
					settings = defaultValues;
					if (!useXML)
						settings.store(new FileOutputStream(file), null);
					else
						settings.storeToXML(new FileOutputStream(file), null);
				}
			else
				try {
					if (!useXML)
						settings.load(new FileInputStream(file));
					else
						settings.loadFromXML(new FileInputStream(file));
					log.info("Reloaded " + filename);
				} catch (InvalidPropertiesFormatException e) {
					log.error("Invalid properties format in '" + filename + "' Resetting '" + filename
							+ "' to default values.");
					settings = defaultValues;
					if (!useXML)
						settings.store(new FileOutputStream(file), null);
					else
						settings.storeToXML(new FileOutputStream(file), null);
				}

		} catch (FileNotFoundException e) {

			log.fatal("FileNotFoundException while reading '" + filename + "'"
					+ e.getMessage());
			log.logStackTrace(e);
			System.exit(1);

		} catch (IOException e) {
			log.fatal("IOException while reading '" + filename + "'"
					+ e.getMessage());
			log.logStackTrace(e);
			System.exit(1);
		}
	}

	/**
	 * Checks if a Settings object contains all the given keys
	 * 
	 * @param keys
	 *            The keys to check for
	 * @return <code>true</code> if all of them are in the <code>Settings</code>
	 *         object
	 */
	public boolean containsKeys(String[] keys) {
		for (String string : keys)
			if (!settings.containsKey(string))
				return false;
		return true;
	}

	/**
	 * Check if the Settings Object contains a given key
	 * 
	 * @param key
	 *            The key to check for
	 * 
	 * @return <code>true</code> if the key is present
	 */
	public boolean containsKey(String key) {
		return settings.containsKey(key);
	}

}