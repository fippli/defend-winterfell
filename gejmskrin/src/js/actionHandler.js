const actionHandler = (() => {

  var tower = {
    actionId: "defender",
    type: "aryaStark",
    x: 123,
    y: 32,
  }

  let actionQueue = [];

  const addAction = (action) => {
    actionQueue = [...actionQueue, action];
  };



  const handleActions = (state) => new Promise((resolve, reject) => {
    if (actionQueue.length > 0) {
      console.log('Trying action');
      // const actionName = actionQueue.splice(0, 1)[0];
      postAction(tower)
        .then(mergeState(state))
        .then(state => {
          resolve(state);
        })
    } else {
      resolve(state);
    }
  });

  const action = (event) => {
    console.log(event);
    tower.x = event.screenX;
    tower.y = event.screenY;
    addAction(name);
  }

  return {
    handleActions,
    action,
  }
})();