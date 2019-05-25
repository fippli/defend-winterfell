/**
 * Draw an image object
 */
const drawImageObject = (function () {

  'use strict';

  return function (state, context, imageKey) {
    const imageObject = state.images[imageKey];
    context.drawImage(
      state.loadedImages[imageKey],
      imageObject.position.x,
      imageObject.position.y,
      imageObject.width,
      imageObject.height,
    );
  };
})();