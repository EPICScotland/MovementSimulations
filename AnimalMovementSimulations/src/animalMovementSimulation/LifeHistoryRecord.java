package animalMovementSimulation;

import java.text.SimpleDateFormat;

/**
 * Output file 			= *_lifeHistories.txt
 * Life History column headers (6):
 * 0. animalID 
 * 1. date of birth 
 * 2. location of birth 
 * 3. date of death, 
 * 4. location of death 
 * 5. species
 * 
 * @author Samantha Lycett
 * @created 6 Feb 2014
 * @version 7 Feb 2014
 */
public class LifeHistoryRecord {
	
	protected static String				delim 	= RandomMovementSimulator.delim;
	protected static SimpleDateFormat 	df		= RandomMovementSimulator.df;								//new SimpleDateFormat("yyyy/MM/dd");
	
	
	public static String columnNames() {
		String line = "AnimalID" + delim + 
					  "BirthDate" + delim + 
					  "BirthFarm" + delim +
					  "DeathDate" + delim + 
					  "DeathFarm" + delim + 
					  "Species";
		return line;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// instance variables and methods
	
	Animal 	animal;
	Farm	birthFarm;
	Farm	deathFarm;
	
	public LifeHistoryRecord() {
		
	}
	
	public LifeHistoryRecord(Animal animal, Farm deathFarm) {
		this.animal 	= animal;
		this.birthFarm  = animal.birthFarm;
		this.deathFarm 	= deathFarm;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the animal
	 */
	public Animal getAnimal() {
		return animal;
	}

	/**
	 * @param animal the animal to set
	 */
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	/**
	 * @return the birthFarm
	 */
	public Farm getBirthFarm() {
		return birthFarm;
	}

	/**
	 * @param birthFarm the birthFarm to set
	 */
	public void setBirthFarm(Farm birthFarm) {
		this.birthFarm = birthFarm;
	}

	/**
	 * @return the deathFarm
	 */
	public Farm getDeathFarm() {
		return deathFarm;
	}

	/**
	 * @param deathFarm the deathFarm to set
	 */
	public void setDeathFarm(Farm deathFarm) {
		this.deathFarm = deathFarm;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getRecord() {
		return toString();
	}
	
	public String toString() {
		String line = 	animal.getID() + delim + 
						df.format(animal.birthDate.getTime()) + delim + 
						birthFarm.getID() + delim +
						df.format(animal.deathDate.getTime()) + delim +  
						deathFarm.getID() + delim + 
						animal.species;
		return line;
	}

}
