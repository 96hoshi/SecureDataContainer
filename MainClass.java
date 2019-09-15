/**
*
* @author Marta Lo Cascio 532686
*/

public class MainClass {

	public static void main(String[] args) throws Exception{
		
//Inizio Test	
		
		SDCTest<Integer> testInt = new SDCTest<>();	
		
		int iData = 42;
		int iAbsent_data = 0;
		int iShared_data = 24;
		
		System.out.println("Tipo di dato: int");
		testInt.testAll(iData, iAbsent_data, iShared_data);
		
		
		SDCTest<String> testString = new SDCTest<>();

		String data = "ciao";
		String absent_data = "42";
		String shared_data = "mondo";
		
		System.out.println("Tipo di dato: String");
		testString.testAll(data, absent_data, shared_data);

//Fine Test
	}
	
}