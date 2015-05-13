/// <reference path="../types/common.d.ts" />
import React = require('react');
var R = React.DOM;
var BS = require('react-bootstrap');
var Input = React.createFactory(BS.Input);
var Button = React.createFactory(BS.Button);
var Row = React.createFactory(BS.Row);
var Col = React.createFactory(BS.Col);
var Panel = React.createFactory(BS.Panel);

export interface Props {
    url: string;
    connectClicked: (url: string) => void;
}

interface State  { 
    url: string;
}

class ConnectForm extends React.Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {
            url: props.url
        }
    }
    render() {
        return (
            Row({ style: { marginTop: '30px' } },
                Col({xs: 4, xsOffset: 4},
                    Panel({ style: {margin: 'auto'} },
                        Input(
                            {
                                label: 'WebSocket URL',
                                type: 'text',
                                value: this.state.url,
                                onChange: (event) => this.setState({url: event.target.value})
                            }
                        ),
                        Button(
                            {
                                bsStyle: 'primary',
                                onClick: (event) => this.props.connectClicked(this.state.url)
                            },
                            'Connect'
                        )
                    )
                )
            )
        );
    }
}
export var Component = React.createFactory<Props>(ConnectForm);