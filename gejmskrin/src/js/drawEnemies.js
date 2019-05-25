/**
 * Draw enemies
 */
const drawEnemies = (function () {

  'use strict';

  return function (state, context) {
    return new Promise(function (resolve, reject) {
      try {
        state.enemies.forEach(enemy => {
          drawImageObject(state, context, enemy.type);
        });
        resolve(state);
      } catch (error) {
        reject(error);
      }
    })
  };
})();