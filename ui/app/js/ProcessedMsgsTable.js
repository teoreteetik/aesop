/// <reference path="../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './sidebar/filter/MsgProcessedFilter'], function (require, exports, React, MsgProcessedFilter) {
    require('fixed-data-table.css');
    var FixedDataTable = require('fixed-data-table');
    var Table = React.createFactory(FixedDataTable.Table);
    var Column = React.createFactory(FixedDataTable.Column);
    var formattedDateDK = 'formattedDate';
    var senderNameDK = 'senderName';
    var recipientNameDK = 'recipientName';
    var formattedMsgBodyDK = 'formattedMsgBody';
    var ProcessedMsgsTable = (function (_super) {
        __extends(ProcessedMsgsTable, _super);
        function ProcessedMsgsTable(props) {
            var _this = this;
            _super.call(this, props);
            this.onColumnResizeEndCallback = function (newColumnWidth, dataKey) {
                _this.state.isColumnResizing = false;
                _this.state.columnWidths[dataKey] = newColumnWidth;
                _this.setState(_this.state);
            };
            this.getFilteredRows = function () {
                var fs = _this.props.filterState;
                var pairPassesFilter = function (pair, row) { return (!pair.senderId || pair.senderId === row.original.senderComponentId) && (!pair.recipientId || pair.recipientId === row.original.recipientComponentId); };
                var passesSenderRecipientFilter = function (row) { return _.some(fs.addedPairs, function (pair) { return pairPassesFilter(pair, row); }) || hasCurrentPair && pairPassesFilter(fs.currentPair, row); };
                var passesStartTimeFilter = function (row) { return !fs.startTime || row.original.processingStartTime >= fs.startTime; };
                var passesEndTimeFilter = function (row) { return !fs.endTime || row.original.processingStartTime <= fs.endTime; };
                var passesTextFilter = function (row) { return !fs.searchText || row.original.msgBody.indexOf(fs.searchText) !== -1; };
                var passesProcessingStateFilter = function (row) { return fs.processingState === undefined || fs.processingState === 0 /* SUCCESS */ && row.original.stackTrace === undefined || fs.processingState === 1 /* FAIL */ && row.original.stackTrace !== undefined; };
                var hasCurrentPair = _this.props.filterState.currentPair.senderId || _this.props.filterState.currentPair.recipientId;
                var isFilterPresent = _this.props.filterState.addedPairs.length > 0 || hasCurrentPair;
                return _this.props.rows.filter(function (row) {
                    return (!isFilterPresent || passesSenderRecipientFilter(row)) && passesTextFilter(row) && passesStartTimeFilter(row) && passesEndTimeFilter(row) && passesProcessingStateFilter(row);
                });
            };
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
        ProcessedMsgsTable.prototype.render = function () {
            var _this = this;
            var rows = this.getFilteredRows();
            var rowGetter = function (index) { return rows[index]; };
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
                    classNames.push(rows[index].isRead ? 'readError' : 'unreadError');
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
                onColumnResizeEndCallback: this.onColumnResizeEndCallback,
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
        return ProcessedMsgsTable;
    })(React.Component);
    exports.Component = React.createFactory(ProcessedMsgsTable);
});
//# sourceMappingURL=ProcessedMsgsTable.js.map