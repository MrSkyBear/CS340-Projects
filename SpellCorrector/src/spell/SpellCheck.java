package spell;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import spell.MyTrie;

public class SpellCheck implements SpellCorrector
{

	protected MyTrie words = new MyTrie();
	private Set<String> possible_matches = new HashSet<String>();
	private ArrayList<MyTrie> dictionaries = new ArrayList<MyTrie>();
	private Set<String> found_matches = new HashSet<String>();

	
	public void loadDictionary(InputStream dictionary, int id) throws IOException
	{
		dictionaries.add(new MyTrie());
		
		if (dictionary != null)
		{
			Scanner reader = new Scanner(dictionary);
			reader.useDelimiter(",");
			
			while (reader.hasNext())
			{
				String word = reader.next();
				dictionaries.get(id).add(word);
			}
			
			reader.close();
		}
	}
	
	public void set_suggestion_dictionary(int index)
	{
		words = dictionaries.get(index);
	}
	
	public boolean find(String word, int id)
	{
		if (dictionaries.get(id).is_empty())
		{
			return true;
		}
		
		Trie.Node n = dictionaries.get(id).find(word);
		return (n != null);
	}
	
	public boolean is_empty()
	{
		return dictionaries.size() == 0;
	}

	@Override
	public Set<String> suggestSimilarWord(String inputWord)
			//throws NoSimilarWordFoundException
	{
		if (words.is_empty())
		{
			System.out.println("Dictionary empty");
			return null;
		}
		
		found_matches.clear();
		
		inputWord = inputWord.toLowerCase();
		possible_matches.clear();
			
		possible_matches.add(inputWord);
		possible_matches.addAll(edit_distance(possible_matches));
		
		match(possible_matches);		

		match(edit_distance(possible_matches));
			
		return found_matches;

	}
	
	public Set<String> edit_distance(Set<String> list)
	{
		Set<String> matches = new HashSet<String>();
		for (String s : list)
		{
			matches.addAll(Deletion_Distance(s));
			matches.addAll(Transposition_Distance(s));
			matches.addAll(Alteration_Distance(s));
			matches.addAll(Insertion_Distance(s));
		}	
		
		return matches;			
	}
	
	
	public void match(Set<String> list)
	{	
		if (list.size() > 0)
		{	
			for (String s : list)
			{
				if (words.find(s) != null && words.find(s).getValue() > 0)
				{
					found_matches.add(s);
				}
			}
		}		
	}
	
	
	public Set<String> Deletion_Distance(String word)
	{
		String temp;
		Set<String> matches = new HashSet<String>();
		
		for (int i = 0; i < word.length(); i++)
		{
			temp = "";
			for (int j = 0; j < word.length(); j++)
			{
				if (j != i)
				{
					temp += word.charAt(j);
				}
			}
			
			matches.add(temp);
		}
		
		return matches;	
	}
	
	

	public Set<String> Transposition_Distance(String word)
	{
		Set<String> matches = new HashSet<String>();
		String temp = "";
		if (word.length() == 1)
		{
			matches.add(word);
			return matches;
		}
		
		for (int i = 0; i < word.length()-1; i++)
		{
			temp = "";
			for (int j = 0; j < word.length(); j++)
			{
				if (j == i)
				{
					temp += word.charAt(i+1);
				}
				
				else if (j == i+1)
				{
					temp += word.charAt(i);
				}	
				
				else
				{
					temp += word.charAt(j);
				}
			}
			
			matches.add(temp);
			
		}
		
		return matches;
	}
	
	public Set<String> Alteration_Distance(String word)
	{
		Set<String> matches = new HashSet<String>();
		
		for (int i = 0; i < word.length(); i++)
		{
			StringBuilder alteration = new StringBuilder(word);
			for (int j = 0; j < 26; j++)
			{
				alteration.setCharAt(i, num_to_char(j));
				matches.add(alteration.toString());
			}	
		}
		
		return matches;	
	}
	
	public Set<String> Insertion_Distance(String word)
	{
		Set<String> matches = new HashSet<String>();
		for (int i = 0; i <= word.length(); i++)
		{
			for (int j = 0; j < 26; j++)
			{
				StringBuilder insertion = new StringBuilder(word);
				insertion.insert(i, num_to_char(j));
				matches.add(insertion.toString());
			}	
		}
		
		return matches;	
	}
	
	public char num_to_char(int x)
	{
		return (char) (x+'a');	
	}
}
