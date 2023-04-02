import { atom } from 'recoil';
// import script from '../models/script';

export const clickedEmotionState = atom({
  key: 'clickedEmotionState',
  default: ['슬픔'],
});

export const clickedGenreState = atom({
  key: 'clickedGenreState',
  default: [],
});

export const clickedBtnState = atom({
  key: 'clickedBtnState',
  default: [1],
});

export const pageState = atom({
  key: 'pageState',
  default: 0,
});

export const scriptState = atom({
  key: 'scriptState',
  default: [],
});

export const optionState = atom({
  key: 'optionState',
  default: 'title',
});

export const keywordState = atom({
  key: 'keywordState',
  default: '최신순',
});

export const sortingState = atom({
  key: 'sortingState',
  default: '최신순',
});
