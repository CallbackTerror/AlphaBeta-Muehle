package Back_end;

import java.util.BitSet;

/**
 * Mithilfe dieser Klasse werden Objekte erzeugt mit denen Züge gespeichert
 * werden können.
 * 
 * @author Nicolas Wyss
 */
public class ObjSituation {

	/**
	 * Positionen der Steine
	 */
	BitSet s_bitSetWhiteStones = new BitSet();
	BitSet s_bitSetBlackStones = new BitSet();

	/**
	 * aktueller Spieler
	 */
	boolean s_isCurrentPlayerWhite;

	/**
	 * aktuelle Phase
	 */
	char s_phase;

	/**
	 * Anzahl nicht gesetzer Steine
	 */
	int s_whiteStonesNotPlaced;
	int s_blackStonesNotPlaced;

	/**
	 * Erstellt das Situations Objekt
	 * 
	 * @param whitestones          @see {@link ObjSituation#s_bitSetWhiteStones}
	 * @param blackstones          @see {@link ObjSituation#s_bitSetWhiteStones}
	 * @param isCurrentPlayerWhite @see {@link ObjSituation#s_isCurrentPlayerWhite}
	 * @param phase                @see {@link ObjSituation#s_phase}
	 * @param whitestonesNotPlaced @see {@link ObjSituation#s_whiteStonesNotPlaced}
	 * @param blackstonesNotPlaced @see {@link ObjSituation#s_whiteStonesNotPlaced}
	 */
	public ObjSituation(BitSet whitestones, BitSet blackstones, boolean isCurrentPlayerWhite, char phase,
			int whitestonesNotPlaced, int blackstonesNotPlaced) {
		s_bitSetWhiteStones = whitestones;
		s_bitSetBlackStones = blackstones;
		s_isCurrentPlayerWhite = isCurrentPlayerWhite;
		s_phase = phase;
		s_whiteStonesNotPlaced = whitestonesNotPlaced;
		s_blackStonesNotPlaced = blackstonesNotPlaced;
	}

	/**
	 * Getter und Setter Methoden.
	 */
	
	public BitSet getWhiteStones() {
		return s_bitSetWhiteStones;
	}

	public BitSet getBlackStones() {
		return s_bitSetBlackStones;
	}

	public char getPhase() {
		return s_phase;
	}

	public int getWhiteStonesNotPlaced() {
		return s_whiteStonesNotPlaced;
	}

	public int getBlackStonesNotPlaced() {
		return s_blackStonesNotPlaced;
	}

	public boolean getIsCurrentPlayerWhite() {
		return s_isCurrentPlayerWhite;
	}

}
