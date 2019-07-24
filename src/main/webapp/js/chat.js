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
        buildChatDom( conv.id, dom => {
          chatDom.appendChild(dom);
          loadChat(conv.id);
        })
        // chatDom.appendChild( buildChatDom(conv.id) );
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

function buildChatDom( convid, callback ){
  fetch( `/chat/get/conversation/?convid=${convid}` )
    .then( resp => resp.json() )
    .then( conv => {
      let dom = document.createElement('div');
      dom.classList.add('chat');
      dom.style.display = 'none';
      dom.id = `chat-${conv.id}`;
      dom.innerHTML = `
        <div>
          <div class="chat-header">${conv.nickname}</div>
          <div class="chat-container">Loading...</div>
        </div>
        <div id="message-input">
          <input type="text" id="message-${conv.id}">
          <button onclick="sendMessage('${conv.id}')">Send</button>
        </div>
      `;
      callback(dom);
    });
}

function getUser( callback ) {
  fetch( '/login-status' )
    .then( resp => resp.json() )
    .then( data => callback(data) );
}

function loadChat( id ){
  console.log(`Loading : ${id}`);
  let dom = document.getElementById(`chat-${id}`);
  if( dom == null ) return;

  let domChatEl = dom.getElementsByClassName('chat-container')[0];
  getUser( user => {
    fetch( `/chat/get/messages/?convid=${id}` )
      .then( response => response.json() )
      .then( data => {
        data.sort( (a, b) => a.timestamp < b.timestamp ? -1 : 1 );
        domChatEl.innerHTML = '';
        for( let message of data ){
          let currDom = buildMessageDom(message, user.username);
          domChatEl.appendChild(currDom);
        }

        domChatEl.scrollTop = domChatEl.scrollTopMax;
      });
  })
}

var curChatId = '';
var curInterval = setInterval( () => {}, 1000000 );
function showChat( id ){
  if( curChatId ) {
    console.log(`Clearing : ${id}`);
    clearInterval(curInterval);
  }
  console.log( `Showing : ${id}` );
  let chatDoms = document.getElementsByClassName('chat');
  for( let chatDom of chatDoms ){
    chatDom.style.display = 'none';
  }
  document.getElementById(`chat-${id}`).style.display = 'block';

  curInterval = setInterval( `loadChat('${id}')`, 1000 );
  curChatId = id;
}

function buildMessageDom( message, username='' ){
  let dom = document.createElement('div');
  dom.classList.add('chat-message-wrapper');
  if( username == message.user ){
    dom.classList.add('ml-auto');
  }
  dom.innerHTML = `
    <p class="chat-user">${message.user}</p>
    <p class="chat-message">${message.message}</p>
    <p class="chat-date">${new Date(message.timestamp).toLocaleString()}</p>
  `
  return dom;
}

function sendMessage( convid ){
  let msgInputDom = document.getElementById(`message-${convid}`);

  console.log(`Sending message to ${convid}`);

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