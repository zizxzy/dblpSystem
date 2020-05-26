<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap 101 Template</title>

    <%--jquery--%>
    <script src="static/js/jquery-3.4.1.min.js"></script>
    <!-- Bootstrap -->
    <link href="static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

</head>
<body>
<h1>ss_dblp首页</h1><br><br>


<form action="author" method="post">
    作者名: <input type="text" name="authorName" value="A Clara Kanmani"/>
    <input type="submit" value="查询作者"/>
</form>
<br/>

<form action="article" method="post">
    文章标题: <input type="text" name="articleName" value='A "Build-Down" Scheme for Linear Programming.'/>
    <input type="submit" value="查询文章"/>
</form>
<br/>
<br/>

<h3><a href="article/toList">点击此处查看文章列表（JSON解析）</a></h3>
<h3><a href="author/toList">点击此处查看作者列表（JSON解析）</a></h3>
<h3><a href="author/toRelation">点击此处查看作者合作关系图</a></h3>
<h3><a href="author/toRankList">点击此处查看文章数作者排行（JSON解析）</a> </h3>
<h3><a href="fuzzyQuery/toQueryPage">点击此处进行模糊查询</a></h3>
<h3><a href="hotWords/toAnnulHotWords">点击此处查看年度热词表格</a></h3>
<h3><a href="comSubGraph/toQuerySubGraph">点击此处进行查询k阶子图的详细信息</a></h3>
<h3>下面是初始化模糊查询需要的列表的按钮，需要时间大概三、四分钟（摊手），<strong>建议先点击一次</strong>，在初始化期间可以进行其他操作</h3>
<button type="button" class="btn btn-danger" id="init" onclick="initTitle()">初始化</button>
<script type="text/javascript">
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

                }
            },
            error: function (err) {
                alert(err);
            }
        })
    }

</script>
</body>
</html>
