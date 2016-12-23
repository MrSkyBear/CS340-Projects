package hangman;

import java.util.HashSet;
import java.util.Set;

public class Entry implements Comparable<Entry>
{
	private String key;
	private Set<String> values;
	
	public Entry(String key, Set<String> values)
	{
		this.values = new HashSet<String>();
		this.key = key;
		for (String s : values)
		{
			this.values.add(s);
		}
	}
	
	public Set<String> get_values()
	{
		return this.values;
	}
	
	public String get_key()
	{
		return this.key;
	}
	
	public int get_size()
	{
		return this.values.size();
	}
	
	public boolean isDashes(String key)
	{
		for (int i = 0; i < key.length(); i++)
		{
			if (key.charAt(i) != '-')
			{
				return false;
			}
		}
		
		return true;	
	}
	
	public int revealed()
	{
		int count = 0;
		for (int i = 0; i < this.key.length(); i++)
		{
			if (this.key.charAt(i) != '-')
			{
				count++;
			}
		}
		return count;
	}
	
	private int right_score()
	{
		int score = 0;
		
		for (int i = this.key.length()-1; i >= 0; i--)
		{
			if (this.key.charAt(i) != '-')
			{
				score += (i + this.key.length());
			}	
		}
		return score;
	}


	@Override
	public int compareTo(Entry o) 
	{
		if (this.values.size() > o.get_size())
		{
			return -1;
		}
		else if (o.get_size() > this.values.size())
		{
			return 1;
		}
		else
		{
			if (isDashes(this.key) && !isDashes(o.get_key()))
			{
				return -1;
			}
			else if (isDashes(o.get_key()) && !isDashes(this.key))
			{
				return 1;
			}
			else
			{
				if (this.revealed() < o.revealed())
				{
					return -1;
				}
				else if (o.revealed() < this.revealed())
				{
					return 1;
				}
				else
				{
					if (this.right_score() > o.right_score())
					{
						return -1;
					}
					else if (o.right_score() > this.right_score())
					{
						return 1;
					}
				}
			}
		}
		
		return 0;
	}

}
