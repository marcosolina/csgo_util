package com.marco.csgoutil.roundparser.partitionlibrary;

public class Test {
	public static void main(String[] args){
		IdNumbers ids = new IdNumbers(9);
		ids.add(0,new IdNumber(1,35.3));
		ids.add(1,new IdNumber(2,8.88));
		ids.add(2,new IdNumber(3,4.41));
		ids.add(3,new IdNumber(4,12.0));
		ids.add(4,new IdNumber(5,11.75));
		ids.add(5,new IdNumber(6,10.46));
		ids.add(6,new IdNumber(7,9.38));
		ids.add(7,new IdNumber(8,5.43));
		double penaltyWeitgh = 2;
		Result res = new Result(9, 2, penaltyWeitgh);
		new Partition(ids, res,ids.getAvrSum(2), 1, penaltyWeitgh);
		res.print(0);
	}
}
