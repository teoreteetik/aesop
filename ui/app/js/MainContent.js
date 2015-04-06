/// <reference path="../types/react/react.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './ProcessedMsgsTable'], function (require, exports, React, ProcessedMsgsTable) {
    var R = React.DOM;
    var MainContent = (function (_super) {
        __extends(MainContent, _super);
        function MainContent(props) {
            var _this = this;
            _super.call(this, props);
            this._update = function () {
                var win = window;
                var widthOffset = 400;
                _this.state.tableWidth = win.innerWidth - widthOffset;
                _this.state.tableHeight = win.innerHeight - 200;
                _this.setState(_this.state);
            };
            this._onResize = function () {
                clearTimeout(_this._updateTimerId);
                _this._updateTimerId = setTimeout(_this._update, 16);
            };
            this.state = {
                tableWidth: 0,
                tableHeight: 0
            };
        }
        MainContent.prototype.componentDidMount = function () {
            this._update();
            this._attatchResizeHandler();
        };
        MainContent.prototype._attatchResizeHandler = function () {
            var win = window;
            if (win.addEventListener)
                win.addEventListener('resize', this._onResize, false);
            else if (win.attachEvent)
                win.attachEvent('onresize', this._onResize);
            else
                win.onresize = this._onResize;
        };
        MainContent.prototype.render = function () {
            return R.div({ id: 'main' }, ProcessedMsgsTable.Component({
                rows: this.props.msgProcessedRows,
                tableWidth: this.state.tableWidth,
                tableHeight: this.state.tableHeight,
                filterState: this.props.filterState
            }));
        };
        return MainContent;
    })(React.Component);
    exports.Component = React.createFactory(MainContent);
});
//# sourceMappingURL=MainContent.js.map