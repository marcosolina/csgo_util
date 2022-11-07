package com.ixigo.library.partitioning;

/**
 * Effective Partition Problem solution
 * @author Fedor Naumenko
 */
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents partition data
 * 
 * @author Fedor Naumenko
 */
public final class Subset implements Comparable<Subset> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Subset.class);
	
	/** subset's ID */
	private int id;
	/** sum of subset numbers */
	private Double sumVal;
	/** number IDs container */
	private List<Integer> numbIDs;

	/**
	 * Creates an empty Subset with reserved capacity
	 * 
	 * @param nCnt expected count of numbers
	 */
	Subset(int nCnt) {
		id = 0;
		sumVal = Double.valueOf(0);
		numbIDs = new ArrayList<>(nCnt);
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
		sumVal += n.getVal();
		numbIDs.add(n.getId());
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
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("set %d\tsum %f\tnumbIDs:", id, sumVal));
		
		if (prNumbCnt > 0 && prNumbCnt < numbIDs.size()) {
			for (int i = 0; i < prNumbCnt; i++) {
				sb.append(String.format(" %d", numbIDs.get(i)));
			}
			sb.append(String.format("... (%d})", numbIDs.size()));
		} else {
			for (int id : numbIDs) {
				sb.append(String.format(" %2d", id));
			}
		}
		
		_LOGGER.debug(sb.toString());
	}
	
	/**
	 * Gets number IDs container
	 * 
	 * @return number IDs container
	 */
	public List<Integer> getNumbIDs() {
		return numbIDs;
	}

	/**
	 * Gets subset's ID
	 * 
	 * @return subset's ID
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets sum of subset numbers
	 * 
	 * @return sum of subset numbers
	 */
	public Double getSumVal() {
		return sumVal;
	}

	public void setSumVal(Double sumVal) {
		this.sumVal = sumVal;
	}

}