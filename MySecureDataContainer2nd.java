import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
*
* @author Marta Lo Cascio 532686
*/
public class MySecureDataContainer2nd<E> implements SecureDataContainer<E> {
    /** Overview: Tipo modificabile di Data Storage al cui interno gestisce un'insieme di utenti,
	 * 			  ognuno con la propria collezione di dati, condivisi e non. 
	 * 			  Implementazione con TreeSet
	 *  Typical Element: <{user_0, user_1, ..., user_n-1}> 
	 *  				 dove n è il numero di utenti nel container
	 *  
	 *  Rep Invariant: forall i. 0 <= i < users.size => (user_i.getId != null && user_i.getPassw != null && user_i.getData != null) 
	 *  				&& forall i,j. 0 <= i < j < n-1 => user_i.getId != user_j.getId  				
	 *  				
	 *  Abstract Function: f(x) = {user_0, ..., user_users.size}
	 *  						tale che 
	 *  						user_i = {id, password, {dataCollection}}
	 *  						user_i.getId != null
	 *  						user_i.getPassw != null
	 *  						user_i.dataCollection.getData != null
	 *  						per ogni 0 <= i < users.size
	 */
	private static final String ALGORITHM = "AES";
    private byte[] key;
	
	private Collection <User2nd> users;
	
	
	public MySecureDataContainer2nd () {
		this.users = new TreeSet<User2nd>();	
		key = new byte[16];
		new Random().nextBytes(key);
	}
	
	// Crea l’identità un nuovo utente della collezione
	@Override
	public void createUser(String id, String passw) throws NullPointerException, IllegalArgumentException  {
		/* REQUIRES: Id != null && passw != null && Id non appartiene già alla collezione
		 * MODIFIES: this
		 * THROWS: se Id o passw sono null lancia NullPointerException 
		 * 				(eccezione disponibile in Java, unchecked) 
		 * 		   se Id è già presente in lista lancia IllegalArgumentException
		 * 				(eccezione disponibile in Java, unchecked)
		 * EFFECTS: aggiunge un Utente con il proprio "Id" univoco e la password "passw" alla collezione
		 * */
		
		if (id == null || passw == null)	throw new NullPointerException();
		
		//controllo se id è già utilizzato
		for (User2nd user : users) {
			if (user.getId().equals(id))	
				throw new IllegalArgumentException("ERRORE: Id: " + id + " è già esistente");
		}
		users.add(new User2nd(id,passw));	
	}

	// Rimuove l’utente dalla collezione
	@Override
	public void RemoveUser(String id, String passw) throws NullPointerException, IllegalArgumentException {
		/* REQUIRES: Id != null && passw != null
		 * MODIFIES: this
		 * THROWS: se Id o passw sono null lancia NullPointerException 
		 * 				(eccezione disponibile in Java, unchecked) 
		 * 		   se Id non è presente nella collezione o passw non è relativa all'Utente Id 
		 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)
		 * EFFECTS: elimina un Utente "Id" dalla collezione se esiste
		 * */
		
		if (id == null || passw == null)	throw new NullPointerException();
		
		//autenticazione
		User2nd user = searchUser(id);
		
		if(user.checkPassw(passw))
			users.remove(user);	
		
		else throw new IllegalArgumentException("ERRORE: Password errata per l'utente: " + id);
	}

	
	// Restituisce il numero degli elementi di un utente presenti nella
	// collezione
	@Override
	public int getSize(String owner, String passw) throws NullPointerException, IllegalArgumentException{
		/* REQUIRES: Owner != null && passw != null
		 * THROWS: se Owner o passw sono null lancia NullPointerException 
		 * 				(eccezione disponibile in Java, unchecked) 
		 * 		   se Owner non è presente nella collezione o passw non è relativa all'Utente Owner 
		 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)
		 * EFFECTS: restituisce il numero intero di dati posseduti da "Owner"
		 * */
		
		if (owner == null || passw == null)	throw new NullPointerException();
		
		User2nd user = searchUser(owner);
		
		if(user.checkPassw(passw))
			return user.getDataCollectionSize();
		
		else throw new IllegalArgumentException("ERRORE: Password errata per l'utente: " + owner);
	}
	
	// Inserisce il valore del dato nella collezione
	// se vengono rispettati i controlli di identità
	@Override
	public boolean put(String owner, String passw, E data) throws Exception{
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
		
		if (owner == null || passw == null || data == null)	throw new NullPointerException();
		
		User2nd user = searchUser(owner);
		
		if(user.checkPassw(passw))
			return user.addData(encrypt(data));
		
		else throw new IllegalArgumentException("ERRORE: Password errata per l'utente: " + owner);
	}

	// Ottiene una copia del valore del dato nella collezione
	// se vengono rispettati i controlli di identità
	@Override
	public E get(String owner, String passw, E data) throws Exception{
		/* REQUIRES: Owner != null && passw != null && data != null
		 * THROWS: se Owner, passw o data sono null lancia NullPointerException 
		 * 				(eccezione disponibile in Java, unchecked)
		 *		   se Owner o data non sono presenti nella collezione o passw non è relativa all'Utente Owner 
		 * 				lancia IllegalArgumentException (eccezione disponibile in Java, unchecked)	 
		 *  	   se la criptazione e la decriptazione non va a buon fine 
		 * 				lancia Exception (checked)
		 * EFFECTS:viene restituita una copia del valore "data" relativa all'Utente "Owner"
		 * */
		
		if (owner == null || passw == null || data == null)	throw new NullPointerException();
		
		User2nd user = searchUser(owner);
		
		if(user.checkPassw(passw))
			return decrypt(user.searchData(encrypt(data)).getData());
		
		else throw new IllegalArgumentException("ERRORE: Password errata per l'utente: " + owner);

	}
	// Rimuove il dato nella collezione
	// se vengono rispettati i controlli di identità
	@Override
	public E remove(String owner, String passw, E data) throws Exception {
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
		
		if (owner == null || passw == null || data == null)	throw new NullPointerException();
		
		User2nd user = searchUser(owner);
		
		if(user.checkPassw(passw)) {
			
			SData toRemove = user.searchData(encrypt(data));
			if(toRemove != null) {
				
				//se sono il creatore del dato
				if(toRemove.getOwner().equals(owner)) {	
					//cancello per ogni other a cui l'ho condiviso
					for(String otherUser : toRemove.getOtherCollection()) {
						
						User2nd userShared = searchUser(otherUser);
						userShared.removeData(encrypt(data));
					}
				} else	toRemove.removeOther(owner);
				
			return decrypt(user.removeData(encrypt(data)).getData());
			}
		} throw new IllegalArgumentException("ERRORE: Password errata per l'utente: " + owner);
	}

	// Crea una copia del dato nella collezione
	// se vengono rispettati i controlli di identità
	@Override
	public void copy(String owner, String passw, E data) throws Exception {
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
		
		if (owner == null || passw == null || data == null)	throw new NullPointerException();
		
		User2nd user = searchUser(owner);
		
		if(user.checkPassw(passw)) {
			
			user.searchData(encrypt(data));
			user.addData(encrypt(data));
		} else throw new IllegalArgumentException("ERRORE: Password errata per l'utente: " + owner);
	}
	
	// Condivide il dato nella collezione con un altro utente
	// se vengono rispettati i controlli di identità
	@Override
	public void share(String owner, String passw, String other, E data) throws Exception{	
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
		
		if (owner == null || passw == null || data == null || other == null)	throw new NullPointerException();	
		
		User2nd user = searchUser(owner);
			
		if (user.checkPassw(passw)) {
			
			SData sharedData = user.searchData(encrypt(data));
			if (sharedData.getOwner().equals(owner)) { 
				//se il possessore del dato sono io

				sharedData.addOther(other); 			
				User2nd userOther = searchUser(other);
				userOther.addShared(sharedData);
				
		} else	throw new UnsupportedOperationException("ERRORE:" + owner + " non può condividere il dato");
			
	} else throw new IllegalArgumentException("ERRORE: Password errata per l'utente: " + owner);
}

	// restituisce un iteratore (senza remove) che genera tutti i dati
	// dell’utente in ordine arbitrario
	// se vengono rispettati i controlli di identità
	@Override
	public Iterator<E> getIterator(String owner, String passw) throws Exception {
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
		
		if (owner == null || passw == null)	throw new NullPointerException();	
		
		User2nd user = searchUser(owner);
			
		if (user.checkPassw(passw))
			return new Iterator<E>() {
			 Iterator<byte[]> it = user.userDataIterator();

				@Override
				public boolean hasNext() {
					return it.hasNext();
				}

				@Override
				public E next() {
					if (!this.hasNext()) {
		        		throw new NoSuchElementException();
		        	}
					try {
						return decrypt(it.next());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			};
						
		throw new IllegalArgumentException("ERRORE: Password errata per l'utente: " + owner);
	}
	
	
	
	private User2nd searchUser(String id) throws IllegalArgumentException {
		/* REQUIRES: id != null && id non è già presente nel container
		 * THROWS: IllegalArgumentException (eccezione presente in Java, unchecked)
		 * EFFECTS: restituisce, se trovato, l'utente relativo all'ID "id"
		 * */
		for(User2nd u : users) {
			if(u.getId().equals(id))
				return u;
		}
		throw new IllegalArgumentException("ERRORE: Utente " + id + " non trovato");
	}

	public boolean userExists(String id) {
		/* REQUIRES: id != null && id non è già presente nel container
		 * THROWS: IllegalArgumentException (eccezione presente in Java, unchecked)
		 * EFFECTS: restituisce, se trovato, l'utente relativo all'ID "id"
		 * */
		
		try {
			searchUser(id);
			return true;
		} catch(IllegalArgumentException e){
			return false;
		}
	}
	

    private byte[] encrypt(E data) throws Exception {
    	/* REQUIRES: E != null
		 * THROWS: Exception (checked)
		 * EFFECTS: cripta il dato in un array di byte
		 * */
    	
    	ByteArrayOutputStream b = new ByteArrayOutputStream();
    	ObjectOutputStream o = new ObjectOutputStream(b);
    	o.writeObject(data);
    	
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(b.toByteArray());
    }

    @SuppressWarnings("unchecked")
	private E decrypt(byte[] cipherData) throws Exception {
    	/* REQUIRES: chiperData != null
		 * THROWS: Exception (checked)
		 * EFFECTS: decripta l'array di byte e restituisce il dato corrispondente
		 * */
    	
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        ByteArrayInputStream b = new ByteArrayInputStream(cipher.doFinal(cipherData)); //
    	ObjectInputStream o = new ObjectInputStream(b);
    	Object temp = o.readObject();
    	
    	return (E)temp;
    }
		
}