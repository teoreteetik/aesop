var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react'], function (require, exports, React) {
    var R = React.DOM;
    var BS = require('react-bootstrap');
    var Input = React.createFactory(BS.Input);
    var Button = React.createFactory(BS.Button);
    var Row = React.createFactory(BS.Row);
    var Col = React.createFactory(BS.Col);
    var Panel = React.createFactory(BS.Panel);
    var ConnectForm = (function (_super) {
        __extends(ConnectForm, _super);
        function ConnectForm(props) {
            _super.call(this, props);
            this.state = {
                url: props.url
            };
        }
        ConnectForm.prototype.render = function () {
            var _this = this;
            return (Row({ style: { marginTop: '30px' } }, Col({ xs: 4, xsOffset: 4 }, Panel({ style: { margin: 'auto' } }, Input({
                label: 'WebSocket URL',
                type: 'text',
                value: this.state.url,
                onChange: function (event) { return _this.setState({ url: event.target.value }); }
            }), Button({
                bsStyle: 'primary',
                onClick: function (event) { return _this.props.connectClicked(_this.state.url); }
            }, 'Connect')))));
        };
        return ConnectForm;
    })(React.Component);
    exports.Component = React.createFactory(ConnectForm);
});
//# sourceMappingURL=ConnectForm.js.map