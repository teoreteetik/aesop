/// <reference path="../../../types/common.d.ts" />

import React = require('react');
import IdName = require('../../util/IdName');
import DateTimeInput = require('./DateTimeInput');
import ProcessingState = require('./ProcessingState');
var BS = require('react-bootstrap');
var Button = BS.Button;
var Input = BS.Input;
var Glyphicon = BS.Glyphicon;
var R = React.DOM;

module MsgProcessedFilter {
    export interface Props {
        filterState: FilterState;
        unreadErrors: number;
        uniqueSenderIdNames: IdName[];
        uniqueRecipientIdNames: IdName[];
        onFilterStateChanged: (filterState:FilterState) => void;
    }
    export interface FilterState {
        currentPair: Pair;
        addedPairs: Pair[];
        startTime: number;
        endTime: number;
        searchText: string;
        processingState: ProcessingState;
    }


    export interface Pair {
        senderId: string;
        recipientId: string;
    }

}

class MsgProcessedFilter extends React.Component<MsgProcessedFilter.Props, {}> {

    private getComponentDropdown = (label: string,
                                    currentValue: string,
                                    items: IdName[],
                                    currentPairGetter: (selectedValue:string) => MsgProcessedFilter.Pair) => {
        var options = items.map(idName => React.jsx(`
            <option title={idName.id} value={idName.id}>
                {idName.name}
            </option>
        `));
        options.unshift(React.jsx(`
            <option title="any" value="">
            </option>
        `));

        var fs = this.props.filterState;
        var onChange = (e) => {
            fs.currentPair = currentPairGetter(e.target.value);
            this.props.onFilterStateChanged(fs);
        };
        return React.jsx(`
            <Input value={currentValue === undefined ? '' : currentValue}
                   type="select"
                   label={label}
                   onChange={onChange}>
                {options}
            </Input>
        `);
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
        var options = [
            React.jsx(`<option value=""></option>`),
            React.jsx(`<option value={ProcessingState[ProcessingState.SUCCESS]}>Successful</option>`),
            React.jsx(`<option value={ProcessingState[ProcessingState.FAIL]}>Failed ({this.props.unreadErrors} new)</option>`)
        ];
        var onChange = (e) => {
            var newState = _.clone(this.props.filterState);
            newState.processingState = ProcessingState[<string>e.target.value];
            this.props.onFilterStateChanged(newState);
        };
        return React.jsx(`
            <Input type="select"
                   label="Processing status"
                   onChange={onChange}>
                {options}
            </Input>
        `);
    };

    private getAddedComponentPairs = () => {
        var fs = this.props.filterState;
        return (
            fs.addedPairs.map((pair, index) => {
                var senderName = pair.senderId ? _.find(this.props.uniqueSenderIdNames, idName => idName.id === pair.senderId).name
                                               : 'Any';
                var recipientName = pair.recipientId ? _.find(this.props.uniqueRecipientIdNames, idName => idName.id === pair.recipientId).name
                                                     : 'Any';
                var onClick =  () => {
                    var newState = _.clone(fs);
                    newState.addedPairs = fs.addedPairs.splice(0);
                    newState.addedPairs.splice(index, 1);
                    this.props.onFilterStateChanged(newState);
                };
                return React.jsx(`
                    <div style={{border: '1px solid grey', padding: '5px'}}>
                        <Button style={{float: 'right', background: 'none', border: 'none', color: 'white'}}
                                bsStyle="xsmall"
                                onClick={onClick}>
                            <Glyphicon glyph="remove-circle"/>
                        </Button>
                        <div className="control-label" style={{wordWrap: 'break-word'}}>
                            Sender: {senderName}
                        </div>
                        <div className="control-label" style={{wordWrap: 'break-word'}}>
                            Recipient: {recipientName}
                        </div>
                    </div>
                `);
            })
        );
    };

    private getStartDateTimeInput = () => {
        var onChange = (value: number) => {
            var newState = _.clone(this.props.filterState);
            newState.startTime = value;
            this.props.onFilterStateChanged(newState);
        };
        return React.jsx(`
            <DateTimeInput value={this.props.filterState.startTime}
                           label="Start"
                           onChange={onChange}/>
        `);
    };

    private getEndDateTimeInput = () =>{
        var onChange = (value: number) => {
            var newState = _.clone(this.props.filterState);
            newState.endTime = value;
            this.props.onFilterStateChanged(newState);
        };
        return React.jsx(`
            <DateTimeInput value={this.props.filterState.endTime}
                           label="End"
                           onChange={onChange}/>
        `);
    };

    private getAddComponentPairButton = () => {
        var fs = this.props.filterState;
        var onClick = () => {
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
        };
        return React.jsx(`
            <Button bsSize="small"
                    onClick={onClick}>
                Add
            </Button>
        `);
    };

    private getMsgBodySearchInput = () => {
        var onChange = (event) => {
            var newState = _.clone(this.props.filterState);
            newState.searchText = event.target.value;
            this.props.onFilterStateChanged(newState);
        };
        return React.jsx(`
            <Input type="textarea"
                   label="Message body"
                   onChange={onChange}/>
        `);
    };

    render() {
        return React.jsx(`
            <div>
                {this.getProcessingStateDropdown()}
                {this.getStartDateTimeInput()}
                {this.getEndDateTimeInput()}
                {this.getAddedComponentPairs()}
                {this.getSenderDropDown()}
                {this.getRecipientDropdown()}
                {this.getAddComponentPairButton()}
                {this.getMsgBodySearchInput()}
            </div>
        `);
    }
}
export = MsgProcessedFilter;
