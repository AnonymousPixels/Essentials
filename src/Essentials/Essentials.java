package Essentials;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;

/**
 * This 'Toolkit'-class contains some quite useful voids.
 * 
 * @author BeutterFx
 * @version 0.1.0-1
 */
public class Essentials {

	/**
	 * The 'addComponent' method adds a component to a container using the
	 * GridBagLayout.
	 * 
	 * @param container
	 * @param layout
	 * @param component
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param weightx
	 * @param weighty
	 * @param insets
	 * @return boolean if false, exception occurred.
	 */
	public static boolean addComponent(Container container, GridBagLayout layout, Component component, int x, int y,
			int width, int height, double weightx, double weighty, Insets insets) {

		try {

			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = x;
			constraints.gridy = y;
			constraints.gridwidth = width;
			constraints.gridheight = height;
			constraints.weightx = weightx;
			constraints.weighty = weighty;
			constraints.insets = insets;
			layout.setConstraints(component, constraints);
			container.add(component);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * The 'getHashMapObjects' method returns a Object[] which contains the keys
	 * of the previously transfered HashMap.
	 * 
	 * @param hashmap
	 * @return Object[] which contains HashMap data.
	 */
	public static Object[] getHashMapObjects(HashMap<String, Object> hashmap) {

		Object[] objects = new Object[0];

		Set<String> keys = hashmap.keySet();
		for (String s : keys) {

			Object[] array = new Object[objects.length + 1];
			System.arraycopy(objects, 0, array, 0, objects.length);
			array[objects.length] = s;
			objects = array;
		}

		return objects;
	}

	/**
	 * The 'log' method writes a string with a timestamp into a file.
	 * 
	 * @param s
	 * @param file
	 * @return boolean if false, exception occurred.
	 */
	public static boolean log(String s, File file) {

		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			if (!file.exists())
				file.createNewFile();

			FileWriter fileWriter = new FileWriter(file, true);
			fileWriter.append((CharSequence) simpleDateFormat.format(timestamp) + " " + s + "\n");
			fileWriter.close();
			System.out.println(simpleDateFormat.format(timestamp) + " " + s);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * The 'printStringToFile' method writes a string into a file.
	 * 
	 * @param s
	 * @param file
	 * @return boolean if false, exception occurred.
	 */
	public static boolean printStringToFile(String s, File file) {

		try {

			if (!file.exists())
				file.createNewFile();

			FileWriter fileWriter = new FileWriter(file, true);
			fileWriter.append(s + "\n");
			fileWriter.close();
			System.out.println(s);
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
