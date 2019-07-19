// Fetch messages and add them to the page.
function fetchMessages(container){
  const url = '/feed';
  fetch(url).then((response) => {
    return response.json();
  }).then((messages) => {
    const messageContainer = document.getElementById(container);
    if(messages.length == 0){
      messageContainer.innerHTML = '<p>There are no posts yet.</p>';
    }
    else{
      messageContainer.innerHTML = '';
    }
    messages.forEach((message) => {
      const messageDiv = buildMessageDiv(message);
      messageContainer.appendChild(messageDiv);
    });
  });
  showMessageFormIfLoggedIn();
}

function buildMessageDiv(message){
  const usernameDiv = document.createElement('div');
  usernameDiv.classList.add("left-align");
  usernameDiv.appendChild(document.createTextNode(message.user));

  const timeDiv = document.createElement('div');
  timeDiv.classList.add('right-align');
  timeDiv.appendChild(document.createTextNode(new Date(message.timestamp)));

  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(usernameDiv);
  headerDiv.appendChild(timeDiv);

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.appendChild(document.createTextNode(message.text));

  const messageDiv = document.createElement('div');
  messageDiv.classList.add("message-div");
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

function postMessage(){
  var textarea = document.getElementById("message-input");
  const text = textarea.value;
  console.log(text);
  const params = new URLSearchParams();
  params.append("text",text);
  fetch('/messages', {
    method: 'POST',
    body: params
  }).then(function(response){
    fetchMessages('chat-content');
    textarea.value="";
  });
}

/**
* Shows the message form if the user is logged in and viewing their own page.
*/
function showMessageFormIfLoggedIn() {
  fetch('/login-status')
  .then((response) => {
    return response.json();
  })
  .then((loginStatus) => {
    if (loginStatus.isLoggedIn) {
      const messageForm = document.getElementById('message-form');
      messageForm.classList.remove('hidden');
    }
  });
}

// Fetch data and populate the UI of the page.
function buildUI(){
  addLoginOrLogoutLinkToNavigation();
  fetchMessages('message-container');

}
