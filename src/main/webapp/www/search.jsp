<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <%@ include file="head.jsp" %>
    </head>

    <body>
        <%@ include file="sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Rechercher le profil d'un joueur</h2>

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

                <form class="form-horizontal" action="search" method="post">
                    <fieldset>
                        <div class="form-group">
                            <label for="inputPseudo" class="col-lg-2 control-label">Pseudo</label>

                            <div class="col-lg-10">
                                <input type="text" class="form-control" name="pseudo" id="inputPseudo" placeholder="Pseudo">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-lg-10 col-lg-offset-2">
                                <button type="submit" class="btn btn-primary">Rechercher</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </body>
</html>