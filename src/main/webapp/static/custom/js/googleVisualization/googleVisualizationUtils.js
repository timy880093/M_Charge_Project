function googleChartsInit() {
    // Load the Visualization API and the piechart package.
    google.charts.load('visualization', '1.0', {
        'packages': ['corechart', 'table', 'annotationchart']
    });

    return {
        initDataTable: initDataTable,
        drawDataTable: drawDataTable,
        drawRevenueRunChart: drawRevenueRunChart,
        drawQuarterlyRevenueRunChart: drawQuarterlyRevenueRunChart,
        drawRevenueForSamePeriodRunChart: drawRevenueForSamePeriodRunChart,
    };
}

function initDataTable(data) {
    // Create the data table.
    //['Month', 'Total Amount', 'Median', 'avg3', 'avg12', 'm2m', 'q2q', 'avg3-avg12'],
    let allData = new google.visualization.DataTable();
    allData.addColumn('date', 'Month');
    allData.addColumn('number', 'Total Amount');
    allData.addColumn('number', 'Median');
    allData.addColumn('number', 'avg3');
    allData.addColumn('number', 'avg12');
    allData.addColumn('number', 'm2m');
    allData.addColumn('number', 'q2q');
    allData.addColumn('number', 'avg3-avg12');
    if (data instanceof Array) {
        allData.addRows(googleVirtualizeObjectWrapper(data));
    } else {
        allData.addRows([]);
    }
    return allData;
}

function googleDateObjectFormat(data) {
    let year = data.substring(0, 4);
    let month = data.substring(4, 6);
    return new Date(year, month - 1, 1);
}

function googleVirtualizeObjectWrapper(objList) {
    let resultList = [];
    objList.forEach(element => {
        let result = [];
        result.push(googleDateObjectFormat(element['month']));
        result.push(element['total']);
        result.push(element['median']);
        result.push(element['avgPre3Month']);
        result.push(element['avgPre12Month']);
        result.push(element['m2m']);
        result.push(element['q2q']);
        result.push(element['avg3MinusAvg12']);
        resultList.push(result);
    });
    return resultList;
}

function drawDataTable(elementId, googleDataTable) {

    let formatterA = new google.visualization.BarFormat({width: 50});
    let formatterB = new google.visualization.ColorFormat();
    formatterB.addRange(null, -25, 'red', null);
    formatterB.addRange(25, null, 'green', null);
    let formatterC = new google.visualization.ColorFormat();
    formatterC.addRange(null, 0, 'red', null);
    formatterC.addRange(0, null, 'green', null);

    let formatter11 = new google.visualization.NumberFormat({pattern: '##.#'});
    let formatter4 = new google.visualization.NumberFormat({pattern: '$###,###.#'});
    let formatter5 = new google.visualization.NumberFormat({pattern: '$###,###'});

    formatter4.format(googleDataTable, 1);
    formatter4.format(googleDataTable, 2);
    formatter5.format(googleDataTable, 3);
    formatter5.format(googleDataTable, 4);
    formatter11.format(googleDataTable, 5);
    formatterB.format(googleDataTable, 5);
    formatterA.format(googleDataTable, 5);
    formatter11.format(googleDataTable, 6);
    formatterB.format(googleDataTable, 6);
    formatterA.format(googleDataTable, 6);
    formatter5.format(googleDataTable, 7);
    formatterC.format(googleDataTable, 7);
    formatterA.format(googleDataTable, 7);

    //allData.sort([{column: 1, asc: !event.descending}]);
    let options_table = {
        showRowNumber: true,
        interpolateNulls: true,
        allowHtml: true,
        width: '100%',
        height: '100%',
        page: 'enable',
        pageSize: 150,
        frozenColumns: 1,
        backgroundColor: '#f9f9f9',
        page: 'enable'
    }
    let table = new google.visualization.Table(document.getElementById(elementId));
    table.draw(googleDataTable, options_table);
}

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawRevenueRunChart(elementId, googleDataTable) {

    // Create the data table.
    //['Month', 'Total Amount', 'Median', 'avg3', 'avg12', 'm2m', 'q2q', 'avg3-avg12'],
    let data1 = new google.visualization.DataView(googleDataTable);
    data1.setColumns([0, 1, 2, 3, 4]);

    // Set chart options
    let options1 = {
        'title': '',
        //interpolateNulls: true,
        vAxis: {title: '金額', gridlines: {count: 10}},
        hAxis: {title: '年月', format: 'yyyy-MM', gridlines: {count: googleDataTable.getNumberOfRows()}},

        seriesType: 'lines',
        lineWidth: 3,
        series: {
            0: {type: 'bars', opacity: 0.3},
            1: {opacity: 0.3},
            2: {opacity: 0.3},
            3: {opacity: 0.3},
            4: {opacity: 0.3},
            5: {opacity: 0.3}
        },
        trendlines: {
            0: {
                type: 'linear',
                opacity: 0.3, lineWidth: 4,
                showR2: true,
                //visibleInLegend: true
            }
        },
        'height' : 500,
        tooltip: {showColorCode: true},
        backgroundColor: '#f9f9f9',
    };

    // Instantiate and draw our chart, passing in some options.
    let chart1 = new google.visualization.ComboChart(document.getElementById(elementId));
    chart1.draw(data1, options1);
}

function drawQuarterlyRevenueRunChart(elementId, googleDataTable) {
    // Create the data table.
    //['Month', 'Total Amount', 'Median', 'avg3', 'avg12', 'm2m', 'q2q', 'avg3-avg12'],
    let data = new google.visualization.DataView(googleDataTable);
    data.setColumns([0, 3, 4, 7]);

    // Set chart options
    let options = {
        'title': '',
        //interpolateNulls: true,
        vAxis: {title: '金額', gridlines: {count: 10}},
        hAxis: {title: '年月', format: 'yyyy-MM', gridlines: {count: googleDataTable.getNumberOfRows()}},
        seriesType: 'lines',
        lineWidth: 3,
        opacity: 0.3,
        series: {
            0: {opacity: 0.3},
            1: {opacity: 0.3},
            2: {type: 'bars', opacity: 0.3},
            3: {opacity: 0.3},
            4: {opacity: 0.3},
            5: {opacity: 0.3}
        },
        trendlines: {
            0: {
                type: 'linear',
                opacity: 0.3, lineWidth: 4,
                //showR2: true,
                //visibleInLegend: true
            }, 1: {
                type: 'linear',
                opacity: 0.3, lineWidth: 4,
                //showR2: true,
                //visibleInLegend: true
            }, 2: {
                type: 'linear',
                opacity: 0.3, lineWidth: 4,
                //showR2: true,
                //visibleInLegend: true
            }
        },
        tooltip: {showColorCode: true},
        backgroundColor: '#f9f9f9',
        'height': 500
    };

    // Instantiate and draw our chart, passing in some options.
    let chart = new google.visualization.ComboChart(document.getElementById(elementId));
    chart.draw(data, options);
}

function objectWrapperForRevenueRunChart(objList) {
    let resultList = [];
    // let lastM2m = 0;
    // let lastQ2q = 0;
    objList.forEach(element => {
        let result = [];
        result.push(googleDateObjectFormat(element['month']));
        // lastM2m = element['m2m'] ;
        // lastQ2q = element['q2q'] ;
        result.push(element['m2m']);
        result.push(element['q2q']);
        result.push(25);
        result.push(-25);
        resultList.push(result);
    });
    return resultList;
}

function drawRevenueForSamePeriodRunChart(elementId, rawData) {
    // Create the data table.
    let data3 = new google.visualization.DataTable();
    data3.addColumn('date', 'Month');
    data3.addColumn('number', 'M2M');
    data3.addColumn('number', 'Q2Q');
    data3.addColumn('number', 'Upper Bound');
    data3.addColumn('number', 'Lower Bound');
    data3.addRows(objectWrapperForRevenueRunChart(rawData));
    // Set chart options
    let options3 = {
        'title': '',
        vAxis: {title: '百分比％', gridlines: {count: 10}},
        hAxis: {title: '月份', format: 'yyyy-MM', gridlines: {count: rawData.length}},
        tooltip: {showColorCode: true},
        'height': 500,
        seriesType: 'lines',
        series: {
            0: {type: 'lines', opacity: 0.3},
            1: {type: 'lines', opacity: 0.3},
            2: {opacity: 0.3, lineWidth: 5},
            3: {opacity: 0.3, lineWidth: 5},
            4: {opacity: 0.3},
            5: {opacity: 0.3}
        },
        trendlines: {
            0: {
                type: 'linear',
                opacity: 0.3, lineWidth: 4,
                //showR2: true,
                //visibleInLegend: true
            }, 1: {
                type: 'linear',
                opacity: 0.3, lineWidth: 4,
                //showR2: true,
                //visibleInLegend: true
            }
        },
        displayAnnotations: true,
        backgroundColor: '#f9f9f9',
    };

    let chart3 = new google.visualization.ComboChart(document.getElementById(elementId));
    chart3.draw(data3, options3);
}