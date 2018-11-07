import React from 'react';
import './App.css';
import {Router} from "react-router";
import Routing from "./util/Routing";
import MainView from './component/MainView';
import appStore from "./store/Store";
import {Provider} from "react-redux";

const App = () => {
    return <Provider store={appStore}>
        <Router history={Routing._history}>
            <MainView/>
        </Router>
    </Provider>;
};

export default App;
