$(window).load(function(){
  let map;
  function initTrails() {
    var mapOptions = {
      disableDoubleClickZoom: true,
      zoom: 7,
      center: new google.maps.LatLng(47.64, 107.061),
      mapTypeId: 'terrain'
    };

    map = new google.maps.Map(document.getElementById('map'), mapOptions);
    fetchTrails();
  }
  function fetchTrails(){
    fetch('/article').then((response) => {
      return response.json();
    }).then((articles) => {
      articles.forEach((article) => {
        console.log(article);
        console.log(article.authors);
        console.log(article.tags);
        console.log(article.header);
        console.log(article.body);
        createTrailForDisplay(article.authors, article.tags, article.header, article.body, article.coordinates)
      });
    });
  }
  /** Creates a trail on map. */
  function createTrailForDisplay(authors, tags, header, body, coordinates){
    console.log(coordinates);
    let arr = coordinates.split(',');
    console.log(arr.length / 2);
    let trail = new google.maps.MVCArray();
    for (let i = 0; i < arr.length; i+=2) {
      trail.push(new google.maps.LatLng(parseFloat(arr[i]), parseFloat(arr[i + 1])));
    }
    const poly = new google.maps.Polyline({
      path: trail,
      editable: false,
      strokeColor: '#FF0000',
      strokeOpacity: 1.0,
      strokeWeight: 2,
      map: map
    });
    poly.setMap(map);
    //click event -> shows up Article on the left side
    google.maps.event.addListener(poly, 'click', function() {
      pathInfo(authors, tags, header, body);
    });
  }
  function pathInfo(authors, tags, header, body){
    const containerDiv = document.getElementById('pathinfo');
    containerDiv.innerHTML = '';
    const h3 = document.createElement('h3');
    h3.innerHTML = 'Trail info';
    containerDiv.appendChild(h3);

    var ul = document.createElement('ul');
    ul.setAttribute('id', 'path');
    var li_authors = document.createElement('li');
    li_authors.innerHTML = 'Authors: ' + authors[0];
    ul.appendChild(li_authors);

    var li_tags = document.createElement('li');
    li_tags.innerHTML = 'Tags: ' + tags[0];
    ul.appendChild(li_tags);

    var li_header = document.createElement('li');
    li_header.innerHTML = 'Header: ' + header;
    ul.appendChild(li_header);

    var li_body = document.createElement('li');
    li_body.innerHTML = 'Body: ' + body;
    ul.appendChild(li_body);

    containerDiv.appendChild(ul);
  }
  initTrails();

});
