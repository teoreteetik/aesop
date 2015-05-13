/// <reference path="../../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './AnalyzersList', './filter/FilterSidebar'], function (require, exports, React, AnalyzersList, FilterSidebar) {
    var BS = require('react-bootstrap');
    var Button = React.createFactory(BS.Button);
    var Glyphicon = React.createFactory(BS.Glyphicon);
    var R = React.DOM;
    var CurrentView;
    (function (CurrentView) {
        CurrentView[CurrentView["ANALYZERS_LIST"] = 0] = "ANALYZERS_LIST";
        CurrentView[CurrentView["FILTER"] = 1] = "FILTER";
    })(CurrentView || (CurrentView = {}));
    var Sidebar = (function (_super) {
        __extends(Sidebar, _super);
        function Sidebar(props) {
            var _this = this;
            _super.call(this, props);
            this.getAnalyzersListComponent = function () {
                var analyzersListProps = {
                    activeAnalyzerId: _this.props.activeAnalyzerId,
                    analyzers: _this.props.analyzers.map(function (a) {
                        return {
                            idName: a.idName,
                            componentIdNames: a.componentIdNames,
                            unreadErrors: a.unreadErrors
                        };
                    }),
                    onAnalyzerClicked: _this.props.onAnalyzerClicked
                };
                return AnalyzersList.Component(analyzersListProps);
            };
            this.getFilterComponent = function () {
                var analyzerState = _.find(_this.props.analyzers, function (analyzer) { return analyzer.idName.id === _this.props.activeAnalyzerId; });
                var filterProps = {
                    uniqueSenderIdNames: analyzerState.uniqueSenderIdNames,
                    uniqueRecipientIdNames: analyzerState.uniqueRecipientIdNames,
                    filterState: _this.props.filterState,
                    onFilterStateChanged: _this.props.onFilterStateChanged,
                    unreadErrors: analyzerState.unreadErrors
                };
                return FilterSidebar.Component(filterProps);
            };
            this.getCurrentContent = function () {
                switch (_this.state.currentView) {
                    case 0 /* ANALYZERS_LIST */:
                        return _this.getAnalyzersListComponent();
                    case 1 /* FILTER */:
                        return _this.getFilterComponent();
                }
            };
            this.getVisibleButtons = function () {
                var buttons = [];
                if (_this.state.currentView !== 0 /* ANALYZERS_LIST */)
                    buttons.push(Button({ bsSize: 'xsmall', onClick: function () { return _this.setState({ currentView: 0 /* ANALYZERS_LIST */ }); } }, Glyphicon({ glyph: 'list' }), 'Analyzers'));
                if (_this.state.currentView !== 1 /* FILTER */)
                    buttons.push(Button({ bsSize: 'xsmall', onClick: function () { return _this.setState({ currentView: 1 /* FILTER */ }); } }, Glyphicon({ glyph: 'filter' }), 'Filter'));
                return buttons;
            };
            this.state = {
                currentView: 0 /* ANALYZERS_LIST */
            };
        }
        Sidebar.prototype.render = function () {
            return (R.div({ id: 'sidebar' }, R.ul({ className: 'sidebar-nav' }, R.li({ className: 'sidebar-brand' }, R.a({ href: '#' }, '')), R.li({}, this.getVisibleButtons())), this.getCurrentContent()));
        };
        return Sidebar;
    })(React.Component);
    exports.Component = React.createFactory(Sidebar);
});
//# sourceMappingURL=Sidebar.js.map