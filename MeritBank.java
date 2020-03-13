package com.meritamerica.assignment3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Scanner;

import javax.swing.filechooser.FileFilter;

/**
 * This class is the main point for storing bank information
 * All methods and variables should be static 
 * 
 * @date 3/5/2020
 *
 */

public class MeritBank {
	
	private static AccountHolder[] accountHolders = new AccountHolder[100];
	private static int accountHolderIndex = 0;
	private static CDOffering[] cdOfferings = new CDOffering[100];
	private static long nextAccountNumber = 1234567;
	
	/**
	 * Call this method from the App to save a created account holder 
	 *      to the bank's list
	 *       
	 * @param accountHolder the AccountHolder object to save
	 */
	public static void addAccountHolder(AccountHolder accountHolder) {
		//determine the size to make the account holder array
		int arraySize = 0;
		for(int i=0; i<accountHolders.length; i++) {
			if(accountHolders[i] == null) {
				break;
			}
			arraySize ++;
		}
				
		AccountHolder[] temp = new AccountHolder[arraySize + 1];
		for(int i=0; i<arraySize; i++) {
			temp[i] = accountHolders[i];
		}
		temp[arraySize] = accountHolder;
		accountHolders = temp;	
		
		//accountHolders[accountHolderIndex] = accountHolder;
		//System.out.println("Added " + accountHolder.getFirstName() + " to " + accountHolderIndex);
		accountHolderIndex ++;
	}
	
	// begin getters
	public static AccountHolder[] getAccountHolders() {
		return accountHolders;
	}
	
	static CDOffering[] getCDOfferings() {
		return cdOfferings;
	}
	// end getters
	
	/**
	 * Method to find the best CD account for a customer
	 *   Because the assignment details did not specify any length of time
	 *   this only return the best interest rate, regardless of time
	 *   
	 * @param depositAmmount not used in calculation =(
	 * @return the cdOffering with the best interest rate
	 */
	static CDOffering getBestCDOffering(double depositAmmount) {
		if(cdOfferings == null) {return null;}
		double bestValue = 0;
		int bestIndex = -1;
		for(int i=0; i<cdOfferings.length; i++) {
			if(cdOfferings[i].getInterestRate() > bestValue) {
				bestValue = cdOfferings[i].getInterestRate();
				bestIndex = i;
			}
		}
		
		return cdOfferings[bestIndex];
	}
	
	/**
	 * Method to find the 2nd best CD account for a customer
	 *   Because the assignment details did not specify any length of time
	 *   this only return the best interest rate, regardless of time
	 *   
	 *   calls bestCDOffering and rejects object equal to that
	 *   
	 * @param depositAmmount not used in calculation =(
	 * @return the cdOffering with the best interest rate
	 */
	public static CDOffering getSecondBestCDOffering(double depositAmount) {
		if(cdOfferings == null) {return null;}
		CDOffering best = getBestCDOffering(depositAmount);
		
		double secondBestValue = 0;
		int secondBestIndex = -1;
		for(int i=0; i<cdOfferings.length; i++) {
			if(cdOfferings[i].getInterestRate() > secondBestValue 
					&& !best.equals(cdOfferings[i])) {
				secondBestValue = cdOfferings[i].getInterestRate();
				secondBestIndex = i;
			}
		}
		if(secondBestIndex == -1) { return null; }
		return cdOfferings[secondBestIndex];
	}
	
	/**
	 * Erase all existing CDOfferings
	 */
	public static void clearCDOfferings() {
		cdOfferings = null;
	}
	
	/**
	 * Define which CDOfferings are offered
	 * 
	 * @param offerings An array of CDOfferings to make available to the accountHolders
	 */
	public static void setCDOfferings(CDOffering[] offerings) {
		//determine the size to make the offerings array
		int arraySize = 0;
		for(int i=0; i<offerings.length; i++) {
			if(offerings[i] == null) {
				break;
			}
			arraySize ++;
		}
		
		cdOfferings = new CDOffering[arraySize];
		for(int i=0; i<arraySize; i++) {
			cdOfferings[i] = offerings[i];
		}
	}
	
	public static long getNextAccountNumber() {
		return nextAccountNumber;
	}

	public static void setNextAccountNumber(long accountNumber) {
		nextAccountNumber = accountNumber;
	}
	
	/**
	 * total the value of all accounts held by bank's account holders
	 * 
	 * @return sum A double value of the combined accounts 
	 */
	static double totalBalances() {
		double sum = 0;
		for(AccountHolder ah : accountHolders) {
			if(ah == null) {break;}
			
			for(CheckingAccount account: ah.getCheckingAccounts()) {
				try {
					sum += account.getBalance();
				} catch (NullPointerException expected) {
					//if account holder has less CheckingAccounts than other 
					//kinds this will catch. No additional handling needed
				}
			}
			for(SavingsAccount account: ah.getSavingsAccounts()) {
				try {
					sum += account.getBalance();
				} catch (NullPointerException expected) {
					//if account holder has less SavingsAccounts than other 
					//kinds this will catch. No additional handling needed
				}
			}
			for(CDAccount account: ah.getCDAccounts()) {
				try {
					sum += account.getBalance();
				} catch (NullPointerException expected) {
					//if account holder has less CDAccounts than other 
					//kinds this will catch. No additional handling needed
				}
			}
		}
		
		
		return sum;
	}
	
	/**
	 * Calculates the value of an account with interest applied
	 * 
	 * @param presentValue a double indicating the starting value of the account
	 * @param interestRate a double indicating the interest rate to apply
	 * @param term an int indicating the number of years the interest will be applied for
	 * 
	 * @return a double of the projected account balance
	 */
	static double futureValue(double presentValue, double interestRate, int term) {
		double futureValue = presentValue * (Math.pow(1+ presentValue, term));
		return futureValue;
	}
	
	/**
	 * erase the current arrays and counters to their start point
	 * 
	 * call this before reading from a file
	 */
	static void clearMemory() {
		accountHolders = new AccountHolder[100];
		accountHolderIndex = 0;
		cdOfferings = new CDOffering[100];
	}
	
	/**
	 * load saved information- customers, accounts, offerings, etc
	 * 
	 */
	static boolean readFromFile(String fileName) {
		clearMemory();
		
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(reader);			
			
			AccountHolder loadedHolder = new AccountHolder();		
			String line; // current line from the file
			
			line = bufferedReader.readLine(); // read account number
			MeritBank.setNextAccountNumber( Long.parseLong(line) );
			
			
			line = bufferedReader.readLine(); // read number of cd offerings
			int totalCDO = Integer.parseInt(line);
			CDOffering[] loadedCDOfferings = new CDOffering[totalCDO];
			
			for(int i=0; i < totalCDO; i++) {
				line = bufferedReader.readLine(); // read cd offering
				loadedCDOfferings[i] = CDOffering.readFromString(line);
			}
			setCDOfferings(loadedCDOfferings);
			
			line = bufferedReader.readLine(); // read number of account holders
			int totalAccountHolders = Integer.parseInt(line);
			
			for(int i=0; i < totalAccountHolders; i++) {
				line = bufferedReader.readLine(); // read account holder
				loadedHolder = AccountHolder.readFromString(line);
				addAccountHolder(loadedHolder);
				
				line = bufferedReader.readLine(); // read number of checking accounts
				int totalChecking = Integer.parseInt(line);
				for(int j=0; j < totalChecking; j++) {
					line = bufferedReader.readLine(); // read checking accounts
					CheckingAccount c = CheckingAccount.readFromString(line);
					loadedHolder.addCheckingAccount(c);
				}
				
				line = bufferedReader.readLine(); // read number of savings accounts
				int totalSavings = Integer.parseInt(line);
				for(int j=0; j < totalSavings; j++) {
					line = bufferedReader.readLine(); // read savings accounts
					SavingsAccount s = SavingsAccount.readFromString(line);
					loadedHolder.addSavingsAccount(s);
				}
				
				line = bufferedReader.readLine(); // read number of cd accounts
				int totalCD = Integer.parseInt(line);
				for(int j=0; j < totalCD; j++) {
					line = bufferedReader.readLine(); // read cd accounts
					CDAccount s = CDAccount.readFromString(line);
					loadedHolder.addCDAccount(s);
				}
				
			}
			bufferedReader.close();
			return true;
		} catch (Exception e) {

		}
		return false;
	}
	
	
	/**
	 * Save current information in memory to a text file for future access
	 * 
	 * currently not implemented, no tests require it.
	 * 
	 * when implemented, should iterate though all objects similar to the 
	 * getTotalBalance method and call each object's writeToString 
	 * 
	 * @param fileName
	 * @return true if successful
	 */
	static boolean writeToFile(String fileName) {
		
		return false;
	}
	
	/**
	 * Sorts based on combined value of all accounts
	 * See compareTo in AccountHolder for more info 
	 * 
	 * @return the sorted array of AccountHolders
	 */
	static AccountHolder[] sortAccountHolders() {
		Arrays.sort(accountHolders);
		return accountHolders;
	}
	
	

}
