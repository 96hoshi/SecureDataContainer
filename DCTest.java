import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
/**
*
* @author Marta Lo Cascio 532686
*/
public class SDCTest<E> {
	
	@Test
	void testCreateUser(SecureDataContainer<E> container) {		
			
		//id nullo
		try{
	        container.createUser(null, "passw");
	        fail("expected NullPointerException");

	    } catch(NullPointerException e){
	        //this exception is expected.
	    }
		
		//password nulla
		try{
	        container.createUser("id", null);
	        fail("expected NullPointerException");

	    } catch(NullPointerException e){
	        //this exception is expected.
	    }
		
		//aggiunge un utente correttamente
	     container.createUser("id", "passw");
	     assertTrue(container.userExists("id"));
	     
	     //cerca di aggiungere un utente con lo stesso Id	     
	     try{
		     container.createUser("id", "passw2");
		        fail("expected IllegalArgumentException");

		 } catch(IllegalArgumentException e){
		    	//this exception is expected.
		 }
	    System.out.println("1:testCreateUser tutto ok!");
	}

	@Test
	void testRemoveUser(SecureDataContainer<E> container) {
				
		//uno o più elemeti sono nulli
		try{
	        container.RemoveUser(null, "passw");
	        fail("expected NullPointerException");

	    } catch(NullPointerException e){
	        //this exception is expected.
	    }

		//rimuove un utente che non c'è
		try {
			container.RemoveUser("toRemove", "passw");
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
		}
		
		//rimuove un utente che c'è, ma la password è sbagliata
		container.createUser("toRemove", "passw");
		try {
			container.RemoveUser("toRemove", "passw_errata");
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
		}

		//rimuove correttamente un utente
	    container.RemoveUser("toRemove", "passw");
	    assertFalse(container.userExists("toRemove"));
		
	    System.out.println("2:testRemoveUser tutto ok!");
	}

	@Test
	void testGetSize(SecureDataContainer<E> container, E data) throws Exception{
		
		//uno o più elemeti sono nulli
		try {
			container.getSize(null, "passw");
	        fail("expected NullPointerException");
		} catch(NullPointerException e) {
	        //this exception is expected.
		}
		
		//cerco i dati di un utente che non c'è nel container
		try {
			container.getSize("id_non_presente", "passw");
		    fail("expected IllegalArgument");
		} catch(IllegalArgumentException e) {
		        //this exception is expected.
		}
		
		//conto quanti dati ha un utente appensa registrato, expected 0
		container.createUser("id", "passw");

		int dim = container.getSize("id", "passw");
		assertEquals(0, dim);
		
		//inserisco un elemento e conto quanti dati ha, expected 1 
		try{
			container.put("id", "passw", data);
		} catch(NullPointerException e){
			System.out.println(e.getMessage() + " 3: input errato");
			return;
		}

		dim = container.getSize("id", "passw");
		assertEquals(1, dim);
		
	    System.out.println("3:testGetSize tutto ok!");
	}

	@Test
	void testPut(SecureDataContainer<E> container, E data) throws Exception{
		
		//uno o più elemeti sono nulli
		try {
			container.put(null, null, data);
	        fail("expected NullPointerException");
		} catch(NullPointerException e) {
	        //this exception is expected.
		}	

		//inserimento nella collezione di un utente che non è nel container
		try {
			container.put("id_assente", "passw", data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}	
		
		container.createUser("id", "passw");
		
		//inserimento di un dato illegale
		try {
			container.put("id", "passw", null);
	        fail("expected NullPointerException");
		} catch(NullPointerException e) {
	        //this exception is expected.
		}	
		//inserimento corretto
		container.put("id", "passw", data);
		assertEquals(data, container.get("id", "passw", data));
		
	    System.out.println("4:testPut tutto ok!");
		
	}

	@Test
	void testGet(SecureDataContainer<E> container, E data, E absent_data, E shared_data) throws Exception {
		
		//uno o più elemeti sono nulli
		try {
			container.get(null, null, data);
	        fail("expected NullPointerException");
		} catch(NullPointerException e) {
	        //this exception is expected.
		}
		
		container.createUser("id", "passw");
		container.put("id", "passw", data);
		container.put("id", "passw", shared_data);
		
		//tentativo di prendere un dato non presente nella collezione
		try {
			container.get("id", "passw", absent_data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}
		
		//tentativo di prendere un dato presente nella collezione ma con password errata
		try {
			container.get("id", "passw_errata", data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}
		
		//prende di un dato condiviso ma precedentemente rimosso dal creatore
		container.createUser("other_id", "other_passw");
		container.share("id", "passw", "other_id", shared_data);
		container.remove("id", "passw", shared_data);

		try {
			container.get("other_id", "passw", shared_data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}
		
		//esempio corretto
		E actual = container.get("id", "passw", data);
        assertEquals(data, actual);
		
	    System.out.println("5:testGet tutto ok!");
			
	}

	@Test
	void testRemove(SecureDataContainer<E> container, E data, E absent_data, E shared_data) throws Exception{
				
		//uno o più elemeti sono nulli
		try {
			container.remove("id", null, null);
	        fail("expected NullPointerException");
		} catch(NullPointerException e) {
	        //this exception is expected.
		}	
		
		container.createUser("id", "passw");
		container.put("id", "passw", data);
		container.put("id", "passw", shared_data);
		assertEquals(2, container.getSize("id", "passw"));


		//rimuove un dato nella collezione di un utente che non è nel container
		try {
			container.remove("id_assente", "passw", data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}
		
		container.createUser("other_id", "other_passw");
		container.share("id", "passw", "other_id", shared_data);
		assertEquals(1, container.getSize("other_id", "other_passw"));
		//adesso other_id ha solo shared_data, dato condiviso da id
		
		//rimuove un dato condiviso, che non appartiene ad id
		//mi aspetto che venga rimosso solo il dato di other_id, dati di other_id==0
		container.remove("other_id", "other_passw", shared_data);
		assertEquals(0, container.getSize("other_id", "other_passw"));
	
		
		container.share("id", "passw", "other_id", shared_data);	
		assertEquals(1, container.getSize("other_id", "other_passw"));

		//id rimuove un dato condiviso, di cui è l'owner
		//mi aspetto che il dato venga eliminato ANCHE agli altri utenti a cui l'ho condiviso
		container.remove("id", "passw", shared_data);
		assertEquals(1, container.getSize("id", "passw"));
		assertEquals(0, container.getSize("other_id", "other_passw"));
		
		//rimuove un elemento non presente nella collezione/illegale
		try {
			container.remove("id", "passw", absent_data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}	
				
		//caso normale
		container.remove("id", "passw", data);
		assertEquals(0, container.getSize("id", "passw"));

		System.out.println("6:testRemove tutto ok!");

	}

	@Test
	void testCopy(SecureDataContainer<E> container, E data, E absent_data) throws Exception {
		
		//uno o più elemeti sono nulli
		try {
			container.copy(null, "passw", data);
	        fail("expected NullPointerException");
		} catch(NullPointerException e) {
	        //this exception is expected.
		}	

		//copia di un dato nella collezione di un utente che non è nel container
		try {
			container.copy("id_assente", "passw", data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}	
		
		container.createUser("id", "passw");
		container.put("id", "passw", data);
		
		//copia di un elemento non presente nella collezione/illegale
		try {
			container.copy("id", "passw", absent_data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}	
				
		//esempio corretto
		container.copy("id", "passw", data);
		assertEquals(2, container.getSize("id", "passw"));
		//mi aspetto che se elimino un elemento copiato,
		//non elimino entrambi gli elementi
		container.remove("id", "passw", data);
		assertEquals(1, container.getSize("id", "passw"));

	
	    System.out.println("7:testCopy tutto ok!");

	}

	@Test
	void testShare(SecureDataContainer<E> container, E data, E absent_data) throws Exception {
		
		//uno o più elemeti sono nulli
		try {
			container.share("id", null, null, data);
	        fail("expected NullPointerException");
		} catch(NullPointerException e) {
	        //this exception is expected.
		}	

		container.createUser("id", "passw");
		container.put("id", "passw", data);

		container.createUser("other_id", "other_passw");
		
		//condivido un dato di un utente che non è nel container
		try {
			container.share("id_assente", "passw", "other_id", data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}	
		
		//condivide un dato che non è presente nella collezione di "id"
		try {
			container.share("id", "passw","other_id", absent_data);
	        fail("expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
	        //this exception is expected.
		}
		
		//esempio corretto
		container.share("id", "passw","other_id", data);
		//expected: il riferimento è uguale per id e other_id
		E original = container.get("id", "passw", data);
		E shared = container.get("other_id", "other_passw", data);
		assertEquals(original, shared);
		
	    System.out.println("8:testShare tutto ok!");
	}

	@Test
	void testGetIterator(SecureDataContainer<E> container, E data, E data_next) throws Exception{
		
		container.createUser("id", "passw");
		
		//rimozione illegale da specifica
		try {
            container.getIterator("id", "passw").remove();
	        fail("expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e){
	        //this exception is expected.
        }
        //accesso a un dato successivo in un container vuoto
        try {
            container.getIterator("id", "passw").next();
	        fail("expected NoSuchElementException");
        } catch(NoSuchElementException e) {
	        //this exception is expected.
        }
        
        //esempio corretto
		container.put("id", "passw",data);
		container.put("id", "passw",data_next);
		
		Iterator<E> testIter = container.getIterator("id", "passw");
		while(testIter.hasNext()) {
			testIter.next();
    	
		}
	    System.out.println("9:testGetIterator tutto ok!");
	}
	
	@Test
	void testUserExsist(SecureDataContainer<E> container) {
		container.createUser("id", "passw");
		//l'utente è presente
		assertTrue(container.userExists("id"));
		//l'utente non è presente
		assertFalse(container.userExists("absent_id"));
		//utente null
		assertFalse(container.userExists(null));

	    System.out.println("10:testUserExists tutto ok!");		
		
	}
	
    public void testAll(E data, E absent_data, E shared_data) throws Exception{
		if(data == null || absent_data == null || shared_data == null)
			throw new NullPointerException();
    	
    	SDCTest<E> test = new SDCTest<>();
    	
		System.out.println("---------Prima implementazione---------");
		test.testCreateUser(new MySecureDataContainer<E>());
		test.testRemoveUser(new MySecureDataContainer<E>());
		test.testGetSize(new MySecureDataContainer<E>(),data);
		test.testPut(new MySecureDataContainer<E>(),data);
		test.testGet(new MySecureDataContainer<E>(),data,absent_data,shared_data);
		test.testRemove(new MySecureDataContainer<E>(),data,absent_data,shared_data);
		test.testCopy(new MySecureDataContainer<E>(),data,absent_data);
		test.testShare(new MySecureDataContainer<E>(),data, absent_data);
		test.testGetIterator(new MySecureDataContainer<E>(),data, shared_data);
		test.testUserExsist(new MySecureDataContainer<E>());
		System.out.println();

		System.out.println("---------Seconda implementazione---------");
		test.testCreateUser(new MySecureDataContainer2nd<E>());
		test.testRemoveUser(new MySecureDataContainer2nd<E>());
		test.testGetSize(new MySecureDataContainer2nd<E>(),data);
		test.testPut(new MySecureDataContainer2nd<E>(),data);
		test.testGet(new MySecureDataContainer2nd<E>(),data,absent_data,shared_data);
		test.testRemove(new MySecureDataContainer2nd<E>(),data,absent_data,shared_data);
		test.testCopy(new MySecureDataContainer2nd<E>(),data,absent_data);
		test.testShare(new MySecureDataContainer2nd<E>(),data, absent_data);
		test.testGetIterator(new MySecureDataContainer2nd<E>(),data, shared_data);
		test.testUserExsist(new MySecureDataContainer2nd<E>());
		System.out.println();

    }

}
