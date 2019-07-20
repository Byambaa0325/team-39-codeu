<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<%@ page errorPage="error404.jsp" %>
<!DOCTYPE html>
<html>
  <head>
    <% Date date = new Date((long)request.getAttribute("timestamp"));
    %>
    <meta name="viewport" content="width=device-width, initial-scale=1">
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
        <title><%= request.getAttribute("header") %></title>
        <script src="js/navigation-loader.js"></script>
        <script src="js/comments.js"></script>
        <link rel="stylesheet" href="/css/article.css">
          <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDu9yGg9xNDrRaTumeiIWyOeUzdF5xISu4"
            async defer></script>
            <script src="https://code.jquery.com/jquery-1.12.3.min.js"></script>
            <style>
            #mapArticle{
              height: 600px;
              width: 100%;
            }

            body{
              padding-top:100px
            }
            a {
              color: #fff;
            }

            .navbar {
              background-color: #297fb8;
              border-bottom: 1px solid #5398c6;
              padding: 1% 0;
            }

            .navbar-brand {
              min-height: 55px;
              padding: 0 15px 5px;
            }

            .navbar-nav li {
              padding-right: 20px;
            }

            .navbar-inverse .navbar-nav li a {
              color: #fff;
            }


            .navbar-inverse .navbar-nav li a:hover {
              background-color: #5398c6;
            }

            .navbar-header {
              padding-bottom: 5px;
            }

            .navbar-inverse .navbar-collapse, .navbar-inverse .navbar-form {
              border-color: #5398c6;
            }

            .navbar-inverse .navbar-toggle {
              border-color: #5398c6;
            }

            .navbar-inverse .navbar-toggle:hover, .navbar-inverse .navbar-toggle:focus {
              background-color: #5398c6;
            }
            </style>
          </head>
          <body onload="addLoginOrLogoutLinkToNavigation();">
            <nav class="navbar navbar-inverse navbar-fixed-top">
              <div class="container-fluid">
                <div class="navbar-header">
                  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                  </button>
                  <a href="#" class="navbar-brand"></a>
                </div><!---End of navbar header-->
                <div class="collapse navbar-collapse" id="myNavbar">
                  <ul id="navigation" class="nav navbar-nav navbar-right">
                    <li><a href="index.html">Home</a></li>
                    <li><a href="explore.html">Explore</a></li>
                  </ul>
                </div><!--End of navbar collapse--->
              </div><!---End of container-->
            </nav><!-- end of nav-->
            <!-- Page Content -->
            <div class="container">

              <div class="row">

                <!-- Post Content Column -->
                <div class="col-lg-8">

                  <!-- Title -->
                  <h1 class="mt-4"><%= request.getAttribute("header") %></h1>

                  <!-- Author -->
                  <p class="lead">
                    by
                    <%= request.getAttribute("authors") %>
                  </p>
                  <p>
                    Tags: <%= request.getAttribute("tags") %>
                  </p>

                  <hr>

                    <!-- Date/Time -->
                    <p>Posted on <%= date.toString() %></p>

                    <hr>

                      <!-- Preview Map -->
                      <% if(((String) request.getAttribute("coordinates")).length() != 0){%>
                      <div id="mapArticle"></div>
                      <%}%>

                      <hr>

                        <!-- Post Content -->
                        <p class="lead"><%= request.getAttribute("body") %></p>

                        <hr>

                        </div>

                      </div>
                      <!-- /.row -->
                      <div class="row">
                        <div class="col-lg-8">
                          <div id="comment-form" class="hidden">
                            Enter a new comment:
                            <br/>
                            <textarea name="text" id="comment-input"></textarea>
                            <br/>
                            <button onclick="postComment('<%= request.getAttribute("id")%>')">Submit</button>
                          </div>
                          <div id="comments-container">
                            No Comments Yet!
                          </div>
                        </div>
                      </div>
                    </div>
                    <!-- /.container -->
                  </body>
                  <% if(((String) request.getAttribute("coordinates")).length() != 0){%>
                  <script>
                  $(window).load(function(){
                    let map;
                    function initTrails() {
                      var mapOptions = {
                        disableDoubleClickZoom: true,
                        zoom: 7,
                        center: new google.maps.LatLng(<%= ((String)request.getAttribute("coordinates")).split(",")[0]%>, <%= ((String)request.getAttribute("coordinates")).split(",")[1]%>),
                        mapTypeId: 'terrain'
                      };

                      map = new google.maps.Map(document.getElementById('mapArticle'), mapOptions);
                      createTrailForDisplay("<%= request.getAttribute("coordinates")%>");

                    }
                    /** Creates a trail on map. */
                    function createTrailForDisplay(coordinates){
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
                      })

                      poly.setMap(map);

                    }

                    initTrails();

                  });

                  </script>
                  <%}%>
                  <script>
                  fetchComments("comments-container","<%= request.getAttribute("id")%>");
                  </script>
                </html>
