package datacontroller;

import login.StaffUser;
import eventlog.*;
import database.Movie;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.io.*;

public class DataController {

	private Scanner fileScanner;
	private List<String> filePaths;
	private EventLog eventLogger;
	private StaffUser staff;

	public DataController() throws FileNotFoundException {
		this.filePaths = new ArrayList<>();
     	this.eventLogger = new EventLog();
     	this.staff = new StaffUser();
	}

   public void addTable(String tableName)
   {
   		try
   		{
   			File table = new File("resources/" + tableName + ".tsv");
      		if (!table.exists())
      		{
         		table.createNewFile();

         		eventLogger.addEvent(new Event(LocalDateTime.now(), tableName, EventType.CREATETABLE, staff.getId()));
      		}
   		}
   		catch (IOException e)
   		{
   			System.out.println(e);
   		}
   }

   public void removeTable(String tableName)
   {
   		if (staff.getId() > 0) // -1 means not logged in, a boolean would be redundant
      	{
      		File table = new File("resources/" + tableName + ".tsv");
         	if (table.exists())
         	{
            	table.delete();

            	eventLogger.addEvent(new Event(LocalDateTime.now(), tableName, EventType.DELETETABLE, staff.getId()));
         	}
      	}
      	else
      	{
         	System.out.println("You must be logged in to perform this task.");
      	}
   }

	/**
	 *
	 * @param index
	 */
	public void addLine(String line, String tableName) {
		if (staff.getId() > 0)
		{
			try
			{
				File table = new File("resources/" + tableName + ".tsv");
				if (table.canWrite())
				{
					FileWriter fileWriter = new FileWriter(table, true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      				PrintWriter printWriter = new PrintWriter(bufferedWriter);

      				printWriter.println(line);
      				printWriter.flush();
      				printWriter.close();

					eventLogger.addEvent(new Event(LocalDateTime.now(), tableName, EventType.CREATE, staff.getId()));
				}
				else
				{
					System.out.println("Cannot write to table: " + tableName);
				}
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
		}
		else
		{
			System.out.println("Login is required to perform this task.");
		}
	}

	/**
	 *
	 * @param index
	 */
	public String[] readLine(int index, String tableName)
	{
		String[] entry = null;

   		try
   		{
			File table = new File("resources/" + tableName + ".tsv");

    		if (!table.canRead())
    		{
    			System.out.println("Cannot read from table: '" + tableName + "'");
    		}
    		else
    		{
    			fileScanner = new Scanner(table, "UTF-8");

    			for (int i = 0; i < index; i++)
    			{
            		if (fileScanner.hasNextLine())
            		{
               			entry = fileScanner.nextLine().split("\t");
            		}
            		else
            		{
               			System.out.println("Line does not exist. Lines read before stopping: " + i);
               			entry = null;
            		}
        		}
    		}
    	}
    	catch (IOException e)
    	{
    		System.out.println(e);
    	}

    	return entry;
	}

	public String[] readLine(String primaryKey, String tableName)
	{
		String[] entry = null;

   		try
   		{
			File table = new File("resources/" + tableName + ".tsv");

    		if (!table.canRead())
    		{
    			System.out.println("Cannot read from table: '" + tableName + "'");
    		}
    		else
    		{
    			fileScanner = new Scanner(table, "UTF-8");

    			boolean searchFinished = false;
    			while (!searchFinished)
    			{
    				if (fileScanner.hasNextLine())
    				{
    					entry = fileScanner.nextLine().split("\t");
    					if (primaryKey.equals(entry[0]))
    					{
    						searchFinished = true;
    					}
    				}
    				else
    				{
    					searchFinished = true;
    				}
    			}
    		}
    	}
    	catch (IOException e)
    	{
    		System.out.println(e);
    	}

    	return entry;
	}

	/**
	 *
	 * @param index
	 */
	public void updateLine(int index, String updatedEntry, String tableName) {
		// TODO - implement DataController.updateEntry
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param index
	 */
	public void removeLine(int index) {

	}


	public EventLog getEventLogger() {
		return eventLogger;
	}
	/**
	 *
	 * @param username
	 * @param password
	 */
	public boolean login(String username, String password) {
		try
		{
			File users = new File("datacontroller/login.txt");
			if (users.canRead())
			{
				fileScanner = new Scanner(users);

				Scanner inputScanner;
				String lineRead = "";

				while (fileScanner.hasNextLine())
				{
					lineRead = fileScanner.nextLine();
					String[] userInfo = lineRead.split("\t");

					if (userInfo[4].equals(username) && userInfo[5].equals(password))
					{
						try
						{
							int id = Integer.parseInt(userInfo[0]);
							staff = new StaffUser(id, userInfo[1], userInfo[2], userInfo[3], userInfo[4], userInfo[5], userInfo[6]);

							System.out.println("\nSuccesfully logged in!");

							return true;
						}
						catch (NumberFormatException e)
						{
							System.out.println("Login failed. File is corrupted.");
						}
					}
				}

				System.out.println("\nLogin failed. Wrong username/password entered.");
			}
			else
			{
				System.out.println("Login failed. File cannot be read.");
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

		return false;
	}

	public boolean getIsLoggedIn() {
		return (staff.getId() > 0);
	}

	public void logOut() {
		staff = new StaffUser();
	}

}
