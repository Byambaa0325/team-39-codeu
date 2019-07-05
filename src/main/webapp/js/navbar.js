function getNavbar( dom ){
  fetch('/navbar')
    .then( response => response.text() )
    .then( text => {
      dom.innerHTML = text;
    });
}