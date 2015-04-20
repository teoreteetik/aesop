/// <reference path="../types/react/react.d.ts" />
/// <reference path="../types/lib.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', './WebSocketMsgs', './sidebar/Sidebar', './util/FormatUtil', './MainContent'], function (require, exports, React, WebSocketMsgs, Sidebar, FormatUtil, MainContent) {
    var R = React.DOM;
    var _Page = (function (_super) {
        __extends(_Page, _super);
        function _Page(props) {
            var _this = this;
            _super.call(this, props);
            this.initSocket = function () {
                var socket = new WebSocket("ws://127.0.0.1:8900");
                socket.onmessage = function (wsEvent) {
                    var mwEvent = JSON.parse(event.data);
                    var mwEventType = WebSocketMsgs.EventType[mwEvent.eventType];
                    if (mwEventType == 0 /* AnalyzerInfo */)
                        _this.handleAnalyzerInfoEvent(mwEvent.event);
                    else if (mwEventType == 2 /* LogEvent */)
                        _this.handleLogEvent(mwEvent.event);
                    else if (mwEventType == 1 /* ProcessedMsgEvent */)
                        _this.handleProcessedMsg(mwEvent.event);
                };
            };
            this.handleAnalyzerInfoEvent = function (analyzer) {
                var componentIdNames = [];
                for (var id in analyzer.componentNameById)
                    componentIdNames.push({ id: id, name: analyzer.componentNameById[id] });
                var analyzerState = {
                    idName: { id: analyzer.id, name: analyzer.name },
                    componentIdNames: componentIdNames,
                    msgProcessedRows: [],
                    logEventRows: [],
                    uniqueSenderNameById: {},
                    uniqueRecipientNameById: {}
                };
                _this.state.analyzers[analyzerState.idName.id] = analyzerState;
                if (!_this.state.activeAnalyzerId)
                    _this.state.activeAnalyzerId = analyzerState.idName.id;
                _this.setState(_this.state);
            };
            this.handleLogEvent = function (msg) {
                var analyzerState = _this.state.analyzers[msg.analyzerId];
                if (analyzerState) {
                    //uniquecomponentid-sse ka midagi?
                    analyzerState.logEventRows.push(_this.logEventToRow(msg));
                    _this.setState(_this.state);
                }
            };
            this.logEventToRow = function (msg) {
                var componentIdNames = _this.state.analyzers[msg.analyzerId].componentIdNames;
                return {
                    logLevel: msg.logLevel,
                    formattedDate: FormatUtil.formatDate(new Date(msg.time)),
                    componentName: FormatUtil.getComponentName(msg.componentId, componentIdNames),
                    formattedMsgBody: msg.msgBody,
                    original: msg
                };
            };
            this.handleProcessedMsg = function (msg) {
                var analyzerState = _this.state.analyzers[msg.analyzerId];
                if (analyzerState) {
                    analyzerState.uniqueRecipientNameById[msg.recipientComponentId] = FormatUtil.getComponentName(msg.recipientComponentId, analyzerState.componentIdNames);
                    analyzerState.uniqueSenderNameById[msg.senderComponentId] = FormatUtil.getComponentName(msg.senderComponentId, analyzerState.componentIdNames);
                    analyzerState.msgProcessedRows.push(_this.msgProcessedToRow(msg));
                    _this.setState(_this.state);
                }
            };
            this.msgProcessedToRow = function (msg) {
                var componentIdNames = _this.state.analyzers[msg.analyzerId].componentIdNames;
                return {
                    formattedDate: FormatUtil.formatDate(new Date(msg.processingStartTime)),
                    senderName: FormatUtil.getComponentName(msg.senderComponentId, componentIdNames),
                    recipientName: FormatUtil.getComponentName(msg.recipientComponentId, componentIdNames),
                    formattedMsgBody: msg.msgBody,
                    original: msg
                };
            };
            this.state = {
                activeAnalyzerId: undefined,
                analyzers: {},
                filterState: {
                    msgProcessedFilterState: {
                        currentPair: {
                            senderId: undefined,
                            recipientId: undefined
                        },
                        addedPairs: [],
                        startTime: undefined,
                        endTime: undefined,
                        searchText: undefined
                    },
                    logEventFilterState: {
                        startTime: undefined,
                        endTime: undefined,
                        componentId: undefined
                    }
                }
            };
        }
        _Page.prototype.componentDidMount = function () {
            this.initSocket();
        };
        _Page.prototype.render = function () {
            var _this = this;
            var analyzers = [];
            for (var id in this.state.analyzers) {
                var a = this.state.analyzers[id];
                var uniqueSenderIdNames = [];
                for (var id in a.uniqueSenderNameById)
                    uniqueSenderIdNames.push({ id: id, name: a.uniqueSenderNameById[id] });
                var uniqueRecipientIdNames = [];
                for (var id in a.uniqueRecipientNameById)
                    uniqueRecipientIdNames.push({ id: id, name: a.uniqueRecipientNameById[id] });
                analyzers.push({
                    idName: a.idName,
                    componentIdNames: a.componentIdNames,
                    uniqueSenderIdNames: uniqueSenderIdNames,
                    uniqueRecipientIdNames: uniqueRecipientIdNames
                });
            }
            var sidebarProps = {
                activeAnalyzerId: this.state.activeAnalyzerId,
                analyzers: analyzers,
                onAnalyzerClicked: function (id) {
                    _this.state.activeAnalyzerId = id;
                    _this.setState(_this.state);
                },
                filterState: this.state.filterState,
                onFilterStateChanged: function (filterState) {
                    _this.state.filterState = filterState;
                    _this.setState(_this.state);
                }
            };
            var mainContentProps = {
                msgProcessedRows: this.state.activeAnalyzerId ? this.state.analyzers[this.state.activeAnalyzerId].msgProcessedRows : [],
                logEventRows: this.state.activeAnalyzerId ? this.state.analyzers[this.state.activeAnalyzerId].logEventRows : [],
                filterState: this.state.filterState,
                onLogFilterStateChanged: function (newState) {
                    _this.state.filterState.logEventFilterState = newState;
                    _this.setState(_this.state);
                }
            };
            return R.div({ id: 'wrapper' }, Sidebar.Component(sidebarProps), MainContent.Component(mainContentProps));
        };
        return _Page;
    })(React.Component);
    var Page = React.createFactory(_Page);
    return Page;
});
//# sourceMappingURL=Page.js.map