package Back_end;

/**
 * Mithilfe dieser Klasse werden Objekte erzeugt mit denen Z�ge gespeichert
 * werden k�nnen.
 * 
 * @author Nicolas Wyss
 *
 */
public class ObjMove {

	/**
	 * Variablen zum Speichern der Positionen, wo Steine gesetzt und gel�scht werden
	 * m�ssen.
	 */
	public int setStone;
	public int deleteStone;
	public int deleteStoneM�hle;

	/**
	 * Erstellt ein neues 'Zug'-Objekt
	 * 
	 * @param setstone         Position wo ein Stein gesetzt werden soll
	 * @param deletestone      Position wo ein Stein gel�scht werden soll (Falls
	 *                         einer verschoben wird)
	 * @param deletestonem�hle Zweite Position wo ein Stein gel�scht werden soll
	 *                         (Falls eine M�hle geschlossen wurde)
	 */
	public ObjMove(int setstone, int deletestone, int deletestonem�hle) {
		setStone = setstone;
		deleteStone = deletestone;
		deleteStoneM�hle = deletestonem�hle;
	}

}
