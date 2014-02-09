package animalMovementSimulation;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Cattle ID, species, destination location, date reached destination, movement type
 * @author Samantha Lycett
 *
 */
public class MovementRecord {
	
	// class variables and methods
	
	protected static String				delim 	= RandomMovementSimulator.delim;							// "\t"
	protected static SimpleDateFormat 	df		= RandomMovementSimulator.df;								//new SimpleDateFormat("yyyy/MM/dd");
	
	public static String columnNames_paired() {
		String txt = "AnimalID" + delim + 
					 "Species"  + delim +
					 "FromFarm" + delim + 
					 "FromDate" + delim +
					 "ToFarm"   + delim + 
					 "ToDate";
		return txt;
	}
	
	public static String columnNames_single() {
		String txt = "AnimalID" + delim + 
					 "Species" + delim + 
					 "FarmID" + delim + 
					 "Date" + delim + 
					 "Type";
		return txt;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// instance variables and methods
	
	Animal 		animal;
	Farm		fromFarm;
	Farm		toFarm;
	Calendar	fromDate;
	Calendar	toDate;
	MovementType type;
	
	/**
	 * constructor to use for BIRTH events
	 * @param animal
	 */
	public MovementRecord(Animal animal) {
		this.animal = animal;
		this.type 	= MovementType.BIRTH;
	}
	
	/**
	 * constructor to use for DEATH events
	 * @param animal
	 * @param fromFarm
	 * @param fromDate
	 */
	public MovementRecord(Animal animal, Farm fromFarm, Calendar fromDate) {
		this.animal 	= animal;
		this.fromFarm   = fromFarm;
		this.fromDate	= fromDate;
		this.type		= MovementType.DEATH;
	}
	
	/**
	 * constructor to use for general movement events
	 * @param animal
	 * @param fromFarm
	 * @param toFarm
	 * @param fromDate
	 * @param toDate
	 */
	public MovementRecord(Animal animal, Farm fromFarm, Farm toFarm, Calendar fromDate, Calendar toDate) {
		this.animal 	= animal;
		this.fromFarm 	= fromFarm;
		this.toFarm		= toFarm;
		this.fromDate	= fromDate;
		this.toDate		= toDate;
		this.type		= MovementType.PAIRED;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////
	// getters and setters
	
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
	 * @return the fromFarm
	 */
	public Farm getFromFarm() {
		return fromFarm;
	}

	/**
	 * @param fromFarm the fromFarm to set
	 */
	public void setFromFarm(Farm fromFarm) {
		this.fromFarm = fromFarm;
	}

	/**
	 * @return the toFarm
	 */
	public Farm getToFarm() {
		return toFarm;
	}

	/**
	 * @param toFarm the toFarm to set
	 */
	public void setToFarm(Farm toFarm) {
		this.toFarm = toFarm;
	}

	/**
	 * @return the fromDate
	 */
	public Calendar getFromDate() {
		return fromDate;
	}

	/**
	 * @param date the fromDate to set
	 */
	public void setFromDate(Calendar date) {
		this.fromDate = date;
	}
	
	/**
	 * @return the toDate
	 */
	public Calendar getToDate() {
		return toDate;
	}

	/**
	 * @param date the toDate to set
	 */
	public void setToDate(Calendar date) {
		this.toDate = date;
	}
	
	public MovementType getType() {
		return this.type;
	}
	
	public void setType(MovementType type) {
		this.type = type;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	// double ended movements
	
	/**
	 * returns the paired record if the movement type is PAIRED
	 * @return
	 */
	String get_paired_record() {
		if (this.type.equals(MovementType.PAIRED)) {
			String txt = 	animal.getID() + delim + 
				 		animal.species   + delim +
				 		fromFarm.getID() + delim + 
				 		df.format(fromDate.getTime()) + delim +
				 		toFarm.getID()   + delim + 
				 		df.format( toDate.getTime() );
			return txt;
		} else {
			return null;
		}
	}
	
	// single ended movements
	
	/**
	 * returns the birth record, movement type is not checked because all information from animal object
	 * @return
	 */
	String get_birth_record() {
		String txt	= 	animal.getID() + delim +
						animal.species + delim + 
						animal.birthFarm.getID() + delim +
						df.format(animal.birthDate.getTime()) + delim + 
						MovementType.BIRTH;
		return txt;
	}
	
	/**
	 * returns death record, if the movement type is DEATH
	 */
	String get_death_record() {
		if (this.type.equals(MovementType.DEATH)) {
			String txt	= 	animal.getID() + delim +
						animal.species + delim +
						fromFarm.getID() + delim + 
						df.format( fromDate.getTime()) + delim +
						MovementType.DEATH;
			return txt;
		} else {
			return null;
		}
	}
	
	/**
	 * returns ON record if the movement type is PAIRED or ON
	 * @return
	 */
	String get_on_record() {
		if ( this.type.equals(MovementType.PAIRED) || this.type.equals(MovementType.ON)) {
			String txt = animal.getID()   + delim + 
						 animal.species   + delim +
						 toFarm.getID()   + delim + 
						 df.format( toDate.getTime() ) + delim + 
						 MovementType.ON;
			return txt;
		} else {
			return null;
		}
	}
	
	/**
	 * returns OFF record fi the movement type is PAIRED or OFF
	 * @return
	 */
	String get_off_record() {
		if ( this.type.equals(MovementType.PAIRED) || this.type.equals(MovementType.OFF)) {
			String txt = animal.getID()   + delim + 
				 	 	animal.species   + delim +
				 	 	fromFarm.getID()   + delim + 
				 	 	df.format( fromDate.getTime() ) + delim + 
				 	 	MovementType.OFF;
			return txt;
		} else {
			return null;
		}
	}
	
	/**
	 * gets record according to type of this record
	 * @return
	 */
	public String getRecord() {
		if (type.equals(MovementType.ON)) {
			return get_on_record();
		} else if (type.equals(MovementType.OFF)) {
			return get_off_record();
		} else if (type.equals(MovementType.PAIRED)) {
			return get_paired_record();
		} else if (type.equals(MovementType.BIRTH)) {
			return get_birth_record();
		} else if (type.equals(MovementType.DEATH)) {
			return get_death_record();
		} else {
			return null;
		}
	}
	
	/**
	 * returns record of input type if possible otherwise returns null
	 * @param type
	 * @return
	 */
	public String getRecord(MovementType type) {
		
		if (type.equals(MovementType.ON)) {
			return get_on_record();
		} else if (type.equals(MovementType.OFF)) {
			return get_off_record();
		} else if (type.equals(MovementType.PAIRED)) {
			return get_paired_record();
		} else if (type.equals(MovementType.BIRTH)) {
			return get_birth_record();
		} else if (type.equals(MovementType.DEATH)) {
			return get_death_record();
		} else {
			return get_paired_record();
		}
	}
	
	
	/**
	 * returns paired record
	 */
	public String toString() {
		return get_paired_record();
	}
	
	
	
}
