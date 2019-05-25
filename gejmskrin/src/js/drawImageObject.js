/**
 * Draw an image object
 */
const drawImageObject = (function () {

  'use strict';

  return function (state, context, imageKey, position) {
    const imageObject = state.images[imageKey];
    context.drawImage(
      state.loadedImages[imageKey],
      position.x,
      position.y,
      imageObject.width,
      imageObject.height,
    );
  };
})();