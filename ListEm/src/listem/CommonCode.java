package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CommonCode
{

	private Vector<File> the_files = new Vector<File>();
	protected Pattern p;
	protected Matcher m;
	abstract void process_line(String line);
	public Map<File, List<String>> grep_map = new HashMap<File, List<String>>();
	public Map<File, Integer> line_map = new HashMap<File, Integer>();
	public List<String> match_list = new ArrayList<String>();
	protected int line_count = 0;
	
	
	public void process_file(File directory, String fileSelectionPattern, boolean recursive) 
	{
		line_map.clear();
		grep_map.clear();
		the_files.clear();
		p = Pattern.compile(fileSelectionPattern);
		
		populate_list(directory, recursive);
		
			for (File file : the_files)
			{
				try
				{
					Scanner reader = new Scanner(new FileReader(file));
					match_list = new ArrayList<String>();
					line_count = 0;
					while(reader.hasNextLine())
					{	
						String temp = reader.nextLine();
						process_line(temp);
					}
					
					if (!(match_list.isEmpty()))
					{
						grep_map.put(file, match_list);		
					}
					
					else if (line_count > 0)
					{
						line_map.put(file, line_count);
					}
					
					reader.close();
				}
				
				catch(FileNotFoundException fe) 
				{
			        System.out.println("Please input correct filename");
				}	
			}	
		
	}
	
	public void populate_list(File directory, boolean recursive)
	{
		if (directory.isDirectory())
		{
			File[] temp = directory.listFiles();
			for (File x : temp)
			{
				if (x.isDirectory() && recursive)
				{
					populate_list(x, true);
				}
				else if(x.isFile())
				{
					m = p.matcher(x.getName());
					boolean b = m.matches();
					
					if (b)
					{	
						the_files.add(x);
					}
				}
			}
		}
		
		else
		{
			if (directory.isFile())
			the_files.add(directory);
		}
	}
	
	

}
