var config = {
    addVendor: function (name, path) {
        this.resolve.alias[name] = path;
        this.module.noParse.push(new RegExp(path));
    },

    entry: ['./app/main.js'],
    output: {
        path: './build',
        filename: 'bundle.js'
    },
    resolve: {
        extensions: ['', '.webpack.js', '.web.js', '.ts', '.js'],
        alias: {}
    },




    devtool: 'source-map',
    module: {
        noParse: [],
        loaders: [
            {test: /\.ts$/, loader: 'typescript-loader'},
            {test: /\.css$/, loader: 'style-loader!css-loader'},
            {test: /\.(png|woff|svg|woff2|eot|ttf)$/, loader: 'url-loader'}
        ]
    }
};
config.addVendor('bootstrap.css', __dirname + '/node_modules/bootstrap/dist/css/bootstrap.css');
config.addVendor('fixed-data-table.css', __dirname + '/node_modules/fixed-data-table/dist/fixed-data-table.css');
config.addVendor('react-select.css', __dirname + '/node_modules/react-select/dist/default.css');
config.addVendor('react-date-picker.css', __dirname + '/node_modules/react-date-picker/index.css');

module.exports = config;