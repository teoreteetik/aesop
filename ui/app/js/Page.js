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
                    if (mwEventType == 0 /* AnalyzerInfo */) {
                        var analyzerInfoEvent = mwEvent.event;
                        _this.handleAnalyzerInfoEvent(analyzerInfoEvent);
                    }
                    else if (mwEventType == 2 /* LogEvent */) {
                        var logEvent = mwEvent.event;
                    }
                    else if (mwEventType == 1 /* ProcessedMsgEvent */) {
                        var msgProcessedEvent = mwEvent.event;
                        _this.handleProcessedMsg(msgProcessedEvent);
                    }
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
                    logEvents: [],
                    uniqueSenderNameById: {},
                    uniqueRecipientNameById: {}
                };
                _this.state.analyzers[analyzerState.idName.id] = analyzerState;
                if (!_this.state.activeAnalyzerId)
                    _this.state.activeAnalyzerId = analyzerState.idName.id;
                _this.setState(_this.state);
            };
            this.handleLogEvent = function (msg) {
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
                    currentPair: {
                        senderId: undefined,
                        recipientId: undefined
                    },
                    addedPairs: [],
                    searchText: undefined
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
                filterState: this.state.filterState
            };
            return R.div({ id: 'wrapper' }, Sidebar.Component(sidebarProps), MainContent.Component(mainContentProps));
        };
        return _Page;
    })(React.Component);
    var Page = React.createFactory(_Page);
    return Page;
});
//# sourceMappingURL=Page.js.map