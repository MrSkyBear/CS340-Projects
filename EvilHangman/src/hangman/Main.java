package hangman;

import hangman.EvilHangmanGame.GuessAlreadyMadeException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main
{

	public static void main(String[] args) throws IOException {
		
		File dictionary_file = new File(args[0]);
		int word_length = Integer.parseInt(args[1]);	
		int guesses = Integer.parseInt(args[2]);
		
		ArrayList<String> guessed = new ArrayList<String>();
		Set<String> words = new HashSet<String>();
		ArrayList<String> word_list = new ArrayList<String>();
		
		String test = "";
		boolean flag = false;
		
		EvilHangmanGame my_game = new EvilHangman();
		my_game.startGame(dictionary_file, word_length);
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < word_length; i++)
		{
			builder.append('-');
		}
		
		
		for (int i = 0; i < guesses; i++)
		{
			if (guesses-i == 1)
			{
				System.out.printf("You have %d guess left\n", guesses-i);
			}
			else
			{
				System.out.printf("You have %d guesses left\n", guesses-i);
			}
			
			System.out.print("Used letters: ");
			for (String s : guessed)
			{
				System.out.printf("%s ", s);
			}
			
			System.out.println();
			System.out.printf("Word: %s\n",builder.toString());
			System.out.print("Enter guess: ");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			char guess = (char) br.read();
			
			if (Character.isAlphabetic(guess))
			{
				try 
				{
					guess = Character.toLowerCase(guess);
					words = my_game.makeGuess(guess);
					
					String letter = "";
					letter += guess;
					guessed.add(letter);
					
					word_list.clear();
					for (String s : words)
					{
						word_list.add(s);
					}
					
					Random rn = new Random();
					int rand = rn.nextInt(word_list.size());
					
					test = word_list.get(rand);
					int count = 0;
					for (int j = 0; j < test.length(); j++)
					{
						if (test.charAt(j) == guess)
						{
							builder.setCharAt(j, guess);
							count++;
						}
					}
					
					if (count == 1)
					{
						System.out.printf("Yes, there is one %d %s\n", count, guess);
					}
					else if (count > 1)
					{
						System.out.printf("Yes, there are %d %s's\n", count, guess);
					}
					else
					{
						System.out.printf("Sorry, there are no %s's\n", guess);
					}
					
					System.out.println();
					
					if (correct(builder.toString()))
					{
						System.out.println("You win!");
						System.out.printf("The word was: %s\n", builder.toString());
						i = guesses + 1;
						flag = true;
					}
					
					
				}
				catch (GuessAlreadyMadeException e) 
				{
					System.out.println("Guess already made");
					System.out.println();
					i--;
				}
			}
			
			else
			{
				System.out.println("Invalid Input");
				System.out.println();
				i--;
			}
			
		}
		
		if (!flag)
		{
			System.out.println("You lose!");
			System.out.printf("The word was: %s\n", test);
		}

	}
	
	public static boolean correct(String word)
	{
		for (int x = 0; x < word.length(); x++)
		{
			if (word.charAt(x) == '-')
			{
				return false;
			}
		}
		
		return true;
	}

}
