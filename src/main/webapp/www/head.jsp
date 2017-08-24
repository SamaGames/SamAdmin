<%@ page import="net.samagames.samadmin.SamAdmin" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String prefix = null;

    if (request.getAttribute("base-url") != null)
        prefix = (String) request.getAttribute("base-url");
%>
        <meta charset="utf-8" />
        <title><%= request.getAttribute("title") %> - SamAdmin</title>

        <link rel="icon" href="<%= (prefix != null ? prefix : "") %>www/favicon.ico">

        <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
        <link href="<%= (prefix != null ? prefix : "") %>www/static/css/roboto.min.css" rel="stylesheet">
        <link href="<%= (prefix != null ? prefix : "") %>www/static/css/material.min.css" rel="stylesheet">
        <link href="<%= (prefix != null ? prefix : "") %>www/static/css/ripples.min.css" rel="stylesheet">
        <link href="<%= (prefix != null ? prefix : "") %>www/static/css/styles.css" rel="stylesheet">
        <link href="<%= (prefix != null ? prefix : "") %>www/static/css/sidebar.css" rel="stylesheet">

        <script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

        <script src="<%= (prefix != null ? prefix : "") %>www/static/js/ripples.min.js"></script>
        <script src="<%= (prefix != null ? prefix : "") %>www/static/js/material.min.js"></script>
        <script src="<%= (prefix != null ? prefix : "") %>www/static/js/canvasjs.js"></script>

        <%
            if (!request.getRequestURI().equals((((SamAdmin) request.getAttribute("samadmin")).getConfiguration().PRODUCTION ? "" : "/SamAdmin") + "/www/login.jsp"))
            {
        %>
        <script src="<%= (prefix != null ? prefix : "") %>www/static/js/sidebar.js"></script>
        <%
            }
        %>

        <script>
            $(document).ready(function() {
                $.material.init();
            });
        </script>