const drawImageArray = (function () {
  'use strict';
  return (context, imageArray) => (state) => {
    return new Promise(function (resolve, reject) {
      try {
        imageArray.forEach(imageObject => {
          drawImageObject(state, context, imageObject.type);
        });
        resolve(state);
      } catch (error) {
        reject(error);
      }
    })
  };
})();