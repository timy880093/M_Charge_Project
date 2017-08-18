<%--
  Created by IntelliJ IDEA.
  User: simon
  Date: 2014/7/4
  Time: 下午 06:24
  To change this template use File | Settings | File Templates.
--%>

<link class="include" rel="stylesheet" type="text/css" href="/backendAdmin/dist/jquery.jqplot.min.css" />

<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="/backendAdmin/dist/excanvas.js"></script><![endif]-->


<!-- Example scripts go here -->

<div id="chart2" class="plot" style="width:80%;height:50%;"></div>
<div id="chart3" class="plot" style="width:80%;height:50%;"></div>

<script language="javascript" type="text/javascript">



    $(document).ready(function(){

        var c0401amountArray = [0,0,0,0,0,0,0];
        var c0501amountArray = [0,0,0,0,0,0,0];
        var c0701amountArray = [0,0,0,0,0,0,0];


        var dateArray = [getDay(6),getDay(5),getDay(4),getDay(3),getDay(2),getDay(1),getDay(0)];
        var c0401countArray = [0,0,0,0,0,0,0];
        var c0501countArray = [0,0,0,0,0,0,0];
        var c0701countArray = [0,0,0,0,0,0,0];
        var data;
        if (${!empty REQUEST_SEND_OBJECT_0}) {
            data = $.parseJSON('${REQUEST_SEND_OBJECT_0}');

        }else{
            data = '';
        }
        var maxCount= 0;
        var maxAmount = 0;
        $.each(data,function(index){
            var date = this.invoice_date;
            //alert(index + ' ' + date);


            for(var i=0; i<dateArray.length; i++) {
                if (dateArray[i] === date) {

                    c0401countArray[i] = this.opened;
                    c0501countArray[i] = this.canceled;
                    c0701countArray[i] = this.empty;
                    c0401amountArray[i] = this.opened_amount;
                    c0501amountArray[i] = this.canceled_amount;
                    c0701amountArray[i] = this.empty_amount;

                    if (this.opened > maxCount) {
                        maxCount = this.opened;
                    }
                    if (this.canceled > maxCount) {
                        maxCount = this.canceled;
                    }
                    if (this.empty > maxCount) {
                        maxCount = this.empty;
                    }
                    if (this.opened_amount > maxAmount) {
                        maxAmount = this.opened_amount;
                    }
                    if (this.canceled_amount > maxAmount) {
                        maxAmount = this.canceled_amount;
                    }
                    if (this.empty_amount > maxAmount) {
                        maxAmount = this.empty_amount;
                    }
                }

            }
        });


        if(maxCount>0){maxCount = getCloseMaxNumber(Math.ceil(maxCount*1.2));}
        //alert(Math.ceil(maxAmount*1.2));
        //alert(getCloseMaxNumber(Math.ceil(maxAmount*1.2)));
        if(maxAmount>0){maxAmount = getCloseMaxNumber(Math.ceil(maxAmount*1.2));}

        //alert(maxCount);
        /*
         for(i=0;i<dateArray.length; i++)
         {
         alert(dateArray[i] + '\n' +
         c0401countArray[i] + ' ' + c0401amountArray[i] + '\n'
         + c0501countArray[i] + ' ' + c0501amountArray[i] + '\n'
         + c0701countArray[i] + ' ' + c0701amountArray[i]
         );
         }*/

        //var jString = JSON.stringify(data);

        //alert(jString);


        //alert('maxcount = ' + maxCount);

        pop1980 = [7071639, 2968528, 3005072, 1595138, 789704, 1688210, 785940];
        pop1990 = [7322564, 3485398, 2783726, 1630553, 983403, 1585577, 935933];
        pop2000 = [8008654, 3694644, 2896051, 1974152, 1322025, 1517550, 1160005];

        getCloseMaxNumber(maxCount);

        //ticks = ["2014/12/01", "2014/12/01", "2014/12/02", "2014/12/03", "2014/12/04", "2014/12/05", "2014/12/06"];

        plot2 = $.jqplot('chart2',[c0401countArray, c0501countArray, c0701countArray],{
            title: '發票紀錄',
            legend: {
                show: true,
                placement: 'outside'
            },
            seriesDefaults: {
                renderer: $.jqplot.BarRenderer,
                pointLabels: { show: true, location:'n',hideZeros: true},
                rendererOptions: {
                    barPadding: 2
                }
            },

            series: [
                {label: '發票數量'},
                {label: '作廢數量'},
                {label: '註銷數量'}
            ],
            axes: {
                xaxis: {
                    label: '',
                    renderer: $.jqplot.CategoryAxisRenderer,
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                    ticks: dateArray,
                    tickOptions: {
                        angle: -0
                    }
                },
                yaxis: {
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    max: maxCount,
                    min: 0,
                    tickOptions: {
                        formatString: '%d',
                        angle: -0
                    }
                }
            }
        });

        plot2 = $.jqplot('chart3',[c0401amountArray, c0501amountArray, c0701amountArray],{
            title: '發票金額紀錄',
            legend: {
                show: true,
                placement: 'outside'
            },
            seriesDefaults: {
                renderer: $.jqplot.BarRenderer,
                pointLabels: { show: true, location:'n',hideZeros: true},
                rendererOptions: {
                    barPadding: 2
                }
            },
            series: [
                {label: '發票金額'},
                {label: '作廢金額'},
                {label: '註銷金額'}
            ],
            axes: {
                xaxis: {
                    label: '',
                    renderer: $.jqplot.CategoryAxisRenderer,
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                    ticks: dateArray,
                    tickOptions: {
                        angle: -0
                    }
                },
                yaxis: {
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    max: maxAmount,
                    min: 0,
                    tickOptions: {
                        formatString: '%d',
                        angle: -0
                    }
                }
            }
        });

    });



    function getDay(days){
        var now = new Date();
        now.setDate(now.getDate()-days);
        var yyyy = now.getFullYear().toString();
        var mm = (now.getMonth()+1).toString(); // getMonth() is zero-based
        var dd  = now.getDate().toString();
        return yyyy + (mm[1]?mm:"0"+mm[0]) + (dd[1]?dd:"0"+dd[0]); // padding
    }

    function getCloseMaxNumber(number)
    {

        var length =  number.toString().length;
        if(length<3)
        {
            number = Math.ceil(number/4)*4;
            return number;
        }
        var first2number =  number.toString().substring(0,2);
        var processNumber = parseInt(first2number) +1;

        var newNumber  = Math.ceil((processNumber)/4)*4;
        for( i =0; i< length-2; i++)
        {
            newNumber = newNumber *10;
        }


        return newNumber;


    }
//    jQuery(window).load(function () {
//                alert('本次系統升級已順利結束, 電子發票證明聯總計部分已加入千分號, \n 請您將電腦重新開關機一次, 以利印表機驅動程式更新. 謝謝! \n 若您已經重開機過, 請不用理會此訊息.');
//            }
//    );

</script>

<!-- End example scripts -->

<!-- Don't touch this! -->


<script class="include" type="text/javascript" src="/backendAdmin/dist/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="/backendAdmin/dist/examples/syntaxhighlighter/scripts/shCore.min.js"></script>
<script type="text/javascript" src="/backendAdmin/dist/examples/syntaxhighlighter/scripts/shBrushJScript.min.js"></script>
<script type="text/javascript" src="/backendAdmin/dist/examples/syntaxhighlighter/scripts/shBrushXml.min.js"></script>
<!-- End Don't touch this! -->

<!-- Additional plugins go here -->
<script type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.pointLabels.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.barRenderer.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.pieRenderer.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.donutRenderer.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.highlighter.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.funnelRenderer.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.canvasTextRenderer.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
<script language="javascript" type="text/javascript" src="/backendAdmin/dist/plugins/jqplot.canvasAxisLabelRenderer.min.js"></script>

<!-- End additional plugins -->
