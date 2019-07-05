$(window).load(function(){
  function initialize() {
    var mapOptions = {
      disableDoubleClickZoom: true,
      zoom: 8,
      center: new google.maps.LatLng(47.64, 107.061),
      mapTypeId: 'terrain'
    };

    var map = new google.maps.Map(document.getElementById('map'), mapOptions);

    var polyline = new google.maps.Polyline({
      editable: true,
      strokeColor: '#FF0000',
      strokeOpacity: 1.0,
      strokeWeight: 2,
      map: map
    });

    var deleteMenu = new DeleteMenu();

    google.maps.event.addListener(polyline, 'rightclick', function(e) {
      // Check if click was on a vertex control point
      if (e.vertex == undefined) {
        return;
      }
      deleteMenu.open(map, polyline.getPath(), e.vertex);
    });

    google.maps.event.addListener(map, 'dblclick', function(e) {
      var currentPath = polyline.getPath();
      currentPath.push(e.latLng);
    });

    const containerDiv = document.getElementById('savepath');
    const button = document.createElement('button');
    button.appendChild(document.createTextNode('Submit'));
    button.onclick = () => {
      postTrail(polyline);
    };
    containerDiv.appendChild(button);
  }

  function postTrail(poly) {
    //Article infos
    const header = document.getElementsByName("header")[0].value;
    const authors = document.getElementsByName("authors")[0].value;
    const tags = document.getElementsByName("tags")[0].value;
    const body = document.getElementsByName("body")[0].value;

    //trail coordinates
    var coordinate_poly = poly.getPath().getArray();
    var newCoordinates_poly = [];
    for (var i = 0; i < coordinate_poly.length; i++) {
      lat_poly = coordinate_poly[i].lat();
      lng_poly = coordinate_poly[i].lng();

      newCoordinates_poly.push(lat_poly);
      newCoordinates_poly.push(lng_poly);
    }
    // var str_coordinates_poly = JSON.stringify(newCoordinates_poly);
    // var json_poly = "{\"coordinates\":" + str_coordinates_poly + "}";
    // document.getElementById('json_polyline').value = json_poly;
    const params = new URLSearchParams();
    params.append('header', header);
    params.append('authors', authors);
    params.append('tags', tags);
    params.append('body', body);
    params.append('coordinates', newCoordinates_poly);
    console.log(newCoordinates_poly);
    fetch('/article', {
      method: 'POST',
      body: params
    });
    window.location.replace("/explore.html");
  }

  /**
   * A menu that lets a user delete a selected vertex of a path.
   * @constructor
   */
  function DeleteMenu() {
    this.div_ = document.createElement('div');
    this.div_.className = 'delete-menu';
    this.div_.innerHTML = 'Delete';

    var menu = this;
    google.maps.event.addDomListener(this.div_, 'click', function() {
      menu.removeVertex();
    });
  }
  DeleteMenu.prototype = new google.maps.OverlayView();

  DeleteMenu.prototype.onAdd = function() {
    var deleteMenu = this;
    var map = this.getMap();
    this.getPanes().floatPane.appendChild(this.div_);

    // mousedown anywhere on the map except on the menu div will close the
    // menu.
    this.divListener_ = google.maps.event.addDomListener(map.getDiv(), 'mousedown', function(e) {
      if (e.target != deleteMenu.div_) {
        deleteMenu.close();
      }
    }, true);
  };

  DeleteMenu.prototype.onRemove = function() {
    google.maps.event.removeListener(this.divListener_);
    this.div_.parentNode.removeChild(this.div_);

    // clean up
    this.set('position');
    this.set('path');
    this.set('vertex');
  };

  DeleteMenu.prototype.close = function() {
    this.setMap(null);
  };

  DeleteMenu.prototype.draw = function() {
    var position = this.get('position');
    var projection = this.getProjection();

    if (!position || !projection) {
      return;
    }

    var point = projection.fromLatLngToDivPixel(position);
    this.div_.style.top = point.y + 'px';
    this.div_.style.left = point.x + 'px';
  };

  /**
   * Opens the menu at a vertex of a given path.
   */
  DeleteMenu.prototype.open = function(map, path, vertex) {
    this.set('position', path.getAt(vertex));
    this.set('path', path);
    this.set('vertex', vertex);
    this.setMap(map);
    this.draw();
  };

  /**
   * Deletes the vertex from the path.
   */
  DeleteMenu.prototype.removeVertex = function() {
    var path = this.get('path');
    var vertex = this.get('vertex');

    if (!path || vertex == undefined) {
      this.close();
      return;
    }

    path.removeAt(vertex);
    this.close();
  };

  google.maps.event.addDomListener(window, 'load', initialize);
  initialize();
});
