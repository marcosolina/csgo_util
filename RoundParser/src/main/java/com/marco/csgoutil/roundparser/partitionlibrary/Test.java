package com.marco.csgoutil.roundparser.partitionlibrary;

public class Test {
	public static void main(String[] args){
		IdNumbers ids = new IdNumbers(9);
		ids.add(0,new IdNumber(1,31.08));
		ids.add(1,new IdNumber(2,30.69));
		ids.add(2,new IdNumber(3,26.16));
		ids.add(3,new IdNumber(4,23.75));
		ids.add(4,new IdNumber(5,22.92));
		ids.add(5,new IdNumber(6,12.82));
		ids.add(6,new IdNumber(7,12.0));
		ids.add(7,new IdNumber(8,10.64));
		ids.add(8,new IdNumber(9,5.43));
		Result res = new Result(9, 2);
		Partition p = new Partition(ids, res,ids.getAvrSum(2), 1);
		res.print(0);
	}
}
