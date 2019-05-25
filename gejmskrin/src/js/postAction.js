const postAction = (() => {

  return (body) => fetch('http://localhost:8001/action', {
    method: 'post',
    body: JSON.stringify(body),
    headers: {
      'content-type': 'application/json',
    },
    mode: 'cors',
  })
    .then(response => response.json())
    .catch(error => { console.error(error) });
})();