package com.ixigo.library.partitioning;

/**
 * Represents min and max sums
 * 
 * @author Fedor Naumenko
 */
public class Range {
	private Double minSum;
	private Double maxSum;
	private boolean updated; // true, if sums are updated

	public Range(Double val) {
		minSum = maxSum = val;
		updated = false;
	}

	public Double getMinSum() {
		return minSum;
	}

	public void setMinSum(Double minSum) {
		this.minSum = minSum;
	}

	public Double getMaxSum() {
		return maxSum;
	}

	public void setMaxSum(Double maxSum) {
		this.maxSum = maxSum;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

}