package com.marco.csgoutil.roundparser.partitionlibrary;

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
	private int binIInd;
	/** number's ID */
	private int id;
	/** number's value */
	private Double val;

	/**
	 * Constructor by number ID and value
	 * 
	 * @param id  number's ID
	 * @param val number's value
	 */
	public IdNumber(int id, Double val) {
		this.binIInd = 0;
		this.id = id;
		this.val = val;
	}

	/**
	 * Constructor by number
	 * 
	 * @param numb number to be copied
	 */
	public IdNumber(IdNumber numb) {
		this.binIInd = numb.binIInd;
		this.id = numb.id;
		this.val = numb.val;
	}

	/**
	 * Gets number's value
	 * 
	 * @return number's value
	 */
	public Double getValue() {
		return this.val;
	}

	/**
	 * Gets number's ID
	 * 
	 * @return number's ID
	 */
	public int getID() {
		return this.id;
	}

	@Override
	public int compareTo(IdNumber n) {
		if (this.val < n.val)
			return 1;
		if (this.val > n.val)
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
	boolean isFitted(Double val) {
		return this.binIInd == 0 && this.val <= val;
	}

	public int getBinIInd() {
		return this.binIInd;
	}

	public void setBinIInd(int binIInd) {
		this.binIInd = binIInd;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getVal() {
		return this.val;
	}

	public void setVal(Double val) {
		this.val = val;
	}

}