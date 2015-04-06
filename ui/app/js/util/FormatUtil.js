define(["require", "exports"], function (require, exports) {
    exports.formatDate = function (date) {
        var day = date.getDate();
        var month = date.getMonth() + 1;
        var year = date.getFullYear();
        var hour = date.getHours();
        var minute = date.getMinutes();
        var second = date.getSeconds();
        var milli = date.getMilliseconds();
        var prepend0 = function (num) { return (num <= 9 ? '0' + num : num); };
        return '' + prepend0(hour) + ':' + prepend0(minute) + ':' + prepend0(second) + ':' + milli + ' ' + prepend0(day) + '/' + prepend0(month) + '/' + year;
    };
    exports.getComponentName = function (id, idNames) {
        for (var i = 0; i < idNames.length; i++)
            if (idNames[i].id === id)
                return idNames[i].name;
        if (id.indexOf('akka://Main') !== -1)
            return id.substr(11);
        else
            return id;
    };
    exports.truncate = function (str, maxLength) {
        return (str.length > maxLength) ? (str.substr(0, maxLength - 3) + '...') : str;
    };
});
//# sourceMappingURL=FormatUtil.js.map