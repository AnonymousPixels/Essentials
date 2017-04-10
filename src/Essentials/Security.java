/**
 * 
 */
package essentials;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Maximilian
 *
 */
public class Security {

	/**
	 * Encrypts file with the AES-Algorithm
	 * 
	 * @param inputFile
	 *            The path of the file to encrypt
	 * 
	 * @param key
	 *            A 128 bit (16 byte) key to encrypt the file
	 * @param outputFile
	 *            The path were the encrypted file should be saved
	 * @return False if an Exception occurred
	 */
	public static boolean encrypt(String inputFile, String key, File outputFile) {
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * Encrypts strings with the AES-Algorithm
	 * 
	 * @param input
	 *            The string to encrypt
	 * @param key
	 *            A 128 bit (16 byte) key to encrypt the file
	 *
	 * @return False if an Exception occurred
	 */
	public static String encrypt(String input, String key) {
		byte[] outputBytes;
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] inputBytes = input.getBytes();

			outputBytes = cipher.doFinal(inputBytes);

		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			e.printStackTrace();
			return "";
		}

		return new String(outputBytes);

	}

	/**
	 * Decrypts files with the AES-Algorithm
	 * 
	 * @param inputFile
	 *            The path of the file to decrypt
	 * @param key
	 *            A 128 bit (16 byte) key to decrypt the file
	 * @param outputFile
	 *            The path were the decrypted file should be saved
	 * @return False if an Exception occurred
	 */
	public static boolean decrypt(File inputFile, String key, File outputFile) {
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * Decrypts strings with the AES-Algorithm
	 * 
	 * @param input
	 *            The string to decrypt
	 * @param key
	 *            A 128 bit (16 byte) key to decrypt the file
	 *
	 * @return False if an Exception occurred
	 */
	public static String decrypt(String key, String input) {

		byte[] outputBytes;
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] inputBytes = input.getBytes();

			outputBytes = cipher.doFinal(inputBytes);

		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			e.printStackTrace();
			return "";
		}

		return new String(outputBytes);

	}

	/**
	 * Returns the serial number of the selected drive
	 * 
	 * @param drive
	 *            The drive to use
	 * @return the serial number of the drive
	 */
	public static String getDriveSN(String drive) {
		String result = "";
		try {
			File file = File.createTempFile("getdrivesn", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
					+ "Set colDrives = objFSO.Drives\n"
					+ "Set objDrive = colDrives.item(\""
					+ drive
					+ "\")\n"
					+ "Wscript.Echo objDrive.SerialNumber"; // see note
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}

	/**
	 * Returns the serial number of your Motherboard
	 * 
	 * @return the serial number of the MOtherboard
	 */
	public static String getMotherboardSN() {
		String result = "";
		try {
			File file = File.createTempFile("getmotherboardsn", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n"
					+ "   (\"Select * from Win32_BaseBoard\") \n"
					+ "For Each objItem in colItems \n"
					+ "    Wscript.Echo objItem.SerialNumber \n"
					+ "    exit for  ' do the first cpu only! \n" + "Next \n";

			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}

	/**
	 * Will return the MAC address of the NetworkInterface currently in use.
	 * Will return null if something goes wrong.
	 * 
	 * @return The MAC address
	 */
	public static String getMac() {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : ""));
			}
			return sb.toString();
		} catch (UnknownHostException | SocketException e) {
			System.out.println("Can't get MAC address");
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Returns the SHA-256 hash of a given string
	 * 
	 * @param data
	 *            the <code>String</code> to be hashed
	 * @return the hash of the <code>String</code>
	 */
	public static byte[] hash(String data) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(data.getBytes());
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			System.out
					.println("NoSuchAlgorithmException! Can't calculate your hash!");
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Get a unique Hardware Identifier consisting of the serial numbers of the
	 * C drive, the motherboard and the processor identifier.
	 * <p>
	 * The return value will be a 64 character long string that represents a
	 * SHA-256 hash.
	 * <p>
	 * DO NOT use it for stuff like hardware-banning because it is easy to mount
	 * drives to other letters or to change your MAC address. What you can do is
	 * whitelisting. The HWID is basically a SHA-256 hash, therefore it is
	 * impossible to reverse-engineer what's inside.
	 * <p>
	 * It is recommended not to use the MAC address, because it may change if
	 * you connect via an other NetworkInterface.
	 * 
	 * @param useMac
	 *            If it should use the MAC or not. NOT RECOMMENDED
	 * @return The HWID
	 */
	public static String getHWID(boolean useMac) {
		String data = getMotherboardSN() + " " + getDriveSN("C") + " "
				+ System.getenv("PROCESSOR_IDENTIFIER");
		data += (useMac ? getMac() : "");
		byte[] bytes = hash(data);
		return Essentials.bytesToHex(bytes);

	}

}