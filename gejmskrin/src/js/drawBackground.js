/**
 * 
 */
const drawBackground = (function () {

  'use strict';

  return function (state, context) {
    return new Promise(function (resolve, reject) {
      try {
        drawImageObject(state, context, 'grass');
        drawImageObject(state, context, 'road');
        drawImageObject(state, context, 'castle');
        resolve(state);
      } catch (error) {
        console.error(error);
        reject(error);
      }
    })
  };
})();