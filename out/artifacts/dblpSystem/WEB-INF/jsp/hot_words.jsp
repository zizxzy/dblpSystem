<%--
  Created by IntelliJ IDEA.
  User: lizeyu
  Date: 2020/3/29
  Time: 0:16
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>AnnualHotWordsTable</title>
    <%--jquery--%>
    <script src="${ctx}/static/js/jquery-3.4.1.min.js"></script>
    <%--Bootstrap--%>
    <link href="${ctx}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <div class="row">
        <%--标题--%>
        <div class="col-md-12">
            <h1>SCUT-科学文献管理系统-Annual HotWords</h1>
        </div>
    </div>
    <br/>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover">

                <caption>年份热词表格</caption>
                <button type="button" class="btn btn-primary" id="btn1" onclick="initHotWords()">生成年度热词表格</button>

                <thead>
                <tr>
                    <th>年份</th>
                    <th>单词1:频次</th>
                    <th>单词2:频次</th>
                    <th>单词3:频次</th>
                    <th>单词4:频次</th>
                    <th>单词5:频次</th>
                    <th>单词6:频次</th>
                    <th>单词7:频次</th>
                    <th>单词8:频次</th>
                    <th>单词9:频次</th>
                    <th>单词10:频次</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>

</div>

<script>
    function build_hotWords_table(result) {
        //1.数据获取
        let hotWords = result.dataMap.hotWords;
        $("table tbody").empty();
        //2.遍历返回的对象
        Object.getOwnPropertyNames(hotWords).forEach((key) => {
            //3.对每一年和每一年的热词处理
            let year = key;
            let hotWordsList = hotWords[key];
            let yearTitle = $("<td></td>").append(year);
            let yearTr = $("<tr></tr>");
            yearTr.append(yearTitle);

            $.each(hotWordsList, (idnex, item) => {
                yearTr.append($("<td></td>").append(item.value + ":" + item.count));
            });
            yearTr.appendTo($("table tbody"));
        });

    }

    function initHotWords() {
        $.ajax({
            url: "${ctx}/hotWords/json",
            type: "GET",
            success: function (result) {
                console.log(result);
                //数据处理
                build_hotWords_table(result);
            },
            error: function (err) {
                alert("无符合要求的查询数据" + err);
            }
        });
    }
</script>
</body>
</html>
