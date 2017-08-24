<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.text.WordUtils" %>
<%@ page import="net.samagames.samadmin.users.UserPermissionLevel" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <%@ include file="../head.jsp" %>
    </head>

    <body>
        <%@ include file="../sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Gestion des utilisateurs</h2>

                <br/>

                <%
                    if (request.getAttribute("error") != null)
                    {
                %>
                <div class="alert alert-dismissable alert-danger">
                    <button type="button" class="close" data-dismiss="alert">×</button>
                    <strong>Une erreur s'est produite !</strong> <%= request.getAttribute("error") %>
                </div>

                <br/>
                <%
                    }
                %>

                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>#</th>
                            <td>Nom d'utilisateur</td>
                            <td>UUID Minecraft</td>
                            <td>Permission</td>
                            <td>Actions</td>
                        </tr>
                    </thead>

                    <tbody>
                        <%
                            for (User user : ((ArrayList<User>) request.getAttribute("users")))
                            {
                        %>
                        <tr>
                            <td><%= user.getID() %></td>
                            <td><%= user.getUsername() %></td>
                            <td><%= user.getUUID().toString() %></td>
                            <td><%= WordUtils.capitalize(user.getPermissionLevel().name().toLowerCase()) %></td>
                            <td>
                                <div class="btn-group btn-group-xs">
                                    <a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modalEdit<%= user.getID() %>">Editer</a>

                                    <div class="modal fade" id="modalEdit<%= user.getID() %>">
                                    <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form class="form-horizontal" action="users" method="post">
                                                    <fieldset>
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                            <h4 class="modal-title">Editer l'utilisateur : <%= user.getUsername() %></h4>
                                                        </div>

                                                        <div class="modal-body">
                                                            <div class="form-group">
                                                                <label for="inputUsername" class="col-lg-2 control-label">Nom d'utilisateur</label>

                                                                <div class="col-lg-10">
                                                                    <input class="form-control" type="text" name="username" id="inputUsername" value="<%= user.getUsername() %>" required>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="inputPassword" class="col-lg-2 control-label">Mot de passe</label>

                                                                <div class="col-lg-10">
                                                                    <input class="form-control" type="password" name="password" id="inputPassword">
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="inputUUID" class="col-lg-2 control-label">UUID Minecraft</label>

                                                                <div class="col-lg-10">
                                                                    <input class="form-control" type="text" name="uuid" id="inputUUID" value="<%= user.getUUID().toString() %>" required>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="inputPermission" class="col-lg-2 control-label">Permission</label>

                                                                <div class="col-lg-10">
                                                                    <select class="form-control" name="permission" id="inputPermission" required>
                                                                        <%
                                                                            for (UserPermissionLevel permission : UserPermissionLevel.values())
                                                                            {
                                                                        %>
                                                                        <option value="<%= permission.name() %>" <%= (user.getPermissionLevel() == permission ? "selected" : "")%>><%= WordUtils.capitalize(permission.name().toLowerCase()) %></option>
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>

                                                            <input type="hidden" name="action" value="edit" required>
                                                            <input type="hidden" name="user" value="<%= user.getID() %>" required>
                                                        </div>

                                                        <div class="modal-footer">
                                                            <button type="submit" class="btn btn-primary">Enregistrer</button>
                                                        </div>
                                                    </fieldset>
                                                </form>
                                            </div>
                                        </div>
                                    </div>

                                    <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#modalDelete<%= user.getID() %>">Supprimer</a>

                                    <div class="modal fade" id="modalDelete<%= user.getID() %>">
                                    <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form class="form-horizontal" action="users" method="post">
                                                    <fieldset>
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                            <h4 class="modal-title">Supprimer l'utilisateur : <%= user.getUsername() %></h4>
                                                        </div>

                                                        <div class="modal-body">
                                                            <input type="hidden" name="action" value="delete" required>
                                                            <input type="hidden" name="user" value="<%= user.getID() %>" required>
                                                        </div>

                                                        <div class="modal-footer">
                                                            <button type="submit" class="btn btn-danger">Supprimer</button>
                                                        </div>
                                                    </fieldset>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>

                <br/>

                <button class="btn btn-primary" data-toggle="modal" data-target="#modalCreate">Ajouter un utilisateur</button>

                <div class="modal fade" id="modalCreate">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form class="form-horizontal" action="users" method="post">
                                <fieldset>
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                        <h4 class="modal-title">Ajouter un utilisateur</h4>
                                    </div>

                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label for="inputUsernameCreate" class="col-lg-2 control-label">Nom d'utilisateur</label>

                                            <div class="col-lg-10">
                                                <input class="form-control" type="text" name="username" id="inputUsernameCreate" required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="inputPasswordCreate" class="col-lg-2 control-label">Mot de passe</label>

                                            <div class="col-lg-10">
                                                <input class="form-control" type="password" name="password" id="inputPasswordCreate" required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="inputUUIDCreate" class="col-lg-2 control-label">UUID Minecraft</label>

                                            <div class="col-lg-10">
                                                <input class="form-control" type="text" name="uuid" id="inputUUIDCreate" required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="inputPermissionCreate" class="col-lg-2 control-label">Permission</label>

                                            <div class="col-lg-10">
                                                <select class="form-control" name="permission" id="inputPermissionCreate" required>
                                                    <%
                                                        for (UserPermissionLevel permission : UserPermissionLevel.values())
                                                        {
                                                    %>
                                                    <option value="<%= permission.name() %>"><%= WordUtils.capitalize(permission.name().toLowerCase()) %></option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                        </div>

                                        <input type="hidden" name="action" value="create" required>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary">Créer</button>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>