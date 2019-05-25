const postAction = (body) => {
  return fetch('http://localhost:8001/action', {
    method: 'POST',
    body: JSON.stringify(body),
    headers: {
      accept: 'application/json',
      'Content-Type': 'application/json',
    },
    mode: 'cors',
  })
    .then(response => response.json())
    .catch(error => { console.error(error) })
};