package listem;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MyGrep extends CommonCode implements Grep
{

	private String sub;
	
	public MyGrep()
	{
		
	}
	
	@Override
	public Map<File, List<String>> grep(File directory,
			String fileSelectionPattern, String substringSelectionPattern,
			boolean recursive)
	{
		sub = substringSelectionPattern;
		process_file(directory, fileSelectionPattern, recursive);	
		
		return grep_map;
	}

	@Override
	void process_line(String line)
	{
		p = Pattern.compile(sub);
		m = p.matcher(line);
		
		if(m.find())
		{
			match_list.add(line);
		}	
	}
}
