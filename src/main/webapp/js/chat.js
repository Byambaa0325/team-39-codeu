function buildConversations(){
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

      data.forEach( conv => {
        document.getElementById( 'conversation-wrapper' )
          .appendChild( buildConversationDom(conv) );
        console.log( 'Created :', conv.nickname );
      });
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