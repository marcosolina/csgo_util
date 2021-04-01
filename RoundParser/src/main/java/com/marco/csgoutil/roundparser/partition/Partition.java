package com.marco.csgoutil.roundparser.partition;

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
	/** the minimum DSTree() invokes limit */
	final static int MIN_CALL_LIMIT = 1000000;
	/** flag of DSTree() completion by limit */
	final static byte LIM_FLAG = 0x1;
	/** flag of DSTree() completion by 'perfect' result */
	final static byte PERF_FLAG = 0x2;

	/** current DSTree() invokes limit */
	long callLimit;
	/** ounter of DSTree() invokes */
	int callCnt;
	/** input numbers */
	IdNumbers numbs;
	/** numbers with the best unsaved maximum sum difference: used in DSTree() */
	IdNumbers standbyNumbs;
	/** current result */
	Result currResult;
	/** final result */
	Result finalResult;
	/** current minimum sum among subsets */
	Double minSum;
	/** current maximum sum among subsets */
	Double maxSum;
	/** current maximum difference between the subset sums (inaccuracy) */
	Double sumDiff;
	/** // last best inaccuracy: used in DSTree() */
	Double lastSumDiff;
	/** unsaved maximum difference between the subset sums: used in DSTree() */
	Double standbySumDiff;
	/** average sum value among subsets */
	double avrSum;
	/** holder of DSTree() completion flags (signs) */
	byte complete;
	/** true if avrSum is not an integer (has a nonzero fractional part) */
	boolean isAvrFract;
	// TEST fields
	/** counter of tree bottom points: used in DSTree() */
	int bottomCnt;
	/** counter of 'up' iterations: used in DSTree() */
	int upCnt;

	/**
	 * Raises completion flag
	 * 
	 * @param flag completion flag to be raised
	 */
	void RaiseComplFlag(byte flag) {
		complete |= flag;
	}

	/** Gets true if 'perfect' completion flag is raised */
	boolean IsCompleteByPerfect() {
		return (complete & PERF_FLAG) != 0;
	}

	/** Gets true if current result is the best possible */
	boolean IsResultPerfect() {
		return sumDiff == 0 || (isAvrFract && sumDiff == 1);
	}

	/**
	 * Sets the best minimum/maximum subset sum and their difference
	 * 
	 * @param res current result that delivers the sums as candidates for the bes
	 * @return true if the more narrow range is achieved
	 */
	boolean SetRange(Result res) {
		Range range = res.SetSumDiff();
		if (range.updated && res.SumDiff < sumDiff) {
			sumDiff = res.SumDiff;
			minSum = range.minSum;
			maxSum = range.maxSum;
			return true;
		}
		return false;
	}

	/**
	 * Performs 'Unconditional Greedy' partition
	 * 
	 * @param ssCnt count of subsets
	 */
	void UGreedy(int ssCnt) {
		int i = 0, shift = 1;

		for (IdNumber n : numbs) {
			finalResult.Bins.get(i).addNumb(n);
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
		SetRange(finalResult); // set minSum, maxSum, sumDiff
		finalResult.SetIDs(); // set subsets ID
	}

	/**
	 * Performs 'Greedy' partition
	 * 
	 * @param ssCnt count of subsets
	 */
	void Greedy(int ssCnt) {
		int i, k; // subsets cyclic index, index of subset with minimum sum

		for (IdNumber n : numbs) {
			if (n.BinIInd != 0)
				continue; // needs for SGreedy, for Greedy itself is redundant
			minSum = currResult.Bins.get(k = 0).sumVal;
			for (i = 1; i < ssCnt; i++) // loop through the bins from the second one
				if (currResult.Bins.get(i).sumVal <= minSum)
					minSum = currResult.Bins.get(k = i).sumVal;
			currResult.Bins.get(k).sumVal += n.Val;
			n.BinIInd = ssCnt - k;
		}

		if (SetRange(currResult)) { // is the result better than after previous UGreedy() call?
			finalResult.Fill(numbs, 0, currResult.SumDiff); // outfit final result
			finalResult.SetIDs();
		}
	}

	/**
	 * Wrapper to 'Greedy' partition
	 * 
	 * @param ssCnt count of subsets
	 */
	void WrapGreedy(int ssCnt) {
		numbs.Reset();
		currResult = new Result(numbs.size(), ssCnt);
		Greedy(ssCnt);
	}

	/**
	 * Performs 'Sequential stuffing Greedy' partition
	 * 
	 * @param ssCnt count of subsets
	 */
	void SGreedy(int ssCnt) {
		int freeCnt; // number of unallocated numbs
		int i, k = 1; // bin index, delta multiplicator
		Double avrUp; // raised average sum
		Double up = numbs.GetMinUp(); // delta above average sum

		// loop through the numbs until number of unallocated numbs becomes less then
		// half number of bins
		do {
			freeCnt = numbs.size();
			numbs.Reset();
			currResult.Clear();
			avrUp = avrSum + up * k++;

			for (i = 0; i < currResult.Bins.size(); i++)
				for (IdNumber n : numbs)
					if (n.IsFitted(avrUp - currResult.Bins.get(i).sumVal)) {
						currResult.Bins.get(i).sumVal += n.Val;
						n.BinIInd = ssCnt - i;
						freeCnt--;
					}
		}
		// this heuristic contition provided satisfactory inaccuracy in a single pass in
		// most of cases
		while (freeCnt >= ssCnt / 2);

		// distribute remaining unallocated numbs by Greed protocol
		// Checking for freeCnt==0 can be omitted, since as it happens very rarely
		Greedy(ssCnt);
		if (EffPartition.TEST)
			callCnt = k - 1;
	}

	/**
	 * Performs 'Dynamic Search Tree' ('perfect') partition
	 * 
	 * @param currItInd numb's index from which the cycle continues
	 * @param invInd    current inverse index of subset
	 */
	void DSTree(int currItInd, int invInd) {
		if (complete != 0)
			return;
		if (++callCnt == callLimit)
			RaiseComplFlag(LIM_FLAG);
		Subset ss = currResult.Bins.get(currResult.Bins.size() - 1 - invInd); // curent bin

		if (invInd != 0) { // not last bin
			IdNumber n;
			for (int i = currItInd; i < numbs.size(); i++) {
				n = numbs.get(i);
				if (n.BinIInd == 0 && n.Val + ss.sumVal < maxSum) {
					ss.sumVal += n.Val; // take number's value into account
					n.BinIInd = invInd; // take number's bin index
					if (i + 1 < numbs.size()) // checkup just to avoid blank recursive invoke
						DSTree(i + 1, invInd); // try to fit next numb to the same bin
					if (ss.sumVal > minSum) // bin is full
						DSTree(0, invInd - 1); // try to fit unallocated numbs to the next bin
					ss.sumVal -= n.Val; // discharge number's value
					n.BinIInd = 0; // discharge number's bin index
				}
			}
		} else { // last bin
			if (EffPartition.TEST)
				bottomCnt++;
			// accumulate sum for the last bin
			for (IdNumber n : numbs)
				if (n.BinIInd == 0) // zero invIndex means that number belongs to the last bin
					ss.sumVal += n.Val;
			if (SetRange(currResult)) { // is inaccuracy better than the previous one?
				standbyNumbs.CopyIndexes(numbs); // keep current numbers as the standby one
				lastSumDiff = standbySumDiff = sumDiff; // for the next standby result selection
				if (IsResultPerfect())
					RaiseComplFlag(PERF_FLAG);
			} else if (currResult.SumDiff < standbySumDiff) { // should we keep current result as standby?
				standbyNumbs.CopyIndexes(numbs); // keep current numbers as the standby one
				standbySumDiff = currResult.SumDiff;
			}
			ss.sumVal = Double.valueOf(0); // clear last bin sum
		}
	}

	/**
	 * Performs iterative 'Dynamic Search Tree' partition.
	 * 
	 * @param ssCnt count of subsets
	 */
	void ISTree(int ssCnt) {
		// initial range expansion around average
		double up = avrSum < numbs.get(0).Val ? (numbs.get(0).Val - avrSum + 2) : 1;
		if (up > avrSum)
			up = avrSum - 1;
		standbySumDiff = EffPartition.LARGEST_SUM; // undefined standby inaccuracy
		lastSumDiff = finalResult.SumDiff;
		standbyNumbs = new IdNumbers(numbs);
		if (EffPartition.TEST)
			bottomCnt = upCnt = 0;
		do {
			minSum = (avrSum - up);
			maxSum = (avrSum + up);
			sumDiff = maxSum - minSum;
			callCnt = complete = 0;
			numbs.Reset();
			currResult.Clear();
			if (EffPartition.TEST)
				upCnt++;
			DSTree(0, ssCnt - 1);

			if (IsCompleteByPerfect() || (up *= 2) >= minSum // increase and checkup range expansion
					|| currResult.SumDiff > standbySumDiff) // is current result worse than standby one?
				break;
		} while (lastSumDiff != currResult.SumDiff); // until previous and current inaccuracy are different
		// use last fitted result
		if (EffPartition.TEST || currResult.SumDiff < finalResult.SumDiff) {
			SetRange(finalResult);
			finalResult.Fill(standbyNumbs, 1, standbySumDiff);
		}
	}

	/** Partition method invoke interface */
	public interface IDoPartition {
		void DoPart(int ssCnt);
	}

	/** Partition method delegates */
	static IDoPartition[] methods = new IDoPartition[4];

	/** Partition method titles to print while testing */
	final static String[] METHOD_TITLES = { "UGreedy", "Greedy", "SGreedy", "DSTree" };

	/**
	 * Performes partitioning
	 * 
	 * @param i     index of partition method to call
	 * @param ssCnt count od subsets
	 * @return true if result is 'perfect'
	 */
	boolean DoPartition(int i, int ssCnt) {
		long startTime;
		if (EffPartition.TEST) {
			System.out.printf("\n%s\t", METHOD_TITLES[i]);
			sumDiff = EffPartition.LARGEST_SUM;
			startTime = System.nanoTime();
		}
		methods[i].DoPart(ssCnt);
		if (EffPartition.TEST) {
			System.out.printf("%.3f ms\t", (float) (System.nanoTime() - startTime) / (1000 * 1000));
			System.out.printf("%d\t(%.2f)%%", finalResult.SumDiff, 100F * finalResult.SumDiff / avrSum);
			if (i == 2) // SGreedy
			{
				if (callCnt > 1)
					System.out.printf("\titers: %d", callCnt);
			} else if (i == 3) // DSTree
				System.out.printf("  calls|bottoms|ups: %d %d %d", callCnt, bottomCnt, upCnt);
			System.out.println();
//            finalResult.Print(0);
			return false;
		} else
			return IsResultPerfect();
	}

	/**
	 * Creates an instance and performs partition
	 * 
	 * @param nmbs    identified values to be distributed
	 * @param result  final result
	 * @param avr     average value sum
	 * @param limMult DSTree method call's limit multiplier; if 0 then omit DSTree
	 *                method invoking
	 */
	public Partition(IdNumbers nmbs, Result result, float avr, int limMult) {
		numbs = nmbs;
		finalResult = result;
		avrSum = avr;
		isAvrFract = (int) avrSum != avrSum;
		callLimit = MIN_CALL_LIMIT * limMult;
		numbs.SortByDescent();
		sumDiff = EffPartition.LARGEST_SUM;
		int ssCnt = finalResult.SubsetCount();
		int i = 0;
		methods[i++] = (Integer) -> {
			UGreedy(ssCnt);
		};
		methods[i++] = (Integer) -> {
			WrapGreedy(ssCnt);
		};
		methods[i++] = (Integer) -> {
			SGreedy(ssCnt);
		};
		methods[i++] = (Integer) -> {
			ISTree(ssCnt);
		};

		if (EffPartition.TEST) {
			// _items.Print();
			System.out.print("avr: ");
			System.out.printf(avrSum % 1.0 != 0 ? "%f" : "%s", avrSum);
			System.out.printf("\t%d...%d", numbs.get(numbs.size() - 1).Val, numbs.get(0).Val);
		}

		int cnt = limMult > 0 ? 4 : 3;
		if (!EffPartition.TEST)
			// for the degenerate case numbs.size()<=ssCnt method UGreedy() is comepletely
			// enough
			if (numbs.size() <= ssCnt)
				cnt = 1;
		for (i = 0; i < cnt; i++)
			if (DoPartition(i, ssCnt))
				return;
	}
}