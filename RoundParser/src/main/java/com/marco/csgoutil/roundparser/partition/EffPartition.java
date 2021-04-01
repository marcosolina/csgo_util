package com.marco.csgoutil.roundparser.partition;

/*
 * Effective Partition Problem (optimization version) solution
 * by Fedor Naumenko (fedor.naumenko@gmail.com), 2018
 * 
 * Effectively distributes a set of input integers into k subsets,
 * such that the difference between the subset sums is minimized.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents min and max sums
 * 
 * @author Fedor Naumenko
 */
class Range {
	Double minSum;
	Double maxSum;
	boolean updated; // true, if sums are updated

	Range(Double val) {
		minSum = maxSum = val;
		updated = false;
	}
}

/**
 * Represents subsets and their inaccuracy
 * 
 * @author Fedor Naumenko
 */
class Result {
	/** maximum difference between the subset sums (inaccuracy) */
	Double SumDiff;
	/** subsets */
	ArrayList<Subset> Bins;

	/**
	 * Constructs Result and reserves subsets capacity
	 * 
	 * @param nCnt  count of numbers
	 * @param ssCnt count of subsets
	 */
	Result(int nCnt, int ssCnt) {
		SumDiff = EffPartition.LARGEST_SUM;
		Bins = new ArrayList<>(ssCnt);
		for (int i = 0; i < ssCnt; i++)
			Bins.add(new Subset(nCnt / ssCnt + 2));
	}

	/** Gets the count of subsets */
	int SubsetCount() {
		return Bins.size();
	}

	/**
	 * Sorts subsets by their sum
	 * 
	 * @param ascend if true then in in ascending order, otherwise in descending
	 *               order
	 */
	void Sort(boolean ascend) {
		if (ascend)
			Collections.reverse(Bins);
		else
			Collections.sort(Bins);
	}

	/** Sorts subsets in descending order and sets subsets ID starting with 1 */
	void SetIDs() {
		if (!EffPartition.TEST)
			Sort(false); // sorts subsets in descending order
		// set subset's ID
		int i = 1; // first subset ID
		for (Subset ss : Bins)
			ss.id = i++;
	}

	/**
	 * Clears instance: removes all subsets number IDs and resets the sum of the
	 * number values
	 */
	void Clear() {
		SumDiff = EffPartition.LARGEST_SUM;
		Bins.forEach((ss) -> {
			ss.clear();
		});
	}

	/**
	 * Outfits this result
	 * 
	 * @param numbs numbers to be distributed
	 * @param ind   index of the first subset: 1 for SGreedy() or 0 for DSTree()
	 * @param diff  difference between the subset sums
	 */
	void Fill(IdNumbers numbs, int ind, Double diff) {
		int ssCnt = SubsetCount() - ind; // count of subsets minus first index
		Clear();
		numbs.forEach((n) -> {
			Bins.get(ssCnt - n.BinIInd).addNumb(n);
		});
		SumDiff = diff;
	}

	/**
	 * Sets the minimum/maximum subset sums and their difference for this instance
	 * 
	 * @return Range instance with this instance actual min amd max sums
	 */
	Range SetSumDiff() {
		// for(int i=1; i<Bins.Count; i++)
		// if (Bins[i].sumVal > maxSum) maxSum = Bins[i].sumVal;
		// else if (Bins[i].sumVal < minSum) minSum = Bins[i].sumVal;
		Range range = new Range(Bins.get(0).sumVal);
		Bins.forEach((ss) -> {
			if (ss.sumVal > range.maxSum)
				range.maxSum = ss.sumVal;
			else if (ss.sumVal < range.minSum)
				range.minSum = ss.sumVal;
		});
		Double newSumDiff = range.maxSum - range.minSum;
		if (newSumDiff < SumDiff) {
			SumDiff = newSumDiff;
			range.updated = true;
		} else
			range.updated = false;
		return range;
	}

	/**
	 * Prints an instance
	 * 
	 * @param prNumbCnt maximum count of printed number IDs or 0 if all
	 */
	void Print(int prNumbCnt) {
		if (Bins.isEmpty())
			return;
		byte width = DigitsCnt(Bins.get(0).sumVal); // first sum is the biggest
		Bins.forEach((ss) -> {
			ss.print(width, prNumbCnt);
		});
	}
	
	public List<Subset> getSubsets(){
		return Bins;
	}

	/**
	 * Gets count of digist in a value
	 * 
	 * @param val value
	 * @return count of digist
	 */
	static byte DigitsCnt(Double val) {
		byte res = 0;
		for (; val > 0; val /= 10, res++)
			;
		return res;
	}
}

/**
 * Creates items partition among subsets, possibly according equally item values
 * 
 * @author Fedor Naumenko
 */
public final class EffPartition {
	/** if set on TRUE then then print test messages */
	public final static boolean TEST = false;

	/** largest possible sum */
	final static Double LARGEST_SUM = Double.MAX_VALUE;
	/** final partition */
	Result result;
	/** average sum value among subsets */
	float avr;

	/**
	 * Initializes partition by identified numbers
	 * 
	 * @param numbs:  identified numbers to be distributed
	 * @param ssCnt   count of subsets
	 * @param limMult DSTree method call's limit multiplier; if 0 then omit DSTree
	 *                method invoking
	 */
	void Init(IdNumbers numbs, int ssCnt, int limMult) {
		result = new Result(numbs.size(), ssCnt);
		if (ssCnt == 0)
			return;
		new Partition(numbs, result, avr = numbs.AvrSum(ssCnt), limMult);
	}

	/**
	 * Constructs numbers partition by identified numbers, with sums sorted in
	 * descending order
	 * 
	 * @param numbs   identified numbers to be distributed
	 * @param ssCnt   count of subsets; if 0 then creates an empty partition with
	 *                undefined (maximum type's value) inaccuracy
	 * @param limMult DSTree method call's limit multiplier - it increases the limit
	 *                of 1 million recursive invokes by limMult times; if 0 then
	 *                omit DSTree method invoking (fast, but not 'perfect')
	 */
	public EffPartition(IdNumbers numbs, int ssCnt, int limMult) {
		Init(numbs, ssCnt, limMult);
	}

	/**
	 * Constructs numbers partition by identified numbers, with sums sorted in
	 * descending order
	 * 
	 * @param numbs identified numbers to be distributed
	 * @param ssCnt count of subsets; if 0 then creates an empty partition with
	 *              undefined (maximum type's value) inaccuracy
	 */
	public EffPartition(IdNumbers numbs, int ssCnt) {
		Init(numbs, ssCnt, 1);
	}

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
	public EffPartition(List<Double> vals, int ssCnt, int limMult) {
		int i = 1;
		IdNumbers numbs = new IdNumbers(ssCnt);

		for (Double val : vals)
			numbs.add(new IdNumber(i++, val));
		Init(numbs, ssCnt, limMult);
	}

	/**
	 * Constructs numbers partition by numbers, with sums sorted in descending order
	 * 
	 * @param vals  numbers to be distributed; their ID are assigned according to
	 *              their ordinal numbers in array
	 * @param ssCnt count of subsets; if 0 then creates an empty partition with
	 *              undefined (maximum type's value) inaccuracy
	 */
	public EffPartition(List<Double> vals, int ssCnt) {
		int i = 1;
		IdNumbers numbs = new IdNumbers(ssCnt);

		for (Double val : vals) {
			numbs.add(new IdNumber(i++, val));
		}
		Init(numbs, ssCnt, 1);
	}

	/**
	 * Gets the count of subsets
	 * 
	 * @return count of subsets
	 */
	public int SubsetCount() {
		return result.SubsetCount();
	}

	/**
	 * Gets a reference to the subsets container
	 * 
	 * @return reference to the subsets container
	 */
	public List<Subset> Subsets() {
		return result.Bins;
	}

	/**
	 * Gets average summary value among subsets
	 * 
	 * @return average summary value among subsets
	 */
	public float AvrSum() {
		return avr;
	}

	/**
	 * Gets inaccuracy: the difference between maximum and minimum summary value
	 * among subsets
	 * 
	 * @return inaccuracy
	 */
	public Double Inacc() {
		return result.SumDiff;
	}

	/**
	 * Gets relative inaccuracy: the inaccuracy in percentage to average summary
	 * 
	 * @return relative inaccuracy
	 */
	public Double RelInacc() {
		return 100F * Inacc() / avr;
	}

	/**
	 * Sorts subsets by their sum
	 * 
	 * @param ascend if true then in in ascending order, otherwise in descending
	 *               order
	 */
	public void Sort(boolean ascend) {
		if (Inacc() > 0)
			result.Sort(ascend);
	}

	/**
	 * Outputs subsets to console
	 * 
	 * @param prSumDiff if true then prints sum diference (in absolute and relative)
	 * @param prNumbCnt maximum number of printed number IDs or 0 if ptint all
	 */
	public void Print(boolean prSumDiff, int prNumbCnt) {
		if (prSumDiff)
			System.out.printf("inaccuracy: %f (%.2f%%)\n", Inacc(), RelInacc());
		result.Print(prNumbCnt);
	}
}
