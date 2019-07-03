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
    fetch('/trails').then((response) => {
      return response.json();
    }).then((trails) => {
      trails.forEach((trail) => {
       createTrailForDisplay(trail.coordinates)
      });
    });
  }
  /** Creates a trail on map. */
  function createTrailForDisplay(coordinates){
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
    //click event -> jump to Article
    //........
  }
  initTrails();

});
