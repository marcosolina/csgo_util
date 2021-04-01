package com.marco.csgoutil.roundparser.partition;

/**
 * Effective Partition Problem solution
 * @author Fedor Naumenko
 */

/**
 * Represent the identified number to be partitioned
 * 
 * @author Fedor Naumenko
 */
public final class IdNumber implements Comparable<IdNumber> {
	/** bin's inverse index: count_of_bins-1 for the first and 0 for the last one */
	int BinIInd;
	/** number's ID */
	int Id;
	/** number's value */
	Double Val;

	/**
	 * Constructor by number ID and value
	 * 
	 * @param id  number's ID
	 * @param val number's value
	 */
	public IdNumber(int id, Double val) {
		BinIInd = 0;
		Id = id;
		Val = val;
	}

	/**
	 * Constructor by number
	 * 
	 * @param numb number to be copied
	 */
	IdNumber(IdNumber numb) {
		BinIInd = numb.BinIInd;
		Id = numb.Id;
		Val = numb.Val;
	}

	/**
	 * Gets number's value
	 * 
	 * @return number's value
	 */
	public Double Value() {
		return Val;
	}

	/**
	 * Gets number's ID
	 * 
	 * @return number's ID
	 */
	public int ID() {
		return Id;
	}

	@Override
	public int compareTo(IdNumber n) {
		if (Val < n.Val)
			return 1;
		if (Val > n.Val)
			return -1;
		return 0;
	}

	/**
	 * Returns true if number is not allocated and its value is fitted to the upper
	 * limit
	 * 
	 * @param val upper limit
	 * @return true if number is not allocated and its value is fitted to the upper
	 *         limit
	 */
	boolean IsFitted(Double val) {
		return BinIInd == 0 && Val <= val;
	}
}