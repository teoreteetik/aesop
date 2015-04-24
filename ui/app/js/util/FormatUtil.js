/// <reference path="../../types/common.d.ts" />
define(["require", "exports", 'moment'], function (require, exports, moment) {
    exports.formatDate = function (date) {
        return moment(date).format('HH:mm:ss DD.MM.YYYY');
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