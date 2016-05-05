package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Client {
	
	private String entite;
	
	private String nom;
	
	private String prenom;
	
	private boolean genre;
	
	private String adresse;
	
	public Client() {
		
	}
	
	public Client(String entite, String nom, String prenom, boolean genre,
			String adresse) {
		this.entite = entite;
		this.nom = nom;
		this.prenom = prenom;
		this.genre = genre;
		this.adresse = adresse;
	}

	public String getEntite() {
		return entite;
	}

	public void setEntite(String entite) {
		this.entite = entite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public boolean getGenre() {
		return genre;
	}

	public void setGenre(boolean genre) {
		this.genre = genre;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

}
