<%@ page import="org.apache.commons.lang3.text.WordUtils" %>
<%@ page import="net.samagames.samadmin.messages.Message" %>
<%@ page import="java.util.concurrent.TimeUnit" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <%@ include file="../head.jsp" %>
    </head>

    <body>
        <%@ include file="../sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Gestion des messages programmés</h2>

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
                            <td>Au nom de</td>
                            <td>Message</td>
                            <td>Période</td>
                            <td>Actions</td>
                        </tr>
                    </thead>

                    <tbody>
                        <%
                            for (Message message : (ArrayList<Message>) request.getAttribute("messages"))
                            {
                        %>
                        <tr>
                            <td><%= message.getFrom() %></td>
                            <td><%= message.getMessage() %></td>
                            <td><%= message.getDelay() + " " + WordUtils.capitalize(message.getTimeUnit().name().toLowerCase()) %></td>
                            <td>
                                <div class="btn-group btn-group-xs">
                                    <a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modalEdit<%= message.getID() %>">Editer</a>

                                    <div class="modal fade" id="modalEdit<%= message.getID() %>">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form class="form-horizontal" action="messages" method="post">
                                                    <fieldset>
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                            <h4 class="modal-title">Editer le message programmé : <%= message.getMessage() %></h4>
                                                        </div>

                                                        <div class="modal-body">
                                                            <div class="form-group">
                                                                <label for="inputFrom" class="col-lg-2 control-label">Au nom de</label>

                                                                <div class="col-lg-10">
                                                                    <input class="form-control" type="text" name="from" id="inputFrom" value="<%= message.getFrom() %>" required>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="inputMessage" class="col-lg-2 control-label">Message</label>

                                                                <div class="col-lg-10">
                                                                    <input class="form-control" type="text" name="message-text" id="inputMessage" value="<%= message.getMessage() %>" required>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="inputDelay" class="col-lg-2 control-label">Période</label>

                                                                <div class="col-lg-10">
                                                                    <input class="form-control" type="number" name="delay" id="inputDelay" value="<%= message.getDelay() %>" required>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="inputTimeUnit" class="col-lg-2 control-label">Unité de temps</label>

                                                                <div class="col-lg-10">
                                                                    <select class="form-control" name="time-unit" id="inputTimeUnit" required>
                                                                        <%
                                                                            for (TimeUnit timeUnit : TimeUnit.values())
                                                                            {
                                                                        %>
                                                                        <option value="<%= timeUnit.name() %>" <%= (message.getTimeUnit() == timeUnit ? "selected" : "")%>><%= WordUtils.capitalize(timeUnit.name().toLowerCase()) %></option>
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>

                                                            <input type="hidden" name="action" value="edit" required>
                                                            <input type="hidden" name="message" value="<%= message.getID() %>" required>
                                                        </div>

                                                        <div class="modal-footer">
                                                            <button type="submit" class="btn btn-primary">Enregistrer</button>
                                                        </div>
                                                    </fieldset>
                                                </form>
                                            </div>
                                        </div>
                                    </div>

                                    <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#modalDelete<%= message.getID() %>">Supprimer</a>

                                    <div class="modal fade" id="modalDelete<%= message.getID() %>">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form class="form-horizontal" action="messages" method="post">
                                                    <fieldset>
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                            <h4 class="modal-title">Supprimer le message programmé : <%= message.getMessage() %></h4>
                                                        </div>

                                                        <div class="modal-body">
                                                            <input type="hidden" name="action" value="delete" required>
                                                            <input type="hidden" name="message" value="<%= message.getID() %>" required>
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

                <button class="btn btn-primary" data-toggle="modal" data-target="#modalCreate">Ajouter un message programmé</button>

                <div class="modal fade" id="modalCreate">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form class="form-horizontal" action="messages" method="post">
                                <fieldset>
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                        <h4 class="modal-title">Ajouter un message programmé</h4>
                                    </div>

                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label for="inputFromCreate" class="col-lg-2 control-label">Au nom de</label>

                                            <div class="col-lg-10">
                                                <input class="form-control" type="text" name="from" id="inputFromCreate" required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="inputMessageCreate" class="col-lg-2 control-label">Message</label>

                                            <div class="col-lg-10">
                                                <input class="form-control" type="text" name="message-text" id="inputMessageCreate" required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="inputDelayCreate" class="col-lg-2 control-label">Période</label>

                                            <div class="col-lg-10">
                                                <input class="form-control" type="number" name="delay" id="inputDelayCreate" required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="inputTimeUnitCreate" class="col-lg-2 control-label">Unité de temps</label>

                                            <div class="col-lg-10">
                                                <select class="form-control" name="time-unit" id="inputTimeUnitCreate" required>
                                                    <%
                                                        for (TimeUnit timeUnit : TimeUnit.values())
                                                        {
                                                    %>
                                                    <option value="<%= timeUnit.name() %>"><%= WordUtils.capitalize(timeUnit.name().toLowerCase()) %></option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                        </div>

                                        <input type="hidden" name="action" value="create" required>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary">Programmer</button>
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