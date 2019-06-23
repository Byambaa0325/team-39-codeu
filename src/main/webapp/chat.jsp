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
          <form action="/chat/new/" method="POST">
            <input type="text" name="nickname" placeholder="Nickname">
            <input type="text" name="invitee" placeholder="Invitee email(s)">
            <button>Submit</button>
          </form>
        </div>
      </div>
    </div>

    <div id="chat-wrapper">
      <div id="conversations">
        <div style="border-bottom: 1px black solid">Conversations</div>
        <div id="conversation-wrapper"></div>
      </div>
      <div id="chat"></div>
    </div>

    <script src="/js/chat.js"></script>
  </body>
</html>