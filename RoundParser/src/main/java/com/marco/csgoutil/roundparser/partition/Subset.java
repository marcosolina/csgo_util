package com.marco.csgoutil.roundparser.partition;

/**
 * Effective Partition Problem solution
 * @author Fedor Naumenko
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Represents partition data
 * 
 * @author Fedor Naumenko
 */
public final class Subset implements Comparable<Subset> {
	/** subset's ID */
	int id;
	/** sum of subset numbers */
	Double sumVal;
	/** number IDs container */
	ArrayList<Integer> numbIDs;

	/**
	 * Creates an empty Subset with reserved capacity
	 * 
	 * @param nCnt expected count of numbers
	 */
	Subset(int nCnt) {
		id = 0;
		sumVal = Double.valueOf(0);
		numbIDs = new ArrayList<Integer>(nCnt);
	}

	/**
	 * Clears instance: removes number IDs, sets the sum of number values to zero
	 */
	void clear() {
		sumVal = Double.valueOf(0);
		numbIDs.clear();
	}

	/**
	 * Adds number
	 * 
	 * @param n number to add
	 */
	void addNumb(IdNumber n) {
		sumVal += n.Val;
		numbIDs.add(n.Id);
	}

	/**
	 * Compare in descending order
	 * 
	 * @param ss comparison instance
	 * @return 1 if less than ss, -1 of more than ss, 0 if equal
	 */
	@Override
	public int compareTo(Subset ss) {
		if (sumVal < ss.sumVal)
			return 1;
		if (sumVal > ss.sumVal)
			return -1;
		return 0;
	}

	/**
	 * Prints subset
	 * 
	 * @param sumWidth  maximum count of digits in subsets sum value to align sums
	 *                  left (doesn't used)
	 * @param prNumbCnt maximum count of printed number IDs or 0 if all
	 */
	void print(byte sumWidth, int prNumbCnt) {
		System.out.printf("set %d\tsum %f\tnumbIDs:", id, sumVal);
		if (prNumbCnt > 0 && prNumbCnt < numbIDs.size()) {
			for (int i = 0; i < prNumbCnt; i++) {
				System.out.printf(" %d", numbIDs.get(i));
			}
			System.out.printf("... (%d})", numbIDs.size());
		} else {
			for (int id : numbIDs) {
				System.out.printf(" %2d", id);
			}
		}
		System.out.println();
	}

	/**
	 * Gets subset's ID
	 * 
	 * @return subset's ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Gets sum of subset numbers
	 * 
	 * @return sum of subset numbers
	 */
	public Double sum() {
		return sumVal;
	}

	/**
	 * Gets number IDs container
	 * 
	 * @return number IDs container
	 */
	public List<Integer> getNumbIDs() {
		return numbIDs;
	}
}