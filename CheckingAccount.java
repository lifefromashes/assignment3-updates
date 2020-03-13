package com.meritamerica.assignment3;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Each instance of this class represents a checking account 
 * Every checking account must be related to a customer
 * 
 * @date 3/9/2020
 * 
 */

public class CheckingAccount extends BankAccount{
	
	static final double INTEREST_RATE = .0001; 
	
	// constructor
	public CheckingAccount (double openingBalance) {
		super(openingBalance, INTEREST_RATE);
	}
	
	public CheckingAccount (BankAccount bankAccount) {
		super(bankAccount.getBalance(), bankAccount.getInterestRate(), bankAccount.getOpenedOn(), bankAccount.getAccountNumber());
	}
	
	/**
	 * Turns a string of text loaded from a file into a Bank Account object
	 * 
	 * See the MeritBank.readFromFile method for information formatting
	 * @return the created object
	 */
	public static CheckingAccount readFromString(String accountData) throws ParseException {
		return new CheckingAccount( BankAccount.readFromString(accountData) );
	}
	
	/**
	 * Condenses account info into a String for output
	 * 
	 * @return a String of the account information, ready for outputting
	 */
	@Override
	public String toString() {
		DecimalFormat format = new DecimalFormat("0.00");
		DecimalFormat other = new DecimalFormat("0.0000");
		String string = "";
		string += "Checking Account Balance: $";
		string += format.format(getBalance()) + "\n";
		string += "Checking Account Interest Rate: ";
		string += other.format(getInterestRate()) + "\n";
		string += "Checking Account Balance in 3 years: $";
		string += format.format(futureValue(3));
		
		return string; 
	}
	
}