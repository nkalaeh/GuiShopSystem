package shopSystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import shopSystem.core.Warengruppe;
import shopSystem.ex.NoArtikelFoundException;

public class WarengruppeDaoSqlite {

   private final String CLASSNAME = "org.sqlite.JDBC";
   private final String CONNECTIONSTRING = "jdbc:sqlite:shopsystem.db";

   /**
    * Konstruktor.
    *
    * @throws ClassNotFoundException
    */
   public WarengruppeDaoSqlite() throws ClassNotFoundException {
      Class.forName(CLASSNAME);
   }

   public ArrayList<Warengruppe> read() throws NoArtikelFoundException {
      ArrayList<Warengruppe> liste = new ArrayList<Warengruppe>();

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         // Verbindung zu Datenbank herstellen.
         connection = DriverManager.getConnection(CONNECTIONSTRING);
         // SQL-Abfrage erstellen.
         String sql = "SELECT * FROM warengruppe ORDER BY id";
         preparedStatement = connection.prepareStatement(sql);
         // SQL-Abfrage ausf�hren.
         ResultSet resultSet = preparedStatement.executeQuery();
         while (resultSet.next()) {
            Warengruppe warengruppe = new Warengruppe();
            warengruppe.setId(resultSet.getInt("id"));
            warengruppe.setBezeichnung(resultSet.getString("Bezeichnung"));
            liste.add(warengruppe);
         }
      } catch (SQLException e) {
         e.printStackTrace();
         throw new NoArtikelFoundException("Tabelle Warengruppe kann nicht gelesen werden:\n" + e.getMessage());
      } finally {
         try {
            preparedStatement.close();
            connection.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return liste;
   }

}
