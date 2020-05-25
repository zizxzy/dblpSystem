<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<!-- 引入 ECharts 文件 -->
	<script src="../../static/js/echarts.js"></script>
	<style type="text/css">
        html, body, #main { height: 100%; width: 100%;margin:0;padding:0 }
    </style>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">

</head>
<body>
	<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
	<script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script> 
	<script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
	<div class="container">
		<div class="row">
			<div class="input-group col-md-8">
				<div class="input-group-prepend">
					<span class="input-group-text">作者名称</span>
				</div>
				<input id="anthorsName" type="text" aria-label="Name" class="form-control">
			</div>
			<button type="button" id="getData" class="btn btn-primary col-md-2">获取合作图</button>
		</div>
	</div>
	<div id="main" style="width: 100%;height:90%;"></div>
	<script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        
        // 指定图表的配置项和数据
         option = {
            title: { text: '作者合作关系图' },
            tooltip: {
                formatter: function (x) {
                    return x.data.des;
                }
            },
            series: [
                {
                    type: 'graph',
                    layout: 'force',
                    symbolSize: 80,
                    roam: true,
                    edgeSymbol: ['circle', 'none'],
                    edgeSymbolSize: [4, 10],
                    edgeLabel: {
                        normal: {
                            textStyle: {
                                fontSize: 20
                            }
                        }
                    },
                    force: {
                        repulsion: 2500,
                        edgeLength: [10, 50]
                    },
                    draggable: true,
                    itemStyle: {
                        normal: {
                            color: '#4b565b'
                        }
                    },
                    lineStyle: {
                        normal: {
                            width: 2,
                            color: '#4b565b'

                        }
                    },
                    edgeLabel: {
                        normal: {
                            show: true,
                            formatter: function (x) {
                                return x.data.name;
                            }
                        }
                    },
                    label: {
                        normal: {
                            show: true,
                            textStyle: {
                            }
                        }
                    },
                    data: [],
                    links: []
                }
            ]
        }; 
        //拿数据
    	$(function(){
    		$('#getData').click(function(){
    			option['series'][0]['data'] = [];
    			option['series'][0]['links'] = [];
    			
    			inputnames = $('#anthorsName').val().split(",");
    			console.log(inputnames);
	        	$.ajax({
		            type: "POST",
		            url: "http://localhost:8888/dblpSystem/author/collaboration",
		            contentType: "application/json;charset=utf-8",
		            // data:JSON.stringify(['Toshihiro Osaragi']),
		            data:JSON.stringify(inputnames),
		            dataType: "json",
		            success:function (message) {
		            	console.log(message);
		            	if(message["msg"]=="操作成功"){
		            		let edges = message["dataMap"]["collaborativeRelations"];
		            	
			                let names = []
			                for(let i =0;i<edges.length;i++){
			                	//加姓名到临时数组names中
			                	if(names.indexOf(edges[i]["authorName1"])==-1){
			                		names.push(edges[i]["authorName1"]);
			                	}
			                	if(names.indexOf(edges[i]["authorName2"])==-1){
			                		names.push(edges[i]["authorName2"]);
			                	}
			                	option['series'][0]['links'].push({source:edges[i]["authorName1"],target:edges[i]["authorName2"],des:edges[i]["articleName"]})
			                }
			                //临时数据names中的数据加入到option中
			                for(let i =0;i<names.length;i++){
			                	option['series'][0]['data'].push({name:names[i]})
			                }
			                
			                myChart.setOption(option);
		            	}
		            	
		            
		            },
		            error:function (message) {
		                alert("提交失败"+JSON.stringify(message));
		            }
		        });
	        })
    	});

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
    <script type="text/javascript">
    	
    </script>
</body>
</html>