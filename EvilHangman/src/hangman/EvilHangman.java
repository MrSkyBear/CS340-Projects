package hangman;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class EvilHangman implements EvilHangmanGame{

	private Set<String> current_set;
	private Set<String> guessed_letters;
	private Map<String, Set<String>> my_map;
	private Queue<Entry> my_queue;
	
	public EvilHangman()
	{
		current_set = new HashSet<String>();
		guessed_letters = new HashSet<String>();
		my_queue = new PriorityQueue<Entry>();
		my_map = new HashMap<String, Set<String>>();
	}
	
	@Override
	public void startGame(File dictionary, int wordLength)
	{
		current_set.clear();
		try 
		{
			Scanner reader = new Scanner(new FileReader(dictionary));
			while (reader.hasNext())
			{
				String word = reader.next().toLowerCase();
				if (valid(word, wordLength)) //pulls all words from dictionary with valid characters and proper word length
				{
					current_set.add(word);
				}
			}
			reader.close();
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}	
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException
	{
		my_queue = new PriorityQueue<Entry>();
		my_map = new HashMap<String, Set<String>>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(guess);
		
		String pattern = "";
		if (guessed_letters.contains(sb.toString()))
		{
			throw new GuessAlreadyMadeException();
		}
		
		else
		{
			guessed_letters.add(sb.toString());
			
			for (String x : current_set)
			{
				pattern = create_pattern(x, guess);
				
				if (my_map.containsKey(pattern))
				{
					my_map.get(pattern).add(x);
				}
				else
				{
					my_map.put(pattern, new HashSet<String>());
					my_map.get(pattern).add(x);
				}
			}
			
			for (Map.Entry<String, Set<String>> i : my_map.entrySet())
			{
				Entry temp = new Entry(i.getKey(), i.getValue());
				my_queue.add(temp);
			}
			
			current_set = my_queue.poll().get_values();
			
		}		
		
		return current_set;
	}
	
	public String create_pattern(String word, char letter)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) != letter)
			{
				sb.append("-");
			}
			
			else
			{
				sb.append(letter);
			}
		}
		
		return sb.toString();
	}

	public boolean valid(String word, int wordLength)
	{
		for (int i = 0; i < word.length(); i++)
		{	
			if (!(Character.isLetter(word.charAt(i))))
			{
				return false;
			}
		}
		
		if (wordLength == word.length())
		{
			return true;
		}
		
		return false;
	
	}
}
