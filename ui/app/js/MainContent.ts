/// <reference path="../types/react/react.d.ts" />

import React = require('react');
import WebSocketMsgs = require('./WebSocketMsgs');
import ProcessedMsgsTable = require('./ProcessedMsgsTable');
import FormatUtil = require('./util/FormatUtil');
import MsgFilter = require('./sidebar/filter/MsgFilter');

var R = React.DOM;

export interface Props {
    msgProcessedRows: ProcessedMsgsTable.Row[];
    filterState: MsgFilter.FilterState;
}
interface State {
    tableWidth: number;
    tableHeight: number;
}

class MainContent extends React.Component<Props, State> {
    constructor(props) {
        super(props);
        this.state = {
            tableWidth: 0,
            tableHeight: 0
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

    private _update = (): void => {
        var win = window;
        var widthOffset = 400;
        this.state.tableWidth = win.innerWidth - widthOffset;
        this.state.tableHeight = win.innerHeight - 200;
        this.setState(this.state);
    };

    private _updateTimerId: number;
    private _onResize = () => {
        clearTimeout(this._updateTimerId);
        this._updateTimerId = setTimeout(this._update, 16);
    };
    render() {
        return R.div({id: 'main'},
            ProcessedMsgsTable.Component({
                    rows: this.props.msgProcessedRows,
                    tableWidth: this.state.tableWidth,
                    tableHeight: this.state.tableHeight,
                    filterState: this.props.filterState
                }));
    }
}
export var Component = React.createFactory(MainContent);