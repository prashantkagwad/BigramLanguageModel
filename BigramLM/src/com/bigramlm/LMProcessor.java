package com.bigramlm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

/**
 * @author Prashant Kagwad [pdk130030]
 * 
 */
public class LMProcessor {

	private HashMap<String, UniGramObj> hashMap;
	private int totalNumberOfWords;
	private int numberOfWordsOccOnce;
	private int numberOfWordsOccTwice;

	public HashMap<String, UniGramObj> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, UniGramObj> hashMap) {
		this.hashMap = hashMap;
	}

	public int getTotalNumberOfWords() {
		return totalNumberOfWords;
	}

	public void setTotalNumberOfWords(int totalNumberOfWords) {
		this.totalNumberOfWords = totalNumberOfWords;
	}

	public int getNumberOfWordsOccOnce() {
		return numberOfWordsOccOnce;
	}

	public void setNumberOfWordsOccOnce(int numberOfWordsOccOnce) {
		this.numberOfWordsOccOnce = numberOfWordsOccOnce;
	}

	public int getNumberOfWordsOccTwice() {
		return numberOfWordsOccTwice;
	}

	public void setNumberOfWordsOccTwice(int numberOfWordsOccTwice) {
		this.numberOfWordsOccTwice = numberOfWordsOccTwice;
	}

	public LMProcessor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static int getWordOccurence(List<Entry<String, Integer>> list,
			int times) {

		int totalWordCount = 0;

		for (Map.Entry<String, Integer> entry : list) {

			if (entry.getValue() == times) {
				totalWordCount++;
			}
		}

		return totalWordCount;
	}

	public void generateUniGramCounts(String fileName) {

		FileReader file = null;
		BufferedReader reader = null;
		String line = "";

		hashMap = new HashMap<String, UniGramObj>();
		try {

			file = new FileReader(fileName);
			reader = new BufferedReader(file);
			while ((line = reader.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line, " ");

				while (st.hasMoreTokens()) {

					String key = st.nextToken().toLowerCase();

					if (hashMap.containsKey(key)) {

						UniGramObj obj = hashMap.get(key);
						// System.out.println("key : " + key);
						// System.out.println("count : " + tempCount);
						obj.setUnigramCount(obj.getUnigramCount() + 1);
						hashMap.put(key, obj);

					} else {

						UniGramObj obj = new UniGramObj();
						obj.setUnigramCount(1);
						hashMap.put(key, obj);

					}
					totalNumberOfWords++;
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

	public void generateUniGramProbablities() {

		try {

			for (Entry<String, UniGramObj> entry : hashMap.entrySet()) {

				String wordListing = "";
				String key = entry.getKey();

				UniGramObj unigramObj = entry.getValue();
				int count = unigramObj.getUnigramCount();
				double probability = ((double) count / totalNumberOfWords);

				wordListing = wordListing + probability + " ";
				wordListing = wordListing + key + " ";
				wordListing = wordListing + count + "\n";

				// System.out.println(key + " " + count + " " + probability);

				unigramObj.setUnigramProb(probability);

				hashMap.put(key, unigramObj);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void generateBiGramCounts(String fileName) {

		FileReader file = null;
		BufferedReader reader = null;
		String line = "";

		try {

			file = new FileReader(fileName);
			reader = new BufferedReader(file);
			while ((line = reader.readLine()) != null) {

				String st[] = line.split(" ");

				for (int iterator = 0; iterator < st.length - 1; iterator++) {

					String word1 = st[iterator].toLowerCase();
					String word2 = st[iterator + 1].toLowerCase();

					if (hashMap.containsKey(word1)) {

						UniGramObj x = hashMap.get(word1);
						HashMap<String, BiGramObj> y = x.getHashMap();

						if (y.containsKey(word2)) {

							BiGramObj obj2 = y.get(word2);
							obj2.setBigramCount(obj2.getBigramCount() + 1);
							y.put(word2, obj2);
							x.setHashMap(y);

						} else {

							BiGramObj obj2 = new BiGramObj();
							obj2.setBigramCount(1);
							y.put(word2, obj2);
							x.setHashMap(y);

						}
						hashMap.put(word1, x);
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

	public void generateBigramProbabilities() {

		try {

			for (Entry<String, UniGramObj> entry : hashMap.entrySet()) {

				String key = entry.getKey();
				UniGramObj unigramObj = entry.getValue();

				int unigramCount = unigramObj.getUnigramCount();

				HashMap<String, BiGramObj> g = unigramObj.getHashMap();
				for (Entry<String, BiGramObj> entry2 : g.entrySet()) {

					String bigramKey = entry2.getKey();
					BiGramObj u = entry2.getValue();
					int bigramCount = u.getBigramCount();

					double bigramProb = (double) bigramCount / unigramCount;

					u.setBigramProb(bigramProb);

					g.put(bigramKey, u);

					if (bigramCount == 1) {
						numberOfWordsOccOnce++;
					}
					if (bigramCount == 2) {
						numberOfWordsOccTwice++;
					}
				}

				unigramObj.setHashMap(g);
				hashMap.put(key, unigramObj);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public void generateLM(String fileName) {

		File file = new File(fileName);
		FileWriter fileWriter = null;
		BufferedWriter writer = null;

		try {
			fileWriter = new FileWriter(fileName);
			writer = new BufferedWriter(fileWriter);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			writer.write("unigrams:\n");
			for (Entry<String, UniGramObj> entry : hashMap.entrySet()) {

				String wordListing = "";
				String unigramKey = entry.getKey();
				UniGramObj unigramObj = entry.getValue();

				int count = unigramObj.getUnigramCount();
				double prob = Math.log10(unigramObj.getUnigramProb())
						/ Math.log10(2);

				HashMap<String, BiGramObj> bigramHashMap = unigramObj
						.getHashMap();

				double summationBigramML = 0;
				double summationGT = 0;
				double summationUnigramML = 0;

				for (Entry<String, BiGramObj> entry2 : bigramHashMap.entrySet()) {

					String bigramKey = entry2.getKey();
					BiGramObj bigramObj = entry2.getValue();

					if (bigramObj.getBigramCount() == 1) {

						double discountedProbablity = (double) ((2 * (double) numberOfWordsOccTwice / numberOfWordsOccOnce) / (double) count);

						summationGT = summationGT + discountedProbablity;

					} else if (bigramObj.getBigramCount() > 1) {

						summationBigramML = summationBigramML
								+ bigramObj.getBigramProb();
					}

					if (hashMap.containsKey(bigramKey)) {

						summationUnigramML = summationUnigramML
								+ hashMap.get(bigramKey).getUnigramProb();
					}
				}

				if (summationBigramML == 1) {
					summationBigramML = summationBigramML * 0.99;
				}

				// System.out.println("unigramKey : " + unigramKey);
				// System.out.println("Count : " + count);
				// System.out.println("Count 2 : " + countBigramKeys);
				// System.out.println("summationBigramML : " +
				// summationBigramML);
				// System.out.println("summationGT : " + summationGT);
				// System.out.println("summationUnigramML :" +
				// summationUnigramML);
				// System.out.println();

				double alpha = (double) ((1 - summationBigramML - summationGT) / (double) (1 - summationUnigramML));

				alpha = Math.log10(alpha) / Math.log10(2);

				wordListing = wordListing + prob + " ";
				wordListing = wordListing + unigramKey + " ";
				wordListing = wordListing + alpha + "\n";

				writer.write(wordListing);
			}
			writer.write("\n");

			writer.write("bigrams:\n");
			for (Entry<String, UniGramObj> entry : hashMap.entrySet()) {

				String unigramKey = entry.getKey();
				UniGramObj unigramObj = entry.getValue();
				HashMap<String, BiGramObj> bigramHashMap = unigramObj
						.getHashMap();

				for (Entry<String, BiGramObj> entry2 : bigramHashMap.entrySet()) {

					String bigramKey = entry2.getKey();
					BiGramObj bigramObj = entry2.getValue();
					double prob = Math.log10(bigramObj.getBigramProb())
							/ Math.log10(2);

					String wordListing = "";
					String key = unigramKey + " " + bigramKey;
					wordListing = wordListing + prob + " ";
					wordListing = wordListing + key + "\n";
					writer.write(wordListing);
				}
			}

			writer.close();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			if (fileWriter != null) {
				try {
					writer.close();
					fileWriter.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void printMap() {

		try {

			System.out.println("Total : " + totalNumberOfWords);
			System.out.println("N1 : " + numberOfWordsOccOnce);
			System.out.println("N2 : " + numberOfWordsOccTwice);
			System.out.println();

			for (Entry<String, UniGramObj> entry : hashMap.entrySet()) {

				String key = entry.getKey();
				UniGramObj unigramObj = entry.getValue();
				double prob = unigramObj.getUnigramProb();

				//System.out.println(prob + " " + key);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void printBigram(String unigramKey) {

		try {

			UniGramObj f = hashMap.get(unigramKey);
			System.out.println("Key : " + unigramKey);
			System.out.println("Count : " + f.getUnigramCount());
			System.out.println("Prob : " + f.getUnigramProb());

			HashMap<String, BiGramObj> t = f.getHashMap();

			for (Entry<String, BiGramObj> entry : t.entrySet()) {

				String key = entry.getKey();
				BiGramObj bigramObj = entry.getValue();
				int count = bigramObj.getBigramCount();
				double prob = bigramObj.getBigramProb();

				System.out.println(">> " + key + " " + count + " " + prob);
			}
			System.out.println();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
