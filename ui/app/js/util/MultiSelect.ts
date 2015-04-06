/// <reference path="../../types/react/react.d.ts" />
/// <reference path="../../types/lib.d.ts" />

import React = require('react');
import IdName = require('../util/IdName');
var R = React.DOM;
var BS = require('react-bootstrap');
var Input = React.createFactory(BS.Input);

export interface Props {
    items: IdName[];
    selectedValue: string;
    label?: string;
    onSelectionChanged: (selectedValue: string) => void;
}

class MultiSelect extends React.Component<Props, {}> {
    render() {
        var p = this.props;
        var onChange = (event) => {
            var selectedValue = event.target.value;
            p.onSelectionChanged(selectedValue);
        };
        var options = p.items.map(idName =>
            R.option({ title: idName.id,
                       value: idName.id
            }, idName.name));
        options.unshift(R.option({title: 'any', value: ''}, undefined))
        return Input({
                value: p.selectedValue === undefined ? '' : p.selectedValue,
                type: 'select',
                label: p.label,
                onChange: onChange
        }, options);
    }
}
export var Component = React.createFactory(MultiSelect);