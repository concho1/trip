let peopleChartOptions = {
    chart : {
        type: 'line',
        animations: {
            enabled: true,
            easing: 'liner',
            speed: 800,
            animateGradually: {
                enabled: true,
                delay: 150
            },
            dynamicAnimation: {
                enabled: true,
                speed: 350
            }
        },
        height: '400px',
        width: '400px'
    },
    series: [{
        name: 'sales',
        data: [30, 40, 35, 50, 49, 60, 70, 91, 125]
    },
        {
            name: 'price',
            data: [20, 20, 25, 30, 28, 50, 72, 63, 97]
        }],
    xaxis: {
        categories: [1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999]
    }
}

let hotelAPICountChartOptions = {
    chart: {
        height: '400px',
        width: '200px',
        type: 'radialBar',
    },
    series: [70],
    labels: ['호텔 API 이용률'],
}

let flightAPICountChartOptions = {
    chart: {
        height: '400px',
        width: '200px',
        type: 'radialBar',
    },
    series: [70],
    labels: ['항공 API 이용률'],
}

let peopleChart = new ApexCharts(document.querySelector("#peopleChart"), peopleChartOptions);
let hotelChart = new ApexCharts(document.querySelector("#hotelChart"), hotelAPICountChartOptions);
let flightChart = new ApexCharts(document.querySelector("#flightChart"), flightAPICountChartOptions);

peopleChart.render();
hotelChart.render();
flightChart.render();