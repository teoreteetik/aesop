/// <reference path="../../../types/react/react.d.ts" />
/// <reference path="../../../types/lodash/lodash.d.ts" />

import React = require('react');
import IdName = require('../../util/IdName');
import MultiSelect = require('../../util/MultiSelect');
import _ = require('lodash');
var BS = require('react-bootstrap');
var Input = React.createFactory(BS.Input);
var R = React.DOM;
import DateTimeInput = require('./DateTimeInput');

export interface FilterState {
    startTime: number;
    endTime: number;
    componentId: string;
}

export interface Props {
    filterState: FilterState;
    onFilterStateChanged: (filterState: FilterState) => void;
}

class LogEventFilter extends React.Component<Props, {}> {
    render() {
        return null;
    }
}
export var Component = React.createFactory(LogEventFilter);