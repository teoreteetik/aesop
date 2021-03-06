/// <reference path="./types/common.d.ts" />
require('bootstrap.css');
require('./css/custom.css');
var React = require('react');
Object['assign'] = require('object-assign');
var Main = React.createFactory(require('./js/Main'));
var initReact = function () {
    React.render(Main(), document.getElementById('root'));
};
window.onload = function () {
    initReact();
};
