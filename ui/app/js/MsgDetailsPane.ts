/// <reference path="../types/common.d.ts" />

import React = require('react');
import ProcessedMsgsTable = require('./ProcessedMsgsTable');
var BS = require('react-bootstrap');
var Button = React.createFactory(BS.Button);
var ButtonToolbar = React.createFactory(BS.ButtonToolbar);
var Row = React.createFactory(BS.Row);
var Col = React.createFactory(BS.Col);
var Table = React.createFactory(BS.Table);
var Panel = React.createFactory(BS.Panel);
var Input = React.createFactory(BS.Input);
var R = React.DOM;

export interface Props {
    height: number;
    row: ProcessedMsgsTable.Row;
    onBackClicked: () => void;
}

class MsgDetailsPane extends React.Component<Props, {}> {

    private getMsgDetailsColumn = () => {
        var panelHeader = (
            R.div({}, Row({}, Col({ xs: 1 }, Button({ bsSize: 'xsmall', onClick: this.props.onBackClicked }, 'Back')),
                Col({xs: 11},
                    Table({condensed: true, bordered: true, className: 'msgDetailsHeader'},
                        R.tr({}, R.td({}, 'Time'), R.td({}, this.props.row.formattedDate)),
                        R.tr({}, R.td({}, 'Sender'), R.td({}, this.props.row.senderName)),
                        R.tr({}, R.td({}, 'Recipient'), R.td({}, this.props.row.recipientName))))))
        );
        var rowSpan = this.props.row.original.stackTrace ? 6 : 12;
        return (
            Col({ xs: rowSpan, style: { height: this.props.height } },
                Panel({ header: panelHeader, className: 'formatted overviewPanel' },
                    R.pre({}, this.props.row.formattedMsgBody)))
        );
    };

    private getStacktraceColumn = () => {
        if (this.props.row.original.stackTrace)
            return Col({ xs: 6, style: { height: this.props.height } },
                    Panel({ header: 'Stacktrace', bsStyle: 'danger', className: 'overviewPanel' },
                        this.props.row.original.stackTrace));
        else
            return null;
    };

    render() {
        return (
            Row({},
                this.getMsgDetailsColumn(),
                this.getStacktraceColumn())
        );
    }
}
export var Component = React.createFactory(MsgDetailsPane);