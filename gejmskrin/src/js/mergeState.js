const mergeState = (() => {
  return state => (APIState) => {
    return {
      ...state,
      ...APIState,
    };
  };
})();