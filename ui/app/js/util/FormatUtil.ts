/// <reference path="../../types/common.d.ts" />

import IdName = require('./IdName');
import moment = require('moment');

export var formatDate = (date: Date): string => {
    return moment(date).format('HH:mm:ss DD.MM.YYYY');
};

export var getComponentName = (id:string, idNames: IdName[]): string => {
    for (var i = 0; i < idNames.length; i++)
        if (idNames[i].id === id)
            return idNames[i].name;
    if (id.indexOf('akka://Main') !== -1)
        return id.substr(11);
    else
        return id;
};

export var truncate = (str: string, maxLength: number): string => {
    return (str.length > maxLength) ? (str.substr(0, maxLength - 3) + '...')
                                    : str;
};