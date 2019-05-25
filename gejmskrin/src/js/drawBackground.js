const drawBackground = (function () {
  'use strict';
  return context => (state) => {
    return new Promise((resolve, reject) => {
      try {
        drawImageObject(state, context, 'grass', state.images.grass.position);
        drawImageObject(state, context, 'road', state.images.road.position);
        drawImageObject(state, context, 'castle', state.images.castle.position);
        resolve(state);
      } catch (error) {
        reject(error);
      }
    })
  };
})();