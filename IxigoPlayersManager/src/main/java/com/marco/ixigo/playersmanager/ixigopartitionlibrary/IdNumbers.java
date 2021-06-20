package com.marco.ixigo.playersmanager.ixigopartitionlibrary;

/**
 * Effective Partition Problem solution
 * @author Fedor Naumenko
 */
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a specialized container of identified numbers
 * 
 * @author Fedor Naumenko
 */
public final class IdNumbers extends ArrayList<IdNumber> {
	private static final long serialVersionUID = 1L;
	/**
	 * minimum tolerance: part of minimal numb value defines the start accuracy with
	 * which the free space of the average sum is measured. It is a heuristic value
	 * provides satisfactory inaccuracy in a single pass in most of cases.
	 */
	private static final int MIN_TOL = 20;

	/**
	 * Creates an empty instance
	 * 
	 * @param capacity collection capacity
	 */
	public IdNumbers(int capacity) {
		super(capacity);
	}

	/**
	 * Copy constructor
	 * 
	 * @param numbers numbers to be copied
	 */
	public IdNumbers(IdNumbers numbers) {
		numbers.forEach(n -> add(new IdNumber(n)));
	}

	/** Copies bin's indexes */
	void copyIndexes(IdNumbers numbers) {
		for (int i = 0; i < size(); i++) {
			get(i).setBinIInd(numbers.get(i).getBinIInd());
		}
	}

	/**
	 * Gets minimum accuracy with which the available space of the average sum
	 * should be measured
	 */
	Double getMinUp() {
		return Math.max(get(size() - 1).getVal() / MIN_TOL, 1);
	}

	/**
	 * Calculates average values sum
	 * 
	 * @param ssCnt count of subsets
	 * @return average values sum
	 */
	Double getAvrSum(int ssCnt) {
		Double sum = Double.valueOf(0);
		for (IdNumber n : this) {
			sum += n.getVal();
		}
		return sum / ssCnt;
	}

	/** Marks all numbers as unallocated */
	void reset() {
		this.forEach(n -> n.setBinIInd(0));
	}

	public void sortByDescent() {
		Collections.sort(this);
	}
}