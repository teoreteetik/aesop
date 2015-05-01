/// <reference path="../../../types/common.d.ts" />

import React = require('react');
import IdName = require('../../util/IdName');
import DateTimeInput = require('./DateTimeInput');
var BS = require('react-bootstrap');
var Button = React.createFactory(BS.Button);
var Input = React.createFactory(BS.Input);
var Glyphicon = React.createFactory(BS.Glyphicon);
var R = React.DOM;

export interface FilterState {
    currentPair: Pair;
    addedPairs: Pair[];
    startTime: number;
    endTime: number;
    searchText: string;
    processingState: ProcessingState;
    numOfNewFailedMsgs: number;
}

export enum ProcessingState {
    SUCCESS,
    FAIL
}

export interface Pair {
    senderId: string;
    recipientId: string;
}

export interface Props {
    filterState: FilterState;
    uniqueSenderIdNames: IdName[];
    uniqueRecipientIdNames: IdName[];
    onFilterStateChanged: (filterState:FilterState) => void;
}

class MsgProcessedFilter extends React.Component<Props, {}> {

    private getComponentDropdown = (label:string, currentValue:string, items:IdName[], currentPairGetter:(selectedValue:string) => Pair) => {
        var options = items.map(idName => R.option({ title: idName.id, value: idName.id }, idName.name));
        options.unshift(R.option({ title: 'any', value: '' }, ''));

        var fs = this.props.filterState;
        var onChange = (e) => {
            fs.currentPair = currentPairGetter(e.target.value);
            this.props.onFilterStateChanged(fs);
        };
        return Input({
            value: currentValue === undefined ? '' : currentValue,
            type: 'select',
            label: label,
            onChange: onChange
        }, options);
    };

    private getSenderDropDown = () => {
        return this.getComponentDropdown('Sender',
                                     this.props.filterState.currentPair.senderId,
                                     this.props.uniqueSenderIdNames,
                                     (selectedValue: string) => {
                                         return {
                                             senderId: selectedValue,
                                             recipientId: this.props.filterState.currentPair.recipientId
                                         }
        });
    };
    private getRecipientDropdown = () => {
        return this.getComponentDropdown('Recipient',
                                     this.props.filterState.currentPair.recipientId,
                                     this.props.uniqueRecipientIdNames,
                                     (selectedValue: string) => {
                                         return {
                                             senderId: this.props.filterState.currentPair.senderId,
                                             recipientId: selectedValue
                                         }
        });
    };

    private getProcessingStateDropdown = () => {
        var options = [R.option({ value: '' }, ''),
                       R.option({ value: ProcessingState[ProcessingState.SUCCESS]}, 'Successful'),
                       R.option({ value: ProcessingState[ProcessingState.FAIL]}, 'Failed (' + this.props.filterState.numOfNewFailedMsgs + ' new)')];
        return (
            Input({
                type: 'select',
                label: 'Processing status',
                onChange: (e) => {
                    var newState = _.clone(this.props.filterState);
                    newState.processingState = ProcessingState[<string>e.target.value];
                    this.props.onFilterStateChanged(newState);
                }
            }, options)
        );
    };

    private getAddedComponentPairs = () => {
        var fs = this.props.filterState;
        return (
            fs.addedPairs.map((pair, index) => {
                var senderName = pair.senderId ? _.find(this.props.uniqueSenderIdNames, idName => idName.id === pair.senderId).name
                                               : 'Any';
                var recipientName = pair.recipientId ? _.find(this.props.uniqueRecipientIdNames, idName => idName.id === pair.recipientId).name
                                                     : 'Any';
                return R.div({style: {border: '1px solid grey', padding: '5px'}},
                    Button({
                        style: {float: 'right', background: 'none', border: 'none', color: 'white'},
                        bsSize: 'xsmall',
                        onClick: () => {
                            var newState = _.clone(fs);
                            newState.addedPairs = fs.addedPairs.splice(0);
                            newState.addedPairs.splice(index, 1);
                            this.props.onFilterStateChanged(newState);
                        }
                    }, Glyphicon({glyph: 'remove-circle'})),
                    R.div({className: 'control-label', style: {wordWrap: 'break-word'}}, 'Sender: ' + senderName),
                    R.div({className: 'control-label', style: {wordWrap: 'break-word'}}, 'Recipient: ' + recipientName))
                })
        );
    };

    private getStartDateTimeInput = () => {
        return (
            DateTimeInput.Component({
                value: this.props.filterState.startTime,
                label: 'Start',
                onChange: (value: number) => {
                    var newState = _.clone(this.props.filterState);
                    newState.startTime = value;
                    this.props.onFilterStateChanged(newState);
                }
            })
        );
    };

    private getEndDateTimeInput = () =>{
        return (
            DateTimeInput.Component({
                value: this.props.filterState.endTime,
                label: 'End',
                onChange: (value: number) => {
                    var newState = _.clone(this.props.filterState);
                    newState.endTime = value;
                    this.props.onFilterStateChanged(newState);
                }
            })
        );
    };

    private getAddComponentPairButton = () => {
        var fs = this.props.filterState;
        return (
            Button({
                bsSize: 'small',
                onClick: () => {
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
                    this.props.onFilterStateChanged(newState);
                }
            }, 'Add')
        )
    };

    private getMsgBodySearchInput = () => {
        return (
            Input({
                type: 'textarea', label: 'Message body', onChange: (event) => {
                    var newState = _.clone(this.props.filterState);
                    newState.searchText = event.target.value;
                    this.props.onFilterStateChanged(newState);
                }
            })
        );
    };

    render() {
        return (
            R.div({},
                this.getProcessingStateDropdown(),
                this.getStartDateTimeInput(),
                this.getEndDateTimeInput(),
                this.getAddedComponentPairs(),
                this.getSenderDropDown(),
                this.getRecipientDropdown(),
                this.getAddComponentPairButton(),
                this.getMsgBodySearchInput())
        );
    }
}
export var Component = React.createFactory(MsgProcessedFilter);