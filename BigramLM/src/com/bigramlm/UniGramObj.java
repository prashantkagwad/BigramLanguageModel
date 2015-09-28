package com.bigramlm;

import java.util.HashMap;

public class UniGramObj {

	private int unigramCount;
	private double unigramProb;
	private HashMap<String, BiGramObj> hashMap = new HashMap<String, BiGramObj>();

	public UniGramObj() {
		// TODO Auto-generated constructor stub
	}

	public int getUnigramCount() {
		return unigramCount;
	}

	public void setUnigramCount(int unigramCount) {
		this.unigramCount = unigramCount;
	}

	public double getUnigramProb() {
		return unigramProb;
	}

	public void setUnigramProb(double unigramProb) {
		this.unigramProb = unigramProb;
	}

	public HashMap<String, BiGramObj> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, BiGramObj> hashMap) {
		this.hashMap = hashMap;
	}

}
