package spell;

import java.util.Arrays;
import java.lang.StringBuilder;

public class MyTrie implements Trie
{
	private int word_count;
	private int node_count;
	private Node current_node;
	private Node root;
	private static boolean exists;
	
	public MyTrie()
	{
		root = new Node();
		current_node = root;
		word_count = 0;
		node_count = 1;
	}
	
	public void add(String word)
	{
		current_node = root;
		String lower_word = word.toLowerCase();
		
		if (correct_word(lower_word))
		{
		
			for (int i = 0; i < lower_word.length(); i++)
			{
				add(current_node, char_index(lower_word.charAt(i)));
			
				if (i == lower_word.length()-1 && !exists)
				{
					word_count++;
				}
			}
		
			current_node.increase_frequency();	
		}
	}
	
	public boolean correct_word(String word)
	{
		for (int i = 0; i < word.length(); i++)
		{
			if (char_index(word.charAt(i)) > 26 || char_index(word.charAt(i)) < 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void add(Node node, int index)
	{
		if (node.nodes[index] == null)
		{
			node.nodes[index] = new Node();
			node_count++;
			exists = false;
		}
	
		else
		{
			exists = true;
		}
		
		current_node = node.nodes[index];
	}
	
	public Node find(String word)
	{
		String lower_word = word.toLowerCase();
		current_node = root;
		
		for (int x = 0; x < lower_word.length(); x++)
		{
			if (current_node.nodes[char_index(lower_word.charAt(x))] != null)
			{
				current_node = current_node.nodes[char_index(lower_word.charAt(x))];
			}
			
			else
			{
				return null;
			}
		}
		
		if (current_node.getValue() > 0)
		{
			return current_node;
		}
		
		else
		{
			return null;
		}
	}
	
	public int char_index(char c)
	{
		if (c == ' ')
		{
			return 26;
		}
		
		return c - 'a';
	}
	
	public boolean is_empty()
	{
		return word_count == 0;
	}
	

	public char num_to_char(int x)
	{
		if (x == 26)
		{
			return ' ';
		}
		return (char) (x+'a');	
	}
	
	public int getWordCount()
	{
		return word_count;
	}
	
	public int getNodeCount()
	{
		return node_count;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		Recursive_toString(root, sb, "");	
		return sb.toString();
	}
	
	public void Recursive_toString(Node node, StringBuilder sb, String word)
	{
		if (node != null && node.getValue() > 0)
		{
			sb.append(word);
			sb.append(" " + find(word).getValue());
			sb.append("\n");
		}
		
		for (int i = 0; i < 26; i++)
		{
			if (node.nodes[i] != null)
			{
				Recursive_toString(node.nodes[i], sb, (word + num_to_char(i)));
			}
		}	
	}

	
	@Override
	public int hashCode()
	{
		return (root.initialized_nodes() + word_count + node_count);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		
		if (this == o)
			return true;
		
		if (this.getClass() != o.getClass())
		{
			return false;
		}
		
		MyTrie other = (MyTrie) o;
		if (getWordCount() != other.getWordCount())
		{
			return false;
		}
		if (getNodeCount() != other.getNodeCount())
		{
			return false;
		}
		return (this.root.equals(other.root));
	}

	public class Node implements Trie.Node 
	{

		private int frequency;
		private Node[] nodes;
		
		public Node()
		{
			frequency = 0;
			nodes = new Node[27];
		}
	
		public int getValue()
		{
			return frequency;
		}
		
		public Node[] getNodes()
		{
			return nodes;
		}
		
		public int initialized_nodes() //used in hash function; returns number of initialized nodes
		{
			int counter = 0;
			for (int i = 0; i < nodes.length; i++)
			{
				if (nodes[i] != null)
				{
					counter++;
				}
			}
			
			return counter;
		}
		
		public void increase_frequency()
		{
			this.frequency++;
		}

		private MyTrie getOuterType()
		{
			return MyTrie.this;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + frequency;
			result = prime * result + Arrays.hashCode(nodes);
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == null)
			{
				return false;
			}
			
			if (this == obj)
			{
				return true;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			Node other = (Node) obj;
			if (frequency != other.frequency)
			{
				return false;
			}
			if (!Arrays.equals(nodes, other.nodes))
			{
				return false;
			}
			
			return true;
		}
		
	}

}
