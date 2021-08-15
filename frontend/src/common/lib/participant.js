import { sendMessage } from './webSocket'
import root from '@/store/index.js'


const PARTICIPANT_MAIN_CLASS = 'participant main el-col el-col-6 is-guttered';
const PARTICIPANT_CLASS = 'participant el-col el-col-6 is-guttered';

function Participant(name) {
	this.name = name;
	var container = document.createElement('div');
  container.className = isPresentMainParticipant() ? PARTICIPANT_CLASS : PARTICIPANT_MAIN_CLASS;
	container.id = name;
	var span = document.createElement('span');
	var span2 = document.createElement('span');
	var video = document.createElement('video');
	var rtcPeer;

	container.appendChild(video);
	container.appendChild(span);
	container.appendChild(span2);
  container.onclick = switchContainerClass;
  container.onclick = votePlayer;
	document.getElementById('participants').appendChild(container);

  span.appendChild(document.createTextNode(name));
  span2.style.color = 'red'
  span2.style.fontSize = '20px'
  span2.style.fontWeight = 'bold'

	video.id = 'video-' + name;
	video.autoplay = true;
	video.controls = false;


	this.getElement = function() {
		return container;
	}

	this.getVideoElement = function() {
		return video;
  }

  function sendVoteMessage(toName) {
    let pathname = window.location.pathname.split('/');
    let roomNum = 0
    console.log(root.state.userId + " voted To " + toName);
    if (pathname[1] === 'game') {
      roomNum = parseInt(pathname[3])
      if (root.state.stompClient && root.state.stompClient.connected) {
        const msg = {
          roomId: roomNum,
          fromName: root.state.userId,
          toName: toName,
        };
        root.state.stompClient.send('/sub/vote/room/' + roomNum, JSON.stringify(msg), {});
      }
      container.parentElement.style.pointerEvents = 'none'
    }
  }

  function votePlayer() {
    if (container.id !== root.state.userId) {
      console.log('check!!! : ', container.id, root.state.userId)
      sendVoteMessage(container.id)
      // console.log('clicked : ', root.state)
    } else {
      alert('자기 자신에게는 투표할 수 없습니다!')
    }
  }

  // style cursor none 만들기 좋은 함수
	function switchContainerClass() {
		if (container.className === PARTICIPANT_CLASS) {
      var elements = Array.prototype.slice.call(document.getElementsByClassName(PARTICIPANT_MAIN_CLASS));
    //   console.log('elements : ', elements)
      elements.forEach(function (item) {
          console.log('ITEM : ', item)
					item.className = PARTICIPANT_CLASS;
				});

				container.className = PARTICIPANT_MAIN_CLASS;
			} else {
			container.className = PARTICIPANT_CLASS;
    }
  }


	function isPresentMainParticipant() {
		return ((document.getElementsByClassName(PARTICIPANT_MAIN_CLASS)).length != 0);
	}

	this.offerToReceiveVideo = function(error, offerSdp, wp){
		if (error) return console.error ("sdp offer error")
		// console.log('Invoking SDP offer callback function');
		var msg =  { id : "receiveVideoFrom",
				sender : name,
				sdpOffer : offerSdp
			};
		sendMessage(msg);
	}


	this.onIceCandidate = function (candidate, wp) {
		//   console.log("Local candidate" + JSON.stringify(candidate));

		  var message = {
		    id: 'onIceCandidate',
		    candidate: candidate,
		    name: name
		  };
		  sendMessage(message);
	}

	Object.defineProperty(this, 'rtcPeer', { writable: true});

	this.dispose = function() {
		console.log('Disposing participant ' + this.name);
		this.rtcPeer.dispose();
		try{
			container.parentNode.removeChild(container);
		} catch {
			console.log('removeChild 오류 발생')
		}
	};
}

export {Participant};
