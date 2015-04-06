/// <reference path="../../../types/react/react.d.ts" />
/// <reference path="../../../types/lodash/lodash.d.ts" />

import React = require('react');
import IdName = require('../../util/IdName');
import MultiSelect = require('../../util/MultiSelect');
import _ = require('lodash');
var BS = require('react-bootstrap');
var Input = React.createFactory(BS.Input);
var R = React.DOM;

export interface FilterState {
    currentPair: Pair;
    addedPairs: Pair[];
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

class _FilterSidebar extends React.Component<Props, {}> {
    private getPartyDropdown = (label:string, currentValue:string, items:IdName[], newPairGetter:(selectedValue:string) => Pair) => {
        var options = items.map(idName =>
            R.option({title: idName.id, value: idName.id},
                idName.name));
        options.unshift(R.option({title: 'any', value: ''}, ''));
        var fs = this.props.filterState;
        var onChange = (e) => {
            var selectedValue = e.target.value;
            this.props.onFilterStateChanged({
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
    private getSenderDropDown = () => {
        return this.getPartyDropdown('Sender',
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
        return this.getPartyDropdown('Recipient',
                                     this.props.filterState.currentPair.recipientId,
                                     this.props.uniqueRecipientIdNames,
                                     (selectedValue: string) => {
                                         return {
                                             senderId: this.props.filterState.currentPair.senderId,
                                             recipientId: selectedValue
                                         }
        });
    };

    render() {
        var fs = this.props.filterState;
        var addedPairs = fs.addedPairs.map((pair, index) => {
            var senderName = pair.senderId ? _.find(this.props.uniqueSenderIdNames, idName => idName.id === pair.senderId).name : 'Any';
            var recipientName = pair.recipientId ? _.find(this.props.uniqueRecipientIdNames, idName => idName.id === pair.recipientId).name : 'Any';
            return R.div({},
                R.div({className: 'white'}, 'Sender: ' + senderName),
                R.div({className: 'white'}, 'Recipient: ' + recipientName),
                R.button({
                    onClick: () => {
                        var newPairs = fs.addedPairs.splice(0);
                        newPairs.splice(index, 1);
                        this.props.onFilterStateChanged({
                            currentPair: fs.currentPair,
                            addedPairs: newPairs,
                            searchText: fs.searchText
                        });
                    }
                }, 'Delete'))
        });
        return R.div({},
            addedPairs,
            this.getSenderDropDown(),
            this.getRecipientDropdown(),
            R.button({
                onClick: () => {
                    var newPairs = fs.addedPairs.splice(0);
                    newPairs.push({
                        senderId: fs.currentPair.senderId,
                        recipientId: fs.currentPair.recipientId
                    });
                    this.props.onFilterStateChanged({
                        currentPair: {
                            senderId: undefined,
                            recipientId: undefined
                        },
                        addedPairs: newPairs,
                        searchText: fs.searchText
                    });
                }
            }, 'Add'),
            Input({
                type: 'text', label: 'Message body', onChange: (event) => {
                    this.props.onFilterStateChanged({
                        currentPair: fs.currentPair,
                        addedPairs: fs.addedPairs,
                        searchText: event.target.value
                    });
                }
            }));
    }
}
export var Component = React.createFactory(_FilterSidebar);