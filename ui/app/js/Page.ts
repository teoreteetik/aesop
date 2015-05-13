/// <reference path="../types/common.d.ts" />

import React = require('react');
import _ = require('lodash');
import WebSocketMsgs = require('./WebSocketMsgs');
import Sidebar = require('./sidebar/Sidebar');
import FormatUtil = require('./util/FormatUtil');
import IdName = require('./util/IdName');
import ProcessedMsgsTable = require('./ProcessedMsgsTable');
import LogEventsTable = require('./LogEventsTable');
import MainContent = require('./MainContent');
import FilterSidebar = require('./sidebar/filter/FilterSidebar');
var R = React.DOM;

interface PageState {
    activeAnalyzerId: string;
    analyzers: {[id: string]: AnalyzerState};
    filterState: FilterSidebar.FilterState;
}
interface AnalyzerState {
    idName: IdName;
    componentIdNames: IdName[];
    msgProcessedRows: ProcessedMsgsTable.Row[];
    logEventRows: LogEventsTable.Row[];
    uniqueSenderNameById: {[id: string]: string};
    uniqueRecipientNameById: {[id: string]: string};
    unreadErrors: number;
}

class _Page extends React.Component<{}, PageState> {
    constructor(props) {
        super(props);
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
    componentDidMount() {
        this.initSocket();
    }

    private initSocket = (): void => {
        var socket = new WebSocket("ws://127.0.0.1:8900");
        socket.onmessage = (wsEvent: MessageEvent) => {
            var mwEvent:WebSocketMsgs.WebSocketMsg = JSON.parse(wsEvent.data);
            var mwEventType = WebSocketMsgs.EventType[mwEvent.eventType];
            if (mwEventType == WebSocketMsgs.EventType.AnalyzerInfo)
                this.handleAnalyzerInfoEvent(<WebSocketMsgs.AnalyzerInfo>mwEvent.event);
            else if (mwEventType == WebSocketMsgs.EventType.LogEvent)
                this.handleLogEvent(<WebSocketMsgs.LogEvent>mwEvent.event);
            else if (mwEventType == WebSocketMsgs.EventType.ProcessedMsgEvent)
                this.handleProcessedMsg(<WebSocketMsgs.ProcessedMsgEvent>mwEvent.event);

        };
    };

    private handleAnalyzerInfoEvent= (analyzer: WebSocketMsgs.AnalyzerInfo): void => {
        var componentIdNames: IdName[] = [];
        _.forOwn(analyzer.componentNameById, (name, id) => componentIdNames.push({ id: id, name: name }));
        var analyzerState: AnalyzerState = {
            idName: { id: analyzer.id, name: analyzer.name },
            componentIdNames: componentIdNames,
            msgProcessedRows: [],
            logEventRows: [],
            uniqueSenderNameById: {},
            uniqueRecipientNameById: {},
            unreadErrors: 0
        };
        this.state.analyzers[analyzerState.idName.id] = analyzerState;
        if (!this.state.activeAnalyzerId)
            this.state.activeAnalyzerId = analyzerState.idName.id;
        this.setState(this.state);
    };

    private handleLogEvent = (msg: WebSocketMsgs.LogEvent): void => {
        var analyzerState = this.state.analyzers[msg.analyzerId];
        if (analyzerState) {
            analyzerState.logEventRows.push(this.logEventToRow(msg));
            this.setState(this.state);
        }
    };

    private logEventToRow = (msg: WebSocketMsgs.LogEvent): LogEventsTable.Row => {
        var componentIdNames = this.state.analyzers[msg.analyzerId].componentIdNames;
        return {
            logLevel: msg.logLevel,
            formattedDate: FormatUtil.formatDate(new Date(msg.time)),
            componentName: FormatUtil.getComponentName(msg.componentId, componentIdNames),
            formattedMsgBody: msg.msgBody,
            original: msg
        };
    };

    private handleProcessedMsg = (msg:WebSocketMsgs.ProcessedMsgEvent): void => {
        var analyzerState: AnalyzerState = this.state.analyzers[msg.analyzerId];
        if (analyzerState) {
            analyzerState.uniqueRecipientNameById[msg.recipientComponentId] = FormatUtil.getComponentName(msg.recipientComponentId, analyzerState.componentIdNames);
            analyzerState.uniqueSenderNameById[msg.senderComponentId] = FormatUtil.getComponentName(msg.senderComponentId, analyzerState.componentIdNames);
            analyzerState.msgProcessedRows.push(this.msgProcessedToRow(msg));
            if (msg.stackTrace)
                analyzerState.unreadErrors++;
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
            original: msg,
            isRead: false
        };
    };

    private getSidebarProps = (): Sidebar.Props => {
        var analyzers: Sidebar.AnalyzerState[] = [];
        _.forOwn(this.state.analyzers, (analyzer, analyzerId) => {
            var uniqueSenderIdNames: IdName[] = [];
            _.forOwn(analyzer.uniqueSenderNameById, (name, id) => uniqueSenderIdNames.push({ id: id, name: name }));

            var uniqueRecipientIdNames: IdName[] = [];
            _.forOwn(analyzer.uniqueRecipientNameById, (name, id) => uniqueRecipientIdNames.push({ id: id, name: name }));

            analyzers.push({
                idName: analyzer.idName,
                componentIdNames: analyzer.componentIdNames,
                uniqueSenderIdNames: uniqueSenderIdNames,
                uniqueRecipientIdNames: uniqueRecipientIdNames,
                unreadErrors: analyzer.unreadErrors
            });
        });

        return {
            activeAnalyzerId: this.state.activeAnalyzerId,
            analyzers: analyzers,
            onAnalyzerClicked: id => {
                this.state.activeAnalyzerId = id;
                this.setState(this.state);
            },
            filterState: this.state.filterState,
            onFilterStateChanged: (filterState: FilterSidebar.FilterState) => {
                this.state.filterState = filterState;
                this.setState(this.state);
            }
        };
    };

    private getMainContentProps = (): MainContent.Props => {
        return {
            msgProcessedRows: this.state.activeAnalyzerId ? this.state.analyzers[this.state.activeAnalyzerId].msgProcessedRows : [],
            logEventRows: this.state.activeAnalyzerId ? this.state.analyzers[this.state.activeAnalyzerId].logEventRows : [],
            filterState: this.state.filterState,
            onLogFilterStateChanged: (newState) => {
                this.state.filterState.logEventFilterState = newState;
                this.setState(this.state);
            },
            markRowAsRead: (row: ProcessedMsgsTable.Row) => {
                if (!row.isRead) {
                    row.isRead = true;
                    if (row.original.stackTrace) {
                        this.state.analyzers[this.state.activeAnalyzerId].unreadErrors--;
                    }
                    this.setState(this.state);
                }
            }
        };
    };

    render() {
        return (
            R.div({ id: 'wrapper' },
                Sidebar.Component(this.getSidebarProps()),
                MainContent.Component(this.getMainContentProps()))
        );
    }
}
var Page = React.createFactory(_Page);
export = Page;