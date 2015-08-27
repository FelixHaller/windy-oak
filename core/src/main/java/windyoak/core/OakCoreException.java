package windyoak.core;

/**
 *
 * @author Felix Haller
 */
public class OakCoreException extends Exception{
    
    /**
	 * Kontruktor mit übergebener Fehlermeldung als String.
	 * 
	 * @param message aussagekräftige Fehlermeldung, welche dann ausgeliefert wird
	 */
    
	public OakCoreException(String message)
	{
		super(message);
	}

}