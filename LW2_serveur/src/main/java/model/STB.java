package model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class STB {
	
	private STBInfo info;
	
	private Client client;
	
	private List<Membre> equipe;
	
	private List<Fonctionnalite> fonctionnalites;
	
	public STB() {
		
	}

	public STB(STBInfo info, Client client, List<Membre> equipe,
			List<Fonctionnalite> fonctionnalites) {
		if (equipe.size() < 2 || equipe.size() > 7) {
			throw new IllegalArgumentException("une épuipe possède "
					+ "entre 2 et 7 membres");
		}
		this.info = info;
		this.client = client;
		this.equipe = equipe;
		this.fonctionnalites = fonctionnalites;
	}

	public STBInfo getInfo() {
		return info;
	}

	public void setInfo(STBInfo info) {
		this.info = info;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Membre> getEquipe() {
		return equipe;
	}

	public void setEquipe(List<Membre> equipe) {
		this.equipe = equipe;
	}

	public List<Fonctionnalite> getFonctionnalites() {
		return fonctionnalites;
	}

	public void setFonctionnalites(List<Fonctionnalite> fonctionnalites) {
		this.fonctionnalites = fonctionnalites;
	}

}
