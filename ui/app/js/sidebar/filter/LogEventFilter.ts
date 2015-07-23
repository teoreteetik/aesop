/// <reference path="../../../types/common.d.ts" />

import React = require('react');

module LogEventFilter {
    export interface FilterState {
        startTime: number;
        endTime: number;
        componentId: string;
    }

    export interface Props {
        filterState: FilterState;
        onFilterStateChanged: (filterState: FilterState) => void;
    }

}

class LogEventFilter extends React.Component<LogEventFilter.Props, {}> {
    render() {
        return null;
    }
}
export = LogEventFilter;
