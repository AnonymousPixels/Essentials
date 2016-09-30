package essentials;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Contains some quite useful methods.
 * 
 * @author Maximilian von Gaisberg
 * @author Felix Beutter
 * 
 */
public class Essentials {

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();// something
																				// bytesToHex()
																				// needs

	/**
	 * Adds a component to a container using the GridBagLayout.
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
	 * Returns a Object[] that contains the keys of the given HashMap.
	 * 
	 * @param hashmap
	 *            HashMap<String, Object> object
	 * @return Object[] which contains HashMap data
	 */
	public static Object[] getHashMapObjects(HashMap<Object, Object> hashmap) {

		Object[] objects = new Object[0];

		Set<Object> keys = hashmap.keySet();
		for (Object s : keys) {

			Object[] array = new Object[objects.length + 1];
			System.arraycopy(objects, 0, array, 0, objects.length);
			array[objects.length] = s;
			objects = array;
		}

		return objects;
	}

	/**
	 * Writes a string (with a timestamp) to a file.
	 * 
	 * @deprecated {@link SimpleLog} handles this much more comfortable and
	 *             professionally
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
	 * Writes a String to the end of a file.
	 * 
	 * @param text
	 *            The String, that will be written to the file
	 * @param file
	 *            The file that should be written to
	 * @return boolean if false, exception occurred
	 */
	public static boolean printStringToFile(String text, File file) {

		try {

			if (!file.exists())
				file.createNewFile();

			FileWriter fileWriter = new FileWriter(file, true);
			fileWriter.append(text);
			fileWriter.close();
			System.out.println("Wrote '" + text + "' into '" + file.getPath()
					+ "'");
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * Reads a given file and returns the text
	 * 
	 * @param file
	 *            The file to read
	 * 
	 * @return The content of the file
	 */
	public static String readFile(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				file.getAbsolutePath()));
		StringBuilder sb = new StringBuilder();
		try {

			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
		} finally {
			br.close();
		}

		return sb.toString();

	}

	/**
	 * Counts the number of lines in the given file.
	 * 
	 * @param file
	 *            The file, which lines will be counted
	 * @return amount of lines
	 * @throws IOException
	 */
	public static int countFileLines(File file) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	/**
	 * Puts files in an uncompressed .zip folder
	 * 
	 * @param zipFile
	 *            The target zip-folder
	 * @param containingFiles
	 *            The files to put in the zip-folder
	 * @throws IOException
	 */
	public static boolean zip(File zipFile, File[] containingFiles)
			throws IOException {

		if (zipFile.exists()) {
			System.err.println("Zip file already exists, please try another");
			return false;
		}
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		int bytesRead;
		byte[] buffer = new byte[1024];
		CRC32 crc = new CRC32();
		for (int i = 0, n = containingFiles.length; i < n; i++) {
			File file = containingFiles[i];
			if (!file.exists()) {
				zos.close();
				throw new FileNotFoundException("Couldn't find file "
						+ file.getAbsolutePath());
			}
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			crc.reset();
			while ((bytesRead = bis.read(buffer)) != -1) {
				crc.update(buffer, 0, bytesRead);
			}
			bis.close();
			bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(file.getName());
			entry.setMethod(ZipEntry.STORED);
			entry.setCompressedSize(file.length());
			entry.setSize(file.length());
			entry.setCrc(crc.getValue());
			zos.putNextEntry(entry);
			while ((bytesRead = bis.read(buffer)) != -1) {
				zos.write(buffer, 0, bytesRead);
			}
			bis.close();
		}
		zos.close();
		return true;
	}

	/**
	 * Puts files in a .zip folder and compresses them
	 * 
	 * @param files
	 *            The files to put into the zip-file
	 * @param target
	 *            The path of the target zip-file
	 * @throws IOException
	 */
	public static void zipAndCompress(String target, String[] files)
			throws IOException {
		byte b[] = new byte[512];
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(target));
		for (int i = 0; i < files.length; i++) {
			InputStream in = new FileInputStream(files[i]);
			ZipEntry e = new ZipEntry(new File(files[i]).getName());
			zout.putNextEntry(e);
			int len = 0;
			while ((len = in.read(b)) != -1) {
				zout.write(b, 0, len);
			}
			zout.closeEntry();
			in.close();
		}
		zout.close();

	}

	/**
	 * A method to send HTTP requests to a Webserver and fetch the answer
	 * 
	 * @param url
	 *            The URL you want to send a request to
	 * @return The answer from that URL
	 * @throws IOException
	 */
	public static String sendHTTPRequest(URL url) throws IOException {
		System.setProperty("http.agent", "Chrome");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String answer = "";
		String line = "";
		while (null != (line = br.readLine())) {
			answer = answer + line + "\n";
		}
		return answer;
	}

	/**
	 * Downloads a file from an URL and saves it on the computer
	 * 
	 * @param url
	 *            The URL of the file
	 * @param saveFile
	 *            The path where the file should be saved
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static boolean downloadFileFromURL(URL url, File saveFile)
			throws IOException, FileNotFoundException {

		HttpURLConnection c;

		c = (HttpURLConnection) url.openConnection();

		c.connect();

		BufferedInputStream in = new BufferedInputStream(c.getInputStream());

		OutputStream out = new BufferedOutputStream(new FileOutputStream(
				saveFile));
		byte[] buf = new byte[256];
		int n = 0;
		while ((n = in.read(buf)) >= 0) {
			out.write(buf, 0, n);
		}
		out.flush();
		out.close();

		return true;
	}

	/**
	 * Copies a BufferedImage
	 * 
	 * @param image
	 *            The image that should be copied
	 * 
	 * @returns The copied image
	 */
	public static BufferedImage copyBufferedImage(BufferedImage image) {
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Search for all IPs in the local network using the arp -a command. Only
	 * works on Windows machines
	 * 
	 * @param includeLocalhost
	 *            If true, the first entry will be 'localhost'
	 * 
	 * @return An String array containing the IPs
	 * @throws IOException
	 */
	public static String[] searchIPs(boolean includeLocalhost)
			throws IOException {

		if (includeLocalhost) {
			String[] ips = searchIPs(false);
			String[] ips2 = new String[ips.length + 1];
			ips2[0] = "localhost";
			for (int i = 1; i < ips2.length; i++) {
				ips2[i] = ips[i - 1];
			}
			return ips2;
		}

		String[] line = null;
		String[] ips = null;
		String answer = "";
		try {
			Process p = Runtime.getRuntime().exec("arp -a");
			InputStream is = p.getInputStream();
			int c;
			while ((c = is.read()) != -1) {
				// System.out.print((char) c );
				answer = answer + (char) c;
			}

			line = answer.split(Pattern.quote("\n"));
			int length = line.length;
			String[] line2 = new String[length];
			ips = new String[line.length - 3];
			for (int i = 3; i < line.length; i++) {
				line2[i - 2] = line[i];
				ips[i - 3] = line[i].substring(0, 17).trim();

			}
		} catch (NegativeArraySizeException e) {
			return new String[0];
		}
		return ips;

	}

	/**
	 * Assembles a String array to one String, in which the parts are separated
	 * by blanks
	 * 
	 * @param array
	 *            The String array that will be assembled
	 * @return The assembled String
	 */
	public static String getAssembledStringArray(String[] array) {

		String string = "";
		for (String part : array) {
			string = string + part + " ";
		}
		return string.substring(0, string.length() - 1);
	}

	/**
	 * Represents a byte array in a much more comfortable an readable way
	 * 
	 * @param bytes
	 *            the bytes you want to convert
	 * @return the hex string
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Checks if a string is included in a string array
	 * 
	 * @param array
	 *            String array
	 * @param string
	 *            String
	 * @return true if included, false if not
	 */
	public static boolean included(String[] array, String string) {

		for (String s : array) {
			if (s.equals(string))
				return true;
		}
		return false;
	}
}
