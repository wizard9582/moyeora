import { Participant } from './participant';
import { sendMessage, ws } from './webSocket';
import kurentoUtils from 'kurento-utils'
import root from '@/store/index.js'

var participants = {};
var name;

window.onbeforeunload = function() {
	ws.close();
};

ws.onmessage = function(message) {
	var parsedMessage = JSON.parse(message.data);
	// console.info('Received message: ' + message.data);

	switch (parsedMessage.id) {
	case 'existingParticipants':
		onExistingParticipants(parsedMessage);
		break;
	case 'newParticipantArrived':
		onNewParticipant(parsedMessage);
		break;
	case 'participantLeft':
		onParticipantLeft(parsedMessage);
		break;
	case 'receiveVideoAnswer':
		receiveVideoResponse(parsedMessage);
		break;
	case 'iceCandidate':
		participants[parsedMessage.name].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
	        if (error) {
		      console.error("Error adding candidate: " + error);
		      return;
	        }
	    });
	    break;
	default:
		// console.error('Unrecognized message', parsedMessage);
	}
}

function register(roomNum, userName) {
  name = userName
  var room = roomNum

  var message = {
	  id: 'joinRoom',
	  name: name,
	  room: room,
	}
	sendMessage(message);
}

function onNewParticipant(request) {
	receiveVideo(request.name);
}

function receiveVideoResponse(result) {
	participants[result.name].rtcPeer.processAnswer (result.sdpAnswer, function (error) {
		if (error) return console.error (error);
  });

  if (root.state.micOff) {
    console.log('마이크가 꺼져있다면 시작 전에 꺼버리자 : ', participants[name].rtcPeer)
    console.log('마이크가 꺼져있다면 시작 전에 꺼버리자 : ', participants[name].rtcPeer.peerConnection.getLocalStreams())
    participants[name].rtcPeer.audioEnabled = false;
  }
  if (root.state.videoOff) {
    participants[name].rtcPeer.videoEnabled = false;
  }
}

function callResponse(message) {
	if (message.response != 'accepted') {
		// console.info('Call not accepted by peer. Closing call');
		stop();
	} else {
		webRtcPeer.processAnswer(message.sdpAnswer, function (error) {
			if (error) return console.error (error);
		});
	}
}

function onExistingParticipants(msg) {
	var constraints = {
		audio : true,
		video : {
			mandatory : {
				maxWidth : 200,
				maxFrameRate : 15,
				minFrameRate : 15
			}
		}
	};
	// console.log(name + " registered in room " + room);
	var participant = new Participant(name);
	participants[name] = participant;
	// console.log('in conference room : ', Object.keys(participants).length)
	var video = participant.getVideoElement();

	var options = {
	      localVideo: video,
	      mediaConstraints: constraints,
	      onicecandidate: participant.onIceCandidate.bind(participant)
	    }
	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options,
		function (error) {
		  if(error) {
			  return console.error(error);
		  }
		  this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});

	msg.data.forEach(receiveVideo);
}

function leaveRoom() {
	sendMessage({
		id : 'leaveRoom'
	});

  for (var key in participants) {
		participants[key].dispose();
	}

	// document.getElementById('join').style.display = 'block';
	// document.getElementById('room').style.display = 'none';

	//ws.close();
}

function receiveVideo(sender) {
	var participant = new Participant(sender);
	participants[sender] = participant;
	var video = participant.getVideoElement();

	var options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant)
    }

	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
			function (error) {
			  if(error) {
				  return console.error(error);
			  }
			  this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});;
}

function onParticipantLeft(request) {
	console.log('Participant ' + request.name + ' left');
	var participant = participants[request.name];
	participant.dispose();
	delete participants[request.name];
}

function muteMic(name, payload) {
  console.log('마이크 끄기 .js : ', participants[name].rtcPeer)
  if(payload)
    participants[name].rtcPeer.audioEnabled = false;
  else
    participants[name].rtcPeer.audioEnabled = true;
}

function offCam(name, payload) {
  if (payload)
    participants[name].rtcPeer.videoEnabled = false;
  else
    participants[name].rtcPeer.videoEnabled = true;
}


export {register, leaveRoom, participants, muteMic, offCam};
