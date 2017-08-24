<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <%@ include file="head.jsp" %>
    </head>

    <body>
        <div class="container container-table">
            <div class="row vertical-center-row">
                <div class="text-center col-md-4 col-md-offset-4">
                    <form class="form-horizontal" action="login" method="post">
                        <fieldset>
                            <legend>Se connecter à SamAdmin</legend>

                            <%
                                if (request.getAttribute("failed") != null)
                                {
                            %>
                            <p class="text-danger">Nom d'utilisateur ou mot de passe incorrect !</p>
                            <br/>
                            <%
                                }
                            %>

                            <input type="text" class="form-control" name="username" placeholder="Nom d'utilisateur" required>
                            <input type="password" class="form-control" name="password" placeholder="Mot de passe" required>

                            <br/>

                            <button type="submit" class="btn btn-primary btn-lg btn-block">Se connecter</button>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
