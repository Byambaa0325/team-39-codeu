
var sidePanelForum;
var locationDiv;

function buildArticleDiv(article){
  const containerDiv = document.createElement("div");
  containerDiv.classList.add('pathinfo');
  containerDiv.innerHTML = '';

  const link = document.createElement("a");
  link.href ="/article?id="+article.id;
  const header = document.createElement('h3');
  header.innerHTML = article.header;
  link.appendChild(header);
  containerDiv.appendChild(link);

  const author = document.createElement("sub");
  author.innerHTML = article.authors[0];
  containerDiv.appendChild(author);
  var date = new Date(article.timestamp);
  containerDiv.innerHTML += date.toDateString();
  return containerDiv;
}

function fetchForum(){
  sidePanelForum.innerHTML = "";
  var countryName = locationDiv.childNodes[0].childNodes[0].innerHTML.toLowerCase();
  fetch('/forumJSON?country='+countryName).then((response) => {
    return response.json();
  }).then((articles) => {
    articles.forEach((article) => {
      const articleDiv = buildArticleDiv(article)
      sidePanelForum.appendChild(articleDiv);
    });
  });
}

$(window).load(function(){


function initForumPanel(){
  fetchForum();
}
sidePanelForum = document.getElementById("forum-content");
locationDiv = document.getElementById("current-location");

initForumPanel();
});
