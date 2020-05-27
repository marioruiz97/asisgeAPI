package com.asisge.apirest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
public class AsisgeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsisgeApiApplication.class, args);

		FirebaseOptions options;
		try {
			FirebaseApp.getInstance();
		} catch (IllegalStateException ex) {
			try {
				String bucketName = System.getenv("ASISGE_STORAGE_BUCKET").replace("gs://", "").replace("/", "");
				String serviceAccountJson = messageWhitespace(System.getenv("ASISGE_SERVICE_ACCOUNT"));
				InputStream serviceAccount = new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8));
				options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setStorageBucket(bucketName)
						.build();
				FirebaseApp.initializeApp(options);
				System.out.println("Firebase iniciado con éxito");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String messageWhitespace(String toFormat) {
		StringBuilder newString = new StringBuilder("");
		for (Character c : toFormat.toCharArray()) {
			if ("00a0".equals(Integer.toHexString(c | 0x10000).substring(1))) {
				newString.append(" ");
			} else {
				newString.append(c);
			}
		}
		return newString.toString();
	}

}
