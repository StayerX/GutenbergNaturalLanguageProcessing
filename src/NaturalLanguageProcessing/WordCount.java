package NaturalLanguageProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

/**
 * Word frequency counter (indexing) for a word file
 * implemented with ArrayList<Word>
 * Where word is defined as the tuple word frequency.
 * @author Stanislav
 */
public class WordCount {

	ArrayList<Word> wordAL;

	public static void main(String[] argv) {
		double start = System.currentTimeMillis();
		String fileName = "pg2600.txt";
		WordCount wordCount = new WordCount();
		wordCount.populate(fileName);
		wordCount.printToFile("tf_2.csv");
		wordCount.sort(false);
		wordCount.printToFile("top10_2.csv",10);
		double end = System.currentTimeMillis();
		System.out.println(end-start);
	}

	private void sort(Boolean ascending) {
		if (ascending) {
			wordAL.sort(Word.CountComparator);
		} else {
			wordAL.sort(Word.CountComparatorDescending);
		}
	}

	public WordCount() {
		wordAL = new ArrayList<Word>();
	}

	private static class Word implements Comparable<Word> {
		private String word;
		private Integer count;

		public Word() {
			word = "";
			count = 0;
		}

		public Word(String word) {
			this.word = word;
			this.count = 1;
		}

		public Word(Word word) {
			this.word = word.word;
			this.count = word.count;
		}

		public Word increaseCount() {
			count++;
			return this;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public String getWord() {
			return this.word;
		}

		public Integer getCount() {
			return this.count;
		}

		public static Comparator<Word> WordComparator = new Comparator<Word>() {

			public int compare(Word w1, Word w2) {
				String wordOne = w1.word;
				String wordTwo = w2.word;
				// ascending order
				return wordOne.compareTo(wordTwo);
			}
		};

		/* Comparator for sorting the list by count no ascending*/
		public static Comparator<Word> CountComparator = new Comparator<Word>() {
			public int compare(Word w1, Word w2) {
				int rollno1 = w1.getCount();
				int rollno2 = w2.getCount();
				/* For ascending order */
				return rollno1 - rollno2;
			}
		};

		/* Comparator for sorting the list by count no descending*/
		public static Comparator<Word> CountComparatorDescending = new Comparator<Word>() {

			public int compare(Word w1, Word w2) {
				int rollno1 = w1.getCount();
				int rollno2 = w2.getCount();
				/* For descending order */
				return rollno2 - rollno1;
			}
		};

		@Override
		public String toString() {
			return this.word;
		}

		@Override
		public int compareTo(Word o) {
			return this.word.compareTo(o.word);
		}

		@Override
		public boolean equals(Object object) {
			boolean isEqual = false;

			if (object != null && object instanceof Word) {
				isEqual = (this.word.equals(((Word) object).word));
			}

			return isEqual;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			for (int i = 0; i < this.word.length(); i++) {
				hash = hash * 31 + this.word.charAt(i);
			}
			return hash;
		}
	}

	/**
	 * Populate will parse an existing file and populate the array containing the
	 * words and their frequency
	 * 
	 * @param fileName
	 *            the file that we are parsing
	 */
	public void populate(String fileName) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = null;
			Word myword = new Word();
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("[^A-z]", " ");
				for (String word : line.split(" ")) {
					word = word.toLowerCase(Locale.ENGLISH);
					if (word.isEmpty()) {
						continue;
					}
					myword.setWord(word);
					int indexWord = wordAL.indexOf(myword);
					if (indexWord >= 0) {
						// System.out.printf("%s:%d||%d-%s\n",myword.getWord(),indexWord,wordAL.size(),wordAL.get(0).word);
						// System.out.println("~~");
						wordAL.set(indexWord, wordAL.get(indexWord).increaseCount());
						// (new Exception(""));
					} else {
						// System.out.println("~1~");
						wordAL.add(new Word(word));
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.out.printf("File not found.\n");
		} catch (IOException e) {
			System.out.printf("BufferReader Error.\n");
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void printToFile(String fileName) {
		printToFile(fileName, this.wordAL.size());
	}

	public void printToFile(String fileName, int howMany) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("word, frequency");
			for (Word word : this.wordAL) {
				if (--howMany < 0) {
					break;
				}
				writer.printf("%s, %d\n", word.getWord(), word.getCount());
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// To do auto generated
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}
}
