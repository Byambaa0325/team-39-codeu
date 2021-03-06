<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"> 
    <title>Chat</title>
    <script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/chat.css">
    
  </head>
  <body onload="buildChat()">
    <div id="navbar">
      <%@ include file="navbar.jsp" %>
    </div>
    <div class="container">
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
        <div id="conversations" class="col-md-2">
          <div style="border-bottom: 1px black solid">Conversations</div>
          <div id="conversation-wrapper"></div>
        </div>
        <div id="chat" class="col-md-10">
          <div id="chat-container"></div>
        </div>
      </div>
      <script src="/js/chat.js"></script>
      <script src="/js/navbar.js"></script>
    </div>
  </body>
</html>