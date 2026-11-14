package com.ixigo.library.partitioning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents subsets and their inaccuracy
 * 
 * @author Fedor Naumenko
 */
public class Result {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Result.class);
	/** maximum difference between the subset sums (inaccuracy) */
	private Double sumDiff;
	private Double penaltyWeitgh;
	/** subsets */
	private List<Subset> bins;

	/**
	 * Constructs Result and reserves subsets capacity
	 * 
	 * @param nCnt  count of numbers
	 * @param ssCnt count of subsets
	 */
	Result(int nCnt, int ssCnt, double penaltyWeitgh) {
		this.penaltyWeitgh = penaltyWeitgh;
		this.sumDiff = IxigoEfficientPartition.LARGEST_SUM;
		this.bins = new ArrayList<>(ssCnt);
		for (int i = 0; i < ssCnt; i++) {
			bins.add(new Subset(nCnt / ssCnt + 2));
		}
	}

	/** Gets the count of subsets */
	int getSubsetCount() {
		return bins.size();
	}

	/**
	 * Sorts subsets by their sum
	 * 
	 * @param ascend if true then in in ascending order, otherwise in descending
	 *               order
	 */
	void sort(boolean ascend) {
		if (ascend) {
			Collections.reverse(bins);
		} else {
			Collections.sort(bins);
		}
	}

	/** Sorts subsets in descending order and sets subsets ID starting with 1 */
	void setIDs() {
		if (!IxigoEfficientPartition.TEST) {
			sort(false); // sorts subsets in descending order
		}
		// set subset's ID
		int i = 1; // first subset ID
		for (Subset ss : bins) {
			ss.setId(i++);
		}
	}

	/**
	 * Clears instance: removes all subsets number IDs and resets the sum of the
	 * number values
	 */
	void clear() {
		this.sumDiff = IxigoEfficientPartition.LARGEST_SUM;
		bins.forEach(Subset::clear);
	}

	/**
	 * Outfits this result
	 * 
	 * @param numbs numbers to be distributed
	 * @param ind   index of the first subset: 1 for SGreedy() or 0 for DSTree()
	 * @param diff  difference between the subset sums
	 */
	void fill(IdNumbers numbs, int ind, Double diff) {
		int ssCnt = getSubsetCount() - ind; // count of subsets minus first index
		clear();
		numbs.forEach(n -> bins.get(ssCnt - n.getBinIInd()).addNumb(n));
		this.sumDiff = diff;
	}

	/**
	 * Sets the minimum/maximum subset sums and their difference for this instance
	 * 
	 * @return Range instance with this instance actual min amd max sums
	 */
	Range setSumDiff() {
		// for(int i=1; i<Bins.Count; i++)
		// if (Bins[i].sumVal > maxSum) maxSum = Bins[i].sumVal;
		// else if (Bins[i].sumVal < minSum) minSum = Bins[i].sumVal;
		Range range = new Range(bins.get(0).getSumVal());
		bins.forEach(ss -> {
			if (ss.getSumVal() > range.getMaxSum()) {
				range.setMaxSum(ss.getSumVal());
			} else if (ss.getSumVal() < range.getMinSum()) {
				range.setMinSum(ss.getSumVal());
			}
		});
		// add a penalty for team sizes not matching
		int size0 = this.getSubsets().get(0).getSize();
		int size1 = this.getSubsets().get(1).getSize();
		double penalty = 0;
		if (Math.abs(size1 - size0) == 1) {// if only one difference, only add penalty if the larger team has the
												// advantage
			if (size0 - size1 > 0 && this.getSubsets().get(0).getSumVal() > this.getSubsets().get(1).getSumVal()
					|| size1 - size0 > 0
							&& this.getSubsets().get(1).getSumVal() > this.getSubsets().get(0).getSumVal()) {
				penalty = penaltyWeitgh;
			}
		} else if (Math.abs(size0 - size1) > 1) {// if more than one different add a penalty for each player different
			if (size0 - size1 > 0 && this.getSubsets().get(0).getSumVal() > this.getSubsets().get(1).getSumVal()
					|| size1 - size0 > 0
							&& this.getSubsets().get(1).getSumVal() > this.getSubsets().get(0).getSumVal()) {
				penalty = Math.abs(size0 - size1) * (penaltyWeitgh+.5);
			}else {
				penalty = Math.abs(size0 - size1) * penaltyWeitgh;
			}
			
		}
		Double newSumDiff = range.getMaxSum() - range.getMinSum() + penalty;

		if (_LOGGER.isDebugEnabled()) {
			_LOGGER.debug(String.format("%f (%f) - %dv%d", range.getMaxSum() - range.getMinSum() + penalty, penalty, size0, size1));
		}
		if (newSumDiff < sumDiff) {
			sumDiff = newSumDiff;
			range.setUpdated(true);
		} else {
			range.setUpdated(false);
		}
		return range;
	}

	/**
	 * Prints an instance
	 * 
	 * @param prNumbCnt maximum count of printed number IDs or 0 if all
	 */
	void print(int prNumbCnt) {
		if (bins.isEmpty())
			return;
		byte width = digitsCnt(bins.get(0).getSumVal()); // first sum is the biggest
		bins.forEach(ss -> ss.print(width, prNumbCnt));
	}

	public List<Subset> getSubsets() {
		return bins;
	}

	/**
	 * Gets count of digist in a value
	 * 
	 * @param val value
	 * @return count of digist
	 */
	static byte digitsCnt(Double val) {
		byte res = 0;
		for (; val > 0; val /= 10, res++)
			;
		return res;
	}

	public Double getSumDiff() {
		return sumDiff;
	}

	public void setSumDiff(Double sumDiff) {
		this.sumDiff = sumDiff;
	}

	public void setBins(List<Subset> bins) {
		this.bins = bins;
	}

}