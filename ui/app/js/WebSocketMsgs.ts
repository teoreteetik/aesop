export enum EventType {
    AnalyzerInfo,
    ProcessedMsgEvent,
    LogEvent
}

export interface WebSocketMsg {
    eventType: string;
    event: any;
}

export interface AnalyzerInfo {
    id: string;
    name: string;
    componentNameById: { [id:string]: string; }
}

export interface ProcessedMsgEvent {
    analyzerId: string;
    senderComponentId: string;
    recipientComponentId: string;
    msgBody: string;
    processingStartTime: number;
    processingEndTime: number;
    stackTrace: string;
}

export interface LogEvent {
    logLevel: string;
    analyzerId: string;
    componentId: string;
    msgBody: string;
    time: number;
}