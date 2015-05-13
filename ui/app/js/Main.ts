import React = require('react');
var R = React.DOM;
import ConnectForm = require('./ConnectForm');
import Page = require('./Page');

interface State {
    socket: WebSocket;
}

class _Main extends React.Component<{}, State> {
    constructor(props) {
        super(props);
        this.state = {
            socket: undefined
        }
    }
    private urlEntered = (url: string): void => {
        try {
            var socket = new WebSocket(url);
            socket.onopen = (ev: Event) => {
                this.setState({ socket: socket });
            };
            socket.onclose = (ev: CloseEvent) => {
                this.setState({ socket: undefined });
                alert('Connection refused');
            };
        } catch (e) {
            alert(e);
        }
    };
    private getConnectForm = (): React.ReactElement<ConnectForm.Props> => ConnectForm.Component({
        connectClicked: this.urlEntered,
        url: 'ws://127.0.0.1:8900'
    });
    private getUI = (): React.ReactElement<Page.Props> => Page.Component({socket: this.state.socket});
    render() {
        return (
            this.state.socket === undefined ? this.getConnectForm() : this.getUI()
        );
    }
}
var Main = React.createFactory(_Main);
export = Main;