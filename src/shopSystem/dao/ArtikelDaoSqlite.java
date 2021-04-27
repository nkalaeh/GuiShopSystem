package shopSystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import shopSystem.core.Artikel;
import shopSystem.ex.NoArtikelFoundException;

public class ArtikelDaoSqlite {

   private final String CLASSNAME = "org.sqlite.JDBC";
   private final String CONNECTIONSTRING = "jdbc:sqlite:shopsystem.db";

   /**
    * Konstruktor.
    *
    * @throws ClassNotFoundException
    */
   public ArtikelDaoSqlite() throws ClassNotFoundException {
      Class.forName(CLASSNAME);
   }

   public Artikel read(int artikelNr) throws NoArtikelFoundException {
      Artikel artikel = null;
      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         // Verbindung zu Datenbank herstellen.
         connection = DriverManager.getConnection(CONNECTIONSTRING);
         // SQL-Abfrage erstellen.
         String sql = "SELECT * FROM artikel WHERE artikelNr = ?";
         preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setInt(1, artikelNr);
         // SQL-Abfrage ausf�hren.
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            artikel = create(resultSet);
         } else {
            throw new NoArtikelFoundException("Artikel-Nummer " + artikelNr + " existiert nicht");
         }
      } catch (SQLException e) {
         e.printStackTrace();
         throw new NoArtikelFoundException("Tabelle Artikel " + artikelNr + " kann nicht gelesen werden:\n" + e.getMessage());
      } finally {
         try {
            preparedStatement.close();
            connection.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return artikel;
   }

   public ArrayList<Artikel> read() throws NoArtikelFoundException {
      ArrayList<Artikel> liste = new ArrayList<Artikel>();

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         // Verbindung zu Datenbank herstellen.
         connection = DriverManager.getConnection(CONNECTIONSTRING);
         // SQL-Abfrage erstellen.
         String sql = "SELECT * FROM artikel ORDER BY artikelNr";
         preparedStatement = connection.prepareStatement(sql);
         // SQL-Abfrage ausf�hren.
         ResultSet resultSet = preparedStatement.executeQuery();
         while (resultSet.next()) {
            Artikel artikel = new Artikel();
            artikel.setArticleNr(resultSet.getInt("artikelNr"));
            artikel.setBezeichnung(resultSet.getString("Bezeichnung"));
            liste.add(artikel);
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

   public Artikel insert(Artikel artikel) throws NoArtikelFoundException {
      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         // Verbindung zu Datenbank herstellen.
         connection = DriverManager.getConnection(CONNECTIONSTRING);
         // Abfrage ausf�hren.
         String sql;
         sql = "INSERT INTO artikel (bezeichnung, gewicht, ek, warengruppe) values (?, ?, ?, ?)";
         preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setString(1, artikel.getDescription());
         preparedStatement.setDouble(2, artikel.getWeight());
         preparedStatement.setDouble(3, artikel.getPrice());
         preparedStatement.setInt(4, artikel.getWarengruppe());
         preparedStatement.executeUpdate();
         // Nummer des neu hinzugef�gten Men�s ermitteln.
         ResultSet resultSet = preparedStatement.getGeneratedKeys();
         resultSet.next();
         int idNeu = resultSet.getInt("last_insert_rowid()");
         artikel.setArticleNr(idNeu);
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            preparedStatement.close();
            connection.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return artikel;
   }

   public void delete(int artikelnummer) throws NoArtikelFoundException {
      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         // Verbindung zu Datenbank herstellen.
         connection = DriverManager.getConnection(CONNECTIONSTRING);
         // Abfrage ausf�hren.
         String sql;
         sql = "DELETE FROM artikel WHERE artikelNr = ?";
         preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setInt(1, artikelnummer);
         preparedStatement.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            preparedStatement.close();
            connection.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   public void update(Artikel artikel) throws NoArtikelFoundException {
      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         // Verbindung zu Datenbank herstellen.
         connection = DriverManager.getConnection(CONNECTIONSTRING);
         // Abfrage ausf�hren.
         String sql;
         sql = "UPDATE artikel SET bezeichnung = ?, gewicht = ?, warengruppe = ?, ek = ? WHERE artikelNr = ?";
         preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setString(1, artikel.getDescription());
         preparedStatement.setDouble(2, artikel.getWeight());
         preparedStatement.setDouble(4, artikel.getPrice());
         preparedStatement.setInt(3, artikel.getWarengruppe());
         preparedStatement.setInt(5, artikel.getArticlelNr());
         preparedStatement.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            preparedStatement.close();
            connection.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   private Artikel create(ResultSet resultSet) {
      Artikel artikel = new Artikel();
      try {
         artikel.setArticleNr(resultSet.getInt("artikelNr"));
         artikel.setBezeichnung(resultSet.getString("bezeichnung"));
         artikel.setGewicht(resultSet.getDouble("gewicht"));
         artikel.setWarengruppe(resultSet.getInt("warengruppe"));
         artikel.setEk(resultSet.getDouble("ek"));
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return artikel;
   }

}
