/// <reference path="../../../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './DateTimeInput'], function (require, exports, React, DateTimeInput) {
    var BS = require('react-bootstrap');
    var Button = React.createFactory(BS.Button);
    var Input = React.createFactory(BS.Input);
    var Glyphicon = React.createFactory(BS.Glyphicon);
    var R = React.DOM;
    (function (ProcessingState) {
        ProcessingState[ProcessingState["SUCCESS"] = 0] = "SUCCESS";
        ProcessingState[ProcessingState["FAIL"] = 1] = "FAIL";
    })(exports.ProcessingState || (exports.ProcessingState = {}));
    var ProcessingState = exports.ProcessingState;
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
                    fs.currentPair = currentPairGetter(e.target.value);
                    _this.props.onFilterStateChanged(fs);
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
            this.getProcessingStateDropdown = function () {
                var options = [R.option({ value: '' }, ''), R.option({ value: ProcessingState[0 /* SUCCESS */] }, 'Successful'), R.option({ value: ProcessingState[1 /* FAIL */] }, 'Failed (' + _this.props.filterState.numOfNewFailedMsgs + ' new)')];
                return (Input({
                    type: 'select',
                    label: 'Processing status',
                    onChange: function (e) {
                        var newState = _.clone(_this.props.filterState);
                        newState.processingState = ProcessingState[e.target.value];
                        _this.props.onFilterStateChanged(newState);
                    }
                }, options));
            };
            this.getAddedComponentPairs = function () {
                var fs = _this.props.filterState;
                return (fs.addedPairs.map(function (pair, index) {
                    var senderName = pair.senderId ? _.find(_this.props.uniqueSenderIdNames, function (idName) { return idName.id === pair.senderId; }).name : 'Any';
                    var recipientName = pair.recipientId ? _.find(_this.props.uniqueRecipientIdNames, function (idName) { return idName.id === pair.recipientId; }).name : 'Any';
                    return R.div({ style: { border: '1px solid grey', padding: '5px' } }, Button({
                        style: { float: 'right', background: 'none', border: 'none', color: 'white' },
                        bsSize: 'xsmall',
                        onClick: function () {
                            var newState = _.clone(fs);
                            newState.addedPairs = fs.addedPairs.splice(0);
                            newState.addedPairs.splice(index, 1);
                            _this.props.onFilterStateChanged(newState);
                        }
                    }, Glyphicon({ glyph: 'remove-circle' })), R.div({ className: 'control-label', style: { wordWrap: 'break-word' } }, 'Sender: ' + senderName), R.div({ className: 'control-label', style: { wordWrap: 'break-word' } }, 'Recipient: ' + recipientName));
                }));
            };
            this.getStartDateTimeInput = function () {
                return (DateTimeInput.Component({
                    value: _this.props.filterState.startTime,
                    label: 'Start',
                    onChange: function (value) {
                        var newState = _.clone(_this.props.filterState);
                        newState.startTime = value;
                        _this.props.onFilterStateChanged(newState);
                    }
                }));
            };
            this.getEndDateTimeInput = function () {
                return (DateTimeInput.Component({
                    value: _this.props.filterState.endTime,
                    label: 'End',
                    onChange: function (value) {
                        var newState = _.clone(_this.props.filterState);
                        newState.endTime = value;
                        _this.props.onFilterStateChanged(newState);
                    }
                }));
            };
            this.getAddComponentPairButton = function () {
                var fs = _this.props.filterState;
                return (Button({
                    bsSize: 'small',
                    onClick: function () {
                        var newState = _.clone(fs);
                        newState.addedPairs = fs.addedPairs.splice(0);
                        newState.addedPairs.push({
                            senderId: fs.currentPair.senderId,
                            recipientId: fs.currentPair.recipientId
                        });
                        newState.currentPair = {
                            senderId: undefined,
                            recipientId: undefined
                        };
                        _this.props.onFilterStateChanged(newState);
                    }
                }, 'Add'));
            };
            this.getMsgBodySearchInput = function () {
                return (Input({
                    type: 'textarea',
                    label: 'Message body',
                    onChange: function (event) {
                        var newState = _.clone(_this.props.filterState);
                        newState.searchText = event.target.value;
                        _this.props.onFilterStateChanged(newState);
                    }
                }));
            };
        }
        MsgProcessedFilter.prototype.render = function () {
            return (R.div({}, this.getProcessingStateDropdown(), this.getStartDateTimeInput(), this.getEndDateTimeInput(), this.getAddedComponentPairs(), this.getSenderDropDown(), this.getRecipientDropdown(), this.getAddComponentPairButton(), this.getMsgBodySearchInput()));
        };
        return MsgProcessedFilter;
    })(React.Component);
    exports.Component = React.createFactory(MsgProcessedFilter);
});
//# sourceMappingURL=MsgProcessedFilter.js.map