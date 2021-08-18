import { sendMessage } from './webSocket'
import { ElMessage } from 'element-plus'
import root from '@/store/index.js'


const PARTICIPANT_MAIN_CLASS = 'participant main el-col el-col-6 is-guttered';
const PARTICIPANT_CLASS = 'participant el-col el-col-6 is-guttered';

function Participant(name) {
  this.name = name;
  var container = document.createElement('div');
  // container.className = isPresentMainParticipant() ? PARTICIPANT_CLASS : PARTICIPANT_MAIN_CLASS;
  container.className = PARTICIPANT_CLASS;
  container.id = name;
  container.style.paddingLeft = '10px'
  container.style.paddingRight = '10px'
  var div = document.createElement('div');
  var namespace = document.createElement('div');
  var span = document.createElement('span');
  var voteSpace = document.createElement('div');
  var video = document.createElement('video');
  var img = document.createElement('img');
  var rtcPeer;

  container.appendChild(div);
  container.appendChild(namespace);
  namespace.appendChild(span);
  namespace.appendChild(voteSpace);
  div.appendChild(img);
  div.appendChild(video);
  container.onclick = switchContainerClass;
  container.onclick = votePlayer;
  document.getElementById('participants').appendChild(container);

  div.style = 'position:relative;'
  img.src = require('@/assets/img-dead.png');
  img.style = 'height:50%;opacity:0;position:absolute;'

  // 이름 스타일
  span.style.float = 'left'
  span.style.paddingLeft = '10px'
  span.style.fontWeight = 'bold'
  span.appendChild(document.createTextNode(name));

  voteSpace.style.position = 'relative'
  voteSpace.id = 'vote-space-' + name;

  // 비디오 스타일
  video.id = 'video-' + name;
  video.autoplay = true;
  video.controls = false;
  video.style.height = '100%'
  video.style.width = '100%'


  this.getElement = function() {
      return container;
   }

  this.getVideoElement = function() {
      return video;
  }

  function sendVoteMessage(toName) {
    let pathname = window.location.pathname.split('/');
    let roomNum = 0
    //console.log(root.state.userId + " voted To " + toName);
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
    //console.log('투표할 수 있나요?? ', root.state.voteStarted)
    if (root.state.voteStarted) {
        if (!root.state.mylife) {
          ElMessage({
            dangerouslyUseHTMLString: true,
            message: '<strong>죽은 사람은 투표할 수 없습니다!</strong>',
            type: 'danger',
          });
          // alert('죽은 사람은 투표할 수 없습니다!')
        }
        else if (container.id !== root.state.userId) {
          for (let player of root.state.participantsList) {
              if (player.userId == container.id) {
                if (player.death) {
                  ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: '<strong>죽은 사람에게는 투표할 수 없습니다!</strong>',
                    type: 'danger',
                  });
                    // alert('죽은 사람에게는 투표할 수 없습니다!')
                } else {
                  //console.log('check!!! : ', container.id, root.state.userId)
                  sendVoteMessage(container.id)
                }
                break;
              }
          }
        } else {
        ElMessage({
          dangerouslyUseHTMLString: true,
          message: '<strong>자기 자신에게는 투표할 수 없습니다!</strong>',
          type: 'danger',
        });
          // alert('자기 자신에게는 투표할 수 없습니다!')
      }
    }
  }

  // style cursor none 만들기 좋은 함수
  function switchContainerClass() {
    if (container.className === PARTICIPANT_CLASS) {
      var elements = Array.prototype.slice.call(document.getElementsByClassName(PARTICIPANT_MAIN_CLASS));
      //   //console.log('elements : ', elements)
      elements.forEach(function (item) {
          //console.log('ITEM : ', item)
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
    if (error) return //console.error ("sdp offer error")
    // //console.log('Invoking SDP offer callback function');
    var msg =  { id : "receiveVideoFrom",
          sender : name,
          sdpOffer : offerSdp
        };
    sendMessage(msg);
  }


  this.onIceCandidate = function (candidate, wp) {
      //   //console.log("Local candidate" + JSON.stringify(candidate));
    var message = {
      id: 'onIceCandidate',
      candidate: candidate,
      name: name
    };
    sendMessage(message);
  }

  Object.defineProperty(this, 'rtcPeer', { writable: true});

  this.dispose = function() {
    //console.log('Disposing participant ' + this.name);
    this.rtcPeer.dispose();
    try{
      container.parentNode.removeChild(container);
    } catch {
      //console.log('removeChild 오류 발생')
    }
  };
}

export {Participant};
