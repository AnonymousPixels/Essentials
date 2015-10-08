package essentials;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;

/**
 * This 'Essentials'-class contains some quite useful voids.
 * 
 * @author Felix Beutter
 * @version 0.2.1-8 version: major version number.minor version number.bug
 *          fixes-build number
 */
public class Essentials {

	/**
	 * The 'addComponent' method adds a component to a container using the
	 * GridBagLayout.
	 * 
	 * @param container
	 *            Container to which the component will be added
	 * @param layout
	 *            Used GridBagLayout object
	 * @param component
	 *            Component which will be added to the container
	 * @param x
	 *            x coordinate of the component in the grid
	 * @param y
	 *            y coordinate of the component in the grid
	 * @param width
	 *            width of the component
	 * @param height
	 *            height of the component
	 * @param weightx
	 *            x weighting of the component
	 * @param weighty
	 *            y weighting of the component
	 * @param insets
	 *            insets object which declares the distances around the
	 *            component
	 * @return boolean if false, exception occurred
	 */
	public static boolean addComponent(Container container,
			GridBagLayout layout, Component component, int x, int y, int width,
			int height, double weightx, double weighty, Insets insets) {

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
	 *            HashMap<String, Object> object
	 * @return Object[] which contains HashMap data
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
	 * The 'log' method writes a string (with a timestamp) into a file.
	 * 
	 * @param text
	 *            The String, that will be written to the file
	 * @param file
	 *            The file where the text will be saved to
	 * @param printTimestamp
	 *            If true, there will be a timestamp in front of the text
	 * @return boolean if false, exception occurred
	 */
	public static boolean log(String text, File file, boolean printTimestamp) {

		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd.MM.yyyy hh:mm:ss");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			if (!file.exists())
				file.createNewFile();

			FileWriter fileWriter = new FileWriter(file, true);

			if (printTimestamp)
				fileWriter.append((CharSequence) simpleDateFormat
						.format(timestamp) + " " + text + "\n");
			else
				fileWriter.append(text + "\n");

			fileWriter.close();
			System.out.println(simpleDateFormat.format(timestamp) + " " + text);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * The 'printStringToFile' method writes a string into a file.
	 * 
	 * @param text
	 *            The String, that will be written to the file
	 * @param file
	 *            The file where the text will be saved to
	 * @return boolean if false, exception occurred
	 */
	public static boolean printStringToFile(String text, File file) {

		try {

			if (!file.exists())
				file.createNewFile();

			FileWriter fileWriter = new FileWriter(file, true);
			fileWriter.append(text + "\n");
			fileWriter.close();
			System.out.println("Wrote '" + text + "' into '" + file.getPath()
					+ "'");
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * The 'expandObjectArray' method expands the transfered array about one
	 * slot.
	 * 
	 * @param array
	 *            The Object[] which will be expanded
	 * @return expanded Object[]
	 */
	public static Object[] expandObjectArray(Object[] array) {

		Object[] array2 = new Object[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandStringArray' method expands the transfered array about one
	 * slot.
	 * 
	 * @param array
	 *            The String[] which will be expanded
	 * @return expanded String[]
	 */
	public static String[] expandStringArray(String[] array) {

		String[] array2 = new String[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandIntegerArray' method expands the transfered array about one
	 * slot.
	 * 
	 * @param array
	 *            The Integer[] which will be expanded
	 * @return expanded Integer[]
	 */
	public static int[] expandIntegerArray(int[] array) {

		int[] array2 = new int[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandBooleanArray' method expands the transfered array about one
	 * slot.
	 * 
	 * @param array
	 *            The boolean[] which will be expanded
	 * @return expanded boolean[]
	 */
	public static boolean[] expandBooleanArray(boolean[] array) {

		boolean[] array2 = new boolean[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandDoubleArray' method expands the transfered array about one
	 * slot.
	 * 
	 * @param array
	 *            The double[] which will be expanded
	 * @return expanded double[]
	 */
	public static double[] expandDoubleArray(double[] array) {

		double[] array2 = new double[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandCharArray' method expands the transfered array about one slot.
	 * 
	 * @param array
	 *            The char[] which will be expanded
	 * @return expanded char[]
	 */
	public static char[] expandCharArray(char[] array) {

		char[] array2 = new char[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandByteArray' method expands the transfered array about one slot.
	 * 
	 * @param array
	 *            The byte[] which will be expanded
	 * @return expanded byte[]
	 */
	public static byte[] expandByteArray(byte[] array) {

		byte[] array2 = new byte[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandShortArray' method expands the transfered array about one
	 * slot.
	 * 
	 * @param array
	 *            The short[] which will be expanded
	 * @return expanded short[]
	 */
	public static short[] expandShortArray(short[] array) {

		short[] array2 = new short[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandLongArray' method expands the transfered array about one slot.
	 * 
	 * @param array
	 *            The long[] which will be expanded
	 * @return expanded long[]
	 */
	public static long[] expandLongArray(long[] array) {

		long[] array2 = new long[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'expandFloatArray' method expands the transfered array about one
	 * slot.
	 * 
	 * @param array
	 *            The float[] which will be expanded
	 * @return expanded float[]
	 */
	public static float[] expandFloatArray(float[] array) {

		float[] array2 = new float[array.length + 1];
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	/**
	 * The 'findSubstring' method searches for a substing in string and returns
	 * the index of the substring if found.
	 * 
	 * @param string
	 *            The String, in where for the substring should be searched for
	 * @param substring
	 *            The String, which is sought after
	 * @return index of found substring, if the returned value is -1, nothing
	 *         was found
	 */
	public static int findSubstring(String string, String substring) {

		int i = -1;
		if (string.contains(substring))
			i = string.indexOf(substring);
		return i;
	}

	/**
	 * The 'countFileLines' method counts the number of lines of the transfered
	 * file.
	 * 
	 * @param file
	 *            The file, which lines will be counted
	 * @return amount of lines
	 * @throws IOException
	 */
	public static int countFileLines(File file) throws IOException {

		int i = 0;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while (reader.readLine() != null)
			i++;
		reader.close();
		return i;
	}
}
