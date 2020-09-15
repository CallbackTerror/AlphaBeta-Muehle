package Back_end;

/**
 * Mithilfe dieser Klasse werden Objekte erzeugt mit denen Züge gespeichert
 * werden können.
 * 
 * @author Nicolas Wyss
 *
 */
public class ObjMove {

	/**
	 * Variablen zum Speichern der Positionen, wo Steine gesetzt und gelöscht werden
	 * müssen.
	 */
	public int setStone;
	public int deleteStone;
	public int deleteStoneMühle;

	/**
	 * Erstellt ein neues 'Zug'-Objekt
	 * 
	 * @param setstone         Position wo ein Stein gesetzt werden soll
	 * @param deletestone      Position wo ein Stein gelöscht werden soll (Falls
	 *                         einer verschoben wird)
	 * @param deletestonemühle Zweite Position wo ein Stein gelöscht werden soll
	 *                         (Falls eine Mühle geschlossen wurde)
	 */
	public ObjMove(int setstone, int deletestone, int deletestonemühle) {
		setStone = setstone;
		deleteStone = deletestone;
		deleteStoneMühle = deletestonemühle;
	}

}
