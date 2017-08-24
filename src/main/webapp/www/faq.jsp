<%@ page import="net.samagames.samadmin.questions.Question" %>
<%@ page import="java.util.Collection" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <%@ include file="head.jsp" %>
    </head>

    <body>
        <%@ include file="sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Foire Aux Questions</h2>

                <p>
                    Chère Equipe, mais plus particulièrement Guides. Cette page est à votre disposition pour vous fournir des exemples types de questions que pourront vous poser
                    les joueurs. Avec celles-ci vous seront exposé les réponses que vous devrez leur répondre. Au fur et à mesure du temps, vous apprenderez les réponses et vous
                    n'aurez plus besoin de visiter cette page ;)
                </p>

                <br/>

                <%
                    for (Question question : (Collection<Question>) request.getAttribute("questions"))
                    {
                %>
                <div class="panel panel-default">
                    <div class="panel-heading"><%= question.getQuestion() %></div>
                    <div class="panel-body"><%= question.getAnswer() %></div>
                </div>

                <br/>
                <%
                    }
                %>
            </div>
        </div>
    </body>
</html>