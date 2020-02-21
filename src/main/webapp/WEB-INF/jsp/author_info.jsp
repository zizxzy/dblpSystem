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
                        <label class="col-sm-2 control-label">作者名称</label>
                        <div class="col-sm-9">
                            <p class="form-control-static" id="author_name_static"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">索引</label>
                        <div class="col-sm-9">
                            <p class="form-control-static" id="author_locations_static"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">合作者</label>
                        <div class="col-sm-9">
                            <p class="form-control-static" id="author_collaborators_static"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">记录</label>
                        <div class="col-sm-9">
                            <p class="form-control-static" id="author_records_static"></p>
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
    /**
     * 查询指定id的员工信息并显示
     * @param empId
     */
    function getAuthorInfo(author_name) {
        $.ajax({
            url: "${ctx}/author",
            type: "POST",
            data: {'authorName': author_name},
            success: function (result) {
                if (result.code === "200") {
                    var author = result.dataMap.author;
                    $('#author_name_static').text(author.name);
                    var locations = "";
                    $.each(author.locations, function (index, item) {
                        locations += item;
                        if (index !== author.locations.length - 1) {
                            locations += ", ";
                        }
                    });
                    $("#author_locations_static").text(locations);
                    var collaborators = "";
                    $.each(author.collaborators, function (index, item) {
                        collaborators += item + "<br/>";
                    });
                    $("#author_collaborators_static").append(collaborators);
                    var articles = "";
                    $.each(author.articles, function (index, item) {
                        articles += "第" + index + "条记录:\n" + item + "\n\n";
                    });
                    $('#author_records_static').text(articles)
                }
            }
        });
    }

    /**
     * 给所有编辑按钮(class：.btn_edit)绑定点击事件
     *        $(".btn_edit").click(function () {
					alert("edit");
				});
     此种方式：页面加载完成会去给具有.btn_edit类的按钮绑定事件，
     但我们的表格数据(包括操作部分的修改和删除按钮)是页面加载完后通过发送ajax请求
     获取数据，再构造出来的,因此此种方式无法给之后创建的按钮绑定上事件

     解决：
     1.在创建按钮的时候绑定事件
     2.使用.live()方法绑定事件，即便是后面才创建出来，也有效
     3.jquery高版本取消了.live()方法，提供了.on()方法，使用
     $("父元素选择器").on("事件名"，"子元素选择器", function () {
						alert("edit");
					});
     */
    /**(
     * 遍历document的全部子孙节点，给所有具有.btn_edit的按钮绑定单击事件
     */
    $(document).on("click", ".btn_info", function () {
        // alert("edit");
        // 2.查出当前员工信息并显示
        var author_name = $(this).attr("author_name");
        getAuthorInfo(author_name);
        // 3.弹出模态框进行修改操作
        $("#infoModal").modal({
            backdrop: "static"
        });
    });

</script>

