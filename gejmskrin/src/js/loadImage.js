/**
 * Load an image
 */
const loadImage = (function () {

  'use strict';

  return function (state, imageKey) {
    return new Promise(function (resolve, reject) {
      try {
        const newImage = new Image();
        newImage.onload = function () {
          resolve({
            ...state,
            loadedImages: {
              ...state.loadedImages,
              [imageKey]: newImage,
            }
          });
        }
        newImage.src = state.images[imageKey].src;
      } catch (error) {
        reject(error);
      }
    });
  };
})();