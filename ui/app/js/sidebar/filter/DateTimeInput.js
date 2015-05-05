/// <reference path="../../../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', 'moment'], function (require, exports, React, moment) {
    var BS = require('react-bootstrap');
    var Input = React.createFactory(BS.Input);
    var Row = React.createFactory(BS.Row);
    var Col = React.createFactory(BS.Col);
    var DateTimeInput = (function (_super) {
        __extends(DateTimeInput, _super);
        function DateTimeInput(props) {
            var _this = this;
            _super.call(this, props);
            this.onChange = function (event) {
                var timeString = _this.refs['timeInput'].getInputDOMNode().value;
                var dateString = _this.refs['dateInput'].getInputDOMNode().value;
                var date = moment("" + timeString + " " + dateString, 'HH:mm:ss DD.MM.YYYY', true);
                if (date.isValid())
                    _this.props.onChange(date.valueOf());
                else
                    _this.props.onChange(undefined);
            };
            if (props.value) {
                var date = moment(props.value);
                this.state = {
                    timeString: date.format('HH:mm:ss'),
                    dateString: date.format('DD.MM.YYYY')
                };
            }
            else {
                this.state = {
                    timeString: undefined,
                    dateString: undefined
                };
            }
        }
        DateTimeInput.prototype.render = function () {
            return (Row({}, Col({ xs: 5, style: { paddingRight: 0 } }, Input({
                ref: 'timeInput',
                label: "" + this.props.label + " time",
                type: 'text',
                placeholder: 'HH:mm:ss',
                onChange: this.onChange,
                value: this.state.timeString
            })), Col({ xs: 7 }, Input({
                ref: 'dateInput',
                label: "" + this.props.label + " date",
                type: 'text',
                placeholder: 'dd.MM.yyyy',
                onChange: this.onChange,
                value: this.state.dateString
            }))));
        };
        return DateTimeInput;
    })(React.Component);
    exports.Component = React.createFactory(DateTimeInput);
});
//# sourceMappingURL=DateTimeInput.js.map