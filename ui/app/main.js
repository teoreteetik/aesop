require('bootstrap.css');
require('./css/custom.css');
var React = require('react');
Object.assign = require('object-assign');

var Main = require('./js/Main');

initReact = function () {
    React.render(Main(), document.getElementById('root'));
};
