import axios from 'axios'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl}
})

function ParticipantDto( name ){
  this.name = name;
  this.events = [];
}

function EventDto( name, date, start, end ){
  this.name = name;
  this.eventDate = date;
  this.startTime = start;
  this.endTime = end;
}

export default {
  name: 'eventregistration',
  data () {
    return {
      participants: [],
      newParticipant: '',
      errorParticipant: '',
      response: [],
      events: [],
      errorEvent: '',
      newEvent:{
    	  name:'',
    	  startTime:'',
    	  endTime:'',
    	  eventDate:''},
      selectedParticipant:'',
      selectedEvent:'',
      registration:[],
      errorRegistration:''
    }
  },
  created: function() {
	// Initializing participants from backend
		AXIOS.get(`/participants`)
			.then(response => {
			// JSON responses are automatically parsed.
			this.participants = response.data
			})
			.catch(e => {
	      this.errorParticipant = e;
			});
	// Initializing events from backend
		AXIOS.get(`/events`)
		.then(response => {
		// JSON responses are automatically parsed.
		this.events = response.data
		})
		.catch(e => {
      this.errorEvent = e;
		});
  },
  methods: {
	  createParticipant: function (participantName) {
		  AXIOS.post(`/participants/`+participantName, {}, {})
		    .then(response => {
		    // JSON responses are automatically parsed.
		      this.participants.push(response.data)
		      this.newParticipant = ''
		      this.errorParticipant = ''
		    })
		    .catch(e => {
		      var errorMsg = e.response.data.message
		      console.log(errorMsg)
		      this.errorParticipant = errorMsg
		    });
		  
	  	},	
  		createEvent: function (newEvent){	
  			AXIOS.post(`/events/`+newEvent.name+`?date=`+newEvent.eventDate+`&startTime=`+newEvent.startTime+`&endTime=`+newEvent.endTime,
  					{params:{
  						name: newEvent.name,
  						date: newEvent.eventDate,
  						startTime: newEvent.startTime,
  						endTime: newEvent.endTime
  					}},{})
  				.then(response=>{
  					this.events.push(response.data)
  					this.newEvent={
  				    	  name:'',
  				    	  startTime:'',
  				    	  endTime:'',
  				    	  eventDate:''}
  					this.errorEvent=''
  				})
  				.catch(e=> {
  					var errorMsg = e.response.data.message
  					console.log(errorMsg)
  					this.errorEvent = errorMsg
  				});
  			
  			},
		  /*createEvent: function(newEvent){
			  document.getElementById('test').innerHTML = (newEvent.startTime);
		  }*/
  		register: function(selectedParticipant,selectedEvent){
  			AXIOS.post(`/register?participant=`+selectedParticipant+`&event=`+selectedEvent,{},{})
  			.then(response=>{
  				this.registration.push(response.data);
  				this.selectedParticipant='';
  				this.selectedEvent='';
  				this.errorRegistration='';
  			})
  			.catch(e=>{
  				var errorMsg = e.response.data.message
  					console.log(errorMsg)
  					this.errorEvent = errorMsg
  			});
  			location.reload(true);
  			},
  		check: function(selectedParticipant,selectedEvent){
  			if(selectedParticipant != '' && selectedEvent!=''){
  				document.getElementById("b").disabled =false;
  			}
  		}
  }
}
