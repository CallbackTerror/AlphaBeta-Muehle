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
	 * Führt den Zug aus, der Vom Computer als "bester Zug" errechnet wurde.
	 */
	public static void makeBestMove() {
		System.err.println(numberofmovesevaluated);
		System.out.println("bestmove + set " + bestMoveTemp.setStone + "  del " + bestMoveTemp.deleteStone
				+ "  del Mühl " + bestMoveTemp.deleteStoneMühle + "depth" + bestmovedepthTemp);
		;
		Board.checkPhase();
		setCurrentPlayerStone(bestMoveTemp.setStone);
		deleteCurrentPlayerStone(bestMoveTemp.deleteStone);
		deleteOtherPlayerStone(bestMoveTemp.deleteStoneMühle);
		designOfStone[bestMoveTemp.setStone] = 1;
		designOfStone[bestMoveTemp.deleteStoneMühle] = 2;
		bestMoveTemp = null;
		Board.nextMove();
	}

	/**
	 * berechnet den Besten Zug für den Computer.
	 * 
	 * @param depth Tiefe mit der der Computer Züge suchen soll.
	 * @param alpha speichert den wert des momentan besten Zuges (startet bei
	 *              -100000)
	 * @param beta  definiert den wert der überschritten werden muss, damit das
	 *              Prunning eintrifft
	 * @return
	 */
	public static int AlphaBeta(int depth, int alpha, int beta) {
		/**
		 * Falls die entsprechende depth je nach Phase erreicht wurde, wird die aktuelle
		 * Spielsituation ausgewertet und der Wert zurückgegeben.
		 */
		if (getPhase() == 'j' && depth <= 1)
			return Heuristic.Evaluate(getIsCurrentPlayerWhite());
		if (depth == 0)
			return Heuristic.Evaluate(getIsCurrentPlayerWhite());

		/**
		 * erzeugt alle möglichen Züge in der aktuellen Spielsituation und speichert
		 * diese in einer ArrayList ab.
		 */
		ArrayList<ObjMove> moves = GenerateLegalMoves();
		if (moves.size() == 0) {
			System.out.println();
			return -beta;
		}
		for (int i = 0; i < moves.size(); i++) { 				// Schleife um alle Züge in Moves durchzuführen
			MakeNextMove(moves.get(i)); 						// führt den Zug durch
			int val = -AlphaBeta(depth - 1, -beta, -alpha); 	// erzeugt die neuen Zug zur Situation oder erhält den Wert
																// der Situation (Rekursiv-aufruf)
			UnmakeMove(moves.get(i)); 							// Macht den Zug rückgängig
			if (val >= beta)
				return beta; 									// Prunning falls der zug besser ist als Beta
			if (val > alpha) { 									// Erhöht alpha falls val besser ist als der letzt-beste Zug
				alpha = val;
				if (depth == minmaxdepth)
					bestMoveTemp = moves.get(i); 				// Der letzte Zug auf depth = suchtiefe wird gespeichert
			}
		}
		return alpha; 											// Sobald alle Züge durchgeführt wurde wird Alpha zurückgegeben
	}

	/**
	 * Diese Methode führt den aktuellen Zug durch
	 * 
	 * @param moves Zug der durchgeführt werden soll
	 */
	public static void MakeNextMove(ObjMove moves) {
		setCurrentPlayerStone(moves.setStone);
		deleteCurrentPlayerStone(moves.deleteStone);
		deleteOtherPlayerStone(moves.deleteStoneMühle);
		changeCurrentPlayer();
	}

	/**
	 * Diese Methode macht den aktuellen Zug rückgängig
	 * 
	 * @param moves Zug der rückgängig gemacht werden soll
	 */
	public static void UnmakeMove(ObjMove moves) {
		changeCurrentPlayer();
		deleteCurrentPlayerStone(moves.setStone);
		if (moves.deleteStone != 24)
			setCurrentPlayerStone(moves.deleteStone);
		if (moves.deleteStoneMühle != 24)
			setOtherPlayerStone(moves.deleteStoneMühle);

	}

	/**
	 * Diese Methode generiert zu der aktuellen Situation alle möglichen Züge für
	 * den aktuellen Spieler. Am ende gibt sie eine ArrayList ObjMove mit allen
	 * möglichen Zügen zurück.
	 * 
	 * @return movesList ArrayList die alle möglichen Züge enthält.
	 */
	public static ArrayList<ObjMove> GenerateLegalMoves() {
		ArrayList<ObjMove> movesList = new ArrayList<ObjMove>();
		for (int locOne = 0; locOne < 24; locOne++) {
			if (getPhase() == 's') {
				if (isLocationFree(locOne)) {
					if (Board.checkMühleMinMax(24, locOne)) {
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
							if (Board.checkMühleMinMax(locOne, locTwo)) {
								for (int locThree = 0; locThree < 24; locThree++) {
									if (isLocationOtherPlayer(locThree) && Board.checkIfRemovable(locThree)) {
										// System.out.println("set " + locOne + "del " + locTwo + "delMüöl " +
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
							if (Board.checkMühleMinMax(locOne, locTwo)) {
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
 * Klasse zur Bewertung von Spielzügen.
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
	private static int valueOfMühle = 1;

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
		int numberOfMühlenWhite = 0;
		int numberOfMühlenBlack = 0;
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
			int[][] arrmühlen = mühlen[i];
			if (isLocationWhite(i) && isLocationWhite(arrmühlen[0][0]) && isLocationWhite(arrmühlen[0][1]))
				numberOfMühlenWhite++;
			if (isLocationWhite(i) && isLocationWhite(arrmühlen[1][0]) && isLocationWhite(arrmühlen[1][1]))
				numberOfMühlenWhite++;
			if (isLocationBlack(i) && isLocationBlack(arrmühlen[0][0]) && isLocationBlack(arrmühlen[0][1]))
				numberOfMühlenBlack++;
			if (isLocationBlack(i) && isLocationBlack(arrmühlen[1][0]) && isLocationBlack(arrmühlen[1][1]))
				numberOfMühlenBlack++;
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
			situationValue += valueOfMühle*numberOfMühlenWhite;
			situationValue -= valueOfMühle*numberOfMühlenBlack;
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
			situationValue -= valueOfMühle*numberOfMühlenWhite;
			situationValue += valueOfMühle*numberOfMühlenBlack;
		}
		return situationValue;
	}

}
