var map;
$(window).load(function(){
  var geocoder = new google.maps.Geocoder;
  function initTrails() {
    var mapOptions = {
      disableDoubleClickZoom: true,
      zoom: 7,
      center: new google.maps.LatLng(47.64, 107.061),
      mapTypeId: 'terrain',
      minZoom: 3,
      restriction: {
        latLngBounds: {
          north: 85,
          south: -85,
          west: -180,
          east: 180
          }
        }
      };

      map = new google.maps.Map(document.getElementById('mapFull'), mapOptions);
      fetchTrails();

      /* Attach geolocater to map's dragend event to track where user's viewing location*/
      map.addListener('dragend', function() {
        //Get center coordinate
        var latlng = map.getCenter().toJSON();
        //Send reverse-geolocation request
        geocoder.geocode({'location': latlng}, function(results, status) {
          if (status === 'OK') {
            //update several elements with country name
            if (results[0]) {
              var currentLocationElement = document.getElementById('current-location');
              var currentLocationForumLink = document.getElementById('side-panel-forumlink');
              var country = ""

              //Find country name from components of address returned
              results[0].address_components.forEach((component) => {
                if (component.types.toString().includes('country')){
                  country = component.long_name;
                }
              });

              //if we are in a country
              if(country != ""){
                currentLocationElement.innerHTML = "<a href = \"forum?country="+country.toLowerCase()+"\"><h1>"+country+"</h1></a>";
                currentLocationForumLink.innerHTML = "<a href = \"forum?country="+country.toLowerCase()+"\" style=\"color:darkslateblue\" ><em>"+"View Discussion in "+country+"</em></a>";
              }
              //update forum by the country
              fetchForum();
            } else {
              console.log('No results found');
            }
          } else {
            console.log('Geocoder failed due to: ' + status);
          }
        });
      });
    }

    function fetchTrails(){
      let loaderStatus = true;
      //Toggle loader on the page
      toggleLoader(loaderStatus, 'mapFull', 'loadDiv');
      fetch('/articles').then((response) => {
        return response.json();
      }).then((articles) => {
        articles.forEach((article) => {

          //only create markers for articles with trails
          if(article.coordinates.length != 0){
            createTrailForDisplay(article.authors, article.tags, article.header, article.body, article.coordinates, article.id);

            //After creating marker on map if loader is still on disable it
            //P.S: This is mildly inefficient since it needs to trigger only once but is checked on every trail
            if(loaderStatus){
              loaderStatus = false;
              toggleLoader(loaderStatus, 'mapFull', 'loadDiv');
            }
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
      const poly = new google.maps.Polyline({
        path: trail,
        editable: false,
        strokeColor: '#FF0000',
        strokeOpacity: 1.0,
        strokeWeight: 6
      });

      var infowindow = new google.maps.InfoWindow({
        content: "",
        pixelOffset: new google.maps.Size(0, -40)
      });

      var containerDiv = pathInfo(authors, tags, header, body, id);

      //Hide trail on click on the trail
      poly.addListener('click',function(){
        poly.setMap(null);
      });

      //infowindow on hovering a marker
      marker.addListener('mouseover', function(){
        infowindow.setContent(containerDiv.outerHTML);
        infowindow.setPosition(startingPoint);
        infowindow.open(map);
      });

      //close the infowindow opened by hover
      marker.addListener('mouseout', function(){
        infowindow.close();
      });

      //Set details on pane and pan to the markers location.
      marker.addListener('click', function(){
        poly.setMap(map);
        toggleArticleOnArticlePanel(id);
        $("#article-container").collapse('show');
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

      var li_body = document.createElement('div');
      if(body.length>=30){
        li_body.innerHTML = body.substring(0,30);
      }
      else{
        li_body.innerHTML = body;
      }

      var li_link = document.createElement('a');
      li_link.href = '/article?id='+id;
      li_link.style.color="black";
      li_link.innerHTML="<sub>Click to see more>>></sub>"
      containerDiv.appendChild(li_body);

      containerDiv.appendChild(li_link);
      return containerDiv;
    }
    initTrails();

  });
