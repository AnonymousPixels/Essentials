/**
 * 
 */
package essentials;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * A simple class for a simple log
 * 
 * @version 1.0
 * @author Maximilian von Gaisberg
 *
 */
public class SimpleLog {

	static File file;
	static SimpleDateFormat df;
	static boolean useTimestamp;

	/**
	 * 
	 * @param file
	 *            The File where the Log should be saved to
	 * @param useSameFile
	 *            If false, there will be a new file for every launch
	 * @param useTimestamp
	 *            If true, there will be a timestamp in front of every entry
	 * @param startText
	 *            The text that will be printed in the log when the Log is
	 *            initialized
	 * @throws IOException
	 */
	public SimpleLog(File file, boolean useSameFile, boolean useTimestamp,
			String startText) throws IOException {
		df = new SimpleDateFormat("dd.MM.yyyy_hh:mm:ss");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		SimpleLog.useTimestamp = useTimestamp;
		if (!useSameFile) {
			SimpleLog.file = new File(file.getPath() + "_" + df.format(time)
					+ ".txt");
		} else {
			SimpleLog.file = file;
		}
		if (!file.exists())
			file.createNewFile();
		this.log(startText);

	}

	/**
	 * Add a new entry to the logfile
	 * 
	 * @param s
	 *            The String, that will be written in the file
	 * @return False, if an IOException has occurred
	 */
	public boolean log(String s) {
		try {
			Timestamp time = new Timestamp(System.currentTimeMillis());

			FileWriter out = new FileWriter(file, true);
			if (useTimestamp) {
				out.append((CharSequence) df.format(time) + " ");
				System.out.println(df.format(time) + "  ");
			}
			out.append(s + "\n\r");
			out.close();
			System.out.println(s);

		} catch (IOException e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	public boolean logStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		try {
			Timestamp time = new Timestamp(System.currentTimeMillis());

			FileWriter out = new FileWriter(file, true);
			if (useTimestamp) {
				out.append((CharSequence) df.format(time) + " ");
				System.out.println(df.format(time) + "  ");
			}
			String s = sw.toString();
			out.append(s + "\n\r");
			out.close();
			System.err.println(s);

		} catch (IOException f) {
			f.printStackTrace();
			return false;
		}
		return true;
	}

}
