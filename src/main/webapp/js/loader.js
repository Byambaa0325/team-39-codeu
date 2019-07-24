/*
  To use add loadDiv anywhere in your html. link loader.css to your page.
  wrap the content you hide during the loading in div with id bodyContainer.
  use toggleLoader from js.
*/
function toggleLoader(status, elementToHide, loader){
  if(status == true){
    document.getElementById(elementToHide).style.display="none";
    document.getElementById(loader).style.display="block";
  }
  else{
    document.getElementById(elementToHide).style.display="block";
    document.getElementById(loader).style.display="none";
  }
}
