package com.bigramlm;

public class BigramTrain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			// Check for number of arguments.
			if (args.length != 4) {
				System.err.println("You must call BigramTrain as "
						+ "follows:\n\njava BigramTrain "
						+ "-text <Training_File_Name> -lm <LM_File_Name> \n");
				System.exit(1);
			}

			// Read in the file names.
			String trainingFileName = args[1]; // "hw2_train.txt";
			String lmFileName = args[3]; // "lm.txt";

			String trainingFile = System.getProperty("user.dir") + "\\"
					+ trainingFileName;
			String lmFile = System.getProperty("user.dir") + "\\" + lmFileName;

			long start = System.currentTimeMillis();

			LMProcessor processor = new LMProcessor();

			processor.generateUniGramCounts(trainingFile);
			processor.generateUniGramProbablities();

			processor.generateBiGramCounts(trainingFile);
			processor.generateBigramProbabilities();

			processor.generateLM(lmFile);

			processor.printMap();
			// processor.printBigram("a");

			long elapsedTime = System.currentTimeMillis() - start;
			System.out.println("Language Model Generated in " + elapsedTime
					+ " milli-seconds at : " + lmFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
