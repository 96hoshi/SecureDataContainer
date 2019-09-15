import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
*
* @author Marta Lo Cascio 532686
*/
public class User2nd implements Comparable<User2nd>{
	/* Overview: tipo di dato astratto che rappresenta un utente identificato da un id univoco e da una password passw.
	 * 			 Ogni utente ha la propria collezione di dati. 
	 * 			 Implementazione con Vector.
	 * Typical Element: <id, passw, {data_0, data_1, ..., data_k-1}>
	 * 					dove:
	 * 					k è il numero di dati che un utente possiede,
	 * 					nella lista di dati data_0, ..., data_k-1 ci sono sia dati propri che dati condivisi da altri utenti
	 * 
	 * Rep Invariant: id != null && passw != null && dataCollection.toArray[i] != null forall i. 0 <= i < dataCollection.size
	 * 
	 * Abstract Function: f(id, password, dataCollection) = <id, passw, {dataCollection.toArray()[0], ..., dataCollection.toArray()[size-1]}>
	 * 
	 */
	
	private String id;
	private String passw;
	private Collection <SData> dataCollection;
	
	public User2nd(String id, String passw){
		this.id = id;
		this.passw = passw;
		this.dataCollection = new Vector<>();
	}
	
	public SData searchData (byte[] data) throws IllegalArgumentException {
		/* REQUIRES: data != null
		 * THROWS: se non trova l'elemento data in dataCollectione lancia IllegalArgumentException
		 * 		   (eccezione presente in Java, unchecked)
		 * EFFECTS: cerca e restituisce, se lo trova, data nella dataCollection di this
		 * */
		
		for(SData sd : this.dataCollection) {
			if(Arrays.equals(data,sd.getData()))
				return sd;
		}
		throw new IllegalArgumentException("ERRORE: Dato non trovato");
	}
	
	public boolean addData(byte[] data) {	
		/* REQUIRES: data != null
		 * MODIFIES: this
		 * EFFECTS: crea un oggetto di topo SData e lo aggiunge alla collezione di dati di this
		 * */
		
		return this.dataCollection.add(new SData(this.id, data));
	}
	
	public void addShared(SData data_shared) {
		/* REQUIRES: data_shared != null
		 * MODIFIES: this
		 * EFFECTS: aggiunge un riferimento all'SData alla collezione di this
		 * */
		
		this.dataCollection.add(data_shared);
	}
	
	public SData removeData(byte[] data) throws IllegalArgumentException {
		/* REQUIRES: data != null
		 * THROWS: se non trova l'elemento data in dataCollectione lancia IllegalArgumentException
		 * 		   (eccezione presente in Java, unchecked)
		 * EFFECTS: restituisce un oggetto di tipo SData che contiene data, se trovato, rimosso da dataCollection di this
		 * */

		
		//cerco il dato
		for(SData sd : this.dataCollection) {
			
			if(Arrays.equals(data,sd.getData())) {
				SData remove = sd;
				this.dataCollection.remove(sd);
				return remove;
			}
		}
		throw new IllegalArgumentException("ERRORE: Dato non trovato, impossibile eliminare");
	}
	
	public boolean checkPassw(String passw) {
		/* REQUIRES: passw != null
		 * EFFECTS: mi dice se la password inserita corrisonde alla password di this
		 * */
		return this.passw.equals(passw);
	}
	

	public int getDataCollectionSize() {
		/*
		 *  EFFECTS: restituisce la dimensione della collezioni di dati che ha this
		 * */
		return this.dataCollection.size();
	}
	
	public Iterator<byte[]> userDataIterator() throws  UnsupportedOperationException{	
		/* THROWS: se si tenta di usare la remove dell'iteratoer lancia UnsupportedOperationException
		 * 		   (eccezione disponibile in Java, unchecked)
		 * EFFECTS: restituisce un iteratore dei dati
		 * */
		
		Object[] array = dataCollection.toArray();
		
		Iterator<byte[]> dataIterator = new Iterator<byte[]>() {
		
			private int index = 0;
			
	        @Override
	        public boolean hasNext() {
	            return index < array.length;
	        }
	
			@Override
	        public byte[] next() { 
	            return ((SData) array[index++]).getData();
	        }
	
	        @Override
	        public void remove() {
	            throw new UnsupportedOperationException("ERRORE: non puoi rimuovere il dato");
	        }
	    };
	    return dataIterator;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassw() {
		return this.passw;
	}
	
	@Override
	public int compareTo(User2nd o) {
		if (o == null)
			return -1;
		return this.id.compareTo(o.getId());
	}

}
