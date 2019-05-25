const actionHandler = (function () {
  'use strict'

  const actionQueue = {
    pendingActions: [],
  };

  // Somewhat mutating sorry :(
  const addAction = (actionName) => {
    actionQueue.pendingActions = [...actionQueue.pendingActions, actionName];
  };

  return function (event) {
    console.log()
  };

})();