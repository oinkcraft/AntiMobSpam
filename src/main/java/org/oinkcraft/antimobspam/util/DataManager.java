package org.oinkcraft.antimobspam.util;

import org.oinkcraft.antimobspam.AntiMobSpam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DataManager {

	AntiMobSpam plugin;
	String dataFolder;
	String url;
	
	int range = 5;
	
	public DataManager(AntiMobSpam plugin) {
		this.plugin = plugin;
		String sql = "CREATE TABLE IF NOT EXISTS monstereggspawns (x INTEGER, y INTEGER, z INTEGER, player text NOT NULL, world text NOT NULL, type text NOT NULL);";
		dataFolder = plugin.getDataFolder().getAbsolutePath();
		url = "jdbc:sqlite:"+dataFolder+"/monstereggs.db";
		try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // Create a new table //
            stmt.execute(sql);
        } catch (SQLException e) {
            Logger.logToFile("Error: Could not create table. Debugger information: "+e.getMessage());
        }
	}
	
	private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
	
	public void addMobSpawn(int x, int y, int z, String player, String type, String world) {
		String sql = "INSERT INTO monstereggspawns(x,y,z,player,type,world) VALUES(?,?,?,?,?,?)";
		 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, x);
            pstmt.setInt(2, y);
            pstmt.setInt(3, z);
            pstmt.setString(4, player);
            pstmt.setString(5, type);
            pstmt.setString(6, world);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Logger.logToFile("Error: Could not insert data, Debugger information: "+e.getMessage());
        }
	}
	
	public ArrayList<Vector3> getMobSpawns(int x, int y, int z, String world) {
		String sql = "SELECT x, y, z, player, type FROM monstereggspawns WHERE x BETWEEN "+Integer.toString(x-range)+" AND "+Integer.toString(x+range)+" AND y BETWEEN "+Integer.toString(y-range)+" AND "+Integer.toString(y+range)+" AND z BETWEEN "+Integer.toString(z-range)+" AND "+Integer.toString(z+range);
        
		ArrayList<Vector3> mobSpawns = new ArrayList<Vector3>();
		
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
            	mobSpawns.add(new Vector3(new Vector3(rs.getInt("x"),rs.getInt("y"),rs.getInt("z")),rs.getString("player"),rs.getString("type")));
            }
        } catch (SQLException e) {
            Logger.logToFile("Error: Could not select data, Debugger information: "+e.getMessage());
        }
        
        return mobSpawns;
	}
	
}
