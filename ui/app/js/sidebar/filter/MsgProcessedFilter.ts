/// <reference path="../../../types/common.d.ts" />

import React = require('react');
import IdName = require('../../util/IdName');
import DateTimeInput = require('./DateTimeInput');
var BS = require('react-bootstrap');
var Input = React.createFactory(BS.Input);
var R = React.DOM;

export interface FilterState {
    currentPair: Pair;
    addedPairs: Pair[];
    startTime: number;
    endTime: number;
    searchText: string;
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
            var selectedValue = e.target.value;
            this.props.onFilterStateChanged({
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

    private getAddedComponentPairs = () => {
        var fs = this.props.filterState;
        return (
            fs.addedPairs.map((pair, index) => {
                var senderName = pair.senderId ? _.find(this.props.uniqueSenderIdNames, idName => idName.id === pair.senderId).name
                                               : 'Any';
                var recipientName = pair.recipientId ? _.find(this.props.uniqueRecipientIdNames, idName => idName.id === pair.recipientId).name
                                                     : 'Any';
                return R.div({},
                    R.div({className: 'control-label'}, 'Sender: ' + senderName),
                    R.div({className: 'control-label'}, 'Recipient: ' + recipientName),
                    R.button({
                        onClick: () => {
                            var newPairs = fs.addedPairs.splice(0);
                            newPairs.splice(index, 1);
                            this.props.onFilterStateChanged({
                                startTime: fs.startTime,
                                endTime: fs.endTime,
                                currentPair: fs.currentPair,
                                addedPairs: newPairs,
                                searchText: fs.searchText
                            });
                        }
                    }, 'Delete'))
                })
        );
    };

    private getStartDateTimeInput = () => {
        return (
            DateTimeInput.Component({
                value: this.props.filterState.startTime,
                label: 'Start',
                onChange: (value: number) => {
                    var fs = this.props.filterState;
                    this.props.onFilterStateChanged({
                        currentPair: fs.currentPair,
                        addedPairs: fs.addedPairs,
                        startTime: value,
                        endTime: fs.endTime,
                        searchText: fs.searchText
                    });
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
                    var fs = this.props.filterState;
                    this.props.onFilterStateChanged({
                        currentPair: fs.currentPair,
                        addedPairs: fs.addedPairs,
                        startTime: fs.startTime,
                        endTime: value,
                        searchText: fs.searchText
                    });
                }
            })
        );
    };

    private getAddComponentPairButton = () => {
        var fs = this.props.filterState;
        return (
            R.button({
                onClick: () => {
                    var newPairs = fs.addedPairs.splice(0);
                    newPairs.push({
                        senderId: fs.currentPair.senderId,
                        recipientId: fs.currentPair.recipientId
                    });
                    this.props.onFilterStateChanged({
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
            }, 'Add')
        )
    };

    private getMsgBodySearchInput = () => {
        var fs = this.props.filterState;
        return (
            Input({
                type: 'text', label: 'Message body', onChange: (event) => {
                    this.props.onFilterStateChanged({
                        startTime: fs.startTime,
                        endTime: fs.endTime,
                        currentPair: fs.currentPair,
                        addedPairs: fs.addedPairs,
                        searchText: event.target.value
                    });
                }
            })
        );
    };

    render() {
        return (
            R.div({},
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