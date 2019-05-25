/**
 * Load an image
 */
const loadImages = (function () {

  'use strict';

  const loadAllImages = function(state, imageKeys, index) {
    if (index === imageKeys.length - 1) {
      return loadImage(state, imageKeys[index]);
    }
    return loadImage(state, imageKeys[index])
      .then(state => {
        return loadAllImages(state, imageKeys, index + 1);
      });
  };

  return function (state) {
    const imageKeys = Object.keys(state.images);
    return loadAllImages(state, imageKeys, 0);
  }
})();