package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;
import ca.mcgill.ecse321.eventregistration.persistence.PersistenceXStream;



@Service
public class EventRegistrationService {
	private RegistrationManager rm;
	
	public EventRegistrationService(RegistrationManager rm) {
		this.rm = rm;
	}
	
	public Participant createParticipant(String name) throws InvalidInputException {
		if (name == null || name.trim().length() == 0) {
			//trim() delete space from the start and end
		    throw new InvalidInputException("Participant name cannot be empty!");
		  }
		    
		Participant p = new Participant(name);
		rm.addParticipant(p);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return p;
	}
	
	public List<Participant> findAllParticipants()
	{
		return rm.getParticipants();
	}
	
	public List<Event> findAllEvents(){
		return rm.getEvents();
	}
	
	public List<Event> getEventsForParticipant(Participant p) {
		List<Registration> allRegistrations = rm.getRegistrations();
		List<Event> eventsForAParticipant = new ArrayList<Event>();
		for(Registration registration : allRegistrations) {
			if(registration.getParticipant() == p) {
				eventsForAParticipant.add(registration.getEvent());
			}
		}
		return eventsForAParticipant;
	}
	//need to write test for it...
	
	public Event createEvent(String name, Date date, Time startTime, Time endTime) throws InvalidInputException {
		if(name == null && date == null && startTime == null && endTime == null){
			
				throw new InvalidInputException("Event name cannot be empty! Event date cannot be empty! Event start time cannot be empty! Event end time cannot be empty!");
			
		}
		if(name.trim().length() == 0) {
			throw new InvalidInputException("Event name cannot be empty!");
		}
		if(startTime.getTime() > endTime.getTime()) {
			throw new InvalidInputException("Event end time cannot be before event start time!");
		}
		
		Event e = new Event(name, date, startTime, endTime);
		    rm.addEvent(e);
		    PersistenceXStream.saveToXMLwithXStream(rm);
		    return e;
		}
	

	public Registration register(Participant participant, Event event) throws InvalidInputException {
		String error = "";
		if (participant == null)
			error = error + "Participant needs to be selected for registration! ";
		else if (!rm.getParticipants().contains(participant))
			error = error + "Participant does not exist! ";
		if (event == null)
			error = error + "Event needs to be selected for registration!";
		else if (!rm.getEvents().contains(event))
			error = error + "Event does not exist!";
		error = error.trim();
		if (error.length() > 0)
			throw new InvalidInputException(error);
		Registration r = new Registration(participant, event);
		rm.addRegistration(r);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return r;

}

	public Participant getParticipantByName(String name) {
		List<Participant> participants = rm.getParticipants();
		Participant participant = null;
		for(Participant p : participants)
			if (name.equals(p.getName()))
			{	participant = p;
				break;
			}
		return participant;
	}

	
	public Event getEventByName(String name) {
		Event event =null;
		List<Event> allEvents = rm.getEvents();
		for(Event e: allEvents) {
			if((e.getName()).equals(name))
			{	event = e;
				break;
			}	
		}
		return event;
	}
}


