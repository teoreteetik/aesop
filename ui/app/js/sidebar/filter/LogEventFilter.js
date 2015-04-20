/// <reference path="../../../types/react/react.d.ts" />
/// <reference path="../../../types/lodash/lodash.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react'], function (require, exports, React) {
    var BS = require('react-bootstrap');
    var Input = React.createFactory(BS.Input);
    var R = React.DOM;
    var LogEventFilter = (function (_super) {
        __extends(LogEventFilter, _super);
        function LogEventFilter() {
            _super.apply(this, arguments);
        }
        LogEventFilter.prototype.render = function () {
            return null;
        };
        return LogEventFilter;
    })(React.Component);
    exports.Component = React.createFactory(LogEventFilter);
});
//# sourceMappingURL=LogEventFilter.js.map