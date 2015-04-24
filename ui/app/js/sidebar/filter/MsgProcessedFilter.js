/// <reference path="../../../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './DateTimeInput'], function (require, exports, React, DateTimeInput) {
    var BS = require('react-bootstrap');
    var Input = React.createFactory(BS.Input);
    var R = React.DOM;
    var MsgProcessedFilter = (function (_super) {
        __extends(MsgProcessedFilter, _super);
        function MsgProcessedFilter() {
            var _this = this;
            _super.apply(this, arguments);
            this.getComponentDropdown = function (label, currentValue, items, currentPairGetter) {
                var options = items.map(function (idName) { return R.option({ title: idName.id, value: idName.id }, idName.name); });
                options.unshift(R.option({ title: 'any', value: '' }, ''));
                var fs = _this.props.filterState;
                var onChange = function (e) {
                    var selectedValue = e.target.value;
                    _this.props.onFilterStateChanged({
                        startTime: undefined,
                        endTime: undefined,
                        currentPair: currentPairGetter(selectedValue),
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
                return _this.getComponentDropdown('Sender', _this.props.filterState.currentPair.senderId, _this.props.uniqueSenderIdNames, function (selectedValue) {
                    return {
                        senderId: selectedValue,
                        recipientId: _this.props.filterState.currentPair.recipientId
                    };
                });
            };
            this.getRecipientDropdown = function () {
                return _this.getComponentDropdown('Recipient', _this.props.filterState.currentPair.recipientId, _this.props.uniqueRecipientIdNames, function (selectedValue) {
                    return {
                        senderId: _this.props.filterState.currentPair.senderId,
                        recipientId: selectedValue
                    };
                });
            };
            this.getAddedComponentPairs = function () {
                var fs = _this.props.filterState;
                return (fs.addedPairs.map(function (pair, index) {
                    var senderName = pair.senderId ? _.find(_this.props.uniqueSenderIdNames, function (idName) { return idName.id === pair.senderId; }).name : 'Any';
                    var recipientName = pair.recipientId ? _.find(_this.props.uniqueRecipientIdNames, function (idName) { return idName.id === pair.recipientId; }).name : 'Any';
                    return R.div({}, R.div({ className: 'control-label' }, 'Sender: ' + senderName), R.div({ className: 'control-label' }, 'Recipient: ' + recipientName), R.button({
                        onClick: function () {
                            var newPairs = fs.addedPairs.splice(0);
                            newPairs.splice(index, 1);
                            _this.props.onFilterStateChanged({
                                startTime: fs.startTime,
                                endTime: fs.endTime,
                                currentPair: fs.currentPair,
                                addedPairs: newPairs,
                                searchText: fs.searchText
                            });
                        }
                    }, 'Delete'));
                }));
            };
            this.getStartDateTimeInput = function () {
                return (DateTimeInput.Component({
                    value: _this.props.filterState.startTime,
                    label: 'Start',
                    onChange: function (value) {
                        var fs = _this.props.filterState;
                        _this.props.onFilterStateChanged({
                            currentPair: fs.currentPair,
                            addedPairs: fs.addedPairs,
                            startTime: value,
                            endTime: fs.endTime,
                            searchText: fs.searchText
                        });
                    }
                }));
            };
            this.getEndDateTimeInput = function () {
                return (DateTimeInput.Component({
                    value: _this.props.filterState.endTime,
                    label: 'End',
                    onChange: function (value) {
                        var fs = _this.props.filterState;
                        _this.props.onFilterStateChanged({
                            currentPair: fs.currentPair,
                            addedPairs: fs.addedPairs,
                            startTime: fs.startTime,
                            endTime: value,
                            searchText: fs.searchText
                        });
                    }
                }));
            };
            this.getAddComponentPairButton = function () {
                var fs = _this.props.filterState;
                return (R.button({
                    onClick: function () {
                        var newPairs = fs.addedPairs.splice(0);
                        newPairs.push({
                            senderId: fs.currentPair.senderId,
                            recipientId: fs.currentPair.recipientId
                        });
                        _this.props.onFilterStateChanged({
                            startTime: fs.startTime,
                            endTime: fs.endTime,
                            currentPair: {
                                senderId: undefined,
                                recipientId: undefined
                            },
                            addedPairs: newPairs,
                            searchText: fs.searchText
                        });
                    }
                }, 'Add'));
            };
            this.getMsgBodySearchInput = function () {
                var fs = _this.props.filterState;
                return (Input({
                    type: 'text',
                    label: 'Message body',
                    onChange: function (event) {
                        _this.props.onFilterStateChanged({
                            startTime: fs.startTime,
                            endTime: fs.endTime,
                            currentPair: fs.currentPair,
                            addedPairs: fs.addedPairs,
                            searchText: event.target.value
                        });
                    }
                }));
            };
        }
        MsgProcessedFilter.prototype.render = function () {
            return (R.div({}, this.getStartDateTimeInput(), this.getEndDateTimeInput(), this.getAddedComponentPairs(), this.getSenderDropDown(), this.getRecipientDropdown(), this.getAddComponentPairButton(), this.getMsgBodySearchInput()));
        };
        return MsgProcessedFilter;
    })(React.Component);
    exports.Component = React.createFactory(MsgProcessedFilter);
});
//# sourceMappingURL=MsgProcessedFilter.js.map