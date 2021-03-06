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
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sun.xml.internal.messaging.saaj.util.Base64;

/**
 * Contains some quite useful methods.
 * 
 * @see <a href="http://grunzwanzling.me/Essentials/">Javadoc Website</a>
 * 
 * @author <a href="http://grunzwanzling.me">Maximilian von Gaisberg
 *         (Grunzwanzling)</a>
 * @author Felix Beutter
 * 
 * 
 */
public class Essentials {

	protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Adds a <code>Component</code> to a <code>Container</code> using the
	 * {@link GridBagLayout}.
	 * 
	 * @param c
	 *            <code>Container</code> to which the <code>Component</code>
	 *            will be added
	 * @param l
	 *            Used <code>GridBagLayout</code> object
	 * @param component
	 *            <code>Component</code> which will be added to the
	 *            <code>Container</code>
	 * @param x
	 *            x coordinate of the <code>Component</code> in the grid
	 * @param y
	 *            y coordinate of the <code>Component</code> in the grid
	 * @param width
	 *            width of the <code>Component</code>
	 * @param height
	 *            height of the <code>Component</code>
	 * @param weightx
	 *            x weight of the <code>Component</code>
	 * @param weighty
	 *            y weight of the <code>component</code>
	 * @param i
	 *            <code>Insets</code> which defines the distances around the
	 *            <code>Component</code>
	 * @return boolean false if exception occurred
	 */
	public static boolean addComponent(Container c,
			GridBagLayout l, Component component, int x, int y, int width,
			int height, double weightx, double weighty, Insets i) {

		if (i == null)
			i = new Insets(0, 0, 0, 0);
		try {

			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = x;
			constraints.gridy = y;
			constraints.gridwidth = width;
			constraints.gridheight = height;
			constraints.weightx = weightx;
			constraints.weighty = weighty;
			constraints.insets = i;
			l.setConstraints(component, constraints);
			c.add(component);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Returns a <code>Object[]</code> that contains the keys of the given
	 * <code>HashMap</code>.
	 * 
	 * @param m
	 *            <code>HashMap&lt;String, Object&gt;</code>
	 * @return <code>Object[]</code> which contains HashMap data
	 */
	public static Object[] getHashMapObjects(HashMap<Object, Object> m) {

		Object[] objects = new Object[0];

		Set<Object> keys = m.keySet();
		for (Object s : keys) {

			Object[] array = new Object[objects.length + 1];
			System.arraycopy(objects, 0, array, 0, objects.length);
			array[objects.length] = s;
			objects = array;
		}

		return objects;
	}

	/**
	 * Writes a <code>String</code> (with a timestamp) to a <code>File</code>.
	 * 
	 * @deprecated {@link SimpleLog} handles this much more comfortably and
	 *             professionally
	 * 
	 * @param text
	 *            The <code>String</code>, that will be written to the file
	 * @param f
	 *            The <code>File</code> where the text will be saved to
	 * @param printTimestamp
	 *            If true, there will be a timestamp in front of the text
	 * @return boolean if false, exception occurred
	 */
	public static boolean log(String text, File f, boolean printTimestamp) {

		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd.MM.yyyy hh:mm:ss");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			if (!f.exists())
				f.createNewFile();

			FileWriter fileWriter = new FileWriter(f, true);

			fileWriter.append((!printTimestamp ? text + "" : (CharSequence) simpleDateFormat.format(timestamp) + " " + text) + "\n");

			fileWriter.close();
			System.out.println(simpleDateFormat.format(timestamp) + " " + text);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * Writes a <code>String</code> to the end of a <code>File</code>.
	 * 
	 * @param text
	 *            The <code>String</code>, that will be written to the
	 *            <code>File</code>
	 * @param f
	 *            The <code>File</code> that should be written to
	 * @return boolean false, exception occurred
	 */
	public static boolean printStringToFile(String text, File f) {

		try {

			if (!f.exists())
				f.createNewFile();

			FileWriter fileWriter = new FileWriter(f, true);
			fileWriter.append(text);
			fileWriter.close();
			System.out.println("Wrote '" + text + "' into '" + f.getPath()
					+ "'");
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * Reads a given <code>File</code> and returns the text
	 * 
	 * @param f
	 *            The <code>File</code> to read
	 * 
	 * @return The content of the file
	 * @throws IOException
	 *             if file isn't found or can't be read
	 */

	public static String readFile(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				f.getAbsolutePath()));
		StringBuilder sb = new StringBuilder();
		try {

			for (String line = br.readLine(); line != null;) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
		} finally {
			br.close();
		}

		return sb + "";

	}

	/**
	 * Counts the number of lines in the given <code>File</code>. Empty lines
	 * will be skipped
	 * 
	 * @param f
	 *            The <code>File</code> to count the lines of
	 * @return amount of lines
	 * @throws IOException
	 *             if file is not found or can't be read
	 */
	public static int countFileLines(File f) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(f));
		try {
			byte[] c = new byte[1024];
			int count = 0, readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i)
					if (c[i] == '\n')
						++count;
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	/**
	 * Put files in an uncompressed .zip archive
	 * 
	 * @param zipFile
	 *            The target zip-archive
	 * @param containingFiles
	 *            The files to put in the zip-archive
	 * @return success
	 * @throws IOException
	 *             if files aren't found or can't be written to
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
		for (int i = 0, n = containingFiles.length; i < n; ++i) {
			File file = containingFiles[i];
			if (!file.exists()) {
				zos.close();
				throw new FileNotFoundException("Couldn't find file "
						+ file.getAbsolutePath());
			}
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			crc.reset();
			while ((bytesRead = bis.read(buffer)) != -1)
				crc.update(buffer, 0, bytesRead);
			bis.close();
			bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(file.getName());
			entry.setMethod(ZipEntry.STORED);
			entry.setCompressedSize(file.length());
			entry.setSize(file.length());
			entry.setCrc(crc.getValue());
			zos.putNextEntry(entry);
			while ((bytesRead = bis.read(buffer)) != -1)
				zos.write(buffer, 0, bytesRead);
			bis.close();
		}
		zos.close();
		return true;
	}

	/**
	 * Puts files in a .zip archive and compresses them
	 * 
	 * @param files
	 *            The files to put into the zip-archive
	 * @param target
	 *            The path of the target zip-archive
	 * @throws IOException
	 *             if files aren't found or can't be written to
	 */
	public static void zipAndCompress(String target, String[] files)
			throws IOException {
		byte b[] = new byte[512];
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(target));
		for (int i = 0; i < files.length; ++i) {
			InputStream in = new FileInputStream(files[i]);
			zout.putNextEntry(new ZipEntry(new File(files[i]).getName()));
			for (int len = in.read(b); len != -1;)
				zout.write(b, 0, len);
			zout.closeEntry();
			in.close();
		}
		zout.close();

	}

	/**
	 * Send HTTP requests to a webserver and fetch the answer. Will send
	 * <code>http.agent=Chrome</code>
	 * 
	 * @param l
	 *            The <code>URL</code> you want to send a request to
	 * @return The answer from that <code>URL</code>
	 * @throws IOException
	 *             if connection failed
	 */
	public static String sendHTTPRequest(URL l) throws IOException {
		System.setProperty("http.agent", "Chrome");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				l.openStream()));
		String answer = "", line = "";
		while ((line = br.readLine()) != null)
			answer += line + "\n";
		br.close();
		return answer;
	}

	/**
	 * Send a HTTP-Request to a webserver and fetch the answer. Uses basic
	 * .htaccess authentication. Will send <code>http.agent=Chrome</code>
	 *
	 * @param l
	 *            The <code>URL</code> you want to send a request to
	 * @param username
	 *            The username to send
	 * @param password
	 *            The password to send
	 * @return The answer from that <code>URL</code>
	 * @throws IOException
	 *             if connection failed
	 */
	public static String sendHTTPRequest(URL l, String username,
			String password) throws IOException {
		System.setProperty("http.agent", "Chrome");
		URLConnection uc = l.openConnection();
		String userpass = username + ":" + password;
		new Base64();
		uc.setRequestProperty("Authorization", "Basic " + String.valueOf(Base64.encode(userpass.getBytes())));
		InputStream in = uc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String answer = "", line = "";
		while ((line = br.readLine()) != null)
			answer += line + "\n";
		br.close();
		return answer;
	}

	/**
	 * Send HTTP requests to a webserver and fetch the answer including a
	 * cookie. Mainly used to login to sites. Will send
	 * <code>http.agent=Chrome</code>
	 * 
	 * @param l
	 *            The <code>URL</code> you want to send a request to
	 * @param cookie
	 *            The cookie represented as a string:
	 *            <code>"name1=value1; name2=value2; name3=value3"</code>
	 * @return The answer from that <code>URL</code>
	 * @throws IOException
	 *             if connection failed
	 */
	public static String sendHTTPRequest(URL l, String cookie)
			throws IOException {
		System.setProperty("http.agent", "Chrome");
		HttpURLConnection uc = (HttpURLConnection) l.openConnection();
		uc.setRequestMethod("GET");
		uc.setRequestProperty("Connection", "Keep-Alive");
		uc.setRequestProperty("Cookie", cookie);
		InputStream in = uc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String answer = "", line = "";
		while ((line = br.readLine()) != null)
			answer += line + "\n";
		br.close();
		return answer;
	}

	/**
	 * Downloads a <code>File</code> from an <code>URL</code> and saves it to
	 * the computer
	 * 
	 * @param l
	 *            The <code>URL</code> of the <code>File</code>
	 * @param saveFile
	 *            The path where the <code>File</code> should be saved
	 * @return success
	 * @throws IOException
	 *             if HTTP error code is returned
	 * @throws FileNotFoundException
	 *             if file has not be found
	 */
	public static boolean downloadFileFromURL(URL l, File saveFile)
			throws IOException, FileNotFoundException {

		HttpURLConnection c = (HttpURLConnection) l.openConnection();
		c.connect();

		BufferedInputStream in = new BufferedInputStream(c.getInputStream());
		OutputStream out = new BufferedOutputStream(new FileOutputStream(
				saveFile));
		byte[] buf = new byte[256];
		for (int n = in.read(buf); n >= 0;)
			out.write(buf, 0, n);
		out.flush();
		out.close();

		return true;
	}

	/**
	 * Copies a <code>BufferedImage</code>
	 * 
	 * @param i
	 *            The image that should be copied
	 * 
	 * @return The copied image
	 */
	public static BufferedImage copyBufferedImage(BufferedImage i) {
		ColorModel cm = i.getColorModel();
		return new BufferedImage(cm, i.copyData(null), cm.isAlphaPremultiplied(), null);
	}

	/**
	 * Search for all IPs on the local network using the <code>arp -a</code>
	 * command. Works only on Windows machines
	 * 
	 * @param includeLocalhost
	 *            If true, the first entry will be 'localhost'
	 * 
	 * @return A <code>String[]</code> containing the IPs
	 * @throws IOException
	 *             if command can not be executed
	 */
	public static String[] searchIPs(boolean includeLocalhost)
			throws IOException {

		if (includeLocalhost) {
			String[] ips = searchIPs(false), ips2 = new String[ips.length + 1];
			ips2[0] = "localhost";
			for (int i = 1; i < ips2.length; ++i)
				ips2[i] = ips[i - 1];
			return ips2;
		}

		String[] line = null, ips = null;
		String answer = "";
		try {
			InputStream is = Runtime.getRuntime().exec("arp -a").getInputStream();
			for (int c = is.read(); c != -1;)
				answer += (char) c;

			line = answer.split(Pattern.quote("\n"));
			int length = line.length;
			String[] line2 = new String[length];
			ips = new String[line.length - 3];
			for (int i = 3; i < line.length; ++i) {
				line2[i - 2] = line[i];
				ips[i - 3] = line[i].substring(0, 17).trim();

			}
		} catch (NegativeArraySizeException e) {
			return new String[0];
		}
		return ips;

	}

	/**
	 * Assembles a <code>String[]</code> to one <code>String</code>, in which
	 * the parts are separated by blanks
	 * 
	 * @param array
	 *            The String array that will be assembled
	 * @return The assembled String
	 */
	public static String getAssembledStringArray(String[] array) {

		String string = "";
		for (String part : array)
			string += part + " ";
		return string.substring(0, string.length() - 1);
	}

	/**
	 * Represents a <code>byte[]</code> in a much more comfortable an readable
	 * way
	 * 
	 * @param bs
	 *            the bytes you want to convert
	 * @return the hex string
	 */
	public static String bytesToHex(byte[] bs) {
		char[] hexChars = new char[2 * bs.length];
		for (int j = 0; j < bs.length; ++j) {
			int v = bs[j] & 0xFF;
			hexChars[2 * j] = hexArray[v >>> 4];
			hexChars[2 * j + 1] = hexArray[v & 0x0F];
		}
		return String.valueOf(hexChars);
	}

	/**
	 * Convert a <code>String</code> to an <code>URL</code> by escaping illegal
	 * characters correctly. <code>URLEndocer.encode();</code> is probably
	 * better, but it is somehow deprecated
	 * 
	 * @param s
	 *            The url to escape
	 * @return The URL object
	 */
	public static URL escapeURL(String s) {
		try {
			String decodedURL = URLDecoder.decode(s, "UTF-8");
			URL url = new URL(decodedURL);
			return (new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), url.getRef())).toURL();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Checks if a <code>String</code> is included in a <code>String[]</code> *
	 * 
	 * @param array
	 *            String array
	 * @param string
	 *            String
	 * @return true if included, false if not
	 */
	public static boolean isIncluded(String[] array, String string) {

		for (String s : array)
			if (s.equals(string))
				return true;
		return false;
	}
}
