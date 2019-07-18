var map;
var markerCluster;
$(window).load(function(){
  var markers = [];
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
    console.log(markers);
    markerCluster = new MarkerClusterer(map, markers,{imagePath: 'images/marker-cluster-icons'});


  }
  function fetchTrails(){
    fetch('/articles').then((response) => {
      return response.json();
    }).then((articles) => {
      articles.forEach((article) => {
        if(article.coordinates.length != 0){
          createTrailForDisplay(article.authors, article.tags, article.header, article.body, article.coordinates, article.id);
        }
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
    markers.push(marker);
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

    var containerDiv = pathInfo(authors, tags, header, body, id);
    poly.addListener('mouseover',function(){
      infowindow.close();
    });
    poly.addListener('mouseout',function(){
      infowindow.open(map);
    });
    marker.addListener('click', function(){
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
    containerDiv.classList.add("content");
    containerDiv.innerHTML = '';

    const h3 = document.createElement('h3');
    h3.innerHTML = header;
    containerDiv.appendChild(h3);

    var li_authors = document.createElement('div');
    li_authors.innerHTML = '<i><sub>Posted by ' + authors[0]+'</sub></i>';
    containerDiv.appendChild(li_authors);

    var hr = document.createElement('hr');
    containerDiv.appendChild(hr);



    var li_body = document.createElement('p');
    if(body.length>=30){
    li_body.innerHTML = body;
    }
    else{
      li_body.innerHTML = body.substring(0,30);
    }

    var li_link = document.createElement('a');
    li_link.href = '/article?id='+id;
    li_link.style.color="black";
    li_link.appendChild(li_body);

    containerDiv.appendChild(li_link);
    return containerDiv;
  }
  initTrails();

});
