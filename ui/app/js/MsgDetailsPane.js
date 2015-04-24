/// <reference path="../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react'], function (require, exports, React) {
    var BS = require('react-bootstrap');
    var Button = React.createFactory(BS.Button);
    var ButtonToolbar = React.createFactory(BS.ButtonToolbar);
    var Row = React.createFactory(BS.Row);
    var Col = React.createFactory(BS.Col);
    var Table = React.createFactory(BS.Table);
    var Panel = React.createFactory(BS.Panel);
    var Input = React.createFactory(BS.Input);
    var R = React.DOM;
    var MsgDetailsPane = (function (_super) {
        __extends(MsgDetailsPane, _super);
        function MsgDetailsPane() {
            var _this = this;
            _super.apply(this, arguments);
            this.getMsgDetailsColumn = function () {
                var panelHeader = (R.div({}, Row({}, Col({ xs: 1 }, Button({ bsSize: 'xsmall', onClick: _this.props.onBackClicked }, 'Back')), Col({ xs: 11 }, Table({ condensed: true, bordered: true, className: 'msgDetailsHeader' }, R.tr({}, R.td({}, 'Time'), R.td({}, _this.props.row.formattedDate)), R.tr({}, R.td({}, 'Sender'), R.td({}, _this.props.row.senderName)), R.tr({}, R.td({}, 'Recipient'), R.td({}, _this.props.row.recipientName)))))));
                var rowSpan = _this.props.row.original.stackTrace ? 6 : 12;
                return (Col({ xs: rowSpan, style: { height: _this.props.height } }, Panel({ header: panelHeader, className: 'formatted overviewPanel' }, R.pre({}, _this.props.row.formattedMsgBody))));
            };
            this.getStacktraceColumn = function () {
                if (_this.props.row.original.stackTrace)
                    return Col({ xs: 6, style: { height: _this.props.height } }, Panel({ header: 'Stacktrace', bsStyle: 'danger', className: 'overviewPanel' }, _this.props.row.original.stackTrace));
                else
                    return null;
            };
        }
        MsgDetailsPane.prototype.render = function () {
            return (Row({}, this.getMsgDetailsColumn(), this.getStacktraceColumn()));
        };
        return MsgDetailsPane;
    })(React.Component);
    exports.Component = React.createFactory(MsgDetailsPane);
});
//# sourceMappingURL=MsgDetailsPane.js.map