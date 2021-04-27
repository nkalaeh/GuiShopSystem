package shopSystem.ex;

public class NoArtikelFoundException extends Exception {

   /**
    * Konstruktor.
    *
    * @param message Die Fehlermeldung.
    */
   public NoArtikelFoundException(String message) {
      super(message);
   }

}
