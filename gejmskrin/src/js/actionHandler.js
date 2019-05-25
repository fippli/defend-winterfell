const actionHandler = (() => {

  const towerPrep = {
    actionId: "defender",
    type: "aryaStark",
    position: {
      x: 123,
      y: 32,
    }
  }

  let actionQueue = [];

  const addAction = (action) => {
    actionQueue = [...actionQueue, action];
  };

  const getActionBody = (actionName) => {
    return towerPrep;
  }

  const handleActions = (state) => new Promise((resolve, reject) => {
    if (actionQueue.length > 0) {
      const actionName = actionQueue.splice(0, 1)[0];
      const requestBody = getActionBody(actionName)
      postAction(requestBody)
        .then(state => {
          resolve(state)
        })
    } else {
      resolve(state);
    }
  });

  const action = (name) => {
    switch (name) {
      case 'place-arya-stark': {
        addAction('place-arya-stark')
      }
    }
  }

  return {
    handleActions,
    action,
  }
})();