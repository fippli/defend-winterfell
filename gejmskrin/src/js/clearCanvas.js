const clearCanvas = (function () {
  'use strict';
  return (state, canvas, context) => {
    return new Promise((resolve, reject) => {
      try {
        context.clearRect(0, 0, canvas.width, canvas.height);
        resolve(state);
      } catch (error) {
        reject(error);
      }
    })
  };
})();