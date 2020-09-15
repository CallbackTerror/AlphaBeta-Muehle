package Back_end;

import Front_end.GUI;


/**
 * Diese Klasse ist für das Spielfeld zuständig. Einige der Aufgaben sind: 1.
 * Speichern und laden der Spielsituationen. 2.
 * Verarbeiten/überprüfen/durchführen der Spielzüge. 3. überprüfen des
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
	 * Mit dieser Methode wird der nächst-letzte Spielstand "geladen", der
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
	 * zurückgesetzt.
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
	 * Mit dieser Methode wird der nächste Spielzug vorbereitet. Dafür wird z.b. der
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
	 * Diese Methode wird ausgeführt wenn der "echte" Spieler einen Knopf drückt.
	 * Danach wird von diese Methode der Zug des "echten" Spielers verarbeitet.
	 * Dabei wird z.b. geprüft ob der Zug legal ist und ob der Zug eine Mühle
	 * geschlossen hat.
	 * 
	 * @param locButtonPressed Die Position die vom "echten" Spieler gedrückt wurde.
	 */
	public static void Move(int locButtonPressed) {
		// System.err.println(getPhase());
		// System.out.println(isLocationFree(locButtonPressed));
		if (getIsMühleClosed()) { // Mühle Got closed -> Stone needs to be deleted
			if (isLocationOtherPlayer(locButtonPressed) && checkIfRemovable(locButtonPressed)) { // If the position is
																									// besetzt by a
																									// stone of the
																									// other player
																									// delete it
				deleteOtherPlayerStone(locButtonPressed);
				setIsMühleClosed(false);
				nextMove();
			} else {
				System.err.println(" Mühle Choose other field");
			}
		} else { // No mühle got closed
			if (getSourceLocTemp() == -1) { // No SourceLoc waiting in temp storage
				if (getPhase() == 's' && isLocationFree(locButtonPressed)) { // Setzphase
					setCurrentPlayerStone(locButtonPressed);
					checkMühle(locButtonPressed);
					if (!getIsMühleClosed())
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
					checkMühle(locButtonPressed);
					if (!getIsMühleClosed())
						nextMove();

				} else if (checkIfNeighbour(getSourceLocTemp(), locButtonPressed)) {
					deleteCurrentPlayerStone(getSourceLocTemp());
					setCurrentPlayerStone(locButtonPressed);
					setSourceLocTemp(-1);
					checkMühle(locButtonPressed);
					if (!getIsMühleClosed())
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
	 * Diese Methode wird ausgeführt sobald das Spiel beendet ist. Sie erhöht den
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
	 * Diese Methode lädt das Fenster neu.
	 */
	public static void refreshGui() {
		GUI.refreshMainContent();
		GUI.refreshSideBar();
	}

	/**
	 * Diese Methode wird vom "Computer" Spieler bei der Generierung der möglichen
	 * Züge genutzt. Mit dieser Methode wird geprüft ob sich der vom Computer
	 * gesetzte/verschobene Stein in einer Mühle befinden.
	 *
	 * @param sourceLoc Gibt den Ort an wo der Stein herkommt. In der Schiebe- und
	 *                  Sprungphase liegt der Wert zwischen 0 und 23 und gibt die
	 *                  Ursprungposition des Steines an.
	 * @param targetLoc the target loc
	 * @return boolean Ob der Stein eine Mühle geschlossen hat.
	 */
	public static boolean checkMühleMinMax(int sourceLoc, int targetLoc) {
		boolean mühle = false;
		int[][] arrMühle = mühlen[targetLoc];
		if (getPhase() == 24) {
			setCurrentPlayerStone(targetLoc);
			if (isLocationCurrentPlayer(arrMühle[0][0]) && isLocationCurrentPlayer(arrMühle[0][1])
					|| isLocationCurrentPlayer(arrMühle[1][0]) && isLocationCurrentPlayer(arrMühle[1][1])) {
				mühle = true;
			}
			deleteCurrentPlayerStone(targetLoc);
		} else {
			deleteCurrentPlayerStone(sourceLoc);
			setCurrentPlayerStone(targetLoc);
			if (isLocationCurrentPlayer(arrMühle[0][0]) && isLocationCurrentPlayer(arrMühle[0][1])
					|| isLocationCurrentPlayer(arrMühle[1][0]) && isLocationCurrentPlayer(arrMühle[1][1])) {
				mühle = true;
			}
			deleteCurrentPlayerStone(targetLoc);
			setCurrentPlayerStone(sourceLoc);
		}
		return mühle;

	}

	/**
	 * Diese Methode prüft / legt fest, in welcher Phase sich das Spiel aktuell
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
	 * Diese Methode prüft ob ein Stein on der Position loc, frei Felder neben sich
	 * hat. Wird benutzt um zu sehen ob dieser Stein verschoben werden kann, kann er
	 * nähmlich nur wenn er frei felder neben sich hat.
	 * 
	 * @param loc Position die überprüft werden soll.
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
	 * Diese Methode überpüft ob die Position locOne neben der Position locTwo
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
	 * Diese Methode prüft ob das Spiele beendet ist. Das ist der Fall, wenn ein
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
	 * Diese Methode überpüft ob sich der Stein an der Position loc in einer Mühle
	 * befindet.
	 * 
	 * @param loc Position des Steines der geprüft werden soll.
	 */
	public static void checkMühle(int loc) {
		// Check for Mühle
		int[][] arrMühle = mühlen[loc];
		if (isLocationCurrentPlayer(arrMühle[0][0]) && isLocationCurrentPlayer(arrMühle[0][1])) {
			setIsMühleClosed(true);
			refreshGui();
		} else if (isLocationCurrentPlayer(arrMühle[1][0]) && isLocationCurrentPlayer(arrMühle[1][1])) {
			setIsMühleClosed(true);
			refreshGui();
		}

	}

	/**
	 * Überprüft ob ein Stein entfernt werden kann. Ein Stein darf entfernt werden
	 * falls: 1. Er befindet sich in keiner Mühle. 2. Es gibt keine "freien" Steine
	 * die sich nicht in einer Mühle befinden.
	 *
	 * @param loc Position des Steines
	 * @return true, if successful
	 */
	public static boolean checkIfRemovable(int loc) {
		int freestones = 0;
		int[][] arrMühle = mühlen[loc];
		if (!(isLocationOtherPlayer(arrMühle[0][0]) && isLocationOtherPlayer(arrMühle[0][1])
				|| isLocationOtherPlayer(arrMühle[1][0]) && isLocationOtherPlayer(arrMühle[1][1]))) {
			// System.out.println("ist in keiner Mühle");
			return true;
		}

		for (int i = 0; i < 24; i++) {
			if (isLocationOtherPlayer(i)) {
				int[][] mühlenI = mühlen[i];
				if (!(isLocationOtherPlayer(mühlenI[0][0]) && isLocationOtherPlayer(mühlenI[0][1])
						|| isLocationOtherPlayer(mühlenI[1][0]) && isLocationOtherPlayer(mühlenI[1][1]))) {
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
