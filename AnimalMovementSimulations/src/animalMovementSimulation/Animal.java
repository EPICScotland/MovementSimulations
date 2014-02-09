package animalMovementSimulation;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Animal implements Comparable<Animal> {
	
	protected static SimpleDateFormat df = RandomMovementSimulator.df;
	protected static String delim 		 = RandomMovementSimulator.delim;

	private static int animalCount = 0;
	
	private static int getNextID() {
		animalCount++;
		return animalCount;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	private int id;
	String			animalID;
	Calendar		birthDate;
	Calendar		deathDate;
	Farm			birthFarm;
	Species			species;
	
	public Animal(Calendar birthDate, Farm birthFarm, int ageInYears, Species species) {
		
		this.species 	= species;
		setAnimalID();
		this.birthDate  = birthDate;
		this.birthFarm  = birthFarm;
		this.deathDate  = (Calendar)birthDate.clone();
		deathDate.add(Calendar.YEAR, ageInYears);
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	private void setAnimalID() {
		id			= getNextID();
		animalID 	= "A" + String.format("%09d", id);
	}
	
	public void setBirthFarm(Farm f) {
		this.birthFarm = f;
	}
	
	public void setBirthDate(Calendar birthDate) {
		this.birthDate = birthDate;
	}
	
	public void setDeathDate(Calendar deathDate) {
		this.deathDate = deathDate;
	}
	
	public void setSpecies(Species species) {
		this.species = species;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getID() {
		return animalID;
	}
	
	public Species getSpecies() {
		return species;
	}
	
	public boolean isAlive(Calendar d) {		
		boolean ans = ( d.after(birthDate) || d.equals(birthDate) ) && ( (d.before(deathDate))  );
		return ans;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Animal)) {
			return false;
		}
		Animal other = (Animal) obj;
		if (birthDate == null) {
			if (other.birthDate != null) {
				return false;
			}
		} else if (!birthDate.equals(other.birthDate)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		return true;
	}


	@Override
	public int compareTo(Animal b) {
		if (birthDate.after(b.birthDate)) {
			return 1;
		} else if (birthDate.before(b.birthDate)) {
			return -1;
		} else {
			return 0;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String toString() {
		
		String line = animalID + delim + birthFarm.getID() + delim + df.format( birthDate.getTime() ) + delim + df.format( deathDate.getTime() ) + delim + species;
		return line;
	}
	
}
