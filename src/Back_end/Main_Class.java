/**
 * Im Back_end Package befinden sich alle Klassen die sich mit der Datenverarbeitung beschäftigen und 
 * für den Benutzer nicht direkt ersichtlich sind.
 */
package Back_end;

import java.util.ArrayList;
import java.util.BitSet;

import Front_end.GUI;

/**
 * Das ist die Main Klasse meiner Mühle Anwendung. In dieser Klasse sind
 * verschiedene, globale Variablen zusammen mit deren Getter und
 * Setter-Methoden, gespeichert. Zudem befindet sich in dieser Klasse die Main
 * methode mit welcher das Programm gestartet wird.
 * 
 * @author Nicolas Wyss
 */
public class Main_Class {

	/**
	 * Speichert die Tiefe mit welcher der Computer züge berechnen soll.
	 */
	public static int minmaxdepth = 5;

	/**
	 * Die Bitsets werden benutzen um die Position der Steine zu merken. BitSet kann
	 * man dabei als boolean Array verstehen. Ist z.B. das 3.bit = true befindet
	 * sich an der Position 3 ein Stein dieser Farbe.
	 */
	private static BitSet bitSetWhiteStones = new BitSet();
	/**
	 * @see Main_Class#bitSetWhiteStones
	 */
	private static BitSet bitSetBlackStones = new BitSet();

	/**
	 * In diesem Array wird das "Design eines Steines gespeichert. Dabei gilt, 0 =
	 * normal, 1 = grüner Rand, 2 = 50% transparent.
	 */
	public static int[] designOfStone = new int[25];

	/**
	 * Hier werden die Anzahl Steine gespeichert die noch noch platziert wurden.
	 */
	private static int whiteStonesNotPlaced;
	/**
	 * @see Main_Class#whiteStonesNotPlaced
	 */
	private static int blackStonesNotPlaced;

	/**
	 * Hier wird die Anzahl gewonnener Spiele gespeichert.
	 */
	private static int scoreWhite;
	/**
	 * @see Main_Class#scoreWhite
	 */
	private static int scoreBlack;

	/**
	 * Hier wird gespeichert in welcher Phase sich das Spiel momentan befindet.
	 * Dabei gilt, s = Setzphase, m = (Move)/Schiebephase, j = (Jump)/Springphase.
	 * Da im deutschen alle Phasen mit 's' beginnen wurden hier die englischen
	 * anfangsbuchstaben benutzt.
	 */
	private static char phase;

	/**
	 * Hier wird in einer boolean gespeichert ob gerade weiss am Zug ist. Dies ist
	 * eine der wichtigsten Variablen da sie für das korekte abwechseln der Spieler
	 * zuständig ist.
	 */
	private static boolean isCurrentPlayerWhite;

	/**
	 * Hier wird gespeichert ob das Spiel beendet ist.
	 */
	private static boolean isGameOver;

	/**
	 * Wenn sich der echte Spieler in der Schiebe oder Springphase befindet, wird
	 * hier die Position des Steines zwischengespeichert, den er verschieben will.
	 * (Der stein der als erstes ausgewählt wurde.
	 */
	private static int sourceLocTemp = -1;

	/**
	 * Hier wird gespeichert ob in diesem Zug gerade eine Mühle geschlossen wurde.
	 */
	private static boolean isMühleClosedTemp;

	/**
	 * Hier wird gespeichert ob eine Farbe vom Computer oder von einem echten
	 * Spieler gesteuert wird.
	 */
	private static boolean isWhiteHuman = true;
	/**
	 * @see Main_Class#isWhiteHuman
	 */
	private static boolean isBlackHuman;

	/**
	 * Hier wird gespeicher ob das Spiel gerade läuft.
	 */
	private static boolean isGameRunning;

	/**
	 * In dieser ArrayList werden alle Spielsituationen gespeichert.
	 */
	public static ArrayList<ObjSituation> dataSituations = new ArrayList<ObjSituation>();

	/**
	 * In diesem int[][] Array werden alle Nachbaren jeder Position gespeichert.
	 * Beispiel: Um die Nachbaren der Position 4 zu bekommen braucht man das int[]
	 * neighboursVon4 = neighbours[4]. In neighboursVon4 sind dann die Nachbaren von
	 * der Position 4 gespeichert.
	 */
	public static int[][] neighbours = { { 1, 7 }, { 0, 2, 9 }, { 1, 3 }, { 2, 4, 11 }, { 3, 5 }, { 4, 6, 13 },
			{ 5, 7 }, { 0, 6, 15 }, { 9, 15 }, { 1, 8, 10, 17 }, { 9, 11 }, { 3, 10, 12, 19 }, { 11, 13 },
			{ 5, 12, 14, 21 }, { 13, 15 }, { 7, 8, 14, 23 }, { 17, 23 }, { 9, 16, 18 }, { 17, 19 }, { 11, 18, 20 },
			{ 19, 21 }, { 13, 20, 22 }, { 21, 23 }, { 15, 16, 22 } };

	/**
	 * In diesem int[][][] array wird zu jeder Position die dazugehörigen senkrechte
	 * und waagrechte Mühle abgespeichert. Beispiel: Um die Mühle der Position 14 zu
	 * bekommen braucht man erst das int[][] mühlenVon14 = mühlen[14]. In
	 * mühlenVon14[0] ist dann z.b. die senkrechte Mühle und in mühlenVon14[1] die
	 * waagrechte Mühle gespeichert.
	 */
	public static int[][][] mühlen = { { { 1, 2 }, { 6, 7 } }, { { 0, 2 }, { 9, 17 } }, { { 0, 1 }, { 3, 4 } },
			{ { 11, 19 }, { 2, 4 } }, { { 5, 6 }, { 2, 3 } }, { { 4, 6 }, { 13, 21 } }, { { 4, 5 }, { 0, 7 } },
			{ { 15, 23 }, { 0, 6 } }, { { 9, 10 }, { 14, 15 } }, { { 8, 10 }, { 1, 17 } }, { { 8, 9 }, { 11, 12 } },
			{ { 3, 19 }, { 10, 12 } }, { { 13, 14 }, { 10, 11 } }, { { 12, 14 }, { 5, 21 } }, { { 12, 13 }, { 8, 15 } },
			{ { 7, 23 }, { 8, 14 } }, { { 17, 18 }, { 22, 23 } }, { { 16, 18 }, { 1, 9 } }, { { 16, 17 }, { 19, 20 } },
			{ { 3, 11 }, { 18, 20 } }, { { 21, 22 }, { 18, 19 } }, { { 20, 22 }, { 5, 13 } },
			{ { 20, 21 }, { 16, 23 } }, { { 7, 15 }, { 16, 22 } }

	};

	public static void setSourceLocTemp(int loc) {
		sourceLocTemp = loc;
	}

	public static void setScoreWhite(int score) {
		scoreWhite = score;
	}

	public static void setScoreBlack(int score) {
		scoreBlack = score;
	}

	public static void increaseScoreCurrentPlayer() {
		if (isCurrentPlayerWhite)
			scoreWhite = scoreWhite + 1;
		else
			scoreBlack = scoreBlack + 1;
	}

	public static void setIsCurrentPlayerWhite(boolean setToWhite) {
		isCurrentPlayerWhite = setToWhite;
	}

	public static void setIsMühleClosed(boolean mühleClosed) {
		isMühleClosedTemp = mühleClosed;
	}

	public static void setIsGameOver(boolean gameover) {
		isGameOver = gameover;
	}

	public static void clearBtnBorder() {
		for (int i = 0; i < 24; i++)
			designOfStone[i] = 0;
	}

	public static void setBitSetWhiteStones(BitSet whiteBitSet) {
		bitSetWhiteStones = whiteBitSet;
	}

	public static void setBitSetBlackStones(BitSet blackBitSet) {
		bitSetBlackStones = blackBitSet;
	}

	public static void setPhase(char setPhase) {
		if (setPhase == 's' || setPhase == 'm' || setPhase == 'j') {
			phase = setPhase;
		} else {
			System.err.println("Unvalid Phase");
		}
	}

	public static void setWhiteStonesNotPlaced(int stones) {
		whiteStonesNotPlaced = stones;
	}

	public static void setBlackStonesNotPlaced(int stones) {
		blackStonesNotPlaced = stones;
	}

	public static void setCurrentPlayerStonesNotPlaced(int stones) {
		if (isCurrentPlayerWhite)
			whiteStonesNotPlaced = stones;
		else
			blackStonesNotPlaced = stones;
	}

	public static void setWhiteStone(int loc) {
		bitSetWhiteStones.set(loc);
	}

	public static void setBlackStone(int loc) {
		bitSetBlackStones.set(loc);
	}

	public static void setCurrentPlayerStone(int loc) {
		if (isCurrentPlayerWhite)
			bitSetWhiteStones.set(loc);
		else
			bitSetBlackStones.set(loc);
	}

	public static void setOtherPlayerStone(int loc) {
		if (!isCurrentPlayerWhite)
			bitSetWhiteStones.set(loc);
		else
			bitSetBlackStones.set(loc);
	}

	public static void deleteWhiteStone(int loc) {
		bitSetWhiteStones.clear(loc);
	}

	public static void deleteBlackStone(int loc) {
		bitSetBlackStones.clear(loc);
	}

	public static void deleteCurrentPlayerStone(int loc) {
		if (isCurrentPlayerWhite)
			bitSetWhiteStones.clear(loc);
		else
			bitSetBlackStones.clear(loc);
	}

	public static void deleteOtherPlayerStone(int loc) {
		if (!isCurrentPlayerWhite)
			bitSetWhiteStones.clear(loc);
		else
			bitSetBlackStones.clear(loc);
	}

	public static void clearAllStones() {
		bitSetBlackStones.clear();
		bitSetWhiteStones.clear();
	}

	public static void setIsGameRunningTrue() {
		isGameRunning = true;
	}

	public static void setIsGameRunningFalse() {
		isGameRunning = false;
	}

	public static void changePlayerWhite() {
		isWhiteHuman = !isWhiteHuman;
	}

	public static void changePlayerBlack() {
		isBlackHuman = !isBlackHuman;
	}

	public static void changeCurrentPlayer() {
		isCurrentPlayerWhite = !isCurrentPlayerWhite;
	}

	public static void changeMühleClosed() {
		isMühleClosedTemp = !isMühleClosedTemp;
	}

	public static int getSourceLocTemp() {
		return sourceLocTemp;
	}

	public static int getScoreWhite() {
		return scoreWhite;
	}

	public static int getScoreBlack() {
		return scoreBlack;
	}

	public static boolean getIsCurrentPlayerWhite() {
		return isCurrentPlayerWhite;
	}

	public static boolean getIsMühleClosed() {
		return isMühleClosedTemp;
	}

	public static boolean getIsWhiteHuman() {
		return isWhiteHuman;
	}

	public static boolean getIsBlackHuman() {
		return isBlackHuman;
	}

	public static boolean getIsCurrentPlayerHuman() {
		if (isCurrentPlayerWhite)
			return isWhiteHuman;
		else
			return isBlackHuman;
	}

	public static boolean getIsGameRunning() {
		return isGameRunning;
	}

	public static boolean getIsGameOver() {
		return isGameOver;
	}

	public static int getWhiteStonesNotPlaced() {
		return whiteStonesNotPlaced;
	}

	public static int getBlackStonesNotPlaced() {
		return blackStonesNotPlaced;
	}

	public static int getCurrentPlayerStonesNotPlaced() {
		if (isCurrentPlayerWhite)
			return whiteStonesNotPlaced;
		else
			return blackStonesNotPlaced;
	}

	public static int getOtherPlayerStonesNotPlaced() {
		if (!isCurrentPlayerWhite)
			return whiteStonesNotPlaced;
		else
			return blackStonesNotPlaced;
	}

	public static char getPhase() {
		return phase;
	}

	public static BitSet getbitSetWhiteStones() {
		BitSet whiteStones = (BitSet) bitSetWhiteStones.clone();
		return whiteStones;
	}

	public static BitSet getbitSetBlackStones() {
		BitSet blackStones = (BitSet) bitSetBlackStones.clone();
		return blackStones;
	}

	public static BitSet getbitSetCurrentPlayer() {
		BitSet whiteStones = (BitSet) bitSetWhiteStones.clone();
		BitSet blackStones = (BitSet) bitSetBlackStones.clone();
		if (isCurrentPlayerWhite)
			return whiteStones;
		else
			return blackStones;
	}

	public static BitSet getbitSetOtherPlayer() {
		BitSet whiteStones = (BitSet) bitSetWhiteStones.clone();
		BitSet blackStones = (BitSet) bitSetBlackStones.clone();
		if (!isCurrentPlayerWhite)
			return whiteStones;
		else
			return blackStones;
	}

	public static boolean isLocationCurrentPlayer(int location) {
		if (isCurrentPlayerWhite && getbitSetWhiteStones().get(location))
			return true;
		else if (!isCurrentPlayerWhite && getbitSetBlackStones().get(location))
			return true;
		else
			return false;
	}

	public static boolean isLocationOtherPlayer(int location) {
		if (!isCurrentPlayerWhite && getbitSetWhiteStones().get(location))
			return true;
		else if (isCurrentPlayerWhite && getbitSetBlackStones().get(location))
			return true;
		else
			return false;
	}

	public static boolean isLocationWhite(int location) {
		if (getbitSetWhiteStones().get(location))
			return true;
		else
			return false;
	}

	public static boolean isLocationBlack(int location) {
		if (getbitSetBlackStones().get(location))
			return true;
		else
			return false;
	}

	public static boolean isLocationFree(int Location) {
		if (getbitSetWhiteStones().get(Location) || getbitSetBlackStones().get(Location))
			return false;
		else
			return true;
	}

	/**
	 * Diese Methode startet das Programm, indem sie das Gui erstellt.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new GUI();
	}

}
