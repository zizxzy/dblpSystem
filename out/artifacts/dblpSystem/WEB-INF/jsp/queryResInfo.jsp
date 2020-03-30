<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<!-- 详情信息的模态框 -->
<div class="modal fade" id="infoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">详情记录</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="emp_edit_form">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">标题</label>
                        <div class="col-sm-9">
                            <p class="form-control-static" id="article_title_static"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">索引</label>
                        <div class="col-sm-9">
                            <p class="form-control-static" id="article_locations_static"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">记录</label>
                        <div class="col-sm-9">
                            <p class="form-control-static" id="article_records_static"></p>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    function clearInfo() {
        $("#article_title_static").text('');
        $("#article_locations_static").text('');
        $("#article_records_static").text('');
    }

    /**
     * 查询指定id的员工信息并显示
     * @param empId
     */
    function getArticleInfo(article_name) {
        $.ajax({
            url: "${ctx}/article",
            type: "POST",
            data: {'articleName': article_name},
            success: function (result) {
                if (result.code === "200") {
                    var article = result.dataMap.article;
                    $('#article_title_static').text(article.title);

                    var locations = "";
                    $.each(article.locations, function (index, item) {
                        locations += item;
                        if (index !== article.locations.length - 1) {
                            locations += ", ";
                        }
                    });
                    $("#article_locations_static").text(locations);

                    var articles = "";
                    $.each(article.records, function (index, item) {
                        articles += "第" + index + "条记录:\n" + item + "\n\n";
                    });
                    $("#article_records_static").text(articles);
                }
            }
        });
    }

    /**(
     * 遍历document的全部子孙节点，给所有具有.btn_edit的按钮绑定单击事件
     */
    $(document).on("click", ".btn_info", function () {
        clearInfo();
        // alert("edit");
        // 2.查出当前员工信息并显示
        var article_title = $(this).attr("article_title");
        getArticleInfo(article_title);
        // 3.弹出模态框进行修改操作
        $("#infoModal").modal({
            backdrop: "static"
        });
    });

</script>

