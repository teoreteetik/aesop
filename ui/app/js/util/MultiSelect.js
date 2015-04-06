/// <reference path="../../types/react/react.d.ts" />
/// <reference path="../../types/lib.d.ts" />
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
    var MultiSelect = (function (_super) {
        __extends(MultiSelect, _super);
        function MultiSelect() {
            _super.apply(this, arguments);
        }
        MultiSelect.prototype.render = function () {
            var p = this.props;
            var onChange = function (event) {
                var selectedValue = event.target.value;
                p.onSelectionChanged(selectedValue);
            };
            var options = p.items.map(function (idName) { return R.option({ title: idName.id, value: idName.id }, idName.name); });
            options.unshift(R.option({ title: 'any', value: '' }, undefined));
            return Input({
                value: p.selectedValue === undefined ? '' : p.selectedValue,
                type: 'select',
                label: p.label,
                onChange: onChange
            }, options);
        };
        return MultiSelect;
    })(React.Component);
    exports.Component = React.createFactory(MultiSelect);
});
//# sourceMappingURL=MultiSelect.js.map