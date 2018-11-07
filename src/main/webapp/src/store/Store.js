import {applyMiddleware, createStore} from 'redux';
import {createLogger} from 'redux-logger';
import thunk from 'redux-thunk';
import reducer from '../reducer/Reducers';

const loggerMiddleware = createLogger();

const store = createStore(reducer, applyMiddleware(thunk, loggerMiddleware));

export default store;