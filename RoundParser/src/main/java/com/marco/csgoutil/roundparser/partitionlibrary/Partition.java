package com.marco.csgoutil.roundparser.partitionlibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Effective Partition Problem solution
 * @author Fedor Naumenko
 */

/**
 * Encapsulates partition methods
 * 
 * @author Fedor Naumenko
 */
final class Partition {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Partition.class);

	/** the minimum DSTree() invokes limit */
	private static final int MIN_CALL_LIMIT = 1000000;
	/** flag of DSTree() completion by limit */
	private static final byte LIM_FLAG = 0x1;
	/** flag of DSTree() completion by 'perfect' result */
	private static final byte PERF_FLAG = 0x2;
	/** Partition method delegates */
	private IDoPartition[] methods = new IDoPartition[4];
	/** Partition method titles to print while testing */
	private static final String[] METHOD_TITLES = { "UGreedy", "Greedy", "SGreedy", "DSTree" };

	/** current DSTree() invokes limit */
	private long callLimit;
	/** ounter of DSTree() invokes */
	private int callCnt;
	/** input numbers */
	private IdNumbers numbs;
	/** numbers with the best unsaved maximum sum difference: used in DSTree() */
	private IdNumbers standbyNumbs;
	/** current result */
	private Result currResult;
	/** final result */
	private Result finalResult;
	/** current minimum sum among subsets */
	private Double minSum;
	/** current maximum sum among subsets */
	private Double maxSum;
	/** current maximum difference between the subset sums (inaccuracy) */
	private Double sumDiff;
	/** // last best inaccuracy: used in DSTree() */
	private Double lastSumDiff;
	/** unsaved maximum difference between the subset sums: used in DSTree() */
	private Double standbySumDiff;
	/** average sum value among subsets */
	private double avrSum;
	/** holder of DSTree() completion flags (signs) */
	private byte complete;
	/** true if avrSum is not an integer (has a nonzero fractional part) */
	private boolean isAvrFract;
	// TEST fields
	/** counter of tree bottom points: used in DSTree() */
	private int bottomCnt;
	/** counter of 'up' iterations: used in DSTree() */
	private int upCnt;

	/**
	 * Creates an instance and performs partition
	 * 
	 * @param nmbs    identified values to be distributed
	 * @param result  final result
	 * @param avr     average value sum
	 * @param limMult DSTree method call's limit multiplier; if 0 then omit DSTree
	 *                method invoking
	 */
	public Partition(IdNumbers nmbs, Result result, Double avr, int limMult, double penaltyWeitgh) {
		this.numbs = nmbs;
		this.finalResult = result;
		this.avrSum = avr;
		this.isAvrFract = (int) avrSum != avrSum;
		this.callLimit = MIN_CALL_LIMIT * limMult;
		this.numbs.sortByDescent();
		this.sumDiff = PartitionTwoTeams.LARGEST_SUM;
		int ssCnt = finalResult.getSubsetCount();
		int i = 0;
		this.methods[i++] = Integer -> doUGreedy(ssCnt);
		this.methods[i++] = Integer -> wrapGreedy(ssCnt, penaltyWeitgh);
		this.methods[i++] = Integer -> doSGreedy(ssCnt);
		this.methods[i++] = Integer -> doISTree(ssCnt);

		if (PartitionTwoTeams.TEST) {
			StringBuilder sb = new StringBuilder();
			sb.append("avr: ");
			sb.append(String.format(avrSum % 1.0 != 0 ? "%f" : "%s", avrSum));
			sb.append(String.format("\t%d...%d", numbs.get(numbs.size() - 1).getVal(), numbs.get(0).getVal()));
			_LOGGER.debug(sb.toString());
		}

		int cnt = limMult > 0 ? 4 : 3;
		if (!PartitionTwoTeams.TEST) {
			// for the degenerate case numbs.size()<=ssCnt method UGreedy() is comepletely
			// enough
			if (numbs.size() <= ssCnt) {
				cnt = 1;
			}
		}
		for (i = 0; i < cnt; i++) {
			if (doPartition(i, ssCnt)) {
				return;
			}
		}
		return;
	}

	/**
	 * Raises completion flag
	 * 
	 * @param flag completion flag to be raised
	 */
	void setRaiseComplFlag(byte flag) {
		complete |= flag;
	}

	/** Gets true if 'perfect' completion flag is raised */
	boolean isCompleteByPerfect() {
		return (complete & PERF_FLAG) != 0;
	}
	
	boolean isCompleteByLimit() {
		return (complete & LIM_FLAG) != 0;
	}

	/** Gets true if current result is the best possible */
	boolean isResultPerfect() {
		return sumDiff == 0 || (isAvrFract && sumDiff == 1);
	}

	/**
	 * Sets the best minimum/maximum subset sum and their difference
	 * 
	 * @param res current result that delivers the sums as candidates for the bes
	 * @return true if the more narrow range is achieved
	 */
	boolean setRange(Result res) {
		Range range = res.setSumDiff();
		if (range.isUpdated() && res.getSumDiff() < sumDiff) {
			sumDiff = res.getSumDiff();
			minSum = range.getMinSum();
			maxSum = range.getMaxSum();
			return true;
		}
		return false;
	}

	/**
	 * Performs 'Unconditional Greedy' partition
	 * 
	 * @param ssCnt count of subsets
	 */
	void doUGreedy(int ssCnt) {
		int i = 0, shift = 1;

		for (IdNumber n : numbs) {
			finalResult.getSubsets().get(i).addNumb(n);
			i += shift;
			if (i / ssCnt > 0) {
				i--;
				shift = -1;
			} // last subset, flip to reverse round order
			else if (i < 0) {
				i++;
				shift = 1;
			} // first subset, flip to direct round order
		}
		setRange(finalResult); // set minSum, maxSum, sumDiff
		finalResult.setIDs(); // set subsets ID
	}

	/**
	 * Performs 'Greedy' partition
	 * 
	 * @param ssCnt count of subsets
	 */
	void doGreedy(int ssCnt) {
		int i, k; // subsets cyclic index, index of subset with minimum sum

		for (IdNumber n : numbs) {
			if (n.getBinIInd() != 0) {
				continue; // needs for SGreedy, for Greedy itself is redundant
			}
			minSum = currResult.getSubsets().get(k = 0).getSumVal();
			for (i = 1; i < ssCnt; i++) { // loop through the bins from the second one
				if (currResult.getSubsets().get(i).getSumVal() <= minSum) {
					minSum = currResult.getSubsets().get(k = i).getSumVal();
				}
			}
			Subset subset = currResult.getSubsets().get(k);
			subset.setSumVal(subset.getSumVal() + n.getVal());
			subset.setSize(subset.getSize()+1);
			n.setBinIInd(ssCnt - k);
		}

		if (setRange(currResult)) { // is the result better than after previous UGreedy() call?
			finalResult.fill(numbs, 0, currResult.getSumDiff()); // outfit final result
			finalResult.setIDs();
		}
	}

	/**
	 * Wrapper to 'Greedy' partition
	 * 
	 * @param ssCnt count of subsets
	 */
	void wrapGreedy(int ssCnt, double penaltyWeitgh) {
		numbs.reset();
		currResult = new Result(numbs.size(), ssCnt, penaltyWeitgh);
		doGreedy(ssCnt);
	}

	/**
	 * Performs 'Sequential stuffing Greedy' partition
	 * 
	 * @param ssCnt count of subsets
	 */
	void doSGreedy(int ssCnt) {
		int freeCnt; // number of unallocated numbs
		int i, k = 1; // bin index, delta multiplicator
		Double avrUp; // raised average sum
		Double up = numbs.getMinUp(); // delta above average sum

		// loop through the numbs until number of unallocated numbs becomes less then
		// half number of bins
		do {
			freeCnt = numbs.size();
			numbs.reset();
			currResult.clear();
			avrUp = avrSum + up * k++;

			for (i = 0; i < currResult.getSubsets().size(); i++) {
				for (IdNumber n : numbs) {
					if (n.isFitted(avrUp - currResult.getSubsets().get(i).getSumVal())) {
						Subset subset = currResult.getSubsets().get(i);
						subset.setSumVal(subset.getSumVal() + n.getVal());
						subset.setSize(subset.getSize()+1);
						n.setBinIInd(ssCnt - i);
						freeCnt--;
					}
				}
			}
		}
		// this heuristic contition provided satisfactory inaccuracy in a single pass in
		// most of cases
		while (freeCnt >= ssCnt / 2);

		// distribute remaining unallocated numbs by Greed protocol
		// Checking for freeCnt==0 can be omitted, since as it happens very rarely
		doGreedy(ssCnt);
		if (PartitionTwoTeams.TEST) {
			callCnt = k - 1;
		}
	}

	/**
	 * Performs 'Dynamic Search Tree' ('perfect') partition
	 * 
	 * @param currItInd numb's index from which the cycle continues
	 * @param invInd    current inverse index of subset
	 */
	void doDSTree(int currItInd, int invInd) {
		if (complete != 0) {
			return;
		}
		if (++callCnt == callLimit) {
			setRaiseComplFlag(LIM_FLAG);
		}
		Subset ss = currResult.getSubsets().get(currResult.getSubsets().size() - 1 - invInd); // curent bin

		if (invInd != 0) { // not last bin
			IdNumber n;
			for (int i = currItInd; i < numbs.size(); i++) {
				n = numbs.get(i);
				if (n.getBinIInd() == 0 && n.getVal() + ss.getSumVal() < maxSum) {
					ss.setSumVal(ss.getSumVal() + n.getVal()); // take number's value into account
					ss.setSize(ss.getSize() + 1);
					n.setBinIInd(invInd); // take number's bin index
					if (i + 1 < numbs.size()) { // checkup just to avoid blank recursive invoke
						doDSTree(i + 1, invInd); // try to fit next numb to the same bin
					}
					if (ss.getSumVal() > minSum) { // bin is full
						doDSTree(0, invInd - 1); // try to fit unallocated numbs to the next bin
					}
					ss.setSumVal(ss.getSumVal() - n.getVal()); // discharge number's value
					ss.setSize(ss.getSize() - 1);
					n.setBinIInd(0); // discharge number's bin index
				}
			}
		} else { // last bin
			if (PartitionTwoTeams.TEST) {
				bottomCnt++;
			}
			// accumulate sum for the last bin
			for (IdNumber n : numbs) {
				if (n.getBinIInd() == 0) { // zero invIndex means that number belongs to the last bin
					ss.setSumVal(ss.getSumVal() + n.getVal());
					ss.setSize(ss.getSize() + 1);
				}
			}
			if (setRange(currResult)) { // is inaccuracy better than the previous one?
				standbyNumbs.copyIndexes(numbs); // keep current numbers as the standby one
				lastSumDiff = standbySumDiff = sumDiff; // for the next standby result selection
				if (isResultPerfect()) {
					setRaiseComplFlag(PERF_FLAG);
				}
			} else if (currResult.getSumDiff() < standbySumDiff) { // should we keep current result as standby?
				standbyNumbs.copyIndexes(numbs); // keep current numbers as the standby one
				standbySumDiff = currResult.getSumDiff();
			}
			ss.setSumVal(Double.valueOf(0)); // clear last bin sum
			ss.setSize(0);
		}
	}

	/**
	 * Performs iterative 'Dynamic Search Tree' partition.
	 * 
	 * @param ssCnt count of subsets
	 */
	void doISTree(int ssCnt) {
		// initial range expansion around average
		double up = avrSum < numbs.get(0).getVal() ? (numbs.get(0).getVal() - avrSum + 2) : 1;
		if (up > avrSum) {
			up = avrSum - 1;
		}
		standbySumDiff = PartitionTwoTeams.LARGEST_SUM; // undefined standby inaccuracy
		lastSumDiff = finalResult.getSumDiff();
		standbyNumbs = new IdNumbers(numbs);
		if (PartitionTwoTeams.TEST) {
			bottomCnt = upCnt = 0;
		}
		do {
			minSum = (avrSum - up);
			maxSum = (avrSum + up);
			sumDiff = maxSum - minSum;
			complete = 0;
			numbs.reset();
			currResult.clear();
			if (PartitionTwoTeams.TEST) {
				upCnt++;
			}
			doDSTree(0, ssCnt - 1);

			if (isCompleteByPerfect() || isCompleteByLimit() || (up *= 2) >= minSum // increase and checkup range expansion
					|| currResult.getSumDiff() > standbySumDiff) { // is current result worse than standby one?
				break;
			}
		} while (lastSumDiff != currResult.getSumDiff()); // until previous and current inaccuracy are different
		// use last fitted result
		if (PartitionTwoTeams.TEST || currResult.getSumDiff() < finalResult.getSumDiff()) {
			setRange(finalResult);
			finalResult.fill(standbyNumbs, 1, standbySumDiff);
		}
	}

	/**
	 * Performes partitioning
	 * 
	 * @param i     index of partition method to call
	 * @param ssCnt count od subsets
	 * @return true if result is 'perfect'
	 */
	boolean doPartition(int i, int ssCnt) {
		long startTime;
		if (PartitionTwoTeams.TEST) {
			_LOGGER.debug(String.format("%s\t", METHOD_TITLES[i]));
			sumDiff = PartitionTwoTeams.LARGEST_SUM;
			startTime = System.nanoTime();
		}
		methods[i].doPart(ssCnt);
		if (PartitionTwoTeams.TEST) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%.3f ms\t", (float) (System.nanoTime() - startTime) / (1000 * 1000)));
			sb.append(
					String.format("%d\t(%.2f)%%", finalResult.getSumDiff(), 100F * finalResult.getSumDiff() / avrSum));

			if (i == 2) { // SGreedy

				if (callCnt > 1) {
					sb.append(String.format("\titers: %d", callCnt));
				}
			} else if (i == 3) { // DSTree
				sb.append(String.format("  calls|bottoms|ups: %d %d %d", callCnt, bottomCnt, upCnt));
			}
			_LOGGER.debug(sb.toString());
			return false;
		} else
			return isResultPerfect();
	}

}