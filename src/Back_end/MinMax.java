package Back_end;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

import org.omg.CORBA.PUBLIC_MEMBER;

public class MinMax extends Main_Class {

	public static ObjMove bestMoveTemp;
	public static int bestmovedepthTemp;
	public static boolean isCurrentMinMaxPlayerWhite;
	public static int numberofmovesevaluated = 0;
	public static long time = 1000;
	public static boolean timeOver;

	
	
	/**
	 * F�hrt den Zug aus, der Vom Computer als "bester Zug" errechnet wurde.
	 */
	public static void makeBestMove() {
		System.err.println(numberofmovesevaluated);
		System.out.println("bestmove + set " + bestMoveTemp.setStone + "  del " + bestMoveTemp.deleteStone
				+ "  del M�hl " + bestMoveTemp.deleteStoneM�hle + "depth" + bestmovedepthTemp);
		;
		Board.checkPhase();
		setCurrentPlayerStone(bestMoveTemp.setStone);
		deleteCurrentPlayerStone(bestMoveTemp.deleteStone);
		deleteOtherPlayerStone(bestMoveTemp.deleteStoneM�hle);
		designOfStone[bestMoveTemp.setStone] = 1;
		designOfStone[bestMoveTemp.deleteStoneM�hle] = 2;
		bestMoveTemp = null;
		Board.nextMove();
	}

	/**
	 * berechnet den Besten Zug f�r den Computer.
	 * 
	 * @param depth Tiefe mit der der Computer Z�ge suchen soll.
	 * @param alpha speichert den wert des momentan besten Zuges (startet bei
	 *              -100000)
	 * @param beta  definiert den wert der �berschritten werden muss, damit das
	 *              Prunning eintrifft
	 * @return
	 */
	public static int AlphaBeta(int depth, int alpha, int beta) {
		/**
		 * Falls die entsprechende depth je nach Phase erreicht wurde, wird die aktuelle
		 * Spielsituation ausgewertet und der Wert zur�ckgegeben.
		 */
		if (getPhase() == 'j' && depth <= 1)
			return Heuristic.Evaluate(getIsCurrentPlayerWhite());
		if (depth == 0)
			return Heuristic.Evaluate(getIsCurrentPlayerWhite());

		/**
		 * erzeugt alle m�glichen Z�ge in der aktuellen Spielsituation und speichert
		 * diese in einer ArrayList ab.
		 */
		ArrayList<ObjMove> moves = GenerateLegalMoves();
		if (moves.size() == 0) {
			System.out.println();
			return -beta;
		}
		for (int i = 0; i < moves.size(); i++) { 				// Schleife um alle Z�ge in Moves durchzuf�hren
			MakeNextMove(moves.get(i)); 						// f�hrt den Zug durch
			int val = -AlphaBeta(depth - 1, -beta, -alpha); 	// erzeugt die neuen Zug zur Situation oder erh�lt den Wert
																// der Situation (Rekursiv-aufruf)
			UnmakeMove(moves.get(i)); 							// Macht den Zug r�ckg�ngig
			if (val >= beta)
				return beta; 									// Prunning falls der zug besser ist als Beta
			if (val > alpha) { 									// Erh�ht alpha falls val besser ist als der letzt-beste Zug
				alpha = val;
				if (depth == minmaxdepth)
					bestMoveTemp = moves.get(i); 				// Der letzte Zug auf depth = suchtiefe wird gespeichert
			}
		}
		return alpha; 											// Sobald alle Z�ge durchgef�hrt wurde wird Alpha zur�ckgegeben
	}

	/**
	 * Diese Methode f�hrt den aktuellen Zug durch
	 * 
	 * @param moves Zug der durchgef�hrt werden soll
	 */
	public static void MakeNextMove(ObjMove moves) {
		setCurrentPlayerStone(moves.setStone);
		deleteCurrentPlayerStone(moves.deleteStone);
		deleteOtherPlayerStone(moves.deleteStoneM�hle);
		changeCurrentPlayer();
	}

	/**
	 * Diese Methode macht den aktuellen Zug r�ckg�ngig
	 * 
	 * @param moves Zug der r�ckg�ngig gemacht werden soll
	 */
	public static void UnmakeMove(ObjMove moves) {
		changeCurrentPlayer();
		deleteCurrentPlayerStone(moves.setStone);
		if (moves.deleteStone != 24)
			setCurrentPlayerStone(moves.deleteStone);
		if (moves.deleteStoneM�hle != 24)
			setOtherPlayerStone(moves.deleteStoneM�hle);

	}

	/**
	 * Diese Methode generiert zu der aktuellen Situation alle m�glichen Z�ge f�r
	 * den aktuellen Spieler. Am ende gibt sie eine ArrayList ObjMove mit allen
	 * m�glichen Z�gen zur�ck.
	 * 
	 * @return movesList ArrayList die alle m�glichen Z�ge enth�lt.
	 */
	public static ArrayList<ObjMove> GenerateLegalMoves() {
		ArrayList<ObjMove> movesList = new ArrayList<ObjMove>();
		for (int locOne = 0; locOne < 24; locOne++) {
			if (getPhase() == 's') {
				if (isLocationFree(locOne)) {
					if (Board.checkM�hleMinMax(24, locOne)) {
						for (int locThree = 0; locThree < 24; locThree++) {
							if (isLocationOtherPlayer(locThree) && Board.checkIfRemovable(locThree)) {
								movesList.add(new ObjMove(locOne, 24, locThree));
							}
						}
					} else {
						movesList.add(new ObjMove(locOne, 24, 24));
					}
				}
			} else if (getPhase() == 'm') {
				if (getbitSetCurrentPlayer().get(locOne)) {
					int[][] neighs = neighbours;
					for (int locTwo = 0; locTwo < 24; locTwo++) {
						final int two = locTwo;
						if (IntStream.of(neighs[locOne]).anyMatch(x -> x == two) && isLocationFree(locTwo)) {
							if (Board.checkM�hleMinMax(locOne, locTwo)) {
								for (int locThree = 0; locThree < 24; locThree++) {
									if (isLocationOtherPlayer(locThree) && Board.checkIfRemovable(locThree)) {
										// System.out.println("set " + locOne + "del " + locTwo + "delM��l " +
										// locThree);
										movesList.add(new ObjMove(locTwo, locOne, locThree));
									}
								}
							} else {
								movesList.add(new ObjMove(locTwo, locOne, 24));

							}
						}
					}
				}
			} else if (getPhase() == 'j') {
				if (getbitSetCurrentPlayer().get(locOne)) {
					for (int locTwo = 0; locTwo < 24; locTwo++) {
						if (isLocationFree(locTwo)) {
							if (Board.checkM�hleMinMax(locOne, locTwo)) {
								for (int locThree = 0; locThree < 24; locThree++) {
									if (isLocationOtherPlayer(locThree) && Board.checkIfRemovable(locThree)) {
										movesList.add(new ObjMove(locTwo, locOne, locThree));
									}
								}
							} else {
								movesList.add(new ObjMove(locTwo, locOne, 24));
							}
						}
					}
				}
			}
		}
		return movesList;

	}
}

/**
 * Klasse zur Bewertung von Spielz�gen.
 * 
 * @author Nicolas Wyss
 *
 */
class Heuristic extends Main_Class {

	/**
	 * Drei Variablen zur Bewertung der Situationen.
	 */
	private static int valueOfStones = 10;
	private static int valueOfMoves = 3;
	private static int valueOfM�hle = 1;

	/**
	 * Berechnet den Wert der aktuellen Spielsituation
	 * 
	 * @param isWhiteCurrentPlayer
	 * @return Wert der aktuellen Situation
	 */
	public static int Evaluate(boolean isWhiteCurrentPlayer) {
		MinMax.numberofmovesevaluated++;
		int situationValue = 0;
		int numberOfStonesWhite = 0;
		int numberOfStonesBlack = 0;
		int numberOfMovesWhite = 0;
		int numberOfMovesBlack = 0;
		int numberOfM�hlenWhite = 0;
		int numberOfM�hlenBlack = 0;
		for (int i = 0; i < 24; i++) {
			int[] arrneigh = neighbours[i];
			for (int j = 0; j < arrneigh.length; j++) {
				if (isLocationWhite(i))
					if (isLocationFree(arrneigh[j]))
						numberOfMovesWhite++;
				if (isLocationBlack(i))
					if (isLocationFree(arrneigh[j]))
						numberOfMovesBlack++;
			}
		}
		for (int i = 0; i < 24; i++) {
			int[][] arrm�hlen = m�hlen[i];
			if (isLocationWhite(i) && isLocationWhite(arrm�hlen[0][0]) && isLocationWhite(arrm�hlen[0][1]))
				numberOfM�hlenWhite++;
			if (isLocationWhite(i) && isLocationWhite(arrm�hlen[1][0]) && isLocationWhite(arrm�hlen[1][1]))
				numberOfM�hlenWhite++;
			if (isLocationBlack(i) && isLocationBlack(arrm�hlen[0][0]) && isLocationBlack(arrm�hlen[0][1]))
				numberOfM�hlenBlack++;
			if (isLocationBlack(i) && isLocationBlack(arrm�hlen[1][0]) && isLocationBlack(arrm�hlen[1][1]))
				numberOfM�hlenBlack++;
		}
		numberOfStonesWhite = getbitSetWhiteStones().cardinality();
		numberOfStonesBlack = getbitSetBlackStones().cardinality();
		if (isWhiteCurrentPlayer) {
			if (numberOfStonesBlack < 3 && getBlackStonesNotPlaced() == 0)
				situationValue += 2000;

			if (numberOfStonesWhite < 3 && getWhiteStonesNotPlaced() == 0)
				situationValue -= 1000;

			if (numberOfMovesBlack == 0 && getBlackStonesNotPlaced() == 0)
				situationValue += 2000;

			if (numberOfMovesWhite == 0 && getWhiteStonesNotPlaced() == 0)
				situationValue -= 1000;

			if (numberOfStonesWhite > 3)
				situationValue += valueOfStones * (numberOfStonesWhite - 2);
			if (numberOfStonesBlack > 3)
				situationValue -= valueOfStones * (numberOfStonesBlack - 2);
			if (numberOfMovesWhite > 0)
				situationValue += valueOfMoves * numberOfMovesWhite;
			if (numberOfMovesBlack > 0)
				situationValue -= valueOfMoves * numberOfMovesBlack;
			situationValue += valueOfM�hle*numberOfM�hlenWhite;
			situationValue -= valueOfM�hle*numberOfM�hlenBlack;
		} else {
			if (numberOfStonesWhite < 3 && getWhiteStonesNotPlaced() == 0)
				situationValue += 2000;
			if (numberOfStonesBlack < 3 && getBlackStonesNotPlaced() == 0)
				situationValue -= 1000;
			if (numberOfMovesWhite == 0 && getWhiteStonesNotPlaced() == 0)
				situationValue += 2000;
			if (numberOfMovesBlack == 0 && getBlackStonesNotPlaced() == 0)
				situationValue -= 1000;
			if (numberOfStonesWhite > 3)
				situationValue -= valueOfStones * (numberOfStonesWhite - 2);
			if (numberOfStonesBlack > 3)
				situationValue += valueOfStones * (numberOfStonesBlack - 2);
			if (numberOfMovesWhite > 0)
				situationValue -= valueOfMoves * numberOfMovesWhite;
			if (numberOfMovesBlack > 0)
				situationValue += valueOfMoves * numberOfMovesBlack;
			situationValue -= valueOfM�hle*numberOfM�hlenWhite;
			situationValue += valueOfM�hle*numberOfM�hlenBlack;
		}
		return situationValue;
	}

}
