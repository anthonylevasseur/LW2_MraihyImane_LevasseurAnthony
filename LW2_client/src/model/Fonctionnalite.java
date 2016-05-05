package model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Fonctionnalite {
	
	private String desc;
	
	private int priorite;
	
	private List<Exigence> exigences;
	
	public Fonctionnalite() {
		exigences = new ArrayList<Exigence>();
	}

	public Fonctionnalite(String desc, int priorite, List<Exigence> exigences) {
		if (priorite < 1 || priorite > 10) {
			throw new IllegalArgumentException("priorité invalide");
		}
		if (exigences.size() < 2) {
			throw new IllegalArgumentException("une fonctionnalité possède "
					+ "2 exigences minimum");
		}
		this.desc = desc;
		this.priorite = priorite;
		this.exigences = exigences;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getPriorite() {
		return priorite;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public List<Exigence> getExigences() {
		return exigences;
	}

	public void setExigences(List<Exigence> exigences) {
		this.exigences = exigences;
	}

}
