/// <reference path="../../types/common.d.ts" />

import React = require('react');
import IdName = require('../util/IdName');
import AnalyzersList = require('./AnalyzersList');
import FilterSidebar = require('./filter/FilterSidebar');
var BS = require('react-bootstrap');
var Button = React.createFactory(BS.Button);
var Glyphicon = React.createFactory(BS.Glyphicon);
var R = React.DOM;

export interface Props {
    activeAnalyzerId: string;
    analyzers: AnalyzerState[];
    onAnalyzerClicked: (id:string) => void;
    filterState: FilterSidebar.FilterState;
    onFilterStateChanged: (filterState: FilterSidebar.FilterState) => void;
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
        var analyzerState = _.find(this.props.analyzers, analyzer => analyzer.idName.id === this.props.activeAnalyzerId);
        var filterProps: FilterSidebar.Props = {
            uniqueSenderIdNames: analyzerState.uniqueSenderIdNames,
            uniqueRecipientIdNames: analyzerState.uniqueRecipientIdNames,
            filterState: this.props.filterState,
            onFilterStateChanged: this.props.onFilterStateChanged
        };
        return FilterSidebar.Component(filterProps);
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
            buttons.push(Button({ bsSize: 'xsmall',
                                  onClick: () => this.setState({ currentView: CurrentView.ANALYZERS_LIST }) },
                            Glyphicon({ glyph: 'list' }),
                            'Analyzers'));
        if (this.state.currentView !== CurrentView.FILTER)
            buttons.push(Button({ bsSize: 'xsmall',
                                  onClick: () => this.setState({ currentView: CurrentView.FILTER }) },
                            Glyphicon({ glyph: 'filter' }),
                            'Filter'));
        return buttons;
    };

    render() {
        return (
            R.div({ id: 'sidebar' },
                R.ul({ className: 'sidebar-nav' },
                    R.li({ className: 'sidebar-brand' },
                        R.a({ href: '#' }, '')),
                    R.li({},
                        this.getVisibleButtons())),
                this.getCurrentContent())
        );
    }
}
export var Component = React.createFactory(Sidebar);