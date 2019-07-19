fetch('/login-status')
    .then((response) => {
      return response.json();
    })
    .then((loginStatus) => {
      if (!loginStatus.isLoggedIn) {
        window.location.href="/login?redirectBack="+window.location.href;
      }
    });
