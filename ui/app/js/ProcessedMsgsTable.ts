/// <reference path="../types/react/react.d.ts" />
/// <reference path="../types/lodash/lodash.d.ts" />
/// <reference path="../types/lib.d.ts" />

import React = require('react');
import WebSocketMsgs = require('./WebSocketMsgs');
import MsgFilter = require('./sidebar/filter/MsgFilter');
import _ = require('lodash');
require('fixed-data-table.css');
var FixedDataTable = require('fixed-data-table');
var Table = React.createFactory(FixedDataTable.Table);
var Column = React.createFactory(FixedDataTable.Column);


export interface Props {
    rows: Row[];
    tableWidth: number;
    tableHeight: number;
    filterState: MsgFilter.FilterState;
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
}

class _ProcessedMsgsTable extends React.Component<Props, State> {
    constructor(props) {
        super(props);
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

    private _onColumnResizeEndCallback = (newColumnWidth:number, dataKey:string):void => {
        this.state.isColumnResizing = false;
        this.state.columnWidths[dataKey] = newColumnWidth;
        this.setState(this.state);
    };

    render() {
        var passesTextFilter = (row: Row) => {
            return !this.props.filterState.searchText || row.original.msgBody.indexOf(this.props.filterState.searchText) !== -1;
        };
        var hasCurrentPair = this.props.filterState.currentPair.senderId || this.props.filterState.currentPair.recipientId;
        var pairPassesFilter = (pair: MsgFilter.Pair, row: Row) => (!pair.senderId || pair.senderId === row.original.senderComponentId) &&
                                                         (!pair.recipientId || pair.recipientId === row.original.recipientComponentId);

        var passesSenderRecipientFilter = (row: Row) => {
            return _.some(this.props.filterState.addedPairs, (pair: MsgFilter.Pair) => pairPassesFilter(pair, row)) ||
                        hasCurrentPair && pairPassesFilter(this.props.filterState.currentPair, row);
        };
        var isFilterPresent = this.props.filterState.addedPairs.length > 0 || hasCurrentPair;
        var rows = this.props.rows.filter(row => {
            return (!isFilterPresent || passesSenderRecipientFilter(row)) && passesTextFilter(row);
        });
        var rowGetter = (index:number):Row => {
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
                onRowClick: (event, index, data) => {console.log(index + 'row clicked')}
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
export var Component = React.createFactory(_ProcessedMsgsTable);