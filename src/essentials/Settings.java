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
	Properties settings;
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
		this.defaultValues = defaultValues;
		this.file = file;
		filename = file.getName();
		this.useXML = useXML;
		log.debug("Reading " + filename);
		try {
			if (file.canRead()) {

				try {
					if (useXML)
						settings.loadFromXML(new FileInputStream(file));
					else
						settings.load(new FileInputStream(file));
					log.info("Loaded " + filename);
				} catch (InvalidPropertiesFormatException e) {
					log.error("Invalid properties format in '" + filename
							+ "' Resetting '" + filename
							+ "' to default values.");
					settings = defaultValues;
					if (useXML)
						settings.storeToXML(new FileOutputStream(file), null);
					else
						settings.store(new FileOutputStream(file), null);
				}

			} else {
				if (file.exists()) {
					log.fatal("Can't read '" + filename + "'");
					System.exit(1);
				} else {
					log.warning("'" + filename
							+ "' doesn't exist! Using default values");
					file.createNewFile();
					settings = defaultValues;
					if (useXML)
						settings.storeToXML(new FileOutputStream(file), null);
					else
						settings.store(new FileOutputStream(file), null);
				}
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
	 * @return
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
			if (useXML)
				settings.storeToXML(new FileOutputStream(this.file), null);
			else
				settings.store(new FileOutputStream(this.file), null);
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
	 * @return
	 */
	public String[] getArray(String key) {

		return settings.getProperty(key).split(
				"[\\r\\n\\p{Punct}\\p{Blank}\\s]+");

	}

	/**
	 * Add a comment to the beginning of the File. Will only be added if you
	 * call .setSetting afterwards or set 'updateNow' to true
	 * 
	 * @param comment
	 */
	public boolean setComment(String comment, boolean updateNow) {
		this.comment = comment;
		if (updateNow) {

			try {
				if (useXML)
					settings.storeToXML(new FileOutputStream(this.file),
							comment);
				else
					settings.store(new FileOutputStream(this.file), comment);
			} catch (IOException e) {
				log.fatal("IOException while saving\n" + e.getMessage());
				log.logStackTrace(e);
				return false;
			}

		}
		return true;
	}

	/**
	 * Get the properties object. If you modify values in this object it won't
	 * be automatically synced to the file. Use .writeToFile() to do so
	 * 
	 * @return
	 */
	public Properties getProperties() {
		return settings;
	}

	/**
	 * Set the properties object. It will be synced to the file.
	 * 
	 * @param props
	 *            The properties object ot be synced
	 */
	public void setProperties(Properties props) {
		this.settings = props;
	}

	/**
	 * Sync all values to the file. Not necessary if you use .setSetting()
	 */
	public boolean writeToFile() {
		try {
			if (useXML)
				settings.storeToXML(new FileOutputStream(this.file), comment);
			else
				settings.store(new FileOutputStream(this.file), comment);
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
			if (file.canRead()) {

				try {
					if (useXML)
						settings.loadFromXML(new FileInputStream(file));
					else
						settings.load(new FileInputStream(file));
					log.info("Reloaded " + filename);
				} catch (InvalidPropertiesFormatException e) {
					log.error("Invalid properties format in '" + filename
							+ "' Resetting '" + filename
							+ "' to default values.");
					settings = defaultValues;
					if (useXML)
						settings.storeToXML(new FileOutputStream(file), null);
					else
						settings.store(new FileOutputStream(file), null);
				}

			} else {
				if (file.exists()) {
					log.fatal("Can't read '" + filename + "'");
					System.exit(1);
				} else {
					log.warning("'" + filename
							+ "' doesn't exist! Using default values");
					file.createNewFile();
					settings = defaultValues;
					if (useXML)
						settings.storeToXML(new FileOutputStream(file), null);
					else
						settings.store(new FileOutputStream(file), null);
				}
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
}