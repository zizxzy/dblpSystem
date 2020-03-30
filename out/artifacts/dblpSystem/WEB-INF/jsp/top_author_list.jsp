<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Top Authors</title>
    <%--jquery--%>
    <script src="${ctx}/static/js/jquery-3.4.1.min.js"></script>
    <%--Bootstrap--%>
    <link href="${ctx}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <%--第一行 标题--%>
    <div class="row">
        <div class="col-md-12">
            <h1>SCUT-科学文献管理系统-发表文章数作者排行</h1>
        </div>
    </div>
    <br/>
    <%--第二行 搜索框--%>
    <div class="row">
        <div class="col-md-12">
            <form action="${ctx}/author" method="post">
                <div class="form-group">
                    <label for="authorName">作者名称 Author name</label>
                    <input type="text" name="authorName" class="form-control" id="authorName" placeholder="请输入作者名称">
                    <small id="emailHelp" class="form-text text-muted">
                        请输入完整作者名，否则可能无法匹配
                        示例: Fritz Obermeyer
                    </small>
                </div>
                <button type="submit" class="btn btn-primary">提交</button>
            </form>
        </div>
    </div>
    <%--第三行 表格--%>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>排名</th>
                    <th>作者</th>
                    <th>发表文章数</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
    <%--第四行 底部导航栏--%>
    <div class="row">
        <%--页面信息--%>
        <div class="col-md-6" id="page_info_area"></div>
        <%--页面导航--%>
        <div class="col-md-6" id="nav_info_area"></div>
    </div>
</div>

<jsp:include page="author_info.jsp"/>


<script>

    <%--页面加载完成后，发起ajax请求，获取json数据--%>
    $(function () {
        ajax_to_page(1);
    });

    /**
     * 发送ajax请求，获取指定页数据
     * @param pageNum
     */
    function ajax_to_page(pageNum) {
        $.ajax({
            url: "${ctx}/author/top",
            type: "GET",
            // result是服务器返回结果(InfoDTO对象)
            success: function (result) {
                // 1.解析并显示员工信息
                build_authors_table(result);
            }
        });
    }

    /**
     * 将获取的json数据解析并显示到table员工信息部分
     * @param result
     */
    function build_authors_table(result) {
        // 清空上一页的显示数据
        $("table tbody").empty();

        // 取出article列表
        var authors = result.dataMap.topAuthors;
        // 遍历集合author, 对于每一条记录，执行回调函数
        // 将每一条记录封装到一个tr中，添加到表格中
        $.each(authors,function (index,item) {
            // 每一个属性字段放在一个td里面
            var rankTd = $("<td></td>").append(index+1);
            var nameTd = $("<td></td>").append(item.name);
            var articleNumberTd = $("<td></td>").append(item.articleNumber);

            // <button class="btn btn-info btn-sm">
            //         <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
            //         查看详情
            // </button>
            var btnEdit = $("<button></button>").addClass("btn btn-info btn-sm btn_info")
                .append($("<span></span>").addClass("glyphicon glyphicon-pencil"))
                .append("查看详情");
            // 自定义一个属性，用于保存当前记录标题,便于之后的查询传值
            btnEdit.attr("author_name",item.name);

            // 所有td组成一个tr
            var operateTd = $("<td></td>").append(btnEdit);
            var itemTr = $("<tr></tr>").append(rankTd).append(nameTd)
                .append(articleNumberTd).append(operateTd);
            // 将此tr加到table tbody里面
            itemTr.appendTo($("table tbody"));
        });
    }
</script>
</body>
</html>