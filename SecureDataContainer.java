import java.util.Iterator;

/**
*
* @author Marta Lo Cascio 532686
*/
public interface SecureDataContainer<E>{
	/* Overview: Tipo modificabile di Data Storage al cui interno gestisce un'insieme di utenti,
	 * 			 ognuno con la propria collezione di dati di tipo E.
	 */
	
	// Crea l’identità un nuovo utente della collezione
	public void createUser(String Id, String passw);
	/* REQUIRES: Id != null && passw != null && Id non appartiene già alla collezione
	 * MODIFIES: this
	 * THROWS: se Id o passw sono null lancia NullPointerException 
	 * 				(eccezione disponibile in Java, unchecked) 
	 * 		   se Id è già presente in lista lancia IllegalArgumentException
	 * 				(eccezione disponibile in Java, unchecked)
	 * EFFECTS: aggiunge un Utente con il proprio "Id" univoco e la password "passw" alla collezione
	 * */
	
	// Rimuove l’utente dalla collezione
	public void RemoveUser(String Id, String passw);
	/* REQUIRES: Id != null && passw != null
	 * MODIFIES: this
	 * THROWS: se Id o passw sono null lancia NullPointerException 
	 * 				(eccezione disponibile in Java, unchecked) 
	 * 		   se Id non è presente nella collezione o passw non è relativa all'Utente Id 
	 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: elimina un Utente "Id" dalla collezione se esiste
	 * */
	
	// Restituisce il numero degli elementi di un utente presenti nella
	// collezione
	public int getSize(String Owner, String passw);
	/* REQUIRES: Owner != null && passw != null
	 * THROWS: se Owner o passw sono null lancia NullPointerException 
	 * 				(eccezione disponibile in Java, unchecked) 
	 * 		   se Owner non è presente nella collezione o passw non è relativa all'Utente Owner 
	 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce il numero intero di dati posseduti da "Owner"
	 * */
	
	// Inserisce il valore del dato nella collezione
	// se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E data) throws Exception;
	/* REQUIRES: Owner != null && passw != null && data != null
	 * MODIFIES: this
	 * THROWS: se Owner, passw o data sono null lancia NullPointerException 
	 * 				(eccezione disponibile in Java, unchecked)
	 *		   se Owner non è presente nella collezione o passw non è relativa all'Utente Owner 
	 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)	 
	 *  	   se la criptazione e la decriptazione non va a buon fine 
	 * 				lancia Exception (checked)
	 * EFFECTS: inserisce il valore "data" nella collezione dell'Utente "Owner"
	 * */
	
	// Ottiene una copia del valore del dato nella collezione
	// se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E data) throws Exception;
	/* REQUIRES: Owner != null && passw != null && data != null
	 * THROWS: se Owner, passw o data sono null lancia NullPointerException 
	 * 				(eccezione disponibile in Java, unchecked)
	 *		   se Owner o data non sono presenti nella collezione o passw non è relativa all'Utente Owner 
	 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)	 
	 *  	   se la criptazione e la decriptazione non va a buon fine 
	 * 				lancia Exception (checked)
	 * EFFECTS:viene restituita una copia del valore "data" relativa all'Utente "Owner"
	 * */
	
	// Rimuove il dato nella collezione
	// se vengono rispettati i controlli di identità
	public E remove(String Owner, String passw, E data) throws Exception;
	/* REQUIRES: Owner != null && passw != null && data != null
	 * MODIFIES: this
	 * THROWS: se Owner, passw o data sono null lancia NullPointerException 
	 * 				(eccezione disponibile in Java, unchecked)
	 *		   se Owner o data non sono presenti nella collezione o passw non è relativa all'Utente Owner 
	 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)	 
	 *  	   se la criptazione e la decriptazione non va a buon fine 
	 * 				lancia Exception (checked)
	 * EFFECTS: rimuove e restituisce il valore "data" dalla collezione di "Owner":
	 * 				se il creatore del dato "data" è "Owner" allora elimina ogni occorrenza di "data" condivisa con gli altri utenti autorizzati
	 * 				altrimenti elimina solo la propria copia
	 * */
	
	// Crea una copia del dato nella collezione
	// se vengono rispettati i controlli di identità
	public void copy(String Owner, String passw, E data) throws Exception;
	/* REQUIRES: Owner != null && passw != null && data != null
	 * MODIFIES: this
	 * THROWS: se Owner, passw o data sono null lancia NullPointerException 
	 * 				(eccezione disponibile in Java, unchecked)
	 *		   se Owner o data non sono presenti nella collezione o passw non è relativa all'Utente Owner 
	 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)	 
	 *  	   se la criptazione e la decriptazione non va a buon fine 
	 * 				lancia Exception (checked)
	 * EFFECTS: fa una copia del valore data nella collezione di dati dell'utente "Owner". 
	 * 			"Owner" sarà il nuovo proprietario della copia creata.
	 * */
	
	// Condivide il dato nella collezione con un altro utente
	// se vengono rispettati i controlli di identità
	public void share(String Owner, String passw, String Other, E data) throws Exception;
	/* REQUIRES: Owner != null && passw != null && Other != null && data != null
	 * MODIFIES: this
	 * THROWS: se Owner, passw, Other o data sono null lancia NullPointerException 
	 * 				(eccezione disponibile in Java, unchecked)
	 *		   se Owner, Other o data non sono presenti nella collezione o passw non è relativa all'Utente Owner 
	 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)	 
	 * 		   se Owner non è il creatore di data e prova a condividerlo 
	 * 				lancia UnsupportedOperationException (eccezione disponibile in Java, unchecked) 
	 *  	   se la criptazione e la decriptazione non va a buon fine 
	 * 				lancia Exception (checked)
	 * EFFECTS: crea una copia di "data", appartenente ad "Owner", e la inserisce nella collezione dell'Utente "Other". 
	 * */
	
	// restituisce un iteratore (senza remove) che genera tutti i dati
	// dell’utente in ordine arbitrario
	// se vengono rispettati i controlli di identità
	public Iterator<E> getIterator(String Owner, String passw) throws Exception;
	/* REQUIRES: Owner != null && passw != null
	 * THROWS: se Owner, passw sono null 
	 * 				lancia NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   se provo ad accedere ad un elemento oltre l'ultimo disponibile 
	 * 				lancia NoSuchElementException (eccezione disponibile in Java, unchecked)
	 *		   se Owner non è presente nella collezione o passw non è relativa all'Utente Owner 
	 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * 		   se si tenta di usare la remove dell'iteratoer 
	 * 				lancia UnsupportedOperationException (eccezione disponibile in Java, unchecked)
	 * 		   se la criptazione e la decriptazione non va a buon fine 
	 * 				lancia Exception (checked)
	 * EFFECTS: restituisce un iteratore, senza remove, che genera tutti i dati di "Owner"
	 * */	
	
	
	public boolean userExists(String id);
	/* REQUIRES: id != null && id non è già presente nel container
	 * THROWS: IllegalArgumentException (eccezione presente in Java, unchecked)
	 * EFFECTS: restituisce, se trovato, l'utente relativo all'ID "id"
	 * */
		
}
