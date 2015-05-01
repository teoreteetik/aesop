/// <reference path="../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './ProcessedMsgsTable', './LogEventsTable', './MsgDetailsPane'], function (require, exports, React, ProcessedMsgsTable, LogEventsTable, MsgDetailsPane) {
    var R = React.DOM;
    var MainContent = (function (_super) {
        __extends(MainContent, _super);
        function MainContent(props) {
            var _this = this;
            _super.call(this, props);
            this.attatchResizeHandler = function () {
                var win = window;
                if (win.addEventListener)
                    win.addEventListener('resize', _this._onResize, false);
                else if (win.attachEvent)
                    win.attachEvent('onresize', _this._onResize);
                else
                    win.onresize = _this._onResize;
            };
            this.update = function () {
                var win = window;
                var widthOffset = 300;
                _this.state.tableWidth = win.innerWidth - widthOffset;
                _this.state.msgProcessedTableHeight = Math.round(win.innerHeight * 0.6) - 20;
                _this.state.logEventsTableHeight = Math.round(win.innerHeight * 0.4) - 20;
                _this.setState(_this.state);
            };
            this._onResize = function () {
                clearTimeout(_this.updateTimerId);
                _this.updateTimerId = setTimeout(_this.update, 16);
            };
            this.getUpperComponent = function () { return _this.state.msgDetails ? _this.getMsgDetailsPane() : _this.getProcessedMsgsTable(); };
            this.getMsgDetailsPane = function () {
                return (MsgDetailsPane.Component({
                    row: _this.state.msgDetails,
                    height: _this.state.msgProcessedTableHeight,
                    onBackClicked: function () {
                        _this.props.onLogFilterStateChanged({
                            startTime: undefined,
                            endTime: undefined,
                            componentId: undefined
                        });
                        _this.state.msgDetails = undefined;
                        _this.setState(_this.state);
                    }
                }));
            };
            this.getProcessedMsgsTable = function () {
                return (ProcessedMsgsTable.Component({
                    rows: _this.props.msgProcessedRows,
                    tableWidth: _this.state.tableWidth,
                    tableHeight: _this.state.msgProcessedTableHeight,
                    filterState: _this.props.filterState.msgProcessedFilterState,
                    scrollTop: _this.state.msgProcessedTableScrollTop,
                    onScrollChanged: function (scrollTop) {
                        _this.state.msgProcessedTableScrollTop = scrollTop;
                        _this.setState(_this.state);
                    },
                    onRowDoubleClicked: function (row) {
                        _this.state.msgDetails = row;
                        _this.setState(_this.state);
                        _this.props.markRowAsRead(row);
                        _this.props.onLogFilterStateChanged({
                            startTime: row.original.processingStartTime,
                            endTime: row.original.processingEndTime,
                            componentId: row.original.recipientComponentId
                        });
                    }
                }));
            };
            this.getLogEventsTable = function () {
                return LogEventsTable.Component({
                    rows: _this.props.logEventRows,
                    tableWidth: _this.state.tableWidth,
                    tableHeight: _this.state.logEventsTableHeight,
                    filterState: _this.props.filterState.logEventFilterState
                });
            };
            this.state = {
                tableWidth: 0,
                msgProcessedTableHeight: 0,
                msgProcessedTableScrollTop: 0,
                logEventsTableHeight: 0,
                msgDetails: undefined
            };
        }
        MainContent.prototype.componentDidMount = function () {
            this.update();
            this.attatchResizeHandler();
        };
        MainContent.prototype.render = function () {
            return (R.div({ id: 'main' }, this.getUpperComponent(), this.getLogEventsTable()));
        };
        return MainContent;
    })(React.Component);
    exports.Component = React.createFactory(MainContent);
});
//# sourceMappingURL=MainContent.js.map