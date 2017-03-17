package codeTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import essentials.Security;

/**
 * @author Maximilian
 *
 */
public class CodeTracker {

	public static void main(String[] args) {
		String user = Security.getHWID(false);
		System.out.println(user);
		sendInfo(user, "test", "test", "1");
	}

	public static void sendInfo(String user, String info, String program, String ver) {
		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(
						java.security.cert.X509Certificate[] cs,
						String authType) {
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] cs,
						String authType) {
				}
			} };
			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc
						.getSocketFactory());
			} catch (Exception e) {
			}

			URL url = new URL(
					"https://grunzwanzling.me/addons/addon-tracker.php?pid="
							+ program + "&ver=" + ver + "&user=" + user
							+ "&inf=" + info + "&typ=p");
			System.out.println(url);
			// Essentials.sendHTTPRequest();

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			in.readLine();
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
