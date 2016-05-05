package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Membre {
	
	private String nom;

	private String prenom;
	
	private boolean genre;

	public Membre() {
		
	}
	
	public Membre(String nom, String prenom, boolean genre) {
		this.nom = nom;
		this.prenom = prenom;
		this.genre = genre;
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

}
