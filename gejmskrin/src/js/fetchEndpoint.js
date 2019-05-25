const fetchEndpoint = (() => {
  'use strict';
  return (endpoint) => fetch(`http://localhost:8001/${endpoint}`, {
    headers: {
      'content-type': 'application/json',
    },
    mode: 'cors',
  })
    .then(response => response.json())
    .catch(error => { console.error(error) });
})();