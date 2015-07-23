/// <reference path="../types/common.d.ts" />
import React = require('react');
var R = React.DOM;
var BS = require('react-bootstrap');
var Input = BS.Input;
var Button = BS.Button;
var Row = BS.Row;
var Col = BS.Col;
var Panel = BS.Panel;

module ConnectForm {
    export interface Props {
        url: string;
        connectClicked: (url: string) => void;
    }
}
interface State  {
    url: string;
}

class ConnectForm extends React.Component<ConnectForm.Props, State> {
    constructor(props: ConnectForm.Props) {
        super(props);
        this.state = {
            url: props.url
        }
    }
    render() {
        var onChange = (event) => this.setState({url: event.target.value});
        var onClick = (event) => this.props.connectClicked(this.state.url);
        return React.jsx(`
            <Row style={{marginTop: '30px'}}>
                <Col xs={4} xsOffset={4}>
                    <Panel style={{margin: 'auto'}}>
                        <Input label="WebSocket URL" type="text" value={this.state.url} onChange={onChange}/>
                        <Button bsStyle="primary" onClick={onClick}>Connect</Button>
                    </Panel>
                </Col>
            </Row>
        `);
    }
}
export = ConnectForm;
