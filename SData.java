import java.util.ArrayList;
import java.util.Collection;

/**
*
* @author Marta Lo Cascio 532686
*/
public class SData{
	/* Overview: tipo di dato mutabile che rappresenta il dato dell'utente,
	 * 			 tenendo conto del creatore del dato e della lista di utenti a cui è stato condiviso
	 * Typical Element: <data, {other_0, other_1, ... , other_m-1}, owner> 
	 * 					dove:
	 * 					m è il numero di utenti a cui viene ondiviso il dato data
	 * 					other_i e owner sono String che rappresentano l'id di utenti registrati
	 * 					other_i != other_j per ogni 0 <= i,j < m
	 * 
	 * Rep Invariant: other_i != owner per ogni 0 <= i < n
	 * 
	 * Abstract Function:
	 */
	
	private byte[] data;
	private String owner;
	private Collection<String> otherCollection;

	public SData(String owner, byte[] data) {
		this.data = data;
		this.owner = owner;
		otherCollection = new ArrayList<>();
	}
	
	public void addOther(String other) {
		/* REQUIRES: other != null
		 * MODIFIES: this
		 * EFFECTS: aggiungo il nome "other" nella lista otherCollection che contiene
		 * 			gli id degli utenti a cui ho condiviso this.data
		 * */
		
		this.otherCollection.add(other);
	}
	
	public void removeOther(String other) {
		/* REQUIRES: other != null
		 * MODIFIES: this
		 * EFFECTS: rimuove il nome "other" nella lista otherCollection che contiene
		 * 			gli id degli utenti a cui ho condiviso this.data
		 * */
		
		this.otherCollection.remove(other);
	}
	
	public byte[] getData() {
		return data;
	}

	public String getOwner() {
		return this.owner;
	}
	
	public Collection<String> getOtherCollection() {
		return otherCollection;
	}	
}
