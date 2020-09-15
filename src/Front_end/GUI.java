/**
 * Dieses Package ist für den Teil zuständig den man sieht. (GUI)
 */
package Front_end;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Back_end.Main_Class;
import Back_end.Board;

public class GUI extends Main_Class implements ActionListener {

	public static JFrame Frame = new JFrame();
	public static JLabel labelMainContent = new JLabel();
	public static JLabel labelSideBar = new JLabel();
	public static JButton btnUndo = new JButton("Undo");
	public static JButton btnNewGame = new JButton("New Game");
	public static JLabel playerSelectionJLabel = new JLabel("White is:           Black is:");
	public static JButton btnplayerwhite = new JButton();
	public static JButton btnplayerblack = new JButton();
	public static JLabel labelIconsWhite = new JLabel();
	public static JLabel labelIconsBlack = new JLabel();
	public static JLabel labelPlayersTurn = new JLabel("Players Turn");
	public static JLabel labelPhase = new JLabel("Phase-Info");
	public static JLabel labelScore = new JLabel("Score");
	public static JLabel labelScoreWhite = new JLabel("white");
	public static JLabel labelScoreBlack = new JLabel("black");
	public static JButton btnGameOver = new JButton();

	public static JButton btnA1 = new JButton("1");
	public static JButton btnA4 = new JButton("2");
	public static JButton btnA7 = new JButton("3");
	public static JButton btnB2 = new JButton("9");
	public static JButton btnB4 = new JButton("10");
	public static JButton btnB6 = new JButton("11");
	public static JButton btnC3 = new JButton("17");
	public static JButton btnC4 = new JButton("18");
	public static JButton btnC5 = new JButton("19");
	public static JButton btnD1 = new JButton("8");
	public static JButton btnD2 = new JButton("16");
	public static JButton btnD3 = new JButton("24");
	public static JButton btnD5 = new JButton("20");
	public static JButton btnD6 = new JButton("12");
	public static JButton btnD7 = new JButton("4");
	public static JButton btnE3 = new JButton("23");
	public static JButton btnE4 = new JButton("22");
	public static JButton btnE5 = new JButton("21");
	public static JButton btnF2 = new JButton("15");
	public static JButton btnF4 = new JButton("14");
	public static JButton btnF6 = new JButton("13");
	public static JButton btnG1 = new JButton("7");
	public static JButton btnG4 = new JButton("6");
	public static JButton btnG7 = new JButton("5");
	public static JButton[] buttons = { btnA1, btnA4, btnA7, btnD7, btnG7, btnG4, btnG1, btnD1, btnB2, btnB4, btnB6,
			btnD6, btnF6, btnF4, btnF2, btnD2, btnC3, btnC4, btnC5, btnD5, btnE5, btnE4, btnE3, btnD3 };;

	public static Image sideBarbackground = new ImageIcon(Main_Class.class.getResource("/SideBar.png")).getImage();
	public static Image maincontenbackground = new ImageIcon(Main_Class.class.getResource("/Mühle_Bg.png")).getImage();
	public static ImageIcon StoneWhite = new ImageIcon(Main_Class.class.getResource("/whiteStone.png"));
	public static ImageIcon StoneWhiteGreen = new ImageIcon(Main_Class.class.getResource("/whiteStoneGrün.png"));
	public static ImageIcon StoneBlack = new ImageIcon(Main_Class.class.getResource("/blackStone.png"));
	public static ImageIcon StoneBlackGreen = new ImageIcon(Main_Class.class.getResource("/blackStoneGrün.png"));
	public static ImageIcon StoneWhiteTrans = new ImageIcon(Main_Class.class.getResource("/whiteTrans.png"));
	public static ImageIcon StoneBlackTrans = new ImageIcon(Main_Class.class.getResource("/blackTrans.png"));

	public static Font standardFont = new Font("Serif", Font.PLAIN, 14);

	public static JLabel labelwhite1 = new JLabel();
	public static JLabel labelwhite2 = new JLabel();
	public static JLabel labelwhite3 = new JLabel();
	public static JLabel labelwhite4 = new JLabel();
	public static JLabel labelwhite5 = new JLabel();
	public static JLabel labelwhite6 = new JLabel();
	public static JLabel labelwhite7 = new JLabel();
	public static JLabel labelwhite8 = new JLabel();
	public static JLabel labelwhite9 = new JLabel();
	public static JLabel labelblack1 = new JLabel();
	public static JLabel labelblack2 = new JLabel();
	public static JLabel labelblack3 = new JLabel();
	public static JLabel labelblack4 = new JLabel();
	public static JLabel labelblack5 = new JLabel();
	public static JLabel labelblack6 = new JLabel();
	public static JLabel labelblack7 = new JLabel();
	public static JLabel labelblack8 = new JLabel();
	public static JLabel labelblack9 = new JLabel();
	public static ArrayList<JLabel> AllLabelsWhite = new ArrayList<JLabel>(Arrays.asList(labelwhite1, labelwhite2,
			labelwhite3, labelwhite4, labelwhite5, labelwhite6, labelwhite7, labelwhite8, labelwhite9));
	public static ArrayList<JLabel> AllLabelsBlack = new ArrayList<JLabel>(Arrays.asList(labelblack1, labelblack2,
			labelblack3, labelblack4, labelblack5, labelblack6, labelblack7, labelblack8, labelblack9));

	/**
	 * Erstellt das Fenster
	 */
	public GUI() {
		setUpWindow();
		addMainContent();
		addSideBar();
		refreshSideBar();
		System.out.println(getSourceLocTemp());
		
	}

	/**
	 * Für das Frame zuständig (hinterste Ebene die alle Inhalte enthält)
	 */
	public static void setUpWindow() {

		// Entire Frame
		Frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Frame.setLocationRelativeTo(null);
		Frame.setResizable(false);
		Frame.setLayout(new GridBagLayout());


		

		Frame.setVisible(true);
	}

	/**
	 * Erstellt den MainContent. Der Maincontent enthält das Spielfeld und liegt auf
	 * dem Frame
	 */
	public void addMainContent() {
		GridBagConstraints grid = new GridBagConstraints();

		// GridLayout für Frame
		grid.fill = GridBagConstraints.HORIZONTAL;
		grid.gridx = 0;
		grid.gridy = 0;
		grid.gridwidth = 9;
		

		// fügt maincontent dem Frame hinzu
		labelMainContent.setIcon(new javax.swing.ImageIcon(maincontenbackground));
		labelMainContent.setPreferredSize(new Dimension(700, 700));
		Frame.add(labelMainContent, grid);
		Frame.pack();

		// Einstellung für die Knöpfe
		for (int i = 0; i < 24; i++) {
			buttons[i].setName(Integer.toString(i));
			buttons[i].addActionListener(this);
			buttons[i].setOpaque(false);
			buttons[i].setForeground(Color.white);
			buttons[i].setContentAreaFilled(false);
			buttons[i].setBorderPainted(false);
			buttons[i].setText(null);

		}

		// Einstellungen für den GameOver Knopf
		btnGameOver.setOpaque(false);
		btnGameOver.setForeground(Color.white);
		btnGameOver.setContentAreaFilled(false);
		btnGameOver.setBorderPainted(false);

		// Raster für die Knöpfe
		labelMainContent.setLayout(new GridLayout(7, 7));

		// Fügt die Knöpfe dem raster hinzu

		// erste Reihe
		labelMainContent.add(btnA1);
		labelMainContent.add(new JLabel());
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnA4);
		labelMainContent.add(new JLabel());
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnA7);
		// zweite Reihe
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnB2);
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnB4);
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnB6);
		labelMainContent.add(new JLabel());
		// dritte Reihe
		labelMainContent.add(new JLabel());
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnC3);
		labelMainContent.add(btnC4);
		labelMainContent.add(btnC5);
		labelMainContent.add(new JLabel());
		labelMainContent.add(new JLabel());
		// vierte Reihe
		labelMainContent.add(btnD1);
		labelMainContent.add(btnD2);
		labelMainContent.add(btnD3);
		labelMainContent.add(btnGameOver);
		labelMainContent.add(btnD5);
		labelMainContent.add(btnD6);
		labelMainContent.add(btnD7);
		// fünfte Reihe
		labelMainContent.add(new JLabel());
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnE3);
		labelMainContent.add(btnE4);
		labelMainContent.add(btnE5);
		labelMainContent.add(new JLabel());
		labelMainContent.add(new JLabel());
		// sechste Reihe
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnF2);
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnF4);
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnF6);
		labelMainContent.add(new JLabel());
		// siebte Reihe
		labelMainContent.add(btnG1);
		labelMainContent.add(new JLabel());
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnG4);
		labelMainContent.add(new JLabel());
		labelMainContent.add(new JLabel());
		labelMainContent.add(btnG7);

		//positioniert Frame in der Mitte des Bildschirmes
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = Frame.getSize().width;
		int h = Frame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		Frame.setLocation(x, y);
		
	}

	/**
	 * Fügt die SideBar dem frame hinzu
	 */
	public static void addSideBar() {

		// Positioniert die SideBar auf der Frame Ebene
		GridBagConstraints grid = new GridBagConstraints();
		grid.fill = GridBagConstraints.HORIZONTAL;
		grid.gridx = 10;
		grid.gridy = 0;
		grid.gridwidth = 1;
		labelSideBar.setPreferredSize(new Dimension(200, 700));
		labelSideBar.setIcon(new javax.swing.ImageIcon(sideBarbackground));
		Frame.add(labelSideBar, grid);
		Frame.pack();

		// Raster für die Sidebar
		labelSideBar.setLayout(new GridBagLayout());

		// konfiguriert den BtnUndo und positioniert ihn auf der Sidebar
		grid.gridx = 0;
		grid.gridy = 0;
		btnUndo.setPreferredSize(new Dimension(100, 50));
		btnUndo.setOpaque(false);
		btnUndo.setContentAreaFilled(false);
		btnUndo.setBorderPainted(false);
		btnUndo.setFont(standardFont);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(getbitSetWhiteStones());
				System.out.println(getbitSetBlackStones());
				System.out.println(getPhase());
				System.out.println(getIsCurrentPlayerWhite());

				Board.loadLastSituation();

			}
		});
		labelSideBar.add(btnUndo, grid);

		// konfiguriert den BtnNewGame und positioniert ihn auf der Sidebar
		grid.gridx = 1;
		grid.gridy = 0;
		btnNewGame.setPreferredSize(new Dimension(100, 50));
		btnNewGame.setOpaque(false);
		btnNewGame.setContentAreaFilled(false);
		btnNewGame.setBorderPainted(false);
		btnNewGame.setFont(standardFont);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Board.newGame();
			}
		});
		labelSideBar.add(btnNewGame, grid);

		// konfiguriert das playerSelectionLabel und positioniert es auf der Sidebar
		grid.gridx = 0;
		grid.gridy = 1;
		grid.gridwidth = 2;
		playerSelectionJLabel.setPreferredSize(new Dimension(200, 25));
		playerSelectionJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerSelectionJLabel.setFont(standardFont);
		labelSideBar.add(playerSelectionJLabel, grid);

		// konfiguriert den btnplayerwhite und positioniert ihn auf der Sidebarr
		grid.gridx = 0;
		grid.gridy = 2;
		grid.gridwidth = 1;
		btnplayerwhite.setPreferredSize(new Dimension(100, 50));
		btnplayerwhite.setOpaque(false);
		btnplayerwhite.setContentAreaFilled(false);
		btnplayerwhite.setBorderPainted(false);
		btnplayerwhite.setHorizontalAlignment(SwingConstants.CENTER);
		btnplayerwhite.setFont(standardFont);
		btnplayerwhite.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// if(!getIsGameRunning()) {
				changePlayerWhite();
				refreshSideBar();
				// }

			}
		});
		labelSideBar.add(btnplayerwhite, grid);

		// konfiguriert den btnplayerblack und positioniert ihn auf der Sidebarr
		grid.gridx = 1;
		grid.gridy = 2;
		grid.gridwidth = 1;
		btnplayerblack.setPreferredSize(new Dimension(100, 50));
		btnplayerblack.setOpaque(false);
		btnplayerblack.setContentAreaFilled(false);
		btnplayerblack.setBorderPainted(false);
		btnplayerblack.setHorizontalAlignment(SwingConstants.LEFT);
		btnplayerblack.setFont(standardFont);
		btnplayerblack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// if(!getIsGameRunning()) {
				changePlayerBlack();
				refreshSideBar();
				// }

			}
		});
		labelSideBar.add(btnplayerblack, grid);

		// Konfiguriert und fügt die labeliconswhite der SideBar hinzu. Die LabelIcons
		// zeigen die Anzahl steine an die ein SPieler noch setzen kann.
		grid.gridx = 0;
		grid.gridy = 3;
		grid.gridwidth = 2;
		labelIconsWhite.setPreferredSize(new Dimension(200, 200));
		labelIconsWhite.setLayout(new GridLayout(3, 3));
		for (int i = 0; i < 9; i++) {
			AllLabelsWhite.get(i).setHorizontalAlignment(SwingConstants.CENTER);
			AllLabelsWhite.get(i).setIcon(StoneWhite);
			labelIconsWhite.add(AllLabelsWhite.get(i));
		}
		;
		labelSideBar.add(labelIconsWhite, grid);

		// Konfiguriert und fügt die labeliconsblack der SideBar hinzu. Die LabelIcons
		// zeigen die Anzahl steine an die ein SPieler noch setzen kann.
		grid.gridx = 0;
		grid.gridy = 4;
		grid.gridwidth = 2;
		labelIconsBlack.setPreferredSize(new Dimension(200, 200));
		labelIconsBlack.setLayout(new GridLayout(3, 3));
		for (int i = 0; i < 9; i++) {
			AllLabelsBlack.get(i).setHorizontalAlignment(SwingConstants.CENTER);
			AllLabelsBlack.get(i).setIcon(StoneBlack);
			labelIconsBlack.add(AllLabelsBlack.get(i));
		}
		;
		labelSideBar.add(labelIconsBlack, grid);

		grid.gridx = 0;
		grid.gridy = 5;
		grid.gridwidth = 2;
		labelPlayersTurn.setPreferredSize(new Dimension(200, 25));
		labelPlayersTurn.setHorizontalAlignment(SwingConstants.CENTER);
		labelPlayersTurn.setFont(standardFont);
		labelSideBar.add(labelPlayersTurn, grid);

		//konfiguriert und fügt die "Phase-Anzeige" der SideBar hinzu
		grid.gridx = 0;
		grid.gridy = 6;
		grid.gridwidth = 2;
		labelPhase.setPreferredSize(new Dimension(200, 75));
		labelPhase.setHorizontalAlignment(SwingConstants.CENTER);
		labelPhase.setFont(standardFont);
		labelSideBar.add(labelPhase, grid);

		//konfiguriert und fügt das ScoreLabel der SideBar hinzu

		grid.gridx = 0;
		grid.gridy = 7;
		grid.gridwidth = 2;
		labelScore.setPreferredSize(new Dimension(200, 25));
		labelScore.setHorizontalAlignment(SwingConstants.CENTER);
		labelScore.setFont(standardFont);
		labelSideBar.add(labelScore, grid);

		//konfiguriert und fügt das labelscoreWhite der SideBar hinzu
		grid.gridx = 0;
		grid.gridy = 8;
		grid.gridwidth = 1;
		labelScoreWhite.setPreferredSize(new Dimension(100, 25));
		labelScoreWhite.setHorizontalAlignment(SwingConstants.CENTER);
		labelScoreWhite.setFont(standardFont);
		labelScoreWhite.setFont(standardFont);

		labelSideBar.add(labelScoreWhite, grid);

		//konfiguriert und fügt das labelscoreBlack der SideBar hinzu
		grid.gridx = 1;
		grid.gridy = 8;
		labelScoreBlack.setPreferredSize(new Dimension(100, 25));
		labelScoreBlack.setHorizontalAlignment(SwingConstants.CENTER);
		labelScoreBlack.setFont(standardFont);
		labelScoreBlack.setFont(standardFont);

		labelSideBar.add(labelScoreBlack, grid);

	}

	/**
	 * Lädt die SideBar neu und benutzt dafür die Daten aus der Main_Class
	 */
	public static void refreshSideBar() {
		// refresh playerOne and playerTwo
		if (getIsWhiteHuman()) {
			btnplayerwhite.setText("Human");
		} else {
			btnplayerwhite.setText("Computer");
		}
		if (getIsBlackHuman()) {
			btnplayerblack.setText("Human");
		} else {
			btnplayerblack.setText("Computer");
		}

		// refresh Icons
		for (int i = 0; i < 9; i++) {
			if ((i + 1) > getWhiteStonesNotPlaced()) {
				AllLabelsWhite.get(i).setIcon(null);
			} else {
				AllLabelsWhite.get(i).setIcon(StoneWhite);
			}
			if ((i + 1) > getBlackStonesNotPlaced()) {
				AllLabelsBlack.get(i).setIcon(null);
			} else {
				AllLabelsBlack.get(i).setIcon(StoneBlack);
			}
		}
		;

		// refresh PlayersTurn
		if (getIsCurrentPlayerWhite()) {
			labelPlayersTurn.setText("White's Turn");
		} else {
			labelPlayersTurn.setText("Black's Turn");
		}

		// refresh LablePhase
		if (getPhase() == 's') {
			labelPhase.setText("<html><body><p align = 'center'>Setzphase<br>Mühle geschlossen = " + getIsMühleClosed()
					+ "</p></body></html>");
		} else if (getPhase() == 'm') {
			labelPhase.setText("<html><body><p align = 'center'>Schiebephase<br>Mühle geschlossen = "
					+ getIsMühleClosed() + "</p></body></html>");
		} else if (getPhase() == 'j') {
			labelPhase.setText("<html><body><p align = 'center'>Springphase<br>Mühle geschlossen = "
					+ getIsMühleClosed() + "</p></body></html>");
		}

		// refresh Score
		labelScoreWhite.setText("" + getScoreWhite());
		labelScoreBlack.setText("" + getScoreBlack());

	}

	/**
	 * Lädt den MainContet neu und benutzt dafür die Daten aus der Main_Class
	 */
	public static void refreshMainContent() {
		for (int loc = 0; loc < 24; loc++) {
			if (getbitSetWhiteStones().get(loc)) {
				if (designOfStone[loc] == 0)
					buttons[loc].setIcon(StoneWhite);
				else if (designOfStone[loc] == 1)
					buttons[loc].setIcon(StoneWhiteGreen);

			} else if (getbitSetBlackStones().get(loc)) {
				if (designOfStone[loc] == 0)
					buttons[loc].setIcon(StoneBlack);
				else if (designOfStone[loc] == 1)
					buttons[loc].setIcon(StoneBlackGreen);
			} else {
				if (designOfStone[loc] == 0)
					buttons[loc].setIcon(null);
				else if (designOfStone[loc] == 2 && getIsWhiteHuman())
					buttons[loc].setIcon(StoneWhiteTrans);
				else if (designOfStone[loc] == 2 && getIsBlackHuman())
					buttons[loc].setIcon(StoneBlackTrans);

			}

		}

	}

	/**
	 * Sobald ein Knopf aus dem MainContent gedrückt wird, wird diese Klasse aufgerufen
	 */
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		Integer loc = Integer.valueOf(btn.getName());
		Board.Move(loc);
	}

}
