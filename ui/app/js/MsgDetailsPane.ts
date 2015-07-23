/// <reference path="../types/common.d.ts" />

import React = require('react');
import ProcessedMsgsTable = require('./ProcessedMsgsTable');
var BS = require('react-bootstrap');
var Button = BS.Button;
var ButtonToolbar = BS.ButtonToolbar;
var Row = BS.Row;
var Col = BS.Col;
var Table = BS.Table;
var Panel = BS.Panel;
var Input = BS.Input;
var R = React.DOM;

module MsgDetailsPane {
    export interface Props {
        height: number;
        row: ProcessedMsgsTable.Row;
        onBackClicked: () => void;
    }
}

class MsgDetailsPane extends React.Component<MsgDetailsPane.Props, {}> {

    private getMsgDetailsColumn = () => {
        var panelHeader = React.jsx(`
                <div>
                    <Row>
                        <Col xs={1}>
                            <Button bsSize="xsmall" onClick={this.props.onBackClicked}>
                                Back
                            </Button>
                        </Col>
                        <Col xs={11}>
                            <Table condensed={true} bordered={true} className="msgDetailsHeader">
                                <tr>
                                    <td>Time</td>
                                    <td>{this.props.row.formattedDate}</td>
                                </tr>
                                <tr>
                                    <td>Sender</td>
                                    <td>{this.props.row.senderName}</td>
                                </tr>
                                <tr>
                                    <td>Recipient</td>
                                    <td>{this.props.row.recipientName}</td>
                                </tr>
                            </Table>
                        </Col>
                    </Row>
                </div>
        `);
        var rowSpan = this.props.row.original.stackTrace ? 6 : 12;
        return React.jsx(`
                <Col xs={rowSpan} style={{height: this.props.height}}>
                    <Panel header={panelHeader} className="formatted overviewPanel">
                        <pre>{this.props.row.formattedMsgBody}</pre>
                    </Panel>
                </Col>
        `);
    };

    private getStacktraceColumn = () => {
        if (this.props.row.original.stackTrace)
            return React.jsx(`
                <Col xs={6} style={{height: this.props.height}}>
                    <Panel header="Stacktrace" bsStyle="danger" className="overviewPanel">
                        {this.props.row.original.stackTrace}
                    </Panel>
                </Col>
            `);
        else
            return null;
    };

    render() {
        return React.jsx(`
            <Row>
                {this.getMsgDetailsColumn()}
                {this.getStacktraceColumn()}
            </Row>
        `);
    }
}
export = MsgDetailsPane;
