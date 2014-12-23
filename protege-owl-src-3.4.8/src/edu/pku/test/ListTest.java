package edu.pku.test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

	public static void main(String[] args) {
		
		List<List<String>> twoArray = new ArrayList<List<String>>();
		
		System.out.println(twoArray.size());
		
		
			List<String> array = new ArrayList();
			array.add(0, "XXX");
			array.add(1, "YYY");
			System.out.println(array);
			twoArray.add(0, array);
			twoArray.add(1, array);
			
			System.out.println(twoArray.size());
			
		for (int i = 0; i < twoArray.size(); i++) {
			
			List<String> arr = twoArray.get(i);
			System.out.println(arr);
		}
		
		//
		double[] match = new double[10];
		
		System.out.println(match.length);
	}
}
