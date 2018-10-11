package com.foo.mywebapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAO {

	private static final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

	private static Connection connection = null;
	
	private static PreparedStatement insert, rowid;
	private static PreparedStatement delete;
	private static PreparedStatement update;

	public static List<Contact> init() throws ClassNotFoundException {
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		List<Contact> cs = new ArrayList<>();
		try {
			// create a database connection
			
			//connection = DriverManager.getConnection("jdbc:sqlite::memory");
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			
			Statement statement = connection.createStatement();
			
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			ResultSet rs1 = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='contact'");
			if(!rs1.next()) {

				statement.executeUpdate("drop table if exists contact");
				statement.executeUpdate("create table contact ( id INTEGER PRIMARY KEY, fio string, "
						+ "birthday string, phone string, address string )");

				statement.executeUpdate("insert into contact values("
						+ "null, 'Жиляев Артем Владимирович', '1976-02-20', '8965-767-44-33', "
						+ "'г. Белгород ул. Ленина д.33 кв.125')");
				statement.executeUpdate("insert into contact values("
						+ "null, 'Прохор Артем Владимирович', '1976-01-13', '8951-367-42-11', "
						+ "'г. Старый Оскол ул. Пролетарская д.15 кв.16')");
			}

			ResultSet rs = statement.executeQuery("select * from contact");
			while (rs.next()) {
				Date d;

				d = ft.parse(rs.getString("birthday"));

				Contact c = new Contact(rs.getInt("id"), rs.getString("fio"), d, rs.getString("phone"),
						rs.getString("address"));
				cs.add(c);

			}
			
			insert = connection.prepareStatement(
					"INSERT INTO contact VALUES (null,?,?,?,?);");
			rowid = connection.prepareStatement(
					"SELECT last_insert_rowid();");
			delete = connection.prepareStatement(
					"DELETE FROM contact WHERE id = ?;");
			update = connection.prepareStatement(
					"UPDATE contact SET fio = ?, birthday = ?, phone = ?, address = ?"
					+ " WHERE id = ?;");
		} catch (ParseException e) {
			System.out.println("Unparseable using " + ft);
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		/*
		 * finally { try { if(connection != null) connection.close(); }
		 * catch(SQLException e) { // connection close failed. System.err.println(e); }
		 * }
		 */
		return cs;
	}

	public static synchronized int insert(Contact contact) {
		int r = 0;
		try {			
			insert.setString(1, contact.getFio());
			insert.setString(2, ft.format(contact.getBirthday()));
			insert.setString(3, contact.getPhone());
			insert.setString(4, contact.getAddress());
			insert.executeUpdate();
			
			ResultSet rs = rowid.executeQuery();
			rs.next();
			r = rs.getInt(1);
		} catch (SQLException e) { 
			System.err.println(e.getMessage());
		}	
		return r;
	}
	
	public static synchronized void update(Contact contact) {		
		try {
			update.setString(1, contact.getFio());			
			update.setString(2, ft.format(contact.getBirthday()));
			update.setString(3, contact.getPhone());
			update.setString(4, contact.getAddress());
			update.setInt(5, contact.getId());
			update.executeUpdate();
		} catch (SQLException e) {		
			System.err.println(e.getMessage());
		}		
	}
	
	public static synchronized void delete(int id) {		
		try {
			delete.setInt(1, id);
			delete.executeUpdate();
		} catch (SQLException e) {		
			System.err.println(e.getMessage());
		}
	}
}
