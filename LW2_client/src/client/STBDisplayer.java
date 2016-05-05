package client;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Exigence;
import model.Fonctionnalite;
import model.Membre;
import model.STB;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class STBDisplayer {
	
	private JFrame frame;
	private STB stb;
	private JList<Fonctionnalite> fonc;
	private DefaultListModel<Fonctionnalite> foncModel;
	private JList<Exigence> exig;
	private DefaultListModel<Exigence> exigModel;
	private JButton export;

	public STBDisplayer(STB stb) {
		this.stb = stb;
		createComponents();
		createView();
		createController();
	}
	
	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void createComponents() {
		frame = new JFrame();
		foncModel = new DefaultListModel<Fonctionnalite>();
		fonc = new JList<Fonctionnalite>(foncModel);
		fonc.setCellRenderer(new FonctionnaliteCellRenderer());
		exigModel = new DefaultListModel<Exigence>();
		exig = new JList<Exigence>(exigModel);
		exig.setCellRenderer(new ExigenceCellRenderer());
		export = new JButton("Exporter en fichier XML");
	}
	
	private void createView() {
		frame.setLayout(new GridBagLayout());
		JPanel p = new JPanel(); {
			p.setLayout(new GridBagLayout());
			p.setBorder(BorderFactory.createTitledBorder("Informations"));
			JLabel titre = new JLabel(stb.getInfo().getTitre());
			titre.setHorizontalAlignment(JLabel.CENTER);
			p.add(titre, new GBC(0,0,2,1).weight(1, 1).fill(GBC.BOTH));
			p.add(new JLabel("Date: " + stb.getInfo().getDate()), new GBC(0, 1).weight(1, 1).fill(GBC.BOTH));
			p.add(new JLabel("Version: " + stb.getInfo().getVersion()), new GBC(1, 1).weight(1, 1).fill(GBC.BOTH));
			p.add(new JLabel("Description: " + stb.getInfo().getDesc()), new GBC(0, 2, 2, 1).weight(1, 1).fill(GBC.BOTH));
		}
		frame.add(p, new GBC(0, 0).weight(1, 0).fill(GBC.BOTH));
		
		p = new JPanel(); {
			p.setLayout(new GridBagLayout());
			p.setBorder(BorderFactory.createTitledBorder("Le client"));
			p.add(new JLabel("Nom:" + stb.getClient().getNom()), new GBC(0,0).weight(1, 1).fill(GBC.BOTH));
			p.add(new JLabel("Prénom:" + stb.getClient().getPrenom()), new GBC(1,0).weight(1, 1).fill(GBC.BOTH));
			String genre = (stb.getClient().getGenre()) ? "Homme" : "Femme";
			p.add(new JLabel("Genre:" + genre), new GBC(2,0).weight(1, 1).fill(GBC.BOTH));
			p.add(new JLabel("Entité: " + stb.getClient().getEntite()), new GBC(0, 1, 3, 1).weight(1, 1).fill(GBC.BOTH));
		}
		frame.add(p, new GBC(0, 1).weight(1, 0).fill(GBC.BOTH));
		
		p = new JPanel(); {
			p.setLayout(new GridBagLayout());
			p.setBorder(BorderFactory.createTitledBorder("L'équipe"));
			for (int i = 0; i < stb.getEquipe().size(); ++i) {
				Membre m = stb.getEquipe().get(i);
				String genre = (m.getGenre()) ? "Homme" : "Femme";
				GBC gbc = new GBC(i%3, i/3).weight(1, 0).fill(GBC.BOTH);
				p.add(new JLabel(m.getNom() + " " + m.getPrenom() + " (" 
						+ genre + ")"), gbc);
				
			}
		}
		frame.add(p, new GBC(0, 2).weight(1, 0).fill(GBC.BOTH));
		
		p = new JPanel(); {
			p.setLayout(new GridLayout(0, 2));
			p.setBorder(BorderFactory.createTitledBorder("Les fonctionnalités"));
			p.add(new JScrollPane(fonc));
			p.add(new JScrollPane(exig));
			for (Fonctionnalite f : stb.getFonctionnalites()) {
				foncModel.addElement(f);
			}
		}
		frame.add(p, new GBC(0, 3).weight(1, 1).fill(GBC.BOTH));
		
		p = new JPanel(); {
			p.add(new JLabel("NB: Afin d'obtenir les exigences, veuillez cliquer sur la fonctionnalité vous intéressant."));
		}
		frame.add(p, new GBC(0, 4).weight(1, 0).fill(GBC.BOTH));
		
		p = new JPanel(); {
			p.add(export);
		}
		frame.add(p, new GBC(0, 5).weight(1, 0).fill(GBC.BOTH));
	}
	
	private void createController() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		fonc.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				Fonctionnalite f = fonc.getSelectedValue();
				exigModel.clear();
				for (Exigence e : f.getExigences()) {
					exigModel.addElement(e);
				}
			}
		});
		
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Document doc = getXMLDocument();
				if (doc != null) {
					JFileChooser fc = new JFileChooser();
					FileFilter filter = new FileNameExtensionFilter("Fichier XML (.xml)", "xml");
					fc.setAcceptAllFileFilterUsed(false);
					fc.setFileFilter(filter);
					fc.showSaveDialog(null);
					File f = fc.getSelectedFile();
					if (f != null) {
						String path = f.getAbsolutePath();
						String[] tab = path.split("\\.");
						if (!tab[tab.length-1].equals("xml")) {
							path += ".xml";
						}
						XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
						try {
							out.output(doc, new FileOutputStream(path));
						} catch (IOException e) {
							JOptionPane.showMessageDialog(
									null, 
									e.getMessage(), 
									"Erreur", 
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
	}
	
	private Document getXMLDocument() {
		Element r = new Element("stb");
		r.setNamespace(Namespace.getNamespace("p","http://univ.fr/stb"));
		Document doc = new Document(r);
		Element t = new Element("titre");
		t.setText(stb.getInfo().getTitre());
		r.addContent(t);
		Element v = new Element("version");
		v.setText("" + stb.getInfo().getVersion());
		r.addContent(v);
		Element d = new Element("date");
		d.setText(stb.getInfo().getDate());
		r.addContent(d);
		Element desc = new Element("description");
		desc.setText(stb.getInfo().getDesc());
		r.addContent(desc);
		//Pour client
		Element client = new Element("client");
		Element entite = new Element("entite");
		entite.setText(stb.getClient().getEntite());
		client.addContent(entite);
		//Pour contact
		Element contact = new Element("contact");
		Element nom = new Element("nom");
		nom.setText(stb.getClient().getNom());
		contact.addContent(nom);
		Element prenom = new Element("prenom");
		prenom.setText(stb.getClient().getPrenom());
		contact.addContent(prenom);
		Element gender = new Element("gender");
		gender.setText("" + stb.getClient().getGenre());
		contact.addContent(gender);
		client.addContent(contact);
		Element adresse = new Element("adresse");
		String[] adr = stb.getClient().getAdresse().split(" ");
		Element num = new Element("num");
		num.setText(adr[0]);
		adresse.addContent(num);
		Element rue = new Element("rue");
		String ru = adr[1];
		for (int i = 2; i <= adr.length - 3; ++i) {
			ru += " " + adr[i];
		}
		rue.setText(ru);
		adresse.addContent(rue);
		Element ville = new Element("ville");
		ville.setText(adr[adr.length-1]);
		adresse.addContent(ville);
		Element code = new Element("code");
		code.setText(adr[adr.length-2]);
		adresse.addContent(code);
		client.addContent(adresse);
		r.addContent(client);
		//Pour equipe
		Element equipe = new Element("equipe");
		for(int i = 0; i < stb.getEquipe().size(); ++i) {
			//Pour chaque membre
			Element membre = new Element("contact");
			Element mnom = new Element("nom");
			mnom.setText(stb.getEquipe().get(i).getNom());
			membre.addContent(mnom);
			Element mprenom = new Element("prenom");
			mprenom.setText(stb.getEquipe().get(i).getPrenom());
			membre.addContent(mprenom);
			Element mgender = new Element("gender");
			mgender.setText("" + stb.getEquipe().get(i).getGenre());
			membre.addContent(mgender);
			equipe.addContent(membre);
		}
		r.addContent(equipe);
		//Fonctionnalités
		for(int i = 0; i < stb.getFonctionnalites().size(); ++i) {
			Element fonctio = new Element("fonctionnalite");
			fonctio.setAttribute("prio", "" + stb.getFonctionnalites().get(i).getPriorite());
			Element fdesc = new Element("description");
			fdesc.addContent(stb.getFonctionnalites().get(i).getDesc());
			fonctio.addContent(fdesc);
			List<Exigence> exis = stb.getFonctionnalites().get(i).getExigences();
			for (Exigence exi : exis) {
				Element exigence = new Element("exigence");
				Element id = new Element("identifiant");
				id.setText(exi.getNom());
				exigence.addContent(id);
				Element prior = new Element("priorite");
				prior.setText("" + exi.getPriorite());
				exigence.addContent(prior);
				Element descr = new Element("description");
				descr.setText(exi.getDesc());
				exigence.addContent(descr);
				fonctio.addContent(exigence);
			}
			r.addContent(fonctio);
		}
		return doc;
	}

}
