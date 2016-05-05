package controller;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import validation.STBValidator;
import config.AppConfig;

@RestController
public class DepotController {

	@RequestMapping(value = "/depot", method = RequestMethod.POST)
	public ResponseEntity<String> deposerSTB(@RequestBody String stb) {
		STBValidator val= new STBValidator();
		Connection connection = null;
		try {
			connection = AppConfig.getConnection();
			Statement stmt = connection.createStatement();
			Document d = val.validate_with_DOM(
					new InputSource(new StringReader(stb)));
			if (d == null) {
				return new ResponseEntity<String>("Le fichier n'est pas valide"
						+ "\nMerci de respecter le xsd", 
					HttpStatus.OK);
			}
			for (int i = 0; i < d.getChildNodes().getLength(); ++i) {
				clean(d.getChildNodes().item(i));
			}
			NodeList client = d.getElementsByTagName("client").item(0).getChildNodes();
			String entite = client.item(0).getTextContent();
			String client_nom = client.item(1).getChildNodes().item(0).getTextContent();
			String client_prenom = client.item(1).getChildNodes().item(1).getTextContent();
			String client_genre = client.item(1).getChildNodes().item(2).getTextContent();
			NodeList adr = client.item(2).getChildNodes();
			String client_adresse = adr.item(0).getTextContent() + " " 
					+ adr.item(1).getTextContent() + " " + adr.item(3).getTextContent()
					+ " " + adr.item(2).getTextContent();
			String request = "SELECT id FROM client where nom=\'" + client_nom 
					+ "\' AND prenom=\'" + client_prenom + "\' AND entite=\'" 
					+ entite + "\';";
			ResultSet result = stmt.executeQuery(request);
			int id_client;
			if (result.next()) {
				id_client = result.getInt("id");
			} else {
				stmt.executeUpdate(
						"INSERT INTO client(entite,nom,prenom,genre,adresse) "
						+ "VALUES(\'"+entite+"\',\'"+client_nom+"\',\'"+client_prenom+"\',\'"
						+client_genre+"\',\'"+client_adresse+"\');");
				result = stmt.executeQuery(request);
				result.next();
				id_client = result.getInt("id");
			}
			
			String titre = d.getElementsByTagName("titre").item(0).getTextContent();
			String version = d.getElementsByTagName("version").item(0).getTextContent();
			String date = d.getElementsByTagName("date").item(0).getTextContent();
			String desc_stb = d.getElementsByTagName("description").item(0).getTextContent();
			stmt.executeUpdate(
					"INSERT INTO stb(titre,version,date,description,id_client) "
					+"VALUES(\'"+titre+"\',"+version+",\'"+date+"\',\'"
					+desc_stb+"\'," + id_client + ");");
			result = stmt.executeQuery(
					"SELECT MAX(id) FROM stb;");
			result.next();
			int id_stb = result.getInt(1);
			
			NodeList equipe = d.getElementsByTagName("equipe").item(0).getChildNodes();
			for (int i = 0; i < equipe.getLength(); ++i) {
				NodeList n = equipe.item(i).getChildNodes();
				String nom_membre = n.item(0).getTextContent();
				String prenom_membre = n.item(1).getTextContent();
				String genre_membre = n.item(2).getTextContent();
				result = stmt.executeQuery("SELECT id FROM membre WHERE nom=\'" 
						+ nom_membre + "\' AND prenom=\'" + prenom_membre + "\'");
				int id_membre;
				if (result.next()) {
					id_membre = result.getInt("id");
				} else {
					stmt.executeUpdate(
							"INSERT INTO membre(nom, prenom, genre) "
							+"VALUES(\'"+nom_membre+"\',\'"+prenom_membre+"\',"+genre_membre+");");
					result = stmt.executeQuery(
							"SELECT MAX(id) FROM membre;");
					result.next();
					id_membre = result.getInt(1);
				}
				stmt.executeUpdate(
						"INSERT INTO equipe(id_stb, id_membre) "
						+"VALUES("+id_stb+","+id_membre+");");
			}
			
			NodeList fonctio = d.getElementsByTagName("fonctionnalite");
			for (int i = 0; i < fonctio.getLength(); ++i) {
				Node n = fonctio.item(i);
				String desc_fonc = n.getChildNodes().item(0).getTextContent();
				int prio_fonc = Integer.valueOf(n.getAttributes().item(0).getTextContent());
				stmt.executeUpdate(
						"INSERT INTO fonctionnalite(description, priorite, id_stb) "
						+"VALUES(\'"+desc_fonc+"\',"+prio_fonc+","+id_stb+");");
				result = stmt.executeQuery(
						"SELECT MAX(id) FROM fonctionnalite;");
				result.next();
				int id_fonc = result.getInt(1);
				for(int j = 1; j < n.getChildNodes().getLength(); ++j) {
					Node exi = n.getChildNodes().item(j);
					String nom_exi = exi.getChildNodes().item(0).getTextContent();
					String prio_exi = exi.getChildNodes().item(1).getTextContent();
					String desc_exi = exi.getChildNodes().item(2).getTextContent();
					stmt.executeUpdate(
							"INSERT INTO exigence(nom, description, priorite, id_fonc) "
							+"VALUES(\'"+nom_exi+"\',\'"+desc_exi+"\',"+prio_exi+","+id_fonc+");");
				}
			}
			connection.close();
			return new ResponseEntity<String>("<html>STB ajoutée avec succès!"
					+ "<br>Identifiant: " + id_stb, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	private void clean(Node node) {
		NodeList childNodes = node.getChildNodes();
		for(int n = childNodes.getLength()-1; n>=0; n--) {
			Node child = childNodes.item(n);
			short nodeType = child.getNodeType();
			if (nodeType == Node.ELEMENT_NODE) {
				clean(child);
			} else if (nodeType == Node.TEXT_NODE) {
				String trimmedNodeVal = child.getNodeValue().trim();
				if (trimmedNodeVal.length() == 0) node.removeChild(child);
				else child.setNodeValue(trimmedNodeVal);
			} else if (nodeType == Node.COMMENT_NODE) {
				node.removeChild(child);
			}
		}
	}
	
}
