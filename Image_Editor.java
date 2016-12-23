
import java.util.Scanner;
import java.io.*;
import java.awt.*;

public class Image_Editor 
{
	private Color[][] input_image;
	
	private String type;
	private int width;
	private int height;
	private int max_color;

	public static void main(String[] args)
	{
		int flag = valid_arguments(args);
		if (flag == 0)
		{
			Image_Editor my_editor = new Image_Editor();
			String filename = args[0];
			String out_file = args[1];
			
			int motion_blur = 0;
			
			if (args.length == 4)
			{
				motion_blur = Integer.parseInt(args[3]);
			}
			
			try
			{
				Scanner reader = new Scanner(new FileReader(filename));
				my_editor.load_image(reader);
				
				my_editor.choose_transformation(args[2], motion_blur);
			
				my_editor.write_image(out_file);
				reader.close();
			}
			
			catch(FileNotFoundException fe) 
			{
		        System.out.println("Please input correct filename");
			}
		}
		
		else
		{
			switch (flag)
			{
				case 1:
					System.out.println("Usage: Incorrect Number of Arguments\n\t");
					System.out.println("Example: java ImageEditor input.ppm output.ppm (grayscale|invert|emboss|motionblur length)");
					break;
				case 2: System.out.println("Usage: Motion blur must be greater than 0"); break;
				default: System.out.println("Error");
			}		
		}
		
	}
	
	public static int valid_arguments(String[] list)
	{
		if (list.length < 3 || list.length > 4)
		{
			return 1; //Wrong number of arguments
		}
	
		if (list[2].equals("invert") || list[2].equals("grayscale") || list[2].equals("emboss") || list[2].equals("motionblur"))
		{
			if (list[2].equals("motionblur"))
			{
				if (Integer.parseInt(list[3]) < 0)
				{
					return 2; //Motion blur must be greater than 0
				}
				
				else
				{
					return 0; //correct transformation input
				}
			}
			else
			{
				return 0; //correct transformation input
			}
		}
		
		return 3; //default, catches any other error
	}
	
	public void load_image(Scanner reader)
	{
		type = reader.nextLine();
		String dummy=reader.nextLine();
		
		width = reader.nextInt(); //width == cols
		height = reader.nextInt(); //height == rows
		max_color = reader.nextInt();
		
		input_image = new Color[height][width];
		
		int r;
		int g;
		int b;
		
		Color rgb;
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				r = reader.nextInt();
				g = reader.nextInt();
				b = reader.nextInt();
			
				rgb = new Color(r, g, b);
				input_image[i][j] = rgb;
			}
		}		
	}
	
	public void choose_transformation(String choice, int blur)
	{		
		if (choice.equals("invert"))
		{
			invert();
		}
		
		else if (choice.equals("grayscale"))
		{
			grayscale();
		}
		
		else if (choice.equals("emboss"))
		{
			emboss();
		}
		
		else if (choice.equals("motionblur"))
		{
			motionblur(blur);
		}
	}
	
	public void write_image(String output_file)
	{
		try
		{
			File file = new File(output_file);
			PrintWriter output = new PrintWriter(file);
			
			if (!file.exists()) 
			{
				file.createNewFile();
			}
					
			int r, g, b;
			
			output.printf("%s\n%d %d\n%d\n", type, width, height, max_color);
						
			for (int i = 0; i < height; i++)
			{
				for (int j = 0; j < width; j++)
				{
					r = input_image[i][j].getRed();
					g = input_image[i][j].getGreen();
					b = input_image[i][j].getBlue();
					
					output.printf("%d %d %d\n", r, g, b);
				}
			}
			
			output.close();
			
		}
		catch (IOException x)
		{
			System.out.println("Error");
		}
	}
	
	public void invert()
	{
		int red, green, blue;
		Color rgb;
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				red = invert_scale(input_image[i][j].getRed());
				green = invert_scale(input_image[i][j].getGreen());
				blue = invert_scale(input_image[i][j].getBlue());
				
				rgb = new Color(red, green, blue);
				input_image[i][j] = rgb;
			}
		}
	}
	
	public int invert_scale(int test)
	{
		if (test == 0)
		{
			return 255;
		}
		
		else
		{
			return (255 % test);
		}	
	}
	
	public void grayscale()
	{
		int average = 0;
		Color rgb;
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				average = 0;
				average += input_image[i][j].getRed();
				average += input_image[i][j].getGreen();
				average += input_image[i][j].getBlue();
				
				average = (average / 3);
				
				rgb = new Color(average, average, average);
				input_image[i][j] = rgb;
			}
		}
		
	}
	
	public void emboss()
	{
		int reddiff, greendiff, bluediff, max_diff;
		Color rgb;
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if ((i-1) < 0 || (j-1) < 0)
				{
					rgb = new Color(128, 128, 128);
					input_image[i][j] = rgb;
				}
				
				else
				{
					reddiff = (input_image[i][j].getRed() - input_image[i-1][j-1].getRed());
					greendiff = (input_image[i][j].getGreen() - input_image[i-1][j-1].getGreen());
					bluediff = (input_image[i][j].getBlue() - input_image[i-1][j-1].getBlue());
					
					max_diff = emboss_conversion(reddiff, greendiff, bluediff);
					
					rgb = new Color(max_diff, max_diff, max_diff);
					input_image[i-1][j-1] = rgb;// **IMPORTANT**
					
				}
			}
		}
		
	}
	
	public int emboss_conversion(int x, int y, int z)
	{
		int ax;
		int ay;
		int az;
		
		int max = 0;
		
		ax = abs(x);
		ay = abs(y);
		az = abs(z);		
		
		if ((ax >= ay) && (ax >= az))
		{
			max = (x + 128);
		}
		
		else if ((ay >= ax) && (ay >= az))
		{
			max = (y + 128);
		}
		
		else if ((az >= ax) && (az >= ay))
		{
			max = (z + 128);
		}	
		
		return scale(max);
	}
	
	
	public int abs(int x)
	{
		if (x < 0)
		{
			x *= -1;
		}
		
		return x;
	}
	
	public int scale(int max)
	{
		if (max < 0)
		{
			max = 0;
		}
		
		else if (max > 255)
		{
			max = 255;
		}
		
		return max;	
	}
	
	public void motionblur(int blur)
	{
		int red_avg, green_avg, blue_avg;
		
		int h;
		int count;
		
		Color rgb;
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				red_avg = 0;
				green_avg = 0;
				blue_avg = 0;
				
				h = j;
				count = 0;
				
				for (int x = 0; x < blur; x++)
				{
					if (h < width)
					{
						red_avg += input_image[i][h].getRed();
						green_avg += input_image[i][h].getGreen();
						blue_avg += input_image[i][h].getBlue();
						count++;
						h++;
					}
					
					else
					{
						break;
					}
					
				}
				
				red_avg /= (count);
				green_avg /= (count);
				blue_avg /= (count);
				
				rgb = new Color(red_avg, green_avg, blue_avg);
				input_image[i][j] = rgb;		
			}
		}			
	}
}
