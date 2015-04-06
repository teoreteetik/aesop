/// <reference path="../../types/react/react.d.ts" />
/// <reference path="../../types/lib.d.ts" />

import React = require('react');
import IdName = require('../util/IdName');
import AnalyzersList = require('./AnalyzersList');
import MsgFilter = require('./filter/MsgFilter');
var BS = require('react-bootstrap');
var Button = React.createFactory(BS.Button);
var Glyphicon = React.createFactory(BS.Glyphicon);

var R = React.DOM;

export interface Props {
    activeAnalyzerId: string;
    analyzers: AnalyzerState[];
    onAnalyzerClicked: (id:string) => void;
    filterState: MsgFilter.FilterState;
    onFilterStateChanged: (filterState:MsgFilter.FilterState) => void;
}

export interface AnalyzerState {
    idName: IdName;
    componentIdNames: IdName[];
    uniqueSenderIdNames: IdName[];
    uniqueRecipientIdNames: IdName[];
}

enum CurrentView {
    ANALYZERS_LIST,
    FILTER
}
interface State {
    currentView: CurrentView;
}

class Sidebar extends React.Component<Props, State> {
    constructor(props) {
        super(props);
        this.state = {
            currentView: CurrentView.ANALYZERS_LIST
        };
    }

    private getAnalyzersListComponent = (): any => {
        var analyzersListProps: AnalyzersList.Props = {
            activeAnalyzerId: this.props.activeAnalyzerId,
            analyzers: this.props.analyzers.map(a => {
                return {idName: a.idName, componentIdNames: a.componentIdNames}
            }),
            onAnalyzerClicked: this.props.onAnalyzerClicked
        };
        return AnalyzersList.Component(analyzersListProps);
    };

    private getFilterComponent = () => {
        var analyzerState: AnalyzerState;
        this.props.analyzers.forEach(a => {
            if (a.idName.id === this.props.activeAnalyzerId)
                analyzerState = a;
        });
        var filterProps:MsgFilter.Props = {
            uniqueSenderIdNames: analyzerState.uniqueSenderIdNames,
            uniqueRecipientIdNames: analyzerState.uniqueRecipientIdNames,
            filterState: this.props.filterState,
            onFilterStateChanged: this.props.onFilterStateChanged
        };
        return MsgFilter.Component(filterProps);
    };

    private getCurrentContent = () => {
        switch (this.state.currentView) {
            case CurrentView.ANALYZERS_LIST:
                return this.getAnalyzersListComponent();
            case CurrentView.FILTER:
                return this.getFilterComponent();
        }
    };
    private getVisibleButtons = () => {
        var buttons = [];
        if (this.state.currentView !== CurrentView.ANALYZERS_LIST)
            buttons.push(Button({bsSize: 'xsmall', onClick: () => this.setState({currentView: CurrentView.ANALYZERS_LIST})},
                            Glyphicon({glyph: 'list'}),
                            'Analyzers'));
        if (this.state.currentView !== CurrentView.FILTER)
            buttons.push(Button({bsSize: 'xsmall', onClick: () => this.setState({currentView: CurrentView.FILTER})},
                            Glyphicon({glyph: 'filter'}),
                            'Filter'));
        return buttons;
    };

    render() {
        return R.
            div({id: 'sidebar'},
            R.ul({className: 'sidebar-nav'},
                R.li({className: 'sidebar-brand'},
                    R.a({href: '#'}, 'AkkaLIS')),
                R.li({},
                    this.getVisibleButtons())),
            this.getCurrentContent());
    }
}
export var Component = React.createFactory(Sidebar);