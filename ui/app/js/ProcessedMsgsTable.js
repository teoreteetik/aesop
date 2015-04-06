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
            this.state = {
                isColumnResizing: false,
                columnWidths: {
                    formattedDate: 200,
                    senderName: 350,
                    recipientName: 350,
                    formattedMsgBody: 2000
                }
            };
        }
        _ProcessedMsgsTable.prototype.render = function () {
            var _this = this;
            var passesTextFilter = function (row) {
                return !_this.props.filterState.searchText || row.original.msgBody.indexOf(_this.props.filterState.searchText) !== -1;
            };
            var hasCurrentPair = this.props.filterState.currentPair.senderId || this.props.filterState.currentPair.recipientId;
            var pairPassesFilter = function (pair, row) { return (!pair.senderId || pair.senderId === row.original.senderComponentId) && (!pair.recipientId || pair.recipientId === row.original.recipientComponentId); };
            var passesSenderRecipientFilter = function (row) {
                return _.some(_this.props.filterState.addedPairs, function (pair) { return pairPassesFilter(pair, row); }) || hasCurrentPair && pairPassesFilter(_this.props.filterState.currentPair, row);
            };
            var isFilterPresent = this.props.filterState.addedPairs.length > 0 || hasCurrentPair;
            var rows = this.props.rows.filter(function (row) {
                return (!isFilterPresent || passesSenderRecipientFilter(row)) && passesTextFilter(row);
            });
            var rowGetter = function (index) {
                return rows[index];
            };
            return Table({
                rowHeight: 30,
                rowGetter: rowGetter,
                rowsCount: rows.length,
                width: this.props.tableWidth,
                height: this.props.tableHeight,
                isColumnResizing: this.state.isColumnResizing,
                onColumnResizeEndCallback: this._onColumnResizeEndCallback,
                headerHeight: 40,
                onRowClick: function (event, index, data) {
                    console.log(index + 'row clicked');
                }
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