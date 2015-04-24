/// <reference path="../types/common.d.ts" />

import React = require('react');
import WebSocketMsgs = require('./WebSocketMsgs');
import LogEventFilter = require('./sidebar/filter/LogEventFilter');

require('fixed-data-table.css');
var FixedDataTable = require('fixed-data-table');
var Table = React.createFactory(FixedDataTable.Table);
var Column = React.createFactory(FixedDataTable.Column);

export interface Props {
    rows: Row[];
    tableWidth: number;
    tableHeight: number;
    filterState: LogEventFilter.FilterState;
}

export interface Row {
    logLevel: string;
    formattedDate: string;
    componentName: string;
    formattedMsgBody: string;
    original: WebSocketMsgs.LogEvent;
}

var logLevelDK = 'logLevel';
var formattedDateDK = 'formattedDate';
var componentNameDK = 'componentName';
var formattedMsgBodyDK = 'formattedMsgBody';

interface State {
    columnWidths: {[dataKey: string]: number};
    isColumnResizing: boolean;
}

class LogEventsTable extends React.Component<Props, State> {
    constructor(props) {
        super(props);
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

    private onColumnResizeEndCallback = (newColumnWidth:number, dataKey:string):void => {
        this.state.isColumnResizing = false;
        this.state.columnWidths[dataKey] = newColumnWidth;
        this.setState(this.state);
    };

    private getFilteredRows = () => {
        var passesStartTimeFilter = (row: Row) => !this.props.filterState.startTime || row.original.time >= this.props.filterState.startTime;
        var passesEndTimeFilter = (row: Row) => !this.props.filterState.endTime || row.original.time <= this.props.filterState.endTime;
        var passesComponentFilter = (row: Row) => !this.props.filterState.componentId || row.original.componentId === this.props.filterState.componentId;
        return (
            this.props.rows.filter(row => passesComponentFilter(row) &&
                                          passesStartTimeFilter(row) &&
                                          passesEndTimeFilter(row))
        );
    };

    render() {
        var rows = this.getFilteredRows();
        var rowGetter = (index: number): Row => rows[index];

        return (
            Table({
                rowHeight: 30,
                rowGetter: rowGetter,
                rowsCount: this.props.rows.length,
                width: this.props.tableWidth,
                height: this.props.tableHeight,
                isColumnResizing: this.state.isColumnResizing,
                onColumnResizeEndCallback: this.onColumnResizeEndCallback,
                headerHeight: 40,
                onRowClick: (event, index, data) => {}
            },
            Column({
                label: 'Level',
                width: this.state.columnWidths[logLevelDK],
                isResizable: true,
                dataKey: logLevelDK
            }),
            Column({
                label: 'Time',
                width: this.state.columnWidths[formattedDateDK],
                isResizable: true,
                dataKey: formattedDateDK
            }),
            Column({
                label: 'Component',
                width: this.state.columnWidths[componentNameDK],
                isResizable: true,
                dataKey: componentNameDK
            }),
            Column({
                label: 'Message',
                width: this.state.columnWidths[formattedMsgBodyDK],
                isResizable: true,
                dataKey: formattedMsgBodyDK
            }))
        );
    }
}
export var Component = React.createFactory(LogEventsTable);