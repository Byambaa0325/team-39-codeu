/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById('page-title').innerText = parameterUsername;
  document.title = parameterUsername + ' - User Page';
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showMessageFormIfViewingSelf() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn &&
            loginStatus.username == parameterUsername) {
          const messageForm = document.getElementById('message-form');
          messageForm.classList.remove('hidden');

          const aboutMeForm = document.getElementById('about-me-form');
          aboutMeForm.classList.remove('hidden');
        }
      });
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const url = '/messages?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          const messageDiv = buildMessageDiv(message);
          messagesContainer.appendChild(messageDiv);
        });
      });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(document.createTextNode(
      message.user + ' - ' + new Date(message.timestamp)));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

/* Fetches About me of the user */
function fetchAboutMe() {
  const url = '/about?user=' + parameterUsername;

  fetch(url).then((response) => {
    return response.text();
  }).then((aboutMe) => {
    const aboutMeContainer = document.getElementById('about-me-container');
    if (aboutMe == '') {
      aboutMe = 'This user has not entered any information yet.';
    }
    aboutMeContainer.innerHTML = aboutMe;
  });
  }
  function fetchArticles(){
    const querystring = window.location.search;
    const name = querystring.slice(querystring.indexOf("user=")+5);
    var containerDiv = document.getElementById("article-container");
    fetch('/articles?user='+name).then((response) => {
      return response.json();
    }).then((articles) => {
      console.log(articles)
      articles.forEach((article) => {
        const articlePanel = buildArticlePanel(article);
        containerDiv.appendChild(articlePanel);
    });
  });
}

  function buildArticlePanel(article){
    const containerDiv = document.createElement("div");
    containerDiv.classList.add("list-group-item");
    containerDiv.innerHTML = '';

    const h3 = document.createElement('h4');
    h3.innerHTML = article.header;
    h3.classList.add("list-group-heading");
    containerDiv.appendChild(h3);

    var li_link = document.createElement('a');
    li_link.href = '/article?id='+article.id;
    li_link.classList.add("list-group-text");
    li_link.style.color="black";


    var li_date = document.createElement('div');
    var date = new Date(article.timestamp);
    li_date.innerHTML = '<sub>Posted on ' + date.toDateString()+'</sub>';
    li_link.appendChild(li_date);

    var li_body = document.createElement('div');
    li_body.innerHTML = article.body.substring(0,50);
    li_link.appendChild(li_body);



    containerDiv.appendChild(li_link);

    return containerDiv;
  }
/** Fetches data and populates the UI of the page. */
function buildUI() {
  setPageTitle();
  showMessageFormIfViewingSelf();
  fetchMessages();
  fetchAboutMe();
  fetchArticles();
}
