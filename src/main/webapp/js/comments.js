// Fetch comments and add them to the page.
function fetchComments(container, articleId){
  const url = '/comment?id='+articleId;
  fetch(url).then((response) => {
    return response.json();
  }).then((comments) => {
    const commentContainer = document.getElementById(container);
    if(comments.length == 0){
      commentContainer.innerHTML = '<p>There are no comments yet.</p>';
    }
    else{
      commentContainer.innerHTML = '';
    }
    comments.forEach((comment) => {
      const commentDiv = buildCommentDiv(comment);
      commentContainer.appendChild(commentDiv);
    });
  });
  showCommentFormIfLoggedIn();
}

function buildCommentDiv(comment){
  const usernameDiv = document.createElement('div');
  usernameDiv.classList.add("left-align");
  usernameDiv.appendChild(document.createTextNode(comment.user));

  const timeDiv = document.createElement('div');
  timeDiv.classList.add('right-align');
  timeDiv.appendChild(document.createTextNode(new Date(comment.timestamp)));

  const headerDiv = document.createElement('div');
  headerDiv.classList.add('comment-header');
  headerDiv.appendChild(usernameDiv);
  headerDiv.appendChild(timeDiv);

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('comment-body');
  bodyDiv.appendChild(document.createTextNode(comment.text));

  const commentDiv = document.createElement('div');
  commentDiv.classList.add("comment-div");
  commentDiv.appendChild(headerDiv);
  commentDiv.appendChild(bodyDiv);

  return commentDiv;
}

function postComment(articleId){
  var textarea = document.getElementById("comment-input");
  const text = textarea.value;
  const params = new URLSearchParams();
  params.append("id",articleId);
  params.append("text",text);
  fetch('/comment', {
    method: 'POST',
    body: params
  }).then(function(response){
    fetchComments('comments-container',articleId);
    textarea.value="";
  });
}

/**
* Shows the comment form if the user is logged in and viewing their own page.
*/
function showCommentFormIfLoggedIn() {
  fetch('/login-status')
  .then((response) => {
    return response.json();
  })
  .then((loginStatus) => {
    if (loginStatus.isLoggedIn) {
      const commentForm = document.getElementById('comment-form');
      commentForm.classList.remove('hidden');
    }
  });
}
