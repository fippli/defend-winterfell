(function () {
  'use strict';

  const state = {
    images: {
      background: {
        src: 'art/winterfell.png',
        position: {
          x: 0,
          y: 0,
        },
        width: 960,
        height: 640,
      },
      nightKing: {
        src: 'art/whiteWalker.png',
        width: 60,
        height: 60,
      },
      aryaStark: {
        src: 'art/arya.png',
        width: 31.5,
        height: 90,
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