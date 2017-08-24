<<%@ page import="java.util.Collection" %>
<%@ page import="net.samagames.samadmin.questions.Question" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <%@ include file="../head.jsp" %>
    </head>

    <body>
        <%@ include file="../sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Gestion des questions</h2>

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
                            <td>Question</td>
                            <td>Réponse</td>
                            <td style="width: 200px;">Actions</td>
                        </tr>
                    </thead>

                    <tbody>
                        <%
                            for (Question question : ((Collection<Question>) request.getAttribute("questions")))
                            {
                        %>
                        <tr>
                            <td><%= question.getQuestion() %></td>
                            <td><%= question.getAnswer() %></td>
                            <td>
                                <div class="btn-group btn-group-xs">
                                    <a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modalEdit<%= question.getID() %>">Editer</a>

                                    <div class="modal fade" id="modalEdit<%= question.getID() %>">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form class="form-horizontal" action="faq" method="post">
                                                    <fieldset>
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                            <h4 class="modal-title">Editer la question : <%= question.getQuestion() %></h4>
                                                        </div>

                                                        <div class="modal-body">
                                                            <div class="form-group">
                                                                <label for="inputQuestion" class="col-lg-2 control-label">Question</label>

                                                                <div class="col-lg-10">
                                                                    <textarea class="form-control" name="question" id="inputQuestion" required><%= question.getQuestion() %></textarea>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="inputAnswer" class="col-lg-2 control-label">Réponse</label>

                                                                <div class="col-lg-10">
                                                                    <textarea class="form-control" name="answer" id="inputAnswer" required><%= question.getAnswer() %></textarea>
                                                                </div>
                                                            </div>

                                                            <input type="hidden" name="action" value="edit" required>
                                                            <input type="hidden" name="question" value="<%= question.getID() %>" required>
                                                        </div>

                                                        <div class="modal-footer">
                                                            <button type="submit" class="btn btn-primary">Enregistrer</button>
                                                        </div>
                                                    </fieldset>
                                                </form>
                                            </div>
                                        </div>
                                    </div>

                                    <a class="btn btn-danger btn-xs" data-toggle="modal" data-target="#modalDelete<%= question.getID() %>">Supprimer</a>

                                    <div class="modal fade" id="modalDelete<%= question.getID() %>">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form class="form-horizontal" action="faq" method="post">
                                                    <fieldset>
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                            <h4 class="modal-title">Supprimer la question : <%= question.getQuestion() %></h4>
                                                        </div>

                                                        <div class="modal-body">
                                                            <input type="hidden" name="action" value="delete" required>
                                                            <input type="hidden" name="question" value="<%= question.getID() %>" required>
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

                <button class="btn btn-primary" data-toggle="modal" data-target="#modalCreate">Ajouter une question</button>

                <div class="modal fade" id="modalCreate">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form class="form-horizontal" action="faq" method="post">
                                <fieldset>
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                        <h4 class="modal-title">Ajouter une question</h4>
                                    </div>

                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label for="inputQuestionCreate" class="col-lg-2 control-label">Question</label>

                                            <div class="col-lg-10">
                                                <textarea class="form-control" name="question" id="inputQuestionCreate" required></textarea>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="inputAnswerCreate" class="col-lg-2 control-label">Réponse</label>

                                            <div class="col-lg-10">
                                                <textarea class="form-control" name="answer" id="inputAnswerCreate" required></textarea>
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