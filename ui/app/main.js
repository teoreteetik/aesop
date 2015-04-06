/// <reference path="./types/react/react.d.ts" />
/// <reference path="./types/lib.d.ts" />

require('bootstrap.css');
require('./css/custom.css');
var React = require('react');
Object.assign = require('object-assign');

var Page = require('./js/Page');

initReact = function () {
    React.render(Page(), document.getElementById('root'));
};
