function createNewConversation(){
  let nicknameDom = document.getElementById('new-conv-nickname');
  let inviteesDom = document.getElementById('new-conv-invitees');

  console.log('Doing POST');

  fetch( '/chat/new/conversation/', {
    method: 'POST',
    body: JSON.stringify({
      nickname: nicknameDom.value, 
      invitee:inviteesDom.value
    })
  }).then(() => {
    nicknameDom.value = '';
    inviteesDom.value = '';
    setTimeout( buildConversations(), 500);
  });
}

function buildConversations(){
  let conversatoinDom = document.getElementById('conversation-wrapper');
  let chatDom = document.getElementById('chat-container');
  fetch( 'chat/get/conversations/' )
    .then( response => response.json() )
    .then( data => {
      // Sort the conversations by the most recent
      data.sort( (first, second) => {
        if(first.latestTime > second.latestTime){
          return -1;
        } else {
          return 1;
        }
      });

      conversatoinDom.innerHTML = '';
      for( let conv of data ){
        conversatoinDom.appendChild( buildConversationDom(conv) );
        chatDom.appendChild( buildChatDom(conv) );
        console.log( 'Created :', conv.nickname );
      }
    });
}

function buildConversationDom( conv ){
  let dom = document.createElement('div');
  dom.innerHTML = `
    <h4 class="conv-nickname">${conv.nickname}</h4>
    <p class="conv-date">${new Date(conv.latestTime).toLocaleString()}</p>
  `;
  dom.classList.add('conversation');
  dom.onclick = () => {
    showChat(conv.id);
    loadChat(conv.id);
  }
  dom.id = conv.id;

  return dom;
}

function buildChatDom( conv ){
  let dom = document.createElement('div');
  dom.classList.add('chat');
  dom.style.display = 'none';
  dom.id = `chat-${conv.id}`;
  dom.innerHTML = `
    <div style="height: 760px; overflow-y: scroll;">
      <div class="chat-header">${conv.nickname}</div>
      <div class="chat-container">Loading...</div>
    </div>
    <div id="message-input">
      <input type="text" id="message-convid" value="${conv.id}" hidden>
      <input type="text" id="message-message">
      <button onclick="sendMessage('${conv.id}')">Send</button>
    </div>
  `;
  return dom;
}

function loadChat( id ){
  let dom = document.getElementById(`chat-${id}`);
  if( dom == null ) return;

  let domChatEl = dom.getElementsByClassName('chat-container')[0];
  fetch( `/chat/get/messages/?convid=${id}` )
    .then( response => response.json() )
    .then( data => {
      data.sort( (a, b) => a.timestamp < b.timestamp ? -1 : 1 );
      domChatEl.innerHTML = '';
      for( let message of data ){
        let currDom = buildMessageDom(message);
        domChatEl.appendChild(currDom);
      }
    });
}

function showChat( id ){
  console.log( `Showing : ${id}` );
  let chatDoms = document.getElementsByClassName('chat');
  for( let chatDom of chatDoms ){
    chatDom.style.display = 'none';
  }
  document.getElementById(`chat-${id}`).style.display = 'block';
  document.getElementById('message-convid').value = id;
}

function buildMessageDom( message ){
  let dom = document.createElement('div');
  dom.classList.add('chat-message-wrapper');
  dom.innerHTML = `
    <p class="chat-user">${message.user}</p>
    <p class="chat-message">${message.message}</p>
    <p class="chat-date">${new Date(message.timestamp).toLocaleString()}</p>
  `
  return dom;
}

function sendMessage(){
  let msgInputDom = document.getElementById('message-message');
  let convid = document.getElementById('message-convid').value;

  console.log('Sending message');

  fetch( '/chat/new/message/', {
    method: 'POST',
    body: JSON.stringify({
      convid: convid, 
      message: msgInputDom.value
    })
  }).then(() => {
    msgInputDom.value = '';
    loadChat( convid );
  });
}


function buildChat(){
  buildConversations();
}