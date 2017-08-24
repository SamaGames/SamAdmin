<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="net.samagames.samadmin.users.User" %>
<%@ page import="net.samagames.samadmin.SamAdmin" %>
<aside id="sidebar" class="sidebar sidebar-default open">
            <div class="sidebar-header header-cover" style="background-image: url(http://blueslime.fr/avatar.jpg);">
                <button type="button" class="sidebar-toggle">
                    <i class="icon-material-sidebar-arrow"></i>
                </button>

                <div class="sidebar-image">
                    <img src="<%= (prefix != null ? prefix : "") %>www/static/img/samagames_logo.png">
                </div>

                <div class="sidebar-brand">
                    <%= ((User) request.getSession().getAttribute("user-object")).getUsername() %>
                </div>
            </div>

            <ul class="nav sidebar-nav">
                <li>
                    <a href="<%= (prefix != null ? prefix : "") %>dashboard">
                        <i class="sidebar-icon mdi-content-send"></i>
                        Accueil
                    </a>
                </li>

                <li>
                    <a href="<%= (prefix != null ? prefix : "") %>faq">
                        <i class="sidebar-icon mdi-action-question-answer"></i>
                        Foire Aux Questions
                    </a>
                </li>

                <li>
                    <a href="<%= (prefix != null ? prefix : "") %>search">
                        <i class="sidebar-icon mdi-file-folder-shared"></i>
                        Profil de joueur
                    </a>
                </li>

                <li>
                    <a href="http://bugs.samagames.net" target="_blank">
                        <i class="sidebar-icon mdi-action-report-problem"></i>
                        Signaler un bug
                    </a>
                </li>

                <li class="divider"></li>

                <%
                    if (SamAdmin.getInstance().getUserManager().isAdmin(request.getSession()))
                    {
                %>

                <li><div class="sidebar-text">Administration</div></li>

                <li class="dropdown">
                    <a href="#" class="ripple-effect dropdown-toggle" data-toggle="dropdown">
                        <i class="sidebar-icon mdi-action-settings"></i>
                        Application
                        <b class="caret"></b>
                    </a>

                    <ul class="dropdown-menu">
                        <li><a href="<%= (prefix != null ? prefix : "") %>admin/users">Gestion des utilisateurs</a></li>
                        <li><a href="<%= (prefix != null ? prefix : "") %>admin/faq">Gestion des questions</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="ripple-effect dropdown-toggle" data-toggle="dropdown">
                        <i class="sidebar-icon mdi-action-settings"></i>
                        Serveur
                        <b class="caret"></b>
                    </a>

                    <ul class="dropdown-menu">
                        <li><a href="<%= (prefix != null ? prefix : "") %>admin/statistics">Statistiques</a></li>
                        <li><a href="<%= (prefix != null ? prefix : "") %>admin/messages">Gestion des messages</a></li>
                    </ul>
                </li>

                <li class="divider"></li>
                <%
                    }
                %>

                <li><a href="<%= (prefix != null ? prefix : "") %>password">Modifier son mot de passe</a></li>
                <li><a href="<%= (prefix != null ? prefix : "") %>login?logout">Se déconnecter</a></li>
            </ul>
        </aside>