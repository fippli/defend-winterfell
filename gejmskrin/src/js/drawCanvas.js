const drawCanvas = (() => {
  return (canvasElement, context) => (state) => {
    return clearCanvas(state, canvasElement, context)
      .then(drawBackground(context))
      .then(drawImageArray(context, state.enemies)) // Draw enemies
      .then(drawImageArray(context, state.defenders)) // Draw defenders
  };
})();