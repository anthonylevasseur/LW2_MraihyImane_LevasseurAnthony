package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import config.AppConfig;

@RestController
public class HomeController {

	@RequestMapping(value="/")
	public ResponseEntity<String> home() {
		Connection connection = null;
		try {
			connection = AppConfig.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM stb");
			result.next();
			int r = result.getInt(1);
			connection.close();
			return new ResponseEntity<String>(
					"Le serveur héberge " + r + " STB", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

}
