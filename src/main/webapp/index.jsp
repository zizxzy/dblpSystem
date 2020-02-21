<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<br/><br/>

<h3><a href="article/toList">点击此处查看文章列表（JSON解析）</a></h3>
<h3><a href="author/toList">点击此处查看作者列表（JSON解析）</a></h3>
<h3><a href="author/toRankList">点击此处查看文章数作者排行（JSON解析）</a> </h3>
</body>
</html>
