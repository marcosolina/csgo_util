package com.marco.csgoutil.roundparser.partition;

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
	final int minTol = 20;

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
	IdNumbers(IdNumbers numbers) {
		numbers.forEach((n) -> {
			add(new IdNumber(n));
		});
	}

	/** Copies bin's indexes */
	void CopyIndexes(IdNumbers numbers) {
		for (int i = 0; i < size(); i++)
			get(i).BinIInd = numbers.get(i).BinIInd;
	}

	/**
	 * Gets minimum accuracy with which the available space of the average sum
	 * should be measured
	 */
	Double GetMinUp() {
		return Math.max((Double) get(size() - 1).Val / minTol, 1);
	}

	/**
	 * Calculates average values sum
	 * 
	 * @param ssCnt count of subsets
	 * @return average values sum
	 */
	float AvrSum(int ssCnt) {
		float sum = 0;
		for (IdNumber n : this)
			sum += n.Val;
		return sum / ssCnt;
	}

	/** Marks all numbers as unallocated */
	void Reset() {
		this.forEach((n) -> {
			n.BinIInd = 0;
		});
	}

	public void SortByDescent() {
		Collections.sort(this);
	}
}