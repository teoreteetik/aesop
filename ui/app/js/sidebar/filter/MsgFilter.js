/// <reference path="../../../types/react/react.d.ts" />
/// <reference path="../../../types/lodash/lodash.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', 'lodash'], function (require, exports, React, _) {
    var BS = require('react-bootstrap');
    var Input = React.createFactory(BS.Input);
    var R = React.DOM;
    var _FilterSidebar = (function (_super) {
        __extends(_FilterSidebar, _super);
        function _FilterSidebar() {
            var _this = this;
            _super.apply(this, arguments);
            this.getPartyDropdown = function (label, currentValue, items, newPairGetter) {
                var options = items.map(function (idName) { return R.option({ title: idName.id, value: idName.id }, idName.name); });
                options.unshift(R.option({ title: 'any', value: '' }, ''));
                var fs = _this.props.filterState;
                var onChange = function (e) {
                    var selectedValue = e.target.value;
                    _this.props.onFilterStateChanged({
                        currentPair: newPairGetter(selectedValue),
                        addedPairs: fs.addedPairs,
                        searchText: fs.searchText
                    });
                };
                return Input({
                    value: currentValue === undefined ? '' : currentValue,
                    type: 'select',
                    label: label,
                    onChange: onChange
                }, options);
            };
            this.getSenderDropDown = function () {
                return _this.getPartyDropdown('Sender', _this.props.filterState.currentPair.senderId, _this.props.uniqueSenderIdNames, function (selectedValue) {
                    return {
                        senderId: selectedValue,
                        recipientId: _this.props.filterState.currentPair.recipientId
                    };
                });
            };
            this.getRecipientDropdown = function () {
                return _this.getPartyDropdown('Recipient', _this.props.filterState.currentPair.recipientId, _this.props.uniqueRecipientIdNames, function (selectedValue) {
                    return {
                        senderId: _this.props.filterState.currentPair.senderId,
                        recipientId: selectedValue
                    };
                });
            };
        }
        _FilterSidebar.prototype.render = function () {
            var _this = this;
            var fs = this.props.filterState;
            var addedPairs = fs.addedPairs.map(function (pair, index) {
                var senderName = pair.senderId ? _.find(_this.props.uniqueSenderIdNames, function (idName) { return idName.id === pair.senderId; }).name : 'Any';
                var recipientName = pair.recipientId ? _.find(_this.props.uniqueRecipientIdNames, function (idName) { return idName.id === pair.recipientId; }).name : 'Any';
                return R.div({}, R.div({ className: 'white' }, 'Sender: ' + senderName), R.div({ className: 'white' }, 'Recipient: ' + recipientName), R.button({
                    onClick: function () {
                        var newPairs = fs.addedPairs.splice(0);
                        newPairs.splice(index, 1);
                        _this.props.onFilterStateChanged({
                            currentPair: fs.currentPair,
                            addedPairs: newPairs,
                            searchText: fs.searchText
                        });
                    }
                }, 'Delete'));
            });
            return R.div({}, addedPairs, this.getSenderDropDown(), this.getRecipientDropdown(), R.button({
                onClick: function () {
                    var newPairs = fs.addedPairs.splice(0);
                    newPairs.push({
                        senderId: fs.currentPair.senderId,
                        recipientId: fs.currentPair.recipientId
                    });
                    _this.props.onFilterStateChanged({
                        currentPair: {
                            senderId: undefined,
                            recipientId: undefined
                        },
                        addedPairs: newPairs,
                        searchText: fs.searchText
                    });
                }
            }, 'Add'), Input({
                type: 'text',
                label: 'Message body',
                onChange: function (event) {
                    _this.props.onFilterStateChanged({
                        currentPair: fs.currentPair,
                        addedPairs: fs.addedPairs,
                        searchText: event.target.value
                    });
                }
            }));
        };
        return _FilterSidebar;
    })(React.Component);
    exports.Component = React.createFactory(_FilterSidebar);
});
//# sourceMappingURL=MsgFilter.js.map