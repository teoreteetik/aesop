/// <reference path="../types/react/react.d.ts" />
/// <reference path="../types/lodash/lodash.d.ts" />
/// <reference path="../types/lib.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react'], function (require, exports, React) {
    require('fixed-data-table.css');
    var FixedDataTable = require('fixed-data-table');
    var Table = React.createFactory(FixedDataTable.Table);
    var Column = React.createFactory(FixedDataTable.Column);
    var logLevelDK = 'logLevel';
    var formattedDateDK = 'formattedDate';
    var componentNameDK = 'componentName';
    var formattedMsgBodyDK = 'formattedMsgBody';
    var LogEventsTable = (function (_super) {
        __extends(LogEventsTable, _super);
        function LogEventsTable(props) {
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
                    logLevel: 100,
                    formattedDate: 200,
                    componentName: 350,
                    formattedMsgBody: 2000
                }
            };
        }
        LogEventsTable.prototype.render = function () {
            var _this = this;
            var passesStartTimeFilter = function (row) { return !_this.props.filterState.startTime || row.original.time >= _this.props.filterState.startTime; };
            var passesEndTimeFilter = function (row) { return !_this.props.filterState.endTime || row.original.time <= _this.props.filterState.endTime; };
            var passesComponentFilter = function (row) { return !_this.props.filterState.componentId || row.original.componentId === _this.props.filterState.componentId; };
            var rows = this.props.rows.filter(function (row) { return passesComponentFilter(row) && passesStartTimeFilter(row) && passesEndTimeFilter(row); });
            var rowGetter = function (index) {
                return rows[index];
            };
            return Table({
                rowHeight: 30,
                rowGetter: rowGetter,
                rowsCount: this.props.rows.length,
                width: this.props.tableWidth,
                height: this.props.tableHeight,
                isColumnResizing: this.state.isColumnResizing,
                onColumnResizeEndCallback: this._onColumnResizeEndCallback,
                headerHeight: 40,
                onRowClick: function (event, index, data) {
                }
            }, Column({
                label: 'Level',
                width: this.state.columnWidths[logLevelDK],
                isResizable: true,
                dataKey: logLevelDK
            }), Column({
                label: 'Time',
                width: this.state.columnWidths[formattedDateDK],
                isResizable: true,
                dataKey: formattedDateDK
            }), Column({
                label: 'Component',
                width: this.state.columnWidths[componentNameDK],
                isResizable: true,
                dataKey: componentNameDK
            }), Column({
                label: 'Message',
                width: this.state.columnWidths[formattedMsgBodyDK],
                isResizable: true,
                dataKey: formattedMsgBodyDK
            }));
        };
        return LogEventsTable;
    })(React.Component);
    exports.Component = React.createFactory(LogEventsTable);
});
//# sourceMappingURL=LogEventsTable.js.map