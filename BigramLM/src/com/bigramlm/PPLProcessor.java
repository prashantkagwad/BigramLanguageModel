package com.bigramlm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Prashant Kagwad [pdk130030]
 * 
 */
public class PPLProcessor {

	private HashMap<String, Double> wordMapProb1 = new HashMap<String, Double>();
	private HashMap<String, Double> wordMapAlpha = new HashMap<String, Double>();
	private HashMap<String, Double> wordMapProb2 = new HashMap<String, Double>();
	private int totalNumberOfWords;

	public HashMap<String, Double> getWordMapProb1() {
		return wordMapProb1;
	}

	public void setWordMapProb1(HashMap<String, Double> wordMapProb1) {
		this.wordMapProb1 = wordMapProb1;
	}

	public HashMap<String, Double> getWordMapAlpha() {
		return wordMapAlpha;
	}

	public void setWordMapAlpha(HashMap<String, Double> wordMapAlpha) {
		this.wordMapAlpha = wordMapAlpha;
	}

	public HashMap<String, Double> getWordMapProb2() {
		return wordMapProb2;
	}

	public void setWordMapProb2(HashMap<String, Double> wordMapProb2) {
		this.wordMapProb2 = wordMapProb2;
	}

	public int getTotalNumberOfWords() {
		return totalNumberOfWords;
	}

	public void setTotalNumberOfWords(int totalNumberOfWords) {
		this.totalNumberOfWords = totalNumberOfWords;
	}

	public PPLProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void getUnigramDeatils(String key) {

		System.out.println("Key : " + key);
		System.out.println("Prob : " + wordMapProb1.get(key));
		System.out.println("Alpha : " + wordMapAlpha.get(key));
	}

	public void getBigramDeatils(String key) {

		System.out.println("Key : " + key);
		System.out.println("Prob : " + wordMapProb2.get(key));
	}

	public int getWordCount(List<Entry<String, Integer>> list) {

		int totalWordCount = 0;
		for (Map.Entry<String, Integer> entry : list) {

			totalWordCount += entry.getValue();
		}
		return totalWordCount;
	}

	public HashMap<String, Integer> readTestFile(String fileName) {

		FileReader file = null;
		BufferedReader reader = null;
		String line = "";

		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		try {

			file = new FileReader(fileName);
			reader = new BufferedReader(file);
			while ((line = reader.readLine()) != null) {

				String st[] = line.split(" ");

				for (int i = 0; i < st.length - 1; i++) {

					String word1 = st[i].toLowerCase();
					String word2 = st[i + 1].toLowerCase();

					String key = word1 + " " + word2;

					if (wordMap.containsKey(key)) {

						wordMap.put(key, wordMap.get(key) + 1);
					} else {

						wordMap.put(key, 1);
					}
					totalNumberOfWords++;
				}
				totalNumberOfWords++;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (file != null) {
				try {
					reader.close();
					file.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return wordMap;
	}

	public void readLMFileForUniGramProbs(String fileName) {

		FileReader file = null;
		BufferedReader reader = null;
		String line = "";

		try {

			file = new FileReader(fileName);
			reader = new BufferedReader(file);
			while ((line = reader.readLine()) != null) {

				if (line.equalsIgnoreCase("unigrams:")) {

					while ((line = reader.readLine()) != null) {

						if (line.equalsIgnoreCase("")) {
							return;
						}

						String st[] = line.split(" ");
						if (st.length == 3) {

							double logProb = Double.parseDouble(st[0]);
							String word = st[1];
							double alpha = Double.parseDouble(st[2]);

							wordMapProb1.put(word.toLowerCase(), logProb);
							wordMapAlpha.put(word.toLowerCase(), alpha);
						}
					}

				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			if (file != null) {
				try {
					reader.close();
					file.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void readLMFileForBiGramProbs(String fileName) {

		FileReader file = null;
		BufferedReader reader = null;
		String line = "";

		try {

			file = new FileReader(fileName);
			reader = new BufferedReader(file);
			while ((line = reader.readLine()) != null) {

				if (line.equalsIgnoreCase("bigrams:")) {

					while ((line = reader.readLine()) != null) {

						String st[] = line.split(" ");
						if (st.length == 3) {

							double logProb = Double.parseDouble(st[0]);
							String word1 = st[1];
							String word2 = st[2];
							String key = word1 + " " + word2;

							wordMapProb2.put(key.toLowerCase(), logProb);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (file != null) {
				try {
					reader.close();
					file.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public double computePerplexity(HashMap<String, Integer> wordMap) {

		double perplexity = 0;

		try {

			double summation = 0;

			for (Entry<String, Integer> entry : wordMap.entrySet()) {

				String bigramKey = entry.getKey();

				if (wordMapProb2.containsKey(bigramKey)) {

					summation = summation
							+ (wordMapProb2.get(bigramKey) * entry.getValue());
				} else {

					String bigramKeys[] = bigramKey.split(" ");
					if (bigramKeys.length == 2) {

						summation = summation
								+ ((double) wordMapAlpha.get(bigramKeys[0]) + (wordMapProb1
										.get(bigramKeys[1]) * entry.getValue()));
					}
				}
			}
			// System.out.println("summation : " + summation);
			// System.out.println("totalNumberOfWords : " + totalNumberOfWords);

			perplexity = Math.pow(2.0,
					(summation / (double) (-1 * totalNumberOfWords)));

		} catch (Exception e) {
			e.printStackTrace();

		}

		return perplexity;
	}

	public void printMaps() {

		try {

			System.out.println("wordMapProb1 : " + wordMapProb1.size());
			System.out.println("wordMapProb2 : " + wordMapProb2.size());
			System.out.println("wordMapAlpha : " + wordMapAlpha.size());

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
