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
        src: 'art/arya-stark.svg',
        width: 44,
        height: 80,
      },
    },
    enemies: [{ type: 'nightKing', position: {} }]
  };


  const canvasElement = document.getElementById('gejmskrin');
  const context = canvasElement.getContext('2d');

  const tick = previousState => () => {
    fetchEndpoint('tick')
      .then(mergeState(previousState))
      .then(state => {
        window.requestAnimationFrame(gameloop(state));
      })
  };

  const gameloop = state => () => {
    // handle actions
    actionHandler.handleActions(state)
      .then(drawCanvas(canvasElement, context))
      .then(state => {
        tick(state)();
      })
  }

  const startGame = (state) => {
    fetchEndpoint('createGame')
      .then(mergeState(state))
      .then(state => {
        gameloop(state)()
      })
  };

  loadImages(state) // This should be done once in the future
    .then(startGame);

})();