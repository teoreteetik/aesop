/// <reference path="./react/react.d.ts" />
/// <reference path="./lodash/lodash.d.ts" />
/// <reference path="./moment/moment.d.ts" />

declare function require(string): any;

declare module "react" {
    function jsx(jsx?: string): ReactElement<any>;
    function __spread(...args: any[]): any; // for JSX Spread Attributes
}
// declare function jsx(string): React.ReactElement<any>;
