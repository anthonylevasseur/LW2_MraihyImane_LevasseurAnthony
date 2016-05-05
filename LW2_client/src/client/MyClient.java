package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.STB;
import model.STBInfo;
import model.STBList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.web.client.RestTemplate;

public class MyClient {
	
	private final String url;
	
	private JFrame frame;
	private JTabbedPane tab;
	private RestTemplate restTemplate;
	
	private JLabel label;
	
	private File f;
	private JTextField file;
	private JButton choose;
	private JButton validate;
	
	private JList<STBInfo> list;
	private DefaultListModel<STBInfo> model;
	private JPopupMenu popup;
	private JMenuItem afficher;
	private JMenuItem supprimer;
	
	private JTextField titre;
	private JTextField date;
	private JTextField version;
	private JEditorPane description;
	private JTextField client_entite;
	private JTextField client_nom;
	private JTextField client_prenom;
	private JComboBox<String> client_genre;
	private JTextField client_num;
	private JTextField client_rue;
	private JTextField client_ville;
	private JTextField client_code;
	private JTable equipe;
	private DefaultTableModel equipeModel;
	private JTable fonc;
	private DefaultTableModel foncModel;
	private JTable exig;
	private DefaultTableModel exigModel;
	private JButton creer;
	private JButton creerSans;
	private JButton clear;
	private JPopupMenu equipePopup;
	private JPopupMenu foncPopup;
	private JPopupMenu exigPopup;
	private JMenuItem ajouterMembre;
	private JMenuItem supprimerMembre;
	private JMenuItem ajouterFonc;
	private JMenuItem supprimerFonc;
	private JMenuItem ajouterExig;
	private JMenuItem supprimerExig;
	private Map<Integer, List<Object[]>> map;
	private int num = 1;
	
	public MyClient(String url) {
		this.url = url;
		createView();
		placeComponents();
		createController();
		String s = restTemplate.getForObject(url, String.class);
		label.setText(s);
	}
	
	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void createView() {
		frame = new JFrame("Client");
		frame.setPreferredSize(new Dimension(600, 600));
		tab = new JTabbedPane();
		restTemplate = new RestTemplate();
		
		label = new JLabel();
		
		file = new JTextField();
		file.setEditable(false);
		choose = new JButton("Choisir une stb");
		validate = new JButton("Déposer");
		validate.setEnabled(false);
		
		list = new JList<STBInfo>();
		model = new DefaultListModel<STBInfo>();
		list.setModel(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new STBCellRenderer());
		popup = new JPopupMenu();
		afficher = new JMenuItem("Afficher en détails");
		supprimer = new JMenuItem("Supprimer la stb");
		popup.add(afficher);
		popup.add(supprimer);
		
		titre = new JTextField();
		date = new JTextField();
		version = new JTextField();
		description = new JEditorPane();
		client_entite = new JTextField();
		client_nom = new JTextField();
		client_prenom = new JTextField();
		client_genre = new JComboBox<String>();
		client_genre.addItem("Femme");
		client_genre.addItem("Homme");
		client_num = new JTextField();
		client_rue = new JTextField();
		client_ville = new JTextField();
		client_code = new JTextField();
		equipeModel = new DefaultTableModel();
		equipeModel.addColumn("Nom");
		equipeModel.addColumn("Prénom");
		equipeModel.addColumn("Genre");
		equipeModel.setRowCount(2);
		equipe = new JTable(equipeModel);
		TableColumn genreColumn = equipe.getColumnModel().getColumn(2);
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("Femme");
		comboBox.addItem("Homme");
		genreColumn.setCellEditor(new DefaultCellEditor(comboBox));
		foncModel = new MyTableModel();
		foncModel.addColumn("N°");
		foncModel.addColumn("Priorité");
		foncModel.addColumn("Description");
		Object[] tab = {1,"",""};
		foncModel.addRow(tab);
		fonc = new JTable(foncModel);
		fonc.setRowHeight(40);
		fonc.getColumn("Description").setCellEditor(new MyTableCellEditor());
		fonc.getColumn("Description").setCellRenderer(new MyTableCellRenderer());
		List<Object[]> l = new ArrayList<Object[]>();
		Object[] t = {1, "", ""};
		Object[] t2 = {1, "", ""};
		l.add(t);
		l.add(t2);
		map = new HashMap<Integer, List<Object[]>>();
		map.put(1, l);
		TableColumn prioColumn = fonc.getColumnModel().getColumn(1);
		JComboBox<Integer> comboBox2 = new JComboBox<Integer>();
		for (int i = 1; i <= 10; ++i) {
			comboBox2.addItem(i);
		}
		prioColumn.setCellEditor(new DefaultCellEditor(comboBox2));
		exigModel = new MyTableModel();
		exigModel.addColumn("N° fonctionnalité");
		exigModel.addColumn("Identifiant");
		exigModel.addColumn("Priorité");
		exigModel.addColumn("Description");
		exig = new JTable(exigModel);
		exig.setRowHeight(40);
		exig.getColumn("Description").setCellEditor(new MyTableCellEditor());
		exig.getColumn("Description").setCellRenderer(new MyTableCellRenderer());
		prioColumn = exig.getColumnModel().getColumn(2);
		prioColumn.setCellEditor(new DefaultCellEditor(comboBox2));
		creer = new JButton("Créer et déposer la STB");
		creerSans = new JButton("Créer le fichier XML");
		clear = new JButton("Vider le formulaire");
		equipePopup = new JPopupMenu();
		ajouterMembre = new JMenuItem("Ajouter un membre");
		supprimerMembre = new JMenuItem("Supprimer le membre");
		equipePopup.add(ajouterMembre);
		equipePopup.add(supprimerMembre);
		equipe.setComponentPopupMenu(equipePopup);
		foncPopup = new JPopupMenu();
		ajouterFonc = new JMenuItem("Ajouter une fonctionnalité");
		supprimerFonc = new JMenuItem("Supprimer la fonctionnalité");
		foncPopup.add(ajouterFonc);
		foncPopup.add(supprimerFonc);
		fonc.setComponentPopupMenu(foncPopup);
		exigPopup = new JPopupMenu();
		ajouterExig = new JMenuItem("Ajouter une exigence");
		supprimerExig = new JMenuItem("Supprimer l'exigence");
		exigPopup.add(ajouterExig);
		exigPopup.add(supprimerExig);
		exig.setComponentPopupMenu(exigPopup);
	}
	
	private void placeComponents() {
		JPanel p = new JPanel(new BorderLayout()); {
			JPanel q = new JPanel(); {
				label.setHorizontalAlignment(JLabel.CENTER);
				q.add(label);
				label.setBorder(BorderFactory.createEtchedBorder());
				q.setBackground(Color.WHITE);
			}
			p.add(q, BorderLayout.NORTH);
			
			q = new JPanel(new BorderLayout()); {
				q.setSize(new Dimension(300, 300));
				URL u = getClass().getResource("/img.jpg");
				ImageIcon img = new ImageIcon(u);
				Image scaledImage = img.getImage().getScaledInstance(q.getWidth(),q.getHeight(),Image.SCALE_SMOOTH);
				JLabel l = new JLabel(new ImageIcon(scaledImage));
				l.setHorizontalAlignment(JLabel.CENTER);
				q.add(l, BorderLayout.CENTER);
				q.setBackground(Color.WHITE);
				
			}
			p.add(q, BorderLayout.CENTER);
			
			q = new JPanel(); {
				JLabel l = new JLabel("@Mraihy Imane - Levasseur Anthony");
				l.setHorizontalAlignment(JLabel.CENTER);
				q.add(l);
				q.setBackground(Color.WHITE);
			}
			p.add(q, BorderLayout.SOUTH);
			p.setBackground(Color.WHITE);
		}
		tab.add("Accueil", p);
		
		p = new JPanel(); {
			p.setLayout(new GridBagLayout());
			JPanel q = new JPanel(); {
				q.setLayout(new GridBagLayout());
				q.setBorder(BorderFactory.createTitledBorder("Informations"));
				q.add(new JLabel("Titre:"), new GBC(0,0).weight(1, 1).fill(GBC.BOTH));
				q.add(titre, new GBC(1,0, 2, 1).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Date (jj/mm/aaaa):"), new GBC(0,1).weight(1, 1).fill(GBC.BOTH));
				q.add(date, new GBC(1,1).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Version: (*Nombre)"), new GBC(0,2).weight(1, 1).fill(GBC.BOTH));
				q.add(version, new GBC(1,2).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Description:"), new GBC(0,3).weight(1, 1).fill(GBC.BOTH));
				q.add(new JScrollPane(description), new GBC(1,3).weight(1, 1).fill(GBC.BOTH));
			}
			p.add(q, new GBC(0, 0).weight(1, 0).fill(GBC.BOTH));
			
			q = new JPanel(); {
				q.setLayout(new GridBagLayout());
				q.setBorder(BorderFactory.createTitledBorder("Le client"));
				q.add(new JLabel("Entité:"), new GBC(0,0).fill(GBC.BOTH));
				q.add(client_entite, new GBC(1,0).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Sexe:"), new GBC(2,0).fill(GBC.BOTH));
				q.add(client_genre, new GBC(3,0).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Nom:"), new GBC(0,1).fill(GBC.BOTH));
				q.add(client_nom, new GBC(1,1).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Prénom:"), new GBC(2,1).fill(GBC.BOTH));
				q.add(client_prenom, new GBC(3,1).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Numéro: (*Nombre)"), new GBC(0,2).fill(GBC.BOTH));
				q.add(client_num, new GBC(1,2).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Rue:"), new GBC(2,2).fill(GBC.BOTH));
				q.add(client_rue, new GBC(3,2).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Ville:"), new GBC(0,3).fill(GBC.BOTH));
				q.add(client_ville, new GBC(1,3).weight(1, 1).fill(GBC.BOTH));
				q.add(new JLabel("Code: (*Nombre à 5 chiffres)"), new GBC(2,3).fill(GBC.BOTH));
				q.add(client_code, new GBC(3,3).weight(1, 1).fill(GBC.BOTH));
			}
			p.add(q, new GBC(0, 1).weight(1, 0).fill(GBC.BOTH));
			
			q = new JPanel(); {
				q.setLayout(new GridBagLayout());
				q.setBorder(BorderFactory.createTitledBorder("L'équipe"));
				q.add(new JScrollPane(equipe), new GBC(0,0).weight(1, 1).fill(GBC.BOTH));
			}
			p.add(q, new GBC(0, 2).weight(1, 2).fill(GBC.BOTH));
			
			q = new JPanel(); {
				q.setLayout(new GridLayout(0,2));
				q.setBorder(BorderFactory.createTitledBorder("Les fonctionnalités"));
				q.add(new JScrollPane(fonc));
				q.add(new JScrollPane(exig));
			}
			p.add(q, new GBC(0, 3).weight(1, 3).fill(GBC.BOTH));
			
			q = new JPanel(); {
				q.setLayout(new GridBagLayout());
				q.setBorder(BorderFactory.createEtchedBorder());
				q.add(new JLabel("N.B: Pour les tables, sélectionner entièrement la ligne en cours de modification avant de passer à une autre pour tenir compte des modifications"), new GBC(0, 0, 3, 1));
				q.add(new JLabel("N.B2: Avant toute utilisation de bouton veillez à ce que les lignes séléctionnées des tableaux le soient entièrement "), new GBC(0, 1, 3, 1));
				JPanel r = new JPanel(); {
					r.add(creer);
				}
				q.add(r, new GBC(0, 2).weight(1, 1));
				r = new JPanel(); {
					r.add(creerSans);
				}
				q.add(r, new GBC(1, 2).weight(1, 1));
				r = new JPanel(); {
					r.add(clear);
				}
				q.add(r, new GBC(2, 2).weight(1, 1));
			}
			p.add(q, new GBC(0, 4).weight(1, 0).fill(GBC.BOTH));
		}
		
		tab.add("Créer une stb", p);
		
		p = new JPanel(new BorderLayout()); {
			JPanel q = new JPanel(new GridLayout(0, 4)); {
				q.add(new JLabel());
				file.setBackground(Color.WHITE);
				q.add(file);
				JPanel r = new JPanel();  {
					r.add(choose);
					r.setBackground(Color.WHITE);
				}
				q.add(r);
				q.add(new JLabel());
				q.setBackground(Color.WHITE);
			}
			p.add(q, BorderLayout.NORTH);
			
			q = new JPanel(new BorderLayout()); {
				q.setSize(new Dimension(150, 100));
				URL u = getClass().getResource("/depot.png");
				ImageIcon img = new ImageIcon(u);
				Image scaledImage = img.getImage().getScaledInstance(q.getWidth(),q.getHeight(),Image.SCALE_SMOOTH);
				JLabel l = new JLabel(new ImageIcon(scaledImage));
				l.setHorizontalAlignment(JLabel.CENTER);
				q.add(l, BorderLayout.CENTER);
				q.setBackground(Color.WHITE);
			}
			p.add(q, BorderLayout.CENTER);
			
			q = new JPanel(); {
				q.add(validate);
				q.setBackground(Color.WHITE);
			}
			p.add(q, BorderLayout.SOUTH);
		}
		p.setBackground(Color.WHITE);
		tab.add("Déposer une stb", p);
		
		p = new JPanel(); {
			p.setLayout(new GridBagLayout());
			p.add(new JScrollPane(list), 
				new GBC(0,0).weight(1, 1).fill(GBC.BOTH).insets(5));
		}
		tab.add("Consulter les stb", p);
		
		frame.add(tab);
	}
	
	private void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tab.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tab.getSelectedIndex() == 0) {
				    String s = restTemplate.getForObject(url, String.class);
					label.setText(s);
				} else if (tab.getSelectedIndex() == 3) {
					model.clear();
				    STBList result = restTemplate.getForObject(url + "resume", STBList.class);
				    for (STBInfo s : result.getList()) {
				    	model.addElement(s);
				    }
				}
			}
		});
		
		list.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent evt) {
		    	int index = list.locationToIndex(evt.getPoint());
		        if (evt.getClickCount() == 2 && index != -1) {
		            STBInfo s = model.getElementAt(index);
		            int id = s.getId();
		            Map<String, Integer> params = new HashMap<String, Integer>();
		            params.put("id", id);
		            STB result = restTemplate.getForObject(url + "resume/{id}", STB.class, params);
		            if (result != null) {
		            	new STBDisplayer(result).display();
		            } else {
		            	JOptionPane.showMessageDialog(null, "STB indisponible", "Erreur", JOptionPane.ERROR_MESSAGE);
		            }
		        } else if (evt.getButton() == MouseEvent.BUTTON3) {
		        	if (index == -1) {
		        		list.setComponentPopupMenu(null);
		        	} else {
		        		list.getSelectionModel().setSelectionInterval(index, index);
		        		list.setComponentPopupMenu(popup);
		        	}
		        }
		    }
		});
		
		afficher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				STBInfo s = model.getElementAt(index);
	            int id = s.getId();
	            Map<String, Integer> params = new HashMap<String, Integer>();
	            params.put("id", id);
	            STB result = restTemplate.getForObject(url + "resume/{id}", STB.class, params);
	            if (result != null) {
	            	new STBDisplayer(result).display();
	            } else {
	            	JOptionPane.showMessageDialog(null, "STB indisponible", "Erreur", JOptionPane.ERROR_MESSAGE);
	            }
			}
			
		});
		
		supprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int r = JOptionPane.showConfirmDialog(
						null, 
						"Etes-vous certain de vouloir supprimer cette stb?", 
						"Confirmation", 
						JOptionPane.YES_NO_OPTION);
				if (r == JOptionPane.YES_OPTION) {
		            int index = list.getSelectedIndex();
		            STBInfo stb = model.getElementAt(index);
		            int id = stb.getId();
		            Map<String, Integer> params = new HashMap<String, Integer>();
		            params.put("id", id);
		            String result = restTemplate.getForObject(url + "supprime/{id}", String.class, params);
		            JOptionPane.showMessageDialog(
		            		null, 
		            		result, 
		            		"Information", 
		            		JOptionPane.INFORMATION_MESSAGE);
		            model.clear();
				    STBList result2 = restTemplate.getForObject(url + "resume", STBList.class);
				    for (STBInfo s : result2.getList()) {
				    	model.addElement(s);
				    }
				}
			}	
		});
		
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier xml (.xml)", "xml");
				fc.addChoosableFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);
				fc.setFileFilter(filter);
				fc.showOpenDialog(null);
				f = fc.getSelectedFile();
				if (f != null) {
					if (f.exists()) {
						file.setText(f.getAbsolutePath());
						validate.setEnabled(true);
					} else {
						file.setText("");
						validate.setEnabled(false);
					}
				}
			}
		});
		
		validate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				byte[] encoded;
				String stb = null;
				try {
					encoded = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
					stb = new String(encoded);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String result = restTemplate.postForObject(url + "depot", stb, String.class);
					JOptionPane.showMessageDialog(null, result, 
						"Résultat", JOptionPane.INFORMATION_MESSAGE);
				file.setText("");
			}
			
		});
		
		ajouterMembre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (equipeModel.getRowCount() == 7) {
					JOptionPane.showMessageDialog(
							null, 
							"Une équipe contient 7 membres au maximum", 
							"Attention", 
							JOptionPane.WARNING_MESSAGE);
				} else {
					Object[] tab = {"", "", ""};
					equipeModel.addRow(tab);
				}
			}
		});
		
		ajouterFonc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				++num;
				Object[] tab = {num, "", ""};
				foncModel.addRow(tab);
				List<Object[]> l = new ArrayList<Object[]>();
				Object[] t = {num, "", ""};
				Object[] t2 = {num, "", ""};
				l.add(t);
				l.add(t2);
				map.put(num, l);
			}
		});
		
		ajouterExig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int idFonc = (Integer) exigModel.getValueAt(0, 0);
				Object[] tab = {idFonc, "", ""};
				exigModel.addRow(tab);
				Object[] t = {idFonc, "", ""};
				map.get(idFonc).add(t);
			}
		});
		
		supprimerMembre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (equipeModel.getRowCount() == 2) {
					JOptionPane.showMessageDialog(
							null, 
							"Une équipe contient 2 membres au minimum", 
							"Attention", 
							JOptionPane.WARNING_MESSAGE);
				} else {
					int n = equipe.getSelectedRow();
					if (n >= 0) {
						equipeModel.removeRow(equipe.getSelectedRow());
					} else {
						JOptionPane.showMessageDialog(
								null, 
								"Veuillez sélectionner une ligne avant de supprimer", 
								"Attention", 
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		supprimerFonc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (fonc.getRowCount() == 1) {
					JOptionPane.showMessageDialog(
							null, 
							"Vous n'avez pas le droit de supprimer\n" + "NB: une stb contient au mnimum une fonctionnalité", 
							"Attention", 
							JOptionPane.WARNING_MESSAGE);
					
				} else {
					int n = fonc.getSelectedRow();
					if (n >= 0) {
						int v = (Integer) foncModel.getValueAt(n, 0);
						map.remove(v);
						foncModel.removeRow(n);
					} else {
						JOptionPane.showMessageDialog(
								null, 
								"Veuillez sélectionner une ligne avant de supprimer", 
								"Attention", 
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		supprimerExig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (exigModel.getRowCount() == 2) {
					JOptionPane.showMessageDialog(
							null, 
							"Une fonctionnalité possède au minimum 2 exigences", 
							"Attention", 
							JOptionPane.WARNING_MESSAGE);
				} else {
					int n = exig.getSelectedRow();
					if (n >= 0) {
						int id = (Integer) exigModel.getValueAt(n, 0);
						List<Object[]> l = map.get(id);
						l.remove(n);
						exigModel.removeRow(exig.getSelectedRow());
					} else {
						JOptionPane.showMessageDialog(
								null, 
								"Veuillez sélectionner une ligne avant de supprimer", 
								"Attention", 
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		fonc.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int n = fonc.getSelectedRow();
				while (exigModel.getRowCount() > 0) {
					exigModel.removeRow(0);
				}
				if (n >= 0) {
					int id = (Integer) foncModel.getValueAt(n, 0);
					List<Object[]> l = map.get(id);
					if (l != null) {
						for (Object[] t : l) {
							exigModel.addRow(t);
						}
					}
				}
			}
		});
		
		exigModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				if (arg0.getType() == TableModelEvent.UPDATE) {
					int n = exig.getSelectedRow();
					if (n >= 0) {
						int idFonc = (Integer) exig.getValueAt(0, 0);
						List<Object[]> l = map.get(idFonc);
						Object[] t = new Object[exigModel.getColumnCount()];
						for (int i = 0; i < exig.getColumnCount(); ++i) {
							t[i] = exigModel.getValueAt(n, i);
						}
						l.set(n, t);
					}
				}
			}
		});
		
		creer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String xml = null;
				try {
					xml = new XMLOutputter().outputString(getXMLDocument());
					return;
				} catch (Exception ex) {
					displayError("Tous les champs ne sont pas remplis et valides");
				}
				if (xml != null) {
					String result = restTemplate.postForObject(url + "depot", xml, String.class);
					JOptionPane.showMessageDialog(null, result, 
						"Résultat", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		creerSans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Document doc = null;
				try {
					doc = getXMLDocument();
				} catch (Exception ex) {
					displayError("Tous les champs ne sont pas remplis et valides");
					return;
				}
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
							displayError(e.getMessage());
						}
					}
				}
			}
		});
		
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				titre.setText("");
				date.setText("");
				version.setText("");
				description.setText("");
				client_entite.setText("");
				client_nom.setText("");
				client_prenom.setText("");
				client_num.setText("");
				client_rue.setText("");
				client_ville.setText("");
				client_code.setText("");
				while (equipeModel.getRowCount() > 2) {
					equipeModel.removeRow(0);
				}
				for (int i = 0; i < equipeModel.getRowCount(); ++i) {
					for (int j = 0; j < equipeModel.getColumnCount(); ++j) {
						equipeModel.setValueAt("", i, j);
					}
				}
				while (foncModel.getRowCount() > 1) {
					foncModel.removeRow(0);
				}
				foncModel.setValueAt(1, 0, 0);
				foncModel.setValueAt("", 0, 1);
				foncModel.setValueAt("", 0, 2);
				while (exigModel.getRowCount() > 0) {
					exigModel.removeRow(0);
				}
				List<Object[]> l = new ArrayList<Object[]>();
				Object[] t = {1, "", ""};
				Object[] t2 = {1, "", ""};
				l.add(t);
				l.add(t2);
				map.clear();
				map.put(1, l);
				fonc.clearSelection();
			}
		});
	}
	
	private static void displayError(String msg) {
		JOptionPane.showMessageDialog(
				null, 
				msg, 
				"Erreur", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	private Document getXMLDocument() {
		Element r = new Element("stb");
		r.setNamespace(Namespace.getNamespace("p","http://univ.fr/stb"));
		Document doc = new Document(r);
		if (titre.getText().equals("")
				|| date.getText().equals("")
				|| version.getText().equals("")
				|| description.getText().equals("")) {
			displayError("La rubrique Informations n'est pas valide");
			return null;
		}
		Element t = new Element("titre");
		t.setText(replace(titre.getText()));
		r.addContent(t);
		try {
			Double.valueOf(version.getText());
			Element v = new Element("version");
			v.setText(version.getText());
			r.addContent(v);
		} catch (NumberFormatException nfe) {
			displayError("La version doit être un réel");
			return null;
		}
		try {
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			f.parse(date.getText());
			Element d = new Element("date");
			d.setText(date.getText());
			r.addContent(d);
		} catch (Exception ex) {
			displayError("La date n'est pas valide");
			return null;
		}
		Element desc = new Element("description");
		desc.setText(replace(description.getText()));
		r.addContent(desc);
		//Pour client
		if (client_nom.getText().equals("") || client_entite.getText().equals("")
				|| client_prenom.getText().equals("")
				|| client_genre.getSelectedItem().toString().equals("") 
				|| client_num.getText().equals("") 
				|| client_rue.getText().equals("") 
				|| client_ville.getText().equals("") 
				|| client_code.getText().equals("")) {
			displayError("La rubrique client n'est pas valide");
			return null;
		}
		Element client = new Element("client");
		Element entite = new Element("entite");
		entite.setText(replace(client_entite.getText()));
		client.addContent(entite);
		//Pour contact
		Element contact = new Element("contact");
		Element nom = new Element("nom");
		nom.setText(replace(client_nom.getText()));
		contact.addContent(nom);
		Element prenom = new Element("prenom");
		prenom.setText(replace(client_prenom.getText()));
		contact.addContent(prenom);
		Element gender = new Element("gender");
		String bool = client_genre.getSelectedItem().toString().equals("Homme") ? "true" : "false";
		gender.setText(bool);
		contact.addContent(gender);
		client.addContent(contact);
		Element adresse = new Element("adresse");
		try {
			Integer.valueOf(client_num.getText());
			Element num = new Element("num");
			num.setText(client_num.getText());
			adresse.addContent(num);
		} catch (NumberFormatException nfe){
			displayError("Le numéro de la rue doit être un entier");
			return null;
		}
		Element rue = new Element("rue");
		rue.setText(replace(client_rue.getText()));
		adresse.addContent(rue);
		Element ville = new Element("ville");
		ville.setText(replace(client_ville.getText()));
		adresse.addContent(ville);
		try {
			int c = Integer.valueOf(client_code.getText());
			if (c < 10000 || c > 99999) {
				throw new Exception();
			}
			Element code = new Element("code");
			code.setText(client_code.getText());
			adresse.addContent(code);
		} catch (Exception ex){
			displayError("Le code postal doit être un entier et valide");
			return null;
		}
		client.addContent(adresse);
		r.addContent(client);
		//Pour equipe
		Element equipe = new Element("equipe");
		for(int i = 0; i < equipeModel.getRowCount(); ++i) {
			//Pour chaque membre
			Element membre = new Element("contact");
			for(int j = 0; j < equipeModel.getColumnCount(); ++j) { 
				String s = (String) equipeModel.getValueAt(i, j);
				if (s == null || s.equals("")) {
					displayError("La rubrique Equipe n'est pas valide");
					return null;
				}
				if (j == 0) {
					Element mnom = new Element("nom");
					mnom.setText(replace(s));
					membre.addContent(mnom);
				} else if (j == 1) {
					Element mprenom = new Element("prenom");
					mprenom.setText(replace(s));
					membre.addContent(mprenom);
				} else if (j == 2) {
					Element mgender = new Element("gender");
					bool = s.equals("Homme") ? "true" : "false";
					mgender.setText(bool);
					membre.addContent(mgender);
				}
			}
			equipe.addContent(membre);
		}
		r.addContent(equipe);
		//Fonctionnalités
		for(int i = 0; i < foncModel.getRowCount(); ++i) {
			Object prio = foncModel.getValueAt(i, 1);
			String descrip = (String) foncModel.getValueAt(i, 2);
			if (prio == null || descrip.equals("")) {
				displayError("La rubrique Fonctionnalités n'est pas valide");
			}
			Element fonctio = new Element("fonctionnalite");
			fonctio.setAttribute("prio", prio.toString());
			Element fdesc = new Element("description");
			fdesc.setText(replace(descrip));
			fonctio.addContent(fdesc);
			List<Object[]> l = map.get((Integer) foncModel.getValueAt(i,0));
			for (Object[] tab : l) {
				if (tab[1] == null || tab[1].equals("") 
					|| tab[2] == null || tab[2].equals("")
					|| tab[3] == null || tab[3].equals("")) {
					displayError("Une des exigences de la fonctionnalité " + tab[0] + " n'est pas valide");
					return null;
				}
				Element exigence = new Element("exigence");
				Element id = new Element("identifiant");
				id.setText(replace(tab[1].toString()));
				exigence.addContent(id);
				Element prior = new Element("priorite");
				prior.setText(tab[2].toString());
				exigence.addContent(prior);
				Element descr = new Element("description");
				descr.setText(replace(tab[3].toString()));
				exigence.addContent(descr);
				fonctio.addContent(exigence);
			}
			r.addContent(fonctio);
		}
		return doc;
	}
	
	private String replace(String s) {
		s = s.replaceAll("[éèê]", "e");
		s = s.replaceAll("[àâ]", "a");
		s = s.replaceAll("[î]", "i");
		s = s.replaceAll("[ô]", "o");
		s = s.replaceAll("[ùû]", "u");
		s = s.replaceAll("['\"]", "");
		return s;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
            		new MyClient("https://lw2-mraihy-levasseur.herokuapp.com/")
            			.display();
            	} catch (Exception e) {
            		e.printStackTrace();
            		displayError(e.getMessage());
            	}
            }
        });
	}

}
