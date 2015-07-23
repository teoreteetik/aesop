/// <reference path="../../types/common.d.ts" />

import React = require('react');
import IdName = require('../util/IdName');
import AnalyzersList = require('./AnalyzersList');
import FilterSidebar = require('./filter/FilterSidebar');
var BS = require('react-bootstrap');
var Button = BS.Button;
var Glyphicon = BS.Glyphicon;
var R = React.DOM;

module Sidebar {
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
        unreadErrors: number;
    }
}

interface State {
    currentView: CurrentView;
}
enum CurrentView {
    ANALYZERS_LIST,
    FILTER
}


class Sidebar extends React.Component<Sidebar.Props, State> {
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
                return {
                    idName: a.idName,
                    componentIdNames: a.componentIdNames,
                    unreadErrors: a.unreadErrors
                }
            }),
            onAnalyzerClicked: this.props.onAnalyzerClicked
        };
        return React.jsx(`
            <AnalyzersList {...analyzersListProps}/>
        `);
    };

    private getFilterComponent = () => {
        var analyzerState = _.find(this.props.analyzers, analyzer => analyzer.idName.id === this.props.activeAnalyzerId);
        var filterProps: FilterSidebar.Props = {
            uniqueSenderIdNames: analyzerState.uniqueSenderIdNames,
            uniqueRecipientIdNames: analyzerState.uniqueRecipientIdNames,
            filterState: this.props.filterState,
            onFilterStateChanged: this.props.onFilterStateChanged,
            unreadErrors: analyzerState.unreadErrors
        };
        return React.jsx(`
            <FilterSidebar {...filterProps}/>
        `);
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
        if (this.state.currentView !== CurrentView.ANALYZERS_LIST) {
            var clickHandler = () => this.setState({ currentView: CurrentView.ANALYZERS_LIST });
            buttons.push(React.jsx(`
                <Button bsSize="xsmall" onClick={clickHandler}>
                    <Glyphicon glyph="list" />
                    Analyzers
                </Button>
            `));
        } else if (this.state.currentView !== CurrentView.FILTER) {
            var clickHandler = () => this.setState({currentView: CurrentView.FILTER});
            buttons.push(React.jsx(`
                <Button bsSize="xsmall" onClick={clickHandler}>
                    <Glyphicon glyph="filter" />
                    Filter
                </Button>
            `));
        }
        return buttons;
    };

    render() {
      return React.jsx(`
        <div id="sidebar">
            <ul className="sidebar-nav">
                <li className="sidebar-brand">
                    <a href="#">Aesop</a>
                </li>
                <li>
                    {this.getVisibleButtons()}
                </li>
            </ul>
            {this.getCurrentContent()}
        </div>
      `);
    }
}
export = Sidebar
