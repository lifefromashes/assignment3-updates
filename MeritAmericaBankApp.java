package com.meritamerica.assignment3;

public class MeritAmericaBankApp {
	public static void main(String[] args) {
		System.out.println("Welcome to Merit Bank!");
		MeritBank.readFromFile("src/test/testMeritBank_good.txt");
		
		
		
		//MeritBank.sortAccountHolders();
	}
}