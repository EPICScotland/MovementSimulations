package animalMovementSimulation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.*;

import math.*;

/**
 * class to simulate random cattle movements, contains a main method for testing
 * only simulates one type of animal (default = cattle)
 * @author Samantha Lycett
 * @created 6 Feb 2014
 * @version 7 Feb 2014
 */
public class RandomMovementSimulator implements Simulator {
	
	static SimpleDateFormat 	df		= new SimpleDateFormat("dd/MM/yyyy");
	static String				delim	= ",";

	List<Farm> 			 	farms;
	List<MovementRecord> 	movementRecords;	
	List<LifeHistoryRecord> lifeHistoryRecords;
	
	Species			species					= Species.CATTLE;
	Calendar		startDate				= new GregorianCalendar(2000, 0, 1);
	Calendar		endDate					= new GregorianCalendar(2014, 0, 2);
	int 			numberOfFarms			= 20;
	int				numberOfAnimalsPerFarm  = 100;
	int				animalAge				= 10;								// 10 years
	int				maxToMove				= 25;								// max animals in one movement
	int				minToMove				= 5;								// min animals in one movement
	int				maxBirth				= 10;								// max animals to be born per time slot
	int				minBirth				= 0;								// min animals to be born per time slot
	int				maxMovsPerTime			= 2;								// max farm-farm movements per time slot
	int				minMovsPerTime			= 0;								// min farm-farm movements per time slot
	
	// monthly time increments
	int 			timeIncrementType 		= Calendar.MONTH;
	int 			timeIncrementValue 		= 1;
	
	boolean			verbose					= true;
	
	String			rootname				= "simulations\\simMov";
	String			ext						= ".csv";
	BufferedWriter	movFile_paired;
	BufferedWriter	movFile_on;
	BufferedWriter  lifFile;
	BufferedWriter	farmFile;
	
	
	public RandomMovementSimulator() {
		
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void initialise() {
		
		farms  				= new ArrayList<Farm>();
		movementRecords		= new ArrayList<MovementRecord>();
		lifeHistoryRecords 	= new ArrayList<LifeHistoryRecord>();
		
		
		// initialise animal births before start of movements, here -5 years
		Calendar offsetBirth = (Calendar)startDate.clone();
		offsetBirth.add(Calendar.YEAR, -(animalAge/2));
		
		// create farms and animals
		for (int i = 0; i < numberOfFarms; i++) {
			Farm afarm = new Farm();
			
			
			if (verbose) {
				System.out.println("Farm = "+afarm.getID());
			}
			
			for (int j = 0; j < numberOfAnimalsPerFarm; j++) {
				Animal a = new Animal(offsetBirth, afarm, animalAge, species);				// animals born at offset date and live for 10 years
				afarm.addAnimal(a);	
				
				MovementRecord mov = new MovementRecord(a);
				mov.setType(MovementType.BIRTH);
				movementRecords.add(mov);
				
				if (verbose) System.out.println(a.toString());
			}
			
			farms.add(afarm);
		}
		
		// write all farm details to file
		try {
			
			BufferedWriter farmLocFile = new BufferedWriter(new FileWriter(rootname + "_farmLocations" + ext));
			farmLocFile.write( Farm.columnNames() );
			farmLocFile.newLine();
			
			for (Farm f : farms) {
				farmLocFile.write(f.toString());
				farmLocFile.newLine();
			}
			
			farmLocFile.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// open movements file - paired, from & to only
		try {
				movFile_paired = new BufferedWriter(new FileWriter(rootname + "_movementRecords_paired" + ext));
				movFile_paired.write( MovementRecord.columnNames_paired() );
				movFile_paired.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// open movements file for single ended movements, births, ons, offs, deaths
		try {
				movFile_on = new BufferedWriter(new FileWriter(rootname + "_movementRecords" + ext));
				movFile_on.write( MovementRecord.columnNames_single() );
				movFile_on.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// open life histories file
		try {
			lifFile = new BufferedWriter(new FileWriter(rootname + "_lifeHistories" + ext));
			lifFile.write( LifeHistoryRecord.columnNames() );
			lifFile.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// open farm information file
		try {
			farmFile = new BufferedWriter(new FileWriter(rootname + "_farmStatus" + ext));
			farmFile.write( Farm.infoColumnNames() );
			farmFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * chooses random from and to farms, and moves some animals between them on date, adds movement record to movementRecords
	 * adds movement record to list
	 * @param date
	 */
	void randomMoveAnimals(Calendar date) {
		
		int f1 = Distributions.randomInt(farms.size());
		int f2 = Distributions.randomInt(farms.size()-1);
		if (f2 >= f1) {
			f2 = f1+1;
		}
		
		Farm fromFarm = farms.get(f1);
		Farm toFarm   = farms.get(f2);
		
		int numberToMove 			= Distributions.randomInt( maxToMove - minToMove ) + minToMove;
		List<Animal> movingAnimals 	= fromFarm.removeOldestAnimals(numberToMove);
		
		if (movingAnimals.size() > 0) {
			toFarm.addAnimals(movingAnimals);
		
			for (Animal a : movingAnimals) {
				MovementRecord mov = new MovementRecord(a, fromFarm, toFarm, date, date);
				mov.setType(MovementType.PAIRED);
				movementRecords.add(mov);
				
				if (verbose) System.out.println(mov.toString());
			}
		
		}
		
	}
	
	/**
	 * removes deceased animals from farms, adds DEATH record to movementRecords, and adds lifehistory records to list at date
	 * @param date
	 */
	void removeDeadAnimals(Calendar date) {
		
		for (Farm f : farms) {
			List<Animal> deceased = f.removeDeadAnimals(date);
			if (deceased.size() > 0) {
				for (Animal a : deceased) {
					MovementRecord mov = new MovementRecord(a, f, date);
					mov.setType(MovementType.DEATH);					// actually set in constructor but just making sure here
					movementRecords.add(mov);
					
					LifeHistoryRecord lif = new LifeHistoryRecord(a, f);
					lifeHistoryRecords.add(lif);
					
					if (verbose) System.out.println(lif.toString());
				}
			}
		}
		
	}
	
	/**
	 * adds a random number of new animals to each farm at date, adds BIRTH movement record to movementRecords
	 * @param date
	 */
	void randomBirthOnFarms(Calendar date) {
		
		for (Farm f : farms) {
			int nbirths = Distributions.randomInt( maxBirth - minBirth ) + minBirth;
			for (int i = 0; i < nbirths; i++) {
				Animal a = new Animal(date, f, animalAge, species);				// animals born at start date and live for 10 years
				f.addAnimal(a);
				
				MovementRecord mov = new MovementRecord(a);
				mov.setType(MovementType.BIRTH);
				movementRecords.add(mov);
				
				if (verbose) System.out.println(a.toString());
			}
		}
		
	}
	
	/**
	 * births between APRIL and SEPTEMBER
	 * @param currentDate
	 * @return
	 */
	boolean isBirthSeason(Calendar currentDate) {
		int currentMonth = currentDate.get(Calendar.MONTH);
		
		if ( ( currentMonth >= Calendar.APRIL ) && ( currentMonth <= Calendar.SEPTEMBER) ) {
			return true;
		} else {
			return false;
		}
				
	}
	
	/**
	 * allow movements all year, but see isBirthSeason for if this should be restricted
	 * @param currentDate
	 * @return
	 */
	boolean isMovingSeason(Calendar currentDate) {
		return true;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void run() {
		initialise();
		
		// step every month
		Calendar currentDate = (Calendar)startDate.clone();
		
		// write initial state of farms to file
		try {
			for (Farm f : farms) {
				farmFile.write(f.info(currentDate));
				farmFile.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while ( currentDate.before(endDate) ) {
			
			if (verbose) {
				System.out.println("Date = "+df.format(currentDate.getTime()));
				System.out.println("Remove Deceased");
			}
			removeDeadAnimals(currentDate);
			
			if ( isBirthSeason(currentDate) ) {
				if (verbose) System.out.println("Births");
				randomBirthOnFarms(currentDate);
			}
			
			if ( isMovingSeason(currentDate) ) {
				if (verbose) System.out.println("Movements");
				
				int numberMovsPerTime = Distributions.randomInt( maxMovsPerTime - minMovsPerTime ) + minMovsPerTime;
				for (int i = 0; i < numberMovsPerTime; i++) {
					randomMoveAnimals(currentDate);
				}
			}
			
			// increment by one month
			currentDate.add(timeIncrementType, timeIncrementValue);
			
			// write movements and life histories to file
			try {
				while (movementRecords.size() > 0) {
					MovementRecord m = movementRecords.remove(0);
					
					if (m.getType().equals(MovementType.PAIRED)) {
						// PAIRED to paired file
						movFile_paired.write(m.getRecord(MovementType.PAIRED));
						movFile_paired.newLine();
						
						// ONs and OFFs to single ended file
						movFile_on.write(m.getRecord(MovementType.ON));
						movFile_on.newLine();
						
						movFile_on.write(m.getRecord(MovementType.OFF));
						movFile_on.newLine();
					} else if (m.getType().equals(MovementType.BIRTH) || m.getType().equals(MovementType.DEATH)) {
						// BIRTHS and DEATHS to single ended file
						movFile_on.write(m.getRecord());
						movFile_on.newLine();
					}
					
					
				}
			
				while (lifeHistoryRecords.size() > 0) {
					LifeHistoryRecord l = lifeHistoryRecords.remove(0);
					lifFile.write(l.getRecord());
					lifFile.newLine();
				}
				
				for (Farm f : farms) {
					farmFile.write(f.info(currentDate));
					farmFile.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		// flush the remaining animal's lifehistories
		if (verbose) System.out.println("Remaining Animals");
		try {
			for (Farm f : farms) {
				for (Animal a : f.animals) {
					LifeHistoryRecord l = new LifeHistoryRecord(a, f);
					lifFile.write(l.getRecord());
					lifFile.newLine();
					
					if (verbose) System.out.println(l.getRecord());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// close log files
		try {
			movFile_paired.close();
			movFile_on.close();
			lifFile.close();
			farmFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void test() {
		Distributions.initialise();
		
		RandomMovementSimulator sim = new RandomMovementSimulator();
		sim.run();
	}
	
	public static void run(String[] args) {
		System.out.println("Setting simulation output root file name to "+args[0]);
		RandomMovementSimulator sim = new RandomMovementSimulator();
		sim.rootname = args[0];
		sim.verbose  = false;
		sim.run();
	}
	
	public static void main(String[] args) {
		System.out.println("** RandomMovementSimulator **");
		System.out.println("-- Simulates random Animal movements between Farms --");
		
		if (args.length == 0) {
			test();
		} else {
			run(args);
		}
		
		System.out.println("** END **");
	}
	
}
