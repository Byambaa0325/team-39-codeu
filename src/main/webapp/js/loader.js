/*
  To use add loadDiv anywhere in your html. link loader.css to your page.
  wrap the content you hide during the loading in div with id bodyContainer.
  use toggleLoader from js.
*/
function toggleLoader(status){
  if(status == true){
    document.getElementById('bodyContainer').style.display="none";
    document.getElementById('loadDiv').style.display="block";
  }
  else{
    document.getElementById('bodyContainer').style.display="block";
    document.getElementById('loadDiv').style.display="none";
  }
}
