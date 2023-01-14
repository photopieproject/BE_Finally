// // 3. 시그널링 서버에 연결하기 위해 WebSocket 연결을 생성.
// // 우리가 구축 한 Spring boot 시그널링 서버가 http://localhost:8080에서 실행되고 있다고 가정하면 연결을 만들 수 있다.
// var conn = new WebSocket('ws://localhost:8080/socket');
//
// // 신호 서버로 메시지를 보내기 위해 다음 단계에서 메시지를 전달하는 데 사용할 send메서드를 만든다.
// function send(message) {
//     conn.send(JSON.stringify(message));
// }
//
// // 4. client.js에서 클라이언트를 설정 한 후 RTCPeerConnection 클래스에 대한 객체를 만들어야한다.
// configuration = null;
//
// var peerConnection = new RTCPeerConnection(configuration);
//
// // 5. 메시지 전달에 사용할 dataChannel을 만들 수 있다.
// var dataChannel = peerConnection.createDataChannel("dataChannel", {reliable: true});
//
// // 6. 데이터 채널에서 다양한 이벤트에 대한 리스너를 만들 수 있다.
// dataChannel.onerror = function (error) {
//     console.log("Error:", error);
// };
// dataChannel.onclose = function () {
//     console.log("Data channel is closed");
// }
//
// // 7. ICE와의 연결 설정
// // 오퍼를 생성 후 peerConnection의 로컬 설명으로 설정한다. 그런 다음 제안을 다른 피어에게 보낸다.
// // send 메소드는 오퍼 정보를 전달하기 위해 시그널링 서버를 호출한다.
// // 서버 측 기술로 send 메소드의 로직을 자유롭게 구현할 수 있다.
// peerConnection.createOffer(function (offer) {
//     send({
//         event: "offer",
//         data: offer
//     });
//     peerConnection.setLocalDescription(offer);
// }, function(error) {
//     // Handle error here
// });
//
// // 8. ICE 후보자처리
// // WebRTC는 ICE(Interactive Connection Establichment) 프로토콜을 사용하여 피어를 검색하고 연결을 설정한다.
// // peerConnection에 로컬 설명을 설정하면 icecandidate 이벤트가 트리거된다.
// // 이 이벤트는 원격 피어가 원격 후보 세트에 후보를 추가할 수 있도록 후보를 원격 피어로 전송해야한다.
// // 이를 위해 onicecandidate이벤트에 대한 리스너를 만든다.
// // icecandidate의 모든 후보가 수집 될 때 이벤트는 빈 후보 문자열을 다시 트리거합니다.
// // 이 후보 객체도 원격 피어에 전달해야합니다.
// // 이 빈 후보 문자열을 전달하여 원격 피어가 모든 icecandidate 객체가 수집 되었음을 알 수 있도록합니다 .
// // 또한 동일한 이벤트가 다시 트리거되어 ICE 후보 수집이 이벤트 에서 null로 설정된 후보 객체 값으로 완료되었음을 나타냅니다 .
// // 이것은 원격 피어로 전달할 필요가 없습니다.
// peerConnection.onicecandidate = function (event) {
//     if (event.candidate){
//         send({
//             event: "candidate",
//             data: event.candidate
//         });
//     }
// };
//
// // 9. ICE 후보자 받기
// // 다른 피어가 보낸 ICE 후보를 처리해야한다.
// // 이 후보를 수신 한 원격 피어는 후보를 후보 풀에 추가해야한다.
// peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
//
// // 10. 제안 받기
// // 그 후 다른 피어가 오퍼를 수신하면 이를 원격 설명으로 설정해야한다.
// // 또한 응답을 생성해야하며 이는 시작 피어로 전송된다.
// peerConnection.setRemoteDescription(new RTCSessionDescription(offer));
// peerConnection.createAnswer(function (answer) {
//     peerConnection.setLocalDescription(answer);
//     send({
//         event: "answer",
//         date: answer
//     });
// }, function (error){
//
// });
//
// // 11. 답변 받기
// // 시작하는 피어는 응답을 받고 원격 설명으로 설정한다.
// // 이를 통해 WebRTC는 성공적인 연결을 설정한다.
// function handleAnswer(answer){
//     peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
// }
//
// // 12. 메시지 보내기
// // 연결을 설정했으므로 dataChannel의 send메서드를 사용하여 피어간에 메시지를 보낼 수 있다.
// dataChannel.send("message");
// dataChannel.onmessage = function (event){
//     console.log("Message:", event.data);
// };
// peerConnection.ondatachannel = function (event){
//     dataChannel = event.channel;
// }
//
// // 13. 비디오 및 오디오 채널 추가
// // WebRTC가 P2P 연결을 설정하면 오디오 및 비디오 스트림을 직접 쉽게 전송할 수 있다.
// const constraints = { // constraints 객체를 사용하여 비디오의 프레임 속도, 너비 및 높이를 지정할 수 있다.
//     video: {
//         frameRate: {
//             ideal: 10,
//             max: 15
//         },
//         width: 1280,
//         height: 720,
//         facingMode: "user" // 후방 카메라를 활성화하고자 한다면 faceMode의 값을 "user"대신 "environment"로 설정할 수 있다.
//     }, audio : true
// };
// navigator.mediaDevices.getUserMedia(constraints).
// then(function(stream) { /* use the stream */ })
//     .catch(function(err) { /* handle the error */ });
//
// // 14. 스트림 보내기
// // WebRTC피어 연결 객체에 스트림을 추가해야한다.
// // 피어 연결에 스트림을 추가하면 연결된 피어에서 addstream 이벤트가 트리거된다.
// peerConnection.addStream(stream);
//
// // 15. 스트림 받기
// // 원격 피어에서 스트림을 수신하기 위해 listener
// peerConnection.onaddstream = function (event) {
//     videoElement.srcObject = event.stream;
// }
//
// // 16. STUN 사용
// // 네트워크 정보를 피어와 공유하기 전에 클라이언트는 STUN 서버에 요청한다.
// // STUN 서버의 책임은 요청을 수신 한 IP주소를 반환하는 것이다.
// // STUN 서버를 쿼리하여 공용 IP주소를 얻습니다. 그런 다음 이 IP 및 포트 정보를 연결하려는 피어와 공유한다.
// // 다른 피어도 동일한 작업을 수행하여 공용 IP를 공유할 수 있다.
// // STUN 서버를 사용하려면 RTCPeerConnection 객체 를 생성하기 위해 구성 객체에 URL을 전달하면됩니다 .
// var configuration = {
//     "iceServers" : [ {
//         "url": "stun:stun2.1.google.com:19302"
//     }]
// }
//
// // 17. TURN 사용
// // {
// //     'iceServers': [
// //     {
// //         'urls': 'stun:stun.l.google.com:19302'
// //     },
// //     {
// //         'urls': 'turn:10.158.29.39:3478?transport=udp',
// //         'credential': 'XXXXXXXXXXXXX',
// //         'username': 'XXXXXXXXXXXXXXX'
// //     },
// //     {
// //         'urls': 'turn:10.158.29.39:3478?transport=tcp',
// //         'credential': 'XXXXXXXXXXXXX',
// //         'username': 'XXXXXXXXXXXXXXX'
// //     }
// // ]
// // }

//connecting to our signaling server
var conn = new WebSocket('ws://localhost:8080/socket');

conn.onopen = function() {
    console.log("Connected to the signaling server");
    initialize();
};

conn.onmessage = function(msg) {
    console.log("Got message", msg.data);
    var content = JSON.parse(msg.data);
    var data = content.data;
    switch (content.event) {
        // when somebody wants to call us
        case "offer":
            handleOffer(data);
            break;
        case "answer":
            handleAnswer(data);
            break;
        // when a remote peer sends an ice candidate to us
        case "candidate":
            handleCandidate(data);
            break;
        default:
            break;
    }
};

function send(message) {
    conn.send(JSON.stringify(message));
}

var peerConnection;
var dataChannel;
var input = document.getElementById("messageInput");

function initialize() {
    var configuration = null;

    peerConnection = new RTCPeerConnection(configuration);

    // Setup ice handling
    peerConnection.onicecandidate = function(event) {
        if (event.candidate) {
            send({
                event : "candidate",
                data : event.candidate
            });
        }
    };

    // creating data channel
    dataChannel = peerConnection.createDataChannel("dataChannel", {
        reliable : true
    });

    dataChannel.onerror = function(error) {
        console.log("Error occured on datachannel:", error);
    };

    // when we receive a message from the other peer, printing it on the console
    dataChannel.onmessage = function(event) {
        console.log("message:", event.data);
    };

    dataChannel.onclose = function() {
        console.log("data channel is closed");
    };

    peerConnection.ondatachannel = function (event) {
        dataChannel = event.channel;
    };

}

function createOffer() {
    console.log("찬이짱")
    peerConnection.createOffer(function(offer) {
        send({
            event : "offer",
            data : offer
        });
        peerConnection.setLocalDescription(offer);
    }, function(error) {
        alert("Error creating an offer");
    });
}

function handleOffer(offer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(offer));

    // create and send an answer to an offer
    peerConnection.createAnswer(function(answer) {
        peerConnection.setLocalDescription(answer);
        send({
            event : "answer",
            data : answer
        });
    }, function(error) {
        alert("Error creating an answer");
    });

};

function handleCandidate(candidate) {
    peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
};

function handleAnswer(answer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
    console.log("connection established successfully!!");
};

function sendMessage() {
    dataChannel.send(input.value);
    input.value = "";
}





