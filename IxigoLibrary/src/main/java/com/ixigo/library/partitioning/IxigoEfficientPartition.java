package com.ixigo.library.partitioning;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A customised version of <a href=
 * "https://www.codeproject.com/Articles/1265125/Fast-and-Practically-perfect-Partition-Problem-Sol#DST">this
 * solution</a>
 * 
 * Creates items partition among subsets, possibly according equally item values
 * 
 * @author Fedor Naumenko
 */
public final class IxigoEfficientPartition {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoEfficientPartition.class);
	static final Double LARGEST_SUM = Double.MAX_VALUE;
	/** if set on TRUE then then print test messages */
	public static final boolean TEST = false;

	/** largest possible sum */
	/** final partition */
	private Result result;
	/** average sum value among subsets */
	private Double avr;

	/**
	 * Constructs numbers partition by numbers, with sums sorted in descending order
	 * 
	 * @param vals    numbers to be distributed; their ID are assigned according to
	 *                their ordinal numbers in array
	 * @param ssCnt   count of subsets; if 0 then creates an empty partition with
	 *                undefined (maximum type's value) inaccuracy
	 * @param limMult DSTree method call's limit multiplier - it increases the limit
	 *                of 1 million recursive invokes by limMult times; if 0 then
	 *                omit DSTree method invoking (fast, but not 'perfect')
	 */
	private IxigoEfficientPartition(List<Double> vals, int ssCnt, int limMult, double penaltyWeitgh) {
		int i = 1;
		IdNumbers numbs = new IdNumbers(ssCnt);

		for (Double val : vals) {
			numbs.add(new IdNumber(i++, val));
		}
		this.result = new Result(numbs.size(), ssCnt, penaltyWeitgh);
		for(Subset s: result.getSubsets()) {
			s.setSize(s.getNumbIDs().size());
		}
		if (ssCnt == 0) {
			return;
		}
		this.avr = numbs.getAvrSum(ssCnt);
		new Partition(numbs, this.result, this.avr, limMult, penaltyWeitgh);
	}

	/**
	 * Constructs two partitions, with sums sorted in descending order
	 * 
	 * @param vals  numbers to be distributed; their ID are assigned according to
	 *              their ordinal numbers in array
	 */
	public IxigoEfficientPartition(List<Double> vals, double penaltyWeitgh) {
		this(vals, 2, 1, penaltyWeitgh);
	}

	/**
	 * Gets the count of subsets
	 * 
	 * @return count of subsets
	 */
	public int getSubsetCount() {
		return this.result.getSubsetCount();
	}

	/**
	 * Gets a reference to the subsets container
	 * 
	 * @return reference to the subsets container
	 */
	public List<Subset> getSubsets() {
		return this.result.getSubsets();
	}

	/**
	 * Gets average summary value among subsets
	 * 
	 * @return average summary value among subsets
	 */
	public Double getAvrSum() {
		return this.avr;
	}

	/**
	 * Gets inaccuracy: the difference between maximum and minimum summary value
	 * among subsets
	 * 
	 * @return inaccuracy
	 */
	public Double getInacc() {
		return this.result.getSumDiff();
	}

	/**
	 * Gets relative inaccuracy: the inaccuracy in percentage to average summary
	 * 
	 * @return relative inaccuracy
	 */
	public Double getRelInacc() {
		return 100F * getInacc() / this.avr;
	}

	/**
	 * Sorts subsets by their sum
	 * 
	 * @param ascend if true then in in ascending order, otherwise in descending
	 *               order
	 */
	public void sort(boolean ascend) {
		if (getInacc() > 0) {
			this.result.sort(ascend);
		}
	}

	/**
	 * Outputs subsets to console
	 * 
	 * @param prSumDiff if true then prints sum diference (in absolute and relative)
	 * @param prNumbCnt maximum number of printed number IDs or 0 if ptint all
	 */
	public void print(boolean prSumDiff, int prNumbCnt) {
		if (prSumDiff) {
			_LOGGER.debug(String.format("inaccuracy: %f (%.2f%%)", getInacc(), getRelInacc()));
		}
		this.result.print(prNumbCnt);
	}
}
