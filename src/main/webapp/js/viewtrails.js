$(window).load(function(){
  let map;
  var geocoder = new google.maps.Geocoder;
  function initTrails() {
    var mapOptions = {
      disableDoubleClickZoom: true,
      zoom: 7,
      center: new google.maps.LatLng(47.64, 107.061),
      mapTypeId: 'terrain'
    };

    map = new google.maps.Map(document.getElementById('mapFull'), mapOptions);
    map.addListener('dragend', function() {
      var latlng = map.getCenter().toJSON();
      geocoder.geocode({'location': latlng}, function(results, status) {
        if (status === 'OK') {
          if (results[0]) {
            var currentLocationElement = document.getElementById('current-location');
            var country = ""
            results[0].address_components.forEach((component) => {
              if (component.types.toString().includes('country')){
                country = component.long_name;
              }
            });
            if(country != ""){
              currentLocationElement.innerHTML = "<a href = \"forum?country="+country.toLowerCase()+"\"><h1>"+country+"</h1></a>";
            }
            fetchForum();
          } else {
            console.log('No results found');
          }
        } else {
          console.log('Geocoder failed due to: ' + status);
        }
      });

    });
    fetchTrails();

  }
  function fetchTrails(){
    fetch('/articles').then((response) => {
      return response.json();
    }).then((articles) => {
      articles.forEach((article) => {
        createTrailForDisplay(article.authors, article.tags, article.header, article.body, article.coordinates, article.id);
      });
    });
  }
  /** Creates a trail on map. */
  function createTrailForDisplay(authors, tags, header, body, coordinates, id){
    let arr = coordinates.split(',');
    let trail = new google.maps.MVCArray();
    for (let i = 0; i < arr.length; i+=2) {
      trail.push(new google.maps.LatLng(parseFloat(arr[i]), parseFloat(arr[i + 1])));
    }
    var startingPoint = {lat:parseFloat(arr[0]), lng:parseFloat(arr[1])};
    var marker = new google.maps.Marker({
      position: startingPoint,
      map: map
    });
    const poly = new google.maps.Polyline({
      path: trail,
      editable: false,
      strokeColor: '#FF0000',
      strokeOpacity: 1.0,
      strokeWeight: 2
    });

    var infowindow = new google.maps.InfoWindow({
        content: ""
      });

    var containerDiv = pathInfo(authors, tags, header, body);
    marker.addListener('mouseover', function(){
      poly.setMap(map);
      infowindow.setContent(containerDiv.outerHTML);
      infowindow.setPosition(startingPoint);
      infowindow.addListener('closeclick',function(){
        poly.setMap(null);
        console.log("close Clicked");
      });
      infowindow.open(map);
      console.log("Clicked on trail");
    });
  }
  function pathInfo(authors, tags, header, body, id){
    const containerDiv = document.createElement("div");
    containerDiv.id = 'pathinfo';
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

    var li_link = document.createElement('a');
    li_link.href = '/article?id='+id;
    li_link.style.color="black";
    li_link.innerHTML = "Read more...";
    ul.appendChild(li_link);

    containerDiv.appendChild(ul);
    return containerDiv;
  }
  initTrails();

});
