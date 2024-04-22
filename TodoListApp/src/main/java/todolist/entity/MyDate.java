package todolist.entity;

import java.sql.Date;
import java.sql.Time;


// classe per definire oggetti da sql di tipo DATETIME 
public class MyDate {
	
	private int year;
	private int month;
	private int day;
	
	private int hour;
	private int minutes;
	
	public MyDate(int year, int month, int day, int hour, int minutes) {
		this.year = year;
		this.month = month;
		this.day = day;
		
		this.hour = hour;
		this.minutes = minutes;
		
		try {
			@SuppressWarnings({ "deprecation", "unused" })
			Date date = new Date(year, month, day);
			@SuppressWarnings({ "deprecation", "unused" })
			Time time = new Time(hour, minutes, 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minutes;
	}
	
	// YY-MM-DD HH:MM
	public static MyDate valueOf(String date) {
		
		String[] stringDate = date.split(" ");
		
		String[] parsingDate = stringDate[0].split("-");
		
		int year = Integer.parseInt(parsingDate[0]);
		int month = Integer.parseInt(parsingDate[1]);
		int day = Integer.parseInt(parsingDate[2]);
		
		String[] parsingTime = stringDate[1].split(":");
		
		int hour = Integer.parseInt(parsingTime[0]);
		int minutes = Integer.parseInt(parsingTime[1]);
		
		return new MyDate(year, month, day, hour, minutes);
	}
	
	

	@Override
	public String toString() {
		return year + "-" + month + "-" + day + " " + hour + ":" + minutes;
	}
	
}
