<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
<script src="scrolling.js"></script>
<script src="/js/navbar.js"></script>
<link rel="stylesheet" href="style.css">
<script type="text/javascript">
  $(document).on('click','.navbar-collapse.in',function(e) {
    if( $(e.target).is('a') && ( $(e.target).attr('class') != 'dropdown-toggle' ) ) {
      $(this).collapse('hide');
    }
  });
</script>

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
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#home">Home</a></li>
        <li><a href="#about">About</a></li>
        <li><a href="#features">Features</a></li>
        <li><a href="#connect">Connect</a></li>
      </ul>
    </div><!--End of navbar collapse--->
  </div><!---End of container-->
</nav><!-- end of nav-->