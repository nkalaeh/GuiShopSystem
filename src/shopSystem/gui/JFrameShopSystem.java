package shopSystem.gui;

import shopSystem.core.Artikel;
import shopSystem.core.Warengruppe;
import shopSystem.dao.ArtikelDaoSqlite;
import shopSystem.dao.WarengruppeDaoSqlite;
import shopSystem.ex.NoArtikelFoundException;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class JFrameShopSystem extends JFrame {

	private final JComboBox<Warengruppe> catComboBox = new JComboBox<Warengruppe>();
	private final DefaultListModel warenkorbModel = new DefaultListModel();
	private final JList warenkorbJList = new JList();
	private final ArrayList<Artikel> warenkorbArrayList = new ArrayList<Artikel>();
	private ArtikelDaoSqlite daoArticle;
	private WarengruppeDaoSqlite daoWarengruppe;
	private ArrayList<Warengruppe> listeWarengruppe;
	private ArrayList<Artikel> arrayListArtikel;
	private Artikel selectedArticle;
	private JTextField sumTextField;
	private JTextField amountTextField;
	private JTextField artNrInput;
	private JTextField artDescInput;
	private JTextField artWeightInput;
	private JTextField artPriceInput;

	/**
	 * Create the frame.
	 *
	 * @throws NoArtikelFoundException
	 */
	public JFrameShopSystem() throws NoArtikelFoundException {
		setResizable(false);
		initGUI();
		initDao();
		initJFrameShopSystem();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					JFrameShopSystem frame = new JFrameShopSystem();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initGUI() {
		setTitle("Shop-System");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 388, 462);
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		JPanel editPanel = new JPanel();
		editPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
				new Color(160, 160, 160)
				), "Men\u00FC", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		editPanel.setBounds(15, 179, 352, 76);
		contentPanel.add(editPanel);
		editPanel.setLayout(null);

		JButton srchBtn = new JButton("Search");
		srchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				buttonSearchActionPerformed(ae);
			}
		});
		srchBtn.setBounds(14, 14, 75, 23);
		editPanel.add(srchBtn);

		JButton insBtn = new JButton("New");
		insBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonInsertActionPerformed(e);
			}
		});
		insBtn.setBounds(95, 14, 75, 23);
		editPanel.add(insBtn);
		{
			JButton delBtn = new JButton("Del");
			delBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonDeleteActionPerformed(e);
				}
			});
			delBtn.setBounds(95, 41, 75, 23);
			editPanel.add(delBtn);
		}
		{
			JButton updBtn = new JButton("Edit");
			updBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonUpdateActionPerformed(e);
				}
			});
			updBtn.setBounds(14, 41, 75, 23);
			editPanel.add(updBtn);
		}
		{
			JButton clrBtn = new JButton("Clear");
			clrBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					btnClear(e);
				}
			});
			clrBtn.setBounds(180, 14, 160, 50);
			editPanel.add(clrBtn);
		}

		JPanel navPanel = new JPanel();
		navPanel.setBorder(new TitledBorder(null, "Navigation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		navPanel.setBounds(15, 260, 352, 55);
		contentPanel.add(navPanel);
		navPanel.setLayout(null);

		JButton navBtnFirst = new JButton("|<<");
		navBtnFirst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				navBtnFirstactionPerformed(e);
			}
		});
		navBtnFirst.setBounds(12, 20, 67, 23);
		navPanel.add(navBtnFirst);

		JButton navBtnPrev = new JButton("<");
		navBtnPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					navBtnPrevactionPerformed(e);
				} catch (NoArtikelFoundException noArtikelFoundException) {
					noArtikelFoundException.printStackTrace();
				}
			}
		});
		navBtnPrev.setBounds(98, 20, 67, 23);
		navPanel.add(navBtnPrev);

		JButton navBtnNext = new JButton(">");
		navBtnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					navBtnNextactionPerformed(e);
				} catch (NoArtikelFoundException noArtikelFoundException) {
					noArtikelFoundException.printStackTrace();
				}
			}
		});
		navBtnNext.setBounds(187, 20, 67, 23);
		navPanel.add(navBtnNext);

		JButton navBtnLast = new JButton(">>|");
		navBtnLast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					navBtnLastactionPerformed(e);
				} catch (NoArtikelFoundException noArtikelFoundException) {
					noArtikelFoundException.printStackTrace();
				}
			}
		});
		navBtnLast.setBounds(271, 20, 67, 23);
		navPanel.add(navBtnLast);

		JPanel listPanel = new JPanel();
		listPanel.setBorder(new TitledBorder(null, "Warenkorb", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		listPanel.setBounds(16, 320, 349, 100);
		contentPanel.add(listPanel);
		listPanel.setLayout(null);

		JButton addBtn = new JButton("+");
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addBtnactionPerformed(e);
			}
		});
		addBtn.setBounds(21, 21, 75, 23);
		listPanel.add(addBtn);

		JButton subBtn = new JButton("-");
		subBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				subBtnactionPerformed(e);
			}
		});
		subBtn.setBounds(21, 62, 75, 23);
		listPanel.add(subBtn);

		JLabel sumLabel = new JLabel("Summe:");
		sumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sumLabel.setBounds(249, 50, 90, 14);
		listPanel.add(sumLabel);

		sumTextField = new JTextField();
		sumTextField.setHorizontalAlignment(SwingConstants.CENTER);
		sumTextField.setEditable(false);
		sumTextField.setBounds(258, 66, 66, 20);
		listPanel.add(sumTextField);
		sumTextField.setColumns(10);

		amountTextField = new JTextField();
		amountTextField.setHorizontalAlignment(SwingConstants.CENTER);
		amountTextField.setEditable(false);
		amountTextField.setColumns(10);
		amountTextField.setBounds(257, 26, 66, 20);
		listPanel.add(amountTextField);

		JLabel amountLabel = new JLabel("St\u00FCck:");
		amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
		amountLabel.setBounds(250, 12, 82, 14);
		listPanel.add(amountLabel);

		warenkorbJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				articleListvalueChanged(e);
			}
		});
		warenkorbJList.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0)), null));
		warenkorbJList.setBounds(106, 21, 138, 65);
		listPanel.add(warenkorbJList);
		warenkorbJList.setModel(warenkorbModel);
		warenkorbJList.setSelectedIndex(0);

		JPanel articlePanel = new JPanel();
		articlePanel.setBorder(new TitledBorder(null, "Artikel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		articlePanel.setBounds(16, 11, 349, 157);
		contentPanel.add(articlePanel);
		articlePanel.setLayout(null);

		JLabel cdNrLabel = new JLabel("Artikelnummer");
		cdNrLabel.setHorizontalAlignment(SwingConstants.LEFT);
		cdNrLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cdNrLabel.setBounds(16, 19, 79, 15);
		articlePanel.add(cdNrLabel);

		JLabel interpretLabel = new JLabel("Artikelbezeichnung");
		interpretLabel.setHorizontalAlignment(SwingConstants.LEFT);
		interpretLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		interpretLabel.setBounds(16, 47, 103, 15);
		articlePanel.add(interpretLabel);

		JLabel weightLabel = new JLabel("Gewicht");
		weightLabel.setHorizontalAlignment(SwingConstants.LEFT);
		weightLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		weightLabel.setBounds(16, 72, 45, 15);
		articlePanel.add(weightLabel);

		JLabel priceLabel = new JLabel("Einzelpreis");
		priceLabel.setHorizontalAlignment(SwingConstants.LEFT);
		priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		priceLabel.setBounds(16, 125, 55, 15);
		articlePanel.add(priceLabel);

		JLabel catLabel = new JLabel("Warengruppe");
		catLabel.setHorizontalAlignment(SwingConstants.LEFT);
		catLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		catLabel.setBounds(16, 100, 75, 15);
		articlePanel.add(catLabel);

		artNrInput = new JTextField();
		artNrInput.setText("");
		artNrInput.setHorizontalAlignment(SwingConstants.RIGHT);
		artNrInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
		artNrInput.setColumns(10);
		artNrInput.setBounds(187, 16, 149, 20);
		articlePanel.add(artNrInput);

		artDescInput = new JTextField();
		artDescInput.setText("");
		artDescInput.setHorizontalAlignment(SwingConstants.RIGHT);
		artDescInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
		artDescInput.setColumns(10);
		artDescInput.setBounds(187, 43, 149, 20);
		articlePanel.add(artDescInput);

		artWeightInput = new JTextField();
		artWeightInput.setText("");
		artWeightInput.setHorizontalAlignment(SwingConstants.RIGHT);
		artWeightInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
		artWeightInput.setColumns(10);
		artWeightInput.setBounds(187, 70, 149, 20);
		articlePanel.add(artWeightInput);

		artPriceInput = new JTextField();
		artPriceInput.setText("0");
		artPriceInput.setHorizontalAlignment(SwingConstants.RIGHT);
		artPriceInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
		artPriceInput.setColumns(10);
		artPriceInput.setBounds(187, 122, 149, 20);
		articlePanel.add(artPriceInput);

		catComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		catComboBox.setBounds(187, 97, 149, 18);
		articlePanel.add(catComboBox);

		JLabel selectLabel = new JLabel(":");
		selectLabel.setBounds(144, 19, 4, 15);
		articlePanel.add(selectLabel);
		selectLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel selectLabel_1 = new JLabel(":");
		selectLabel_1.setBounds(144, 47, 4, 15);
		articlePanel.add(selectLabel_1);
		selectLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel selectLabel_2 = new JLabel(":");
		selectLabel_2.setBounds(144, 72, 4, 15);
		articlePanel.add(selectLabel_2);
		selectLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel selectLabel_3 = new JLabel(":");
		selectLabel_3.setBounds(144, 100, 4, 15);
		articlePanel.add(selectLabel_3);
		selectLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel selectLabel_4 = new JLabel(":");
		selectLabel_4.setBounds(144, 126, 4, 15);
		articlePanel.add(selectLabel_4);
		selectLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JSeparator separator = new JSeparator();
		separator.setBounds(22, 174, 338, 8);
		contentPanel.add(separator);
	}

	/**
	 * Eigene init-Methode
	 */
	private void initJFrameShopSystem() {
		try {
			fillComboBox();
		} catch (NoArtikelFoundException ex) {
			showErrorMessage(ex.getMessage());
		}
		selectedArticle = new Artikel();
		clearArticle();
	}

	private void initDao() {
		try {
			daoArticle = new ArtikelDaoSqlite();
			daoWarengruppe = new WarengruppeDaoSqlite();
			this.arrayListArtikel = daoArticle.read();
			this.listeWarengruppe = daoWarengruppe.read();
		} catch (ClassNotFoundException | NoArtikelFoundException ex) {
			showErrorMessage(ex.getMessage());
		}
	}

	/**
	 * F�llt die Combobox mit den aktuellen Werten.
	 *
	 * @throws !DaoException
	 */
	private void fillComboBox() throws NoArtikelFoundException {
		for (Warengruppe warengruppe : listeWarengruppe) {
			catComboBox.addItem(warengruppe); // gibt Bezeichnung zur�ck
		}
	}

	private void buttonSearchActionPerformed(ActionEvent ae) {

		if (!artNrInput.getText()
				.equals("")) {
			try {
				selectedArticle = daoArticle.read(Integer.parseInt(artNrInput.getText()));
				showSelectedArticle();
			} catch (NoArtikelFoundException ex) {
				showErrorMessage("" + ex.getMessage());
			}
		}
	}

	protected void buttonInsertActionPerformed(ActionEvent e) {
		selectedArticle = getArtikelDisplayed();
		try {
			selectedArticle = daoArticle.insert(selectedArticle);
			showSelectedArticle();
		} catch (NoArtikelFoundException ex) {
			showErrorMessage(ex.getMessage());
		}
	}
	/*

	 */

	private Artikel getArtikelDisplayed() {
		selectedArticle.setArticleNr(Integer.parseInt(artNrInput.getText()));
		selectedArticle.setBezeichnung(artDescInput.getText());
		selectedArticle.setGewicht(Double.parseDouble(artWeightInput.getText()));
		selectedArticle.setWarengruppe(catComboBox.getItemAt(catComboBox.getSelectedIndex())
				.getId());
		selectedArticle.setEk(Double.parseDouble(artPriceInput.getText()
				.replace(',', '.')));
		return selectedArticle;
	}

	/**
	 * Zeigt die gefundenen Inhalte in der GUI an
	 */
	private void showSelectedArticle() {
		artNrInput.setText(Integer.toString(selectedArticle.getArticlelNr()));
		artDescInput.setText(selectedArticle.getDescription());
		artWeightInput.setText(Double.toString(selectedArticle.getWeight()));
		artPriceInput.setText(Double.toString(selectedArticle.getPrice()));
		showWarengruppe(selectedArticle);
	}

	/**
	 * Zeigt Bezeichnung der Warengruppe an.
	 */
	private void showWarengruppe(Artikel artikel) {
		for (int i = 0; i < catComboBox.getItemCount(); i++) {
			if (catComboBox.getItemAt(i)
					.getId() == artikel.getWarengruppe()) {
				catComboBox.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Zeigt ein Fehlerfenster an.
	 *
	 * @param message Die anzuzeigende Nachricht.
	 */
	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, this.getTitle(), JOptionPane.ERROR_MESSAGE);
	}

	private void btnClear(ActionEvent e) {
		clearArticle();
	}

	private void clearArticle() {
		artNrInput.setText("");
		artDescInput.setText("");
		artPriceInput.setText("");
		artWeightInput.setText("");
		catComboBox.setSelectedIndex(1);
	}

	/*
	 * Rechnet die Summe und Anzahl aller Artikel im Warenkorb
	 */
	private void calcSum() {
		var artikelSum = 0.00;
		var artikelCount = 0;
		for (Artikel artikel : warenkorbArrayList) {
			artikelSum += artikel.getPrice();
			artikelCount++;
		}
		sumTextField.setText(String.valueOf(artikelSum));
		amountTextField.setText(String.valueOf(artikelCount));
	}

	protected void buttonDeleteActionPerformed(ActionEvent e) {
		selectedArticle = new Artikel();
		try {
			selectedArticle.setArticleNr(Integer.parseInt(artNrInput.getText()));
			try {
				daoArticle.delete(selectedArticle.getArticlelNr());
				clearArticle();
			} catch (NoArtikelFoundException e1) {
				showErrorMessage("Artikel-Nummer nicht vorhanden");
			}
		} catch (NumberFormatException nfe) {
			showErrorMessage("Artikel-Nummer falsch erfasst");
		}

	}

	protected void buttonUpdateActionPerformed(ActionEvent e) {
		selectedArticle = getArtikelDisplayed();
		try {
			daoArticle.update(selectedArticle);
		} catch (NoArtikelFoundException ex) {
			showErrorMessage(ex.getMessage());
		}
	}

	/*
	 * Springt zum Ersten Artikel
	 */
	protected void navBtnFirstactionPerformed(ActionEvent e) {
		try {
			selectedArticle = daoArticle.read(1);
		} catch (NoArtikelFoundException ex) {
			showErrorMessage(ex.getMessage());
		}
		showSelectedArticle();
	}

	/*
	 * Springt zum vorherigen Artikel
	 */
	protected void navBtnPrevactionPerformed(ActionEvent e) throws NoArtikelFoundException {
		if ((selectedArticle.getArticlelNr() >= 2)) {
			try {
				selectedArticle = daoArticle.read(selectedArticle.getArticlelNr() - 1);
			} catch (NoArtikelFoundException ex) {
				ex.printStackTrace();
				throw new NoArtikelFoundException("" + ex.getMessage());
			}
		}
		showSelectedArticle();
	}

	/*
	 * Springt zum nächsten Artikel
	 */
	protected void navBtnNextactionPerformed(ActionEvent e) throws NoArtikelFoundException {
		var index = arrayListArtikel.size();
		if (selectedArticle.getArticlelNr() != index) {
			try {
				selectedArticle = daoArticle.read(selectedArticle.getArticlelNr() + 1);
			} catch (NoArtikelFoundException ex) {
				ex.printStackTrace();
				throw new NoArtikelFoundException("" + ex.getMessage());
			}
		}
		showSelectedArticle();
	}

	/*
	 * Springt zum letzten Artikel
	 */
	protected void navBtnLastactionPerformed(ActionEvent e) throws NoArtikelFoundException {
		selectedArticle = daoArticle.read(arrayListArtikel.size());
		showSelectedArticle();
	}

	/*
	 * Fügt den ausgewählten Artikel zum Warenkorb hinzu
	 */
	protected void addBtnactionPerformed(ActionEvent e) {

		warenkorbModel.addElement(selectedArticle.getDescription());
		warenkorbArrayList.add(selectedArticle);
		calcSum();
	}

	/*
	 * Entfernt den ausgewählten Artikel vom Warenkorb
	 */
	protected void subBtnactionPerformed(ActionEvent e) {
		if (warenkorbJList.getSelectedIndex() != -1) {
			warenkorbModel.remove(warenkorbJList.getSelectedIndex());
			warenkorbArrayList.remove(warenkorbJList.getSelectedIndex() + 1);
		}
		calcSum();
	}

	protected void articleListvalueChanged(ListSelectionEvent e) {
	}

}
