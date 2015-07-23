/// <reference path="../../../types/common.d.ts" />

import React = require('react');
import MsgProcessedFilter = require('./MsgProcessedFilter');
import LogEventFilter = require('./LogEventFilter');
import IdName = require('../../util/IdName');
var R = React.DOM;

module FilterSidebar {
    export interface FilterState {
        msgProcessedFilterState: MsgProcessedFilter.FilterState;
        logEventFilterState: LogEventFilter.FilterState;
    }

    export interface Props {
        filterState: FilterState;
        uniqueSenderIdNames: IdName[];
        uniqueRecipientIdNames: IdName[];
        onFilterStateChanged: (filterState:FilterState) => void;
        unreadErrors: number;
    }
}


class FilterSidebar extends React.Component<FilterSidebar.Props, {}> {
    private getMsgProcessedFilterProps = (): MsgProcessedFilter.Props => {
        return {
            filterState: this.props.filterState.msgProcessedFilterState,
            uniqueSenderIdNames: this.props.uniqueSenderIdNames,
            uniqueRecipientIdNames: this.props.uniqueRecipientIdNames,
            onFilterStateChanged: (newState: MsgProcessedFilter.FilterState) => {
                this.props.onFilterStateChanged({
                    msgProcessedFilterState: newState,
                    logEventFilterState: this.props.filterState.logEventFilterState
                });
            },
            unreadErrors: this.props.unreadErrors
        };
    };
    private getLogEventFilterProps = (): LogEventFilter.Props => {
        return {
            filterState: this.props.filterState.logEventFilterState,
            onFilterStateChanged: (newState: LogEventFilter.FilterState) => {
                this.props.onFilterStateChanged({
                    msgProcessedFilterState: this.props.filterState.msgProcessedFilterState,
                    logEventFilterState: newState
                });
            }
        }
    };

    render() {
        return React.jsx(`
            <div>
                <MsgProcessedFilter {...this.getMsgProcessedFilterProps()}/>
                <LogEventFilter {...this.getLogEventFilterProps()}/>
            </div>
        `);
    }
}
export = FilterSidebar;
