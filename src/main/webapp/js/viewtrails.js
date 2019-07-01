$(window).load(function(){
  function initTrails() {
    var mapOptions = {
      disableDoubleClickZoom: true,
      zoom: 7,
      center: new google.maps.LatLng(47.64, 107.061),
      mapTypeId: 'terrain'
    };

    var map = new google.maps.Map(document.getElementById('map'), mapOptions);

    var destinations = new google.maps.MVCArray();
    destinations.push(new google.maps.LatLng(47.64, 107.061)); //icn
    destinations.push(new google.maps.LatLng(47.8522, 106.759));
    destinations.push(new google.maps.LatLng(47.920, 106.941));
    destinations.push(new google.maps.LatLng(47.926079, 106.953844));
    destinations.push(new google.maps.LatLng(47.77, 107.154));
    destinations.push(new google.maps.LatLng(48.14, 108.26));

    var polyline = new google.maps.Polyline({
      path: destinations,
      editable: false,
      strokeColor: '#FF0000',
      strokeOpacity: 1.0,
      strokeWeight: 2,
      map: map
    });

    polyline.setMap(map);
  }
  initTrails();

});
