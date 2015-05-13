/// <reference path="../types/common.d.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
define(["require", "exports", 'react', 'lodash', './WebSocketMsgs', './sidebar/Sidebar', './util/FormatUtil', './MainContent'], function (require, exports, React, _, WebSocketMsgs, Sidebar, FormatUtil, MainContent) {
    var R = React.DOM;
    var _Page = (function (_super) {
        __extends(_Page, _super);
        function _Page(props) {
            var _this = this;
            _super.call(this, props);
            this.initSocket = function () {
                var socket = new WebSocket("ws://127.0.0.1:8900");
                socket.onmessage = function (wsEvent) {
                    var mwEvent = JSON.parse(wsEvent.data);
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
                _.forOwn(analyzer.componentNameById, function (name, id) { return componentIdNames.push({ id: id, name: name }); });
                var analyzerState = {
                    idName: { id: analyzer.id, name: analyzer.name },
                    componentIdNames: componentIdNames,
                    msgProcessedRows: [],
                    logEventRows: [],
                    uniqueSenderNameById: {},
                    uniqueRecipientNameById: {},
                    unreadErrors: 0
                };
                _this.state.analyzers[analyzerState.idName.id] = analyzerState;
                if (!_this.state.activeAnalyzerId)
                    _this.state.activeAnalyzerId = analyzerState.idName.id;
                _this.setState(_this.state);
            };
            this.handleLogEvent = function (msg) {
                var analyzerState = _this.state.analyzers[msg.analyzerId];
                if (analyzerState) {
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
                    if (msg.stackTrace)
                        analyzerState.unreadErrors++;
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
                    original: msg,
                    isRead: false
                };
            };
            this.getSidebarProps = function () {
                var analyzers = [];
                _.forOwn(_this.state.analyzers, function (analyzer, analyzerId) {
                    var uniqueSenderIdNames = [];
                    _.forOwn(analyzer.uniqueSenderNameById, function (name, id) { return uniqueSenderIdNames.push({ id: id, name: name }); });
                    var uniqueRecipientIdNames = [];
                    _.forOwn(analyzer.uniqueRecipientNameById, function (name, id) { return uniqueRecipientIdNames.push({ id: id, name: name }); });
                    analyzers.push({
                        idName: analyzer.idName,
                        componentIdNames: analyzer.componentIdNames,
                        uniqueSenderIdNames: uniqueSenderIdNames,
                        uniqueRecipientIdNames: uniqueRecipientIdNames,
                        unreadErrors: analyzer.unreadErrors
                    });
                });
                return {
                    activeAnalyzerId: _this.state.activeAnalyzerId,
                    analyzers: analyzers,
                    onAnalyzerClicked: function (id) {
                        _this.state.activeAnalyzerId = id;
                        _this.setState(_this.state);
                    },
                    filterState: _this.state.filterState,
                    onFilterStateChanged: function (filterState) {
                        _this.state.filterState = filterState;
                        _this.setState(_this.state);
                    }
                };
            };
            this.getMainContentProps = function () {
                return {
                    msgProcessedRows: _this.state.activeAnalyzerId ? _this.state.analyzers[_this.state.activeAnalyzerId].msgProcessedRows : [],
                    logEventRows: _this.state.activeAnalyzerId ? _this.state.analyzers[_this.state.activeAnalyzerId].logEventRows : [],
                    filterState: _this.state.filterState,
                    onLogFilterStateChanged: function (newState) {
                        _this.state.filterState.logEventFilterState = newState;
                        _this.setState(_this.state);
                    },
                    markRowAsRead: function (row) {
                        if (!row.isRead) {
                            row.isRead = true;
                            if (row.original.stackTrace) {
                                _this.state.analyzers[_this.state.activeAnalyzerId].unreadErrors--;
                            }
                            _this.setState(_this.state);
                        }
                    }
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
                        searchText: undefined,
                        processingState: undefined
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
            return (R.div({ id: 'wrapper' }, Sidebar.Component(this.getSidebarProps()), MainContent.Component(this.getMainContentProps())));
        };
        return _Page;
    })(React.Component);
    var Page = React.createFactory(_Page);
    return Page;
});
//# sourceMappingURL=Page.js.map