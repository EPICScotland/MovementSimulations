package animalMovementSimulation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * class to describe a farm, and contains list of animals
 * @author slycett
 * @version 31 March 2014
 */
public class Farm {

	private static int farmCount = 0;
	
	private static int getNextID() {
		farmCount++;
		return farmCount;
	}
	
	public static String columnNames() {
		String txt = "FarmID" + RandomMovementSimulator.delim + 
					 LocationCoordinate.getCoordinateNames();
		return txt;
	}
	
	public static String infoColumnNames() {
		String txt = "FarmID" + RandomMovementSimulator.delim + 
				     LocationCoordinate.getCoordinateNames()  + RandomMovementSimulator.delim + 
				     "NumberOfAnimals" + RandomMovementSimulator.delim + 
				     "Date";
		return txt;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int 		id;
	String 				farmID;
	LocationCoordinate 	loc;
	
	List<Animal> animals;
	
	public Farm() {
		loc		= new LocationCoordinate();
		setID();
		animals = new ArrayList<Animal>();
	}
	
	private void setID() {
		id 		= getNextID();
		farmID	= "F"+String.format("%09d", id);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setLocation(LocationCoordinate loc) {
		this.loc = loc;
	}
	
	public LocationCoordinate getLocation() {
		return loc;
	}
	
	public void addAnimal(Animal a) {
		if (!animals.contains(a)) {
			animals.add(a);
		}
	}
	
	public void addAnimals(List<Animal> alist) {
		animals.addAll(alist);
	}
	
	public void removeAnimal(Animal a) {
		if (animals.contains(a)) {
			animals.remove(a);
		}
	}
	
	public void sortAnimalsByBirthDate() {
		Collections.sort(animals);
	}
	
	public List<Animal> removeOldestAnimals(int n) {
		List<Animal> alist = new ArrayList<Animal>();
		
		int i = 0;
		while ((animals.size() > 0) && (i < n)) {
			Animal a = animals.remove(0);
			alist.add(a);
			i++;									// SJL 31 March 2014
		}
		
		return alist;
	}
	
	public List<Animal> removeDeadAnimals(Calendar date) {
		List<Animal> alist = new ArrayList<Animal>();
		
		for (Animal a : animals) {
			if ( !a.isAlive(date) ) {
				alist.add(a);
			}
		}
		
		for ( Animal a : alist ) {
			animals.remove(a);
		}
		
		return alist;
	}
	
	public int numberOfAnimals() {
		return animals.size();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getID() {
		return farmID;
	}
	
	/**
	 * returns farm ID lat lon numberOfAnimals (currently)
	 * @return
	 */
	public String info(Calendar currentDate) {
		String txt = farmID + RandomMovementSimulator.delim +
					 loc.toString() + RandomMovementSimulator.delim + 
					 animals.size() + RandomMovementSimulator.delim + 
					 RandomMovementSimulator.df.format(currentDate.getTime());
		return txt;
	}
	
	/**
	 * returns farmID lat lon
	 */
	public String toString() {
		String txt 	= farmID + RandomMovementSimulator.delim + loc.toString();
		//txt = txt + Simulator.delim + animals.size();
		return txt;
	}
	
}
