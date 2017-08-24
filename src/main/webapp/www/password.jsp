<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <%@ include file="head.jsp" %>
    </head>

    <body>
        <%@ include file="sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Changer son mot de passe</h2>

                <br/>

                <%
                    if (request.getAttribute("error") != null)
                    {
                %>
                <p class="text-danger">Une erreur s'est produite : <%= request.getAttribute("error") %></p>
                <br/>
                <%
                    }
                %>

                <form class="form-horizontal" action="password" method="post">
                    <fieldset>
                        <div class="form-group">
                            <label for="inputPassword" class="col-lg-2 control-label">Nouveau mot de passe</label>

                            <div class="col-lg-10">
                                <input type="password" class="form-control" name="password" id="inputPassword" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-lg-10 col-lg-offset-2">
                                <button type="submit" class="btn btn-primary">Modifier</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </body>
</html>
