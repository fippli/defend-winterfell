const drawBackground = (function () {
  'use strict';
  return context => (state) => {
    return new Promise((resolve, reject) => {
      try {
        drawImageObject(state, context, 'background', state.images.background.position);
        resolve(state);
      } catch (error) {
        reject(error);
      }
    })
  };
})();