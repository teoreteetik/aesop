/// <reference path="../types/react/react.d.ts" />
/// <reference path="../types/lodash/lodash.d.ts" />
/// <reference path="../types/lib.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', 'lodash'], function (require, exports, React, _) {
    require('fixed-data-table.css');
    var FixedDataTable = require('fixed-data-table');
    var Table = React.createFactory(FixedDataTable.Table);
    var Column = React.createFactory(FixedDataTable.Column);
    var formattedDateDK = 'formattedDate';
    var senderNameDK = 'senderName';
    var recipientNameDK = 'recipientName';
    var formattedMsgBodyDK = 'formattedMsgBody';
    var _ProcessedMsgsTable = (function (_super) {
        __extends(_ProcessedMsgsTable, _super);
        function _ProcessedMsgsTable(props) {
            var _this = this;
            _super.call(this, props);
            this._onColumnResizeEndCallback = function (newColumnWidth, dataKey) {
                _this.state.isColumnResizing = false;
                _this.state.columnWidths[dataKey] = newColumnWidth;
                _this.setState(_this.state);
            };
            this.passesTextFilter = function (row) { return !_this.props.filterState.searchText || row.original.msgBody.indexOf(_this.props.filterState.searchText) !== -1; };
            this.passesStartTimeFilter = function (row) { return !_this.props.filterState.startTime || row.original.processingStartTime >= _this.props.filterState.startTime; };
            this.state = {
                isColumnResizing: false,
                columnWidths: {
                    formattedDate: 200,
                    senderName: 350,
                    recipientName: 350,
                    formattedMsgBody: 2000
                },
                selectedRowIndex: undefined,
                clicks: 0
            };
        }
        _ProcessedMsgsTable.prototype.render = function () {
            var _this = this;
            var passesEndTimeFilter = function (row) {
                return !_this.props.filterState.endTime || row.original.processingStartTime <= _this.props.filterState.endTime;
            };
            var hasCurrentPair = this.props.filterState.currentPair.senderId || this.props.filterState.currentPair.recipientId;
            var pairPassesFilter = function (pair, row) { return (!pair.senderId || pair.senderId === row.original.senderComponentId) && (!pair.recipientId || pair.recipientId === row.original.recipientComponentId); };
            var passesSenderRecipientFilter = function (row) {
                return _.some(_this.props.filterState.addedPairs, function (pair) { return pairPassesFilter(pair, row); }) || hasCurrentPair && pairPassesFilter(_this.props.filterState.currentPair, row);
            };
            var isFilterPresent = this.props.filterState.addedPairs.length > 0 || hasCurrentPair;
            var rows = this.props.rows.filter(function (row) {
                return (!isFilterPresent || passesSenderRecipientFilter(row)) && _this.passesTextFilter(row) && _this.passesStartTimeFilter(row) && passesEndTimeFilter(row);
            });
            var rowGetter = function (index) {
                return rows[index];
            };
            var onRowClick = function (event, index, data) {
                _this.state.clicks++;
                if (_this.state.clicks === 1) {
                    _this.state.selectedRowIndex = index;
                    _this.setState(_this.state);
                    setTimeout(function () {
                        if (_this.state.clicks == 2)
                            _this.props.onRowDoubleClicked(rows[index]);
                        _this.state.clicks = 0;
                        _this.setState(_this.state);
                    }, 500);
                }
            };
            var rowClassNameGetter = function (index) {
                var classNames = [];
                if (index === _this.state.selectedRowIndex)
                    classNames.push('selectedRow');
                if (rows[index].original.stackTrace)
                    classNames.push('error');
                return classNames.join(" ");
            };
            return Table({
                rowHeight: 30,
                onRowClick: onRowClick,
                rowGetter: rowGetter,
                rowsCount: rows.length,
                width: this.props.tableWidth,
                height: this.props.tableHeight,
                isColumnResizing: this.state.isColumnResizing,
                onColumnResizeEndCallback: this._onColumnResizeEndCallback,
                headerHeight: 40,
                scrollTop: this.props.scrollTop,
                onScrollEnd: function (left, top) {
                    _this.props.onScrollChanged(top);
                },
                rowClassNameGetter: rowClassNameGetter
            }, Column({
                label: 'Time',
                width: this.state.columnWidths[formattedDateDK],
                isResizable: true,
                dataKey: formattedDateDK
            }), Column({
                label: 'Sender',
                width: this.state.columnWidths[senderNameDK],
                isResizable: true,
                dataKey: senderNameDK
            }), Column({
                label: 'Recipient',
                width: this.state.columnWidths[recipientNameDK],
                isResizable: true,
                dataKey: recipientNameDK
            }), Column({
                label: 'Message',
                width: this.state.columnWidths[formattedMsgBodyDK],
                isResizable: true,
                dataKey: formattedMsgBodyDK
            }));
        };
        return _ProcessedMsgsTable;
    })(React.Component);
    exports.Component = React.createFactory(_ProcessedMsgsTable);
});
//# sourceMappingURL=ProcessedMsgsTable.js.map