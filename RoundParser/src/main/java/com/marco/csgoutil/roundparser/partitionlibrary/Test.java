package com.marco.csgoutil.roundparser.partitionlibrary;

public class Test {
	public static void main(String[] args){
		String s = "11.45";
		Double d = Double.parseDouble(s);
		
		IdNumbers ids = new IdNumbers(9);
		ids.add(0,new IdNumber(1,30.69));
		ids.add(1,new IdNumber(2,11.17));
		ids.add(2,new IdNumber(3,12.8));
		ids.add(3,new IdNumber(4,12.0));
		ids.add(4,new IdNumber(5,19.43));
		ids.add(5,new IdNumber(6,5.43));
		ids.add(6,new IdNumber(7,25.31));
		double penaltyWeitgh = 2;
		Result res = new Result(9, 2, penaltyWeitgh);
		new Partition(ids, res,ids.getAvrSum(2), 1, penaltyWeitgh);
		res.print(0);
		
	}
}
