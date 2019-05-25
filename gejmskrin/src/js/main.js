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
      },
    },
    enemies: [{ type: 'nightKing', position: {} }]
  };

  const canvasElement = document.getElementById('gejmskrin');
  const context = canvasElement.getContext('2d');

  const mergeState = state => (APIState) => {
    return {
      ...state,
      ...APIState,
    };
  };

  const drawCanvas = (state) => {
    clearCanvas(state, canvasElement, context)
      .then(drawBackground(context))
      .then(drawImageArray(context, state.enemies)) // Draw enemies
    // .then(drawImageArray(context, state.defenders)) // Draw defenders
  };

  const startGame = (state) => {
    createGame()
      .then(mergeState(state))
      .then(drawCanvas)
  };

  loadImages(state) // This should be done once in the future
    .then(startGame);

})();