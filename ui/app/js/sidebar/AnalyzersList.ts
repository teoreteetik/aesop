/// <reference path="../../types/common.d.ts" />

import React = require('react');
import IdName = require('../util/IdName');
var BS = require('react-bootstrap');
var Label = BS.Label;

module AnalyzersList {
    export interface Props {
        activeAnalyzerId: string;
        analyzers: AnalyzerInfo[];
        onAnalyzerClicked: (id:string) => void;
    }

    export interface AnalyzerInfo {
        idName: IdName;
        componentIdNames: IdName[];
        unreadErrors: number;
    }
}

class AnalyzersList extends React.Component<AnalyzersList.Props, {}> {
    private getAnalyzerListItems = () => (
        this.props.analyzers.map((analyzer:AnalyzersList.AnalyzerInfo) => {
            var clickHandler = (e:React.MouseEvent) => {
                e.preventDefault();
                this.props.onAnalyzerClicked(analyzer.idName.id);
            };
            var components = analyzer.componentIdNames.map((idName: IdName) => React.jsx(`
                <li>
                    <a href="#">{idName.name}</a>
                </li>
            `));
            return React.jsx(`
                <li>
                    <a href="#" onClick={clickHandler}>
                        {analyzer.idName.name}
                        <Label bsStyle="danger">
                            {analyzer.unreadErrors === 0 ? '' : analyzer.unreadErrors}
                        </Label>
                        <ul className={this.props.activeAnalyzerId == analyzer.idName.id ? null : 'collapse'}>
                            {components}
                        </ul>
                    </a>
                </li>
            `);
        })
    );

    render() {
        return React.jsx(`
            <ul className="sidebar-nav">
                {this.getAnalyzerListItems()}
            </ul>
        `);
    }
}
export = AnalyzersList;
