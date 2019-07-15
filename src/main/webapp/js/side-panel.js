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
  var date = new Date(article.timestamp*1000);
  containerDiv.innerHTML += date.toDateString();
  return containerDiv;
}

function fetchForum(countryName){
  fetch('/forumJSON?country='+countryName).then((response) => {
    return response.json();
  }).then((articles) => {
    articles.forEach((article) => {
      buildArticleDiv(article)
    });
  });
}

const sidePanelForum = document.getElementById("")
