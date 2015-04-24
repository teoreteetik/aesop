/// <reference path="../types/common.d.ts" />

import React = require('react');
import WebSocketMsgs = require('./WebSocketMsgs');
import MsgProcessedFilter = require('./sidebar/filter/MsgProcessedFilter');

require('fixed-data-table.css');
var FixedDataTable = require('fixed-data-table');
var Table = React.createFactory(FixedDataTable.Table);
var Column = React.createFactory(FixedDataTable.Column);

export interface Props {
    rows: Row[];
    tableWidth: number;
    tableHeight: number;
    scrollTop: number;
    filterState: MsgProcessedFilter.FilterState;
    onRowDoubleClicked: (row: Row) => void;
    onScrollChanged: (scrollTop: number) => void;
}

export interface Row {
    formattedDate: string;
    senderName: string;
    recipientName: string;
    formattedMsgBody: string;
    original: WebSocketMsgs.ProcessedMsgEvent;
}
var formattedDateDK = 'formattedDate';
var senderNameDK = 'senderName';
var recipientNameDK = 'recipientName';
var formattedMsgBodyDK = 'formattedMsgBody';

interface State {
    columnWidths: {[dataKey: string]: number};
    isColumnResizing: boolean;
    selectedRowIndex: number;
    clicks: number;
}

class ProcessedMsgsTable extends React.Component<Props, State> {
    constructor(props) {
        super(props);
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

    private onColumnResizeEndCallback = (newColumnWidth: number, dataKey: string):void => {
        this.state.isColumnResizing = false;
        this.state.columnWidths[dataKey] = newColumnWidth;
        this.setState(this.state);
    };

    private getFilteredRows = (): Row[] => {
        var pairPassesFilter = (pair: MsgProcessedFilter.Pair, row: Row) => (!pair.senderId || pair.senderId === row.original.senderComponentId) &&
                                                                            (!pair.recipientId || pair.recipientId === row.original.recipientComponentId);
        var passesSenderRecipientFilter = (row: Row) => _.some(this.props.filterState.addedPairs, (pair: MsgProcessedFilter.Pair) => pairPassesFilter(pair, row)) ||
                                                        hasCurrentPair && pairPassesFilter(this.props.filterState.currentPair, row);
        var passesStartTimeFilter = (row: Row): boolean => !this.props.filterState.startTime ||
                                                               row.original.processingStartTime >= this.props.filterState.startTime;
        var passesEndTimeFilter = (row: Row) => !this.props.filterState.endTime ||
                                                row.original.processingStartTime <= this.props.filterState.endTime;
        var passesTextFilter = (row: Row): boolean => !this.props.filterState.searchText ||
                                                      row.original.msgBody.indexOf(this.props.filterState.searchText) !== -1;
        var hasCurrentPair = this.props.filterState.currentPair.senderId || this.props.filterState.currentPair.recipientId;

        var isFilterPresent = this.props.filterState.addedPairs.length > 0 || hasCurrentPair;
        return this.props.rows.filter(row => {
            return (!isFilterPresent || passesSenderRecipientFilter(row)) && passesTextFilter(row) && passesStartTimeFilter(row) && passesEndTimeFilter(row);
        });
    };

    render() {
        var rows = this.getFilteredRows();
        var rowGetter = (index:number):Row => rows[index];
        var onRowClick = (event, index: number, data) => {
            this.state.clicks++;
            if (this.state.clicks === 1) {
                this.state.selectedRowIndex = index;
                this.setState(this.state);
                setTimeout(() => {
                    if (this.state.clicks == 2)
                        this.props.onRowDoubleClicked(rows[index]);
                    this.state.clicks = 0;
                    this.setState(this.state);
                }, 500);
            }
        };
        var rowClassNameGetter = (index: number) => {
            var classNames = [];
            if (index === this.state.selectedRowIndex)
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
                onColumnResizeEndCallback: this.onColumnResizeEndCallback,
                headerHeight: 40,
                scrollTop: this.props.scrollTop,
                onScrollEnd: (left, top) => {
                    this.props.onScrollChanged(top);
                },
                rowClassNameGetter: rowClassNameGetter
            },
            Column({
                label: 'Time',
                width: this.state.columnWidths[formattedDateDK],
                isResizable: true,
                dataKey: formattedDateDK
            }),
            Column({
                label: 'Sender',
                width: this.state.columnWidths[senderNameDK],
                isResizable: true,
                dataKey: senderNameDK
            }),
            Column({
                label: 'Recipient',
                width: this.state.columnWidths[recipientNameDK],
                isResizable: true,
                dataKey: recipientNameDK
            }),
            Column({
                label: 'Message',
                width: this.state.columnWidths[formattedMsgBodyDK],
                isResizable: true,
                dataKey: formattedMsgBodyDK
            }))
    }
}
export var Component = React.createFactory(ProcessedMsgsTable);