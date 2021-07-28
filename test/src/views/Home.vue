<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png" />
    <div id="container">
        <div id="wrapper">
            <div id="join" class="animate join">
                <h1>Join a Room</h1>
                <form onsubmit="register(); return false;" accept-charset="UTF-8">
                    <p>
                        <input type="text" name="name" value="" id="name"
                            placeholder="Username" required>
                    </p>
                    <p>
                        <input type="text" name="room" value="" id="roomName"
                            placeholder="Room" required>
                    </p>
                    <p class="submit">
                        <input type="button" name="commit" value="Join!" @click='register()'>
                    </p>
                </form>
            </div>
            <div id="room" style="display: none;">
                <h2 id="room-header"></h2>
                <div id="participants"></div>
                <input type="button" id="button-leave" @mouseup="leaveRoom"
                    value="Leave room">
            </div>
        </div>
    </div>
    <HelloWorld msg="Welcome to Your Vue.js App" />
  </div>
</template>

<script>
// @ is an alias to /src
let scope = '';
import HelloWorld from "@/components/HelloWorld.vue";
import adapter from 'webrtc-adapter';
import kurentoUtils from 'kurento-utils'

export default {
  name: "Home",
  components: {
    HelloWorld,
  },
  data() {
    return {
      PARTICIPANT_MAIN_CLASS : 'participant main',
      PARTICIPANT_CLASS : 'participant',
      ws : null,
      participants : {},
      name: '',
    }
  },
  created() {
       this.connect();
       scope = this;
  },
  methods: {
    connect() {
      console.log(location.host)
      const serverURL = 'wss://localhost:8443/groupcall';
      this.ws = new WebSocket(serverURL);
      console.log("kurento webSocket : ", this.ws)  
      
      this.ws.onopen = () => {
        console.log("kurento 소켓 열림")
      }

      this.ws.onmessage = (message) => {
        var parsedMessage = JSON.parse(message.data);
        console.info('Received message: ' + message.data);

        switch (parsedMessage.id) {
        case 'existingParticipants':
          this.onExistingParticipants(parsedMessage);
          break;
        case 'newParticipantArrived':
          console.log("메세지 받음: newParticipantArrived")
          this.onNewParticipant(parsedMessage);
          break;
        case 'participantLeft':
          this.onParticipantLeft(parsedMessage);
          break;
        case 'receiveVideoAnswer':
          this.receiveVideoResponse(parsedMessage);
          break;
        case 'iceCandidate':
          scope.participants[parsedMessage.name].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
                if (error) {
                console.error("Error adding candidate: " + error);
                return;
                }
            });
            break;
        default:
          console.error('Unrecognized message', parsedMessage);
        }
        }
    },

    Participant(name) {
      console.log("쿠렌토 참가자: ",name)
      console.log("scope: ", scope)
      console.log("class: ", scope.PARTICIPANT_CLASS, scope.PARTICIPANT_MAIN_CLASS)
      scope.name = name;
      var container = document.createElement('div');
      container.className = isPresentMainParticipant() ? scope.PARTICIPANT_CLASS : scope.PARTICIPANT_MAIN_CLASS;
      container.id = name;
      var span = document.createElement('span');
      var video = document.createElement('video');
      var rtcPeer;

      container.appendChild(video);
      container.appendChild(span);
      container.onclick = switchContainerClass;
      document.getElementById('participants').appendChild(container);
      console.log("만들어진 컨테이너: ",container)
      console.log("document: ", document)

      span.appendChild(document.createTextNode(name));

      video.id = 'video-' + name;
      video.autoplay = true;
      video.controls = false;


      this.getElement = function() {
        return container;
      }

      this.getVideoElement = function() {
        return video;
      }

      function switchContainerClass() {
        if (container.className === scope.PARTICIPANT_CLASS) {
          var elements = Array.prototype.slice.call(document.getElementsByClassName(scope.PARTICIPANT_MAIN_CLASS));
          elements.forEach(function(item) {
              item.className = scope.PARTICIPANT_CLASS;
            });

            container.className = scope.PARTICIPANT_MAIN_CLASS;
          } else {
          container.className = scope.PARTICIPANT_CLASS;
        }
      }

      function isPresentMainParticipant() {
        return ((document.getElementsByClassName(scope.PARTICIPANT_MAIN_CLASS)).length != 0);
      }

      this.offerToReceiveVideo = function(error, offerSdp, wp){
        if (error) return console.error ("sdp offer error")
        console.log('Invoking SDP offer callback function', wp);
        var msg =  { id : "receiveVideoFrom",
            sender : name,
            sdpOffer : offerSdp
          };
        scope.sendMessage(msg);
      }


      this.onIceCandidate = function (candidate, wp) {
          console.log("Local candidate" + JSON.stringify(candidate),wp);

          var message = {
            id: 'onIceCandidate',
            candidate: candidate,
            name: name
          };
          scope.sendMessage(message);
      }

      Object.defineProperty(this, 'rtcPeer', { writable: true});

      this.dispose = function() {
        console.log('Disposing participant ' + this.name);
        this.rtcPeer.dispose();
        container.parentNode.removeChild(container);
      };
    },

    register() {
      this.name = document.getElementById('name').value;
      var room = document.getElementById('roomName').value;

      document.getElementById('room-header').innerText = 'ROOM ' + room;
      document.getElementById('join').style.display = 'none';
      document.getElementById('room').style.display = 'block';

      var message = {
        id : 'joinRoom',
        name : this.name,
        room : room,
      }
      this.sendMessage(message);
      console.log(message)
    },

    onNewParticipant(request) {
      console.log("새로운 참가자 도착: onNewParticipant: ", request)
      scope.receiveVideo(request.name);
    },

    receiveVideoResponse(result) {
      scope.participants[result.name].rtcPeer.processAnswer (result.sdpAnswer, function (error) {
        if (error) return console.error (error);
      });
    },

    callResponse(message) {
      if (message.response != 'accepted') {
        console.info('Call not accepted by peer. Closing call');
        stop();
      } else {
        kurentoUtils.webRtcPeer.processAnswer(message.sdpAnswer, function (error) {
          if (error) return console.error (error);
        });
      }
    },

    onExistingParticipants(msg) {
        var constraints = {
          audio : true,
          video : {
            mandatory : {
              maxWidth : 320,
              maxFrameRate : 15,
              minFrameRate : 15
            }
          }
        };
        console.log(scope.name + " registered in room " + scope.room);
        var participant = new scope.Participant(scope.name);
        scope.participants[scope.name] = participant;
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

        msg.data.forEach(this.receiveVideo);
    },

    leaveRoom() {
      this.sendMessage({
        id : 'leaveRoom'
      });

      for ( var key in scope.participants) {
        scope.participants[key].dispose();
      }

      document.getElementById('join').style.display = 'block';
      document.getElementById('room').style.display = 'none';

      this.ws.close();
    },

    receiveVideo(sender) {
      console.log("비디오 보내는 참가자: ", sender)
      var participant = new scope.Participant(sender);
      scope.participants[sender] = participant;
      var video = participant.getVideoElement();
      console.log("비디오 받기 참가자-1: ", participant)
      console.log("비디오 받기 비디오: ", video)

      var options = {
          remoteVideo: video,
          onicecandidate: participant.onIceCandidate.bind(participant)
        }

      participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
          function (error) {
            if(error) {
              return console.error("receivecideo error: ", error);
            }
            this.generateOffer (participant.offerToReceiveVideo.bind(participant));
      });;
      console.log("비디오 받기 참가자-2: ", participant)
    },
    
    onParticipantLeft(request) {
      console.log('Participant ' + request.name + ' left');
      var participant = scope.participants[request.name];
      participant.dispose();
      delete scope.participants[request.name];
    },

    sendMessage(message) {
      var jsonMessage = JSON.stringify(message);
      console.log('Sending message: ' + jsonMessage);
      this.ws.send(jsonMessage);
    },
  },
};
</script>