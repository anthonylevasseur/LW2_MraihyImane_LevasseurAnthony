package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Client;
import model.Exigence;
import model.Fonctionnalite;
import model.Membre;
import model.STB;
import model.STBInfo;
import model.STBList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import config.AppConfig;

@RestController
public class STBController {

	@RequestMapping(value="/resume")
	public ResponseEntity<STBList> getList() {
		STBList list = new STBList();
		Connection connection = null;
		try {
			connection = AppConfig.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * from stb");
			while (result.next()) {
				int id = result.getInt("id");
				String titre = result.getString("titre");
				String date = result.getString("date");
				String desc = result.getString("description");
				double version = result.getDouble("version");
				STBInfo s = new STBInfo(id, titre, desc, date, version);
				list.addSTB(s);
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<STBList>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/resume/{id}")
	public ResponseEntity<STB> getStbById(@PathVariable("id") int id) {
		STB stb = null;
		Connection connection = null;
		try {
			connection = AppConfig.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(
					"SELECT * from stb where id=" + id);
			STBInfo info = null;
			Client client = null;
			List<Membre> equipe = new ArrayList<Membre>();
			List<Fonctionnalite> fonctionnalites = new ArrayList<Fonctionnalite>();
			int id_client = 0;
			result.next();
			int id_stb = result.getInt("id");
			String titre = result.getString("titre");
			String date = result.getString("date");
			String desc = result.getString("description");
			double version = result.getDouble("version");
			id_client = result.getInt("id_client");
			info = new STBInfo(id_stb, titre, desc, date, version);
			result = stmt.executeQuery(
					"SELECT * from client where id=" + id_client);
			result.next();
			String entite = result.getString("entite");
			String nom = result.getString("nom");
			String prenom = result.getString("prenom");
			String adresse = result.getString("adresse");
			boolean genre = result.getBoolean("genre");
			client = new Client(entite, nom, prenom, genre, adresse);
			result = stmt.executeQuery(
					"SELECT * from equipe where id_stb=" + id);
			Statement stmt2 = connection.createStatement();
			while (result.next()) {
				int id_membre = result.getInt("id_membre");
				ResultSet result2 = stmt2.executeQuery(
						"SELECT * from membre where id=" + id_membre);
				result2.next();
				String nom2 = result2.getString("nom");
				String prenom2 = result2.getString("prenom");
				boolean genre2 = result2.getBoolean("genre");
				equipe.add(new Membre(nom2, prenom2, genre2));
			}
			result = stmt.executeQuery(
					"SELECT * from fonctionnalite where id_stb=" + id);
			while (result.next()) {
				int id_fonc = result.getInt("id");
				String desc_fonc = result.getString("description");
				int priorite = result.getInt("priorite");
				ResultSet result2 = stmt2.executeQuery(
						"SELECT * from exigence where id_fonc=" + id_fonc);
				List<Exigence> exigences = new ArrayList<Exigence>();
				while (result2.next()) {
					String nom2 = result2.getString("nom");
					String desc2 = result2.getString("description");
					int priorite2 = result2.getInt("priorite");
					Exigence e = new Exigence(nom2, priorite2, desc2);
					exigences.add(e);
				}
				fonctionnalites.add(new Fonctionnalite(desc_fonc, priorite, exigences));
			}
			stb = new STB(info, client, equipe, fonctionnalites);
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			stb = null;
		}
		ResponseEntity<STB> resp = new ResponseEntity<STB>(stb, HttpStatus.OK);
		return resp;
	}
	
}
