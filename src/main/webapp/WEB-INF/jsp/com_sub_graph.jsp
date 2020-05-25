<%--
  Created by IntelliJ IDEA.
  User: lizeyu
  Date: 2020/3/29
  Time: 15:45
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Query SubGraph</title>
    <%--jquery--%>
    <script src="${ctx}/static/js/jquery-3.4.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/paging/jquery.sPage.css">
    <%--Bootstrap--%>
    <link href="${ctx}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script src="${ctx}/static/paging/jquery.sPage.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/progress/nprogress.css" rel="external nofollow" >
    <script src="${ctx}/static/progress/nprogress.js" type="text/javascript"></script><br>
</head>
<body>
<div class="container">
    <div class="row">
        <%--标题--%>
        <div class="col-md-12">
            <h1>SCUT-科学文献管理系统-Query SubGraph</h1>
        </div>
    </div>
    <br/>
    <%--搜索框--%>
    <div class="row">
        <div class="col-md-12">

            <div class="form-group">
                <label for="queryArray">阶数 K</label>
                <input type="text" class="form-control" id="queryArray"
                       placeholder="请输入阶数K">
                <small id="emailHelp" class="form-text text-muted">
                    请输入阶数 K
                    示例: 5
                    <br/>可输入1-61、64-66、68-69、71、75、77-79、85-86、92、94-96、99、101、105、119、264、287
                    <br/>输入其他的数字不会有数据返回
                    <br/>较低阶数子图较多，请耐心等待
                </small>
            </div>
            <button type="button" class="btn btn-primary" id="btn1" onclick="query_com_subgraph(1)">提交</button>

        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <table class="table-hover table">
                <caption></caption>
                <thead>
                <tr>
                    <th>序号</th>
                    <th>子图结果</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
    <div id="paging">

    </div>
</div>


<script>
    //进度条配置
    $(window).ajaxStart(function () {
        NProgress.start();
    });
    $(window).ajaxStop(function () {
        NProgress.done();
    });
    function build_inited_list(result) {
        $("table tbody").empty();
        let ComSubGraphCounts = result.dataMap.comSubGraphCounts;
        Object.getOwnPropertyNames(ComSubGraphCounts).forEach((key) => {
            //3.对每一阶数和其完全子图的个数处理
            let K = key;
            let number = ComSubGraphCounts[key];
            let KTitle = $("<td></td>").append(K);
            let KNumber = $("<td></td>").append(number);
            let KTr =  $("<tr></tr>").append(KTitle);
            KTr.append(KNumber);
            KTr.appendTo($("table tbody"));
        });
    }

    let tag = true;
    let inputValue = "";
    window.onload = () => {
        $.ajax({
            type: "POST",
            url: "${ctx}/comSubGraph/init",
            traditional: true,
            headers: {'Content-Type': 'application/json;charset=utf8'},
            data: JSON.stringify({
                "type": "init"
            }),
            success: function (result) {
                console.log(result);
                //6.数据处理
                build_inited_list(result);
            },
            error: function (err) {
                alert("无符合要求的查询数据" + err);
            }
        });
    };

    function build_queryRes_list(result) {
        //1.清空上一页的显示数据
        $("table tbody").empty();
        $("caption").empty();
        //2. 获取返回的数据
        let comSubGraph = result.dataMap.comSubGraph.comSubGraphAuthors;
        let number = result.dataMap.comSubGraph.num;
        let k = result.dataMap.num;
        //3. 给表格标题赋值
        let title = $("caption").append("共有" + number + "个" + k + "阶完全子图");
        //4. 遍历返回的子图列表
        $.each(comSubGraph, (index, item) => {
            //5. 对每一个子图的作者名进行拼接
            let authorTxt = item.toString();
            // 生成一行tr
            let itemTr = $("<tr></tr>");
            //7.每一个属性字段放在一个td里面
            itemTr.append($("<td></td>").append(index));
            itemTr.append($("<td></td>").append(authorTxt));
            //8.将此tr加到table tbody里面
            itemTr.appendTo($("table tbody"));
        })
    }

    function query_com_subgraph(page) {
        // 1.获取输入框的值
        let str = document.getElementById('queryArray').value;
        //2. 输入数据校验
        if (str === null) {
            alert("请输入完整的数据！！！");
            return;
        }
        if (typeof Number(str) !== "number") {
            alert("请输入数字类型");
            return;
        }
        tag = inputValue === str;
        console.log(tag + " " + inputValue);
        inputValue = str;

        let p = page || 1;
        //3. 数据赋值
        let data = {
            number: str,
            pageSize: 10,
            pageNum: p,
            tag: tag
        };
        //4. ajax请求
        $.ajax({
            type: "POST",
            url: "${ctx}/comSubGraph/json",
            traditional: true,
            headers: {'Content-Type': 'application/json;charset=utf8'},
            data: JSON.stringify(data),
            success: function (result) {
                //5.无子图的阶数查询处理
                if (result.dataMap.comSubGraph.num === 0) {
                    alert("无查询结果，请输入有完全子图的阶数！！！");
                    return;
                }
                console.log(result);
                //6.数据处理
                build_queryRes_list(result);
                //7.调用分页
                $("#paging").sPage({
                    page: page || 1, //当前页码
                    showTotal: true,
                    totalTxt: "共" + result.dataMap.comSubGraph.num + "个子图",
                    pageSize: 10, //每页显示多少条数据，默认10条
                    showSkip: true,
                    prevPage: "last page",
                    nextPage: "next page",
                    total: result.dataMap.comSubGraph.num, //数据总条数,后台返回
                    backFun: function (page) { //点击分页按钮回调函数，返回当前页码
                        console.log(page);
                        query_com_subgraph(page);
                    }
                });
            },
            error: function (err) {
                alert("无符合要求的查询数据" + err);
            }
        });

    }
</script>
</body>
</html>
