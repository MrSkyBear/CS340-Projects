package listem;

import java.io.File;
import java.util.Map;

public class Counter extends CommonCode implements LineCounter
{

	public Counter()
	{
			
	}
	
	@Override
	public Map<File, Integer> countLines(File directory,
			String fileSelectionPattern, boolean recursive)
	{
		process_file(directory, fileSelectionPattern, recursive);
		return line_map;
	}

	@Override
	void process_line(String line)
	{
		line_count++;
	}
}
