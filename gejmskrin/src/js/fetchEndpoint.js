const fetchEndpoint = (() => {
  'use strict';
  return (endpoint) => fetch(`http://localhost:8001/${endpoint}`)
    .then(response => response.json())
    .catch(error => { console.error(error) });
})();