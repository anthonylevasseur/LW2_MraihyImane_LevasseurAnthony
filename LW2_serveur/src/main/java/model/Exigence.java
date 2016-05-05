package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Exigence {
	
	private String nom;

	private int priorite;
	
	private String desc;
	
	public Exigence() {
		
	}
	
	public Exigence(String nom, int priorite, String desc) {
		if (priorite < 1 || priorite > 10) {
			throw new IllegalArgumentException("priorité invalide");
		}
		this.nom = nom;
		this.priorite = priorite;
		this.desc = desc;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getPriorite() {
		return priorite;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
