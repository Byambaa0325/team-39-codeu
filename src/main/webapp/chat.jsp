<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Chat</title>
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/chat.css">
  </head>
  <body onload="buildChat()">
    <h3>Welcome to chat!</h3>
    <div>
      <div class="dropdown">
        <button class="dropbtn">New conversation</button>
        <div class="dropdown-content">
          <div>
            <input id="new-conv-nickname" type="text" placeholder="Nickname">
            <input id="new-conv-invitees" type="text" placeholder="Invitee(s) email(s)">
            <button onclick="createNewConversation()">Submit</button>
          </div>
        </div>
      </div>
    </div>

    <div id="chat-wrapper">
      <div id="conversations">
        <div style="border-bottom: 1px black solid">Conversations</div>
        <div id="conversation-wrapper"></div>
      </div>
      <div id="chat">
        <div id="chat-container"></div>
        <div id="message-input" class="message-input">
          <input type="text" id="message-convid" value="" hidden>
          <input type="text" id="message-message">
          <button onclick="sendMessage()">Send</button>
        </div>
      </div>
    </div>

    <script src="/js/chat.js"></script>
  </body>
</html>