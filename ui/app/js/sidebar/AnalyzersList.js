/// <reference path="../../types/react/react.d.ts" />
/// <reference path="../../types/lib.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react'], function (require, exports, React) {
    var R = React.DOM;
    var AnalyzersList = (function (_super) {
        __extends(AnalyzersList, _super);
        function AnalyzersList() {
            _super.apply(this, arguments);
        }
        AnalyzersList.prototype.getAnalyzerListItems = function () {
            var _this = this;
            var listItems = this.props.analyzers.map(function (analyzer) {
                var clickHandler = function (e) {
                    e.preventDefault();
                    _this.props.onAnalyzerClicked(analyzer.idName.id);
                };
                var components = analyzer.componentIdNames.map(function (idName) { return R.li({}, R.a({ href: '#' }, idName.name)); });
                return R.li({}, R.a({ href: '#', onClick: clickHandler }, analyzer.idName.name), R.ul({ className: _this.props.activeAnalyzerId == analyzer.idName.id ? null : 'collapse' }, components));
            });
            return listItems;
        };
        AnalyzersList.prototype.render = function () {
            return R.ul({ className: 'sidebar-nav' }, this.getAnalyzerListItems());
        };
        return AnalyzersList;
    })(React.Component);
    exports.Component = React.createFactory(AnalyzersList);
});
//# sourceMappingURL=AnalyzersList.js.map