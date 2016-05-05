package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import config.AppConfig;

@RestController
public class SupprimerController {
	
	@RequestMapping(value = "/supprime/{id}")
	public ResponseEntity<String> supprimerStb(@PathVariable("id") int id) {
		Connection connection = null;
		try {
			connection = AppConfig.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet result = 
				stmt.executeQuery("SELECT id FROM fonctionnalite where id_stb=" + id);
			while (result.next()) {
				int id_fonc = result.getInt("id");
				Statement stmt2 = connection.createStatement();
				stmt2.executeUpdate("DELETE FROM exigence where id_fonc=" + id_fonc);
				stmt2.executeUpdate("DELETE FROM fonctionnalite where id=" + id_fonc);
			}
			stmt.executeUpdate("DELETE FROM equipe where id_stb=" + id);
			stmt.executeUpdate("DELETE FROM stb where id=" + id);
			connection.close();
			return new ResponseEntity<String>("STB supprimée avec succès", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

}
