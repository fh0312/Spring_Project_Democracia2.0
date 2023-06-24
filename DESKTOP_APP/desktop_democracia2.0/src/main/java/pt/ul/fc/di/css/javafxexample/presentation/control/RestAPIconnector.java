package pt.ul.fc.di.css.javafxexample.presentation.control;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestAPIconnector {
	private String BASE_URL;

	public RestAPIconnector(String apiUrl) {
		if (apiUrl == null || apiUrl.equals("")) {
			this.BASE_URL = "http://localhost:8080/api";
		} else {
			this.BASE_URL = apiUrl;
		}
	}

	public String get(String command) {
		try {
			URL urlForGetRequest = new URL(BASE_URL + command);
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			conection.setRequestProperty("DesktopJavaFxApp", "admin");

			int responseCode = conection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				StringBuffer response = new StringBuffer();
				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();				
				
				return response.toString();
				
			} else {
				System.out.println("GET: " + command + " NOT WORKED");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String post(String url, String body) {
		try {
			URL obj = new URL(BASE_URL + url);

			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("DesktopJavaFxApp", "admin");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);

			OutputStream os = postConnection.getOutputStream();
			os.write(body.getBytes());
			os.flush();
			os.close();

			int responseCode = postConnection.getResponseCode();
			System.out.println("POST Response Code :  " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				return response.toString();
			} else {
				System.out.println("POST NOT WORKED");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
