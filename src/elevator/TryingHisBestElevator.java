package student;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import elevator.Elevator;
import model.Person;

public class TryingHisBestElevator implements Elevator {

    private int currentFloor;
	private int peopleWaitingAtFloors;
	private List<List<Person>> peopleByFloor;

	private boolean lastPersonArrived = false;

	private List<Person> peopleInElevator = new ArrayList<>();



    public TryingHisBestElevator(int elevatorCapacity) {
    }

    @Override
    public void startsAtFloor(LocalTime time, int initialFloor) {
		this.currentFloor = initialFloor;
    }

    @Override
    public void peopleWaiting(List<List<Person>> peopleByFloor) {
		this.peopleByFloor = peopleByFloor;
    	this.peopleWaitingAtFloors =
                peopleByFloor.stream()
                        .mapToInt(List::size)
                        .sum();
    }

    @Override
    public int chooseNextFloor() {
    	if (lastPersonArrived && 
    			this.peopleInElevator.size() == 0 &&
    			this.peopleWaitingAtFloors == 0) {
            return 1;
        } else if (lastPersonArrived &&
    			this.peopleInElevator.size() == 0) {
    	    if (noOneIsWaitingAtCurrentFloor()) {
                return findFloorWithTheMostNumberOfPeople();
            } else {
    	        return this.peopleByFloor.get(this.currentFloor - 1).get(0).getDestinationFloor();
            }
        } else if (lastPersonArrived) {
    	    return this.peopleInElevator.get(0).getDestinationFloor();
    	} else {
            if (this.peopleWaitingAtFloors == 0) {
                return 1;
            }
            else {
            	return findFloorWithTheMostNumberOfPeople();
            }
    	}    
    }

    private boolean noOneIsWaitingAtCurrentFloor() {
        return this.peopleByFloor.get(this.currentFloor - 1).isEmpty();
    }
    
    private int findFloorWithTheMostNumberOfPeople() {
        int floorWithTheMostNumberOfPeople = 0;
        int MostNumberOfPeople = 0;
        for (int floorIndex = 0 ; floorIndex < this.peopleByFloor.size() ; floorIndex++) {
            int numberOFPeopleWaiting = this.peopleByFloor.get(floorIndex).size();
            if (numberOFPeopleWaiting >= MostNumberOfPeople) {
                MostNumberOfPeople = numberOFPeopleWaiting;
                floorWithTheMostNumberOfPeople = floorIndex + 1;
            }
        }
        return floorWithTheMostNumberOfPeople;
    }
    
    @Override
    public void arriveAtFloor(int floor) {
    	this.currentFloor = floor;
    }

    @Override
    public void loadPerson(Person person) {
    		this.peopleWaitingAtFloors--;
    	this.peopleByFloor.get(this.currentFloor - 1).remove(person);
    	this.peopleInElevator.add(person);
    }

    @Override
    public void unloadPerson(Person person) {
    	this.peopleInElevator.remove(person);
        System.out.println("People in elevator = " + this.peopleInElevator.size());
        if (lastPersonArrived) {
            System.out.println("Last person arrived");
        }   
    }

    @Override
    public void newPersonWaitingAtFloor(int floor, Person person) {
    	this.peopleWaitingAtFloors++;
        this.peopleByFloor.get(floor - 1).add(person);
    }

    @Override
    public void lastPersonArrived() {
    	this.lastPersonArrived  = true;
    }
}

