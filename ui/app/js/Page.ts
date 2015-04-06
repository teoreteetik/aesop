/// <reference path="../types/react/react.d.ts" />
/// <reference path="../types/lib.d.ts" />

import React = require('react');
import WebSocketMsgs = require('./WebSocketMsgs');
import Sidebar = require('./sidebar/Sidebar');
import FormatUtil = require('./util/FormatUtil');
import IdName = require('./util/IdName');
import ProcessedMsgsTable = require('./ProcessedMsgsTable');
import MainContent = require('./MainContent');
import MsgFilter = require('./sidebar/filter/MsgFilter');

var R = React.DOM;

interface PageState {
    activeAnalyzerId: string;
    analyzers: {[id: string]: AnalyzerState};
    filterState: MsgFilter.FilterState;
}
interface AnalyzerState {
    idName: IdName;
    componentIdNames: IdName[];
    msgProcessedRows: ProcessedMsgsTable.Row[];
    logEvents: WebSocketMsgs.LogEvent[];
    uniqueSenderNameById: {[id: string]: string};
    uniqueRecipientNameById: {[id: string]: string};
}

class _Page extends React.Component<{}, PageState> {
    constructor(props) {
        super(props);
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
    componentDidMount() {
        this.initSocket();
    }
    private initSocket = (): void => {
        var socket = new WebSocket("ws://127.0.0.1:8900");
        socket.onmessage = (wsEvent:MessageEvent) => {
            var mwEvent:WebSocketMsgs.WebSocketMsg = JSON.parse(event.data);
            var mwEventType = WebSocketMsgs.EventType[mwEvent.eventType];
            if (mwEventType == WebSocketMsgs.EventType.AnalyzerInfo) {
                var analyzerInfoEvent:WebSocketMsgs.AnalyzerInfo = mwEvent.event;
                this.handleAnalyzerInfoEvent(analyzerInfoEvent);
            } else if (mwEventType == WebSocketMsgs.EventType.LogEvent) {
                var logEvent:WebSocketMsgs.LogEvent = mwEvent.event;
            } else if (mwEventType == WebSocketMsgs.EventType.ProcessedMsgEvent) {
                var msgProcessedEvent:WebSocketMsgs.ProcessedMsgEvent = mwEvent.event;
                this.handleProcessedMsg(msgProcessedEvent);
            }
        };
    };
    private handleAnalyzerInfoEvent= (analyzer:WebSocketMsgs.AnalyzerInfo): void => {
        var componentIdNames: IdName[] = [];
        for (var id in analyzer.componentNameById)
            componentIdNames.push({id: id, name: analyzer.componentNameById[id]});
        var analyzerState: AnalyzerState = {
            idName: {id: analyzer.id, name: analyzer.name},
            componentIdNames: componentIdNames,
            msgProcessedRows: [],
            logEvents: [],
            uniqueSenderNameById: {},
            uniqueRecipientNameById: {}
        };
        this.state.analyzers[analyzerState.idName.id] = analyzerState;
        if (!this.state.activeAnalyzerId)
            this.state.activeAnalyzerId = analyzerState.idName.id;
        this.setState(this.state);
    };

    private handleLogEvent = (msg:any): void => {
    };

    private handleProcessedMsg = (msg:WebSocketMsgs.ProcessedMsgEvent): void => {
        var analyzerState: AnalyzerState = this.state.analyzers[msg.analyzerId];
        if (analyzerState) {
            analyzerState.uniqueRecipientNameById[msg.recipientComponentId] = FormatUtil.getComponentName(msg.recipientComponentId, analyzerState.componentIdNames);
            analyzerState.uniqueSenderNameById[msg.senderComponentId] = FormatUtil.getComponentName(msg.senderComponentId, analyzerState.componentIdNames);
            analyzerState.msgProcessedRows.push(this.msgProcessedToRow(msg));
            this.setState(this.state);
        }
    };
    private msgProcessedToRow = (msg: WebSocketMsgs.ProcessedMsgEvent): ProcessedMsgsTable.Row => {
        var componentIdNames = this.state.analyzers[msg.analyzerId].componentIdNames;
        return {
            formattedDate: FormatUtil.formatDate(new Date(msg.processingStartTime)),
            senderName: FormatUtil.getComponentName(msg.senderComponentId, componentIdNames),
            recipientName: FormatUtil.getComponentName(msg.recipientComponentId, componentIdNames),
            formattedMsgBody: msg.msgBody,
            original: msg
        };
    };

    render() {
        var analyzers: Sidebar.AnalyzerState[] = [];
        for (var id in this.state.analyzers) {
            var a = this.state.analyzers[id];
            var uniqueSenderIdNames: IdName[] = [];
            for (var id in a.uniqueSenderNameById)
                uniqueSenderIdNames.push({id: id, name: a.uniqueSenderNameById[id]});
            var uniqueRecipientIdNames: IdName[] = [];
            for (var id in a.uniqueRecipientNameById)
                uniqueRecipientIdNames.push({id: id, name: a.uniqueRecipientNameById[id]});
            analyzers.push({
                idName: a.idName,
                componentIdNames: a.componentIdNames,
                uniqueSenderIdNames: uniqueSenderIdNames,
                uniqueRecipientIdNames: uniqueRecipientIdNames
            });
        }
        var sidebarProps: Sidebar.Props = {
            activeAnalyzerId: this.state.activeAnalyzerId,
            analyzers: analyzers,
            onAnalyzerClicked: id => {
                this.state.activeAnalyzerId = id;
                this.setState(this.state);
            },
            filterState: this.state.filterState,
            onFilterStateChanged: (filterState: MsgFilter.FilterState) => {
                this.state.filterState = filterState;
                this.setState(this.state);
            }
        };

        var mainContentProps: MainContent.Props = {
            msgProcessedRows: this.state.activeAnalyzerId ? this.state.analyzers[this.state.activeAnalyzerId].msgProcessedRows : [],
            filterState: this.state.filterState
        };
        return R.div({id: 'wrapper'},
            Sidebar.Component(sidebarProps),
            MainContent.Component(mainContentProps));
    }

}
var Page = React.createFactory(_Page);
export = Page;