function createNewConversation(){
  let nicknameDom = document.getElementById('new-conv-nickname');
  let inviteesDom = document.getElementById('new-conv-invitees');

  console.log('Doing POST');

  fetch( '/chat/new/', {
    method: 'POST',
    body: JSON.stringify({
      nickname: nicknameDom.value, 
      invitee:inviteesDom.value
    })
  }).then(() => {
    nicknameDom.value = '';
    inviteesDom.value = '';
    buildConversations();
  });
}

function buildConversations( targetDom = document.getElementById('conversation-wrapper') ){
  targetDom.innerHTML = '';
  fetch( 'chat/conversations/' )
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

      for( let conv of data ){
        targetDom.appendChild( buildConversationDom(conv) );
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
  dom.id = conv.id;

  return dom;
}

function buildChat(){
  buildConversations();
}