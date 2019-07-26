package org.oinkcraft.antimobspam.util;

import org.oinkcraft.antimobspam.AntiMobSpam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Logger {

	public static AntiMobSpam plugin;
	public static String logFile = "monstoregg.log";
	
	public static void logToFile(String message) {
	    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	    LocalDateTime now = LocalDateTime.now();
		File saveTo = new File(plugin.getDataFolder(), logFile);
		if (!saveTo.exists())
		{
			try {
				saveTo.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(saveTo, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println("["+dtf.format(now)+"] "+message);
		pw.flush();
		pw.close();
	}
	
	public static void clearLogFile() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(plugin.getDataFolder().getAbsoluteFile() + "/" + logFile);
			pw.print("");
			pw.close();
			logToFile("Log Cleared.");
		} catch (FileNotFoundException e) {
			logToFile("Error clearing log: "+e.getMessage());
		}
	}
}
