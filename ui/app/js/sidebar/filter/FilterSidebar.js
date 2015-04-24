/// <reference path="../../../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './MsgProcessedFilter', './LogEventFilter'], function (require, exports, React, MsgProcessedFilter, LogEventFilter) {
    var R = React.DOM;
    var FilterSidebar = (function (_super) {
        __extends(FilterSidebar, _super);
        function FilterSidebar() {
            var _this = this;
            _super.apply(this, arguments);
            this.getMsgProcessedFilterProps = function () {
                return {
                    filterState: _this.props.filterState.msgProcessedFilterState,
                    uniqueSenderIdNames: _this.props.uniqueSenderIdNames,
                    uniqueRecipientIdNames: _this.props.uniqueRecipientIdNames,
                    onFilterStateChanged: function (newState) {
                        _this.props.onFilterStateChanged({
                            msgProcessedFilterState: newState,
                            logEventFilterState: _this.props.filterState.logEventFilterState
                        });
                    }
                };
            };
            this.getLogEventFilterProps = function () {
                return {
                    filterState: _this.props.filterState.logEventFilterState,
                    onFilterStateChanged: function (newState) {
                        _this.props.onFilterStateChanged({
                            msgProcessedFilterState: _this.props.filterState.msgProcessedFilterState,
                            logEventFilterState: newState
                        });
                    }
                };
            };
        }
        FilterSidebar.prototype.render = function () {
            return (R.div({}, MsgProcessedFilter.Component(this.getMsgProcessedFilterProps()), LogEventFilter.Component(this.getLogEventFilterProps())));
        };
        return FilterSidebar;
    })(React.Component);
    exports.Component = React.createFactory(FilterSidebar);
});
//# sourceMappingURL=FilterSidebar.js.map