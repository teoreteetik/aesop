/// <reference path="../types/react/react.d.ts" />

import React = require('react');
import WebSocketMsgs = require('./WebSocketMsgs');
import ProcessedMsgsTable = require('./ProcessedMsgsTable');
import LogEventsTable = require('./LogEventsTable');
import FormatUtil = require('./util/FormatUtil');
import FilterSidebar = require('./sidebar/filter/FilterSidebar');
import LogEventFilter = require('./sidebar/filter/LogEventFilter');
import MsgDetailsPane = require('./MsgDetailsPane');

var R = React.DOM;

export interface Props {
    msgProcessedRows: ProcessedMsgsTable.Row[];
    logEventRows: LogEventsTable.Row[];
    filterState: FilterSidebar.FilterState;
    onLogFilterStateChanged: (newState: LogEventFilter.FilterState) => void;
}

interface State {
    tableWidth: number;
    msgProcessedTableHeight: number;
    msgProcessedTableScrollTop: number;
    logEventsTableHeight: number;

    msgDetails: ProcessedMsgsTable.Row;
}

class MainContent extends React.Component<Props, State> {
    constructor(props) {
        super(props);
        this.state = {
            tableWidth: 0,
            msgProcessedTableHeight: 0,
            msgProcessedTableScrollTop: 0,
            logEventsTableHeight: 0,
            msgDetails: undefined
        };
    }

    componentDidMount() {
        this._update();
        this._attatchResizeHandler();
    }

    _attatchResizeHandler() {
        var win = window;
        if (win.addEventListener)
            win.addEventListener('resize', this._onResize, false);
        else if (win.attachEvent)
            win.attachEvent('onresize', this._onResize);
        else
            win.onresize = this._onResize;
    }

    private _update = ():void => {
        var win = window;
        var widthOffset = 400;
        this.state.tableWidth = win.innerWidth - widthOffset;
        this.state.msgProcessedTableHeight = Math.round(win.innerHeight * 0.6) - 20;
        this.state.logEventsTableHeight = Math.round(win.innerHeight * 0.4) - 20;
        this.setState(this.state);
    };

    private _updateTimerId:number;
    private _onResize = () => {
        clearTimeout(this._updateTimerId);
        this._updateTimerId = setTimeout(this._update, 16);
    };
    private getUpperComponent = () => {
        if (this.state.msgDetails) {
            return MsgDetailsPane.Component({
                row: this.state.msgDetails,
                height: this.state.msgProcessedTableHeight,
                onBackClicked: () => {
                    this.props.onLogFilterStateChanged({
                        startTime: undefined,
                        endTime: undefined,
                        componentId: undefined
                    });
                    this.state.msgDetails = undefined;
                    this.setState(this.state);
                }
            });
        } else {
            return ProcessedMsgsTable.Component({
                rows: this.props.msgProcessedRows,
                tableWidth: this.state.tableWidth,
                tableHeight: this.state.msgProcessedTableHeight,
                filterState: this.props.filterState.msgProcessedFilterState,
                scrollTop: this.state.msgProcessedTableScrollTop,
                onScrollChanged: (scrollTop) => {
                    this.state.msgProcessedTableScrollTop = scrollTop;
                    this.setState(this.state);
                },
                onRowDoubleClicked: (row:ProcessedMsgsTable.Row) => {
                    this.state.msgDetails = row;
                    this.setState(this.state);
                    this.props.onLogFilterStateChanged({
                        startTime: row.original.processingStartTime,
                        endTime: row.original.processingEndTime,
                        componentId: row.original.recipientComponentId
                    });
                }
            });
        }
    };
    private getLogEventsTable = () => {
        return LogEventsTable.Component({
            rows: this.props.logEventRows,
            tableWidth: this.state.tableWidth,
            tableHeight: this.state.logEventsTableHeight,
            filterState: this.props.filterState.logEventFilterState
        });
    };

    render() {
        return R.div({id: 'main'},
            this.getUpperComponent(),
            this.getLogEventsTable()
        );
    }
}
export var Component = React.createFactory(MainContent);