const drawBackground = (function () {
  'use strict';
  return context => (state) => {
    return new Promise((resolve, reject) => {
      try {
        drawImageObject(state, context, 'grass');
        drawImageObject(state, context, 'road');
        drawImageObject(state, context, 'castle');
        resolve(state);
      } catch (error) {
        reject(error);
      }
    })
  };
})();