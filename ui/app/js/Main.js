var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './ConnectForm', './Page'], function (require, exports, React, ConnectForm, Page) {
    var R = React.DOM;
    var _Main = (function (_super) {
        __extends(_Main, _super);
        function _Main(props) {
            var _this = this;
            _super.call(this, props);
            this.urlEntered = function (url) {
                try {
                    var socket = new WebSocket(url);
                    socket.onopen = function (ev) {
                        _this.setState({ socket: socket });
                    };
                    socket.onclose = function (ev) {
                        _this.setState({ socket: undefined });
                        alert('Connection refused');
                    };
                }
                catch (e) {
                    alert(e);
                }
            };
            this.getConnectForm = function () { return ConnectForm.Component({
                connectClicked: _this.urlEntered,
                url: 'ws://127.0.0.1:8900'
            }); };
            this.getUI = function () { return Page.Component({ socket: _this.state.socket }); };
            this.state = {
                socket: undefined
            };
        }
        _Main.prototype.render = function () {
            return (this.state.socket === undefined ? this.getConnectForm() : this.getUI());
        };
        return _Main;
    })(React.Component);
    var Main = React.createFactory(_Main);
    return Main;
});
//# sourceMappingURL=Main.js.map