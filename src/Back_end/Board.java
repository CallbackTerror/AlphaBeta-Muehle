package Back_end;

import Front_end.GUI;


/**
 * Diese Klasse ist f�r das Spielfeld zust�ndig. Einige der Aufgaben sind: 1.
 * Speichern und laden der Spielsituationen. 2.
 * Verarbeiten/�berpr�fen/durchf�hren der Spielz�ge. 3. �berpr�fen des
 * Spielfeldes nach verschiedenen Kriterien.
 * 
 * @author Nicolas Wyss
 *
 */
public class Board extends Main_Class {

	/**
	 * Mit dieser Methode wird der aktuelle Spielstand gespeichert.
	 * Speicherort: @see {@link Main_Class#dataSituations}
	 */
	public static void saveSituation() {
		dataSituations.add(new ObjSituation(getbitSetWhiteStones(), getbitSetBlackStones(), !getIsCurrentPlayerWhite(),
				getPhase(), getWhiteStonesNotPlaced(), getBlackStonesNotPlaced()));
	}

	/**
	 * Mit dieser Methode wird der n�chst-letzte Spielstand "geladen", der
	 * gespeichert wurde.
	 */
	public static void loadLastSituation() {
		ObjSituation lastSituation = dataSituations.get(dataSituations.size() - 1);
		dataSituations.remove(dataSituations.size() - 1);

		setBitSetBlackStones(lastSituation.getBlackStones());
		setBitSetWhiteStones(lastSituation.getWhiteStones());
		setPhase(lastSituation.getPhase());
		setIsCurrentPlayerWhite(lastSituation.getIsCurrentPlayerWhite());
		setWhiteStonesNotPlaced(lastSituation.getWhiteStonesNotPlaced());
		setBlackStonesNotPlaced(lastSituation.getBlackStonesNotPlaced());
		refreshGui();

	}

	/**
	 * Mit dieser Methode wird ein neues Spiel gestartet und die Spiel-Werte werden
	 * zur�ckgesetzt.
	 */
	public static void newGame() {
		GUI.btnGameOver.setText(null);
		clearAllStones();
		setIsGameRunningTrue();
		setBlackStonesNotPlaced(9);
		setWhiteStonesNotPlaced(9);
		clearBtnBorder();
		setIsCurrentPlayerWhite(false);
		refreshGui();
		nextMove();
	}

	/**
	 * Mit dieser Methode wird der n�chste Spielzug vorbereitet. Daf�r wird z.b. der
	 * aktuelle Spieler gewechselt, die Spielsituation gespeichert usw.
	 */
	public static void nextMove() {
		if (getPhase() == 's' && getbitSetWhiteStones().cardinality() > 0)
			setCurrentPlayerStonesNotPlaced(getCurrentPlayerStonesNotPlaced() - 1);
		checkGameOver();
		GameOver();
		if (!getIsCurrentPlayerHuman())
			saveSituation();
		changeCurrentPlayer();
		checkPhase();
		refreshGui();
		clearBtnBorder();

		if (getIsCurrentPlayerWhite() && !getIsWhiteHuman()) {
			MinMax.isCurrentMinMaxPlayerWhite = true;
			System.err.println(MinMax.AlphaBeta(minmaxdepth, -100000, 100000));
			MinMax.makeBestMove();
		} else if (!getIsCurrentPlayerWhite() && !getIsBlackHuman()) {
			MinMax.isCurrentMinMaxPlayerWhite = false;
			System.err.println(MinMax.AlphaBeta(minmaxdepth, -100000, 100000));
			MinMax.makeBestMove();
		}
	}

	/**
	 * Diese Methode wird ausgef�hrt wenn der "echte" Spieler einen Knopf dr�ckt.
	 * Danach wird von diese Methode der Zug des "echten" Spielers verarbeitet.
	 * Dabei wird z.b. gepr�ft ob der Zug legal ist und ob der Zug eine M�hle
	 * geschlossen hat.
	 * 
	 * @param locButtonPressed Die Position die vom "echten" Spieler gedr�ckt wurde.
	 */
	public static void Move(int locButtonPressed) {
		// System.err.println(getPhase());
		// System.out.println(isLocationFree(locButtonPressed));
		if (getIsM�hleClosed()) { // M�hle Got closed -> Stone needs to be deleted
			if (isLocationOtherPlayer(locButtonPressed) && checkIfRemovable(locButtonPressed)) { // If the position is
																									// besetzt by a
																									// stone of the
																									// other player
																									// delete it
				deleteOtherPlayerStone(locButtonPressed);
				setIsM�hleClosed(false);
				nextMove();
			} else {
				System.err.println(" M�hle Choose other field");
			}
		} else { // No m�hle got closed
			if (getSourceLocTemp() == -1) { // No SourceLoc waiting in temp storage
				if (getPhase() == 's' && isLocationFree(locButtonPressed)) { // Setzphase
					setCurrentPlayerStone(locButtonPressed);
					checkM�hle(locButtonPressed);
					if (!getIsM�hleClosed())
						nextMove();

				} else if (isLocationCurrentPlayer(locButtonPressed) && checkIfHasFreeNeighbours(locButtonPressed)
						&& getPhase() == 'm' || getPhase() == 'j') {
					setSourceLocTemp(locButtonPressed);
				} else {
					System.err.println(" Move Choose other field");
				}
			} else if (isLocationFree(locButtonPressed)) {
				if (getPhase() == 'j') {
					deleteCurrentPlayerStone(getSourceLocTemp());
					setCurrentPlayerStone(locButtonPressed);
					setSourceLocTemp(-1);
					checkM�hle(locButtonPressed);
					if (!getIsM�hleClosed())
						nextMove();

				} else if (checkIfNeighbour(getSourceLocTemp(), locButtonPressed)) {
					deleteCurrentPlayerStone(getSourceLocTemp());
					setCurrentPlayerStone(locButtonPressed);
					setSourceLocTemp(-1);
					checkM�hle(locButtonPressed);
					if (!getIsM�hleClosed())
						nextMove();

				}
			} else if (isLocationCurrentPlayer(locButtonPressed)) {
				setSourceLocTemp(locButtonPressed);
			} else {
				System.err.println("targetsrc Choose other field");
			}
		}

	}

	/**
	 * Diese Methode wird ausgef�hrt sobald das Spiel beendet ist. Sie erh�ht den
	 * Score des Spielers der gewonnen hat.
	 */
	public static void GameOver() {
		if (getIsGameOver()) {
			increaseScoreCurrentPlayer();
			GUI.btnGameOver
					.setText("<html><body><p Color = 'red'> Game Over! <br> Click: <br> New Game </p></body></html>");
			setIsGameOver(false);
		}

	}

	/**
	 * Diese Methode l�dt das Fenster neu.
	 */
	public static void refreshGui() {
		GUI.refreshMainContent();
		GUI.refreshSideBar();
	}

	/**
	 * Diese Methode wird vom "Computer" Spieler bei der Generierung der m�glichen
	 * Z�ge genutzt. Mit dieser Methode wird gepr�ft ob sich der vom Computer
	 * gesetzte/verschobene Stein in einer M�hle befinden.
	 *
	 * @param sourceLoc Gibt den Ort an wo der Stein herkommt. In der Schiebe- und
	 *                  Sprungphase liegt der Wert zwischen 0 und 23 und gibt die
	 *                  Ursprungposition des Steines an.
	 * @param targetLoc the target loc
	 * @return boolean Ob der Stein eine M�hle geschlossen hat.
	 */
	public static boolean checkM�hleMinMax(int sourceLoc, int targetLoc) {
		boolean m�hle = false;
		int[][] arrM�hle = m�hlen[targetLoc];
		if (getPhase() == 24) {
			setCurrentPlayerStone(targetLoc);
			if (isLocationCurrentPlayer(arrM�hle[0][0]) && isLocationCurrentPlayer(arrM�hle[0][1])
					|| isLocationCurrentPlayer(arrM�hle[1][0]) && isLocationCurrentPlayer(arrM�hle[1][1])) {
				m�hle = true;
			}
			deleteCurrentPlayerStone(targetLoc);
		} else {
			deleteCurrentPlayerStone(sourceLoc);
			setCurrentPlayerStone(targetLoc);
			if (isLocationCurrentPlayer(arrM�hle[0][0]) && isLocationCurrentPlayer(arrM�hle[0][1])
					|| isLocationCurrentPlayer(arrM�hle[1][0]) && isLocationCurrentPlayer(arrM�hle[1][1])) {
				m�hle = true;
			}
			deleteCurrentPlayerStone(targetLoc);
			setCurrentPlayerStone(sourceLoc);
		}
		return m�hle;

	}

	/**
	 * Diese Methode pr�ft / legt fest, in welcher Phase sich das Spiel aktuell
	 * befindet.
	 */
	public static void checkPhase() {
		// check for Phase
		if (getCurrentPlayerStonesNotPlaced() > 0)
			setPhase('s');
		else if (getbitSetCurrentPlayer().cardinality() > 3 && getCurrentPlayerStonesNotPlaced() == 0) {
			System.err.println(getIsCurrentPlayerWhite());
			setPhase('m');
		} else
			setPhase('j');
	}

	/**
	 * Diese Methode pr�ft ob ein Stein on der Position loc, frei Felder neben sich
	 * hat. Wird benutzt um zu sehen ob dieser Stein verschoben werden kann, kann er
	 * n�hmlich nur wenn er frei felder neben sich hat.
	 * 
	 * @param loc Position die �berpr�ft werden soll.
	 * @return boolean
	 */
	public static boolean checkIfHasFreeNeighbours(int loc) {
		int[] arrNeighbour = neighbours[loc];
		for (int i = 0; i < arrNeighbour.length; i++) {
			if (isLocationFree(arrNeighbour[i]))
				return true;
		}
		return false;
	}

	/**
	 * Diese Methode �berp�ft ob die Position locOne neben der Position locTwo
	 * liegt.(Also ob diese Nachbaren sind.)
	 * 
	 * @param locOne Erste Position
	 * @param locTwo Zweite Position
	 * @return boolean ob locOne sich neben locTwo befindet.
	 */
	public static boolean checkIfNeighbour(int locOne, int locTwo) {
		int[] arrNeighbour = neighbours[locOne];
		// System.out.println(Arrays.toString(arrNeighbour));
		for (int i = 0; i < arrNeighbour.length; i++) {
			if (arrNeighbour[i] == locTwo)
				return true;
		}
		return false;
	}

	/**
	 * Diese Methode pr�ft ob das Spiele beendet ist. Das ist der Fall, wenn ein
	 * Spieler weniger als 3 Steine hat oder keinen Stein mehr bewegen kann.
	 *
	 * @return true, if successful
	 */
	public static boolean checkGameOver() {
		if (getBlackStonesNotPlaced() != 0) {
			return false;
		}
		if (getbitSetWhiteStones().cardinality() < 3 || getbitSetBlackStones().cardinality() < 3) {
			setIsGameOver(true);
			return true;
		}
		if (getPhase() == 'j')
			return false;
		for (int i = 0; i < 24; i++) {
			if (getbitSetOtherPlayer().get(i)) {
				int[] neighbour = neighbours[i];
				for (int j = 0; j < neighbour.length; j++) {
					if (isLocationFree(neighbour[j])) {
						return false;
					}
				}

			}
		}
		setIsGameOver(true);
		return true;

	}

	/**
	 * Diese Methode �berp�ft ob sich der Stein an der Position loc in einer M�hle
	 * befindet.
	 * 
	 * @param loc Position des Steines der gepr�ft werden soll.
	 */
	public static void checkM�hle(int loc) {
		// Check for M�hle
		int[][] arrM�hle = m�hlen[loc];
		if (isLocationCurrentPlayer(arrM�hle[0][0]) && isLocationCurrentPlayer(arrM�hle[0][1])) {
			setIsM�hleClosed(true);
			refreshGui();
		} else if (isLocationCurrentPlayer(arrM�hle[1][0]) && isLocationCurrentPlayer(arrM�hle[1][1])) {
			setIsM�hleClosed(true);
			refreshGui();
		}

	}

	/**
	 * �berpr�ft ob ein Stein entfernt werden kann. Ein Stein darf entfernt werden
	 * falls: 1. Er befindet sich in keiner M�hle. 2. Es gibt keine "freien" Steine
	 * die sich nicht in einer M�hle befinden.
	 *
	 * @param loc Position des Steines
	 * @return true, if successful
	 */
	public static boolean checkIfRemovable(int loc) {
		int freestones = 0;
		int[][] arrM�hle = m�hlen[loc];
		if (!(isLocationOtherPlayer(arrM�hle[0][0]) && isLocationOtherPlayer(arrM�hle[0][1])
				|| isLocationOtherPlayer(arrM�hle[1][0]) && isLocationOtherPlayer(arrM�hle[1][1]))) {
			// System.out.println("ist in keiner M�hle");
			return true;
		}

		for (int i = 0; i < 24; i++) {
			if (isLocationOtherPlayer(i)) {
				int[][] m�hlenI = m�hlen[i];
				if (!(isLocationOtherPlayer(m�hlenI[0][0]) && isLocationOtherPlayer(m�hlenI[0][1])
						|| isLocationOtherPlayer(m�hlenI[1][0]) && isLocationOtherPlayer(m�hlenI[1][1]))) {
					freestones++;
				}
			}
		}

		if (freestones > 0) {
			return false;
		} else {
			return true;
		}
	}

}
