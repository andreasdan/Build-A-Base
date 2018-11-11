package search;

import java.util.Scanner;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import database.Movie;
import database.Person;

public class SearchController
{
	private Scanner fileScanner;

	public SearchController()
		throws FileNotFoundException
	{
	}

	public String getTitle(String tconst)
	{
		String[] entry;
		String result = "";
		boolean searchFinished = false;
		Scanner scanner;

		try
		{
			File table = new File("resources/titlebasics.tsv");

			if (!table.canRead())
			{
				System.out.println("Cannot read from table: 'resources/titlebasics.tsv'");
			}
			else
			{
				scanner = new Scanner(table, "UTF-8");

				while (!searchFinished)
				{
					if (scanner.hasNextLine())
					{
						entry = scanner.nextLine().split("\t");

						if (tconst.equals(entry[0]))
						{
							result = "("+ entry[5] + ") " + entry[2]; // (YEAR) Title
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

		return result;
	}

	public String[] getRatingAndVotes(String tconst)
	{
		String[] entry;
		String[] result = new String[] { "Not found", "Not found" }; // default values in case nothing gets picked up in the process
		boolean searchFinished = false;

		Scanner scanner;

		try
		{
			File table = new File("resources/titleratings.tsv");

			if (!table.canRead())
			{
				System.out.println("Cannot read from table: 'resources/titleratings.tsv'");
			}
			else
			{
				scanner = new Scanner(table, "UTF-8");

				while (!searchFinished)
				{
					if (scanner.hasNextLine())
					{
						entry = scanner.nextLine().split("\t");

						if (tconst.equals(entry[0]))
						{
							result[0] = entry[1];
							result[1] = entry[2];
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

		return result;
	}

	public List<Person> searchPerson(String name) {
		// find person in namebasics.txt
		// lookup titles from tconst.
		// Find titles in titlebasics.txt
		// show line[2] for primarytitle

		int maxResults = 5;
		String[] entry;
		List<Person> persons = new ArrayList<>();
		String[] movies;

   		try
   		{
			File table = new File("resources/namebasics.tsv");

    		if (!table.canRead())
    		{
    			System.out.println("Cannot read from table: 'resources/namebasics.tsv'");
    		}
    		else
    		{
    			fileScanner = new Scanner(table, "UTF-8");

				while (fileScanner.hasNextLine() && persons.size() < maxResults)
				{
             		entry = fileScanner.nextLine().split("\t");

					if (name.toLowerCase().equals(entry[1].toLowerCase())) //make case insensitive to help
					{

						// result[0] = entry[0]; // nconst
						// result[1] = entry[1]; // PrimaryName
						// result[2] = entry[2]; // Birth
						// result[3] = entry[3]; // Death

						// result[5] = "";

						if (entry[3].contains("\\N"))
						{
							entry[3] = "-";
						}

						entry[4] = entry[4].replace(",", ", "); // Profession

						movies = entry[5].split(","); // Movie Titles
						String titles = "";

						for (String m : movies)
						{
							titles += "- " + getTitle(m) + "\n";
						}

						persons.add(new Person(entry[0], entry[1], entry[2], entry[3], entry[4], titles));
					}
				}
    		}
    	}

    	catch (IOException e)
    	{
    		System.out.println(e);
    	}

		return persons;
	}

	public List<Movie> searchTitle(String title)
	{
		int maxResults = 10;
		String[] entry;
		List<Movie> movies = new ArrayList<>();
		boolean searchFinished = false;

   		try
   		{
			File table = new File("resources/titlebasics.tsv");

    		if (!table.canRead())
    		{
    			System.out.println("Cannot read from table: 'resources/titlebasics.tsv'");
    		}
    		else
    		{
    			fileScanner = new Scanner(table, "UTF-8");

				while (fileScanner.hasNextLine() && movies.size() < maxResults)
				{
             		entry = fileScanner.nextLine().split("\t");

					if (title.toLowerCase().equals(entry[2].toLowerCase()) && entry[1].toLowerCase().equals("movie")) //make case insensitive to help.. also check both primary and original title
					{
							/*
							entry[0]; // tconst
							entry[1]; // titleType
							entry[2]; // primaryTitle
							entry[3]; // originalTitle
							entry[4]; // isAdult
							entry[5]; // startYear
							entry[6]; // endYear
							entry[7]; // runTimeMinutes
							entry[8]; // genres
							*/

						if (entry[4].equals("0")) //translate 0/1 to yes/no in terms of "isAdult"
						{
							entry[4] = "No";
						}
						else
						{
							entry[4] = "Yes";
						}

						if (entry[6].contains("\\N")) //replace \N with -
						{
							entry[6] = "-";
						}

						if (entry[7].contains("\\N")) //replace \N with -
						{
							entry[7] = "-";
						} else {
							entry[7] = entry[7] + " minutes";
						}

						entry[8] = entry[8].replace(",", ", ");

						String[] ratingAndVote = getRatingAndVotes(entry[0]);

						movies.add(new Movie(entry[0], entry[1], entry[2], entry[3], entry[4], entry[5], entry[6], entry[7], entry[8], ratingAndVote[0], ratingAndVote[1]));
        			}
				}
    		}
    	}
    	catch (IOException e)
    	{
    		System.out.println(e);
    	}

		return movies;
	}
}
