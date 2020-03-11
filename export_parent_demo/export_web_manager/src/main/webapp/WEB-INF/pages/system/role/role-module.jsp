<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../base.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <base href="${ctx}/">
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 页面meta /-->
    <link rel="stylesheet" href="plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="plugins/ztree/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="plugins/ztree/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="plugins/ztree/js/jquery.ztree.excheck-3.5.js"></script>

    <SCRIPT type="text/javascript">
        var zTreeObj;
        /*
        *   配置ztree
        * */
        var setting = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
        /**
         * 设置ztree的json数据
         */
        var zNodes =[
            { id:11, pId:1, name:"随意勾选 1-1", open:true},
            { id:111, pId:11, name:"随意勾选 1-1-1"},
            { id:112, pId:11, name:"随意勾选 1-1-2"},
            { id:12, pId:1, name:"随意勾选 1-2", open:true},
            { id:121, pId:12, name:"随意勾选 1-2-1", checked:true},
            { id:122, pId:12, name:"随意勾选 1-2-2"},
            { id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
            { id:21, pId:2, name:"随意勾选 2-1"},
            { id:22, pId:2, name:"随意勾选 2-2", open:true},
            { id:221, pId:22, name:"随意勾选 2-2-1", checked:true},
            { id:222, pId:22, name:"随意勾选 2-2-2"},
            { id:23, pId:2, name:"随意勾选 2-3"},
            { id:1, pId:0, name:"随意勾选 1", open:true}
        ];

        /**
         * 在页面上初始化ztree( 展示)
         */
        $(document).ready(function(){ //页面初始化执行的js方法
            //在页面加载的时候,发送ajax请求,帮助获取json数据,展示ztree树
            $.get("/system/role/getZtreeNodes.do?roleId=${role.id}",function(data) {
                //成功调用ajax之后,执行的业务逻辑
                zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, data); //位置,配置,json
                zTreeObj.expandAll(true);// 参数 :true:展开节点 ,false:关闭
            })
        });

        //1,2,3,4
        function submitCheckedNodes() {
            //1.构造模块的ids字符串
            var nodes = zTreeObj.getCheckedNodes(true); //获取所有被勾选的节点 (true:被勾选,flase:未被勾选)
            var moduleIds="";
            for(var i=0;i<nodes.length;i++) {
                //浏览器控制台打印选中的节点
                var node = nodes[i];
                //console.log(node);
                moduleIds += node.id;
                if(i<nodes.length-1) {
                    moduleIds += ",";
                }
            }
            $("#moduleIds").val(moduleIds);
            //2.提交form表单
            $("#icform").submit();
        }
    </SCRIPT>
</head>

<body style="overflow: visible;">
<div id="frameContent" class="content-wrapper" style="margin-left:0px;height: 1200px" >
    <section class="content-header">
        <h1>
            菜单管理
            <small>菜单列表</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
        </ol>
    </section>
    <!-- 内容头部 /-->

    <!-- 正文区域 -->
    <section class="content">

        <!-- .box-body -->
        <div class="box box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">角色 [${role.name}] 权限列表</h3>
            </div>

            <div class="box-body">

                <!-- 数据表格 -->
                <div class="table-box">
                    <!--工具栏-->
                    <div class="box-tools text-left">
                        <button type="button" class="btn bg-maroon" onclick="submitCheckedNodes();">保存</button>
                        <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
                    </div>
                    <!--工具栏/-->
                    <!-- 树菜单 -->
                    <form id="icform" name="icform" method="post" action="${ctx}/system/role/updateRoleModule.do">
                        <input type="text" name="roleid" value="${role.id}"/>
                        <input type="text" id="moduleIds" name="moduleIds" value=""/>
                        <div class="content_wrap">
                            <div class="zTreeDemoBackground left" style="overflow: visible">
                                <ul id="treeDemo" class="ztree"></ul>
                            </div>
                        </div>
                    </form>
                    <!-- 树菜单 /-->

                </div>
                <!-- /数据表格 -->

            </div>
            <!-- /.box-body -->
        </div>
    </section>
</div>
</body>
</html>