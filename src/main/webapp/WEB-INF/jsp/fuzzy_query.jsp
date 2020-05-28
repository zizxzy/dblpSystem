<%--
  Created by IntelliJ IDEA.
  User: lizeyu
  Date: 2020/2/22
  Time: 14:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Fuzzy Query</title>
    <%--jquery--%>
    <script src="${ctx}/static/js/jquery-3.4.1.min.js"></script>
    <%--Bootstrap--%>
    <link href="${ctx}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/paging/jquery.sPage.css">
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
            <h1>SCUT-科学文献管理系统-Fuzzy Query</h1>
        </div>
    </div>
    <br/>
    <%--搜索框--%>
    <div class="row">
        <div class="col-md-12">

            <div class="form-group">
                <label for="queryArray">关键词组 queryArray</label>
                <input type="text" class="form-control" id="queryArray"
                       placeholder="请输入以,作为分隔符的关键词组">
                <small id="emailHelp" class="form-text text-muted">
                    请输入以,为分隔的关键词组，否则可能无法匹配，需要输入完整的单词组，com、vi这些非单词不好生效
                    示例: machine,learning
                    <br/>目前暂不支持含除ASCII以外字符的查询
                    <br/>使用前需要在首页先进行一遍初始化
                    <br/>关键词较多时，因为数据量较大，请耐心等待，一个词大概几秒，两个词大概需要7秒,三个词大概需要9秒
                </small>
            </div>
            <button type="button" class="btn btn-primary" id="btn1" onclick="ajaxPage(1)">提交</button>

       <%--     <button type="button" class="btn btn-danger" id="init" onclick="initTitle()">初始化</button>--%>

        </div>
    </div>
<%--    <div class="progress mt-2" id="progressFath">
        <div id="progress" class="progress-bar progress-bar-striped active " role="progressbar" aria-valuenow="45"
             aria-valuemin="0" aria-valuemax="100" style="width: 80%">
            <span class="sr-only">45% Complete</span>
        </div>
    </div>--%>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>标题</th>
                    <th>在xml文件中的索引</th>
                    <th>操作</th>
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
<jsp:include page="queryResInfo.jsp"/>
<script>
    /**
     * 根据页号进行ajax请求，不刷新页面
     * */
    let tag = true;
    let inputValue = "";
    //ajax的进度条配置
    $(window).ajaxStart(function () {
        NProgress.start();
    });
    $(window).ajaxStop(function () {
        NProgress.done();
    });
    /**
     *初始化原始数据
     * */
    function initTitle() {
        //1. 发起初始化ajax请求
        $.ajax({
            type: "POST",
            url: "${ctx}/fuzzyQuery/init",
            traditional: true,
            data: JSON.stringify({
                "type": "init"
            }),
            headers: {'Content-Type': 'application/json;charset=utf8'},

            success: function (res) {
                console.log(res);
                //2. 初始化成功，进度条拉满
                if (res.dataMap.msg === "success") {
                    document.getElementById("progress").style.cssText = "width: 100%";
                    //3. 移除进度条
                    setTimeout(function () {
                        document.getElementById("progressFath").remove();
                    }, 1000);
                }
            },
            error: function (err) {
                alert(err);
            }
        })
    }

    /*根据后台返回的结果渲染页面*/
    function build_queryRes_table(result) {
        // 清空上一页的显示数据
        $("table tbody").empty();
        // 取出article列表
        var articles = result.dataMap.pageArticles;
        // 遍历集合articles, 对于每一条记录，执行回调函数
        // 将每一条记录封装到一个tr中，添加到表格中
        $.each(articles, function (index, item) {
          if(item !== null){
             // 每一个属性字段放在一个td里面
            var titleTd = $("<td></td>").append(item.title);
            var locationsTd = $("<td></td>").append(item.locations);
            // <button class="btn btn-info btn-sm">
            //         <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
            //         查看详情
            // </button>
            var btnEdit = $("<button></button>").addClass("btn btn-info btn-sm btn_info")
                .append($("<span></span>").addClass("glyphicon glyphicon-pencil"))
                .append("查看详情");
            // 自定义一个属性，用于保存当前记录标题,便于之后的查询传值
            btnEdit.attr("article_title", item.title);

            // 所有td组成一个tr
            var operateTd = $("<td></td>").append(btnEdit);
            var itemTr = $("<tr></tr>").append(titleTd).append(locationsTd).append(operateTd);
            // 将此tr加到table tbody里面
            itemTr.appendTo($("table tbody"));
          }
        });
    }

    /*
    * page：页数
    * ajax请求，根据页数和页码从后台获取数据
    * */
    function ajaxPage(page) {
        // 获取输入框的值
        let str = document.getElementById('queryArray').value;
        if (str === null) {
            alert("请输入完整的数据！！！");
            return;
        }
        //tag标志赋值
        tag = inputValue === str;
        inputValue = str;
        //处理原始输入框中的字符串
        let processSrt = str.trim().split(",");
        let queryArray = [];
        for (let i = 0; i < processSrt.length; i++) {
            queryArray.push(processSrt[i]);
        }
        let p = page || 1;
        //构建数据
        let data = {
            pageNumber: p,
            pageSize: 10,
            queryArray: queryArray,
            tag: tag
        };
        //发起ajax请求
        $.ajax({
            type: "POST",
            url: "${ctx}/fuzzyQuery/json",
            traditional: true,
            headers: {'Content-Type': 'application/json;charset=utf8'},
            data: JSON.stringify(data),
            success: function (result) {
                console.log(result);
                //数据处理
                build_queryRes_table(result);
                // 调用分页
                $("#paging").sPage({
                    page: page || 1, //当前页码
                    showTotal: true,
                    totalTxt: "共" + result.dataMap.totalRecords + "条",
                    pageSize: 10, //每页显示多少条数据，默认10条
                    showSkip: true,
                    prevPage: "last page",
                    nextPage: "next page",
                    total: result.dataMap.totalRecords, //数据总条数,后台返回
                    backFun: function (page) { //点击分页按钮回调函数，返回当前页码
                        ajaxPage(page);
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
