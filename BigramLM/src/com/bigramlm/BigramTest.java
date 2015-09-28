package com.bigramlm;

import java.util.HashMap;

public class BigramTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			// Check for number of arguments.
			if (args.length != 4) {
				System.err.println("You must call BigramTest as "
						+ "follows:\n\njava BigramTest "
						+ "-text <Testing_File_Name> -lm <LM_File_Name> \n");
				System.exit(1);
			}

			// Read in the file names.
			String testFile = args[1]; // "hw2_test.txt";
			String lmFile = args[3]; // "lm.txt";

			String testFileName = System.getProperty("user.dir") + "\\"
					+ testFile;
			String lmFileName = System.getProperty("user.dir") + "\\" + lmFile;

			// long start = System.currentTimeMillis();
			PPLProcessor processor = new PPLProcessor();
			HashMap<String, Integer> wordMap = processor
					.readTestFile(testFileName);

			processor.readLMFileForUniGramProbs(lmFileName);
			processor.readLMFileForBiGramProbs(lmFileName);

			// processor.getUnigramDeatils("10-8");
			// processor.getBigramDeatils("drive israeli");
			// processor.printMaps();

			double perplexity = processor.computePerplexity(wordMap);

			// long elapsedTime = System.currentTimeMillis() - start;
			// System.out.println("Language Model Generated in " + elapsedTime
			// + " milli-seconds");

			System.out.println("Perplexity : " + perplexity);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
