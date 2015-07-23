/// <reference path="../types/common.d.ts" />

import React = require('react');
import ProcessedMsgsTable = require('./ProcessedMsgsTable');
import LogEventsTable = require('./LogEventsTable');
import FilterSidebar = require('./sidebar/filter/FilterSidebar');
import LogEventFilter = require('./sidebar/filter/LogEventFilter');
import MsgDetailsPane = require('./MsgDetailsPane');
var R = React.DOM;

module MainContent {
    export interface Props {
        msgProcessedRows: ProcessedMsgsTable.Row[];
        logEventRows: LogEventsTable.Row[];
        filterState: FilterSidebar.FilterState;
        onLogFilterStateChanged: (newState: LogEventFilter.FilterState) => void;
        markRowAsRead: (row: ProcessedMsgsTable.Row) => void;
    }
}


interface State {
    tableWidth: number;
    msgProcessedTableHeight: number;
    msgProcessedTableScrollTop: number;
    logEventsTableHeight: number;

    msgDetails: ProcessedMsgsTable.Row;
}

class MainContent extends React.Component<MainContent.Props, State> {
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
        this.update();
        this.attatchResizeHandler();
    }

    private attatchResizeHandler = () => {
        var win = window;
        if (win.addEventListener)
            win.addEventListener('resize', this._onResize, false);
        else if (win.attachEvent)
            win.attachEvent('onresize', this._onResize);
        else
            win.onresize = this._onResize;
    };

    private update = () => {
        var win = window;
        var widthOffset = 300;
        this.state.tableWidth = win.innerWidth - widthOffset;
        this.state.msgProcessedTableHeight = Math.round(win.innerHeight * 0.6) - 20;
        this.state.logEventsTableHeight = Math.round(win.innerHeight * 0.4) - 20;
        this.setState(this.state);
    };

    private updateTimerId: number;
    private _onResize = () => {
        clearTimeout(this.updateTimerId);
        this.updateTimerId = setTimeout(this.update, 16);
    };

    private getUpperComponent = () => this.state.msgDetails ? this.getMsgDetailsPane()
                                                            : this.getProcessedMsgsTable();

    private getMsgDetailsPane = () => {
        var onBackClicked = () => {
            this.props.onLogFilterStateChanged({
                startTime: undefined,
                endTime: undefined,
                componentId: undefined
            })
            this.state.msgDetails = undefined;
            this.setState(this.state);
        };
        return React.jsx(`
            <MsgDetailsPane row={this.state.msgDetails}
                            height={this.state.msgProcessedTableHeight}
                            onBackClicked={onBackClicked}/>
        `);
    };

    private getProcessedMsgsTable = () => {
        var Facto = React.createFactory(ProcessedMsgsTable);
        var scrollChanged =  (scrollTop) => {
            this.state.msgProcessedTableScrollTop = scrollTop;
            this.setState(this.state);
        };
        var onRowDoubleClicked = (row:ProcessedMsgsTable.Row) => {
            this.state.msgDetails = row;
            this.setState(this.state);
            this.props.markRowAsRead(row);
            this.props.onLogFilterStateChanged({
                startTime: row.original.processingStartTime,
                endTime: row.original.processingEndTime,
                componentId: row.original.recipientComponentId
            });
        };
        return (React.jsx(`
            <ProcessedMsgsTable rows={this.props.msgProcessedRows}
                                tableWidth={this.state.tableWidth}
                                tableHeight={this.state.msgProcessedTableHeight}
                                filterState={this.props.filterState.msgProcessedFilterState}
                                scrollTop={this.state.msgProcessedTableScrollTop}
                                onScrollChanged={scrollChanged}
                                onRowDoubleClicked={onRowDoubleClicked}
            />
        `));
    };

    private getLogEventsTable = () => {
        return React.jsx(`
            <LogEventsTable
                rows={this.props.logEventRows}
                tableWidth={this.state.tableWidth}
                tableHeight={this.state.logEventsTableHeight}
                filterState={this.props.filterState.logEventFilterState}/>
        `);
    };

    render() {
        return React.jsx(`
            <div id="main">
                {this.getUpperComponent()}
                {this.getLogEventsTable()}
            </div>
        `);

    }
}
export = MainContent;
