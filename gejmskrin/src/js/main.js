(function () {
  'use strict';

  const state = {
    images: {
      grass: {
        src: 'art/grass.svg',
        position: {
          x: 0,
          y: 0,
        },
        width: 800,
        height: 600,
      },
      road: {
        src: 'art/road.svg',
        position: {
          x: 250,
          y: 0,
        },
        width: 300,
        height: 600,
      },
      castle: {
        src: 'art/winterfell.svg',
        position: {
          x: 350,
          y: 500,
        },
        width: 100,
        height: 100,
      },
      nightKing: {
        src: 'art/night-king.svg',
        width: 44,
        height: 80,
      },
      aryaStark: {
        src: 'art/night-king.svg',
        width: 44,
        height: 80,
      }
    },
  };

  const canvasElement = document.getElementById('gejmskrin');
  const context = canvasElement.getContext('2d');

  loadImages(state) // This should be done once in the future
    .then(state => drawBackground(state, context))
    .then(state => drawEnemies(state, conext))
    .catch(error => { console.error(error); });

})();