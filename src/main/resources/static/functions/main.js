'use strict';

var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');


var stompClient = null;
var login = null;
var permissions = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    login = document.querySelector('#name').value.trim();

    if(login) {
        document.querySelector('#loginPage').classList.add('hidden');
        document.querySelector('#chatPage').classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    // Tell your login to the server
    stompClient.send("/api/v1/addUser", {}, JSON.stringify({login: login}));

    connectingElement.classList.add('hidden');
}


function onError(error) {
    console.log(error.content)
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    // if(messageContent && stompClient) {
        var chatMessage = {
            login: login,
            messageBody: messageInput.value
        };

        stompClient.send("/api/v1/addMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';

    event.preventDefault();
}

function getPermissions(login) {
    permissions = stompClient.send("/api/v1/getPermissions", {}, JSON.stringify({login: login}));

}



function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    // if(message.type === 'JOIN') {
    //     messageElement.classList.add('event-message');
    //     message.content = message.login + ' joined!';
    // } else if (message.type === 'LEAVE') {
    //     messageElement.classList.add('event-message');
    //     message.content = message.login + ' left!';
    // } else {
    // var permissions = getPermissions(login);
    if(permissions != null) {
        messageElement.classList.add('chatMessage');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.login);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.login);

        messageElement.appendChild(avatarElement);

        var loginElement = document.createElement('span');
        var loginText = document.createTextNode(message.login);
        loginElement.appendChild(loginText);
        messageElement.appendChild(loginElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

document.querySelector('#loginForm').addEventListener('submit', connect, true)
document.querySelector('#messageForm').addEventListener('submit', sendMessage, true)