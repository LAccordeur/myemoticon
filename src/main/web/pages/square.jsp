<%--
  Created by IntelliJ IDEA.
  User: L'Accordeur
  Date: 2017/1/6
  Time: 17:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Photo</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="http://labfile.oss.aliyuncs.com/libs/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://labfile.oss.aliyuncs.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">
    <!-- 首部 start -->
    <div class="row">
        <div class="col-xs-8 col-xs-offset-2">
            <h3 class="page-header">
                &nbsp;&nbsp;&nbsp;<small>Totals:<span class="badge">${allImages.size()}</span></small>
            </h3>
        </div>
    </div>
    <!-- 首部 end -->

    <!-- 显示图片列表 -->
    <c:forEach items="${allImages}" varStatus="status" var="image">
    <c:choose>
        <c:when test="${status.first or status.index % 4 eq 0}">
            <div class="row">
            <div class="col-xs-2 col-xs-offset-2">
                <a href="#" class="thumbnail text-center">
                    <img name="${image.name}" date="<fmt:formatDate value='${image.date}' pattern='yyyy-MM-dd HH:mm'/>" style="width: 140px; height: 130px;" src="http://of6xakfsc.bkt.clouddn.com/${image.url}"><!--这里请填写自己的链接地址，详见下图-->
                    <input class="pull-left" type="checkbox" value="${image.id}" url="${image.url}"/>${image.name }
                </a>
            </div>
        </c:when>
        <c:when test="${status.index % 4 eq 3 and not status.last }">
            <div class="col-xs-2">
                <a href="#" class="thumbnail text-center">
                    <img name="${image.name}" date="<fmt:formatDate value='${image.date}' pattern='yyyy-MM-dd HH:mm'/>" style="width: 140px; height: 130px;" src="http://of6xakfsc.bkt.clouddn.com/${image.url}"><!--这里请填写自己的链接地址，详见下图-->
                    <input class="pull-left" type="checkbox" value="${image.id}" url="${image.url}" />${image.name }
                </a>
            </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="col-xs-2">
                <a href="#" class="thumbnail text-center">
                    <img name="${image.name}" date="<fmt:formatDate value='${image.date}' pattern='yyyy-MM-dd HH:mm'/>" style="width: 140px; height: 130px;" src="http://of6xakfsc.bkt.clouddn.com/${image.url}"><!--这里请填写自己的链接地址，详见下图-->
                    <input class="pull-left" type="checkbox" value="${image.id}" url="${image.url}"/>${image.name }
                </a>
            </div>
        </c:otherwise>
    </c:choose>
    <c:if test="${status.last}">
</div>
</c:if>
</c:forEach>
<!-- 显示图片列表 end -->
</div>

<!-- 显示图片对话框 start -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body" id="modal-content">
            </div>
        </div>
    </div>
</div>
<!-- 显示图片对话框 end -->



<script src="http://labfile.oss.aliyuncs.com/jquery/1.11.1/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        //点击图片
        $('img').click(function() {
            $('#myModalLabel').html($(this).attr('name') + '&nbsp;&nbsp;&nbsp;<small>' + $(this).attr('date') + '</small>');
            $('#modal-content').html('<img class=\'img-responsive\' src=\'' + $(this).attr('src') + '\'/>');
            $('#myModal').modal('show');
        });


    });

</script>
</body>
</html>