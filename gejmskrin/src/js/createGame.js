const createGame = (function () {
  'use strict';
  return () => fetch('http://localhost:8001/createGame')
    .then(response => response.json())
    .then(data => {
      console.log(data);
    })
    .catch(error => { console.error(error) });
})();