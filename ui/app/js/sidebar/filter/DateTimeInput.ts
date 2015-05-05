/// <reference path="../../../types/common.d.ts" />

import React = require('react');
import moment = require('moment');
var BS = require('react-bootstrap');
var Input = React.createFactory(BS.Input);
var Row = React.createFactory(BS.Row);
var Col = React.createFactory(BS.Col);

export interface Props {
    label: string;
    value: number;
    onChange: (time: number) => void;
}

interface State {
    timeString: string;
    dateString: string;
}

class DateTimeInput extends React.Component<Props, State> {
    constructor(props: Props) {
        super(props);
        if (props.value) {
            var date = moment(props.value);
            this.state = {
                timeString: date.format('HH:mm:ss'),
                dateString: date.format('DD.MM.YYYY')
            };
        } else {
            this.state = {
                timeString: undefined,
                dateString: undefined
            }
        }
    }
    private onChange: (event) => void = event => {
        var timeString = (<any>this.refs['timeInput']).getInputDOMNode().value;
        var dateString = (<any>this.refs['dateInput']).getInputDOMNode().value;
        var date: moment.Moment = moment(`${timeString} ${dateString}`, 'HH:mm:ss DD.MM.YYYY', true);
        if (date.isValid())
            this.props.onChange(date.valueOf());
        else
            this.props.onChange(undefined);
    };
    render() {
        return (
            Row({},
                Col({ xs: 5, style: { paddingRight: 0 } },
                    Input({
                        ref: 'timeInput',
                        label: `${this.props.label} time`,
                        type: 'text',
                        placeholder: 'HH:mm:ss',
                        onChange: this.onChange,
                        value: this.state.timeString
                    })
                ),
                Col({ xs: 7 },
                    Input({
                        ref: 'dateInput',
                        label: `${this.props.label} date`,
                        type: 'text',
                        placeholder: 'dd.MM.yyyy',
                        onChange: this.onChange,
                        value: this.state.dateString
                    })
                ))
        );
    }
}
export var Component = React.createFactory(DateTimeInput);