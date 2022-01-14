package org.restapi.crud.crud.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.restapi.crud.crud.model.*;

public class CrudService {
	// Déclaration de l'objet "Connection JDBC"
	Connection con;
		
	// On crée une méthode centralisée pour écrire le mot de passe facilement en dur
	public static Connection getMySQLConnection() throws SQLException,
	ClassNotFoundException {
    
		// Déclaration des variables pour la connexion SQL
		
		String hostName = "localhost";
		String dbName = "entries";
		String userName = "mk";
		String password = "Minhkha1905$*";
		
		return getMySQLConnection(hostName, dbName, userName, password);
	}
	
	// 2021-11-30
	// Params : hostName, dbName, userName, password
	// Description: créer la connection JDBC avec les params entrés
	public static Connection getMySQLConnection(
			String hostName, 
			String dbName,
			String userName, 
			String password) throws SQLException, ClassNotFoundException {
		// Declare the class Driver for MySQL DB
		// This is necessary with Java 5 (or older)
		// Java6 (or newer) automatically find the appropriate driver.
		// If you use Java> 5, then this line is not needed.
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Déclaration d'une variable intitulée "connectionURL"
        // On affecte à cette variable une certaine valeur
        // Il s'agit en réalité d'une simple concaténation
        // On concatène les variables recues en entrée de la méthod
		String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

		
		Connection conn = DriverManager.getConnection(connectionURL, userName, password);
		
		return conn;
	}
	
	// 2021-11-30
	// Params : rien
	// Description : Constructeur de la classe
	public CrudService() {
		try {
			// Get a Connection object
			this.con = getMySQLConnection();			
		}
		catch (Exception e) {
			System.out.println("ERROR SERVICE ! ["+e.toString()+"]");
		}
	}
	
	// 2021-11-30
	// Params : String word
	// Description : Cherche dans la base de donnée le mot à rechercher entré en param.
	public CrudModel getEntries(String word) {
		System.out.println("HERE ["+word+"]");
		
		CrudModel result = new CrudModel();
		result.setWord(word);
		
		String select = "select wordtype, definition from entries where word = ?;";
		
		try {
			PreparedStatement ps = con.prepareStatement(select);
			ps.setString(1, word);
			
			ResultSet res = ps.executeQuery();
			System.out.println("request executed");
			if(res.next()){
				result.setType(res.getString(1));
				result.setDefinition(res.getString(2));
				System.out.println(res.getString("wordtype")+" "+res.getString("definition"));
			} else {
				System.out.println("does not exist");
				result.setDefinition("does not exist");
				result.setType("none");
			}
			
		}
		catch(Exception e) {
			System.out.println("ERROR SELECT ENTRIES ["+e.toString()+"]");
		}

		return result;
	}

	// 14/01/2022
	// Params : CrudModel word
	// Description : Ajoute dans la base de donnée le mot entré en paramètre et retourne un String
	public String addEntries(CrudModel word) {
		
		System.out.println("HERE INSERTING ["+word.getWord()+"]");
		
		String result = "failed";
		
		String insert = "insert into entries (word, wordtype, definition) values (?, ?, ?);";
		try {
			PreparedStatement ps = con.prepareStatement(insert);
			ps.setString(1, word.getWord());
			ps.setString(2, word.getType());
			ps.setString(3, word.getDefinition());
			
			if (ps.executeUpdate() > 0) {
				System.out.println("request executed");
				result = "Ok : "+ word.getWord() + " added";
			}
		}catch (Exception e) {
			System.out.println("ERROR INSERT INTO ENTRIES ["+e.toString()+"]");
		}
		return result;
	}

	// 14/01/2022
	// Params : CrudModel word
	// Description : Supprime le mot entré en paramètre dans la base de donnée et retourne un String
	public String deleteEntries(CrudModel word) {
		
		System.out.println("HERE DELETING ["+word.getWord()+"]");
		
		String result = "Failed";
		
		String delete = "DELETE FROM entries WHERE word = ? and wordtype = ? and definition = ?;";
		try {
			PreparedStatement ps = con.prepareStatement(delete);
			ps.setString(1, word.getWord());
			ps.setString(2, word.getType());
			ps.setString(3, word.getDefinition());
			
			if (ps.executeUpdate() > 0) {
				System.out.println("request executed");
				result = "Ok : " + ps.getUpdateCount() + " entry(ies) deleted";
			}
		} catch (Exception e) {
			System.out.println("ERROR DELETING ENTRIES ["+e.toString()+"]");
		}
		return result;
	}

	// 14/01/2022
	// Params : CrudModel word
	// Description : Modifie le mot entré en paramètre dans la base de donnée et retourne un message de confirmation depuis la base en String 
	public String updateEntries(CrudModel word) {
		
		System.out.println("HERE UPDATING ["+word.getWord()+"]");
		
		String response = "";
		
		CrudModel result = new CrudModel();
		result.setWord(word.getWord());
		result.setType(word.getType());
		result.setDefinition(word.getDefinition());
		
		String update = "UPDATE entries SET definition = ? WHERE word = ?;";
		try {
			PreparedStatement ps = con.prepareStatement(update);
			ps.setString(1, result.getDefinition());
			ps.setString(2, result.getWord());
			
			ps.executeUpdate();
			System.out.println(ps.getUpdateCount());
			if(ps.getUpdateCount() > 0) {
				System.out.println("request executed");
				response = "Ok : " + ps.getUpdateCount()+" entry(ies) affected";
			} else {
				System.out.println("nothing has been updated");
				response = "Failed : no entry has been updated";
			}
			
		} catch (Exception e) {
			System.out.println("ERROR UPDATING ENTRIES ["+e.toString()+"]");
		}
		return response;
	}
	
}
