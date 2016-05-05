package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class STBInfo {
	public STBInfo() {
		
	}
	public STBInfo(int id, String titre, String desc, String date, double version) {
		this.id = id;
		this.titre = titre;
		this.desc = desc;
		this.date = date;
		this.version = version;
	}
	private int id;
	private String titre;
	private String desc;
	private String date;
	private double version;
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getVersion() {
		return version;
	}
	public void setVersion(double version) {
		this.version = version;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
