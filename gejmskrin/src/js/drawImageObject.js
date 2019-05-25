/**
 * Draw an image object
 */
const drawImageObject = (function () {

  'use strict';

  return function (state, context, imageKey, position, range = undefined) {
    const imageObject = state.images[imageKey];
    context.drawImage(
      state.loadedImages[imageKey],
      position.x,
      position.y,
      imageObject.width,
      imageObject.height,
    );
    if (range) {
      context.beginPath();
      context.strokeStyle = '#ff0000';
      context.arc(
        position.x,
        position.y,
        range,
        0, 2 * Math.PI
      );
      context.stroke();
    }
  };
})();