package shopSystem.core;

public class Artikel {
   private int artikelNr;
   private String bezeichnung;
   private double gewicht;
   private double ek;
   private int warengruppe;

   public int getArticlelNr() {
      return artikelNr;
   }

   public void setArticleNr(int artikelNr) {
      this.artikelNr = artikelNr;
   }

   public String getDescription() {
      return bezeichnung;
   }

   public void setBezeichnung(String bezeichnung) {
      this.bezeichnung = bezeichnung;
   }

   public double getWeight() {
      return gewicht;
   }

   public void setGewicht(double gewicht) {
      this.gewicht = gewicht;
   }

   public double getPrice() {
      return ek;
   }

   public void setEk(double ek) {
      this.ek = ek;
   }

   public int getWarengruppe() {
      return warengruppe;
   }

   public void setWarengruppe(int warengruppe) {
      this.warengruppe = warengruppe;
   }


}
