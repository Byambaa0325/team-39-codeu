var sidePanelForum;
var locationDiv;
var sidePanelArticle;

function toggleArticleOnArticlePanel(id){
  fetch('/articleJSON?id='+id).then((response) => {
    return response.json();
  }).then((article) => {
    if (article.coordinates.length != 0){
      toggleMapCoordinates(article.coordinates);
    }
    const articlePanel = buildArticlePanel(article);
    sidePanelArticle.innerHTML = articlePanel.outerHTML;
  });
}

function toggleMapCoordinates(coords){
  var coordsComp = coords.split(",");
  map.panTo(new google.maps.LatLng(coordsComp[0],coordsComp[1]));
  map.setZoom(12);
}

function buildArticlePanel(article){
  const containerDiv = document.createElement("div");
  containerDiv.classList.add("content");
  containerDiv.innerHTML = '';

  const h3 = document.createElement('h1');
  h3.innerHTML = article.header;
  containerDiv.appendChild(h3);

  var li_authors = document.createElement('em');
  li_authors.style.fontSize = "60%";
  li_authors.innerHTML = 'by ' + article.authors+'';
  containerDiv.appendChild(li_authors);

  var li_date = document.createElement('div');
  var date = new Date(article.timestamp);
  li_date.innerHTML = '<sub>Posted on ' + date.toDateString()+'</sub>';
  containerDiv.appendChild(li_date);

  var li_tags = document.createElement('div');
  li_tags.innerHTML = '<i><sub>Tags: ' + article.tags+'</sub></i>';
  containerDiv.appendChild(li_tags);

  var hr1 = document.createElement('hr');
  var hr2 = document.createElement('hr');
  containerDiv.appendChild(hr1);

  var li_body = document.createElement('div');
  li_body.innerHTML = article.body;
  containerDiv.appendChild(li_body);

  containerDiv.appendChild(hr2);

  var li_link = document.createElement('a');
  li_link.href = '/article?id='+article.id;
  li_link.style.color="black";
  li_link.innerHTML = "See full>>>"
  containerDiv.appendChild(li_link);

  return containerDiv;
}
function buildArticleDiv(article){
  const containerDiv = document.createElement("div");
  containerDiv.style.border="1px solid black";
  containerDiv.classList.add('pathinfo');
  containerDiv.setAttribute("data-toggle", "collapse");
  containerDiv.setAttribute("data-target", "#article-container");
  containerDiv.setAttribute("aria-expanded", "false");
  containerDiv.setAttribute("aria-controls", "article-container");
  containerDiv.setAttribute("onclick", "toggleArticleOnArticlePanel('"+article.id+"')");
  containerDiv.innerHTML = '';

  const header = document.createElement('h3');
  header.innerHTML = article.header;
  containerDiv.appendChild(header);

  const author = document.createElement("small");
  author.innerHTML = article.authors[0];
  containerDiv.appendChild(author);
  var date = new Date(article.timestamp);
  containerDiv.innerHTML += "<br><sub>"+date.toDateString()+"</sub>";
  return containerDiv;
}

function fetchForum(){
  sidePanelForum.innerHTML = "";
  var countryName = locationDiv.childNodes[0].childNodes[0].innerHTML.toLowerCase();
  fetch('/forumJSON?country='+countryName).then((response) => {
    return response.json();
  }).then((articles) => {
    articles.forEach((article) => {
      if (article.coordinates.length != 0){
        const articleDiv = buildArticleDiv(article)
        sidePanelForum.appendChild(articleDiv);
      }
    });
  });
}

$(window).load(function(){
  function initForumPanel(){
    fetchForum();
  }
  sidePanelForum = document.getElementById("forum-content");
  sidePanelArticle = document.getElementById("article-content");
  locationDiv = document.getElementById("current-location");

  initForumPanel();
});
