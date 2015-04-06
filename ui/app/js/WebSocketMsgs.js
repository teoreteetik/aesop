define(["require", "exports"], function (require, exports) {
    (function (EventType) {
        EventType[EventType["AnalyzerInfo"] = 0] = "AnalyzerInfo";
        EventType[EventType["ProcessedMsgEvent"] = 1] = "ProcessedMsgEvent";
        EventType[EventType["LogEvent"] = 2] = "LogEvent";
    })(exports.EventType || (exports.EventType = {}));
    var EventType = exports.EventType;
});
//# sourceMappingURL=WebSocketMsgs.js.map