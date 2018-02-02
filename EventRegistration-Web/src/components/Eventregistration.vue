<template>
<div id="eventregistration">
  	<h2>Participants</h2>
   	 <table v-for="participant in participants" id="participant">
   	 	  <tr>
     	     <td>{{ participant.name }}</td>
     	     <td>
       			 <ul>
          		<li v-for="event in participant.events">
           			 {{event.name}}
         		 </li>
       			 </ul>
     		 </td>
  		</tr>
	</table>
	<br>
	<h2>Events</h2>
	<table id="heading">
		<tr>
			<th>Name</th>
			<th>Start Time</th>
			<th>End Time</th>
			<th>Event Date</th>
		</tr>
	</table>
	<table v-for="event in events" id="event">
		
		<tr>
			<td>{{event.name}}</td>
			<td>{{event.startTime}}</td>
			<td>{{event.endTime}}</td>
			<td>{{event.eventDate}}</td>
		</tr>
	</table>
	<br>
	<br>
	<table>
		<tr>
		<th>Participant Name</th>
		</tr>
		<tr>
    		<td>
       			 <input type="text" v-model="newParticipant" placeholder="Participant Name">
   			</td>
   			<td>
      		 	 <button @click="createParticipant(newParticipant)">Create Participant</button>
    		</td>
  		</tr>
  	<br>
  	<br>
  		<tr>
  			<th>Event Name</th>
  			<th>Event Date</th>
  			<th>Start Time</th>
  			<th>End Time</th>
  		</tr>
  		<tr>
  			<td>
  				<input type="text" v-model="newEvent.name" placeholder="Event Name">
  			</td>
  			<td>
  				<input type="date" v-model="newEvent.eventDate" placeholder="YYYY-MM-DD">
  			</td>

  			<td>
				<input type="time" v-model="newEvent.startTime" placeholder="HH:mm">
			</td>

			<td>
				<input type="time" v-model="newEvent.endTime" placeholder="HH:mm">
  			</td>
  			<td>
  				<button @click="createEvent(newEvent)"> Create Event </button>
  			</td>
  		</tr>
  	<br>
  	<br>
  		<tr>
  		<th>Participant</th>
  		<th>Event</th>
  		</tr>
  		<tr>
  			<td>
  				<select v-model="selectedParticipant" v-on:change="check(selectedParticipant,selectedEvent)">
					<option disabled value="">Please select one</option>
  					<option :value="participant.name" v-for='participant in participants' >{{participant.name}}</option>
  				</select>
  			</td>
  			<td>
  				<select v-model="selectedEvent" v-on:change="check(selectedParticipant,selectedEvent)">
					<option disabled value="">Please select one</option>
  					<option :value="event.name" v-for='event in events' >{{event.name}}</option>
  				</select>
  			</td>
  			<td>
  				<button v-on:click="register(selectedParticipant,selectedEvent)" disabled id="b"> Register </button>
  			</td>
  		</tr>
  	</table>
   		 <p>
     		 <span v-if="errorParticipant" style="color:red">Error: {{errorParticipant}} </span>
   		 </p>
   		 <p>
     		 <span v-if="errorEvent" style="color:red">Error: {{errorEvent}} </span>
   		 </p>
   		 <p>
   		 	<span v-if="errorREgistration" style="color:red">Error: {{errorRegistration}} </span>
   		 </p>
  </div>

</template>

<script src="./registration.js">
</script>

<style>
 	#eventregistration {
   	 font-family: 'Avenir', Helvetica, Arial, sans-serif;
   	 color: #2c3e50;
   	 background: #f2ece8;
  	}
  	#heading th, #event td, #participant td{
    	height: 5px;
    	padding: 10px;
    	width: 200px;
	}

	#event table, #heading table{
		width:80%;
	}
	
</style>

